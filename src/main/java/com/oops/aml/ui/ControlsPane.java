package com.oops.aml.ui;

import com.oops.aml.engine.DynamicGraph;
import com.oops.aml.observers.RapidTransactionDetector;
import com.oops.aml.observers.CircularFlowDetector;
import com.oops.aml.stream.TransactionStreamSimulator;

import javax.swing.*;
import java.awt.*;
import java.time.Duration;

public class ControlsPane extends JPanel {
    private final DynamicGraph graph;
    private final TransactionStreamSimulator simulator;

    private RapidTransactionDetector rapidDetector;
    private CircularFlowDetector circularDetector;

    private JCheckBox cbRapid;
    private JCheckBox cbCircular;

    public ControlsPane(DynamicGraph graph, TransactionStreamSimulator simulator) {
        this.graph = graph;
        this.simulator = simulator;
        build();
    }

    public void setCircularDetector(CircularFlowDetector detector) {
        this.circularDetector = detector;
        if (cbCircular != null) {
            cbCircular.setSelected(detector != null);
        }
    }

    private void build() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JLabel title = new JLabel("Controls");
        JButton start = new JButton("Start Simulator");
        JButton stop = new JButton("Stop Simulator");

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        buttons.add(start);
        buttons.add(stop);

        cbRapid = new JCheckBox("Enable rapid tx detector", true);
        cbCircular = new JCheckBox("Enable circular flow detector", true);

        start.addActionListener(e -> simulator.start(500));
        stop.addActionListener(e -> simulator.stop());

        cbRapid.addChangeListener(e -> {
            boolean enabled = cbRapid.isSelected();
            if (enabled) {
                if (rapidDetector == null) {
                    rapidDetector = new RapidTransactionDetector(Duration.ofMinutes(10), 2, 500.0);
                }
                graph.registerObserver(rapidDetector);
            } else if (rapidDetector != null) {
                graph.unregisterObserver(rapidDetector);
            }
        });

        cbCircular.addChangeListener(e -> {
            boolean enabled = cbCircular.isSelected();
            if (enabled) {
                if (circularDetector == null) {
                    circularDetector = new CircularFlowDetector(Duration.ofSeconds(60));
                }
                graph.registerObserver(circularDetector);
            } else if (circularDetector != null) {
                graph.unregisterObserver(circularDetector);
            }
        });

        add(title);
        add(buttons);
        add(new JLabel("Detectors"));
        add(cbRapid);
        add(cbCircular);
    }
}
