package com.example.loadbalancer.service;

import com.example.loadbalancer.model.Request;
import com.example.loadbalancer.model.Server;
import com.example.loadbalancer.model.ServerStatus;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimulationService {

    private final ExecutorService executorService;

    public SimulationService() {
        this.executorService = Executors.newFixedThreadPool(10);
    }

    public void processRequestAsync(Server server, Request request, Runnable onSuccess, Runnable onFailure) {
        if (server == null || request == null) {
            if (onFailure != null) {
                onFailure.run();
            }
            return;
        }

        executorService.submit(() -> {
            try {
                request.markProcessing();

                Thread.sleep(request.getProcessingTimeMs());

                // Simulate server failure during processing
                if (server.getStatus() != ServerStatus.UP) {
                    throw new RuntimeException("Server became unavailable during processing");
                }

                server.dequeueRequest();
                server.completeCurrentRequest(request);

                if (onSuccess != null) {
                    onSuccess.run();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                server.failCurrentRequest(request);
                if (onFailure != null) {
                    onFailure.run();
                }
            } catch (RuntimeException e) {
                server.failCurrentRequest(request);
                if (onFailure != null) {
                    onFailure.run();
                }
            }
        });
    }

    public void shutdown() {
        executorService.shutdown();
    }
}