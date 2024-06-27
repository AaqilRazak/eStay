package estay.ui;

import javax.swing.*;
import java.awt.*;
import estay.database.BookingDAO;

public class WelcomePanel extends JPanel {
    private JLabel wifiLabel;
    private JLabel amenitiesLabel;
    private JLabel hoursLabel;
    private JButton serviceRequestButton;
    private JButton checkOutButton;
    private HotelCheckInCheckOutUI parent;
    private BookingDAO bookingDAO;
    private String bookingCode;
    private String guestName;

    public WelcomePanel(HotelCheckInCheckOutUI parent) {
        this.parent = parent;
        this.bookingDAO = new BookingDAO();
        setLayout(new BorderLayout());

        // Set colors and fonts
        Color backgroundColor = new Color(0xF9F3DE); // Beige
        Color textColor = new Color(0xFF4E62); // Magic Potion
        Color buttonBackground = new Color(0x2ECFCA); // Maximum Blue Green
        Color buttonForeground = Color.WHITE;
        Font font = new Font("Serif", Font.BOLD, 18);

        setBackground(backgroundColor);

        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBackground(backgroundColor);
        GridBagConstraints gbc = new GridBagConstraints();

        wifiLabel = new JLabel();
        wifiLabel.setFont(font);
        wifiLabel.setForeground(textColor);

        amenitiesLabel = new JLabel("<html><b>Amenities:</b><br>1. Swimming Pool<br>2. Gym<br>3. Free Breakfast</html>");
        amenitiesLabel.setFont(font);
        amenitiesLabel.setForeground(textColor);

        hoursLabel = new JLabel("<html><b>Hours:</b><br>Check-in: 2:00 PM<br>Check-out: 11:00 AM</html>");
        hoursLabel.setFont(font);
        hoursLabel.setForeground(textColor);

        serviceRequestButton = new JButton("Service Requests");
        serviceRequestButton.setFont(font);
        serviceRequestButton.setBackground(buttonBackground);
        serviceRequestButton.setForeground(buttonForeground);

        checkOutButton = new JButton("Check Out");
        checkOutButton.setFont(font);
        checkOutButton.setBackground(buttonBackground);
        checkOutButton.setForeground(buttonForeground);

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
        buttonPanel.setBackground(backgroundColor);
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

        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void setBookingCode(String bookingCode) {
        this.bookingCode = bookingCode;
        BookingDAO.GuestInfo guestInfo = bookingDAO.getSecurityQuestions(bookingCode);
        if (guestInfo != null) {
            guestName = guestInfo.name;
            wifiLabel.setText("Wi-Fi Password: " + guestName.split(" ")[1]); // Assuming last name is the second part
        }
    }
}
