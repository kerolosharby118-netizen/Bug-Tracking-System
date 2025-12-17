package bt.data;

import bt.models.User;
import bt.models.Bug;
import bt.utils.FileUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * فئة Database - تمثل قاعدة البيانات للتطبيق
 * تقوم بإدارة عمليات تحميل وحفظ البيانات للمستخدمين والأخطاء
 */
public class Database {
    // أسماء ملفات تخزين البيانات
    private static final String USERS_FILE = "users.dat"; // ملف تخزين المستخدمين
    private static final String BUGS_FILE = "bugs.dat";   // ملف تخزين الأخطاء

    // قوائم البيانات في الذاكرة
    public static List<User> users = new ArrayList<>(); // قائمة جميع المستخدمين
    public static List<Bug> bugs = new ArrayList<>();   // قائمة جميع الأخطاء

    /**
     * تحميل البيانات من الملفات إلى الذاكرة
     * إذا لم توجد الملفات، يتم إنشاء بيانات افتراضية
     */
    public static void load() {
        // 1. تحميل بيانات المستخدمين
        Object u = FileUtil.loadObject(USERS_FILE);
        if (u instanceof List) {
            // إذا كان الملف موجوداً ويحتوي على بيانات
            users = (List<User>) u; // تحويل البيانات إلى List<User>
        } else {
            // إذا لم يكن الملف موجوداً: إنشاء بيانات افتراضية
            users = new ArrayList<>();
            users.add(new User("admin", "a123", "Admin"));
            users.add(new User("tester1", "t123", "Tester"));
            users.add(new User("dev1", "d123", "Developer"));
            users.add(new User("pm1", "p123", "PM"));
            FileUtil.saveObject(USERS_FILE, users); // حفظ البيانات الافتراضية
        }

        // 2. تحميل بيانات الأخطاء
        Object b = FileUtil.loadObject(BUGS_FILE);
        if (b instanceof List) {
            bugs = (List<Bug>) b; // تحويل البيانات إلى List<Bug>

            // استعادة عداد الأخطاء (لضمان عدم تكرار الأرقام)
            int maxId = 0;
            for (Bug bug : bugs) {
                if (bug.getId() > maxId) maxId = bug.getId();
            }
            bt.models.Bug.setCounter(maxId + 1); // تعيين العدد التالي
        } else {
            // إذا لم يكن الملف موجوداً: إنشاء قائمة فارغة
            bugs = new ArrayList<>();
            FileUtil.saveObject(BUGS_FILE, bugs);
        }
    }

    /**
     * حفظ جميع البيانات في الملفات
     * يتم استدعاؤها عند تعديل أي بيانات
     */
    public static void saveAll() {
        FileUtil.saveObject(USERS_FILE, users); // حفظ المستخدمين
        FileUtil.saveObject(BUGS_FILE, bugs);   // حفظ الأخطاء
    }

    /**
     * البحث عن مستخدم بواسطة اسم المستخدم
     * @param username اسم المستخدم للبحث
     * @return كائن User إذا وجد، null إذا لم يوجد
     */
    public static User findUser(String username) {
        // البحث في قائمة المستخدمين
        for (User u : users) {
            if (u.getUsername().equalsIgnoreCase(username))
                return u;
        }
        return null; // لم يتم العثور على المستخدم
    }

    /**
     * الحصول على قائمة الأخطاء المخصصة لمطور معين
     * @param devUsername اسم المستخدم للمطور
     * @return قائمة الأخطاء المسؤول عنها المطور
     */
    public static List<Bug> getBugsForDeveloper(String devUsername) {
        List<Bug> res = new ArrayList<>(); // القائمة الناتجة
        for (Bug b : bugs) {
            // إذا كان الخطأ مخصصاً لهذا المطور
            if (devUsername.equalsIgnoreCase(b.getAssignedTo()))
                res.add(b);
        }
        return res;
    }
}