package norm.ops;

import norm.exception.IllegalOPSArgument;
import org.junit.Test;
import utils.StringStream;

import java.io.BufferedInputStream;

import static norm.ops.OpsItem.*;
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
        ops.addRight(number(a));
        ops.addRight(number(b));
        ops.addRight(OpsItem.ariphmeticSign("+"));
        assertFalse(ops.move());
        assertFalse(ops.move());
        assertFalse(ops.move());
        assertTrue(ops.move());
        assertEquals(ops.getMagazinTop(), a + b);
    }

    @Test
    public void testAriphmetic() throws Exception, IllegalOPSArgument {
        OPS ops = new OPS();
        //3 2 1 * + == (2*1) + 3
        int a = 3;
        int b = 2;
        int c = 1;
        ops.addRight(number(a));
        ops.addRight(number(b));
        ops.addRight(number(c));
        ops.addRight(ariphmeticSign("*"));
        ops.addRight(ariphmeticSign("+"));
        ops.start();
        assertEquals(ops.getMagazinTop(), b * c + a);
    }

    @Test
    public void testAssignment() throws Exception, IllegalOPSArgument {
        OPS ops = new OPS();
        ops.setOutPrintStream(System.out);
        ops.setInputStream(System.in);
        ops.addRight(OpsItem.number(5));
        ops.addRight(OpsItem.var("a"));

        ops.addRight(OpsItem.assignment());
        ops.addRight(OpsItem.var("a"));
        ops.addRight(OpsItem.var("b"));
        ops.addRight(OpsItem.assignment());
        ops.addRight(OpsItem.var("a"));
        ops.addRight(OpsItem.print());
        ops.start();
        assertTrue(ops.getScope().containsVariable("a"));
        assertTrue(ops.getScope().containsVariable("a"));
        assertEquals(ops.getScope().manage().getVariableValue("a"), Integer.valueOf(5));
        assertEquals(ops.getScope().manage().getVariableValue("b"), Integer.valueOf(5));
    }


    @Test
    public void testInputOutput() throws Exception, IllegalOPSArgument {
        OPS ops = new OPS();
        ops.setOutPrintStream(System.out);
        ops.setInputStream(new BufferedInputStream(new StringStream("123")));
        ops.addRight(OpsItem.var("abb"));
        ops.addRight(OpsItem.scan());
        ops.addRight(OpsItem.var("abb"));
        ops.addRight(OpsItem.print());
        ops.start();
        assertTrue(ops.getScope().containsVariable("abb"));
    }

}