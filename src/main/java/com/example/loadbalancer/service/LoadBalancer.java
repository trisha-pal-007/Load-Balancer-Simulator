package com.example.loadbalancer.service;

import com.example.loadbalancer.model.Request;
import com.example.loadbalancer.model.Server;
import com.example.loadbalancer.model.ServerStatus;
import com.example.loadbalancer.strategy.LoadBalancingStrategy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LoadBalancer {

    private List<Server> servers;
    private LoadBalancingStrategy strategy;
    private SimulationService simulationService;
    private MetricsService metricsService;

    public LoadBalancer(LoadBalancingStrategy strategy) {
        this.servers = new ArrayList<>();
        this.strategy = strategy;
        this.simulationService = new SimulationService();
        this.metricsService = new MetricsService();
    }

    public void addServer(Server server) {
        if (server != null) {
            servers.add(server);
        }
    }

    public void removeServer(String serverId) {
        if (serverId == null) {
            return;
        }

        Iterator<Server> iterator = servers.iterator();
        while (iterator.hasNext()) {
            Server server = iterator.next();
            if (server.getServerId().equals(serverId)) {
                iterator.remove();
                break;
            }
        }
    }

    public void setStrategy(LoadBalancingStrategy strategy) {
        this.strategy = strategy;
    }

    public LoadBalancingStrategy getStrategy() {
        return strategy;
    }

    public List<Server> getServers() {
        return servers;
    }

    public MetricsService getMetricsService() {
        return metricsService;
    }

    public Server findServerById(String serverId) {
        if (serverId == null) {
            return null;
        }

        for (Server server : servers) {
            if (server.getServerId().equals(serverId)) {
                return server;
            }
        }
        return null;
    }

    public Server routeRequest(Request request) {
        if (request == null || strategy == null || servers.isEmpty()) {
            return null;
        }

        metricsService.recordRequestReceived();

        Server selectedServer = strategy.selectServer(servers, request);

        if (selectedServer == null) {
            metricsService.recordRequestFailed(request);
            return null;
        }

        selectedServer.enqueueRequest(request);
        request.markQueued();
        request.setAssignedServerId(selectedServer.getServerId());

        metricsService.recordRequestAssigned(selectedServer);

        return selectedServer;
    }

    public Server submitRequest(Request request) {
        return submitRequestWithRetry(request, 3);
    }

    public Server submitRequestWithRetry(Request request, int maxAttempts) {
        if (request == null) {
            return null;
        }

        int attempts = 0;
        Server lastSelectedServer = null;

        while (attempts < maxAttempts) {
            Server selectedServer = strategy.selectServer(servers, request);

            if (selectedServer == null) {
                break;
            }

            if (selectedServer.getStatus() != ServerStatus.UP) {
                attempts++;
                continue;
            }

            selectedServer.enqueueRequest(request);
            request.markQueued();
            request.setAssignedServerId(selectedServer.getServerId());
            metricsService.recordRequestAssigned(selectedServer);

            final boolean[] success = {false};

            simulationService.processRequestAsync(
                    selectedServer,
                    request,
                    () -> {
                        metricsService.recordStrategyUsage(strategy.getClass().getSimpleName());
                        metricsService.recordRequestCompleted(request);
                        success[0] = true;
                    },
                    () -> {
                        metricsService.recordRequestFailed(request);
                    }
            );

            lastSelectedServer = selectedServer;
            break;
        }

        if (lastSelectedServer == null) {
            request.markFailed();
            metricsService.recordRequestFailed(request);
        }

        return lastSelectedServer;
    }

    public void processRequest(Request request) {
        submitRequest(request);
    }

    public void shutdown() {
        simulationService.shutdown();
    }
}