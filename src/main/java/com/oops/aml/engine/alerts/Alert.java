package com.oops.aml.engine.alerts;

import java.time.LocalDateTime;
import java.util.List;

public class Alert {
    private final AlertType type;
    private final String message;
    private final LocalDateTime timestamp;
    private final double severity;
    private final List<String> involvedAccounts;

    public Alert(AlertType type, String message, double severity, List<String> involvedAccounts) {
        this.type = type;
        this.message = message;
        this.severity = severity;
        this.involvedAccounts = involvedAccounts;
        this.timestamp = LocalDateTime.now();
    }

    public AlertType getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public double getSeverity() {
        return severity;
    }

    public List<String> getInvolvedAccounts() {
        return involvedAccounts;
    }
}