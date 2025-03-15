package vartak;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class sample {

    public static void main(String[] args) {
        String databaseURL = "jdbc:ucanaccess://E://fintrackdbs.accdb"; // Replace with your database path

        try (Connection connection = DriverManager.getConnection(databaseURL))
        {
            // Statement statement = connection.createStatement();

            //  ResultSet resultSet = statement.executeQuery("SELECT * FROM sample")) { // Replace with your table name

            // while (resultSet.next()) {
            //     // Process the result set
            //     System.out.println(resultSet.getString("name")); // Replace with your column name
            // }

             String insertSQL = "INSERT INTO SAMPLE (NAME, AGE) VALUES (?, ?)"; // Replace with your table and column names
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {

                // Set the values for the placeholders
                preparedStatement.setString(1, "VISHAL"); // Replace with your actual values
                preparedStatement.setInt(2, 25);

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Record inserted successfully!");
                } else {
                    System.out.println("Record insertion failed.");
                }
            }





        } 
        catch (SQLException e) {
            //System.err.println("Error connecting to database: " + e.getMessage());
            System.err.println("Error inserting record: " + e.getMessage());
        }
    }
}