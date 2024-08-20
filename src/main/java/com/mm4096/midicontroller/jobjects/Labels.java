package com.mm4096.midicontroller.jobjects;

import javax.swing.*;
import java.awt.*;

public class Labels {
    public static JLabel styleLabelDefault(JLabel label) {
        label.setForeground(Color.WHITE);
        label.setFont(Fonts.getBody());
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }
}
