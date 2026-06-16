package com.example.loadbalancer.service;

import com.example.loadbalancer.dto.RequestDto;
import com.example.loadbalancer.dto.ServerRequestDto;
import com.example.loadbalancer.model.Request;
import com.example.loadbalancer.model.Server;
import com.example.loadbalancer.model.ServerStatus;
import com.example.loadbalancer.strategy.LoadBalancingStrategy;
import com.example.loadbalancer.strategy.LeastConnectionsStrategy;
import com.example.loadbalancer.strategy.RoundRobinStrategy;
import com.example.loadbalancer.strategy.WeightedRoutingStrategy;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LoadBalancerService {

    private final LoadBalancer loadBalancer;
    private final Map<String, Request> requestStore;
    private final HealthCheckService healthCheckService;

    public LoadBalancerService() {
        this.loadBalancer = new LoadBalancer(new RoundRobinStrategy());
        this.requestStore = new ConcurrentHashMap<>();
        this.healthCheckService = new HealthCheckService();
    }

    public Server addServer(ServerRequestDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Server request cannot be null");
        }

        if (dto.getServerId() == null || dto.getServerId().trim().isEmpty()) {
            throw new IllegalArgumentException("Server ID cannot be empty");
        }

        if (dto.getWeight() <= 0) {
            throw new IllegalArgumentException("Server weight must be greater than 0");
        }

        Server server = new Server(
                dto.getServerId(),
                dto.getWeight(),
                new LinkedList<>(),
                0,
                0,
                ServerStatus.UP
        );

        loadBalancer.addServer(server);
        return server;
    }

    public void removeServer(String serverId) {
        if (serverId == null || serverId.trim().isEmpty()) {
            throw new IllegalArgumentException("Server ID cannot be empty");
        }

        loadBalancer.removeServer(serverId);
    }

    public List<Server> getServers() {
        return loadBalancer.getServers();
    }

    public Request routeRequest(RequestDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }

        if (dto.getRequestId() == null || dto.getRequestId().trim().isEmpty()) {
            throw new IllegalArgumentException("Request ID cannot be empty");
        }

        if (dto.getClientIp() == null || dto.getClientIp().trim().isEmpty()) {
            throw new IllegalArgumentException("Client IP cannot be empty");
        }

        if (dto.getPayload() == null) {
            throw new IllegalArgumentException("Payload cannot be null");
        }

        if (dto.getProcessingTimeMs() < 0) {
            throw new IllegalArgumentException("Processing time cannot be negative");
        }

        Request request = new Request(
                dto.getRequestId(),
                dto.getClientIp(),
                dto.getPayload(),
                dto.getProcessingTimeMs()
        );

        requestStore.put(request.getRequestId(), request);

        Server selectedServer = loadBalancer.submitRequest(request);

        if (selectedServer == null) {
            request.markFailed();
        }

        return request;
    }

    public void setStrategy(String strategyName) {
        if (strategyName == null || strategyName.trim().isEmpty()) {
            throw new IllegalArgumentException("Strategy name cannot be empty");
        }

        LoadBalancingStrategy strategy;

        switch (strategyName.trim().toUpperCase()) {
            case "ROUND_ROBIN":
                strategy = new RoundRobinStrategy();
                break;
            case "LEAST_CONNECTIONS":
                strategy = new LeastConnectionsStrategy();
                break;
            case "WEIGHTED":
                strategy = new WeightedRoutingStrategy();
                break;
            default:
                throw new IllegalArgumentException("Unsupported strategy: " + strategyName);
        }

        loadBalancer.setStrategy(strategy);
    }

    public LoadBalancer getLoadBalancer() {
        return loadBalancer;
    }

    public Request getRequestById(String requestId) {
        return requestStore.get(requestId);
    }

    public List<Request> getAllRequests() {
        return List.copyOf(requestStore.values());
    }

    public Server updateServerStatus(String serverId, ServerStatus status) {
        if (serverId == null || status == null) {
            throw new IllegalArgumentException("Server ID and status cannot be null");
        }

        Server server = loadBalancer.findServerById(serverId);

        if (server == null) {
            throw new IllegalArgumentException("Server not found: " + serverId);
        }

        server.setStatus(status);
        return server;
    }
}