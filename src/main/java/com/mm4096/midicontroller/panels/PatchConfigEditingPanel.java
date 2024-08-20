package com.mm4096.midicontroller.panels;

import com.mm4096.midicontroller.classes.ConfigClass;
import com.mm4096.midicontroller.classes.PatchClass;
import com.mm4096.midicontroller.classes.PatchListClass;
import com.mm4096.midicontroller.jobjects.Buttons;
import com.mm4096.midicontroller.jobjects.Fonts;
import com.mm4096.midicontroller.jobjects.Labels;
import com.mm4096.midicontroller.jobjects.TextField;
import com.mm4096.midicontroller.parser.CompileException;
import com.mm4096.midicontroller.parser.Compiler;
import com.mm4096.midicontroller.parser.Parser;
import com.mm4096.midicontroller.tools.*;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PatchConfigEditingPanel extends BasePanel implements ActionListener {
    private final JButton fileSelect;
    private final JButton fileCreate;
    private final JLabel fileSelected;
    private final JPanel configEditingPanel;
    private final JPanel patchEditingPanel;
    private String selectedFile;

    public PatchConfigEditingPanel() {
        this.id = "patchConfigEditing";

        this.setBackground(Color.BLACK);

        BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
        this.setLayout(layout);

        this.add(Box.createRigidArea(new Dimension(0, 50)));

        JLabel label = new JLabel("Patch/Config Editing Panel");
        Labels.styleLabelDefault(label).setFont(Fonts.getHeader());
        this.add(label);

        this.add(Box.createRigidArea(new Dimension(0, 50)));

        fileSelected = new JLabel("No file selected");
        Labels.styleLabelDefault(fileSelected).setFont(Fonts.getBody());
        fileSelected.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.add(fileSelected);

        JPanel horizontalButtonPanel = new JPanel();
        horizontalButtonPanel.setLayout(new BoxLayout(horizontalButtonPanel, BoxLayout.X_AXIS));
        horizontalButtonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.add(horizontalButtonPanel);

        fileSelect = Buttons.getStyledButton("Select File");
        fileSelect.setAlignmentX(Component.CENTER_ALIGNMENT);
        fileSelect.addActionListener(this);
        horizontalButtonPanel.add(fileSelect);

        fileCreate = Buttons.getStyledButton("Create File");
        fileCreate.setAlignmentX(Component.CENTER_ALIGNMENT);
        fileCreate.addActionListener(this);
        horizontalButtonPanel.add(fileCreate);

        this.add(Box.createRigidArea(new Dimension(0, 50)));

        JTabbedPane tabbedPane = new JTabbedPane();

        configEditingPanel = new JPanel();
        configEditingPanel.setBackground(Color.BLACK);
        configEditingPanel.setLayout(new BoxLayout(configEditingPanel, BoxLayout.Y_AXIS));
        configEditingPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        tabbedPane.addTab("Config Editing", configEditingPanel);

        patchEditingPanel = new JPanel();
        patchEditingPanel.setBackground(Color.BLACK);
        patchEditingPanel.setLayout(new BoxLayout(patchEditingPanel, BoxLayout.Y_AXIS));
        patchEditingPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        tabbedPane.addTab("Patch Editing", patchEditingPanel);

        this.add(tabbedPane);

        this.add(Box.createRigidArea(new Dimension(0, 10)));

        JPanel saveBackPanel = new JPanel();
        saveBackPanel.setLayout(new BoxLayout(saveBackPanel, BoxLayout.X_AXIS));

        JButton backButton = Buttons.getStyledButton("Back");
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                boolean confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to discard changes?", "Discard Changes", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;

                if (!confirm) {
                    return;
                }
                discardChanges();
                getMainFrame().changePanel("home");
            }
        });
        saveBackPanel.add(backButton);

        JButton saveButton = Buttons.getStyledButton("Save Changes");
        saveButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                saveChanges();
            }
        });
        saveBackPanel.add(saveButton);

        this.add(saveBackPanel);


        clearPanels();
    }

    private void saveChanges() {
        if (FileHelper.getExtension(selectedFile).equals("midipatch")) {
            String patch_name = FileHelper.readFromFile(Paths.get(FileHelper.getTempDirectory(), "patch_name_temp").toString());
            String patch_content = FileHelper.readFromFile(Paths.get(FileHelper.getTempDirectory(), "patch_temp").toString());
            String config_content = FileHelper.readFromFile(Paths.get(FileHelper.getTempDirectory(), "config_temp").toString());
            String content = patch_name + "\n" +
                    "startconfig\n" +
                    config_content + "\n" +
                    "endconfig\n" +
                    "startlist\n" +
                    patch_content + "\n" +
                    "endlist\n";

            FileHelper.writeToFile(selectedFile, content);
        }
        else if (FileHelper.getExtension(selectedFile).equals("midiconfig")) {
            FileHelper.writeToFile(selectedFile, FileHelper.readFromFile(Paths.get(FileHelper.getTempDirectory(), "config_temp").toString()));
        }
    }

    private void discardChanges() {
        // delete temp file
        try {
            Files.delete(Paths.get(FileHelper.getTempDirectory(), "config_temp"));
        } catch (IOException e) {
            System.out.println("No temp file to delete");
        }
        try {
            Files.delete(Paths.get(FileHelper.getTempDirectory(), "patch_temp"));
        } catch (IOException e) {
            System.out.println("No temp file to delete");
        }
        try {
            Files.delete(Paths.get(FileHelper.getTempDirectory(), "patch_name_temp"));
        } catch (IOException e) {
            System.out.println("No temp file to delete");
        }
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource() == fileSelect) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setCurrentDirectory(new File(FileHelper.getUserDataDirectory()));
            fileChooser.setFileFilter(new FileNameExtensionFilter("Midi Controller Patch or Config File", "midipatch", "midiconfig"));
            fileChooser.showOpenDialog(this);

            if (fileChooser.getSelectedFile() != null) {
                fileSelected.setText(String.format("File selected: %s", fileChooser.getSelectedFile().getName()));
                selectedFile = fileChooser.getSelectedFile().getAbsolutePath();
            }
        }
        else if (actionEvent.getSource() == fileCreate) {
            boolean isPatch = JOptionPane.showConfirmDialog(this,
                    "Do you want to create a patch file? (if not, creates a config file instead)",
                    "File Type", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setCurrentDirectory(new File(isPatch ? FileHelper.getPatchDirectory() : FileHelper.getConfigDirectory()));
            fileChooser.setFileFilter(new FileNameExtensionFilter("Midi Controller Patch or Config File", "midipatch", "midiconfig"));
            fileChooser.setDialogTitle("Select a directory to save the file");
            fileChooser.showSaveDialog(this);

            if (fileChooser.getSelectedFile() != null) {
                String fileName = fileChooser.getSelectedFile().getAbsolutePath();
                if (!fileName.endsWith(".midipatch") && !fileName.endsWith(".midiconfig")) {
                    fileName += isPatch ? ".midipatch" : ".midiconfig";
                }
                selectedFile = fileName;
                fileSelected.setText(String.format("New File: %s", fileName));
            }
            else {
                return;
            }
        }

        if (FileHelper.getExtension(selectedFile).equals("midipatch")) {
            clearPanels();
            editPatch();
        }
        else if (FileHelper.getExtension(selectedFile).equals("midiconfig")) {
            clearPanels();
            editConfig();
        }
    }

    private void editConfig() {
        JScrollPane scrollPane = setUpConfigScrollPane();

        if (FileHelper.fileExists(selectedFile)) {
            Parser parser = new Parser(FileHelper.readFromFile(selectedFile));
            ConfigClass[] configs = parser.parseAsConfig();
            updateConfig(scrollPane, configs);
        }
        else {
            updateConfig(scrollPane, new ConfigClass[0]);
        }
    }

    private JScrollPane setUpConfigScrollPane() {
        configEditingPanel.removeAll();

        JPanel dataPanel = new JPanel();
        dataPanel.setLayout(new BoxLayout(dataPanel, BoxLayout.Y_AXIS));
        dataPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // scroll dataPanel
        JScrollPane scrollPane = new JScrollPane(dataPanel);
        scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        scrollPane.setBackground(Color.BLACK);
        scrollPane.createVerticalScrollBar();

        configEditingPanel.add(scrollPane);

        return scrollPane;
    }

    private void updateConfig(JScrollPane scrollPane, ConfigClass[] configs) {
        Compiler compiler = new Compiler(configs);
        String content;
        try {
            content = compiler.compile();
        } catch (CompileException e) {
            DialogCreator.showErrorDialog("Error", "Error compiling config");
            return;
        }
        FileHelper.writeToFile(Paths.get(FileHelper.getTempDirectory(), "config_temp").toString(), content);

        // region add JObjects
        JPanel dataPanel = new JPanel();
        dataPanel.setBackground(Color.BLACK);
        dataPanel.setLayout(new GridLayout(0, 3));
        dataPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        dataPanel.add(Labels.styleLabelDefault(new JLabel("Alias")));
        dataPanel.add(Labels.styleLabelDefault(new JLabel("Bank ID")));
        dataPanel.add(Labels.styleLabelDefault(new JLabel("Action")));

        // add field
        JTextField newAliasField = new JTextField();
        newAliasField.setBackground(new Color(0x333333));
        newAliasField.setForeground(Color.WHITE);
        dataPanel.add(newAliasField);

        // combine bank with an add button

        JTextField newBankField = new JTextField();
        newBankField.setBackground(new Color(0x333333));
        newBankField.setForeground(Color.WHITE);
        dataPanel.add(newBankField);

        JButton newButton = Buttons.getStyledButton("Add");
        newButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ArrayList<ConfigClass> list = new ArrayList<>(List.of(configs));
                String alias = newAliasField.getText();
                String bankField = "";
                try {
                    Integer.parseInt(newBankField.getText());
                    bankField = newBankField.getText();
                } catch (NumberFormatException e) {
                    if (Objects.equals(alias, "preset")) {
                        bankField = newBankField.getText();
                    } else {
                        DialogCreator.showErrorDialog("Error", "Bank ID must be a number, or alias must be [preset]");
                        return;
                    }
                }
                if (Objects.equals(bankField, "") || alias.isEmpty()) {
                    DialogCreator.showErrorDialog("Error", "Alias and Bank ID must be filled out");
                    return;
                }

                list.add(new ConfigClass(alias, bankField));
                ConfigClass[] arr = new ConfigClass[list.size()];
                arr = list.toArray(arr);
                updateConfig(scrollPane, arr);
            }
        });
        dataPanel.add(newButton);

        for (int i = 0; i < configs.length; i++) {
            ConfigClass config = configs[i];
            final int finalI = i;

            JTextField aliasField = new JTextField(config.getAlias());
            aliasField.setBackground(new Color(0x000000));
            aliasField.setForeground(Color.WHITE);
            aliasField.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    ArrayList<ConfigClass> list = new ArrayList<>(List.of(configs));
                    ConfigClass newConfig = new ConfigClass(aliasField.getText(), config.getBankId());
                    list.set(finalI, newConfig);
                    ConfigClass[] arr = new ConfigClass[list.size()];
                    arr = list.toArray(arr);
                    updateConfig(scrollPane, arr);
                }
            });
            dataPanel.add(aliasField);

            JTextField bankField = new JTextField(String.valueOf(config.getBankId()));
            bankField.setBackground(new Color(0x000000));
            bankField.setForeground(Color.WHITE);
            bankField.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    ArrayList<ConfigClass> list = new ArrayList<>(List.of(configs));
                    ConfigClass newConfig = new ConfigClass(aliasField.getText(), config.getBankId());
                    list.set(finalI, newConfig);
                    ConfigClass[] arr = new ConfigClass[list.size()];
                    arr = list.toArray(arr);
                    updateConfig(scrollPane, arr);
                }
            });
            dataPanel.add(bankField);

            JButton deleteButton = Buttons.getStyledButton("Delete");
            deleteButton.addActionListener(actionEvent -> {

                boolean confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this config?", "Delete Config", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
                if (!confirm) {
                    return;
                }

                ArrayList<ConfigClass> list = new ArrayList<>(List.of(configs));
                list.remove(config);
                ConfigClass[] arr = new ConfigClass[list.size()];
                arr = list.toArray(arr);
                updateConfig(scrollPane, arr);
            });
            dataPanel.add(deleteButton);
        }

        scrollPane.setViewportView(dataPanel);
        // endregion
    }

    private void editPatch() {
        PatchListClass patchListClass = new PatchListClass();
        if (FileHelper.fileExists(selectedFile)) {
            Parser parser = new Parser(FileHelper.readFromFile(selectedFile));
            try {
                patchListClass = parser.parseAsPatchFile();
            } catch (Exception e) {
                DialogCreator.showErrorDialog("Corrupt File", "Patch file could not be parsed!");
                return;
            }
            JScrollPane scrollPane = setUpConfigScrollPane();
            updateConfig(scrollPane, patchListClass.getConfigList());
        }
        patchEditingPanel.removeAll();
        patchEditingPanel.setLayout(new BoxLayout(patchEditingPanel, BoxLayout.Y_AXIS));

        JPanel nameFieldPanel = new JPanel();
        nameFieldPanel.setBackground(Color.BLACK);
        nameFieldPanel.setLayout(new BoxLayout(nameFieldPanel, BoxLayout.X_AXIS));

        nameFieldPanel.add(Labels.styleLabelDefault(new JLabel("Patch Name: ")));

        JTextField patchNameField = new JTextField(patchListClass.getName());
        patchNameField.setBackground(new Color(0x333333));
        patchNameField.setForeground(Color.WHITE);
        patchNameField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent documentEvent) {
                updatePatchName(patchNameField.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent documentEvent) {
                updatePatchName(patchNameField.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent documentEvent) {
                updatePatchName(patchNameField.getText());
            }
        });
        updatePatchName(patchNameField.getText());
        nameFieldPanel.add(patchNameField);

        patchEditingPanel.add(nameFieldPanel);
        patchEditingPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        patchEditingPanel.add(Labels.styleLabelDefault(new JLabel("Patch List")));
        patchEditingPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JPanel dataPanel = new JPanel();
        dataPanel.setBackground(Color.BLACK);
        dataPanel.setLayout(new GridLayout(0, 5));
        dataPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JScrollPane scrollPane = new JScrollPane(dataPanel);
        scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        scrollPane.setBackground(Color.BLACK);
        scrollPane.createVerticalScrollBar();

        patchEditingPanel.add(scrollPane);

        updatePatchList(dataPanel, patchListClass.getPatchList());
    }

    private void updatePatchList(JPanel dataPanel, PatchClass[] patchClasses) {
        dataPanel.removeAll();
        Compiler compiler = new Compiler(patchClasses);
        String content;
        try {
            content = compiler.compile();
        } catch (CompileException e) {
            DialogCreator.showErrorDialog("Error", "Error compiling patch: Corrupted config");
            return;
        }
        FileHelper.writeToFile(Paths.get(FileHelper.getTempDirectory(), "patch_temp").toString(), content);

        dataPanel.add(Labels.styleLabelDefault(new JLabel("Patch Name")));
        dataPanel.add(Labels.styleLabelDefault(new JLabel("Comments")));
        dataPanel.add(Labels.styleLabelDefault(new JLabel("Action")));
        dataPanel.add(Labels.styleLabelDefault(new JLabel("Move Up")));
        dataPanel.add(Labels.styleLabelDefault(new JLabel("Move Down")));

        JTextField newPatchName = TextField.getStyledTextField();
        dataPanel.add(newPatchName);

        JTextField newPatchComments = TextField.getStyledTextField();
        dataPanel.add(newPatchComments);

        JButton newPatchButton = Buttons.getStyledButton("Add");
        newPatchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ArrayList<PatchClass> list = new ArrayList<>(List.of(patchClasses));
                String patchName = newPatchName.getText();
                if (patchName.isEmpty()) {
                    DialogCreator.showErrorDialog("Error", "Patch name must be filled out");
                    return;
                }
                String comments = newPatchComments.getText();
                list.add(new PatchClass(patchName, comments));
                PatchClass[] arr = new PatchClass[list.size()];
                arr = list.toArray(arr);
                newPatchName.setText("");
                newPatchComments.setText("");

                updatePatchList(dataPanel, arr);
            }
        });
        dataPanel.add(newPatchButton);

        dataPanel.add(new JLabel());
        dataPanel.add(new JLabel());

        for (int i = 0; i < patchClasses.length; i++) {
            PatchClass patch = patchClasses[i];
            final int finalI = i;

            JTextField patchNameField = TextField.getStyledTextField();
            patchNameField.setText(patch.getSound());
            patchNameField.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    ArrayList<PatchClass> list = new ArrayList<>(List.of(patchClasses));
                    PatchClass newPatch = new PatchClass(patchNameField.getText(), patch.getComments());
                    list.set(finalI, newPatch);
                    PatchClass[] arr = new PatchClass[list.size()];
                    arr = list.toArray(arr);
                    updatePatchList(dataPanel, arr);
                }
            });
            dataPanel.add(patchNameField);

            JTextField commentsField = TextField.getStyledTextField();
            commentsField.setText(patch.getComments());
            commentsField.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    ArrayList<PatchClass> list = new ArrayList<>(List.of(patchClasses));
                    PatchClass newPatch = new PatchClass(patch.getSound(), commentsField.getText());
                    list.set(finalI, newPatch);
                    PatchClass[] arr = new PatchClass[list.size()];
                    arr = list.toArray(arr);
                    updatePatchList(dataPanel, arr);
                }
            });
            dataPanel.add(commentsField);

            JButton deleteButton = Buttons.getStyledButton("Delete");
            deleteButton.addActionListener(actionEvent -> {

                boolean confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this item?", "Delete Patch", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
                if (!confirm) {
                    return;
                }

                ArrayList<PatchClass> list = new ArrayList<>(List.of(patchClasses));
                list.remove(patch);
                PatchClass[] arr = new PatchClass[list.size()];
                arr = list.toArray(arr);
                updatePatchList(dataPanel, arr);
            });
            dataPanel.add(deleteButton);

            JButton moveUpButton = Buttons.getStyledButton("Move Up");
            moveUpButton.addActionListener(actionEvent -> {
                if (finalI == 0) {
                    return;
                }
                ArrayList<PatchClass> list = new ArrayList<>(List.of(patchClasses));
                PatchClass temp = list.get(finalI - 1);
                list.set(finalI - 1, patch);
                list.set(finalI, temp);
                PatchClass[] arr = new PatchClass[list.size()];
                arr = list.toArray(arr);
                updatePatchList(dataPanel, arr);
            });
            dataPanel.add(moveUpButton);


            JButton moveDownButton = Buttons.getStyledButton("Move Down");
            moveDownButton.addActionListener(actionEvent -> {
                if (finalI == patchClasses.length - 1) {
                    return;
                }
                ArrayList<PatchClass> list = new ArrayList<>(List.of(patchClasses));
                PatchClass temp = list.get(finalI + 1);
                list.set(finalI + 1, patch);
                list.set(finalI, temp);
                PatchClass[] arr = new PatchClass[list.size()];
                arr = list.toArray(arr);
                updatePatchList(dataPanel, arr);
            });
            dataPanel.add(moveDownButton);
        }

        dataPanel.revalidate();
        dataPanel.repaint();
    }

    private void updatePatchName(String name) {
        FileHelper.writeToFile(Paths.get(FileHelper.getTempDirectory(), "patch_name_temp").toString(), name);
    }

    private void clearPanels() {
        configEditingPanel.removeAll();
        patchEditingPanel.removeAll();

        configEditingPanel.add(Labels.styleLabelDefault(new JLabel("No Config To Be Edited. Try selecting a file.")));
        patchEditingPanel.add(Labels.styleLabelDefault(new JLabel("No Patch To Be Edited. Try selecting a patch.")));
    }
}
