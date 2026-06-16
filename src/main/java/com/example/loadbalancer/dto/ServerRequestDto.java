package com.example.loadbalancer.dto;

public class ServerRequestDto {
    private String serverId;
    private int weight;

    public ServerRequestDto() {
    }

    public ServerRequestDto(String serverId, int weight) {
        this.serverId = serverId;
        this.weight = weight;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}