package com.example.loadbalancer.strategy;

import com.example.loadbalancer.model.Request;
import com.example.loadbalancer.model.Server;
import com.example.loadbalancer.model.ServerStatus;

import java.util.ArrayList;
import java.util.List;

public class WeightedRoutingStrategy implements LoadBalancingStrategy {

    private List<Server> weightedSchedule;
    private int currentIndex;

    public WeightedRoutingStrategy() {
        this.weightedSchedule = new ArrayList<>();
        this.currentIndex = 0;
    }

    @Override
    public synchronized Server selectServer(List<Server> servers, Request request) {
        if (servers == null || servers.isEmpty()) {
            return null;
        }

        rebuildSchedule(servers);

        if (weightedSchedule.isEmpty()) {
            return null;
        }

        Server selected = weightedSchedule.get(currentIndex);
        currentIndex = (currentIndex + 1) % weightedSchedule.size();

        return selected;
    }

    private void rebuildSchedule(List<Server> servers) {
        weightedSchedule.clear();

        for (Server server : servers) {
            if (server == null || server.getStatus() != ServerStatus.UP) {
                continue;
            }

            int weight = server.getWeight();

            for (int i = 0; i < weight; i++) {
                weightedSchedule.add(server);
            }
        }

        if (currentIndex >= weightedSchedule.size()) {
            currentIndex = 0;
        }
    }
}