package com.example.loadbalancer.dto;

public class StrategyRequestDto {
    private String strategy;

    public StrategyRequestDto() {
    }

    public StrategyRequestDto(String strategy) {
        this.strategy = strategy;
    }

    public String getStrategy() {
        return strategy;
    }

    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }
}