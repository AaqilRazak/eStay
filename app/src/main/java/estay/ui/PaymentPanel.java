package estay.ui;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.concurrent.TimeUnit;
import estay.database.BookingDAO;

public class PaymentPanel extends JPanel {
    private JLabel refundAmountLabel;
    private JLabel chargeAmountLabel;
    private HotelCheckInCheckOutUI parent;
    private BookingDAO bookingDAO;
    private String bookingCode;
    private static final double DEPOSIT_AMOUNT = 150.00;
    private static final double TAX_RATE = 0.15; // Hardcoded tax rate

    public PaymentPanel(HotelCheckInCheckOutUI parent) {
        this.parent = parent;
        this.bookingDAO = new BookingDAO();
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Set colors and fonts
        Color backgroundColor = new Color(0xF9F3DE); // Beige
        Color textColor = new Color(0xFF4E62); // Magic Potion
        Color buttonBackground = new Color(0x2ECFCA); // Maximum Blue Green
        Color buttonForeground = Color.WHITE;
        Font font = new Font("Serif", Font.BOLD, 18);

        setBackground(backgroundColor);

        // Refund label
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        refundAmountLabel = new JLabel();
        refundAmountLabel.setFont(font);
        refundAmountLabel.setForeground(textColor);
        add(refundAmountLabel, gbc);

        // Charge label
        gbc.gridy = 1;
        chargeAmountLabel = new JLabel();
        chargeAmountLabel.setFont(new Font("Serif", Font.BOLD, 24));
        chargeAmountLabel.setForeground(textColor);
        add(chargeAmountLabel, gbc);

        // Back button
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        JButton backButton = new JButton("Back to Main Menu");
        backButton.setFont(font);
        backButton.setBackground(buttonBackground);
        backButton.setForeground(buttonForeground);
        backButton.addActionListener(e -> {
            parent.showPanel("Login");
            clearData(); // Clear data when going back to login
        });
        add(backButton, gbc);

        // Logout button
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(font);
        logoutButton.setBackground(buttonBackground);
        logoutButton.setForeground(buttonForeground);
        logoutButton.addActionListener(e -> parent.showPanel("Login"));
        add(logoutButton, gbc);
    }

    public void setBookingCode(String bookingCode) {
        this.bookingCode = bookingCode;
        refreshData();
    }

    public void refreshData() {
        if (bookingCode == null) {
            return;
        }
        BookingDAO.BookingInfo bookingInfo = bookingDAO.getBookingDetails(bookingCode);
        if (bookingInfo != null) {
            long duration = bookingInfo.expiration.getTime() - bookingInfo.checkInDate.getTime();
            long days = TimeUnit.MILLISECONDS.toDays(duration);
            double roomCharges = bookingInfo.dayRate * days;

            // Calculate the total accumulated cost from service requests
            double accumulatedCost = 0.0;
            List<BookingDAO.ServiceRequest> serviceRequests = bookingDAO.getServiceRequests(bookingCode);
            for (BookingDAO.ServiceRequest request : serviceRequests) {
                accumulatedCost += request.price * request.quantity;
            }

            double totalCostBeforeTax = roomCharges + accumulatedCost;
            double tax = totalCostBeforeTax * TAX_RATE;
            double totalCost = totalCostBeforeTax + tax;

            double balance = DEPOSIT_AMOUNT - accumulatedCost;
            if (balance >= 0) {
                refundAmountLabel.setText(String.format("You will be refunded $%.2f", balance));
            } else {
                chargeAmountLabel.setText(String.format("You will be charged an additional $%.2f", -balance));
            }
        }
    }

    public void clearData() {
        refundAmountLabel.setText("");
        chargeAmountLabel.setText("");
        bookingCode = null;
    }
}
