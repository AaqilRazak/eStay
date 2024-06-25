package estay;

import javax.swing.*;
import java.awt.*;

public class CheckInPanel extends JPanel {
    public CheckInPanel(HotelCheckInCheckOutUI parent) {
        setLayout(new GridLayout(4, 2));
        add(new JLabel("Select any two information to Check-in"));
        add(new JComboBox<>(new String[]{"Info 1", "Info 2", "Info 3"}));
        add(new JComboBox<>(new String[]{"Info 1", "Info 2", "Info 3"}));
        add(new JLabel("Room"));
        add(new JLabel("Amenities"));
        add(new JLabel("Service"));

        JButton backButton = new JButton("Back to Main Menu");
        backButton.addActionListener(e -> parent.showPanel("Main Menu"));
        add(backButton);

        JButton nextButton = new JButton("Next");
        nextButton.addActionListener(e -> parent.showPanel("Key Code"));
        add(nextButton);
    }
}