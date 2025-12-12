package bt.ui;

import bt.models.User;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public class LoginFrame extends JFrame {
    private JTextField txtUser;
    private JPasswordField txtPass;
    private JComboBox<String> cbRole;

    public LoginFrame() {
        setTitle("Bug Tracker - Login");
        setSize(380,220);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initUI();
    }

    private void initUI() {
        JPanel p = new JPanel();
        p.setLayout(null);

        JLabel lbl = new JLabel("Login");
        lbl.setFont(new Font("Arial", Font.BOLD, 18));
        lbl.setBounds(150, 5, 100, 30);
        p.add(lbl);

        JLabel u = new JLabel("Username:");
        u.setBounds(30,50,80,25);
        p.add(u);
        txtUser = new JTextField();
        txtUser.setBounds(120,50,200,25);
        p.add(txtUser);

        JLabel pw = new JLabel("Password:");
        pw.setBounds(30,85,80,25);
        p.add(pw);
        txtPass = new JPasswordField();
        txtPass.setBounds(120,85,200,25);
        p.add(txtPass);

        JLabel r = new JLabel("Role:");
        r.setBounds(30,120,80,25);
        p.add(r);
        cbRole = new JComboBox<>(new String[] {"Tester","Developer","PM","Admin"});
        cbRole.setBounds(120,120,200,25);
        p.add(cbRole);

        JButton btn = new JButton("Login");
        btn.setBounds(120,155,90,25);
        p.add(btn);

        JButton exit = new JButton("Exit");
        exit.setBounds(230,155,90,25);
        p.add(exit);

        add(p);

        btn.addActionListener(e -> doLogin());
        exit.addActionListener(a -> System.exit(0));
    }

    // ----------------------------------------
    //  Users ثابتين بدون Database
    // ----------------------------------------
    private User findUserLocal(String username) {

        if (username.equalsIgnoreCase("admin"))
            return new User("admin", "123", "Admin");

        if (username.equalsIgnoreCase("tester"))
            return new User("tester", "123", "Tester");

        if (username.equalsIgnoreCase("dev"))
            return new User("dev", "123", "Developer");

        if (username.equalsIgnoreCase("manager"))
            return new User("manager", "123", "PM");

        return null;
    }

    private void doLogin() {
        String user = txtUser.getText().trim();
        String pass = new String(txtPass.getPassword()).trim();
        String role = cbRole.getSelectedItem().toString();

        User u = findUserLocal(user);
        if (u == null) {
            JOptionPane.showMessageDialog(this, "User not found");
            return;
        }

        if (!u.getPassword().equals(pass)) {
            JOptionPane.showMessageDialog(this, "Wrong password");
            return;
        }

        // Role match
        if (!u.getRole().equalsIgnoreCase(role)) {
            JOptionPane.showMessageDialog(this, "Role mismatch");
            return;
        }

        // فتح الشاشات حسب الدور
        switch (role) {
            case "Admin":
                new AdminFrame(u).setVisible(true);
                break;
            case "Tester":
                new TesterFrame(u).setVisible(true);
                break;
            case "Developer":
                new DeveloperFrame(u).setVisible(true);
                break;
            case "PM":
                new PMFrame(u).setVisible(true);
                break;
            default:
                JOptionPane.showMessageDialog(this, "Unknown role");
                return;
        }
        this.dispose();
    }
}
