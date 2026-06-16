package com.example.loadbalancer;

import com.example.loadbalancer.model.Request;
import com.example.loadbalancer.model.Server;
import com.example.loadbalancer.model.ServerStatus;
import com.example.loadbalancer.service.LoadBalancer;
import com.example.loadbalancer.strategy.LeastConnectionsStrategy;

import java.util.LinkedList;

public class TestLeastConnections {
    public static void main(String[] args) {
        Server s1 = new Server("S1", 1, new LinkedList<>(), 5, 0, ServerStatus.UP);
        Server s2 = new Server("S2", 1, new LinkedList<>(), 2, 0, ServerStatus.UP);
        Server s3 = new Server("S3", 1, new LinkedList<>(), 3, 0, ServerStatus.UP);

        LoadBalancer loadBalancer = new LoadBalancer(new LeastConnectionsStrategy());

        loadBalancer.addServer(s1);
        loadBalancer.addServer(s2);
        loadBalancer.addServer(s3);

        Request request = new Request("R1", "10.0.0.1", "login", 500);

        Server selected = loadBalancer.submitRequest(request);
        // Server selected = loadBalancer.routeRequest(request);

        System.out.println("Selected server: " + (selected != null ? selected.getServerId() : "null"));
        System.out.println("Request status: " + request.getStatus());
        System.out.println("S1 active connections: " + s1.getActiveConnections());
        System.out.println("S2 active connections: " + s2.getActiveConnections());
        System.out.println("S3 active connections: " + s3.getActiveConnections());
    }
}