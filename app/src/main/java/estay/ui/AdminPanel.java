package estay.ui;

import javax.swing.*;
import java.awt.*;
import estay.database.BookingDAO;
import java.util.List;

public class AdminPanel extends JPanel {
    private JTextField searchBar;
    private JLabel guestNameLabel;
    private JLabel roomNumberLabel;
    private JLabel checkInStatusLabel;
    private JCheckBox flipStatusCheckBox;
    private JList<String> servicesList;
    private JComboBox<String> actionsComboBox;
    private JButton submitButton;
    private BookingDAO bookingDAO;

    public AdminPanel(HotelCheckInCheckOutUI parent) {
        bookingDAO = new BookingDAO();
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
        searchBar.setToolTipText("Enter Booking Code...");
        add(searchBar, gbc);

        // Validate Button
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.NONE;
        JButton validateButton = new JButton("Validate");
        validateButton.addActionListener(e -> validateBookingCode());
        add(validateButton, gbc);

        // Guest Name
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        guestNameLabel = new JLabel("Guest Name: ");
        add(guestNameLabel, gbc);

        // Room Number
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.EAST;
        roomNumberLabel = new JLabel("Room Number: ");
        add(roomNumberLabel, gbc);

        // Current Check-In Status
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        checkInStatusLabel = new JLabel("Current Checked-in status: ");
        add(checkInStatusLabel, gbc);

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
        servicesList = new JList<>();
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
        submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> handleSubmit());
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

        // Initially disable interaction fields
        setFieldsEnabled(false);
    }

    private void validateBookingCode() {
        String bookingCode = searchBar.getText();
        BookingDAO.BookingInfo bookingInfo = bookingDAO.validateUser(bookingCode, ""); // Assuming you handle credit card elsewhere
        if (bookingInfo != null) {
            // Update labels with booking info
            guestNameLabel.setText("Guest Name: " + bookingInfo.status); // Placeholder for actual guest name logic
            roomNumberLabel.setText("Room Number: " + "101"); // Placeholder for actual room number logic
            checkInStatusLabel.setText("Current Checked-in status: " + (bookingInfo.status.equals("checked_in") ? "Yes" : "No"));

            // Update services list
            List<BookingDAO.ServiceOffering> pendingServices = bookingDAO.getServiceOfferings(); // Adjust to get pending services
            String[] servicesArray = pendingServices.stream().map(s -> s.requestType).toArray(String[]::new);
            servicesList.setListData(servicesArray);

            // Enable fields for further interaction
            setFieldsEnabled(true);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid Booking Code. Please try again.");
        }
    }

    private void setFieldsEnabled(boolean enabled) {
        guestNameLabel.setEnabled(enabled);
        roomNumberLabel.setEnabled(enabled);
        checkInStatusLabel.setEnabled(enabled);
        flipStatusCheckBox.setEnabled(enabled);
        servicesList.setEnabled(enabled);
        actionsComboBox.setEnabled(enabled);
        submitButton.setEnabled(enabled);
    }

    private void handleSubmit() {
        // Handle submit action
    }
}
