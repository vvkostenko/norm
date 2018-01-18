package norm.lexer2;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by akim on 16.01.18.
 */
public class LexicalMatrixTest {
    @Test
    public void get() throws Exception {
        LexicalMatrix lexicalMatrix = new LexicalMatrix();
        LexicalMatrix.Element element =
                lexicalMatrix.get(lexicalMatrix.getStart(),
                        '+'
                        );
        assertNotNull(element);
        assertEquals(element.getCharacter(),'+');
        assertEquals(element.getFinalType(), TokenType.ARIPHMETIC_CONSTANT);
        assertNull(element.getNextState());
    }

    @Test
    public void testSize() throws Exception {
        LexicalMatrix lexicalMatrix = new LexicalMatrix();
        assertNotNull(lexicalMatrix.getLexicalMatrix());
        assertTrue(lexicalMatrix.getLexicalMatrix().size() > 0);
    }

    @Test
    public void getStart() throws Exception {
        LexicalMatrix lexicalMatrix = new LexicalMatrix();
        assertNotNull(lexicalMatrix.getStart());
    }

}