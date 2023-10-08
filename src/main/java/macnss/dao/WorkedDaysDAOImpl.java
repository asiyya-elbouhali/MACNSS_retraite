package macnss.dao;

import macnss.db.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class WorkedDaysDAOImpl {

    public boolean insertWorkedDays(int companyEmployeeId, int daysNb, int periodeId) {
        Connection connection = null; // Obtain your database connection

        // Define your SQL insert statement
        String sql = "INSERT INTO worked_days (employee_company_id, periode_id, days_nb) VALUES (?, ?, ?)";

        try  {
            connection= DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, companyEmployeeId);
            preparedStatement.setInt(2, periodeId);
            preparedStatement.setInt(3, daysNb);

            int rowsAffected = preparedStatement.executeUpdate();

            // If rowsAffected is greater than 0, it means the insert was successful
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // If the insert fails, return false
        return false;
    }

}
