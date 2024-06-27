package estay.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.*;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://69.215.107.194:3306/eStayDB";
    private static final String USER = "aaqil";
    private static final String PASSWORD = "aaqil";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}