package estay;

import javax.swing.*;
import java.awt.*;

public class CheckInPanel extends JPanel {
    public CheckInPanel(HotelCheckInCheckOutUI parent) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Title label
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.insets = new Insets(10, 10, 10, 10);
        add(new JLabel("Select any two information to Check-in", SwingConstants.CENTER), gbc);
        
        gbc.gridwidth = 1;

        // First dropdown and text field
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(5, 5, 5, 5);
        add(new JLabel("1)"), gbc);

        gbc.gridx = 1;
        JComboBox<String> comboBox1 = new JComboBox<>(new String[]{"Question 1", "Question 2", "Question 3"});
        add(comboBox1, gbc);

        gbc.gridx = 2;
        JTextField textField1 = new JTextField();
        textField1.setPreferredSize(new Dimension(200, 25));
        add(textField1, gbc);

        // Second dropdown and text field
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("2)"), gbc);

        gbc.gridx = 1;
        JComboBox<String> comboBox2 = new JComboBox<>(new String[]{"Question 1", "Question 2", "Question 3"});
        add(comboBox2, gbc);

        gbc.gridx = 2;
        JTextField textField2 = new JTextField();
        textField2.setPreferredSize(new Dimension(200, 25));
        add(textField2, gbc);

        // Navigation buttons
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 3;
        gbc.insets = new Insets(20, 10, 10, 10);
        gbc.fill = GridBagConstraints.NONE;
        
        JPanel buttonPanel = new JPanel();
        JButton backButton = new JButton("Back to Main Menu");
        backButton.addActionListener(e -> parent.showPanel("Main Menu"));
        buttonPanel.add(backButton);

        JButton nextButton = new JButton("Next");
        nextButton.addActionListener(e -> parent.showPanel("Key Code"));
        buttonPanel.add(nextButton);

        add(buttonPanel, gbc);
    }
}