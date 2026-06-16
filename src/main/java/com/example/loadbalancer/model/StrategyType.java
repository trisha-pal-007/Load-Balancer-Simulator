package com.example.loadbalancer.model;

public enum StrategyType {
    ROUND_ROBIN,
    LEAST_CONNECTIONS,
    WEIGHTED,
    HASH_BASED
}
