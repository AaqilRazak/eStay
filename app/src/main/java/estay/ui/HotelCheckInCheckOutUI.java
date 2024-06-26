package estay.ui;

import javax.swing.*;
import java.awt.*;
import estay.database.BookingDAO;

public class HotelCheckInCheckOutUI extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private CheckInPanel checkInPanel;
    private RequestPanel requestPanel;

    public HotelCheckInCheckOutUI() {
        setTitle("Hotel Check-In and Check-Out System");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Initialize panels
        JPanel loginPanel = new estay.ui.LoginPanel(this);
        JPanel mainMenuPanel = new MainMenuPanel(this);
        checkInPanel = new CheckInPanel(this);  // Initialize CheckInPanel
        JPanel keyCodePanel = new KeyCodePanel(this);
        JPanel checkOutPanel = new CheckOutPanel(this);
        requestPanel = new RequestPanel(this);  // Initialize RequestPanel
        JPanel paymentPanel = new PaymentPanel(this);
        JPanel adminPanel = new estay.ui.AdminPanel(this);

        // Add panels to the main panel
        mainPanel.add(loginPanel, "Login");
        mainPanel.add(mainMenuPanel, "Main Menu");
        mainPanel.add(checkInPanel, "Check In");
        mainPanel.add(keyCodePanel, "Key Code");
        mainPanel.add(checkOutPanel, "Check Out");
        mainPanel.add(requestPanel, "Request");
        mainPanel.add(paymentPanel, "Payment");
        mainPanel.add(adminPanel, "Admin");

        add(mainPanel);
        cardLayout.show(mainPanel, "Login"); // Show the login panel initially
    }

    public void showPanel(String panelName) {
        cardLayout.show(mainPanel, panelName);
    }

    public void handleBookingStatus(String bookingStatus, java.sql.Timestamp expiration, String bookingCode) {
        java.sql.Timestamp currentTime = new java.sql.Timestamp(System.currentTimeMillis());
        long timeDifference = expiration.getTime() - currentTime.getTime();
        long twoHoursInMillis = 2 * 60 * 60 * 1000;

        if (bookingStatus.equals("not checked in")) {
            checkInPanel.setBookingCode(bookingCode); // Set the booking code before showing the Check In panel
            showPanel("Check In");
        } else if (bookingStatus.equals("checked in") && timeDifference > twoHoursInMillis) {
            requestPanel.setBookingCode(bookingCode); // Set the booking code before showing the Request panel
            showPanel("Request");
        } else if (bookingStatus.equals("checked in") && timeDifference <= twoHoursInMillis) {
            showPanel("Check Out");
        }
    }
}
