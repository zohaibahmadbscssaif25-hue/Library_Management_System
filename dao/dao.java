package dao;

import java.sql.*;
import db.ConnectionDb;

public class dao{

    Connection conn = ConnectionDb.getConnection();


    public void DisplayResult() {
        // We JOIN the tables on user_id to see the Name AND the Skills together
        String query =  "select   BookID, Title, AuthorName, AvailCopies,ISBN from Book " +
            "WHERE ISBN = '9780590353';";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            System.out.println("--- Freelancer Market Directory ---");
            while (rs.next()) {
                int BookID = rs.getInt("BookID");
                String Title = rs.getString("Title");
                String AuthorName = rs.getString("AuthorName");
                int AvailCopies=rs.getInt("AvailCopies");

                String ISBN = rs.getString("ISBN");

                System.out.println("BookID : " + BookID + " | Title: " + Title + " | AuthorName: " + AuthorName
                 +" | AvailCopies: "+ AvailCopies +" | ISBN: "+ISBN);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}