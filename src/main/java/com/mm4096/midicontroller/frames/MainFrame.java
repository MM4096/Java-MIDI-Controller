package com.mm4096.midicontroller.frames;

import com.mm4096.midicontroller.panels.BasePanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class MainFrame extends JFrame implements ActionListener {
    public ArrayList<BasePanel> panels = new ArrayList<>();
    private JFrame frame;

    public MainFrame() {
        frame = new JFrame();
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);

        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setUndecorated(true);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setTitle("Midi Controller");
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {

    }

    public void addPanel(BasePanel panel) {
        for (BasePanel p : panels) {
            if (p.id.equals(panel.id)) {
                return;
            }
        }
        panel.setMainFrame(this);
        panels.add(panel);

        frame.revalidate();
        frame.repaint();
    }

    public void removePanel(BasePanel panel) {
        for (BasePanel p : panels) {
            if (p.id.equals(panel.id)) {
                panels.remove(p);
                return;
            }
        }
    }

    public BasePanel getPanel(String id) {
        for (BasePanel p : panels) {
            if (p.id.equals(id)) {
                return p;
            }
        }
        return null;
    }

    public void changePanel(String id) {
        boolean found = false;
        for (BasePanel p : panels) {
            frame.remove(p);

            if (p.id.equals(id)) {
                p.setVisible(true);
                frame.add(p);
                found = true;
            }
        }

        if (!found) {
            System.out.println("Panel not found, using first panel instead");
            frame.add(panels.get(0));
        }

        frame.revalidate();
        frame.repaint();
    }
}
