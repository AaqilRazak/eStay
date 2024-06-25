package estay;

import javax.swing.*;
import java.awt.*;

public class RequestPanel extends JPanel {
    public RequestPanel(HotelCheckInCheckOutUI parent) {
        setLayout(new GridLayout(3, 1));
        add(new JLabel("Please enter your request"));
        add(new JTextArea());

        JButton submitButton = new JButton("Submit");
        add(submitButton);

        JButton backButton = new JButton("Back to Main Menu");
        backButton.addActionListener(e -> parent.showPanel("Main Menu"));
        add(backButton);

    }
}