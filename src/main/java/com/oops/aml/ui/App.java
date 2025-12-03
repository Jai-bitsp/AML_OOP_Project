package com.oops.aml.ui;

import com.oops.aml.engine.DynamicGraph;
import com.oops.aml.observers.CircularFlowDetector;
import com.oops.aml.stream.TransactionStreamSimulator;

import javax.swing.*;
import java.awt.*;
import java.time.Duration;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DynamicGraph graph = new DynamicGraph();
            AlertsPanel alerts = new AlertsPanel();
            graph.setAlertSink(alerts);

            GraphView graphView = new GraphView(graph);
            TransactionStreamSimulator simulator = new TransactionStreamSimulator(graph, 50);
            ControlsPane controls = new ControlsPane(graph, simulator);
            StatsPane stats = new StatsPane(graph);

            // Register circular flow detector by default and wire it to controls
            CircularFlowDetector circular = new CircularFlowDetector(Duration.ofSeconds(60));
            graph.registerObserver(circular);
            controls.setCircularDetector(circular);

            JFrame frame = new JFrame("AML Graph Engine (MVP)");
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());
            frame.add(graphView, BorderLayout.CENTER);
            frame.add(alerts, BorderLayout.EAST);
            frame.add(controls, BorderLayout.SOUTH);
            frame.add(stats, BorderLayout.WEST);
            frame.setSize(1200, 800);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
