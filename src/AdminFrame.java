import javax.swing.*;

public class AdminFrame extends JFrame {

    public AdminFrame() {
        setTitle("Admin Panel");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel label = new JLabel("Welcome Admin", SwingConstants.CENTER);
        add(label);
    }
}
