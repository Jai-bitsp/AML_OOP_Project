package com.oops.aml.engine;

import com.oops.aml.engine.alerts.Alert;
import com.oops.aml.engine.alerts.AlertSink;
import com.oops.aml.engine.alerts.AlertType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class DynamicGraph {
    private final Map<String, AccountNode> nodes = new HashMap<>();
    private final List<TransactionEdge> edges = new ArrayList<>();
    private final List<GraphObserver> observers = new CopyOnWriteArrayList<>();
    private final List<OnEdgeAddedListener> edgeListeners = new CopyOnWriteArrayList<>();
    private AlertSink alertSink;

    public interface OnEdgeAddedListener {
        void edgeAdded(TransactionEdge edge);
    }

    public void setAlertSink(AlertSink sink) {
        this.alertSink = sink;
    }

    public AlertSink getAlertSink() {
        return alertSink;
    }

    public AccountNode getOrCreate(String id) {
        return nodes.computeIfAbsent(id, AccountNode::new);
    }

    public void addTransaction(String fromId, String toId, double amount) {
        addTransaction(fromId, toId, amount, LocalDateTime.now());
    }

    public void addTransaction(String fromId, String toId, double amount, LocalDateTime ts) {
        AccountNode from = getOrCreate(fromId);
        AccountNode to = getOrCreate(toId);
        TransactionEdge edge = new TransactionEdge(from, to, amount, ts, Collections.emptyMap());
        edges.add(edge);
        from.getOutgoingEdges().add(edge);
        to.getIncomingEdges().add(edge);

        // Light baseline risk bump for activity (UI color mapping uses this)
        from.updateRisk(0.005);
        to.updateRisk(0.005);

        // Notify observers
        for (GraphObserver obs : observers) {
            obs.onTransactionAdded(edge, this);
        }

        // Notify listeners (e.g., UI graph view)
        for (OnEdgeAddedListener l : edgeListeners) {
            l.edgeAdded(edge);
        }
    }

    public void registerObserver(GraphObserver observer) {
        observers.add(observer);
    }

    public void unregisterObserver(GraphObserver observer) {
        observers.remove(observer);
    }

    public void addEdgeListener(OnEdgeAddedListener listener) {
        edgeListeners.add(listener);
    }

    public List<TransactionEdge> getRecentEdges(Duration window) {
        LocalDateTime cutoff = LocalDateTime.now().minus(window);
        List<TransactionEdge> recent = new ArrayList<>();
        for (int i = edges.size() - 1; i >= 0; i--) {
            TransactionEdge e = edges.get(i);
            if (e.getTimestamp().isBefore(cutoff)) break;
            recent.add(e);
        }
        return recent;
    }

    public Collection<AccountNode> getNodes() {
        return nodes.values();
    }

    public List<TransactionEdge> getEdges() {
        return edges;
    }

    // Utility for detectors to emit alerts
    public void emitAlert(AlertType type, String message, double severity, List<String> accounts) {
        if (alertSink != null) {
            alertSink.emit(new Alert(type, message, severity, accounts));
        }
    }
}