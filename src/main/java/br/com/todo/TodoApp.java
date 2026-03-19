package br.com.todo;

import br.com.todo.view.AppFrame;

import javax.swing.*;
import java.awt.*;

public class TodoApp {
    public static void main(String[] args) {

        // deve ser definido ANTES de qualquer inicialização Swing
        System.setProperty("sun.java2d.uiScale.enabled", "true");
        System.setProperty("sun.java2d.uiScale", "1.3");
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");
        System.setProperty("swing.defaultlaf", UIManager.getCrossPlatformLookAndFeelClassName());

        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        UIManager.put("ComboBox.border", BorderFactory.createLineBorder(new Color(50, 50, 70)));
        UIManager.put("ComboBox.selectionBackground", new Color(80, 60, 160));
        UIManager.put("ComboBox.selectionForeground", Color.WHITE);
        UIManager.put("ComboBox.background", new Color(50, 50, 70));
        UIManager.put("ComboBox.foreground", Color.WHITE);

        SwingUtilities.invokeLater(AppFrame::new);
    }
}
