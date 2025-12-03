package com.oops.aml.ui;

import com.oops.aml.engine.AccountNode;
import com.oops.aml.engine.DynamicGraph;
import com.oops.aml.engine.TransactionEdge;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GraphView extends JPanel implements DynamicGraph.OnEdgeAddedListener {
    private final DynamicGraph graph;

    public GraphView(DynamicGraph graph) {
        this.graph = graph;
        setPreferredSize(new Dimension(800, 600));
        graph.addEdgeListener(this);
    }

    @Override
    public void edgeAdded(TransactionEdge edge) {
        SwingUtilities.invokeLater(this::repaint);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        List<AccountNode> nodes = new ArrayList<>(graph.getNodes());
        nodes.sort(Comparator.comparing(AccountNode::getId));
        int n = nodes.size();
        int w = getWidth();
        int h = getHeight();
        int cx = w / 2;
        int cy = h / 2;
        int radius = Math.min(w, h) / 2 - 60;

        // Compute positions on a circle
        Point[] pos = new Point[n];
        for (int i = 0; i < n; i++) {
            double angle = 2 * Math.PI * i / Math.max(n, 1);
            int x = cx + (int) (radius * Math.cos(angle));
            int y = cy + (int) (radius * Math.sin(angle));
            pos[i] = new Point(x, y);
        }

        // Map id -> index
        java.util.Map<String, Integer> index = new java.util.HashMap<>();
        for (int i = 0; i < n; i++) index.put(nodes.get(i).getId(), i);

        // Draw edges
        for (TransactionEdge e : graph.getEdges()) {
            Integer si = index.get(e.getSender().getId());
            Integer ti = index.get(e.getReceiver().getId());
            if (si == null || ti == null) continue;
            Point sp = pos[si];
            Point tp = pos[ti];
            float thickness = (float) Math.min(5.0, 1.0 + e.getAmount() / 5000.0);
            g2.setStroke(new BasicStroke(thickness));
            g2.setColor(new Color(0x90, 0xa4, 0xae));
            g2.drawLine(sp.x, sp.y, tp.x, tp.y);
        }

        // Draw nodes
        for (int i = 0; i < n; i++) {
            AccountNode node = nodes.get(i);
            Point p = pos[i];
            int size = 16;
            Ellipse2D.Double circle = new Ellipse2D.Double(p.x - size / 2.0, p.y - size / 2.0, size, size);
            Color c = toRiskColor(node.getRiskScore());
            g2.setColor(c);
            g2.fill(circle);
            g2.setColor(new Color(0x33, 0x33, 0x33));
            g2.draw(circle);
            g2.setColor(Color.DARK_GRAY);
            g2.drawString(node.getId(), p.x + 6, p.y - 6);
        }

        g2.dispose();
    }

    private Color toRiskColor(double risk) {
        int r = (int) (0x66 + (0xe5 - 0x66) * risk);
        int g = (int) (0xbb - (0xbb - 0x39) * risk);
        int b = (int) (0x6a - (0x6a - 0x35) * risk);
        return new Color(r, g, b);
    }
}
