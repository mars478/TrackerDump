
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.junit.Assert;
import org.junit.Test;

public class SQLiteTest extends Assert {

    public SQLiteTest() {
    }

    @Test
    public void create() throws ClassNotFoundException, SQLException {

        String dbName = "test";
        Class.forName("org.sqlite.JDBC");

        try (Connection c = DriverManager.getConnection("jdbc:sqlite:" + dbName + ".db");
                Statement stmt = c.createStatement()) {
            String sql = "create table if not exists topic "
                    + "(topic_id       INTEGER PRIMARY KEY  NOT NULL,"
                    + " forum_id       INTEGER    NOT NULL, "
                    + " forum_name     TEXT, "
                    + " topic_hash     TEXT, "
                    + " topic_name     TEXT,"
                    + " topic_size     INTEGER, "
                    + " topic_date     TEXT) ";
            stmt.executeUpdate(sql);

            stmt.execute("INSERT INTO topic "
                    + "(forum_id,forum_name,topic_hash,topic_name,topic_size,topic_date) "
                    + "values "
                    + "(1,'fname','hash','tname',100,'date')");

            ResultSet rs = stmt.executeQuery("select * from topic");
            while (rs.next()) {
                System.out.println("topic_id:" + rs.getInt("topic_id"));
                int i = 0;
            }
        }

    }

}
