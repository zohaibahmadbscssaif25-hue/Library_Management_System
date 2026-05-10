package dao;
import java.sql.*;
import db.ConnectionDb;

public class Reports {
    Connection conn = ConnectionDb.getConnection();

    public void getTotalBooksByGenre() {
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(
                "SELECT Genre, COUNT(*) AS TotalBooks FROM Book GROUP BY Genre")) {
            while (rs.next()) {
                Output.println("Genre: " + rs.getString("Genre") +
                               " | Total: " + rs.getInt("TotalBooks"));
            }
        } catch (SQLException e) { Output.println("Error: " + e.getMessage()); }
    }

    public void getTotalFines() {
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(
                "SELECT PaidStatus, SUM(Amount) AS TotalAmount FROM Fine GROUP BY PaidStatus")) {
            while (rs.next()) {
                Output.println("Status: " + rs.getString("PaidStatus") +
                               " | Total: " + rs.getDouble("TotalAmount"));
            }
        } catch (SQLException e) { Output.println("Error: " + e.getMessage()); }
    }

    public void getMemberCountByType() {
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(
                "SELECT MemberType, COUNT(*) AS Total FROM Member GROUP BY MemberType")) {
            while (rs.next()) {
                Output.println("Type: "  + rs.getString("MemberType") +
                               " | Total: " + rs.getInt("Total"));
            }
        } catch (SQLException e) { Output.println("Error: " + e.getMessage()); }
    }
}