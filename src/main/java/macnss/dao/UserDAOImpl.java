package macnss.dao;
import macnss.db.DatabaseConnection;
import macnss.model.Agent;
import macnss.model.Patient;

import java.sql.*;

public class UserDAOImpl implements UserDAO {
    protected Connection connection;

    public UserDAOImpl(Connection connection) {
        this.connection = connection;
    }

    public UserDAOImpl() {

    }

    @Override
    public Patient authenticatePatient(int matricule, String password) {
        String matriculeStr = String.valueOf(matricule);
        String query = "SELECT * FROM patients WHERE matricule = ? AND password = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, matriculeStr);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String birth_date = resultSet.getString("birth_date");

                return new Patient(id, name, email, password, matricule,birth_date);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    public Agent authenticateAgent(String email, String password) {
        String query = "SELECT * FROM agents WHERE email = ? AND password = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");

                return new Agent(id, name, email, password);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
    @Override
    public boolean addAgent(Agent agent) {
        String sql = "INSERT INTO agents (name, email, password) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, agent.getName());
            preparedStatement.setString(2, agent.getEmail());
            preparedStatement.setString(3, agent.getPassword());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public Patient addPatient(Patient patient) {

        Connection connection=null;
            String sql = "INSERT INTO patients (name, email, password, matricule) VALUES (?, ?, ?, ?)";
            try  {
                connection=DatabaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setString(1, patient.getName());
                preparedStatement.setString(2, patient.getEmail());
                preparedStatement.setString(3, patient.getPassword());
                preparedStatement.setInt(4, patient.getMatricule());

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        int userId = generatedKeys.getInt(1);
                        System.out.println(userId);
                        patient.setId(userId);
                    }
                    return patient;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        return null;
    }

    public Patient getPatientByMatricule(int matricule){
        Connection connection=null;
        String sql = "SELECT * FROM patients WHERE matricule = ?";
        try {
            connection=DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, String.valueOf(matricule));
             ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new Patient(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getString("email"), resultSet.getString("password"),resultSet.getInt("matricule"),resultSet.getString("birth_date"));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

}

