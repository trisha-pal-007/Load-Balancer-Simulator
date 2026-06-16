package com.example.loadbalancer.controller;

import com.example.loadbalancer.dto.RequestDto;
import com.example.loadbalancer.dto.RequestResponseDto;
import com.example.loadbalancer.dto.ServerRequestDto;
import com.example.loadbalancer.dto.ServerResponseDto;
import com.example.loadbalancer.dto.StrategyRequestDto;
import com.example.loadbalancer.model.Request;
import com.example.loadbalancer.model.Server;
import com.example.loadbalancer.model.ServerStatus;
import com.example.loadbalancer.service.LoadBalancerService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class LoadBalancerController {

    private final LoadBalancerService loadBalancerService;

    public LoadBalancerController(LoadBalancerService loadBalancerService) {
        this.loadBalancerService = loadBalancerService;
    }

    @PostMapping("/servers")
    public ResponseEntity<ServerResponseDto> addServer(@RequestBody ServerRequestDto dto) {
        Server server = loadBalancerService.addServer(dto);

        ServerResponseDto response = new ServerResponseDto(
                server.getServerId(),
                server.getWeight(),
                server.getActiveConnections(),
                server.getQueueSize(),
                server.getProcessedCount(),
                server.getStatus().name()
        );

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/servers")
    public ResponseEntity<List<ServerResponseDto>> getServers() {
        List<ServerResponseDto> response = loadBalancerService.getServers()
                .stream()
                .map(server -> new ServerResponseDto(
                        server.getServerId(),
                        server.getWeight(),
                        server.getActiveConnections(),
                        server.getQueueSize(),
                        server.getProcessedCount(),
                        server.getStatus().name()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/servers/{serverId}")
    public ResponseEntity<String> removeServer(@PathVariable String serverId) {
        loadBalancerService.removeServer(serverId);
        return ResponseEntity.ok("Server removed successfully: " + serverId);
    }

    @PostMapping("/strategy")
    public ResponseEntity<String> setStrategy(@RequestBody StrategyRequestDto dto) {
        loadBalancerService.setStrategy(dto.getStrategy());
        return ResponseEntity.ok("Strategy updated to: " + dto.getStrategy());
    }

    @PostMapping("/requests")
    public ResponseEntity<RequestResponseDto> routeRequest(@RequestBody RequestDto dto) {
        Request request = loadBalancerService.routeRequest(dto);

        if (request == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new RequestResponseDto(
                            dto != null ? dto.getRequestId() : null,
                            null,
                            null,
                            "No available server",
                            null,
                            null,
                            null,
                            null
                    ));
        }

        String strategyName = loadBalancerService.getLoadBalancer().getStrategy() != null
                ? loadBalancerService.getLoadBalancer().getStrategy().getClass().getSimpleName()
                : "NONE";

        RequestResponseDto response = new RequestResponseDto(
                request.getRequestId(),
                request.getAssignedServerId(),
                strategyName,
                request.getStatus().name(),
                request.getQueuedAt() != null ? request.getQueuedAt().toString() : null,
                request.getProcessingAt() != null ? request.getProcessingAt().toString() : null,
                request.getCompletedAt() != null ? request.getCompletedAt().toString() : null,
                request.getFailedAt() != null ? request.getFailedAt().toString() : null
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/stats")
    public ResponseEntity<String> getStats() {
        StringBuilder stats = new StringBuilder();

        if (loadBalancerService.getServers().isEmpty()) {
            return ResponseEntity.ok("No servers available.");
        }

        loadBalancerService.getServers().forEach(server -> {
            stats.append("Server ID: ").append(server.getServerId())
                    .append(", Weight: ").append(server.getWeight())
                    .append(", Active Connections: ").append(server.getActiveConnections())
                    .append(", Queue Size: ").append(server.getQueueSize())
                    .append(", Processed Count: ").append(server.getProcessedCount())
                    .append(", Status: ").append(server.getStatus())
                    .append("\n");
        });

        return ResponseEntity.ok(stats.toString());
    }

    @GetMapping("/metrics")
    public ResponseEntity<String> getMetrics() {
        return ResponseEntity.ok(
                loadBalancerService.getLoadBalancer()
                        .getMetricsService()
                        .getMetricsSummary()
        );
    }

    @GetMapping("/requests/{requestId}")
    public ResponseEntity<RequestResponseDto> getRequestById(@PathVariable String requestId) {
        Request request = loadBalancerService.getRequestById(requestId);

        if (request == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        String strategyName = loadBalancerService.getLoadBalancer().getStrategy() != null
                ? loadBalancerService.getLoadBalancer().getStrategy().getClass().getSimpleName()
                : "NONE";

        RequestResponseDto response = new RequestResponseDto(
                request.getRequestId(),
                request.getAssignedServerId(),
                strategyName,
                request.getStatus().name(),
                request.getQueuedAt() != null ? request.getQueuedAt().toString() : null,
                request.getProcessingAt() != null ? request.getProcessingAt().toString() : null,
                request.getCompletedAt() != null ? request.getCompletedAt().toString() : null,
                request.getFailedAt() != null ? request.getFailedAt().toString() : null
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/requests")
    public ResponseEntity<List<RequestResponseDto>> getAllRequests() {
        String strategyName = loadBalancerService.getLoadBalancer().getStrategy() != null
                ? loadBalancerService.getLoadBalancer().getStrategy().getClass().getSimpleName()
                : "NONE";

        List<RequestResponseDto> response = loadBalancerService.getAllRequests()
                .stream()
                .map(request -> new RequestResponseDto(
                        request.getRequestId(),
                        request.getAssignedServerId(),
                        strategyName,
                        request.getStatus().name(),
                        request.getQueuedAt() != null ? request.getQueuedAt().toString() : null,
                        request.getProcessingAt() != null ? request.getProcessingAt().toString() : null,
                        request.getCompletedAt() != null ? request.getCompletedAt().toString() : null,
                        request.getFailedAt() != null ? request.getFailedAt().toString() : null
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }
    @PutMapping("/servers/{serverId}/status")
    public ResponseEntity<ServerResponseDto> updateServerStatus(
            @PathVariable String serverId,
            @RequestParam String status) {

        try {
            Server server = loadBalancerService.updateServerStatus(serverId, ServerStatus.valueOf(status.toUpperCase()));

            ServerResponseDto response = new ServerResponseDto(
                    server.getServerId(),
                    server.getWeight(),
                    server.getActiveConnections(),
                    server.getQueueSize(),
                    server.getProcessedCount(),
                    server.getStatus().name()
            );

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

}