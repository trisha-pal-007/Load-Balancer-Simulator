package com.example.loadbalancer.service;

import com.example.loadbalancer.model.Server;
import com.example.loadbalancer.model.Request;

import java.util.HashMap;
import java.util.Map;

public class MetricsService {

    private int totalRequests;
    private int completedRequests;
    private int failedRequests;
    private Map<String, Integer> requestsPerServer;
    private Map<String, Integer> strategyUsage;

    public MetricsService() {
        this.totalRequests = 0;
        this.completedRequests = 0;
        this.failedRequests = 0;
        this.requestsPerServer = new HashMap<>();
        this.strategyUsage = new HashMap<>();
    }

    private int retryCount;

    public synchronized void recordRetry() {
        retryCount++;
    }

    public synchronized int getRetryCount() {
        return retryCount;
    }

    public synchronized void recordRequestReceived() {
        totalRequests++;
    }

    public synchronized void recordRequestAssigned(Server server) {
        if (server == null) {
            return;
        }

        String serverId = server.getServerId();
        requestsPerServer.put(serverId, requestsPerServer.getOrDefault(serverId, 0) + 1);
    }

    public synchronized void recordRequestCompleted(Request request) {
        if (request == null) {
            return;
        }

        completedRequests++;
    }

    public synchronized void recordRequestFailed(Request request) {
        if (request == null) {
            return;
        }

        failedRequests++;
    }

    public synchronized void recordStrategyUsage(String strategyName) {
        if (strategyName == null) {
            return;
        }

        strategyUsage.put(strategyName, strategyUsage.getOrDefault(strategyName, 0) + 1);
    }

    public synchronized int getTotalRequests() {
        return totalRequests;
    }

    public synchronized int getCompletedRequests() {
        return completedRequests;
    }

    public synchronized int getFailedRequests() {
        return failedRequests;
    }

    public synchronized Map<String, Integer> getRequestsPerServer() {
        return requestsPerServer;
    }

    public synchronized Map<String, Integer> getStrategyUsage() {
        return strategyUsage;
    }

    public synchronized String getMetricsSummary() {
        StringBuilder sb = new StringBuilder();

        sb.append("Total Requests: ").append(totalRequests).append("\n");
        sb.append("Completed Requests: ").append(completedRequests).append("\n");
        sb.append("Failed Requests: ").append(failedRequests).append("\n");

        sb.append("Requests Per Server:\n");
        for (Map.Entry<String, Integer> entry : requestsPerServer.entrySet()) {
            sb.append(" - ").append(entry.getKey())
                    .append(": ").append(entry.getValue())
                    .append("\n");
        }

        sb.append("Strategy Usage:\n");
        for (Map.Entry<String, Integer> entry : strategyUsage.entrySet()) {
            sb.append(" - ").append(entry.getKey())
                    .append(": ").append(entry.getValue())
                    .append("\n");
        }

        return sb.toString();
    }
}