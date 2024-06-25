package estay;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPanel extends JPanel {
    public LoginPanel(HotelCheckInCheckOutUI parent) {
        setLayout(new GridLayout(3, 1));
        add(new JLabel("Input code to login"));
        JTextField codeField = new JTextField();
        add(codeField);
        JButton loginButton = new JButton("Login");
        add(loginButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Add your login validation logic here
                parent.showPanel("Main Menu");
            }
        });
    }
}