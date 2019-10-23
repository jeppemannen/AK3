package no.jeppe.jbdc;

import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;


public class ProjectTest {

    @Test
    void shouldRetrieveProjectParticipant() throws SQLException {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setUrl("jdbc:h2:mem:test");

        dataSource.getConnection().createStatement().executeUpdate(
                "create table participants (name varchar(100))"
        );

        ParticipantDao dao = new ParticipantDao(dataSource);
        String participantName = pickOne(new String[] {"Knut", "Asbjørn", "Magda", "Silje"});
        dao.insertParticipant(participantName);
        assertThat(dao.listAll()).contains(participantName);

    }

    private String pickOne(String[] strings) {
        return strings[new Random().nextInt(strings.length)];
    }
}
