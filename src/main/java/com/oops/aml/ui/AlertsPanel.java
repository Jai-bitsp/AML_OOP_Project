package com.oops.aml.ui;
import com.oops.aml.engine.alerts.Alert;
import com.oops.aml.engine.alerts.AlertSink;
import javax.swing.*;
import java.awt.*;

public class AlertsPanel extends JPanel implements AlertSink {
    private final DefaultListModel<String> model = new DefaultListModel<>();
    private final JList<String> list = new JList<>(model);

    public AlertsPanel() {
        setLayout(new BorderLayout(8, 8));
        add(new JLabel("Alerts"), BorderLayout.NORTH);
        add(new JScrollPane(list), BorderLayout.CENTER);
        setPreferredSize(new Dimension(320, 0));
    }

    @Override
    public void emit(Alert alert) {
        SwingUtilities.invokeLater(() -> {
            String entry = String.format("[%s] %s | sev=%.2f", alert.getType(), alert.getMessage(), alert.getSeverity());
            model.addElement(entry);
            list.ensureIndexIsVisible(model.size() - 1);
        });
    }
}
