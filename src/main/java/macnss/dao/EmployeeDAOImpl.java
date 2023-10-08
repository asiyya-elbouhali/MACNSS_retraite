package macnss.dao;

import macnss.db.DatabaseConnection;
import macnss.model.Employee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EmployeeDAOImpl implements EmployeeDAO {
     private Connection connection=null;

    public EmployeeDAOImpl() {
        this.connection = connection;
    }

    @Override
    public boolean addEmployee(Employee employee) {
        String sql = "INSERT INTO employees ( name, email, password, salary, job_title, birth_date) " +
                "VALUES (  ?, ?, ?, ?, ?, ?)RETURNING id";

        try   {
            connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
             statement.setString(1, employee.getName());
            statement.setString(2, employee.getEmail());
            statement.setString(3, employee.getPassword());
            statement.setDouble(4, employee.getSalary());
            statement.setString(5, employee.getJobTitle());
            statement.setString(6, employee.getBirthDate());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int employeeId = resultSet.getInt("id");
                    CompanyDAOImpl companyDAOImpl=new CompanyDAOImpl();
                      int companyId=  companyDAOImpl.getConnectedCompanyId();

                      if (addEmployeeToCompany(employeeId, companyId)) {
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean addEmployeeToCompany(int employeeId, int companyId) {
        String insertQuery = "INSERT INTO employee_company (employee_id, company_id) VALUES (?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(insertQuery)) {
            statement.setInt(1, employeeId);
            statement.setInt(2, companyId);

            int rowsInserted = statement.executeUpdate();

            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    public int getEmployeeTotalWorkedDays(int employeeId) {

        String sql = "SELECT COALESCE(SUM(wd.days_nb), 0) AS total_worked_days " +
                "FROM company_employee ce " +
                "LEFT JOIN worked_days wd ON ce.id = wd.employee_company_id " +
                "WHERE ce.id_patient = ?";

        try  {
            connection=DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, employeeId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                 return resultSet.getInt("total_worked_days");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }


    public double calculateRetirementPension(int patientId) {
        int totalWorkedDays = getEmployeeTotalWorkedDays(patientId);
        int age =calculatePatientAge( patientId);
        if (age >= 55 &&totalWorkedDays >= 1320 ) {
             double avgSalary = calculateAverageSalary(patientId);
             double pension = 0.5 * avgSalary;
            if (totalWorkedDays > 3240) {
                int additionalDays = totalWorkedDays - 3240;
                pension += (additionalDays / 216) * 0.01 * avgSalary;
            }
             double maxPension = 0.7 * avgSalary;
            pension = Math.min(pension, maxPension);
            return pension;
        } else {
            return 0;
        }
    }

    private double calculateAverageSalary(int patientId) {
        String sql = "SELECT AVG((ce.salary / 26) * COALESCE(wd.days_nb, 0)) AS avg_monthly_salary " +
                "FROM company_employee ce " +
                "LEFT JOIN worked_days wd ON ce.id = wd.employee_company_id " +
                "JOIN period p ON wd.periode_id = p.id " +
                "WHERE ce.id_patient = ? " +
                "AND p.month >= (EXTRACT(MONTH FROM current_date) - 96 + EXTRACT(YEAR FROM current_date))";

        try   {
            connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, patientId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getDouble("avg_monthly_salary");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0.0;
    }


    public int calculatePatientAge(int patientId) {
        String sql = "SELECT birth_date FROM patients WHERE id = ?";
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        try {
            connection=DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, patientId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String birthDateStr = resultSet.getString("birth_date");
                Date birthDate = sdf.parse(birthDateStr);
                Date currentDate = new Date();

                long ageInMillis = currentDate.getTime() - birthDate.getTime();
                int ageInYears = (int) (ageInMillis / (1000L * 60 * 60 * 24 * 365.25));

                return ageInYears;
            }
        } catch (SQLException | ParseException e) {
            e.printStackTrace();
        }

        return -1;
    }
}
