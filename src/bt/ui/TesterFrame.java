package bt.ui;

import bt.data.Database;
import bt.models.Bug;
import bt.models.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;

@SuppressWarnings("serial")
public class TesterFrame extends JFrame {
    private User tester;
    private DefaultTableModel tableModel;

    public TesterFrame(User tester) {
        this.tester = tester;
        setTitle("Tester Dashboard - " + tester.getUsername());
        setSize(900,520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        init();
    }

    private void init() {
        JPanel top = new JPanel();
        JLabel head = new JLabel("Tester Panel");
        head.setFont(new Font("Arial", Font.BOLD, 18));
        top.add(head);

        JButton addBug = new JButton("Add Bug");
        top.add(addBug);

        JButton refresh = new JButton("Refresh");
        top.add(refresh);

        add(top, BorderLayout.NORTH);

        // Table
        String[] cols = {"ID","Name","Project","Priority","Level","Status","AssignedTo","ReportedBy","Screenshot"};
        tableModel = new DefaultTableModel(cols, 0);
        JTable table = new JTable(tableModel);
        JScrollPane sp = new JScrollPane(table);
        add(sp, BorderLayout.CENTER);

        JPanel right = new JPanel();
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        JButton assign = new JButton("Assign to Developer");
        JButton view = new JButton("View Screenshot (open path)");
        right.add(assign);
        right.add(Box.createVerticalStrut(10));
        right.add(view);
        add(right, BorderLayout.EAST);

        refreshTable();

        addBug.addActionListener(e -> {
            JTextField name = new JTextField();
            JTextField type = new JTextField();
            String[] pr = {"Low","Medium","High","Critical"};
            JComboBox<String> cbPriority = new JComboBox<>(pr);
            String[] levels = {"UI","Logic","Database","Performance"};
            JComboBox<String> cbLevel = new JComboBox<>(levels);
            JTextField project = new JTextField();
            JTextField screenshot = new JTextField();
            JButton choose = new JButton("Choose File");

            choose.addActionListener(ev -> {
                JFileChooser jf = new JFileChooser();
                int r = jf.showOpenDialog(this);
                if (r == JFileChooser.APPROVE_OPTION) {
                    File f = jf.getSelectedFile();
                    screenshot.setText(f.getAbsolutePath());
                }
            });

            Object[] fields = {
                    "Name", name,
                    "Type", type,
                    "Priority", cbPriority,
                    "Level", cbLevel,
                    "Project", project,
                    "Screenshot (optional)", screenshot,
                    choose
            };

            int res = JOptionPane.showConfirmDialog(this, fields, "Add Bug", JOptionPane.OK_CANCEL_OPTION);
            if (res == JOptionPane.OK_OPTION) {
                Bug b = new Bug(
                        name.getText().trim(),
                        type.getText().trim(),
                        cbPriority.getSelectedItem().toString(),
                        cbLevel.getSelectedItem().toString(),
                        project.getText().trim(),
                        tester.getUsername(),
                        screenshot.getText().trim()
                );
                Database.bugs.add(b);
                Database.saveAll();
                refreshTable();
                JOptionPane.showMessageDialog(this, "Bug added and saved.");
            }
        });

        assign.addActionListener(a -> {
            int sel = table.getSelectedRow();
            if (sel < 0) { JOptionPane.showMessageDialog(this, "Select a bug"); return; }
            int bugId = (int) tableModel.getValueAt(sel, 0);
            String dev = JOptionPane.showInputDialog(this, "Enter developer username to assign:");
            if (dev == null || dev.trim().isEmpty()) return;
            boolean found = false;
            for (bt.models.User u : Database.users) {
                if (u.getUsername().equalsIgnoreCase(dev) && u.getRole().equalsIgnoreCase("Developer")) { found = true; break; }
            }
            if (!found) { JOptionPane.showMessageDialog(this, "Developer not found"); return; }
            for (Bug b : Database.bugs) {
                if (b.getId() == bugId) {
                    b.setAssignedTo(dev);
                    Database.saveAll();
                    refreshTable();
                    JOptionPane.showMessageDialog(this, "Assigned to " + dev + " (notification simulated).");
                    return;
                }
            }
        });

        view.addActionListener(a -> {
            int sel = table.getSelectedRow();
            if (sel < 0) { JOptionPane.showMessageDialog(this, "Select a bug"); return; }
            String path = (String) tableModel.getValueAt(sel, 8);
            if (path == null || path.isEmpty()) { JOptionPane.showMessageDialog(this, "No screenshot path"); return; }
            try {
                Desktop.getDesktop().open(new File(path));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Cannot open file: " + ex.getMessage());
            }
        });

        refresh.addActionListener(a -> refreshTable());
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Bug b : Database.bugs) {
            tableModel.addRow(new Object[]{
                    b.getId(), b.getName(), b.getProjectName(), b.getPriority(),
                    b.getLevel(), b.getStatus(), b.getAssignedTo(), b.getReportedBy(), b.getScreenshotPath()
            });
        }
    }
}
