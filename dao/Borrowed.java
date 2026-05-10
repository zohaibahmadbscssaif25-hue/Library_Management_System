package dao;
import java.sql.*;
import db.ConnectionDb;

public class Borrowed {
    Connection conn = ConnectionDb.getConnection();

    public void issueBook(int BookID, int MemberID, int LibrarianID) {
        String query = "INSERT INTO Borrowed (BookID,MemberID,LibrarianID,IssueDate,DueDate,Status) " +
                       "VALUES (?,?,?,CURDATE(),DATE_ADD(CURDATE(),INTERVAL 14 DAY),'Borrowed')";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, BookID);
            ps.setInt(2, MemberID);
            ps.setInt(3, LibrarianID);
            ps.executeUpdate();
            Output.println("Book issued successfully!");
        } catch (SQLException e) { Output.println("Error: " + e.getMessage()); }
    }

    public void returnBook(int BorrowID) {
        try (PreparedStatement ps = conn.prepareStatement(
                "UPDATE Borrowed SET ReturnDate=CURDATE(),Status='Returned' WHERE BorrowID=?")) {
            ps.setInt(1, BorrowID);
            ps.executeUpdate();
            Output.println("Book returned successfully!");
        } catch (SQLException e) { Output.println("Error: " + e.getMessage()); }
    }

    public void getAllBorrowedBooks() {
        String query = "SELECT b.BorrowID, bk.Title, m.FullName, b.IssueDate, b.DueDate, b.Status " +
                       "FROM Borrowed b " +
                       "JOIN Book bk ON b.BookID = bk.BookID " +
                       "JOIN Member m ON b.MemberID = m.MemberID " +
                       "WHERE b.Status = 'Borrowed'";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Output.println(
                    "BorrowID: " + rs.getInt("BorrowID")   +
                    " | Title: "  + rs.getString("Title")   +
                    " | Member: " + rs.getString("FullName")+
                    " | Issue: "  + rs.getDate("IssueDate") +
                    " | Due: "    + rs.getDate("DueDate")   +
                    " | Status: " + rs.getString("Status"));
            }
        } catch (SQLException e) { Output.println("Error: " + e.getMessage()); }
    }

    public void getOverdueBooks() {
        String query = "SELECT b.BorrowID, bk.Title, m.FullName, m.Phone, b.DueDate " +
                       "FROM Borrowed b " +
                       "JOIN Book bk ON b.BookID = bk.BookID " +
                       "JOIN Member m ON b.MemberID = m.MemberID " +
                       "WHERE b.Status = 'Overdue'";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Output.println(
                    "BorrowID: " + rs.getInt("BorrowID")   +
                    " | Title: "  + rs.getString("Title")   +
                    " | Member: " + rs.getString("FullName")+
                    " | Phone: "  + rs.getString("Phone")   +
                    " | Due: "    + rs.getDate("DueDate"));
            }
        } catch (SQLException e) { Output.println("Error: " + e.getMessage()); }
    }

    public void getMemberBorrowHistory(int MemberID) {
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT bk.Title, b.IssueDate, b.DueDate, b.ReturnDate, b.Status " +
                "FROM Borrowed b JOIN Book bk ON b.BookID=bk.BookID WHERE b.MemberID=?")) {
            ps.setInt(1, MemberID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Output.println(
                    "Title: "   + rs.getString("Title")     +
                    " | Issue: " + rs.getDate("IssueDate")  +
                    " | Due: "   + rs.getDate("DueDate")    +
                    " | Return: "+ rs.getDate("ReturnDate") +
                    " | Status: "+ rs.getString("Status"));
            }
        } catch (SQLException e) { Output.println("Error: " + e.getMessage()); }
    }
}