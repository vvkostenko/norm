package norm.syntaxer;

import norm.exception.LexemNotFound;
import norm.exception.LexemNotResponsed;
import norm.lexer.DummyLexer;
import norm.lexer.Token;
import norm.lexer.LexemReader;
import norm.lexer.TokenType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SyntaxerTest {

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testAriphmetic() throws LexemNotFound, LexemNotResponsed {
        // a + b - 5;
        LexemReader lexemReader = new DummyLexer(
                new Token(TokenType.VAR_IDENTIFIER, "a"),
                new Token(TokenType.ARIPHMETIC_CONSTANT, "+"),
                new Token(TokenType.VAR_IDENTIFIER, "b"),
                new Token(TokenType.ARIPHMETIC_CONSTANT, "-"),
                new Token(TokenType.NUMBER, "5"),
                new Token(TokenType.COMMA_DOT, ";")
        );
        Syntaxer syntaxer = new Syntaxer(lexemReader);
        OPS run = syntaxer.run();
        assertTrue(run.isValid());
        assertEquals(run.getSize(), 5);
    }

    @Test
    public void testRoundBracked() throws LexemNotFound, LexemNotResponsed {
        // var1 * (1 + var2);
        LexemReader lexemReader = new DummyLexer(
                new Token(TokenType.VAR_IDENTIFIER, "var1"),
                new Token(TokenType.ARIPHMETIC_CONSTANT, "*"),
                new Token(TokenType.ROUND_BRAKED_OPEN, "("),
                new Token(TokenType.NUMBER, "1"),
                new Token(TokenType.ARIPHMETIC_CONSTANT, "+"),
                new Token(TokenType.VAR_IDENTIFIER, "var2"),
                new Token(TokenType.ROUND_BRAKED_CLOSE, ")"),
                new Token(TokenType.COMMA_DOT, ";")
        );
        Syntaxer syntaxer = new Syntaxer(lexemReader);
        OPS run = syntaxer.run();
        assertTrue(run.isValid());
        assertEquals(run.getSize(), 5);
    }

    @Test
    public void testMultiplyExprs() throws LexemNotFound, LexemNotResponsed {
        // a = b; c = b + c;
        LexemReader lexemReader = new DummyLexer(
                new Token(TokenType.VAR_IDENTIFIER, "a"),
                new Token(TokenType.ASSIGNMENT_CONST, "="),
                new Token(TokenType.VAR_IDENTIFIER, "b"),
                new Token(TokenType.COMMA_DOT, ";"),
                new Token(TokenType.VAR_IDENTIFIER, "c"),
                new Token(TokenType.ASSIGNMENT_CONST, "="),
                new Token(TokenType.VAR_IDENTIFIER, "b"),
                new Token(TokenType.ASSIGNMENT_CONST, "+"),
                new Token(TokenType.VAR_IDENTIFIER, "c"),
                new Token(TokenType.COMMA_DOT, ";")
        );
        Syntaxer syntaxer = new Syntaxer(lexemReader);
        OPS run = syntaxer.run();
        assertTrue(run.isValid());
        assertEquals(run.getSize(), 8);
    }

    @Test
    public void testArray() throws LexemNotFound, LexemNotResponsed {
        // array(int, 5) arr; int var = arr(2);
        LexemReader lexemReader = new DummyLexer(
                new Token(TokenType.KEYWORD, "array"),
                new Token(TokenType.ROUND_BRAKED_OPEN, "("),
                new Token(TokenType.TYPE, "int"),
                new Token(TokenType.COMMA, ","),
                new Token(TokenType.NUMBER, "5"),
                new Token(TokenType.ROUND_BRAKED_CLOSE, ")"),
                new Token(TokenType.VAR_IDENTIFIER, "arr"),
                new Token(TokenType.COMMA_DOT, ";"),
                new Token(TokenType.TYPE, "int"),
                new Token(TokenType.VAR_IDENTIFIER, "var"),
                new Token(TokenType.ASSIGNMENT_CONST, "="),
                new Token(TokenType.VAR_IDENTIFIER, "arr"),
                new Token(TokenType.ROUND_BRAKED_OPEN, "("),
                new Token(TokenType.NUMBER, "2"),
                new Token(TokenType.ROUND_BRAKED_CLOSE, ")"),
                new Token(TokenType.COMMA_DOT, ";")
        );
        Syntaxer syntaxer = new Syntaxer(lexemReader);
        // <init> <ar_alloc> int 5 arr = <init> int a <index> arr 2
        OPS run = syntaxer.run();
        assertTrue(run.isValid());
        assertEquals(run.getSize(), 5 + 7);
    }

    @Test
    public void testCompare() throws LexemNotFound, LexemNotResponsed {
        // a > b; c <= 3;
        LexemReader lexemReader = new DummyLexer(
                new Token(TokenType.VAR_IDENTIFIER, "a"),
                new Token(TokenType.COMPARE_SIGN, ">"),
                new Token(TokenType.VAR_IDENTIFIER, "b"),
                new Token(TokenType.COMMA_DOT, ";"),
                new Token(TokenType.VAR_IDENTIFIER, "c"),
                new Token(TokenType.COMPARE_SIGN, "<="),
                new Token(TokenType.NUMBER, "3"),
                new Token(TokenType.COMMA_DOT, ";")

        );
        Syntaxer syntaxer = new Syntaxer(lexemReader);
        OPS run = syntaxer.run();
        assertTrue(run.isValid());
        assertEquals(run.getSize(), 6);
    }

    @Test
    public void testBoolExpr() throws LexemNotFound, LexemNotResponsed {
        // a or b; c eq a;
        LexemReader lexemReader = new DummyLexer(
                new Token(TokenType.VAR_IDENTIFIER, "a"),
                new Token(TokenType.BOOL_SIGN, "or"),
                new Token(TokenType.VAR_IDENTIFIER, "b"),
                new Token(TokenType.COMMA_DOT, ";"),
                new Token(TokenType.VAR_IDENTIFIER, "c"),
                new Token(TokenType.BOOL_SIGN, "eq"),
                new Token(TokenType.VAR_IDENTIFIER, "a"),
                new Token(TokenType.COMMA_DOT, ";")

        );
        Syntaxer syntaxer = new Syntaxer(lexemReader);
        OPS run = syntaxer.run();
        assertTrue(run.isValid());
        assertEquals(run.getSize(), 6);
    }

    @Test
    public void testBlock() throws LexemNotFound, LexemNotResponsed {
        // if (a < 3): { a = 3; b = a; } else: { b = 1488; } a = 0;
        LexemReader lexemReader = new DummyLexer(
                new Token(TokenType.KEYWORD, "if"),
                new Token(TokenType.ROUND_BRAKED_OPEN, "("),
                new Token(TokenType.VAR_IDENTIFIER, "a"),
                new Token(TokenType.COMPARE_SIGN, "<"),
                new Token(TokenType.NUMBER, "3"),
                new Token(TokenType.ROUND_BRAKED_CLOSE, ")"),
                new Token(TokenType.TWO_DOTS, ":"),
                new Token(TokenType.FIGURED_BRAKED_OPEN, "{"),
                new Token(TokenType.VAR_IDENTIFIER, "a"),
                new Token(TokenType.ASSIGNMENT_CONST, "="),
                new Token(TokenType.NUMBER, "3"),
                new Token(TokenType.COMMA_DOT, ";"),
                new Token(TokenType.VAR_IDENTIFIER, "b"),
                new Token(TokenType.ASSIGNMENT_CONST, "="),
                new Token(TokenType.VAR_IDENTIFIER, "a"),
                new Token(TokenType.COMMA_DOT, ";"),
                new Token(TokenType.FIGURED_BRAKED_CLOSE, "}"),
                new Token(TokenType.KEYWORD, "else"),
                new Token(TokenType.TWO_DOTS, ":"),
                new Token(TokenType.FIGURED_BRAKED_OPEN, "{"),
                new Token(TokenType.VAR_IDENTIFIER, "b"),
                new Token(TokenType.ASSIGNMENT_CONST, "="),
                new Token(TokenType.NUMBER, "1488"),
                new Token(TokenType.COMMA_DOT, ";"),
                new Token(TokenType.FIGURED_BRAKED_CLOSE, "}"),
                new Token(TokenType.VAR_IDENTIFIER, "a"),
                new Token(TokenType.ASSIGNMENT_CONST, "="),
                new Token(TokenType.NUMBER, "0"),
                new Token(TokenType.COMMA_DOT, ";")
        );
        Syntaxer syntaxer = new Syntaxer(lexemReader);
        OPS run = syntaxer.run();
        assertTrue(run.isValid());
        // TODO: Посчитать тут всё нормально
        //assertEquals(run.getSize(), 322);
    }

    @Test
    public void testWhile() throws LexemNotFound, LexemNotResponsed {
        // while a > 42 : {a = a + 1;}
        LexemReader lexemReader = new DummyLexer(
                new Token(TokenType.KEYWORD, "while"),
                new Token(TokenType.VAR_IDENTIFIER, "a"),
                new Token(TokenType.COMPARE_SIGN, ">"),
                new Token(TokenType.NUMBER, "42"),
                new Token(TokenType.TWO_DOTS, ":"),
                new Token(TokenType.FIGURED_BRAKED_OPEN, "{"),
                new Token(TokenType.VAR_IDENTIFIER, "a"),
                new Token(TokenType.ASSIGNMENT_CONST, "="),
                new Token(TokenType.VAR_IDENTIFIER, "a"),
                new Token(TokenType.ARIPHMETIC_CONSTANT, "+"),
                new Token(TokenType.NUMBER, "1"),
                new Token(TokenType.COMMA_DOT, ";"),
                new Token(TokenType.FIGURED_BRAKED_CLOSE, "}")
                );
        Syntaxer syntaxer = new Syntaxer(lexemReader);
        OPS run = syntaxer.run();
        assertTrue(run.isValid());
    }
}