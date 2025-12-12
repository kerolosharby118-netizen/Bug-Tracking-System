package bt;

import bt.data.Database;
import bt.ui.LoginFrame;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Load persisted data (or create defaults)
        Database.load();

        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });

        // ensure save on exit
        Runtime.getRuntime().addShutdownHook(new Thread(Database::saveAll));
    }
}
