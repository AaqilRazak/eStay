package estay.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import estay.database.AdminDAO;

public class AdminLoginDialog extends JDialog {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private AdminDAO adminDAO;
    private HotelCheckInCheckOutUI parentUI; // Reference to the main UI

    public AdminLoginDialog(Frame parent, HotelCheckInCheckOutUI parentUI) {
        super(parent, "Admin Login", true);
        this.parentUI = parentUI; // Initialize the reference
        adminDAO = new AdminDAO();

        // Set colors and fonts
        Color backgroundColor = new Color(0xF9F3DE); // Beige
        Color labelColor = new Color(0xFF4E62); // Magic Potion
        Color textFieldBackground = new Color(0xF8DF77); // Jasmine
        Color buttonBackground = new Color(0x2ECFCA); // Maximum Blue Green
        Color buttonForeground = Color.WHITE;
        Font font = new Font("Serif", Font.BOLD, 18);

        setLayout(new GridBagLayout());
        getContentPane().setBackground(backgroundColor);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

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

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });

        pack();
        setLocationRelativeTo(parent);
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (adminDAO.validateAdmin(username, password)) {
            JOptionPane.showMessageDialog(this, "Login successful");
            parentUI.showPanel("Admin"); // Navigate to Admin Panel
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials");
        }
    }
}
