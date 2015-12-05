
import com.mars.trackerdump.config.Config;
import com.mars.trackerdump.fs.TopicReader;
import java.io.IOException;
import java.sql.SQLException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class FileReaderTest extends Assert {

    String path = "D:\\rutracker\\rutracker-torrents";

    public FileReaderTest() {
    }

    @BeforeClass
    public static void setup() throws ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
    }

    @Test
    public void testRead() throws IOException {

        TopicReader tr = (TopicReader) new TopicReader().setPath(path);
        tr.scanPath();

    }

}
