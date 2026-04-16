import com.formdev.flatlaf.FlatLightLaf;
import ui.LoginFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Apply the FlatLaf modern Light theme
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }

        // Launch the application using the Event Dispatch Thread (Thread safe Swing)
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}
