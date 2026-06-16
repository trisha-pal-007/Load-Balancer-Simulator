package com.example.loadbalancer.dto;

public class RequestDto {
    private String requestId;
    private String clientIp;
    private String payload;
    private int processingTimeMs;

    public RequestDto() {
    }

    public RequestDto(String requestId, String clientIp, String payload, int processingTimeMs) {
        this.requestId = requestId;
        this.clientIp = clientIp;
        this.payload = payload;
        this.processingTimeMs = processingTimeMs;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public int getProcessingTimeMs() {
        return processingTimeMs;
    }

    public void setProcessingTimeMs(int processingTimeMs) {
        this.processingTimeMs = processingTimeMs;
    }
}