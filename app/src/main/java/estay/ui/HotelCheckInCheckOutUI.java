package estay.ui;

import javax.swing.*;
import java.awt.*;
import java.sql.Timestamp;
import estay.database.BookingDAO;

public class HotelCheckInCheckOutUI extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private CheckInPanel checkInPanel;
    private CheckOutPanel checkOutPanel;
    private PaymentPanel paymentPanel;
    private RequestPanel requestPanel;
    private WelcomePanel welcomePanel;
    private String currentBookingCode;

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
        mainPanel.add(requestPanel, "Request");
        mainPanel.add(adminPanel, "Admin"); // Add Admin Panel to CardLayout

        add(mainPanel);
        cardLayout.show(mainPanel, "Login"); // Show the login panel initially
    }

    public void showPanel(String panelName) {
        if (panelName.equals("Login")) {
            clearUserData();
        } else if (panelName.equals("Check Out")) {
            checkOutPanel.setBookingCode(currentBookingCode);
            checkOutPanel.refreshData();
        } else if (panelName.equals("Request")) {
            requestPanel.setBookingCode(currentBookingCode);
            requestPanel.updateRequestDisplay(); // Ensure request display is updated
        } else if (panelName.equals("Payment")) {
            paymentPanel.setBookingCode(currentBookingCode);
            paymentPanel.refreshData();
        }
        cardLayout.show(mainPanel, panelName);
    }

    public void handleBookingStatus(String bookingStatus, Timestamp expiration, String bookingCode) {
        currentBookingCode = bookingCode;
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        long timeDifference = expiration.getTime() - currentTime.getTime();
        long twoHoursInMillis = 2 * 60 * 60 * 1000;

        if (bookingStatus.equals("not checked in")) {
            checkInPanel.setBookingCode(bookingCode); // Set the booking code before showing the Check In panel
            showPanel("Check In");
        } else if (bookingStatus.equals("checked in") && timeDifference > twoHoursInMillis) {
            welcomePanel.setBookingCode(bookingCode); // Set the booking code before showing the Welcome panel
            showPanel("Welcome");
        } else if (bookingStatus.equals("checked in") && timeDifference <= twoHoursInMillis) {
            checkOutPanel.setBookingCode(bookingCode); // Set the booking code before showing the Check Out panel
            showPanel("Check Out");
        }
    }

    public void setCurrentBookingCode(String bookingCode) {
        this.currentBookingCode = bookingCode;
    }

    public JPanel getPanel(String panelName) {
        switch (panelName) {
            case "Check Out":
                return checkOutPanel;
            case "Payment":
                return paymentPanel;
            default:
                return null;
        }
    }

    public RequestPanel getRequestPanel() {
        return requestPanel;
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

    private void clearUserData() {
        currentBookingCode = null;
        checkInPanel.clearData();
        checkOutPanel.clearData();
        requestPanel.clearData();
        paymentPanel.clearData();
    }
}