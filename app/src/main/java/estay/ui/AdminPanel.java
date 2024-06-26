package estay.ui;

import javax.swing.*;
import java.awt.*;

public class AdminPanel extends JPanel {
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
        gbc.fill = GridBagConstraints.HORIZONTAL; // Make the search bar span across the width
        JTextField searchBar = new JTextField(25); // Adjust the width of the search bar
        searchBar.setToolTipText("Search...");
        add(searchBar, gbc);

        // Guest Name
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(new JLabel("Guest Name: John Doe"), gbc); // Placeholder guest name

        // Room Number
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.EAST;
        add(new JLabel("Room Number: 101"), gbc); // Placeholder room number

        // Current Check-In Status
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(new JLabel("Current Checked-in status: Yes"), gbc); // Placeholder status

        // Flip Check-In Status Checkbox
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.EAST;
        JCheckBox flipStatusCheckBox = new JCheckBox("Flip checked-in status");
        add(flipStatusCheckBox, gbc);

        // List of Pending Services
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        String[] pendingServices = {"Room Service", "Laundry Service", "Spa Service"}; // Placeholder services
        JList<String> servicesList = new JList<>(pendingServices);
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
        String[] actions = {"Mark as Complete", "Cancel"}; // Placeholder actions
        JComboBox<String> actionsComboBox = new JComboBox<>(actions);
        add(actionsComboBox, gbc);

        // Submit Button
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            // Handle submit action here
        });
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

        // Logout button
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> parent.showPanel("Login"));
        add(logoutButton, gbc);
    }
}