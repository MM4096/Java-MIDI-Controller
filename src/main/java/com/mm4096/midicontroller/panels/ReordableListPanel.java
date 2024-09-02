package com.mm4096.midicontroller.panels;

import com.mm4096.midicontroller.jobjects.Buttons;
import com.mm4096.midicontroller.jobjects.Labels;
import com.mm4096.midicontroller.jobjects.TextField;

import javax.swing.*;
import java.awt.*;

public class ReordableListPanel extends JPanel {

    private final String[] headers;
    private final boolean isEditable;
    private final boolean allowDeletion;

    private JTextField[] textFields;

    private String[][] contents;

    /**
     * Creates a ReordableListPanel with the given headers and contents
     *
     * @param headers           The headers for the table. This <b>MUST</b> match the number of columns in the contents.
     * @param contents          The contents for the table. This <b>MUST</b> match the number of headers.
     * @param isEditable        Whether the table can have new elements added to it.
     * @param allowDeletion     Whether the table can have elements removed from it.
     */
    public ReordableListPanel(String[] headers, String[][] contents, boolean isEditable, boolean allowDeletion) {
        if (contents.length > 0 && headers.length != contents[0].length) {
            throw new IllegalArgumentException("Headers and contents must have the same length");
        }
        this.isEditable = isEditable;
        this.allowDeletion = allowDeletion;

        this.headers = headers;
        this.contents = contents;

        this.setBackground(Color.BLACK);

        updateGrid();
    }

    private void updateGrid() {
        this.removeAll();

        int cols = headers.length;
        // 1 for moving up, 1 for moving down
        int gridCols = cols + 2;

        if (allowDeletion) {
            gridCols += 1;
        }

        this.setLayout(new GridLayout(0, gridCols));

        int extraCols = gridCols - cols;

        GridLayout layout = new GridLayout(0, gridCols);
        this.setLayout(layout);

        String[] additionalHeaders = new String[]{"Move Up", "Move Down", "Delete"};

        for (int i = 0; i < gridCols; i++) {
            if (i >= headers.length) {
                this.add(Labels.styleLabelDefault(new JLabel(additionalHeaders[i - headers.length])));
            }
            else {
                String header = headers[i];
                JLabel label = Labels.styleLabelDefault(new JLabel(header));
                label.setAlignmentX(Component.CENTER_ALIGNMENT);
                this.add(label);
            }
        }

        if (isEditable) {
            textFields = new JTextField[cols];
            for (int i = 0; i < cols; i++) {
                JTextField thisTextField = TextField.getStyledTextField();
                textFields[i] = thisTextField;
                this.add(thisTextField);
            }
            JButton addButton = Buttons.getStyledButton("Add");
            addButton.addActionListener(e -> {
                String[] row = new String[headers.length];
                for (int i = 0; i < headers.length; i++) {
                    row[i] = textFields[i].getText();
                }
                insertRow(row);
            });
            this.add(addButton);
            for (int i = 0; i < extraCols - 1; i++) {
                this.add(new JLabel());
            }
        }

        for (int i = 0; i < contents.length; i++) {
            String[] row = contents[i];
            for (String cell : row) {
                JLabel label = Labels.styleLabelDefault(new JLabel(cell));
                this.add(label);
            }

            JButton moveUpButton = Buttons.getStyledButton("Move Up");
            int finalI = i;
            moveUpButton.addActionListener(e -> {
                if (finalI > 0) {
                    String[] temp = contents[finalI - 1];
                    contents[finalI - 1] = contents[finalI];
                    contents[finalI] = temp;
                    updateGrid();
                }
            });
            this.add(moveUpButton);

            JButton moveDownButton = Buttons.getStyledButton("Move Down");
            moveDownButton.addActionListener(e -> {
                if (finalI < contents.length - 1) {
                    String[] temp = contents[finalI + 1];
                    contents[finalI + 1] = contents[finalI];
                    contents[finalI] = temp;
                    updateGrid();
                }
            });
            this.add(moveDownButton);

            if (allowDeletion) {
                JButton deleteButton = Buttons.getStyledButton("Delete");
                deleteButton.addActionListener(e -> {
                    String[][] newContents = new String[contents.length - 1][headers.length];
                    int j = 0;
                    for (int k = 0; k < contents.length; k++) {
                        if (k != finalI) {
                            newContents[j] = contents[k];
                            j++;
                        }
                    }
                    contents = newContents;
                    updateGrid();
                });
                this.add(deleteButton);
            }
        }

        this.revalidate();
        this.repaint();
    }


    public void setContents(String[][] contents) {
        this.contents = contents;
        updateGrid();
    }

    public void insertRow(String[] row) {
        if (row.length != headers.length) {
            throw new IllegalArgumentException("Row must have the same length as headers");
        }
        String[][] newContents = new String[contents.length + 1][headers.length];
        System.arraycopy(contents, 0, newContents, 0, contents.length);
        newContents[contents.length] = row;
        contents = newContents;
        updateGrid();
    }

    public void insertRows(String[][] rows) {
        for (String[] row : rows) {
            insertRow(row);
        }
        updateGrid();
    }

    public String[][] getContents() {
        return contents;
    }
}
