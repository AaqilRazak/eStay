package estay.ui;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import estay.database.BookingDAO;

public class RequestPanel extends JPanel {
    private JLabel nameLabel;
    private JCheckBox[] serviceCheckBoxes;
    private JTextField[] priceFields;
    private JButton submitButton;
    private JButton backButton;
    private HotelCheckInCheckOutUI parent;
    private BookingDAO bookingDAO;
    private String bookingCode;
    private String guestName;

    public RequestPanel(HotelCheckInCheckOutUI parent) {
        this.parent = parent;
        this.bookingDAO = new BookingDAO();
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        nameLabel = new JLabel();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 10, 10, 10);
        add(nameLabel, gbc);

        List<BookingDAO.ServiceOffering> offerings = bookingDAO.getServiceOfferings();
        serviceCheckBoxes = new JCheckBox[offerings.size()];
        priceFields = new JTextField[offerings.size()];

        gbc.gridwidth = 1;

        for (int i = 0; i < offerings.size(); i++) {
            BookingDAO.ServiceOffering offering = offerings.get(i);
            serviceCheckBoxes[i] = new JCheckBox(offering.requestType);
            priceFields[i] = new JTextField(String.format("$%.2f", offering.price));
            priceFields[i].setEditable(false);

            gbc.gridx = 0;
            gbc.gridy = i + 1;
            add(serviceCheckBoxes[i], gbc);

            gbc.gridx = 1;
            add(priceFields[i], gbc);
        }

        submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> handleSubmit());
        gbc.gridx = 0;
        gbc.gridy = offerings.size() + 1;
        gbc.gridwidth = 1;
        add(submitButton, gbc);

        backButton = new JButton("Back to Main Menu");
        backButton.addActionListener(e -> parent.showPanel("Main Menu"));
        gbc.gridx = 1;
        add(backButton, gbc);
    }

    public void setBookingCode(String bookingCode) {
        this.bookingCode = bookingCode;
        System.out.println("Booking code set to: " + bookingCode); // Debug statement
        BookingDAO.GuestInfo guestInfo = bookingDAO.getSecurityQuestions(bookingCode);
        if (guestInfo != null) {
            guestName = guestInfo.name;
            nameLabel.setText("Hello, " + guestName + "! Please select your service requests:");
        }
    }

    private void handleSubmit() {
        System.out.println("Submitting service request for booking code: " + bookingCode); // Debug statement
        double totalCost = 0.0;
        for (int i = 0; i < serviceCheckBoxes.length; i++) {
            if (serviceCheckBoxes[i].isSelected()) {
                String requestType = serviceCheckBoxes[i].getText();
                double price = Double.parseDouble(priceFields[i].getText().replace("$", ""));
                System.out.println("Selected service: " + requestType + " with price: " + price); // Debug statement
                bookingDAO.saveServiceRequest(bookingCode, requestType, price);
                totalCost += price;
            }
        }
        bookingDAO.updateAccumulatedCost(bookingCode, totalCost);
        JOptionPane.showMessageDialog(this, "Thank you! Your request has been submitted.");
        parent.showPanel("Main Menu");
    }
}
