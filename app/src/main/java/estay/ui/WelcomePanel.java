package estay.ui;

import javax.swing.*;
import java.awt.*;
import estay.database.BookingDAO;
import java.security.SecureRandom;

public class WelcomePanel extends JPanel {
    private JLabel wifiLabel;
    private JLabel amenitiesLabel;
    private JLabel hoursLabel;
    private JButton serviceRequestButton;
    private JButton checkOutButton;
    private JButton logoutButton;
    private HotelCheckInCheckOutUI parent;
    private BookingDAO bookingDAO;
    private String bookingCode;
    private String guestName;

    public WelcomePanel(HotelCheckInCheckOutUI parent) {
        this.parent = parent;
        this.bookingDAO = new BookingDAO();
        setLayout(new BorderLayout());

        JPanel infoPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        wifiLabel = new JLabel();
        amenitiesLabel = new JLabel("<html><b>Amenities:</b><br>1. Swimming Pool<br>2. Gym<br>3. Free Breakfast</html>");
        hoursLabel = new JLabel("<html><b>Hours:</b><br>Check-in: 2:00 PM<br>Check-out: 11:00 AM</html>");
        serviceRequestButton = new JButton("Service Requests");
        checkOutButton = new JButton("Check Out");
        logoutButton = new JButton("Logout");

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        infoPanel.add(wifiLabel, gbc);

        gbc.gridy = 1;
        infoPanel.add(amenitiesLabel, gbc);

        gbc.gridy = 2;
        infoPanel.add(hoursLabel, gbc);

        add(infoPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints buttonGbc = new GridBagConstraints();
        buttonGbc.gridx = 0;
        buttonGbc.gridy = 0;
        buttonGbc.insets = new Insets(10, 10, 10, 10);
        buttonGbc.anchor = GridBagConstraints.LAST_LINE_END;

        serviceRequestButton.addActionListener(e -> {
            parent.showPanel("Request");
            parent.getRequestPanel().setBookingCode(bookingCode); // Ensure the booking code is set in the RequestPanel
        });
        buttonPanel.add(serviceRequestButton, buttonGbc);

        buttonGbc.gridy = 1;
        checkOutButton.addActionListener(e -> parent.showPanel("Check Out"));
        buttonPanel.add(checkOutButton, buttonGbc);

        buttonGbc.gridy = 2;
        logoutButton.addActionListener(e -> parent.showPanel("Login"));
        buttonPanel.add(logoutButton, buttonGbc);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void setBookingCode(String bookingCode) {
        this.bookingCode = bookingCode;
        BookingDAO.GuestInfo guestInfo = bookingDAO.getSecurityQuestions(bookingCode);
        if (guestInfo != null) {
            guestName = guestInfo.name;
            if (guestName.split(" ").length > 1) {
                String lastName = guestName.split(" ")[1];
                String wifiPassword = lastName + generateRandomCode();
                wifiLabel.setText("Wi-Fi Password: " + wifiPassword);
            } else {
                wifiLabel.setText("Wi-Fi Password: [Last name not available]");
            }
        } else {
            wifiLabel.setText("Wi-Fi Password: [Guest info not available]");
        }
    }

    private String generateRandomCode() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder code = new StringBuilder(4);
        for (int i = 0; i < 4; i++) {
            code.append(characters.charAt(random.nextInt(characters.length())));
        }
        return code.toString();
    }
}
