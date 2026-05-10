package dao;
import java.sql.*;
import db.ConnectionDb;

public class Book {
    Connection conn = ConnectionDb.getConnection();

    public void getAllBooks() {
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT BookID, Title FROM Book")) {
            while (rs.next()) {
                Output.println("Book ID: " + rs.getInt("BookID") +
                               " | Title: " + rs.getString("Title"));
            }
        } catch (Exception e) { Output.println("Error: " + e.getMessage()); }
    }

    public void searchBookByName(String bookName) {
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT BookID, Title FROM Book WHERE Title LIKE ?")) {
            ps.setString(1, "%" + bookName + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Output.println("Book ID: " + rs.getInt("BookID") +
                               " | Title: " + rs.getString("Title"));
            }
        } catch (Exception e) { Output.println("Error: " + e.getMessage()); }
    }

    public void searchBookByISBN(String isbn) {
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT BookID, Title, ISBN FROM Book WHERE ISBN LIKE ?")) {
            ps.setString(1, "%" + isbn + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Output.println("Book ID: " + rs.getInt("BookID") +
                               " | Title: " + rs.getString("Title") +
                               " | ISBN: "  + rs.getString("ISBN"));
            }
        } catch (Exception e) { Output.println("Error: " + e.getMessage()); }
    }

    public void AvailableBooks() {
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(
                "SELECT BookID, Title, AuthorName FROM Book WHERE AvailCopies > 0")) {
            while (rs.next()) {
                Output.println("Book ID: " + rs.getInt("BookID") +
                               " | Title: "  + rs.getString("Title") +
                               " | Author: " + rs.getString("AuthorName"));
            }
        } catch (Exception e) { Output.println("Error: " + e.getMessage()); }
    }

    public void AddBooks(String ISBN, String Title, String AuthorName,
                         String Genre, int PublishYear, int AvailCopies, int TotalCopies) {
        String query = "INSERT INTO Book (ISBN,Title,AuthorName,Genre,PublishYear,AvailCopies,TotalCopies) VALUES(?,?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, ISBN);
            ps.setString(2, Title);
            ps.setString(3, AuthorName);
            ps.setString(4, Genre);
            ps.setInt(5, PublishYear);
            ps.setInt(6, AvailCopies);
            ps.setInt(7, TotalCopies);
            ps.executeUpdate();
            Output.println("Book added successfully!");
        } catch (Exception e) { Output.println("Error: " + e.getMessage()); }
    }

    public void UpdateBookcopies(int BookID, int AvailCopies) {
        try (PreparedStatement ps = conn.prepareStatement(
                "UPDATE Book SET AvailCopies=? WHERE BookID=?")) {
            ps.setInt(1, AvailCopies);
            ps.setInt(2, BookID);
            ps.executeUpdate();
            Output.println("Book copies updated!");
        } catch (Exception e) { Output.println("Error: " + e.getMessage()); }
    }

    public void DeleteBook(int BookID) {
        try (PreparedStatement ps1 = conn.prepareStatement(
                "DELETE FROM Borrowed WHERE BookID=?");
             PreparedStatement ps2 = conn.prepareStatement(
                "DELETE FROM Book WHERE BookID=?")) {
            ps1.setInt(1, BookID);
            ps1.executeUpdate();
            ps2.setInt(1, BookID);
            ps2.executeUpdate();
            Output.println("Book deleted!");
        } catch (Exception e) { Output.println("Error: " + e.getMessage()); }
    }
}