package com.mm4096.midicontroller;

import com.mm4096.midicontroller.frames.MainFrame;
import com.mm4096.midicontroller.panels.HomePanel;
import com.mm4096.midicontroller.panels.PatchConfigEditingPanel;
import com.mm4096.midicontroller.panels.PerformancePanel;
import com.mm4096.midicontroller.tools.FileHelper;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {

        MainFrame frame = new MainFrame();

        FileHelper.createDirectories();

        UIManager.put("TextField.caretForeground", new Color(0xFFFFFF));

        //region panels
        frame.addPanel(new PatchConfigEditingPanel());
        frame.addPanel(new HomePanel());
        frame.addPanel(new PerformancePanel());
        //endregion
        frame.changePanel("home");
    }
}
