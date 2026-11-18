package com.iamanshu.university;

import com.iamanshu.university.model.UniversityMapModel;
import com.iamanshu.university.ui.UniversityMapFrame;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
                // Keep the standard Swing look-and-feel as a fallback.
            }
            new UniversityMapFrame(new UniversityMapModel()).setVisible(true);
        });
    }
}
