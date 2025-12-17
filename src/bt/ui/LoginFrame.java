package bt.ui;

import bt.data.Database;
import bt.models.User;

import javax.swing.*;
import java.awt.*;

/**
 * نافذة تسجيل الدخول - أول ما يظهر للمستخدم
 * تقوم بالتحقق من صحة بيانات المستخدم وتوجيهه للشاشة المناسبة
 */
@SuppressWarnings("serial")
public class LoginFrame extends JFrame {
    // مكونات واجهة المستخدم
    private JTextField txtUser;      // حقل إدخال اسم المستخدم
    private JPasswordField txtPass;  // حقل إدخال كلمة المرور (مشفر)
    private JComboBox<String> cbRole; // قائمة اختيار الدور

    /**
     * مُنشئ نافذة الدخول - يقوم بإعداد النافذة وعناصرها
     */
    public LoginFrame() {
        setTitle("Bug Tracker - Login"); // عنوان النافذة
        setSize(380, 220);               // حجم النافذة (عرض × ارتفاع)
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // إغلاق البرنامج عند إغلاق النافذة
        setLocationRelativeTo(null);     // عرض النافذة في منتصف الشاشة
        initUI();                        // تهيئة عناصر الواجهة
    }

    /**
     * تهيئة وبناء واجهة المستخدم
     */
    private void initUI() {
        JPanel p = new JPanel(); // اللوحة الرئيسية
        p.setLayout(null);       // نستخدم تنسيق يدوي للمواقع

        // عنوان الشاشة
        JLabel lbl = new JLabel("Login");
        lbl.setFont(new Font("Arial", Font.BOLD, 18));
        lbl.setBounds(150, 5, 100, 30);
        p.add(lbl);

        // حقل اسم المستخدم
        JLabel u = new JLabel("Username:");
        u.setBounds(30, 50, 80, 25);
        p.add(u);
        txtUser = new JTextField();
        txtUser.setBounds(120, 50, 200, 25);
        p.add(txtUser);

        // حقل كلمة المرور
        JLabel pw = new JLabel("Password:");
        pw.setBounds(30, 85, 80, 25);
        p.add(pw);
        txtPass = new JPasswordField();
        txtPass.setBounds(120, 85, 200, 25);
        p.add(txtPass);

        // قائمة اختيار الدور
        JLabel r = new JLabel("Role:");
        r.setBounds(30, 120, 80, 25);
        p.add(r);
        // الأدوار المتاحة في النظام
        cbRole = new JComboBox<>(new String[]{"Admin", "Tester", "Developer", "PM"});
        cbRole.setBounds(120, 120, 200, 25);
        p.add(cbRole);

        // زر الدخول
        JButton btn = new JButton("Login");
        btn.setBounds(120, 155, 90, 25);
        p.add(btn);

        // زر الخروج
        JButton exit = new JButton("Exit");
        exit.setBounds(230, 155, 90, 25);
        p.add(exit);

        add(p); // إضافة اللوحة للنافذة

        // تعريف الأحداث (Event Listeners)
        btn.addActionListener(e -> doLogin());      // عند الضغط على Login
        exit.addActionListener(a -> System.exit(0)); // عند الضغط على Exit
    }

    /**
     * دالة التحقق من بيانات الدخول
     * تقوم بالتحقق من اسم المستخدم، كلمة المرور، والدور
     */
    private void doLogin() {
        // الحصول على البيانات من الحقول
        String user = txtUser.getText().trim();                 // اسم المستخدم
        String pass = new String(txtPass.getPassword()).trim(); // كلمة المرور
        String role = cbRole.getSelectedItem().toString();      // الدور المختار

        // البحث عن المستخدم في قاعدة البيانات
        User u = Database.findUser(user);

        // التحقق من وجود المستخدم
        if (u == null) {
            JOptionPane.showMessageDialog(this, "User not found");
            return;
        }

        // التحقق من كلمة المرور
        if (!u.getPassword().equals(pass)) {
            JOptionPane.showMessageDialog(this, "Wrong password");
            return;
        }

        // التحقق من الدور
        if (!u.getRole().equalsIgnoreCase(role)) {
            JOptionPane.showMessageDialog(this, "Role mismatch");
            return;
        }

        // إذا كانت جميع البيانات صحيحة: فتح الشاشة المناسبة حسب الدور
        switch (role) {
            case "Admin":
                new AdminFrame(u).setVisible(true);      // شاشة المدير
                break;
            case "Tester":
                new TesterFrame(u).setVisible(true);     // شاشة المختبر
                break;
            case "Developer":
                new DeveloperFrame(u).setVisible(true);  // شاشة المطور
                break;
            case "PM":
                new PMFrame(u).setVisible(true);         // شاشة مدير المشروع
                break;
            default:
                JOptionPane.showMessageDialog(this, "Unknown role");
                return;
        }

        this.dispose(); // إغلاق نافذة الدخول بعد النجاح
    }
}