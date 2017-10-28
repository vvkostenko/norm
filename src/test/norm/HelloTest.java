package norm;

import org.junit.Test;

import static org.junit.Assert.*;

public class HelloTest {
    @Test
    public void saySomething() throws Exception {
        Hello hello = new Hello();
        assertEquals(hello.saySomething("man"), "Hello, man");
    }

    @Test
    public void sayCustomGreeting() throws Exception {
        Hello hello = new Hello("Aloha");
        assertEquals(hello.saySomething("man"), "Aloha, man");
    }

    @Test(expected = IllegalArgumentException.class)
    public void sayNullName() {
        Hello hello = new Hello();
        hello.saySomething(null);
    }

}