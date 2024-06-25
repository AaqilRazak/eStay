package estay.ui;

import javax.swing.*;
import java.awt.*;

public class PaymentPanel extends JPanel {
    public PaymentPanel(HotelCheckInCheckOutUI parent) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Label for refund
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        add(new JLabel("You will be deposited back $0"), gbc); // Placeholder refund amount

        // Amount owed label for refund
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        JLabel refundAmountLabel = new JLabel("$50");
        refundAmountLabel.setFont(new Font("Serif", Font.BOLD, 24));
        add(refundAmountLabel, gbc);

        // Label for charge
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        add(new JLabel("You will be charged $0"), gbc); // Placeholder charge amount

        // Amount owed label for charge
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        JLabel chargeAmountLabel = new JLabel("$0");
        chargeAmountLabel.setFont(new Font("Serif", Font.BOLD, 24));
        add(chargeAmountLabel, gbc);

        // Back button
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton backButton = new JButton("Back to Main Menu");
        backButton.addActionListener(e -> parent.showPanel("Main Menu"));
        add(backButton, gbc);
    }
}