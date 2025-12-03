package com.oops.aml.engine;

import java.util.ArrayList;
import java.util.List;

public class AccountNode {
    private final String id;
    private double riskScore = 0.0;
    private final List<TransactionEdge> outgoingEdges = new ArrayList<>();
    private final List<TransactionEdge> incomingEdges = new ArrayList<>();

    public AccountNode(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public double getRiskScore() {
        return riskScore;
    }

    public void updateRisk(double delta) {
        riskScore = Math.max(0.0, Math.min(1.0, riskScore + delta));
    }

    public List<TransactionEdge> getOutgoingEdges() {
        return outgoingEdges;
    }

    public List<TransactionEdge> getIncomingEdges() {
        return incomingEdges;
    }

    public List<AccountNode> getConnections() {
        List<AccountNode> neighbors = new ArrayList<>();
        for (TransactionEdge e : outgoingEdges) {
            neighbors.add(e.getReceiver());
        }
        for (TransactionEdge e : incomingEdges) {
            neighbors.add(e.getSender());
        }
        return neighbors;
    }
}