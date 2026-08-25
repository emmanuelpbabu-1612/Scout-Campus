package scoutcampus;

import com.formdev.flatlaf.FlatDarkLaf;
import scoutcampus.gui.LoginFrame;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.put("Button.arc", 12);
            UIManager.put("Component.arc", 12);
            UIManager.put("TextComponent.arc", 12);
            UIManager.put("Component.focusColor", new Color(124, 92, 255));
            UIManager.put("Component.accentColor", new Color(124, 92, 255));
            FlatDarkLaf.setup();
        } catch (Exception ignored) {
        }

        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}