package view;

import javax.swing.*;
import java.awt.*;

public class GraphicalConsole {
    private static String message = null;

    private GraphicalConsole() {
    }

    public static String getMessage() {
        return message;
    }

    public static void add(Object m) {
        if (message == null)
            message = "" + m;
        else
            message = message + "\n" + m;
    }

    public static void flush() {
        message = null;
    }

    public static void showDialog() {
        showDialog(message);
    }

    public static void showDialog(String message) {
        JTextArea textArea = new JTextArea(20, 80);
        textArea.setBackground(new Color(238, 238, 238));
        textArea.setText(message);
        textArea.setEditable(false);
        JScrollPane sp = new JScrollPane(textArea);
        sp.setBorder(null);
        JOptionPane.showMessageDialog(null, sp, "Info", JOptionPane.INFORMATION_MESSAGE);
    }
}