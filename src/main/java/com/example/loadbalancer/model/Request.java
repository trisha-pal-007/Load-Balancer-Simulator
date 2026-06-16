package com.example.loadbalancer.model;

public class Request {
    private String requestId;
    private String clientIp;
    private String payload;
    private long timestamp;
    private RequestStatus status;
    private int processingTimeMs;
    private String assignedServerId;
    private int retryCount;

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    private Long queuedAt;
    private Long processingAt;
    private Long completedAt;
    private Long failedAt;

    public Request(String requestId, String clientIp, String payload, int processingTimeMs) {
        this.requestId = requestId;
        this.clientIp = clientIp;
        this.payload = payload;
        this.timestamp = System.currentTimeMillis();
        this.status = RequestStatus.CREATED;
        this.processingTimeMs = processingTimeMs;
        this.assignedServerId = null;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getClientIp() {
        return clientIp;
    }

    public String getPayload() {
        return payload;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public int getProcessingTimeMs() {
        return processingTimeMs;
    }

    public String getAssignedServerId() {
        return assignedServerId;
    }

    public Long getQueuedAt() {
        return queuedAt;
    }

    public Long getProcessingAt() {
        return processingAt;
    }

    public Long getCompletedAt() {
        return completedAt;
    }

    public Long getFailedAt() {
        return failedAt;
    }

    public void setAssignedServerId(String assignedServerId) {
        this.assignedServerId = assignedServerId;
    }

    public void markQueued() {
        this.status = RequestStatus.QUEUED;
        this.queuedAt = System.currentTimeMillis();
    }

    public void markProcessing() {
        this.status = RequestStatus.PROCESSING;
        this.processingAt = System.currentTimeMillis();
    }

    public void markCompleted() {
        this.status = RequestStatus.COMPLETED;
        this.completedAt = System.currentTimeMillis();
    }

    public void markFailed() {
        this.status = RequestStatus.FAILED;
        this.failedAt = System.currentTimeMillis();
    }


}