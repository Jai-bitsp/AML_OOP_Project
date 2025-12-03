package com.oops.aml.stream;

import com.oops.aml.engine.DynamicGraph;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TransactionStreamSimulator {
    private final DynamicGraph graph;
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private final Random random = new Random();
    private final List<String> accounts = new ArrayList<>();

    private volatile boolean running = false;

    public TransactionStreamSimulator(DynamicGraph graph, int accountCount) {
        this.graph = graph;
        for (int i = 0; i < accountCount; i++) {
            accounts.add("A" + (1000 + i));
        }
    }

    public void start(long periodMs) {
        if (running) return;
        running = true;
        scheduler.scheduleAtFixedRate(this::tick, 0, periodMs, TimeUnit.MILLISECONDS);
    }

    public void stop() {
        running = false;
        scheduler.shutdownNow();
    }

    private void tick() {
        if (!running) return;
        String from = pickAccount();
        String to = pickAccountDifferent(from);
        double amount = 100 + random.nextDouble() * 4900; // 100-5000
        graph.addTransaction(from, to, amount);

        // Occasionally inject ring pattern A->B->C->A
        if (random.nextDouble() < 0.03) {
            String a = pickAccount();
            String b = pickAccountDifferent(a);
            String c = pickAccountDifferent(b);
            double amt = 500 + random.nextDouble() * 3000;
            graph.addTransaction(a, b, amt);
            graph.addTransaction(b, c, amt);
            graph.addTransaction(c, a, amt);
        }

        // Occasionally inject ping-pong
        if (random.nextDouble() < 0.05) {
            String a = pickAccount();
            String b = pickAccountDifferent(a);
            double amt = 800 + random.nextDouble() * 2000;
            for (int i = 0; i < 3; i++) {
                graph.addTransaction(a, b, amt);
                graph.addTransaction(b, a, amt);
            }
        }
    }

    private String pickAccount() {
        return accounts.get(random.nextInt(accounts.size()));
    }

    private String pickAccountDifferent(String other) {
        String candidate;
        do {
            candidate = pickAccount();
        } while (candidate.equals(other));
        return candidate;
    }
}