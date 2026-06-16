package com.example.loadbalancer.model;

import java.util.LinkedList;
import java.util.Queue;

public class Server {
    private String serverId;
    private int weight;
    private Queue<Request> requestQueue;
    private int activeConnections;
    private long processedCount;
    private ServerStatus status;

    public Server(String serverId, int weight, Queue<Request> requestQueue, int activeConnections, long processedCount, ServerStatus status) {
        this.serverId = serverId;
        this.weight = weight;
        this.requestQueue = (requestQueue != null) ? requestQueue : new LinkedList<>();
        this.activeConnections = activeConnections;
        this.processedCount = processedCount;
        this.status = (status != null) ? status : ServerStatus.UP;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public Queue<Request> getRequestQueue() {
        return requestQueue;
    }

    public void setRequestQueue(Queue<Request> requestQueue) {
        this.requestQueue = requestQueue;
    }

    public int getActiveConnections() {
        return activeConnections;
    }

    public void setActiveConnections(int activeConnections) {
        this.activeConnections = activeConnections;
    }

    public long getProcessedCount() {
        return processedCount;
    }

    public void setProcessedCount(long processedCount) {
        this.processedCount = processedCount;
    }

    public ServerStatus getStatus() {
        return status;
    }

    public void setStatus(ServerStatus status) {
        this.status = status;
    }


    public void enqueueRequest(Request request) {
        this.requestQueue.add(request);
        this.activeConnections++;
        request.markQueued();
        request.setAssignedServerId(this.serverId);
    }

    public Request dequeueRequest() {
        return this.requestQueue.poll();
    }


    public void completeCurrentRequest(Request request) {
        if (request != null) {
            this.activeConnections--;
            this.processedCount++;
            request.markCompleted();
        }
    }

    public void failCurrentRequest(Request request) {
        if (request != null) {
            this.activeConnections--;
            request.markFailed();
        }
    }

    public int getQueueSize() {
        return this.requestQueue.size();
    }

    public boolean isHealthy() {
        return this.status == ServerStatus.UP;
    }
}