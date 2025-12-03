package com.oops.aml.engine;

public interface GraphObserver {
    void onTransactionAdded(TransactionEdge edge, DynamicGraph graph);
}