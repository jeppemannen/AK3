package no.jeppe.jbdc;

import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

public class ParticipantDao {

    private DataSource dataSource;

    public ParticipantDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void insertParticipant(String participantName) {

        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(
                    "insert into participants (name) values (?)");
            statement.setString(1, participantName);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> listAll() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "select * from participants"
            )) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    List<String> result = new ArrayList<>();

                    while (resultSet.next()) {
                        result.add(resultSet.getString("name"));
                    }

                    return result;
                }
            }
        }
    }

    public static void main(String[] args) throws IOException, SQLException {
        System.out.println("Enter the name of a participant of the assignment: ");
        String participantName = new Scanner(System.in).nextLine();

        Properties properties = new Properties();
        properties.load(new FileReader("assignment.properties"));

        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUrl("jdbc:postgresql://localhost:5432/assignment");
        dataSource.setUser("assignment");
        dataSource.setPassword(properties.getProperty("datasource.password"));
        ParticipantDao participantDao = new ParticipantDao(dataSource);
        participantDao.insertParticipant(participantName);

        System.out.println(participantDao.listAll());

    }
}