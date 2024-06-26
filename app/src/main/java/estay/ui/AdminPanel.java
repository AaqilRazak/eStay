package estay.ui;

import javax.swing.*;
import java.awt.*;

public class AdminPanel extends JPanel {
    public AdminPanel(HotelCheckInCheckOutUI parent) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Guest Name
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        add(new JLabel("Guest Name: John Doe"), gbc); // Placeholder guest name

        // Room Number
        gbc.gridx = 1;
        add(new JLabel("Room Number: 101"), gbc); // Placeholder room number

        // Current Check-In Status
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Current Checked-in status: Yes"), gbc); // Placeholder status

        // Flip Check-In Status Checkbox
        gbc.gridx = 1;
        JCheckBox flipStatusCheckBox = new JCheckBox("Flip checked-in status");
        add(flipStatusCheckBox, gbc);

        // List of Pending Services
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        String[] pendingServices = {"Service 1", "Service 2", "Service 3"}; // Placeholder services
        JList<String> servicesList = new JList<>(pendingServices);
        JScrollPane servicesScrollPane = new JScrollPane(servicesList);
        servicesScrollPane.setPreferredSize(new Dimension(200, 150));
        add(servicesScrollPane, gbc);

        // Dropdown Menu for Actions
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.weighty = 0;
        String[] actions = {"Action 1", "Action 2", "Action 3"}; // Placeholder actions
        JComboBox<String> actionsComboBox = new JComboBox<>(actions);
        add(actionsComboBox, gbc);

        // Submit Button
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            // Handle submit action here
        });
        add(submitButton, gbc);
    }
}