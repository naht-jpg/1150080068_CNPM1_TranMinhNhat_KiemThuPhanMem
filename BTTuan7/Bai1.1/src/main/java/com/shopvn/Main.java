package com.shopvn;

import com.shopvn.form.RegistrationFormUI;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            RegistrationFormUI ui = new RegistrationFormUI();
            ui.setVisible(true);
        });
    }
}
