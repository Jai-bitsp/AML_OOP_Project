package com.oops.aml.engine;

import java.time.LocalDateTime;
import java.util.Map;

public class TransactionEdge {
    private final AccountNode sender;
    private final AccountNode receiver;
    private final double amount;
    private final LocalDateTime timestamp;
    private final Map<String, String> meta;

    public TransactionEdge(AccountNode sender, AccountNode receiver, double amount, LocalDateTime timestamp, Map<String, String> meta) {
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
        this.timestamp = timestamp;
        this.meta = meta;
    }

    public AccountNode getSender() {
        return sender;
    }

    public AccountNode getReceiver() {
        return receiver;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public Map<String, String> getMeta() {
        return meta;
    }

    @Override
    public String toString() {
        return "TX{" + sender.getId() + "->" + receiver.getId() + ", $" + amount + ", " + timestamp + "}";
    }
}