
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.annotation.Bean;

/*
 configuration.setProperty("connection.driver_class", "org.postgresql.Driver");
 configuration.setProperty("hibernate.connection.url", "jdbc:postgresql://192.168.1.6:5433/postgres");
 configuration.setProperty("hibernate.connection.username", "postgres");
 configuration.setProperty("hibernate.connection.password", "");
 configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
 configuration.setProperty("show_sql", "true");
 */
public class PGTest extends Assert {

    public PGTest() {
    }

    @BeforeClass
    public static void setup() throws ClassNotFoundException {
        Class.forName("org.postgresql.Driver");

    }

    @Test
    public void create() throws SQLException {

        try (Connection c = DriverManager.getConnection("jdbc:postgresql://192.168.1.6:5433/postgres", "postgres", "");
                Statement stmt = c.createStatement()) {

            stmt.execute("INSERT INTO rutracker.topic "
                    + "(forum_id ,"
                    + "  forum_name ,"
                    + "  topic_hash ,"
                    + "  topic_name ,"
                    + "  topic_size ,"
                    + "  topic_date) "
                    + "values "
                    + "(1,'fname','hash','tname',100,'01.01.2000'::date)");

            ResultSet rs = stmt.executeQuery("select * from rutracker.topic");
            while (rs.next()) {
                System.out.println("id:" + rs.getInt("id"));
                int i = 0;
            }
        }

    }

}
