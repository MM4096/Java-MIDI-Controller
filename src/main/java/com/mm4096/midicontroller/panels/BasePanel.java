package com.mm4096.midicontroller.panels;

import com.mm4096.midicontroller.frames.MainFrame;

import javax.swing.*;

public class BasePanel extends JPanel {
    public String id;
    private MainFrame mainFrame;

    public void setMainFrame(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    public MainFrame getMainFrame() {
        return mainFrame;
    }
}
