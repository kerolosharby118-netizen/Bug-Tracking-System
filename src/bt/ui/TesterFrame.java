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
    // define global variables
    private User tester;
    private DefaultTableModel tableModel;

    // takes a tester of type user as a parameter
    // sets default settings for swing
    public TesterFrame(User tester) {
        this.tester = tester;
        setTitle("Tester Dashboard - " + tester.getUsername());
        setSize(900,520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        init();
    }

    private void init() {
        // Panel header
        JPanel top = new JPanel();
        JLabel head = new JLabel("Tester Panel");
        head.setFont(new Font("Arial", Font.BOLD, 18));
        top.add(head);

        // top buttons
        JButton addBug = new JButton("Add Bug");
        top.add(addBug);

        JButton refresh = new JButton("Refresh");
        top.add(refresh);

        add(top, BorderLayout.NORTH);

        // defines the main content which is the bug table with columns and rows
        String[] cols = {"ID","Name","Project","Priority","Level","Status","AssignedTo","ReportedBy","Screenshot"};
        tableModel = new DefaultTableModel(cols, 0);
        JTable table = new JTable(tableModel);
        JScrollPane sp = new JScrollPane(table);
        add(sp, BorderLayout.CENTER);

        // aligns the right section of the frame and adds the assign developer and screenshot buttons
        JPanel right = new JPanel();
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        JButton assign = new JButton("Assign to Developer");
        JButton view = new JButton("View Screenshot (open path)");
        right.add(assign);
        right.add(Box.createVerticalStrut(10));
        right.add(view);
        add(right, BorderLayout.EAST);

        // removes all rows and repopulates them on frame startup
        refreshTable();

        addBug.addActionListener(e -> {
            // initializes the add bug form components
            JTextField name = new JTextField();
            JTextField type = new JTextField();
            String[] pr = {"Low","Medium","High","Critical"};
            JComboBox<String> cbPriority = new JComboBox<>(pr);
            String[] levels = {"UI","Logic","Database","Performance"};
            JComboBox<String> cbLevel = new JComboBox<>(levels);
            JTextField project = new JTextField();
            JTextField screenshot = new JTextField();
            JButton choose = new JButton("Choose File");

            // opens the file explorer to choose a file for the screenshot option
            choose.addActionListener(ev -> {
                JFileChooser jf = new JFileChooser();
                int r = jf.showOpenDialog(this);
                if (r == JFileChooser.APPROVE_OPTION) {
                    File f = jf.getSelectedFile();
                    screenshot.setText(f.getAbsolutePath());
                }
            });

            // defines the fields that are to be used in the form
            // assigns each field to the defined swing components above
            Object[] fields = {
                    "Name", name,
                    "Type", type,
                    "Priority", cbPriority,
                    "Level", cbLevel,
                    "Project", project,
                    "Screenshot (optional)", screenshot,
                    choose
            };

            // shows the form as a pop-up with the fields defined above
            int res = JOptionPane.showConfirmDialog(this, fields, "Add Bug", JOptionPane.OK_CANCEL_OPTION);

            if (res == JOptionPane.OK_OPTION) {
                // extracts all form input and adds it to a Bug object constructor
                Bug b = new Bug(
                        name.getText().trim(),
                        type.getText().trim(),
                        cbPriority.getSelectedItem().toString(),
                        cbLevel.getSelectedItem().toString(),
                        project.getText().trim(),
                        tester.getUsername(),
                        screenshot.getText().trim()
                );
                // saves in the database, refreshes all rows, and displays success message
                Database.bugs.add(b);
                Database.saveAll();
                refreshTable();
                JOptionPane.showMessageDialog(this, "Bug added and saved.");
            }
        });

        assign.addActionListener(a -> {
            // gets the currently selected row
            int sel = table.getSelectedRow();

            // if no row is selected when the assign button is pressed, it shows an error message and returns
            if (sel < 0) { JOptionPane.showMessageDialog(this, "Select a bug"); return; }


            int bugId = (int) tableModel.getValueAt(sel, 0);

            // adds a simple question pop-up for assigning the developer
            String dev = JOptionPane.showInputDialog(this, "Enter developer username to assign:");

            // if nothing is inputted, return
            if (dev == null || dev.trim().isEmpty()) return;
            boolean found = false;

            // loops through the users in the database, and cross-checks their name and role to the inputted dev
            for (bt.models.User u : Database.users) {
                if (u.getUsername().equalsIgnoreCase(dev) && u.getRole().equalsIgnoreCase("Developer")) { found = true; break; }
            }

            // return with an error message if the developer is not in the database
            if (!found) { JOptionPane.showMessageDialog(this, "Developer not found"); return; }

            // loops through the database and cross-checks each bug's id with the one of the selected row
            for (Bug b : Database.bugs) {
                if (b.getId() == bugId) {
                    // changes the assignedTo variable to the inputted dev and saves it to the database
                    b.setAssignedTo(dev);
                    Database.saveAll();
                    refreshTable();
                    JOptionPane.showMessageDialog(this, "Assigned to " + dev + " (notification simulated).");
                    return;
                }
            }
        });

        view.addActionListener(a -> {
            // gets the currently selected row
            int sel = table.getSelectedRow();

            // if no row is selected when the assign button is pressed, it shows an error message and returns
            if (sel < 0) { JOptionPane.showMessageDialog(this, "Select a bug"); return; }

            // gets the screenshot path from the currently selected row (column 8)
            String path = (String) tableModel.getValueAt(sel, 8);

            // returns with an error message when the row has no screenshot assigned to it
            if (path == null || path.isEmpty()) { JOptionPane.showMessageDialog(this, "No screenshot path"); return; }

            // tries to open the screenshot, if it doesn't open, returns with an error message
            try {
                Desktop.getDesktop().open(new File(path));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Cannot open file: " + ex.getMessage());
            }
        });

        // self-explanatory
        refresh.addActionListener(a -> refreshTable());
    }

    private void refreshTable() {
        // deletes existing rows in the table
        tableModel.setRowCount(0);

        // fetches all the bugs from the database
        for (Bug b : Database.bugs) {
            // adds rows to the globally defined tableModel that is used for rows in the table section
            tableModel.addRow(new Object[]{
                    b.getId(), b.getName(), b.getProjectName(), b.getPriority(),
                    b.getLevel(), b.getStatus(), b.getAssignedTo(), b.getReportedBy(), b.getScreenshotPath()
            });
        }
    }
}
