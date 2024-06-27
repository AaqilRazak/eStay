package estay.ui;

import estay.database.BookingDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import estay.database.BookingDAO;

public class LoginPanel extends JPanel {
    private JTextField codeField;
    private JTextField creditCardField;
    private BookingDAO userDAO;

    public LoginPanel(HotelCheckInCheckOutUI parent) {
        userDAO = new BookingDAO();
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Booking Code label and text field
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.EAST;
        add(new JLabel("Booking Code:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        codeField = new JTextField(15);
        add(codeField, gbc);

        // Credit Card label and text field
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        add(new JLabel("Last 4 Digits of Credit Card:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        creditCardField = new JTextField(15);
        add(creditCardField, gbc);

        // Login button
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton loginButton = new JButton("Login");
        add(loginButton, gbc);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin(parent);
            }
        });

        // Admin button
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton adminButton = new JButton("Admin Login");
        add(adminButton, gbc);

        adminButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleAdminLogin(parent);
            }
        });
    }

    private void handleLogin(HotelCheckInCheckOutUI parent) {
        String bookingCode = codeField.getText();
        String creditCardLast4 = creditCardField.getText();
    
        BookingDAO bookingDAO = new BookingDAO();
        BookingDAO.BookingInfo bookingInfo = bookingDAO.validateUser(bookingCode, creditCardLast4);
        if (bookingInfo != null && !"checked out".equalsIgnoreCase(bookingInfo.status)) {
            parent.setCurrentBookingCode(bookingCode);  // Ensure the booking code is set in the parent UI
            parent.handleBookingStatus(bookingInfo.status, bookingInfo.expiration, bookingCode);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid details. Please try again.");
        }
    }

    private void handleAdminLogin(HotelCheckInCheckOutUI parent) {
        AdminLoginDialog adminLoginDialog = new AdminLoginDialog((Frame) SwingUtilities.getWindowAncestor(this), parent);
        adminLoginDialog.setVisible(true);
    }

    public void clearFields() {
        codeField.setText("");
        creditCardField.setText("");
    }
}