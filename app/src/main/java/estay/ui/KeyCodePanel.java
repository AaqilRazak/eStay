package estay.ui;

import javax.swing.*;
import java.awt.*;

public class KeyCodePanel extends JPanel {
    public KeyCodePanel(HotelCheckInCheckOutUI parent) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Title label
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        add(new JLabel("Your key code to your room", SwingConstants.CENTER), gbc);

        // Room number display
        gbc.gridy = 1;
        JLabel roomNumberLabel = new JLabel("Room Number: 101", SwingConstants.CENTER);
        roomNumberLabel.setFont(new Font("Serif", Font.BOLD, 20));
        add(roomNumberLabel, gbc);

        // Fixed 4-digit code display
        gbc.gridy = 2;
        JLabel keyCodeLabel = new JLabel("1234", SwingConstants.CENTER);
        keyCodeLabel.setFont(new Font("Serif", Font.BOLD, 24));
        add(keyCodeLabel, gbc);

        // Navigation button
        gbc.gridy = 3;
        JPanel buttonPanel = new JPanel();
        JButton backButton = new JButton("Back to Main Menu");
        backButton.addActionListener(e -> parent.showPanel("Main Menu"));
        buttonPanel.add(backButton);
        add(buttonPanel, gbc);
    }
}