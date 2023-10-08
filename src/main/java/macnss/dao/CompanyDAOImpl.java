package macnss.dao;

import macnss.db.DatabaseConnection;
import macnss.model.Company;
import macnss.model.Patient;
import macnss.model.EmployeeCompany;

import java.sql.*;


public class CompanyDAOImpl implements CompanyDAO{
    private int companyId;
    public int getConnectedCompanyId() {
        return companyId;
    }
    public  void setConnectedCompanyId(int companyId){
        this.companyId=companyId;
    }
    public Company addCompany(Company company) {
        Connection connection = null;
        String sql = "INSERT INTO companies (name, email, password) VALUES (?, ?, ?)";
        try {
            connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, company.getName());
            preparedStatement.setString(2, company.getEmail());
            preparedStatement.setString(3, company.getPassword());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int userId = generatedKeys.getInt(1);
                    System.out.println("Generated User ID: " + userId);
                    company.setId(userId);
                }
                return company;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public Company authenticateCompany(String email, String password) {
        Connection connection = null;
        Company connectedCompany = null;

        String query = "SELECT * FROM companies WHERE email = ? AND password = ?";
        try {
            connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");

                connectedCompany = new Company();
                connectedCompany.setId(id);
                connectedCompany.setName(name);
                connectedCompany.setEmail(email);
                connectedCompany.setPassword(password);

                setConnectedCompanyId(id);
                return connectedCompany;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }



    public EmployeeCompany addEmployee(Patient employee, int companyId, int matricule, double salary) {
         Patient patient = getPatientByMatricule(matricule);

        if (patient == null) {
             Connection connection = null;
            UserDAOImpl userDAO = new UserDAOImpl(connection);
            patient = userDAO.addPatient(employee);
        }

         String sql = "INSERT INTO company_employee (salary, id_company, id_patient) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setDouble(1, salary);
            preparedStatement.setInt(2, companyId);
            preparedStatement.setInt(3, patient.getId());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int employeeId = generatedKeys.getInt(1);

                     EmployeeCompany employeeCompany = new EmployeeCompany();
                    employeeCompany.setId(employeeId);
                    employeeCompany.setSalary(salary);

                    return employeeCompany;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

     public Patient getPatientByMatricule(int matricule) {
        Connection connection=null;
        String query = "SELECT * FROM patients WHERE matricule = ?";
        try   {
            connection=DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1,String.valueOf(matricule));
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                String birth_date = resultSet.getString("birth_date");

                 return new Patient(id, name, email, password, matricule,birth_date);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
