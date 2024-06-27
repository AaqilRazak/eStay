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

        // Setup filter field
        JPanel filterPanel = new JPanel(new BorderLayout());
        filterField = new JTextField();
        filterField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                updateTableViews(filterField.getText());
            }
        });
        filterPanel.add(new JLabel("Filter by Booking ID:"), BorderLayout.WEST);
        filterPanel.add(filterField, BorderLayout.CENTER);

        // Setup tables
        bookingTable = new JTable(new DefaultTableModel(new Object[]{"Booking ID", "Room ID", "Check-In Date", "Check-Out Date", "Status", "Accumulated Cost"}, 0));
        serviceRequestTable = new JTable(new DefaultTableModel(new Object[]{"Request ID", "Request Type", "Request Date", "Status", "Price", "Quantity"}, 0));

        // Setup buttons
        JPanel buttonPanel = new JPanel();
        JButton markRequestCompleteButton = new JButton("Mark Request Complete");
        JButton deleteRequestButton = new JButton("Delete Request");
        JButton updateBookingStatusButton = new JButton("Update Booking Status");
        JButton logoutButton = new JButton("Logout");

        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
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
