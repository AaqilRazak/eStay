package estay.ui;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.TimeUnit;

import estay.database.BookingDAO;

public class PaymentPanel extends JPanel {
    private JLabel refundAmountLabel;
    private JLabel chargeAmountLabel;
    private HotelCheckInCheckOutUI parent;
    private BookingDAO bookingDAO;
    private String bookingCode;
    private static final double DEPOSIT_AMOUNT = 50.00;

    public PaymentPanel(HotelCheckInCheckOutUI parent) {
        this.parent = parent;
        this.bookingDAO = new BookingDAO();
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Refund label
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        refundAmountLabel = new JLabel();
        add(refundAmountLabel, gbc);

        // Charge label
        gbc.gridy = 1;
        chargeAmountLabel = new JLabel();
        chargeAmountLabel.setFont(new Font("Serif", Font.BOLD, 24));
        add(chargeAmountLabel, gbc);

        // Back button
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton backButton = new JButton("Back to Main Menu");
        backButton.addActionListener(e -> parent.showPanel("Main Menu"));
        add(backButton, gbc);
    }

    public void setBookingCode(String bookingCode) {
        this.bookingCode = bookingCode;
        BookingDAO.BookingInfo bookingInfo = bookingDAO.getBookingDetails(bookingCode);
        if (bookingInfo != null) {
            long duration = bookingInfo.expiration.getTime() - bookingInfo.checkInDate.getTime();
            long days = TimeUnit.MILLISECONDS.toDays(duration);
            double totalCost = bookingInfo.dayRate * days + bookingInfo.accumulatedCost;
            totalCost += totalCost * 0.06; // Add tax

            double balance = DEPOSIT_AMOUNT - bookingInfo.accumulatedCost;
            if (balance >= 0) {
                refundAmountLabel.setText(String.format("You will be refunded $%.2f", balance));
                chargeAmountLabel.setText("You will not be charged any additional amount.");
            } else {
                refundAmountLabel.setText("You will not be refunded any amount.");
                chargeAmountLabel.setText(String.format("You will be charged an additional $%.2f", -balance));
            }
        }
    }
}
