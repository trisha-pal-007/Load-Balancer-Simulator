package com.example.loadbalancer.strategy;

import com.example.loadbalancer.model.Request;
import com.example.loadbalancer.model.Server;

import java.util.List;

public interface LoadBalancingStrategy {

    Server selectServer(List<Server> servers, Request request);

}
