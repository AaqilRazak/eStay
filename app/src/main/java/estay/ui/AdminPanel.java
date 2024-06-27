package estay.ui;

import estay.database.BookingDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class AdminPanel extends JPanel {
    private JTable bookingTable;
    private JTable serviceRequestTable;
    private JTextField filterField;
    private BookingDAO bookingDAO;
    private HotelCheckInCheckOutUI parent;

    public AdminPanel(HotelCheckInCheckOutUI mainUI) {
        this.parent = mainUI;
        bookingDAO = new BookingDAO();
        setLayout(new BorderLayout());

        // Set colors and fonts
        Color backgroundColor = new Color(0xF9F3DE); // Beige
        Color labelColor = new Color(0xFF4E62); // Magic Potion
        Color textFieldBackground = new Color(0xF8DF77); // Jasmine
        Color buttonBackground = new Color(0x2ECFCA); // Maximum Blue Green
        Color buttonForeground = Color.WHITE;
        Font font = new Font("Serif", Font.BOLD, 14);

        setBackground(backgroundColor);

        // Setup filter field
        JPanel filterPanel = new JPanel(new BorderLayout());
        filterPanel.setBackground(backgroundColor);
        JLabel filterLabel = new JLabel("Filter by Booking ID:");
        filterLabel.setFont(font);
        filterLabel.setForeground(labelColor);
        filterPanel.add(filterLabel, BorderLayout.WEST);

        filterField = new JTextField();
        filterField.setFont(font);
        filterField.setBackground(textFieldBackground);
        filterField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                updateTableViews(filterField.getText());
            }
        });
        filterPanel.add(filterField, BorderLayout.CENTER);

        // Setup tables
        bookingTable = new JTable(new DefaultTableModel(new Object[]{"Room Number", "Check In Date", "Check Out Date", "Status", "Service Charges"}, 0));
        serviceRequestTable = new JTable(new DefaultTableModel(new Object[]{"Request ID", "Request Type", "Request Date", "Status", "Price", "Quantity"}, 0));

        // Set table fonts and backgrounds
        bookingTable.setFont(font);
        bookingTable.getTableHeader().setFont(font);
        bookingTable.getTableHeader().setBackground(buttonBackground);
        bookingTable.getTableHeader().setForeground(buttonForeground);
        serviceRequestTable.setFont(font);
        serviceRequestTable.getTableHeader().setFont(font);
        serviceRequestTable.getTableHeader().setBackground(buttonBackground);
        serviceRequestTable.getTableHeader().setForeground(buttonForeground);

        // Setup buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(backgroundColor);

        JButton markRequestCompleteButton = new JButton("Mark Request Complete");
        markRequestCompleteButton.setFont(font);
        markRequestCompleteButton.setBackground(buttonBackground);
        markRequestCompleteButton.setForeground(buttonForeground);

        JButton deleteRequestButton = new JButton("Delete Request");
        deleteRequestButton.setFont(font);
        deleteRequestButton.setBackground(buttonBackground);
        deleteRequestButton.setForeground(buttonForeground);

        JButton updateBookingStatusButton = new JButton("Update Booking Status");
        updateBookingStatusButton.setFont(font);
        updateBookingStatusButton.setBackground(buttonBackground);
        updateBookingStatusButton.setForeground(buttonForeground);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(font);
        logoutButton.setBackground(buttonBackground);
        logoutButton.setForeground(buttonForeground);

        buttonPanel.add(markRequestCompleteButton);
        buttonPanel.add(deleteRequestButton);
        buttonPanel.add(updateBookingStatusButton);
        buttonPanel.add(logoutButton);

        markRequestCompleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                markRequestComplete();
            }
        });

        deleteRequestButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteRequest();
            }
        });

        updateBookingStatusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateBookingStatus();
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parent.showPanel("Login");
            }
        });

        add(filterPanel, BorderLayout.NORTH);
        add(new JScrollPane(bookingTable), BorderLayout.CENTER);
        add(new JScrollPane(serviceRequestTable), BorderLayout.SOUTH);
        add(buttonPanel, BorderLayout.EAST);

        updateTableViews("");
    }

    private void updateTableViews(String filterText) {
        List<BookingDAO.BookingInfo> filteredBookings = bookingDAO.getFilteredBookings(filterText);
        List<BookingDAO.ServiceRequest> filteredRequests = bookingDAO.getFilteredServiceRequests(filterText);

        DefaultTableModel bookingTableModel = (DefaultTableModel) bookingTable.getModel();
        bookingTableModel.setRowCount(0);

        DefaultTableModel serviceRequestTableModel = (DefaultTableModel) serviceRequestTable.getModel();
        serviceRequestTableModel.setRowCount(0);

        // Updating booking table data
        for (BookingDAO.BookingInfo info : filteredBookings) {
            bookingTableModel.addRow(new Object[]{
                info.roomId,
                info.checkInDate.toString(),
                info.expiration.toString(), // Using expiration as a substitute for check-out date
                info.status,
                info.accumulatedCost
            });
        }

        // Updating service request table data
        for (BookingDAO.ServiceRequest request : filteredRequests) {
            serviceRequestTableModel.addRow(new Object[]{
                request.requestId,
                request.requestType,
                request.requestDate.toString(),
                request.status,
                request.price,
                request.quantity
            });
        }

        bookingTable.setModel(bookingTableModel);
        serviceRequestTable.setModel(serviceRequestTableModel);
    }

    private void markRequestComplete() {
        int selectedRow = serviceRequestTable.getSelectedRow();
        if (selectedRow >= 0) {
            int requestId = (int) serviceRequestTable.getValueAt(selectedRow, 0);
            bookingDAO.updateServiceRequestStatus(requestId, "complete");
            updateTableViews(filterField.getText());
        } else {
            JOptionPane.showMessageDialog(this, "Please select a service request to mark as complete.");
        }
    }

    private void deleteRequest() {
        int selectedRow = serviceRequestTable.getSelectedRow();
        if (selectedRow >= 0) {
            int requestId = (int) serviceRequestTable.getValueAt(selectedRow, 0);
            bookingDAO.deleteServiceRequest(requestId);
            updateTableViews(filterField.getText());
        } else {
            JOptionPane.showMessageDialog(this, "Please select a service request to delete.");
        }
    }

    private void updateBookingStatus() {
        int selectedRow = bookingTable.getSelectedRow();
        if (selectedRow >= 0) {
            String bookingCode = (String) bookingTable.getValueAt(selectedRow, 0);
            String[] options = {"not checked in", "checked in", "checked out"};
            String newStatus = (String) JOptionPane.showInputDialog(this, "Select new status for the booking:", "Update Booking Status", JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
            if (newStatus != null && newStatus.length() > 0) {
                bookingDAO.updateBookingStatus(bookingCode, newStatus);
                updateTableViews(filterField.getText());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a booking to update the status.");
        }
    }
}
