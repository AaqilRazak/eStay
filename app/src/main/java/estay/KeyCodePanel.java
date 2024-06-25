package estay;

import javax.swing.*;
import java.awt.*;

public class KeyCodePanel extends JPanel {
    public KeyCodePanel(HotelCheckInCheckOutUI parent) {
        setLayout(new GridLayout(3, 1));
        add(new JLabel("Your key code to your room"));
        add(new JTextField());

        JButton backButton = new JButton("Back to Main Menu");
        backButton.addActionListener(e -> parent.showPanel("Main Menu"));
        add(backButton);
    }
}