package norm.ops;

import norm.exception.IllegalOPSArgument;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by akim on 18.01.18.
 */
public class OPSTest {

    @Test
    public void testSum() throws Exception, IllegalOPSArgument {
        OPS ops = new OPS();
        int a = 5;
        int b = 10;
        ops.addRight(OpsItem.number(a));
        ops.addRight(OpsItem.number(b));
        ops.addRight(OpsItem.ariphmeticSign("+"));
        assertFalse(ops.move());
        assertFalse(ops.move());
        assertFalse(ops.move());
        assertTrue(ops.move());
        assertEquals(ops.getMagazinTop(), a + b);
    }

}