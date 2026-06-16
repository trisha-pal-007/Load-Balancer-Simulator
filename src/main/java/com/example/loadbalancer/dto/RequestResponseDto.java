package com.example.loadbalancer.dto;

public class RequestResponseDto {
    private String requestId;
    private String selectedServer;
    private String strategy;
    private String status;
    private String queuedAt;
    private String processingAt;
    private String completedAt;
    private String failedAt;

    public RequestResponseDto() {
    }

    public RequestResponseDto(String requestId, String selectedServer, String strategy, String status,
                              String queuedAt, String processingAt, String completedAt, String failedAt) {
        this.requestId = requestId;
        this.selectedServer = selectedServer;
        this.strategy = strategy;
        this.status = status;
        this.queuedAt = queuedAt;
        this.processingAt = processingAt;
        this.completedAt = completedAt;
        this.failedAt = failedAt;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getSelectedServer() {
        return selectedServer;
    }

    public String getStrategy() {
        return strategy;
    }

    public String getStatus() {
        return status;
    }

    public String getQueuedAt() {
        return queuedAt;
    }

    public String getProcessingAt() {
        return processingAt;
    }

    public String getCompletedAt() {
        return completedAt;
    }

    public String getFailedAt() {
        return failedAt;
    }
}