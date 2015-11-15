
import com.mars.trackerdump.fs.TopicReader;
import java.io.IOException;
import org.junit.Assert;
import org.junit.Test;

public class FileReaderTest extends Assert {

    String path = "D:\\rutracker\\rutracker-torrents";

    public FileReaderTest() {
    }

    @Test
    public void testRead() throws IOException {
        TopicReader tr = (TopicReader) new TopicReader().setPath(path);
        tr.scanPath();

    }

}
