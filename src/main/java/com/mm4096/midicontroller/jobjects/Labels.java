package com.mm4096.midicontroller.jobjects;

import javax.swing.*;
import java.awt.*;

public class Labels {
    /**
     * Get a styled label with the following properties:<br>
     * - White text<br>
     * - Center alignment<br>
     * - Body font
     * @param label The label to style
     * @return The styled JLabel
     */
    public static JLabel styleLabelDefault(JLabel label) {
        label.setForeground(Color.WHITE);
        label.setFont(Fonts.getBody());
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }
}
