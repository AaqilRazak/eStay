package estay.ui;

import javax.swing.*;
import java.awt.*;

public class KeyCodePanel extends JPanel {
    private HotelCheckInCheckOutUI parent;
    private JTextField keyCodeField;
    private JButton submitButton;

    public KeyCodePanel(HotelCheckInCheckOutUI parent) {
        this.parent = parent;
        initializeComponents();
        layoutComponents();
    }

    private void initializeComponents() {
        keyCodeField = new JTextField(20);
        submitButton = new JButton("Submit");

        submitButton.addActionListener(e -> handleKeyCodeSubmission());
    }

    private void layoutComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        add(new JLabel("Enter Key Code:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(keyCodeField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(submitButton, gbc);
    }

    private void handleKeyCodeSubmission() {
        String keyCode = keyCodeField.getText();
        if (keyCode.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Key Code cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            // Handle the key code submission logic here
            JOptionPane.showMessageDialog(this, "Key Code submitted: " + keyCode, "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}