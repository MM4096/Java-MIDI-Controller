package com.mm4096.midicontroller;

import com.mm4096.midicontroller.frames.MainFrame;
import com.mm4096.midicontroller.panels.HomePanel;
import com.mm4096.midicontroller.panels.PatchConfigEditingPanel;
import com.mm4096.midicontroller.parser.Parser;
import com.mm4096.midicontroller.tools.FileHelper;
import com.mm4096.midicontroller.tools.Tools;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Paths;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {

        MainFrame frame = new MainFrame();

        FileHelper.createDirectories();

        UIManager.put("TextField.caretForeground", new Color(0xFFFFFF));

        //region panels
        frame.addPanel(new PatchConfigEditingPanel());
        frame.addPanel(new HomePanel());
        //endregion
        frame.changePanel("home");
    }
}
