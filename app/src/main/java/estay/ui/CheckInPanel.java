package estay.ui;

import javax.swing.*;
import java.awt.*;
import estay.database.BookingDAO;
import java.security.SecureRandom;

public class CheckInPanel extends JPanel {
    private JTextField answerField1;
    private JTextField answerField2;
    private JLabel questionLabel1;
    private JLabel questionLabel2;
    private JLabel statusLabel;
    private HotelCheckInCheckOutUI parent;
    private String bookingCode;

    public CheckInPanel(HotelCheckInCheckOutUI parent) {
        this.parent = parent;
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Title label
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 10, 10, 10);
        add(new JLabel("Answer the security questions to Check-in", SwingConstants.CENTER), gbc);
        
        gbc.gridwidth = 1;

        // First question label and text field
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(5, 5, 5, 5);
        questionLabel1 = new JLabel();
        add(questionLabel1, gbc);

        gbc.gridx = 1;
        answerField1 = new JTextField();
        answerField1.setPreferredSize(new Dimension(200, 25));
        add(answerField1, gbc);

        // Second question label and text field
        gbc.gridx = 0;
        gbc.gridy = 2;
        questionLabel2 = new JLabel();
        add(questionLabel2, gbc);

        gbc.gridx = 1;
        answerField2 = new JTextField();
        answerField2.setPreferredSize(new Dimension(200, 25));
        add(answerField2, gbc);

        // Status label
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        statusLabel = new JLabel("", SwingConstants.CENTER);
        add(statusLabel, gbc);

        // Navigation buttons
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 10, 10);
        gbc.fill = GridBagConstraints.NONE;
        
        JPanel buttonPanel = new JPanel();
        JButton backButton = new JButton("Back to Main Menu");
        backButton.addActionListener(e -> parent.showPanel("Main Menu"));
        buttonPanel.add(backButton);

        JButton nextButton = new JButton("Next");
        nextButton.addActionListener(e -> handleCheckIn());
        buttonPanel.add(nextButton);

        add(buttonPanel, gbc);

        // Logout button
        JButton logoutButton = new JButton("Back");
        logoutButton.addActionListener(e -> parent.showPanel("Login"));
        buttonPanel.add(logoutButton);

    }

    public void setBookingCode(String bookingCode) {
        this.bookingCode = bookingCode;
        BookingDAO bookingDAO = new BookingDAO();
        BookingDAO.GuestInfo guestInfo = bookingDAO.getSecurityQuestions(bookingCode);
        if (guestInfo != null) {
            questionLabel1.setText(guestInfo.securityQuestion1);
            questionLabel2.setText(guestInfo.securityQuestion2);
        }
    }

    private void handleCheckIn() {
        String answer1 = answerField1.getText();
        String answer2 = answerField2.getText();
        BookingDAO bookingDAO = new BookingDAO();
        if (bookingDAO.validateSecurityAnswers(bookingCode, answer1, answer2)) {
            statusLabel.setText("Check-in successful!");
            String roomKey = generateRoomKey();
            JOptionPane.showMessageDialog(this, "Check-in successful! Your room key is: " + roomKey);
            bookingDAO.updateBookingStatus(bookingCode, "checked in"); // Update booking status
            parent.showPanel("Request");
        } else {
            statusLabel.setText("Security answers are incorrect. Please try again.");
        }
    }

    private String generateRoomKey() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder roomKey = new StringBuilder(6);
        for (int i = 0; i < 6; i++) {
            roomKey.append(characters.charAt(random.nextInt(characters.length())));
        }
        return roomKey.toString();
    }
}
