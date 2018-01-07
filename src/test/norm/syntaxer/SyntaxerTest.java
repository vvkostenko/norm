package norm.syntaxer;

import norm.lexer.DummyLexer;
import norm.lexer.Lexem;
import norm.lexer.LexemReader;
import norm.lexer.LexemType;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class SyntaxerTest {

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testAdd() {
        LexemReader lexemReader = new DummyLexer(
                new Lexem().setType(LexemType.TYPE)
        );
        Syntaxer syntaxer = new Syntaxer(lexemReader);
        OPS run = syntaxer.run();
        assertTrue(run.isValid());
        assertEquals(run.getSize(), 3);
    }
}