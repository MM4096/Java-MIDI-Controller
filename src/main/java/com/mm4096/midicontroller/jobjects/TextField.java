package com.mm4096.midicontroller.jobjects;

import javax.swing.*;
import java.awt.*;

public class TextField {
    /**
     * Get a styled text field with the following properties:<br>
     * - Black background<br>
     * - White text<br>
     * - Arial font<br>
     * - Plain style<br>
     * - 20pt size
     * @return The styled JTextField
     */
    public static JTextField getStyledTextField() {
        JTextField textField = new JTextField();
        textField.setBackground(Color.BLACK);
        textField.setForeground(Color.WHITE);
        textField.setFont(new Font("Arial", Font.PLAIN, 20));
        return textField;
    }
}
