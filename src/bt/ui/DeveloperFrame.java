package bt.ui;

import bt.data.Database;
import bt.models.Bug;
import bt.models.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

@SuppressWarnings("serial")
public class DeveloperFrame extends JFrame {
    private User dev;
    private DefaultTableModel model;

    public DeveloperFrame(User dev) {
        this.dev = dev;
        setTitle("Developer Dashboard - " + dev.getUsername());
        setSize(800,450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        init();
    }

    private void init() {
        JLabel head = new JLabel("Developer Panel");
        head.setFont(new Font("Arial", Font.BOLD, 18));
        add(head, BorderLayout.NORTH);

        String[] cols = {"ID","Name","Project","Status","ReportedBy","Screenshot"};
        model = new DefaultTableModel(cols, 0);
        JTable table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        JButton refresh = new JButton("Refresh");
        JButton setInProgress = new JButton("Set In Progress");
        JButton setClosed = new JButton("Set Closed");
        bottom.add(refresh);
        bottom.add(setInProgress);
        bottom.add(setClosed);
        add(bottom, BorderLayout.SOUTH);

        refresh.addActionListener(a -> refreshTable());
        setInProgress.addActionListener(a -> changeStatus(table, "In Progress"));
        setClosed.addActionListener(a -> changeStatus(table, "Closed"));

        refreshTable();
    }

    private void refreshTable() {
        model.setRowCount(0);
        for (Bug b : Database.getBugsForDeveloper(dev.getUsername())) {
            model.addRow(new Object[]{b.getId(), b.getName(), b.getProjectName(), b.getStatus(), b.getReportedBy(), b.getScreenshotPath()});
        }
    }

    private void changeStatus(JTable table, String newStatus) {
        int sel = table.getSelectedRow();
        if (sel < 0) {
            JOptionPane.showMessageDialog(this, "Select a bug first");
            return;
        }
        int bugId = (int) table.getValueAt(sel, 0);
        for (Bug b : Database.bugs) {
            if (b.getId() == bugId) {
                b.setStatus(newStatus);
                Database.saveAll();
                refreshTable();
                JOptionPane.showMessageDialog(this, "Status updated to " + newStatus);
                // notify tester (simulated)
                JOptionPane.showMessageDialog(this, "Notification: Tester " + b.getReportedBy() + " will be notified (simulated).");
                return;
            }
        }
    }
}
