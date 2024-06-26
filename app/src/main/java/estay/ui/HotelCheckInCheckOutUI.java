package estay.ui;

import javax.swing.*;
import java.awt.*;
import java.sql.Timestamp;

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
        JPanel loginPanel = new LoginPanel(this);
        JPanel mainMenuPanel = new MainMenuPanel(this);
        checkInPanel = new CheckInPanel(this);
        JPanel keyCodePanel = new KeyCodePanel(this);
        JPanel checkOutPanel = new CheckOutPanel(this);
        requestPanel = new RequestPanel(this);
        JPanel adminPanel = new AdminPanel(this); // Add Admin Panel

        // Add panels to the main panel
        mainPanel.add(loginPanel, "Login");
        mainPanel.add(mainMenuPanel, "Main Menu");
        mainPanel.add(checkInPanel, "Check In");
        mainPanel.add(keyCodePanel, "Key Code");
        mainPanel.add(checkOutPanel, "Check Out");
        mainPanel.add(requestPanel, "Request");
        mainPanel.add(adminPanel, "Admin"); // Add Admin Panel to CardLayout

        add(mainPanel);
        cardLayout.show(mainPanel, "Login"); // Show the login panel initially
    }

    public void showPanel(String panelName) {
        cardLayout.show(mainPanel, panelName);
    }

    public void handleBookingStatus(String bookingStatus, Timestamp expiration, String bookingCode) {
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            HotelCheckInCheckOutUI ui = new HotelCheckInCheckOutUI();
            ui.setVisible(true);

            // Show Admin Login Dialog for testing
            AdminLoginDialog adminLoginDialog = new AdminLoginDialog(ui, ui);
            adminLoginDialog.setVisible(true);
        });
    }
}