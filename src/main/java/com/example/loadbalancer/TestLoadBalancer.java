package com.example.loadbalancer;

import com.example.loadbalancer.model.Request;
import com.example.loadbalancer.model.Server;
import com.example.loadbalancer.model.ServerStatus;
import com.example.loadbalancer.service.LoadBalancer;
import com.example.loadbalancer.strategy.RoundRobinStrategy;

import java.util.LinkedList;

public class TestLoadBalancer {
    public static void main(String[] args) {
        Server s1 = new Server("S1", 1, new LinkedList<>(), 0, 0, ServerStatus.UP);
        Server s2 = new Server("S2", 1, new LinkedList<>(), 0, 0, ServerStatus.UP);
        Server s3 = new Server("S3", 1, new LinkedList<>(), 0, 0, ServerStatus.UP);

        LoadBalancer loadBalancer = new LoadBalancer(new RoundRobinStrategy());

        loadBalancer.addServer(s1);
        loadBalancer.addServer(s2);
        loadBalancer.addServer(s3);

        Request r1 = new Request("R1", "10.0.0.1", "login", 500);
        Request r2 = new Request("R2", "10.0.0.2", "search", 300);
        Request r3 = new Request("R3", "10.0.0.3", "payment", 700);
        Request r4 = new Request("R4", "10.0.0.4", "cart", 200);

        System.out.println("R1 -> " + loadBalancer.routeRequest(r1).getServerId());
        System.out.println("R2 -> " + loadBalancer.routeRequest(r2).getServerId());
        System.out.println("R3 -> " + loadBalancer.routeRequest(r3).getServerId());
        System.out.println("R4 -> " + loadBalancer.routeRequest(r4).getServerId());

        System.out.println("S1 queue size: " + s1.getQueueSize());
        System.out.println("S2 queue size: " + s2.getQueueSize());
        System.out.println("S3 queue size: " + s3.getQueueSize());

        System.out.println("S1 active connections: " + s1.getActiveConnections());
        System.out.println("S2 active connections: " + s2.getActiveConnections());
        System.out.println("S3 active connections: " + s3.getActiveConnections());

        System.out.println("R1 status: " + r1.getStatus());
        System.out.println("R2 status: " + r2.getStatus());
        System.out.println("R3 status: " + r3.getStatus());
        System.out.println("R4 status: " + r4.getStatus());

        System.out.println("S1 processed count: " + s1.getProcessedCount());
        System.out.println("S2 processed count: " + s2.getProcessedCount());
        System.out.println("S3 processed count: " + s3.getProcessedCount());
    }
}