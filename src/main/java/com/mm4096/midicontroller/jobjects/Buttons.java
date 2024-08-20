package com.mm4096.midicontroller.jobjects;

import javax.swing.*;
import java.awt.*;

public class Buttons {
    public static JButton getStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(Color.BLACK);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFocusable(false);
        return button;
    }
}
