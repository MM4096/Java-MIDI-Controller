package com.mm4096.midicontroller.tools;

import javax.swing.*;

public class DialogCreator {
    public static void showInfoDialog(String title, String message) {
        JOptionPane.showMessageDialog(null, message, title, javax.swing.JOptionPane.INFORMATION_MESSAGE);
    }

    public static void showErrorDialog(String title, String message) {
        JOptionPane.showMessageDialog(null, message, title, javax.swing.JOptionPane.ERROR_MESSAGE);
    }

    public static boolean showConfirmDialog(String title, String message) {
        int result = JOptionPane.showConfirmDialog(null, message, title, javax.swing.JOptionPane.YES_NO_OPTION);
        return result == JOptionPane.YES_OPTION;
    }

    public static String showInputDialog(String title, String message) {
        return JOptionPane.showInputDialog(null, message, title);
    }

    public static String showOptionDialog(String title, String message, String[] options) {
        return (String) JOptionPane.showInputDialog(null, message, title, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
    }
}
