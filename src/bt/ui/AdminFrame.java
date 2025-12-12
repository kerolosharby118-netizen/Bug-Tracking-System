package bt.ui;

import bt.data.Database;
import bt.models.User;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public class AdminFrame extends JFrame {
    private User admin;

    public AdminFrame(User admin) {
        this.admin = admin;
        setTitle("Admin Dashboard - " + admin.getUsername());
        setSize(700,450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        init();
    }

    private void init() {
        JPanel p = new JPanel(new BorderLayout());
        JLabel head = new JLabel("Admin Panel", SwingConstants.CENTER);
        head.setFont(new Font("Arial", Font.BOLD, 20));
        p.add(head, BorderLayout.NORTH);

        // center: users list + buttons
        DefaultListModel<String> lm = new DefaultListModel<>();
        for (User u : Database.users) lm.addElement(u.getUsername() + " (" + u.getRole() + ")");
        JList<String> userList = new JList<>(lm);
        JScrollPane sp = new JScrollPane(userList);
        sp.setPreferredSize(new Dimension(300,300));

        JPanel right = new JPanel();
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));

        JButton add = new JButton("Add User");
        JButton del = new JButton("Delete User");
        JButton edit = new JButton("Edit User");
        JButton save = new JButton("Save & Exit");

        right.add(add);
        right.add(Box.createVerticalStrut(10));
        right.add(edit);
        right.add(Box.createVerticalStrut(10));
        right.add(del);
        right.add(Box.createVerticalStrut(10));
        right.add(save);

        JPanel center = new JPanel();
        center.add(sp);
        center.add(right);

        p.add(center, BorderLayout.CENTER);
        add(p);

        add.addActionListener(e -> {
            JTextField u = new JTextField();
            JTextField pw = new JTextField();
            String[] roles = {"Tester","Developer","PM","Admin"};
            JComboBox<String> cb = new JComboBox<>(roles);
            Object[] fields = {
                    "Username", u,
                    "Password", pw,
                    "Role", cb
            };
            int res = JOptionPane.showConfirmDialog(this, fields, "Add User", JOptionPane.OK_CANCEL_OPTION);
            if (res == JOptionPane.OK_OPTION) {
                if (u.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Username required");
                    return;
                }
                Database.users.add(new User(u.getText().trim(), pw.getText().trim(), cb.getSelectedItem().toString()));
                lm.addElement(u.getText().trim() + " (" + cb.getSelectedItem().toString() + ")");
            }
        });

        edit.addActionListener(a -> {
            int sel = userList.getSelectedIndex();
            if (sel < 0) { JOptionPane.showMessageDialog(this, "Select user"); return; }
            User selUser = Database.users.get(sel);
            JTextField u = new JTextField(selUser.getUsername());
            JTextField pw = new JTextField(selUser.getPassword());
            String[] roles = {"Tester","Developer","PM","Admin"};
            JComboBox<String> cb = new JComboBox<>(roles);
            cb.setSelectedItem(selUser.getRole());
            Object[] fields = {"Username", u, "Password", pw, "Role", cb};
            int res = JOptionPane.showConfirmDialog(this, fields, "Edit User", JOptionPane.OK_CANCEL_OPTION);
            if (res == JOptionPane.OK_OPTION) {
                selUser.setPassword(pw.getText().trim());
                selUser.setRole(cb.getSelectedItem().toString());
                lm.set(sel, u.getText().trim() + " (" + cb.getSelectedItem().toString() + ")");
            }
        });

        del.addActionListener(a -> {
            int sel = userList.getSelectedIndex();
            if (sel >= 0) {
                int c = JOptionPane.showConfirmDialog(this, "Delete selected user?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (c == JOptionPane.YES_OPTION) {
                    Database.users.remove(sel);
                    lm.remove(sel);
                }
            } else JOptionPane.showMessageDialog(this, "Select a user first");
        });

        save.addActionListener(a -> {
            Database.saveAll();
            JOptionPane.showMessageDialog(this, "Saved. Exiting.");
            System.exit(0);
        });
    }
}
