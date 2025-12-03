package com.oops.aml.observers;

import com.oops.aml.engine.*;
import com.oops.aml.engine.alerts.AlertType;

import java.time.Duration;
import java.util.List;

public class RapidTransactionDetector implements GraphObserver {
    private final Duration window;
    private final int burstCount;
    private final double minAmount;

    public RapidTransactionDetector(Duration window, int burstCount, double minAmount) {
        this.window = window;
        this.burstCount = burstCount;
        this.minAmount = minAmount;
    }

    @Override
    public void onTransactionAdded(TransactionEdge edge, DynamicGraph graph) {
        // Ping-pong detection: A->B and B->A within window
        List<TransactionEdge> recent = graph.getRecentEdges(window);
        int abCount = 0;
        int baCount = 0;
        for (TransactionEdge e : recent) {
            if (e.getAmount() < minAmount) continue;
            if (e.getSender() == edge.getSender() && e.getReceiver() == edge.getReceiver()) abCount++;
            if (e.getSender() == edge.getReceiver() && e.getReceiver() == edge.getSender()) baCount++;
        }
        if (abCount >= burstCount && baCount >= burstCount) {
            String a = edge.getSender().getId();
            String b = edge.getReceiver().getId();
            String msg = String.format("Rapid ping-pong transfers between %s and %s (>=%d each)", a, b, burstCount);
            graph.emitAlert(AlertType.RAPID_TX, msg, 0.6, List.of(a, b));
            edge.getSender().updateRisk(0.03);
            edge.getReceiver().updateRisk(0.03);
        }
    }
}