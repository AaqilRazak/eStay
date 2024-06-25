package estay.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO {

    public static class BookingInfo {
        public String status;
        public java.sql.Timestamp expiration;

        public BookingInfo(String status, java.sql.Timestamp expiration) {
            this.status = status;
            this.expiration = expiration;
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

    public static class ServiceOffering {
        public String requestType;
        public double price;

        public ServiceOffering(String requestType, double price) {
            this.requestType = requestType;
            this.price = price;
        }
    }

    public BookingInfo validateUser(String bookingCode, String creditCardLast4) {
        String query = "SELECT status, expiration FROM bookings WHERE booking_id = ? AND credit_card_last4 = ?";
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

    public List<ServiceOffering> getServiceOfferings() {
        List<ServiceOffering> offerings = new ArrayList<>();
        String query = "SELECT request_type, price FROM servicerequests";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                String requestType = resultSet.getString("request_type");
                double price = resultSet.getDouble("price");
                offerings.add(new ServiceOffering(requestType, price));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return offerings;
    }

    public void saveServiceRequest(String bookingCode, String requestType, double price) {
        String query = "INSERT INTO servicerequests (booking_id, request_type, request_date, status, price) VALUES (?, ?, NOW(), 'pending', ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, bookingCode);
            preparedStatement.setString(2, requestType);
            preparedStatement.setDouble(3, price);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
