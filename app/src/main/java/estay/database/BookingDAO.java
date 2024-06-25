package estay.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BookingDAO {
    public static class BookingInfo {
        public String status;
        public java.sql.Timestamp expiration;

        public BookingInfo(String status, java.sql.Timestamp expiration) {
            this.status = status;
            this.expiration = expiration;
        }
    }

    public BookingInfo validateUser(String bookingCode, String creditCardLast4) {
        String query = "SELECT status, expiration FROM bookings WHERE booking_id = ? AND payment_method_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, bookingCode);
            preparedStatement.setString(2, creditCardLast4);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String status = resultSet.getString("status");
                java.sql.Timestamp expiration = resultSet.getTimestamp("expiration");
                return new BookingInfo(status, expiration);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
