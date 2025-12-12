package bt.data;

import bt.models.User;
import bt.models.Bug;
import bt.utils.FileUtil;

import java.util.ArrayList;
import java.util.List;

public class Database {
    private static final String USERS_FILE = "users.dat";
    private static final String BUGS_FILE = "bugs.dat";

    public static List<User> users = new ArrayList<>();
    public static List<Bug> bugs = new ArrayList<>();

    public static void load() {
        Object u = FileUtil.loadObject(USERS_FILE);
        if (u instanceof List) {
            users = (List<User>) u;
        } else {
            // create default users
            users = new ArrayList<>();
            users.add(new User("admin", "admin123", "Admin"));
            users.add(new User("tester1", "t123", "Tester"));
            users.add(new User("dev1", "d123", "Developer"));
            users.add(new User("pm1", "p123", "PM"));
            FileUtil.saveObject(USERS_FILE, users);
        }

        Object b = FileUtil.loadObject(BUGS_FILE);
        if (b instanceof List) {
            bugs = (List<Bug>) b;
            // restore Bug counter
            int maxId = 0;
            for (Bug bug : bugs) if (bug.getId() > maxId) maxId = bug.getId();
            bt.models.Bug.setCounter(maxId + 1);
        } else {
            bugs = new ArrayList<>();
            FileUtil.saveObject(BUGS_FILE, bugs);
        }
    }

    public static void saveAll() {
        FileUtil.saveObject(USERS_FILE, users);
        FileUtil.saveObject(BUGS_FILE, bugs);
    }

    public static User findUser(String username) {
        for (User u : users) if (u.getUsername().equalsIgnoreCase(username)) return u;
        return null;
    }

    public static List<Bug> getBugsForDeveloper(String devUsername) {
        List<Bug> res = new ArrayList<>();
        for (Bug b : bugs) if (devUsername.equalsIgnoreCase(b.getAssignedTo())) res.add(b);
        return res;
    }
}

