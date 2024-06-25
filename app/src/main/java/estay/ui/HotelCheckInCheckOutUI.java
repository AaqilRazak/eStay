package estay.ui;

import javax.swing.*;
import java.awt.*;
import estay.*;

public class HotelCheckInCheckOutUI extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;

    public HotelCheckInCheckOutUI() {
        setTitle("Hotel Check-In and Check-Out System");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Initialize panels
        JPanel loginPanel = new LoginPanel(this);
        JPanel mainMenuPanel = new MainMenuPanel(this);
        JPanel checkInPanel = new CheckInPanel(this);
        JPanel keyCodePanel = new KeyCodePanel(this);
        JPanel checkOutPanel = new CheckOutPanel(this);
        JPanel requestPanel = new RequestPanel(this);
        JPanel paymentPanel = new PaymentPanel(this);

        // Add panels to the main panel
        mainPanel.add(loginPanel, "Login");
        mainPanel.add(mainMenuPanel, "Main Menu");
        mainPanel.add(checkInPanel, "Check In");
        mainPanel.add(keyCodePanel, "Key Code");
        mainPanel.add(checkOutPanel, "Check Out");
        mainPanel.add(requestPanel, "Request");
        mainPanel.add(paymentPanel, "Payment");

        add(mainPanel);
        cardLayout.show(mainPanel, "Login"); // Show the login panel initially
    }

    public void showPanel(String panelName) {
        cardLayout.show(mainPanel, panelName);
    }

    public void handleBookingStatus(String bookingStatus, java.sql.Timestamp expiration) {
        java.sql.Timestamp currentTime = new java.sql.Timestamp(System.currentTimeMillis());
        long timeDifference = expiration.getTime() - currentTime.getTime();
        long twoHoursInMillis = 2 * 60 * 60 * 1000;

        if (bookingStatus.equals("not checked in")) {
            showPanel("Check In");
        } else if (bookingStatus.equals("checked in") && timeDifference > twoHoursInMillis) {
            showPanel("Request");
        } else if (bookingStatus.equals("checked in") && timeDifference <= twoHoursInMillis) {
            showPanel("Check Out");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            HotelCheckInCheckOutUI ui = new HotelCheckInCheckOutUI();
            ui.setVisible(true);
        });
    }
}
