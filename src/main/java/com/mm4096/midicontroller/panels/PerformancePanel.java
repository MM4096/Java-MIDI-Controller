package com.mm4096.midicontroller.panels;

import com.mm4096.midicontroller.jobjects.Buttons;
import com.mm4096.midicontroller.jobjects.Fonts;
import com.mm4096.midicontroller.jobjects.Labels;
import com.mm4096.midicontroller.midi.MidiKeyboard;
import com.mm4096.midicontroller.parser.Parser;
import com.mm4096.midicontroller.parser.PerformanceItem;
import com.mm4096.midicontroller.tools.DialogCreator;
import com.mm4096.midicontroller.tools.FileHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class PerformancePanel extends BasePanel {

    private final JPanel fileSelectionPanel;
    private final ReordableListPanel fileOrderingPanel;

    private final JPanel instrumentSelectionPanel;
    private MidiKeyboard midiKeyboard;

    private final JPanel performancePanel;
    private final JPanel performanceListPanel;
    private final JLabel commentsLabel;

    private String[] performanceFiles;

    public PerformancePanel() {
        this.id = "performance";

        this.setBackground(Color.BLACK);

        BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
        this.setLayout(layout);

        this.add(Box.createRigidArea(new Dimension(0, 50)));

        JLabel label = new JLabel("Performance Mode");
        Labels.styleLabelDefault(label).setFont(Fonts.getHeader());
        this.add(label);

        fileSelectionPanel = new JPanel();
        fileSelectionPanel.setBackground(Color.BLACK);
        fileSelectionPanel.setLayout(new BoxLayout(fileSelectionPanel, BoxLayout.Y_AXIS));

        JLabel fileSelectionLabel = new JLabel("Select Performance Files");
        Labels.styleLabelDefault(fileSelectionLabel);
        fileSelectionPanel.add(fileSelectionLabel);

        JButton multiFileButton = getMultiFileButton();
        multiFileButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        fileSelectionPanel.add(multiFileButton);

        fileSelectionPanel.setVisible(true);

        this.add(fileSelectionPanel);

        fileOrderingPanel = new ReordableListPanel(new String[]{"Patch Name"}, new String[][]{}, false, true);

        JScrollPane scrollPane = new JScrollPane(fileOrderingPanel);
        scrollPane.setBackground(Color.BLACK);
        scrollPane.createVerticalScrollBar();

        fileSelectionPanel.add(scrollPane);

        instrumentSelectionPanel = new JPanel();
        instrumentSelectionPanel.setBackground(Color.BLACK);
        instrumentSelectionPanel.setLayout(new BoxLayout(instrumentSelectionPanel, BoxLayout.Y_AXIS));
        updateInstrumentSelectionPanel();

        this.add(instrumentSelectionPanel);
        instrumentSelectionPanel.setVisible(false);

        this.add(Box.createRigidArea(new Dimension(0, 50)));
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.BLACK);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton startButton = Buttons.getStyledButton("Start");
        startButton.addActionListener(e -> {
            if (performanceFiles != null) {
                String[][] new_order = fileOrderingPanel.getContents();
                String[] new_order_files = new String[new_order.length];
                for (int i = 0; i < new_order.length; i++) {
                    new_order_files[i] = FileHelper.getPatchPath(new_order[i][0]);
                }
                System.out.println("Starting performance mode with files: " + Arrays.toString(new_order_files));
                fileSelectionPanel.setVisible(false);
                instrumentSelectionPanel.setVisible(true);
            } else {
                System.out.println("No files selected");
            }
        });

        fileSelectionPanel.add(buttonPanel);

        buttonPanel.add(startButton);

        //region Performance Panel
        performancePanel = new JPanel();
        performancePanel.setBackground(Color.BLACK);
        performancePanel.setLayout(new BorderLayout());

        performanceListPanel = new JPanel();
        performanceListPanel.setBackground(Color.BLACK);
        performanceListPanel.setLayout(new BoxLayout(performanceListPanel, BoxLayout.Y_AXIS));
        performanceListPanel.setPreferredSize(new Dimension(100, 1000));

        JPanel commentsPanel = new JPanel();
        commentsPanel.setPreferredSize(new Dimension(750, 1000));
        commentsPanel.setBackground(Color.BLACK);
        System.out.println(commentsPanel.getMaximumSize());
        commentsPanel.setLayout(new BoxLayout(commentsPanel, BoxLayout.Y_AXIS));
        JLabel commentsHeader = new JLabel("Comments");
        commentsLabel = new JLabel("");
        Labels.styleLabelDefault(commentsLabel);
        Labels.styleLabelDefault(commentsHeader);
        commentsHeader.setAlignmentX(Component.CENTER_ALIGNMENT);

        commentsPanel.add(commentsHeader);
        commentsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        commentsPanel.add(commentsLabel);

        performancePanel.add(performanceListPanel, BorderLayout.CENTER);
        performancePanel.add(commentsPanel, BorderLayout.EAST);

        this.add(performancePanel);
        performancePanel.setVisible(false);

        //endregion
    }

    private JButton getMultiFileButton() {
        JButton multiFileButton = Buttons.getStyledButton("Select Multiple Files");
        multiFileButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setMultiSelectionEnabled(true);
            fileChooser.setCurrentDirectory(new File(FileHelper.getPatchDirectory()));

            int option = fileChooser.showOpenDialog(null);
            if (option == JFileChooser.APPROVE_OPTION) {
                File[] files = fileChooser.getSelectedFiles();
                performanceFiles = new String[files.length];
                for (int i = 0; i < files.length; i++) {
                    performanceFiles[i] = files[i].getAbsolutePath();
                }
                fileOrderingPanel.setVisible(true);
                fileOrderingPanel.insertRows(Arrays.stream(performanceFiles).map(file -> new String[]{FileHelper.removePatchPath(file)}).toArray(String[][]::new));
            } else{
                System.out.println("User exited");
            }
        });
        return multiFileButton;
    }

    private void updateInstrumentSelectionPanel() {
        instrumentSelectionPanel.removeAll();

        JLabel deviceSelectionLabel = new JLabel("Select MIDI Device");
        Labels.styleLabelDefault(deviceSelectionLabel);
        deviceSelectionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        instrumentSelectionPanel.add(deviceSelectionLabel);
        instrumentSelectionPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        for (String device : MidiKeyboard.getMidiDevices()) {
            JButton deviceButton = Buttons.getStyledButton(device);
            deviceButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            deviceButton.addActionListener(e -> {
                try {
                    midiKeyboard = MidiKeyboard.getMidiKeyboard(device);
                    instrumentSelectionPanel.setVisible(false);
                    performancePanel.setVisible(true);
                    startPerformanceMode();
                } catch (Exception ex) {
                    DialogCreator.showErrorDialog("Error", "Couldn't find device: " + device);
                }
            });
            instrumentSelectionPanel.add(deviceButton);
            instrumentSelectionPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        instrumentSelectionPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        JButton refreshButton = Buttons.getStyledButton("Refresh Device List");
        refreshButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        refreshButton.addActionListener(e -> updateInstrumentSelectionPanel());
        instrumentSelectionPanel.add(refreshButton);

        instrumentSelectionPanel.revalidate();
        instrumentSelectionPanel.repaint();
    }

    private int currentFileIndex = 0;
    private int currentPatchIndex = 0;
    private void startPerformanceMode() {
        try {
            updatePerformancePanel();
        } catch (Exception e) {
            DialogCreator.showErrorDialog("Error", "Error while starting performance mode: " + e);
        }

        performanceListPanel.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {

            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {
                ArrayList<Integer> progressKeys = new ArrayList<>(Arrays.asList(KeyEvent.VK_SPACE, KeyEvent.VK_ENTER, KeyEvent.VK_RIGHT));
                ArrayList<Integer> backKeys = new ArrayList<>(Arrays.asList(KeyEvent.VK_BACK_SPACE, KeyEvent.VK_LEFT));
                if (backKeys.contains(keyEvent.getKeyCode())) {
                    currentPatchIndex--;
                    updatePerformancePanel();
                } else if (progressKeys.contains(keyEvent.getKeyCode())) {
                    currentPatchIndex++;
                    updatePerformancePanel();
                }
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {

            }
        });
        performanceListPanel.setFocusable(true);
        performanceListPanel.requestFocus();
    }

    private void updatePerformancePanel() {
        String thisPatch = performanceFiles[currentFileIndex];
        String thisPatchData = FileHelper.readFromFile(thisPatch);

        PerformanceItem[] performanceList = new PerformanceItem[0];
        Parser parser = new Parser(thisPatchData);
        try {
            performanceList = parser.getPerformanceList();

            if (currentPatchIndex >= performanceList.length) {
                currentPatchIndex = 0;
                currentFileIndex++;
                if (currentFileIndex >= performanceFiles.length) {
                    DialogCreator.showInfoDialog("Performance Mode", "Finished performance mode");
                    return;
                }
                thisPatch = performanceFiles[currentFileIndex];
                thisPatchData = FileHelper.readFromFile(thisPatch);
                parser = new Parser(thisPatchData);
                performanceList = parser.getPerformanceList();
            }
            else if (currentPatchIndex < 0) {
                if (currentFileIndex > 0) {
                    currentFileIndex--;
                    thisPatch = performanceFiles[currentFileIndex];
                    thisPatchData = FileHelper.readFromFile(thisPatch);
                    parser = new Parser(thisPatchData);
                    performanceList = parser.getPerformanceList();
                    currentPatchIndex = performanceList.length - 1;
                }
                else {
                    currentPatchIndex = 0;
                }
            }
        } catch (Exception e) {
            DialogCreator.showErrorDialog("Error", "Error while updating performance panel: " + e);
            return;
        }

        performanceListPanel.removeAll();

        JLabel headerLabel = new JLabel("Current Patch: " + FileHelper.removePatchPath(thisPatch));
        Labels.styleLabelDefault(headerLabel);
        headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        performanceListPanel.add(headerLabel);

        JButton previousButton = Buttons.getStyledButton("Previous");
        previousButton.addActionListener(e -> {
            currentPatchIndex--;
            updatePerformancePanel();
        });
        previousButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        performanceListPanel.add(previousButton);

        PerformanceItem currentPerformanceItem = performanceList[currentPatchIndex];
        for (int i = 0; i < performanceList.length; i++) {
            PerformanceItem performanceItem = performanceList[i];
            JButton thisButton = Buttons.getStyledButton(performanceItem.getPatchName());
            int finalI = i;
            thisButton.addActionListener(e -> {
                currentPatchIndex = finalI;
                updatePerformancePanel();
            });
            thisButton.setSize(new Dimension(200, 50));
            thisButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            thisButton.setForeground(i == currentPatchIndex ? Color.GREEN : Color.WHITE);
            performanceListPanel.add(thisButton);
        }
        try {
            midiKeyboard.changeInstrument(currentPerformanceItem.getPatch(), true);
        }
        catch (Exception e) {
            DialogCreator.showErrorDialog("Error", "Error while changing instrument: " + e);
        }

        performanceListPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JButton nextButton = Buttons.getStyledButton("Next");
        nextButton.addActionListener(e -> {
            currentPatchIndex++;
            updatePerformancePanel();
        });

        nextButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        performanceListPanel.add(nextButton);

        commentsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        commentsLabel.setText(currentPerformanceItem.getComments());

        performanceListPanel.requestFocus();
        performanceListPanel.revalidate();
        performanceListPanel.repaint();
    }
}
