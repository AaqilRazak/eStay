package estay;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainMenuPanel extends JPanel {
    public MainMenuPanel(HotelCheckInCheckOutUI parent) {
        setLayout(new GridLayout(5, 1));
        
        // Example guest name; in a real application, this should be dynamic
        String guestName = "Guest Name: John Doe";
        JLabel nameLabel = new JLabel(guestName);
        add(nameLabel);

        // Current time
        String currentTime = new SimpleDateFormat("HH:mm:ss").format(new Date());
        JLabel timeLabel = new JLabel("Current Time: " + currentTime);
        add(timeLabel);

        // Buttons
        JButton checkInButton = new JButton("Check-In");
        checkInButton.addActionListener(e -> parent.showPanel("Check In"));
        add(checkInButton);

        JButton checkOutButton = new JButton("Check-Out");
        checkOutButton.addActionListener(e -> parent.showPanel("Check Out"));
        add(checkOutButton);

        JButton requestButton = new JButton("Request");
        requestButton.addActionListener(e -> parent.showPanel("Request"));
        add(requestButton);
    }
}