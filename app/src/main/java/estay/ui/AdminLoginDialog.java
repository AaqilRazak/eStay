package estay.ui;

import estay.database.AdminDAO;

import javax.swing.*;
import java.awt.*;

public class AdminLoginDialog extends JDialog {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private AdminDAO adminDAO;
    private HotelCheckInCheckOutUI parentUI; // Reference to the main UI

    public AdminLoginDialog(Frame parent, HotelCheckInCheckOutUI parentUI) {
        super(parent, "Admin Login", true);
        this.parentUI = parentUI; // Initialize the reference
        adminDAO = new AdminDAO();
        
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Set font and colors
        Font font = new Font("Serif", Font.BOLD, 18);
        Color backgroundColor = new Color(0xF9F3DE); // Beige
        Color labelColor = new Color(0xFF4E62); // Magic Potion
        Color textFieldBackground = new Color(0xF8DF77); // Jasmine
        Color buttonBackground = new Color(0x2ECFCA); // Maximum Blue Green
        Color buttonForeground = Color.WHITE;

        getContentPane().setBackground(backgroundColor);

        // Username label and text field
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(font);
        usernameLabel.setForeground(labelColor);
        add(usernameLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        usernameField = new JTextField(15);
        usernameField.setFont(font);
        usernameField.setBackground(textFieldBackground);
        add(usernameField, gbc);

        // Password label and password field
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(font);
        passwordLabel.setForeground(labelColor);
        add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        passwordField = new JPasswordField(15);
        passwordField.setFont(font);
        passwordField.setBackground(textFieldBackground);
        add(passwordField, gbc);

        // Login button
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton loginButton = new JButton("Login");
        loginButton.setFont(font);
        loginButton.setBackground(buttonBackground);
        loginButton.setForeground(buttonForeground);
        add(loginButton, gbc);

        loginButton.addActionListener(e -> handleLogin());

        pack();
        setLocationRelativeTo(parent);
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (adminDAO.validateAdmin(username, password)) {
            showCustomMessageDialog("Login successful", JOptionPane.INFORMATION_MESSAGE);
            parentUI.showPanel("Admin"); // Navigate to Admin Panel
            dispose();
        } else {
            showCustomMessageDialog("Invalid credentials", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showCustomMessageDialog(String message, int messageType) {
        Font font = new Font("Serif", Font.BOLD, 18);
        Color labelColor = new Color(0xFF4E62); // Magic Potion
        Color backgroundColor = new Color(0xF9F3DE); // Beige
        Color buttonBackground = new Color(0x2ECFCA); // Maximum Blue Green
        Color buttonForeground = Color.WHITE;

        // Create a custom panel for the message dialog
        JPanel panel = new JPanel();
        panel.setBackground(backgroundColor);
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel messageLabel = new JLabel(message);
        messageLabel.setFont(font);
        messageLabel.setForeground(labelColor);
        panel.add(messageLabel, gbc);

        // Create and customize the message dialog
        UIManager.put("OptionPane.background", backgroundColor);
        UIManager.put("Panel.background", backgroundColor);
        UIManager.put("Button.background", buttonBackground);
        UIManager.put("Button.foreground", buttonForeground);
        UIManager.put("Button.font", font);

        JOptionPane.showMessageDialog(this, panel, "Message", messageType);
    }
}
