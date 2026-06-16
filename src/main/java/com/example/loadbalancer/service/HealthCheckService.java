package com.example.loadbalancer.service;

import com.example.loadbalancer.model.Server;
import com.example.loadbalancer.model.ServerStatus;

public class HealthCheckService {

    public boolean isHealthy(Server server) {
        return server != null && server.getStatus() == ServerStatus.UP;
    }

    public void markUp(Server server) {
        if (server != null) {
            server.setStatus(ServerStatus.UP);
        }
    }

    public void markDown(Server server) {
        if (server != null) {
            server.setStatus(ServerStatus.DOWN);
        }
    }

    public void markDegraded(Server server) {
        if (server != null) {
            server.setStatus(ServerStatus.DEGRADED);
        }
    }
}