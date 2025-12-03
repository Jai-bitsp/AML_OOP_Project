package com.oops.aml.ui;

import com.oops.aml.engine.DynamicGraph;

import javax.swing.*;
import java.awt.*;

public class StatsPane extends JPanel {
    private final DynamicGraph graph;
    private final JLabel nodes = new JLabel("Nodes: 0");
    private final JLabel edges = new JLabel("Edges: 0");

    public StatsPane(DynamicGraph graph) {
        this.graph = graph;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        add(new JLabel("Stats"));
        add(nodes);
        add(edges);

        new javax.swing.Timer(1000, e -> refresh()).start();
    }

    private void refresh() {
        nodes.setText("Nodes: " + graph.getNodes().size());
        edges.setText("Edges: " + graph.getEdges().size());
    }
}
