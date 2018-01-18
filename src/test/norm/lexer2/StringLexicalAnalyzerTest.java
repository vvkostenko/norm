package norm.lexer2;

import norm.exception.LexemNotFound;
import norm.exception.LexemNotResponsed;
import norm.lexer.LexReader;
import norm.lexer.LexemReader;
import norm.lexer.Token;
import norm.lexer.TokenType;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

import static org.junit.Assert.*;

import static norm.lexer.TokenType.*;

/**
 * Created by akim on 16.01.18.
 */
public class StringLexicalAnalyzerTest {

    LexicalMatrix lexicalMatrix;

    public Token tt(TokenType type, String val) {
        return new Token(type, val);
    }

    @Before
    public void setUp() throws Exception {
        lexicalMatrix = new LexicalMatrix();
    }

    @Test
    public void testEOF() throws LexemNotFound, LexemNotResponsed {
        LexemReader reader = new StringLexicalAnalyzer(lexicalMatrix, "");

        Token eofToken = reader.readOne();
        assertNotNull(eofToken);
        assertEquals(eofToken.getType(), TokenType.EOF);
    }

    @Test
    public void testAriphmetic() throws Exception {
        String ariph = "+-/*%";
        LexemReader reader = new StringLexicalAnalyzer(lexicalMatrix, ariph);

        List<Token> list = reader.readMany(ariph.length());
        assertNotNull(list);
        assertEquals(list.size(), ariph.length());
        System.out.println(list);
        for (int i = 0; i < ariph.length(); i++) {
            Character ch = ariph.charAt(i);
            Token t = list.get(i);
            assertEquals(t.getType(), TokenType.ARIPHMETIC_CONSTANT);
            assertEquals(t.getValue(), String.valueOf(ch));
        }
        assertEquals(reader.readOne().getType(), TokenType.EOF);
    }

    @Test
    public void testAdd() throws Exception {
        String val = "a+b;";
        LexemReader reader = new StringLexicalAnalyzer(lexicalMatrix, val);
        assertEquals(reader.readOne(), new Token(TokenType.VAR_IDENTIFIER, "a"));
        assertEquals(reader.readOne(), new Token(TokenType.ARIPHMETIC_CONSTANT, "+"));
        assertEquals(reader.readOne(), new Token(TokenType.VAR_IDENTIFIER, "b"));
        assertEquals(reader.readOne(), new Token(TokenType.COMMA_DOT, ";"));
    }

    @Test
    public void testArrayInitialization() throws Exception {
        String val = "array(int,51) tram;";
        LexemReader reader = new StringLexicalAnalyzer(lexicalMatrix, val);
        assertEquals(reader.readOne(), tt(KEYWORD, "array"));
        assertEquals(reader.readOne(), tt(ROUND_BRAKED_OPEN, "("));
        assertEquals(reader.readOne(), tt(TYPE, "int"));
        assertEquals(reader.readOne(), tt(COMMA, ","));
        assertEquals(reader.readOne(), tt(NUMBER, "51"));
        assertEquals(reader.readOne(), tt(ROUND_BRAKED_CLOSE, ")"));
        assertEquals(reader.readOne(), tt(VAR_IDENTIFIER, "tram"));
        assertEquals(reader.readOne(), tt(COMMA_DOT, ";"));
    }

    @Test
    public void testValidVariableName() throws LexemNotFound, LexemNotResponsed {
        String val = "array123qq;";
        LexemReader reader = new StringLexicalAnalyzer(lexicalMatrix, val);
        assertEquals(reader.readOne(), tt(VAR_IDENTIFIER, "array123qq"));
        assertEquals(reader.readOne(), tt(COMMA_DOT, ";"));
    }

    @Test
    public void testMultipleKeyword() throws Exception {
        LexemReader reader = new StringLexicalAnalyzer(lexicalMatrix, "i;");
        assertEquals(reader.readOne(), tt(VAR_IDENTIFIER, "i"));
        assertEquals(reader.readOne(), tt(COMMA_DOT, ";"));

        LexemReader readerIf = new StringLexicalAnalyzer(lexicalMatrix, "if;");
        assertEquals(readerIf.readOne(), tt(KEYWORD, "if"));
        assertEquals(readerIf.readOne(), tt(COMMA_DOT, ";"));

        LexemReader readerIfa = new StringLexicalAnalyzer(lexicalMatrix, "ifa;");
        assertEquals(readerIfa.readOne(), tt(VAR_IDENTIFIER, "ifa"));
        assertEquals(readerIfa.readOne(), tt(COMMA_DOT, ";"));
    }

    @Test
    public void emptySpaces() throws Exception {
        LexemReader reader = new StringLexicalAnalyzer(lexicalMatrix, "    \t\n");
        assertEquals(reader.readOne(), tt(EOF, ""));
    }

    @Test
    public void testType() throws Exception {
        LexemReader reader = new StringLexicalAnalyzer(lexicalMatrix, "int inta in i");
        assertEquals(reader.readOne(), tt(TYPE, "int"));
        assertEquals(reader.readOne(), tt(VAR_IDENTIFIER, "inta"));
        assertEquals(reader.readOne(), tt(VAR_IDENTIFIER, "in"));
        assertEquals(reader.readOne(), tt(VAR_IDENTIFIER, "i"));
    }

    @Test
    public void testReadFromConsole() throws Exception {
        LexemReader reader = new StringLexicalAnalyzer(lexicalMatrix, "sc variable");
        assertEquals(reader.readOne(), tt(KEYWORD, "sc"));
        assertEquals(reader.readOne(), tt(VAR_IDENTIFIER, "variable"));
    }

    @Test
    public void testPrintToConsole() throws Exception {
        LexemReader reader = new StringLexicalAnalyzer(lexicalMatrix, "out q");
        assertEquals(reader.readOne(), tt(KEYWORD, "out"));
        assertEquals(reader.readOne(), tt(VAR_IDENTIFIER, "q"));
    }

    @Test
    public void testKeywords() throws Exception {
        List<String> kw = Arrays.asList("array", "if", "while", "sc", "out");
        LexemReader reader = new StringLexicalAnalyzer(lexicalMatrix,
                String.join(" ", kw));
        for (String keyword : kw) {
            assertEquals(reader.readOne(), tt(KEYWORD, keyword));
        }
    }

    @Test
    public void testAriphmetic2() throws Exception {
        LexemReader reader = new StringLexicalAnalyzer(lexicalMatrix, "(45 % MOD) + 42)");

        assertEquals(reader.readOne(), tt(ROUND_BRAKED_OPEN, "("));
        assertEquals(reader.readOne(), tt(NUMBER, "45"));
        assertEquals(reader.readOne(), tt(ARIPHMETIC_CONSTANT, "%"));
        assertEquals(reader.readOne(), tt(VAR_IDENTIFIER, "MOD"));
        assertEquals(reader.readOne(), tt(ROUND_BRAKED_CLOSE, ")"));
        assertEquals(reader.readOne(), tt(ARIPHMETIC_CONSTANT, "+"));
        assertEquals(reader.readOne(), tt(NUMBER, "42"));
        assertEquals(reader.readOne(), tt(ROUND_BRAKED_CLOSE, ")"));
    }

    @Test
    public void testCondition() throws Exception {
        LexemReader reader = new StringLexicalAnalyzer(lexicalMatrix, "" +
                "if 72 > radius : { radius = 72; }");
        assertEquals(reader.readOne(), tt(KEYWORD, "if"));
        assertEquals(reader.readOne(), tt(NUMBER, "72"));
        assertEquals(reader.readOne(), tt(COMPARE_SIGN, ">"));
        assertEquals(reader.readOne(), tt(VAR_IDENTIFIER, "radius"));
        assertEquals(reader.readOne(), tt(TWO_DOTS, ":"));
        assertEquals(reader.readOne(), tt(FIGURED_BRAKED_OPEN, "{"));
        assertEquals(reader.readOne(), tt(VAR_IDENTIFIER, "radius"));
        assertEquals(reader.readOne(), tt(ASSIGNMENT_CONST, "="));
        assertEquals(reader.readOne(), tt(NUMBER, "72"));
        assertEquals(reader.readOne(), tt(COMMA_DOT, ";"));
        assertEquals(reader.readOne(), tt(FIGURED_BRAKED_CLOSE, "}"));
    }

    @Test
    public void testFullCondition() throws Exception {

        LexemReader reader = new StringLexicalAnalyzer(lexicalMatrix, "" +
                "if 72 > radius : { radius = 72; }else: { radius = 24; }");
        assertEquals(reader.readOne(), tt(KEYWORD, "if"));
        assertEquals(reader.readOne(), tt(NUMBER, "72"));
        assertEquals(reader.readOne(), tt(COMPARE_SIGN, ">"));
        assertEquals(reader.readOne(), tt(VAR_IDENTIFIER, "radius"));
        assertEquals(reader.readOne(), tt(TWO_DOTS, ":"));
        assertEquals(reader.readOne(), tt(FIGURED_BRAKED_OPEN, "{"));
        assertEquals(reader.readOne(), tt(VAR_IDENTIFIER, "radius"));
        assertEquals(reader.readOne(), tt(ASSIGNMENT_CONST, "="));
        assertEquals(reader.readOne(), tt(NUMBER, "72"));
        assertEquals(reader.readOne(), tt(COMMA_DOT, ";"));
        assertEquals(reader.readOne(), tt(FIGURED_BRAKED_CLOSE, "}"));
        assertEquals(reader.readOne(), tt(KEYWORD, "else"));
        assertEquals(reader.readOne(), tt(TWO_DOTS, ":"));
        assertEquals(reader.readOne(), tt(FIGURED_BRAKED_OPEN, "{"));
        assertEquals(reader.readOne(), tt(VAR_IDENTIFIER, "radius"));
        assertEquals(reader.readOne(), tt(ASSIGNMENT_CONST, "="));
        assertEquals(reader.readOne(), tt(NUMBER, "24"));
        assertEquals(reader.readOne(), tt(COMMA_DOT, ";"));
        assertEquals(reader.readOne(), tt(FIGURED_BRAKED_CLOSE, "}"));
    }

    @Test
    public void testLoop() throws Exception {
        LexemReader reader = new StringLexicalAnalyzer(lexicalMatrix,
                "while a > 42: a = a + 1;}");

        assertEquals(reader.readOne(), tt(KEYWORD, "while"));
        assertEquals(reader.readOne(), tt(VAR_IDENTIFIER, "a"));
        assertEquals(reader.readOne(), tt(COMPARE_SIGN, ">"));
        assertEquals(reader.readOne(), tt(NUMBER, "42"));
        assertEquals(reader.readOne(), tt(TWO_DOTS, ":"));
        assertEquals(reader.readOne(), tt(VAR_IDENTIFIER, "a"));
        assertEquals(reader.readOne(), tt(ASSIGNMENT_CONST, "="));
        assertEquals(reader.readOne(), tt(VAR_IDENTIFIER, "a"));
        assertEquals(reader.readOne(), tt(ARIPHMETIC_CONSTANT, "+"));
        assertEquals(reader.readOne(), tt(NUMBER, "1"));
        assertEquals(reader.readOne(), tt(COMMA_DOT, ";"));
        assertEquals(reader.readOne(), tt(FIGURED_BRAKED_CLOSE, "}"));
    }

    @Test
    public void testArray() throws Exception {
        LexemReader reader = new StringLexicalAnalyzer(lexicalMatrix,
                "array(int,15)");
        assertEquals(reader.readOne(), tt(KEYWORD, "array"));
        assertEquals(reader.readOne(), tt(ROUND_BRAKED_OPEN, "("));
        assertEquals(reader.readOne(), tt(TYPE, "int"));
        assertEquals(reader.readOne(), tt(COMMA, ","));
        assertEquals(reader.readOne(), tt(NUMBER, "15"));
        assertEquals(reader.readOne(), tt(ROUND_BRAKED_CLOSE, ")"));
    }

    @Test
    public void testBoolean() throws Exception {
        LexemReader reader = new StringLexicalAnalyzer(lexicalMatrix,
                "(a<=35) and a <41 or true");

        assertEquals(reader.readOne(), tt(ROUND_BRAKED_OPEN, "("));
        assertEquals(reader.readOne(), tt(VAR_IDENTIFIER, "a"));
        assertEquals(reader.readOne(), tt(COMPARE_SIGN, "<="));
        assertEquals(reader.readOne(), tt(NUMBER, "35"));
        assertEquals(reader.readOne(), tt(ROUND_BRAKED_CLOSE, ")"));
        assertEquals(reader.readOne(), tt(BOOL_CONSTANT, "and"));
        assertEquals(reader.readOne(), tt(VAR_IDENTIFIER, "a"));
        assertEquals(reader.readOne(), tt(COMPARE_SIGN, "<"));
        assertEquals(reader.readOne(), tt(NUMBER, "41"));
        assertEquals(reader.readOne(), tt(BOOL_SIGN, "or"));
        assertEquals(reader.readOne(), tt(BOOL_CONSTANT, "true"));
    }

    @Test
    public void bigProblem() throws LexemNotFound, LexemNotResponsed {
        LexemReader reader = new StringLexicalAnalyzer(lexicalMatrix, "" +
                "if 72 > radius : { radius = 72; }else: { radius = 24; } \n a = 15; b = b + 16; \n ");
        assertEquals(reader.readOne(), tt(KEYWORD, "if"));
        assertEquals(reader.readOne(), tt(NUMBER, "72"));
        assertEquals(reader.readOne(), tt(COMPARE_SIGN, ">"));
        assertEquals(reader.readOne(), tt(VAR_IDENTIFIER, "radius"));
        assertEquals(reader.readOne(), tt(TWO_DOTS, ":"));
        assertEquals(reader.readOne(), tt(FIGURED_BRAKED_OPEN, "{"));
        assertEquals(reader.readOne(), tt(VAR_IDENTIFIER, "radius"));
        assertEquals(reader.readOne(), tt(ASSIGNMENT_CONST, "="));
        assertEquals(reader.readOne(), tt(NUMBER, "72"));
        assertEquals(reader.readOne(), tt(COMMA_DOT, ";"));
        assertEquals(reader.readOne(), tt(FIGURED_BRAKED_CLOSE, "}"));
        assertEquals(reader.readOne(), tt(KEYWORD, "else"));
        assertEquals(reader.readOne(), tt(TWO_DOTS, ":"));
        assertEquals(reader.readOne(), tt(FIGURED_BRAKED_OPEN, "{"));
        assertEquals(reader.readOne(), tt(VAR_IDENTIFIER, "radius"));
        assertEquals(reader.readOne(), tt(ASSIGNMENT_CONST, "="));
        assertEquals(reader.readOne(), tt(NUMBER, "24"));
        assertEquals(reader.readOne(), tt(COMMA_DOT, ";"));
        assertEquals(reader.readOne(), tt(FIGURED_BRAKED_CLOSE, "}"));
        assertEquals(reader.readOne(), tt(VAR_IDENTIFIER, "a"));
        assertEquals(reader.readOne(), tt(ASSIGNMENT_CONST,"="));
        assertEquals(reader.readOne(), tt(NUMBER, "15"));
        assertEquals(reader.readOne(), tt(COMMA_DOT, ";"));
        assertEquals(reader.readOne(), tt(VAR_IDENTIFIER, "b"));
        assertEquals(reader.readOne(), tt(ASSIGNMENT_CONST,"="));
        assertEquals(reader.readOne(), tt(VAR_IDENTIFIER, "b"));
        assertEquals(reader.readOne(), tt(ARIPHMETIC_CONSTANT, "+"));
        assertEquals(reader.readOne(), tt(NUMBER, "16"));
        assertEquals(reader.readOne(), tt(COMMA_DOT, ";"));

    }
}