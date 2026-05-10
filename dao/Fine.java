package dao;
import java.sql.*;
import db.ConnectionDb;

public class Fine {
    Connection conn = ConnectionDb.getConnection();

    public void getUnpaidFines() {
        String query = "SELECT m.FullName, bk.Title, f.Amount, f.PaidStatus " +
                       "FROM Fine f " +
                       "JOIN Borrowed b ON f.BorrowID = b.BorrowID " +
                       "JOIN Member m   ON b.MemberID = m.MemberID " +
                       "JOIN Book bk    ON b.BookID   = bk.BookID " +
                       "WHERE f.PaidStatus = 'Unpaid'";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Output.println(
                    "Member: "  + rs.getString("FullName") +
                    " | Book: " + rs.getString("Title")    +
                    " | Fine: " + rs.getDouble("Amount")   +
                    " | Status: "+ rs.getString("PaidStatus"));
            }
        } catch (SQLException e) { Output.println("Error: " + e.getMessage()); }
    }

    public void calculateFine() {
        String query = "SELECT m.FullName, bk.Title, b.DueDate, " +
                       "DATEDIFF(CURDATE(),b.DueDate) AS OverdueDays, " +
                       "DATEDIFF(CURDATE(),b.DueDate)*10 AS FineAmount " +
                       "FROM Borrowed b " +
                       "JOIN Member m ON b.MemberID = m.MemberID " +
                       "JOIN Book bk  ON b.BookID   = bk.BookID " +
                       "WHERE b.Status = 'Overdue'";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Output.println(
                    "Member: "  + rs.getString("FullName")  +
                    " | Book: " + rs.getString("Title")     +
                    " | Due: "  + rs.getDate("DueDate")     +
                    " | Days: " + rs.getInt("OverdueDays")  +
                    " | Fine: " + rs.getInt("FineAmount"));
            }
        } catch (SQLException e) { Output.println("Error: " + e.getMessage()); }
    }

    public void markFinePaid(int FineID) {
        try (PreparedStatement ps = conn.prepareStatement(
                "UPDATE Fine SET PaidStatus='Paid',PaidDate=CURDATE() WHERE FineID=?")) {
            ps.setInt(1, FineID);
            ps.executeUpdate();
            Output.println("Fine marked as paid!");
        } catch (SQLException e) { Output.println("Error: " + e.getMessage()); }
    }
}