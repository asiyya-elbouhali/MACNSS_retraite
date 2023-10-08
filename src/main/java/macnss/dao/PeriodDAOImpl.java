package macnss.dao;

import macnss.db.DatabaseConnection;
import macnss.model.Patient;
import macnss.model.Periode;

import java.sql.*;

public class PeriodDAOImpl {

    public Periode getPeriodeIdByMonth(int month) {
        Connection connection = null;
        String query = "SELECT * FROM period WHERE month = ?";
        try {
            connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setInt(1, month); // Use setInt for an integer value
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                int monthValue = resultSet.getInt("month");

                // Create and return a Periode object with retrieved details
                return new Periode(id, monthValue);
            } else {
                // If no matching record is found, return a default Periode object
                return new Periode(-1, 0); // You can adjust these default values as needed
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // Handle any exceptions
    }



}
