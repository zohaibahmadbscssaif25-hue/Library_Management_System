package dao;
import java.sql.*;
import db.ConnectionDb;

public class Member {
    Connection conn = ConnectionDb.getConnection();

    public void GetAllMembers() {
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(
                "SELECT MemberID,FullName,Email,Phone,MemberType,JoinDate,Status FROM Member")) {
            while (rs.next()) {
                Output.println(
                    "ID: "     + rs.getInt("MemberID")      +
                    " | Name: "  + rs.getString("FullName")   +
                    " | Email: " + rs.getString("Email")      +
                    " | Phone: " + rs.getString("Phone")      +
                    " | Type: "  + rs.getString("MemberType") +
                    " | Date: "  + rs.getString("JoinDate")   +
                    " | Status: "+ rs.getString("Status"));
            }
        } catch (Exception e) { Output.println("Error: " + e.getMessage()); }
    }

    public void AddMember(int MemberID, String FullName, String Email,
                          String memberType, String status) {
        String query = "INSERT INTO Member (MemberID,FullName,Email,MemberType,Status) VALUES(?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, MemberID);
            ps.setString(2, FullName);
            ps.setString(3, Email);
            ps.setString(4, memberType);
            ps.setString(5, status);
            ps.executeUpdate();
            Output.println("Member added successfully!");
        } catch (Exception e) { Output.println("Error: " + e.getMessage()); }
    }

    public void UpdateMemberStatus(String status, int MemberID) {
        try (PreparedStatement ps = conn.prepareStatement(
                "UPDATE Member SET Status=? WHERE MemberID=?")) {
            ps.setString(1, status);
            ps.setInt(2, MemberID);
            ps.executeUpdate();
            Output.println("Member status updated!");
        } catch (Exception e) { Output.println("Error: " + e.getMessage()); }
    }
}