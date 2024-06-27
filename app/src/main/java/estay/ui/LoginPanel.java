package estay.ui;

import estay.database.BookingDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPanel extends JPanel {
    private JTextField codeField;
    private JTextField creditCardField;
    private BookingDAO userDAO;

    public LoginPanel(HotelCheckInCheckOutUI parent) {
        userDAO = new BookingDAO();
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        setBackground(new Color(0xF9F3DE)); // Beige background

        // Set font and colors
        Font font = new Font("Serif", Font.BOLD, 24);
        Color labelColor = new Color(0xFF4E62); // Magic Potion
        Color textFieldBackground = new Color(0xF8DF77); // Jasmine
        Color buttonBackground = new Color(0x2ECFCA); // Maximum Blue Green
        Color buttonForeground = Color.WHITE;

        // Spacer to center text fields
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weighty = 0.5;
        add(new JLabel(""), gbc); // Empty label as spacer

        // Booking Code label and text field
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 10, 5, 10); // Reduce vertical spacing
        gbc.anchor = GridBagConstraints.EAST;
        JLabel bookingCodeLabel = new JLabel("Booking Code:");
        bookingCodeLabel.setFont(font);
        bookingCodeLabel.setForeground(labelColor);
        add(bookingCodeLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        codeField = new JTextField(15);
        codeField.setFont(font);
        codeField.setBackground(textFieldBackground);
        add(codeField, gbc);

        // Credit Card label and text field
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.insets = new Insets(5, 10, 5, 10); // Reduce vertical spacing
        gbc.anchor = GridBagConstraints.EAST;
        JLabel creditCardLabel = new JLabel("Last 4 Digits of Credit Card:");
        creditCardLabel.setFont(font);
        creditCardLabel.setForeground(labelColor);
        add(creditCardLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        creditCardField = new JTextField(15);
        creditCardField.setFont(font);
        creditCardField.setBackground(textFieldBackground);
        add(creditCardField, gbc);

        // Spacer to move buttons lower
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.weighty = 1.0;
        add(new JLabel(""), gbc); // Empty label as spacer

        // Login button
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.weighty = 0.0;
        gbc.anchor = GridBagConstraints.LINE_START;
        JButton loginButton = new JButton("Login");
        loginButton.setFont(font);
        loginButton.setBackground(buttonBackground);
        loginButton.setForeground(buttonForeground);
        add(loginButton, gbc);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin(parent);
            }
        });

        // Admin button
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        JButton adminButton = new JButton("Admin Login");
        adminButton.setFont(font);
        adminButton.setBackground(buttonBackground);
        adminButton.setForeground(buttonForeground);
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
