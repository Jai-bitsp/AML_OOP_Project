package com.oops.aml.engine.alerts;

public interface AlertSink {
    void emit(Alert alert);
}