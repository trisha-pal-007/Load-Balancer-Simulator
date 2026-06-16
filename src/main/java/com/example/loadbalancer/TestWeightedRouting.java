package com.example.loadbalancer;

import com.example.loadbalancer.model.Request;
import com.example.loadbalancer.model.Server;
import com.example.loadbalancer.model.ServerStatus;
import com.example.loadbalancer.service.LoadBalancer;
import com.example.loadbalancer.strategy.WeightedRoutingStrategy;

import java.util.LinkedList;

public class TestWeightedRouting {
    public static void main(String[] args) {
        Server s1 = new Server("S1", 1, new LinkedList<>(), 0, 0, ServerStatus.UP);
        Server s2 = new Server("S2", 2, new LinkedList<>(), 0, 0, ServerStatus.UP);
        Server s3 = new Server("S3", 3, new LinkedList<>(), 0, 0, ServerStatus.UP);

        LoadBalancer loadBalancer = new LoadBalancer(new WeightedRoutingStrategy());

        loadBalancer.addServer(s1);
        loadBalancer.addServer(s2);
        loadBalancer.addServer(s3);

        for (int i = 1; i <= 6; i++) {
            Request request = new Request("R" + i, "10.0.0." + i, "payload-" + i, 100);
            //Server selected = loadBalancer.routeRequest(request);
            Server selected = loadBalancer.submitRequest(request);

            System.out.println("R" + i + " -> " + (selected != null ? selected.getServerId() : "null"));
        }

        System.out.println("S1 processed: " + s1.getProcessedCount());
        System.out.println("S2 processed: " + s2.getProcessedCount());
        System.out.println("S3 processed: " + s3.getProcessedCount());
    }
}