package com.shopvn;

import com.shopvn.form.RegistrationFormUI;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        // Dùng Nimbus để tránh lỗi render nút trên Windows
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            RegistrationFormUI ui = new RegistrationFormUI();
            ui.setVisible(true);
        });
    }
}
