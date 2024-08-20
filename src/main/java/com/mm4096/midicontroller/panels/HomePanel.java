package com.mm4096.midicontroller.panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.mm4096.midicontroller.jobjects.Buttons;
import com.mm4096.midicontroller.jobjects.Fonts;
import com.mm4096.midicontroller.jobjects.Labels;

public class HomePanel extends BasePanel implements ActionListener {
    private final JButton patchEdit;
    private final JButton quitButton;

    public HomePanel() {
        this.id = "home";

        this.setBackground(Color.BLACK);

        BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
        this.setLayout(layout);

        this.add(Box.createRigidArea(new Dimension(0, 50)));

        JLabel label = new JLabel("MIDI Controller");
        label = Labels.styleLabelDefault(label);
        label.setFont(Fonts.getHeader());
        this.add(label);

        this.add(Box.createRigidArea(new Dimension(0, 50)));

        patchEdit = Buttons.getStyledButton("Edit/Create Patches");
        patchEdit.setAlignmentX(Component.CENTER_ALIGNMENT);
        patchEdit.addActionListener(this);
        this.add(patchEdit);

        this.add(Box.createRigidArea(new Dimension(0, 25)));
        quitButton = Buttons.getStyledButton("Quit");
        quitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        quitButton.addActionListener(this);
        this.add(quitButton);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource() == patchEdit) {
            this.getMainFrame().changePanel("patchConfigEditing");
        }
        if (actionEvent.getSource() == quitButton) {
            System.exit(0);
        }
    }

}
