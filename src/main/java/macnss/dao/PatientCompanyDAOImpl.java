package macnss.dao;

import macnss.db.DatabaseConnection;
import macnss.model.Patient;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PatientCompanyDAOImpl {
    public int getCompanyEmployeeIdByMatriculeAndCompanyId(int matricule, int companyId) {
        UserDAOImpl userDAO= new UserDAOImpl();
        Patient patient = userDAO.getPatientByMatricule(matricule);
        int patientId=patient.getId();
        System.out.println("PatientIdiiiiiiiiiiiiiii");
        System.out.println(patientId);
        System.out.println("CompanyIdiiiiiiiiiiiiiii");
        System.out.println(companyId);
        Connection connection = null;
        String sql = "SELECT id FROM company_employee WHERE id_patient = ? AND id_company = ?";

        try {
            connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, patientId);
            preparedStatement.setInt(2, companyId);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close the connection if needed
            // Handle any exceptions
        }

        return -1; // Return -1 or any other appropriate value to indicate failure
    }

}
