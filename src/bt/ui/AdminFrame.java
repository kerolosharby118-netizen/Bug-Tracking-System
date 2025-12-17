package bt.ui;

import bt.data.Database;
import bt.models.User;

import javax.swing.*;
import java.awt.*;

/**
 * نافذة AdminFrame - الواجهة الرئيسية للمدير (Admin)
 * تمكن المدير من إدارة جميع مستخدمي النظام
 * الوظائف: إضافة مستخدم، تعديل مستخدم، حذف مستخدم
 */
@SuppressWarnings("serial")
public class AdminFrame extends JFrame {
    private User admin; // المستخدم الحالي (المدير)

    /**
     * مُنشئ نافذة المدير
     * @param admin كائن المستخدم الحالي (المدير)
     */
    public AdminFrame(User admin) {
        this.admin = admin;
        setTitle("Admin Dashboard - " + admin.getUsername()); // عنوان النافذة
        setSize(700, 450);                                    // حجم النافذة
        setLocationRelativeTo(null);                          // منتصف الشاشة
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);       // إغلاق البرنامج عند إغلاق النافذة
        init(); // استدعاء دالة تهيئة الواجهة
    }

    /**
     * دالة تهيئة وبناء جميع مكونات الواجهة
     */
    private void init() {
        // ---------- اللوحة الرئيسية ----------
        JPanel p = new JPanel(new BorderLayout()); // تنسيق BorderLayout (شمال، جنوب، شرق، غرب، وسط)

        // ---------- عنوان النافذة (الجزء العلوي) ----------
        JLabel head = new JLabel("Admin Panel", SwingConstants.CENTER); // عنوان في المنتصف
        head.setFont(new Font("Arial", Font.BOLD, 20)); // خط عريض حجم 20
        p.add(head, BorderLayout.NORTH); // إضافة العنوان للجزء الشمالي

        // ---------- الجزء الأوسط: قائمة المستخدمين والأزرار ----------

        // 1. قائمة المستخدمين (JList)
        DefaultListModel<String> lm = new DefaultListModel<>(); // نموذج البيانات للقائمة

        // تحميل جميع المستخدمين من قاعدة البيانات
        for (User u : Database.users) {
            // تنسيق: "اسم المستخدم (الدور)"
            lm.addElement(u.getUsername() + " (" + u.getRole() + ")");
        }

        JList<String> userList = new JList<>(lm); // إنشاء القائمة
        JScrollPane sp = new JScrollPane(userList); // إضافة أشرطة التمرير
        sp.setPreferredSize(new Dimension(300, 300)); // تحديد حجم مناسب

        // 2. لوحة الأزرار الجانبية
        JPanel right = new JPanel();
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS)); // ترتيب عمودي

        // إنشاء الأزرار
        JButton add = new JButton("Add User");     // زر إضافة مستخدم
        JButton del = new JButton("Delete User");  // زر حذف مستخدم
        JButton edit = new JButton("Edit User");   // زر تعديل مستخدم
        JButton save = new JButton("Save & Exit"); // زر حفظ وخروج

        // إضافة الأزرار مع مسافات بينها
        right.add(add);
        right.add(Box.createVerticalStrut(10)); // مسافة 10 بكسل
        right.add(edit);
        right.add(Box.createVerticalStrut(10));
        right.add(del);
        right.add(Box.createVerticalStrut(10));
        right.add(save);

        // 3. دمج القائمة والأزرار في لوحة واحدة
        JPanel center = new JPanel();
        center.add(sp);   // إضافة قائمة المستخدمين
        center.add(right); // إضافة الأزرار

        p.add(center, BorderLayout.CENTER); // إضافة الجزء الأوسط للوحة الرئيسية

        add(p); // إضافة اللوحة الرئيسية للنافذة

        // ---------- أحداث الأزرار (Event Listeners) ----------

        // حدث زر إضافة مستخدم جديد
        add.addActionListener(e -> {
            // إنشاء حقول الإدخال
            JTextField u = new JTextField(); // حقل اسم المستخدم
            JTextField pw = new JTextField(); // حقل كلمة المرور

            String[] roles = {"Tester","Developer","PM","Admin"}; // الأدوار المتاحة
            JComboBox<String> cb = new JComboBox<>(roles); // قائمة اختيار الدور

            // ترتيب الحقول في مصفوفة لعرضها في النافذة
            Object[] fields = {
                    "Username", u,   // التسمية ثم الحقل
                    "Password", pw,
                    "Role", cb
            };

            // عرض نافذة الإدخال (Dialog)
            int res = JOptionPane.showConfirmDialog(this, fields,
                    "Add User", JOptionPane.OK_CANCEL_OPTION);

            // إذا ضغط المستخدم OK
            if (res == JOptionPane.OK_OPTION) {
                // التحقق من إدخال اسم المستخدم
                if (u.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Username required");
                    return;
                }

                // إنشاء كائن User جديد
                Database.users.add(new User(
                        u.getText().trim(),              // اسم المستخدم
                        pw.getText().trim(),             // كلمة المرور
                        cb.getSelectedItem().toString()  // الدور المختار
                ));

                // إضافة المستخدم الجديد للقائمة المعروضة
                lm.addElement(u.getText().trim() + " (" + cb.getSelectedItem().toString() + ")");

                // ملاحظة: لم يتم حفظ البيانات في الملف بعد، سيتم عند الضغط على Save & Exit
                JOptionPane.showMessageDialog(this, "User added successfully!");
            }
        });

        // حدث زر تعديل مستخدم
        edit.addActionListener(a -> {
            int sel = userList.getSelectedIndex(); // الحصول على الفهرس المحدد

            // التحقق من اختيار مستخدم
            if (sel < 0) {
                JOptionPane.showMessageDialog(this, "Select user first");
                return;
            }

            User selUser = Database.users.get(sel); // الحصول على كائن المستخدم المحدد

            // إنشاء حقول الإدخال مع البيانات الحالية
            JTextField u = new JTextField(selUser.getUsername());   // اسم المستخدم الحالي
            JTextField pw = new JTextField(selUser.getPassword());  // كلمة المرور الحالية

            String[] roles = {"Tester","Developer","PM","Admin"}; // الأدوار المتاحة
            JComboBox<String> cb = new JComboBox<>(roles); // قائمة اختيار الدور
            cb.setSelectedItem(selUser.getRole()); // تحديد الدور الحالي

            // ترتيب الحقول
            Object[] fields = {
                    "Username", u,
                    "Password", pw,
                    "Role", cb
            };

            // عرض نافذة التعديل
            int res = JOptionPane.showConfirmDialog(this, fields,
                    "Edit User", JOptionPane.OK_CANCEL_OPTION);

            // إذا ضغط المستخدم OK
            if (res == JOptionPane.OK_OPTION) {
                // تحديث بيانات المستخدم
                selUser.setPassword(pw.getText().trim());         // تحديث كلمة المرور
                selUser.setRole(cb.getSelectedItem().toString()); // تحديث الدور

                // تحديث القائمة المعروضة
                lm.set(sel, u.getText().trim() + " (" + cb.getSelectedItem().toString() + ")");

                JOptionPane.showMessageDialog(this, "User updated successfully!");
            }
        });

        // حدث زر حذف مستخدم
        del.addActionListener(a -> {
            int sel = userList.getSelectedIndex(); // الحصول على الفهرس المحدد

            if (sel >= 0) {
                // طلب تأكيد الحذف
                int c = JOptionPane.showConfirmDialog(this,
                        "Delete selected user?", "Confirm", JOptionPane.YES_NO_OPTION);

                if (c == JOptionPane.YES_OPTION) {
                    Database.users.remove(sel); // حذف المستخدم من قاعدة البيانات
                    lm.remove(sel);             // حذف المستخدم من القائمة المعروضة

                    JOptionPane.showMessageDialog(this, "User deleted successfully!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Select a user first");
            }
        });

        // حدث زر حفظ البيانات والخروج
        save.addActionListener(a -> {
            Database.saveAll(); // حفظ جميع التغييرات في الملفات

            JOptionPane.showMessageDialog(this, "Saved. Exiting.");
            System.exit(0); // إغلاق البرنامج
        });
    }
}