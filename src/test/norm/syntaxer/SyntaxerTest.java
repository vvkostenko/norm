package norm.syntaxer;

import norm.lexer.DummyLexer;
import norm.lexer.Lexem;
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
    public void testAriphmetic() {
        // a + b - 5;
        LexemReader lexemReader = new DummyLexer(
                new Lexem(TokenType.VAR_IDENTIFIER, "a"),
                new Lexem(TokenType.ARIPHMETIC_CONSTANT, "+"),
                new Lexem(TokenType.VAR_IDENTIFIER, "b"),
                new Lexem(TokenType.ARIPHMETIC_CONSTANT, "-"),
                new Lexem(TokenType.NUMBER, "5"),
                new Lexem(TokenType.COMMA_DOT, ";")
        );
        Syntaxer syntaxer = new Syntaxer(lexemReader);
        OPS run = syntaxer.run();
        assertTrue(run.isValid());
        assertEquals(run.getSize(), 5);
    }

    @Test
    public void testRoundBracked() {
        // var1 * (1 + var2);
        LexemReader lexemReader = new DummyLexer(
                new Lexem(TokenType.VAR_IDENTIFIER, "var1"),
                new Lexem(TokenType.ARIPHMETIC_CONSTANT, "*"),
                new Lexem(TokenType.ROUND_BRAKED_OPEN, "("),
                new Lexem(TokenType.NUMBER, "1"),
                new Lexem(TokenType.ARIPHMETIC_CONSTANT, "+"),
                new Lexem(TokenType.VAR_IDENTIFIER, "var2"),
                new Lexem(TokenType.ROUND_BRAKED_CLOSE, ")"),
                new Lexem(TokenType.COMMA_DOT, ";")
        );
        Syntaxer syntaxer = new Syntaxer(lexemReader);
        OPS run = syntaxer.run();
        assertTrue(run.isValid());
        assertEquals(run.getSize(), 5);
    }

    @Test
    public void testMultiplyExprs() {
        // a = b; c = b + c;
        LexemReader lexemReader = new DummyLexer(
                new Lexem(TokenType.VAR_IDENTIFIER, "a"),
                new Lexem(TokenType.ASSIGNMENT_CONST, "="),
                new Lexem(TokenType.VAR_IDENTIFIER, "b"),
                new Lexem(TokenType.COMMA_DOT, ";"),
                new Lexem(TokenType.VAR_IDENTIFIER, "c"),
                new Lexem(TokenType.ASSIGNMENT_CONST, "="),
                new Lexem(TokenType.VAR_IDENTIFIER, "b"),
                new Lexem(TokenType.ASSIGNMENT_CONST, "+"),
                new Lexem(TokenType.VAR_IDENTIFIER, "c"),
                new Lexem(TokenType.COMMA_DOT, ";")
        );
        Syntaxer syntaxer = new Syntaxer(lexemReader);
        OPS run = syntaxer.run();
        assertTrue(run.isValid());
        assertEquals(run.getSize(), 8);
    }

    @Test
    public void testArray() {
        // array(int, 5) arr; int var = arr(2);
        LexemReader lexemReader = new DummyLexer(
                new Lexem(TokenType.KEYWORD, "array"),
                new Lexem(TokenType.ROUND_BRAKED_OPEN, "("),
                new Lexem(TokenType.TYPE, "int"),
                new Lexem(TokenType.COMMA, ","),
                new Lexem(TokenType.NUMBER, "5"),
                new Lexem(TokenType.ROUND_BRAKED_CLOSE, ")"),
                new Lexem(TokenType.VAR_IDENTIFIER, "arr"),
                new Lexem(TokenType.COMMA_DOT, ";"),
                new Lexem(TokenType.TYPE, "int"),
                new Lexem(TokenType.VAR_IDENTIFIER, "var"),
                new Lexem(TokenType.ASSIGNMENT_CONST, "="),
                new Lexem(TokenType.VAR_IDENTIFIER, "arr"),
                new Lexem(TokenType.ROUND_BRAKED_OPEN, "("),
                new Lexem(TokenType.NUMBER, "2"),
                new Lexem(TokenType.ROUND_BRAKED_CLOSE, ")"),
                new Lexem(TokenType.COMMA_DOT, ";")
        );
        Syntaxer syntaxer = new Syntaxer(lexemReader);
        // <init> <ar_alloc> int 5 arr = <init> int a <index> arr 2
        OPS run = syntaxer.run();
        assertTrue(run.isValid());
        assertEquals(run.getSize(), 5 + 7);
    }

    @Test
    public void testCompare() {
        // a > b; c <= 3;
        LexemReader lexemReader = new DummyLexer(
                new Lexem(TokenType.VAR_IDENTIFIER, "a"),
                new Lexem(TokenType.COMPARE_SIGN, ">"),
                new Lexem(TokenType.VAR_IDENTIFIER, "b"),
                new Lexem(TokenType.COMMA_DOT, ";"),
                new Lexem(TokenType.VAR_IDENTIFIER, "c"),
                new Lexem(TokenType.COMPARE_SIGN, "<="),
                new Lexem(TokenType.NUMBER, "3"),
                new Lexem(TokenType.COMMA_DOT, ";")

        );
        Syntaxer syntaxer = new Syntaxer(lexemReader);
        OPS run = syntaxer.run();
        assertTrue(run.isValid());
        assertEquals(run.getSize(), 6);
    }

    @Test
    public void testBoolExpr()
    {
        // a or b; c eq a;
        LexemReader lexemReader = new DummyLexer(
                new Lexem(TokenType.VAR_IDENTIFIER, "a"),
                new Lexem(TokenType.BOOL_SIGN, "or"),
                new Lexem(TokenType.VAR_IDENTIFIER, "b"),
                new Lexem(TokenType.COMMA_DOT, ";"),
                new Lexem(TokenType.VAR_IDENTIFIER, "c"),
                new Lexem(TokenType.BOOL_SIGN, "eq"),
                new Lexem(TokenType.VAR_IDENTIFIER, "a"),
                new Lexem(TokenType.COMMA_DOT, ";")

        );
        Syntaxer syntaxer = new Syntaxer(lexemReader);
        OPS run = syntaxer.run();
        assertTrue(run.isValid());
        assertEquals(run.getSize(), 6);
    }

    @Test
    public void testBlock() {
        // if (a < 3): { a = 3; b = a; } else: { b = 1488; } a = 0;
        LexemReader lexemReader = new DummyLexer(
                new Lexem(TokenType.KEYWORD, "if"),
                new Lexem(TokenType.ROUND_BRAKED_OPEN, "("),
                new Lexem(TokenType.VAR_IDENTIFIER, "a"),
                new Lexem(TokenType.COMPARE_SIGN, "<"),
                new Lexem(TokenType.NUMBER, "3"),
                new Lexem(TokenType.ROUND_BRAKED_CLOSE, ")"),
                new Lexem(TokenType.TWO_DOTS, ":"),
                new Lexem(TokenType.FIGURED_BRAKED_OPEN, "{"),
                new Lexem(TokenType.VAR_IDENTIFIER, "a"),
                new Lexem(TokenType.ASSIGNMENT_CONST, "="),
                new Lexem(TokenType.NUMBER, "3"),
                new Lexem(TokenType.COMMA_DOT, ";"),
                new Lexem(TokenType.VAR_IDENTIFIER, "b"),
                new Lexem(TokenType.ASSIGNMENT_CONST, "="),
                new Lexem(TokenType.VAR_IDENTIFIER, "a"),
                new Lexem(TokenType.COMMA_DOT, ";"),
                new Lexem(TokenType.FIGURED_BRAKED_CLOSE, "}"),
                new Lexem(TokenType.KEYWORD, "else"),
                new Lexem(TokenType.TWO_DOTS, ":"),
                new Lexem(TokenType.FIGURED_BRAKED_OPEN, "{"),
                new Lexem(TokenType.VAR_IDENTIFIER, "b"),
                new Lexem(TokenType.ASSIGNMENT_CONST, "="),
                new Lexem(TokenType.NUMBER, "1488"),
                new Lexem(TokenType.COMMA_DOT, ";"),
                new Lexem(TokenType.FIGURED_BRAKED_CLOSE, "}"),
                new Lexem(TokenType.VAR_IDENTIFIER, "a"),
                new Lexem(TokenType.ASSIGNMENT_CONST, "="),
                new Lexem(TokenType.NUMBER, "0"),
                new Lexem(TokenType.COMMA_DOT, ";")
        );
        Syntaxer syntaxer = new Syntaxer(lexemReader);
        OPS run = syntaxer.run();
        assertTrue(run.isValid());
        // TODO: Посчитать тут всё нормально
        //assertEquals(run.getSize(), 322);
    }

    @Test
    public void testWhile()
    {
        // while a > 42 : {a = a + 1;}
        LexemReader lexemReader = new DummyLexer(
                new Lexem(TokenType.KEYWORD, "while"),
                new Lexem(TokenType.VAR_IDENTIFIER, "a"),
                new Lexem(TokenType.COMPARE_SIGN, ">"),
                new Lexem(TokenType.NUMBER, "42"),
                new Lexem(TokenType.TWO_DOTS, ":"),
                new Lexem(TokenType.FIGURED_BRAKED_OPEN, "{"),
                new Lexem(TokenType.VAR_IDENTIFIER, "a"),
                new Lexem(TokenType.ASSIGNMENT_CONST, "="),
                new Lexem(TokenType.VAR_IDENTIFIER, "a"),
                new Lexem(TokenType.ARIPHMETIC_CONSTANT, "+"),
                new Lexem(TokenType.NUMBER, "1"),
                new Lexem(TokenType.COMMA_DOT, ";"),
                new Lexem(TokenType.FIGURED_BRAKED_CLOSE, "}")
                );
        Syntaxer syntaxer = new Syntaxer(lexemReader);
        OPS run = syntaxer.run();
        assertTrue(run.isValid());
    }
}