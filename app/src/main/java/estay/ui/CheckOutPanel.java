package estay.ui;

import javax.swing.*;
import java.awt.*;
import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;
import estay.database.BookingDAO;

public class CheckOutPanel extends JPanel {
    private JLabel roomNumberLabel;
    private JLabel totalAmountLabel;
    private HotelCheckInCheckOutUI parent;
    private BookingDAO bookingDAO;
    private String bookingCode;
    private static final double TAX_RATE = 0.10; // Hardcoded tax rate

    public CheckOutPanel(HotelCheckInCheckOutUI parent) {
        this.parent = parent;
        this.bookingDAO = new BookingDAO();
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Room number label
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        roomNumberLabel = new JLabel();
        add(roomNumberLabel, gbc);

        // Total amount label
        gbc.gridy = 1;
        totalAmountLabel = new JLabel();
        totalAmountLabel.setFont(new Font("Serif", Font.BOLD, 24));
        add(totalAmountLabel, gbc);

        // Check-Out button
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton checkOutButton = new JButton("Check-Out");
        checkOutButton.addActionListener(e -> handleCheckOut());
        add(checkOutButton, gbc);
    }

    public void setBookingCode(String bookingCode) {
        this.bookingCode = bookingCode;
        BookingDAO.BookingInfo bookingInfo = bookingDAO.getBookingDetails(bookingCode);
        if (bookingInfo != null) {
            roomNumberLabel.setText("Room Number: " + bookingInfo.roomId);
            long duration = bookingInfo.expiration.getTime() - bookingInfo.checkInDate.getTime();
            long days = TimeUnit.MILLISECONDS.toDays(duration);
            double totalCost = bookingInfo.dayRate * days + bookingInfo.accumulatedCost;
            totalCost += totalCost * TAX_RATE; // Add tax
            totalAmountLabel.setText(String.format("Total: $%.2f", totalCost));
        }
    }

    private void handleCheckOut() {
        parent.showPanel("Payment");
        ((PaymentPanel) parent.getPanel("Payment")).setBookingCode(bookingCode); // Pass booking code to PaymentPanel
    }
}
