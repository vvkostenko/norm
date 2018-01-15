package norm.lexer2;

import norm.exception.LexemNotFound;
import norm.exception.LexemNotResponsed;
import norm.lexer.LexemReader;
import norm.lexer.Token;
import norm.lexer.TokenType;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by akim on 16.01.18.
 */
public class StringLexicalAnalyzerTest {

    LexicalMatrix lexicalMatrix;

    @Before
    public void setUp() throws Exception {
        lexicalMatrix = new LexicalMatrix();
    }

    @Test
    public void testEOF() throws LexemNotFound, LexemNotResponsed {
        LexemReader reader = new StringLexicalAnalyzer(lexicalMatrix,"");

        Token eofToken = reader.readOne();
        assertNotNull(eofToken);
        assertEquals(eofToken.getType(), TokenType.EOF);
    }

    @Test
    public void testAriphmetic() throws Exception {
        String ariph = "+-/*%";
        LexemReader reader = new StringLexicalAnalyzer(lexicalMatrix,ariph);

        List<Token> list = reader.readMany(ariph.length());
        assertNotNull(list);
        assertEquals(list.size(),ariph.length());
        System.out.println(list);
        for (int i = 0; i< ariph.length(); i++) {
            Character ch = ariph.charAt(i);
            Token t = list.get(i);
            assertEquals(t.getType(),TokenType.ARIPHMETIC_CONSTANT);
            assertEquals(t.getValue(),String.valueOf(ch));
        }
        assertEquals(reader.readOne().getType(), TokenType.EOF);
    }

    @Test
    public void testAdd() throws Exception {
        String val = "a+b;";
        LexemReader reader = new StringLexicalAnalyzer(lexicalMatrix,val);
        assertEquals(reader.readOne(), new Token(TokenType.VAR_IDENTIFIER,"a"));
        assertEquals(reader.readOne(), new Token(TokenType.ARIPHMETIC_CONSTANT,"+"));
        assertEquals(reader.readOne(), new Token(TokenType.VAR_IDENTIFIER,"b"));
        assertEquals(reader.readOne(), new Token(TokenType.COMMA_DOT,";"));
    }
}