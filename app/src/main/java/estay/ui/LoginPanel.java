package estay.ui;

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
        setLayout(new GridLayout(4, 1));
        add(new JLabel("Booking Code:"));
        codeField = new JTextField();
        add(codeField);
        add(new JLabel("Last 4 Digits of Credit Card:"));
        creditCardField = new JTextField();
        add(creditCardField);
        JButton loginButton = new JButton("Login");
        add(loginButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin(parent);
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
}
