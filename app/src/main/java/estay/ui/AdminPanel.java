package estay.ui;

import javax.swing.*;
import java.awt.*;

public class AdminPanel extends JPanel {

    private JTextField searchBar;
    private JCheckBox flipStatusCheckBox;
    private JList<String> servicesList;
    private JComboBox<String> actionsComboBox;

    public AdminPanel(HotelCheckInCheckOutUI parent) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Booking Code Label
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.EAST;
        add(new JLabel("Booking Code:"), gbc);

        // Search Bar
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        searchBar = new JTextField(25);
        searchBar.setToolTipText("Search...");
        add(searchBar, gbc);

        // Guest Name
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(new JLabel("Guest Name: John Doe"), gbc);

        // Room Number
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.EAST;
        add(new JLabel("Room Number: 101"), gbc);

        // Current Check-In Status
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(new JLabel("Current Checked-in status: Yes"), gbc);

        // Flip Check-In Status Checkbox
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.EAST;
        flipStatusCheckBox = new JCheckBox("Flip checked-in status");
        add(flipStatusCheckBox, gbc);

        // List of Pending Services
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        String[] pendingServices = {"Room Service", "Laundry Service", "Spa Service"};
        servicesList = new JList<>(pendingServices);
        JScrollPane servicesScrollPane = new JScrollPane(servicesList);
        servicesScrollPane.setPreferredSize(new Dimension(200, 150));
        add(servicesScrollPane, gbc);

        // Dropdown Menu for Actions
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.weighty = 0;
        String[] actions = {"Mark as Complete", "Cancel"};
        actionsComboBox = new JComboBox<>(actions);
        add(actionsComboBox, gbc);

        // Submit Button
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> handleSubmitAction());
        add(submitButton, gbc);

        // Exit Button
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> parent.showPanel("Login"));
        add(exitButton, gbc);
    }

    private void handleSubmitAction() {
        // Handle the submit action here
        String bookingCode = searchBar.getText().trim();
        boolean flipStatus = flipStatusCheckBox.isSelected();
        String selectedAction = (String) actionsComboBox.getSelectedItem();

        // Implement logic for handling booking and actions
        // For example:
        if (bookingCode.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a booking code.");
            return;
        }

        // Add logic for other functionalities
        JOptionPane.showMessageDialog(this, "Booking Code: " + bookingCode +
                "\nFlip Status: " + flipStatus +
                "\nSelected Action: " + selectedAction);
    }
}
