package com.example.loadbalancer.strategy;

import com.example.loadbalancer.model.Request;
import com.example.loadbalancer.model.Server;
import com.example.loadbalancer.model.ServerStatus;

import java.util.List;

public class LeastConnectionsStrategy implements LoadBalancingStrategy {

    @Override
    public Server selectServer(List<Server> servers, Request request) {
        if (servers == null || servers.isEmpty()) {
            return null;
        }

        Server selectedServer = null;

        for (Server server : servers) {
            if (server == null || server.getStatus() != ServerStatus.UP) {
                continue;
            }

            if (selectedServer == null) {
                selectedServer = server;
            } else {
                int currentConnections = server.getActiveConnections();
                int bestConnections = selectedServer.getActiveConnections();

                if (currentConnections < bestConnections) {
                    selectedServer = server;
                } else if (currentConnections == bestConnections) {
                    if (server.getQueueSize() < selectedServer.getQueueSize()) {
                        selectedServer = server;
                    }
                }
            }
        }

        return selectedServer;
    }
}