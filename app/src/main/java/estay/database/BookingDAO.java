package estay.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BookingDAO {
    public static class BookingInfo {
        public String status;
        public java.sql.Timestamp expiration;
        public double dayRate;
        public java.sql.Timestamp checkInDate;
        public double accumulatedCost;
        public String roomId;

        public BookingInfo(String status, java.sql.Timestamp expiration, double dayRate, java.sql.Timestamp checkInDate, double accumulatedCost, String roomId) {
            this.status = status;
            this.expiration = expiration;
            this.dayRate = dayRate;
            this.checkInDate = checkInDate;
            this.accumulatedCost = accumulatedCost;
            this.roomId = roomId;
        }
    }

    public static class GuestInfo {
        public String name;
        public String securityQuestion1;
        public String securityQuestion2;

        public GuestInfo(String name, String securityQuestion1, String securityQuestion2) {
            this.name = name;
            this.securityQuestion1 = securityQuestion1;
            this.securityQuestion2 = securityQuestion2;
        }
    }

    public BookingInfo validateUser(String bookingCode, String creditCardLast4) {
        String query = "SELECT status, expiration, day_rate, check_in_date, accumulated_cost, room_id FROM bookings WHERE booking_id = ? AND credit_card_last4 = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, bookingCode);
            preparedStatement.setString(2, creditCardLast4);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String status = resultSet.getString("status");
                java.sql.Timestamp expiration = resultSet.getTimestamp("expiration");
                double dayRate = resultSet.getDouble("day_rate");
                java.sql.Timestamp checkInDate = resultSet.getTimestamp("check_in_date");
                double accumulatedCost = resultSet.getDouble("accumulated_cost");
                String roomId = resultSet.getString("room_id");
                return new BookingInfo(status, expiration, dayRate, checkInDate, accumulatedCost, roomId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public GuestInfo getSecurityQuestions(String bookingCode) {
        String query = "SELECT name, security_question_1, security_question_2 FROM guests WHERE guest_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, bookingCode);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String name = resultSet.getString("name");
                String question1 = resultSet.getString("security_question_1");
                String question2 = resultSet.getString("security_question_2");
                return new GuestInfo(name, question1, question2);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean validateSecurityAnswers(String bookingCode, String answer1, String answer2) {
        String query = "SELECT security_answer_1, security_answer_2 FROM guests WHERE guest_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, bookingCode);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String correctAnswer1 = resultSet.getString("security_answer_1");
                String correctAnswer2 = resultSet.getString("security_answer_2");
                return correctAnswer1.equalsIgnoreCase(answer1) && correctAnswer2.equalsIgnoreCase(answer2);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
public BookingInfo getBookingDetails(String bookingCode) {
        String query = "SELECT status, expiration, day_rate, check_in_date, accumulated_cost, room_id FROM bookings WHERE booking_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, bookingCode);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String status = resultSet.getString("status");
                java.sql.Timestamp expiration = resultSet.getTimestamp("expiration");
                double dayRate = resultSet.getDouble("day_rate");
                java.sql.Timestamp checkInDate = resultSet.getTimestamp("check_in_date");
                double accumulatedCost = resultSet.getDouble("accumulated_cost");
                String roomId = resultSet.getString("room_id");
                return new BookingInfo(status, expiration, dayRate, checkInDate, accumulatedCost, roomId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateAccumulatedCost(String bookingCode, double additionalCost) {
        String query = "UPDATE bookings SET accumulated_cost = accumulated_cost + ? WHERE booking_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setDouble(1, additionalCost);
            preparedStatement.setString(2, bookingCode);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
