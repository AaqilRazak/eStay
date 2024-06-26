package estay.ui;

import javax.swing.*;
import java.awt.*;

public class CheckOutPanel extends JPanel {
    public CheckOutPanel(HotelCheckInCheckOutUI parent) {
        setLayout(new BorderLayout());

        JPanel infoPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        infoPanel.add(new JLabel("Room Number: 101"), gbc); // Placeholder room number

        gbc.gridy = 1;
        infoPanel.add(new JLabel("Services Used: Room Service"), gbc); // Placeholder services

        add(infoPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints buttonGbc = new GridBagConstraints();
        buttonGbc.gridx = 0;
        buttonGbc.gridy = 0;
        buttonGbc.insets = new Insets(10, 10, 10, 10);
        buttonGbc.anchor = GridBagConstraints.LAST_LINE_END;

        JButton checkOutButton = new JButton("Check-Out");
        checkOutButton.addActionListener(e -> parent.showPanel("Payment"));
        buttonPanel.add(checkOutButton, buttonGbc);

        buttonGbc.gridy = 1;
        JButton serviceRequestsButton = new JButton("Service Requests");
        serviceRequestsButton.addActionListener(e -> parent.showPanel("Request"));
        buttonPanel.add(serviceRequestsButton, buttonGbc);

        add(buttonPanel, BorderLayout.SOUTH);
    }
}
