package estay.ui;

import javax.swing.*;
import java.awt.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.TimeUnit;
import estay.database.BookingDAO;
import estay.database.BookingDAO.ServiceRequest;

public class CheckOutPanel extends JPanel {
    private JLabel roomNumberLabel;
    private JLabel totalAmountLabel;
    private JTextArea itemizedBillArea;
    private HotelCheckInCheckOutUI parent;
    private BookingDAO bookingDAO;
    private String bookingCode;
    private static final double TAX_RATE = 0.10; // Hardcoded tax rate

    public CheckOutPanel(HotelCheckInCheckOutUI parent) {
        this.parent = parent;
        this.bookingDAO = new BookingDAO();
        setLayout(new BorderLayout());

        JPanel infoPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Room number label
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        roomNumberLabel = new JLabel();
        infoPanel.add(roomNumberLabel, gbc);

        // Total amount label
        gbc.gridy = 1;
        totalAmountLabel = new JLabel();
        totalAmountLabel.setFont(new Font("Serif", Font.BOLD, 24));
        infoPanel.add(totalAmountLabel, gbc);

        // Itemized bill area
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        itemizedBillArea = new JTextArea(10, 30);
        itemizedBillArea.setEditable(false);
        infoPanel.add(new JScrollPane(itemizedBillArea), gbc);

        add(infoPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints buttonGbc = new GridBagConstraints();
        buttonGbc.gridx = 0;
        buttonGbc.gridy = 0;
        buttonGbc.insets = new Insets(10, 10, 10, 10);
        buttonGbc.anchor = GridBagConstraints.LAST_LINE_END;

        JButton checkOutButton = new JButton("Check-Out");
        checkOutButton.addActionListener(e -> handleCheckOut());
        buttonPanel.add(checkOutButton, buttonGbc);

        buttonGbc.gridy = 1;
        JButton serviceRequestsButton = new JButton("Service Requests");
        serviceRequestsButton.addActionListener(e -> {
            parent.getRequestPanel().setBookingCode(bookingCode); // Pass the booking code to the RequestPanel
            parent.showPanel("Request");
        });
        buttonPanel.add(serviceRequestsButton, buttonGbc);

        buttonGbc.gridy = 2;
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> parent.showPanel("Login"));
        buttonPanel.add(logoutButton, buttonGbc);

        add(buttonPanel, BorderLayout.SOUTH);
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
            roomNumberLabel.setText("Room Number: " + bookingInfo.roomId);
            
            // Calculate duration between check-in and expiration
            long duration = bookingInfo.expiration.getTime() - bookingInfo.checkInDate.getTime();
            long days = TimeUnit.MILLISECONDS.toDays(duration);
            double roomCharges = bookingInfo.dayRate * days;

            // Calculate the total accumulated cost from service requests
            double accumulatedCost = 0.0;
            List<ServiceRequest> serviceRequests = bookingDAO.getServiceRequests(bookingCode);
            StringBuilder itemizedBill = new StringBuilder();
            itemizedBill.append("Days Stayed: ").append(days).append("\n");
            itemizedBill.append(String.format("Day Rate: $%.2f\n", bookingInfo.dayRate));
            itemizedBill.append(String.format("Room Charges: $%.2f\n", roomCharges));
            itemizedBill.append("Service Requests:\n");

            for (ServiceRequest request : serviceRequests) {
                itemizedBill.append(String.format("%s - $%.2f (Qty: %d)\n", request.requestType, request.price, request.quantity));
                accumulatedCost += request.price * request.quantity;
            }

            itemizedBill.append(String.format("Accumulated Cost: $%.2f\n", accumulatedCost));

            // Calculate total cost and tax
            double totalCostBeforeTax = roomCharges + accumulatedCost;
            double tax = totalCostBeforeTax * TAX_RATE;
            double totalCost = totalCostBeforeTax + tax;

            totalAmountLabel.setText(String.format("Total: $%.2f", totalCost));
            itemizedBill.append(String.format("Tax: $%.2f\n", tax));
            itemizedBill.append(String.format("Total Cost: $%.2f\n", totalCost));

            itemizedBillArea.setText(itemizedBill.toString());
        }
    }

    private void handleCheckOut() {
        bookingDAO.updateBookingStatus(bookingCode, "checked out"); // Update booking status to checked out
        parent.showPanel("Payment");
        ((PaymentPanel) parent.getPanel("Payment")).setBookingCode(bookingCode); // Pass booking code to PaymentPanel
    }

    public void clearData() {
        roomNumberLabel.setText("");
        totalAmountLabel.setText("");
        itemizedBillArea.setText("");
        bookingCode = null;
    }
}
