package estay.ui;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import estay.database.BookingDAO;

public class RequestPanel extends JPanel {
    private JLabel nameLabel;
    private JCheckBox[] serviceCheckBoxes;
    private JTextField[] quantityFields;
    private JTextField[] priceFields;
    private JButton submitButton;
    private JButton backButton;
    private JPanel requestDisplayPanel;
    private JTextArea requestDisplayArea;
    private HotelCheckInCheckOutUI parent;
    private BookingDAO bookingDAO;
    private String bookingCode;
    private String guestName;

    public RequestPanel(HotelCheckInCheckOutUI parent) {
        this.parent = parent;
        this.bookingDAO = new BookingDAO();
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        nameLabel = new JLabel();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        inputPanel.add(nameLabel, gbc);

        List<BookingDAO.ServiceOffering> offerings = bookingDAO.getServiceOfferings();
        serviceCheckBoxes = new JCheckBox[offerings.size()];
        quantityFields = new JTextField[offerings.size()];
        priceFields = new JTextField[offerings.size()];

        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;  // Aligning to the left

        for (int i = 0; i < offerings.size(); i++) {
            BookingDAO.ServiceOffering offering = offerings.get(i);
            serviceCheckBoxes[i] = new JCheckBox(offering.requestType);
            quantityFields[i] = new JTextField("1");
            quantityFields[i].setPreferredSize(new Dimension(50, 25));
            priceFields[i] = new JTextField(String.format("$%.2f", offering.price));
            priceFields[i].setEditable(false);
            priceFields[i].setPreferredSize(new Dimension(80, 25));

            gbc.gridx = 0;
            gbc.gridy = i + 1;
            inputPanel.add(serviceCheckBoxes[i], gbc);

            gbc.gridx = 1;
            inputPanel.add(quantityFields[i], gbc);

            gbc.gridx = 2;
            inputPanel.add(priceFields[i], gbc);
        }

        submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> handleSubmit());
        gbc.gridx = 1;
        gbc.gridy = offerings.size() + 1;
        gbc.gridwidth = 1;
        inputPanel.add(submitButton, gbc);

        backButton = new JButton("Back to Welcome");
        backButton.addActionListener(e -> {
            parent.showPanel("Welcome");
            updateRequestDisplay(); // Update the request display when navigating back to the welcome screen
        });
        gbc.gridx = 2;
        inputPanel.add(backButton, gbc);

        add(inputPanel, BorderLayout.NORTH);

        requestDisplayPanel = new JPanel();
        requestDisplayPanel.setLayout(new BoxLayout(requestDisplayPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(requestDisplayPanel);
        scrollPane.setPreferredSize(new Dimension(600, 200));
        add(scrollPane, BorderLayout.CENTER);

        setPreferredSize(new Dimension(800, 600)); // Ensure this size is respected
    }

    public void setBookingCode(String bookingCode) {
        this.bookingCode = bookingCode;
        System.out.println("Booking code set to: " + bookingCode); // Debug statement
        BookingDAO.GuestInfo guestInfo = bookingDAO.getSecurityQuestions(bookingCode);
        if (guestInfo != null) {
            guestName = guestInfo.name;
            nameLabel.setText("Hello, " + guestName + "! Please select your service requests:");
        }
        updateRequestDisplay();
    }

    private void handleSubmit() {
        System.out.println("Submitting service request for booking code: " + bookingCode); // Debug statement
        double totalCost = 0.0;
        for (int i = 0; i < serviceCheckBoxes.length; i++) {
            if (serviceCheckBoxes[i].isSelected()) {
                String requestType = serviceCheckBoxes[i].getText();
                int quantity = Integer.parseInt(quantityFields[i].getText());
                double price = Double.parseDouble(priceFields[i].getText().replace("$", ""));
                System.out.println("Selected service: " + requestType + " with price: " + price); // Debug statement
                bookingDAO.saveServiceRequest(bookingCode, requestType, price, quantity);
                totalCost += price * quantity;
            }
        }
        bookingDAO.updateAccumulatedCost(bookingCode, totalCost);
        JOptionPane.showMessageDialog(this, "Thank you! Your request has been submitted.");
        updateRequestDisplay();
    }

    private void updateRequestDisplay() {
        List<BookingDAO.ServiceRequest> requests = bookingDAO.getServiceRequests(bookingCode);
        requestDisplayPanel.removeAll();
        for (BookingDAO.ServiceRequest request : requests) {
            JPanel requestPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JLabel requestLabel = new JLabel(request.quantity + "x " + request.requestType + " $" + (request.price * request.quantity) + " " + request.status);
            if (request.status.equals("pending")) {
                JButton cancelButton = new JButton("Cancel");
                cancelButton.addActionListener(e -> {
                    bookingDAO.decrementAccumulatedCost(bookingCode, request.price * request.quantity);
                    bookingDAO.deleteServiceRequest(request.requestId);
                    updateRequestDisplay();
                });
                requestPanel.add(requestLabel);
                requestPanel.add(cancelButton);
            } else {
                requestPanel.add(requestLabel);
            }
            requestDisplayPanel.add(requestPanel);
        }
        requestDisplayPanel.revalidate();
        requestDisplayPanel.repaint();
    }
}
