package com.example.loadbalancer.dto;

public class ServerResponseDto {
    private String serverId;
    private int weight;
    private int activeConnections;
    private int queueSize;
    private long processedCount;
    private String status;

    public ServerResponseDto() {
    }

    public ServerResponseDto(String serverId, int weight, int activeConnections, int queueSize, long processedCount, String status) {
        this.serverId = serverId;
        this.weight = weight;
        this.activeConnections = activeConnections;
        this.queueSize = queueSize;
        this.processedCount = processedCount;
        this.status = status;
    }

    public String getServerId() {
        return serverId;
    }

    public int getWeight() {
        return weight;
    }

    public int getActiveConnections() {
        return activeConnections;
    }

    public int getQueueSize() {
        return queueSize;
    }

    public long getProcessedCount() {
        return processedCount;
    }

    public String getStatus() {
        return status;
    }
}