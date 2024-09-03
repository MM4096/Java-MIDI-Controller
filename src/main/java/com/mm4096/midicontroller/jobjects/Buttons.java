package com.mm4096.midicontroller.jobjects;

import javax.swing.*;
import java.awt.*;

public class Buttons {
    /**
     * Get a styled button with the following properties:<br>
     * - Black background<br>
     * - White text<br>
     * - No focus paint<br>
     * - Not focusable
     * @param text Text to display on the button
     * @return A styled JButton
     */
    public static JButton getStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(Color.BLACK);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFocusable(false);
        return button;
    }
}
