package com.example.loadbalancer.strategy;

import com.example.loadbalancer.datastructure.CircularLinkedList;
import com.example.loadbalancer.model.Request;
import com.example.loadbalancer.model.Server;
import com.example.loadbalancer.model.ServerStatus;

import java.util.List;

public class RoundRobinStrategy implements LoadBalancingStrategy {

    private CircularLinkedList<Server> serverRing;
    private int lastServerCount = -1;

    public RoundRobinStrategy() {
        this.serverRing = new CircularLinkedList<>();
    }

    @Override
    public synchronized Server selectServer(List<Server> servers, Request request) {
        if (servers == null || servers.isEmpty()) {
            return null;
        }

        if (serverRing.isEmpty() || servers.size() != lastServerCount) {
            rebuildRing(servers);
            lastServerCount = servers.size();
        }

        if (serverRing.isEmpty()) {
            return null;
        }

        int attempts = 0;
        int maxAttempts = serverRing.size();

        while (attempts < maxAttempts) {
            Server selected = serverRing.getCurrent();

            if (selected != null && selected.getStatus() == ServerStatus.UP) {
                serverRing.moveNext();
                return selected;
            }

            serverRing.moveNext();
            attempts++;
        }

        return null;
    }

    private void rebuildRing(List<Server> servers) {
        serverRing = new CircularLinkedList<>();

        for (Server server : servers) {
            if (server != null && server.getStatus() == ServerStatus.UP) {
                serverRing.add(server);
            }
        }

        serverRing.reset();
    }
}