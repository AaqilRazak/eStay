package estay;

import javax.swing.*;
import java.awt.*;

public class CheckOutPanel extends JPanel {
    public CheckOutPanel(HotelCheckInCheckOutUI parent) {
        setLayout(new GridLayout(5, 2));
        add(new JLabel("Room..."));
        add(new JLabel("Amenities..."));
        add(new JLabel("Service..."));
        add(new JLabel("Total"));
        add(new JTextField());

        JButton backButton = new JButton("Back to Main Menu");
        backButton.addActionListener(e -> parent.showPanel("Main Menu"));
        add(backButton);
        
    }
}