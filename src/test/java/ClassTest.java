
import java.math.BigDecimal;
import org.junit.Assert;
import org.junit.Test;

public class ClassTest extends Assert {

    @Test
    public void numeric() {
        int a = 0;
        float b = 0;
        long r = 0;
        double t = 0;
        byte s = 0;
        BigDecimal c = new BigDecimal(0);

        assertTrue((Object) a instanceof Number);
        assertTrue((Object) b instanceof Number);
        assertTrue((Object) r instanceof Number);
        assertTrue((Object) t instanceof Number);
        assertTrue((Object) s instanceof Number);
        assertTrue((Object) c instanceof Number);

        assertTrue(Number.class.isAssignableFrom(((Object) t).getClass()));
    }

}
