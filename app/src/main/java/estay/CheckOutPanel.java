package estay;

import javax.swing.*;
import java.awt.*;

public class CheckOutPanel extends JPanel {
    public CheckOutPanel(HotelCheckInCheckOutUI parent) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Room and Services labels
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        add(new JLabel("Room Number: 101"), gbc); // Placeholder room number

        gbc.gridy = 1;
        add(new JLabel("Services Used: Room Service"), gbc); // Placeholder services

        // Check-Out button
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton checkOutButton = new JButton("Check-Out");
        checkOutButton.addActionListener(e -> parent.showPanel("Payment"));
        add(checkOutButton, gbc);

        // Total amount label
        gbc.gridx = 1;
        JLabel totalAmountLabel = new JLabel("Total: $0"); // Placeholder total amount
        totalAmountLabel.setFont(new Font("Serif", Font.BOLD, 24));
        add(totalAmountLabel, gbc);
    }
}