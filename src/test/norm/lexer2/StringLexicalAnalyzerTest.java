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

import static norm.lexer.TokenType.*;
/**
 * Created by akim on 16.01.18.
 */
public class StringLexicalAnalyzerTest {

    LexicalMatrix lexicalMatrix;

    public Token tt(TokenType type, String val) {
        return new Token(type,val);
    }

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

    @Test
    public void testArrayInitialization() throws Exception {
        String val = "array(int,51) tram;";
        LexemReader reader = new StringLexicalAnalyzer(lexicalMatrix,val);
        assertEquals(reader.readOne(), tt(KEYWORD,"array"));
        assertEquals(reader.readOne(),tt(ROUND_BRAKED_OPEN,"("));
        assertEquals(reader.readOne(),tt(TYPE,"int"));
        assertEquals(reader.readOne(), tt(COMMA,","));
        assertEquals(reader.readOne(),tt(NUMBER,"51"));
        assertEquals(reader.readOne(),tt(ROUND_BRAKED_CLOSE,")"));
        assertEquals(reader.readOne(),tt(VAR_IDENTIFIER,"tram"));
        assertEquals(reader.readOne(),tt(COMMA_DOT,";"));
    }

    @Test
    public void testMultipleKeyword() throws Exception {
        LexemReader reader = new StringLexicalAnalyzer(lexicalMatrix,"i;");
        assertEquals(reader.readOne(),tt(VAR_IDENTIFIER,"i"));
        assertEquals(reader.readOne(),tt(COMMA_DOT,";"));

        LexemReader readerIf = new StringLexicalAnalyzer(lexicalMatrix,"if;");
        assertEquals(readerIf.readOne(),tt(KEYWORD,"if"));
        assertEquals(readerIf.readOne(),tt(COMMA_DOT,";"));

        LexemReader readerIfa = new StringLexicalAnalyzer(lexicalMatrix,"ifa;");
        assertEquals(readerIfa.readOne(),tt(VAR_IDENTIFIER,"ifa"));
        assertEquals(readerIfa.readOne(),tt(COMMA_DOT,";"));
    }
}