package estay.ui;

import javax.swing.*;
import java.awt.*;
import estay.database.BookingDAO;

public class HotelCheckInCheckOutUI extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private CheckInPanel checkInPanel;
    private RequestPanel requestPanel;
    private WelcomePanel welcomePanel;
    private CheckOutPanel checkOutPanel;
    private PaymentPanel paymentPanel;

    public HotelCheckInCheckOutUI() {
        setTitle("Hotel Check-In and Check-Out System");
        setSize(800, 600);  // Set initial window size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Initialize panels
        JPanel loginPanel = new LoginPanel(this);
        checkInPanel = new CheckInPanel(this);
        requestPanel = new RequestPanel(this);
        welcomePanel = new WelcomePanel(this);
        checkOutPanel = new CheckOutPanel(this);
        paymentPanel = new PaymentPanel(this);

        // Add panels to the main panel
        mainPanel.add(loginPanel, "Login");
        mainPanel.add(checkInPanel, "Check In");
        mainPanel.add(requestPanel, "Request");
        mainPanel.add(welcomePanel, "Welcome");
        mainPanel.add(checkOutPanel, "Check Out");
        mainPanel.add(paymentPanel, "Payment");

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
            welcomePanel.setBookingCode(bookingCode); // Set the booking code before showing the Welcome panel
            showPanel("Welcome");
        } else if (bookingStatus.equals("checked in") && timeDifference <= twoHoursInMillis) {
            showPanel("Check Out");
        }
    }

    public RequestPanel getRequestPanel() {
        return requestPanel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            HotelCheckInCheckOutUI ui = new HotelCheckInCheckOutUI();
            ui.setVisible(true);
        });
    }
}
