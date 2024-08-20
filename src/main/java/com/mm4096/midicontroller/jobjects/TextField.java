package com.mm4096.midicontroller.jobjects;

import javax.swing.*;
import java.awt.*;

public class TextField {
    public static JTextField getStyledTextField() {
        JTextField textField = new JTextField();
        textField.setBackground(Color.BLACK);
        textField.setForeground(Color.WHITE);
        textField.setFont(new Font("Arial", Font.PLAIN, 20));
        return textField;
    }
}
