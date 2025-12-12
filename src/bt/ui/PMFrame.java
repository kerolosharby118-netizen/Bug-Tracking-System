package bt.ui;

import bt.data.Database;
import bt.models.Bug;
import bt.models.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("serial")
public class PMFrame extends JFrame {
    private User pm;

    public PMFrame(User pm) {
        this.pm = pm;
        setTitle("Project Manager - " + pm.getUsername());
        setSize(900,520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        init();
    }

    private void init() {
        JLabel head = new JLabel("Project Manager Panel");
        head.setFont(new Font("Arial", Font.BOLD, 18));
        add(head, BorderLayout.NORTH);

        String[] cols = {"ID","Name","Project","Priority","Level","Status","AssignedTo","ReportedBy"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        JTable table = new JTable(model);
        for (Bug b : Database.bugs) {
            model.addRow(new Object[]{b.getId(), b.getName(), b.getProjectName(), b.getPriority(), b.getLevel(), b.getStatus(), b.getAssignedTo(), b.getReportedBy()});
        }
        add(new JScrollPane(table), BorderLayout.CENTER);

        // simple performance report
        JButton rpt = new JButton("Show Performance");
        add(rpt, BorderLayout.SOUTH);
        rpt.addActionListener(a -> {
            Map<String, Integer> solved = new HashMap<>();
            for (Bug b : Database.bugs) {
                if ("Closed".equalsIgnoreCase(b.getStatus()) && b.getAssignedTo() != null && !b.getAssignedTo().isEmpty()) {
                    solved.put(b.getAssignedTo(), solved.getOrDefault(b.getAssignedTo(), 0) + 1);
                }
            }
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String,Integer> e : solved.entrySet()) {
                sb.append(e.getKey()).append(": ").append(e.getValue()).append(" bugs closed\n");
            }
            if (sb.length() == 0) sb.append("No closed bugs yet.");
            JOptionPane.showMessageDialog(this, sb.toString(), "Performance", JOptionPane.INFORMATION_MESSAGE);
        });
    }
}
