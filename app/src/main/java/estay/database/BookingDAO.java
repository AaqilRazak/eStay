package estay.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class BookingDAO {

    public static class BookingInfo {
        public String status;
        public Timestamp expiration;
        public double dayRate;
        public Timestamp checkInDate;
        public double accumulatedCost;
        public String roomId;

        public BookingInfo(String status, Timestamp expiration, double dayRate, Timestamp checkInDate, double accumulatedCost, String roomId) {
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

    public static class ServiceOffering {
        public String requestType;
        public double price;

        public ServiceOffering(String requestType, double price) {
            this.requestType = requestType;
            this.price = price;
        }
    }

    public static class ServiceRequest {
        public int requestId;
        public String requestType;
        public Timestamp requestDate;
        public String status;
        public int quantity;
        public double price;

        public ServiceRequest(int requestId, String requestType, Timestamp requestDate, String status, int quantity, double price) {
            this.requestId = requestId;
            this.requestType = requestType;
            this.requestDate = requestDate;
            this.status = status;
            this.quantity = quantity;
            this.price = price;
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
                Timestamp expiration = resultSet.getTimestamp("expiration");
                double dayRate = resultSet.getDouble("day_rate");
                Timestamp checkInDate = resultSet.getTimestamp("check_in_date");
                double accumulatedCost = resultSet.getDouble("accumulated_cost");
                String roomId = resultSet.getString("room_id");
                return new BookingInfo(status, expiration, dayRate, checkInDate, accumulatedCost, roomId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateBookingStatus(String bookingCode, String status) {
        String query = "UPDATE bookings SET status = ? WHERE booking_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, status);
            preparedStatement.setString(2, bookingCode);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
        String query = "SELECT name, price FROM ServiceOfferings";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                String requestType = resultSet.getString("name");
                double price = resultSet.getDouble("price");
                offerings.add(new ServiceOffering(requestType, price));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return offerings;
    }

    public void saveServiceRequest(String bookingCode, String requestType, double price, int quantity) {
        String findOfferingIdQuery = "SELECT offering_id FROM ServiceOfferings WHERE name = ?";
        String insertRequestQuery = "INSERT INTO servicerequests (booking_id, offering_id, request_date, status, price, quantity) VALUES (?, ?, NOW(), 'pending', ?, ?)";
        
        try (Connection connection = DatabaseConnection.getConnection()) {
            // Step 1: Find the offering_id
            int offeringId = -1;
            try (PreparedStatement findOfferingIdStmt = connection.prepareStatement(findOfferingIdQuery)) {
                findOfferingIdStmt.setString(1, requestType);
                try (ResultSet resultSet = findOfferingIdStmt.executeQuery()) {
                    if (resultSet.next()) {
                        offeringId = resultSet.getInt("offering_id");
                    } else {
                        throw new SQLException("Service offering not found for type: " + requestType);
                    }
                }
            }
    
            // Step 2: Insert into servicerequests
            try (PreparedStatement insertRequestStmt = connection.prepareStatement(insertRequestQuery)) {
                insertRequestStmt.setString(1, bookingCode); // assuming bookingCode is actually booking_id
                insertRequestStmt.setInt(2, offeringId);
                insertRequestStmt.setDouble(3, price);
                insertRequestStmt.setInt(4, quantity);
                insertRequestStmt.executeUpdate();
            }
    
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

    public void decrementAccumulatedCost(String bookingCode, double cost) {
        String query = "UPDATE bookings SET accumulated_cost = accumulated_cost - ? WHERE booking_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setDouble(1, cost);
            preparedStatement.setString(2, bookingCode);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteServiceRequest(int requestId) {
        String query = "DELETE FROM servicerequests WHERE request_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, requestId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<ServiceRequest> getServiceRequests(String bookingCode) {
        List<ServiceRequest> requests = new ArrayList<>();
        String query = "SELECT sr.request_id, so.name AS request_type, sr.request_date, sr.status, sr.quantity, sr.price FROM servicerequests sr JOIN ServiceOfferings so ON sr.offering_id = so.offering_id WHERE sr.booking_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, bookingCode);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int requestId = resultSet.getInt("request_id");
                String requestType = resultSet.getString("request_type");
                Timestamp requestDate = resultSet.getTimestamp("request_date");
                String status = resultSet.getString("status");
                int quantity = resultSet.getInt("quantity");
                double price = resultSet.getDouble("price");
                requests.add(new ServiceRequest(requestId, requestType, requestDate, status, quantity, price));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
    }

    public BookingInfo getBookingDetails(String bookingCode) {
        String query = "SELECT status, expiration, day_rate, check_in_date, accumulated_cost, room_id FROM bookings WHERE booking_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, bookingCode);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String status = resultSet.getString("status");
                Timestamp expiration = resultSet.getTimestamp("expiration");
                double dayRate = resultSet.getDouble("day_rate");
                Timestamp checkInDate = resultSet.getTimestamp("check_in_date");
                double accumulatedCost = resultSet.getDouble("accumulated_cost");
                String roomId = resultSet.getString("room_id");
                return new BookingInfo(status, expiration, dayRate, checkInDate, accumulatedCost, roomId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

}