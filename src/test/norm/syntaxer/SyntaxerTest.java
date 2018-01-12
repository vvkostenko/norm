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
    public void testAriphmetic() {
        // a + b - 5
        LexemReader lexemReader = new DummyLexer(
                new Lexem(LexemType.VAR_IDENTIFIER, "a"),
                new Lexem(LexemType.ARIPHMETIC_CONSTANT, "+"),
                new Lexem(LexemType.VAR_IDENTIFIER, "b"),
                new Lexem(LexemType.ARIPHMETIC_CONSTANT, "-"),
                new Lexem(LexemType.NUMBER, "5")
        );
        Syntaxer syntaxer = new Syntaxer(lexemReader);
        OPS run = syntaxer.run();
        assertTrue(run.isValid());
        assertEquals(run.getSize(), 5);
    }

    @Test
    public void testRoundBracked() {
        // var1 * (1 + var2)
        LexemReader lexemReader = new DummyLexer(
                new Lexem(LexemType.VAR_IDENTIFIER, "var1"),
                new Lexem(LexemType.ARIPHMETIC_CONSTANT, "*"),
                new Lexem(LexemType.ROUND_BRAKED_OPEN, "("),
                new Lexem(LexemType.NUMBER, "1"),
                new Lexem(LexemType.ARIPHMETIC_CONSTANT, "+"),
                new Lexem(LexemType.VAR_IDENTIFIER, "var2"),
                new Lexem(LexemType.ROUND_BRAKED_CLOSE, ")")
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
                new Lexem(LexemType.VAR_IDENTIFIER, "a"),
                new Lexem(LexemType.ASSIGNMENT_CONST, "="),
                new Lexem(LexemType.VAR_IDENTIFIER, "b"),
                new Lexem(LexemType.COMMA_DOT, ";"),
                new Lexem(LexemType.VAR_IDENTIFIER, "c"),
                new Lexem(LexemType.ASSIGNMENT_CONST, "="),
                new Lexem(LexemType.VAR_IDENTIFIER, "b"),
                new Lexem(LexemType.ASSIGNMENT_CONST, "+"),
                new Lexem(LexemType.VAR_IDENTIFIER, "c"),
                new Lexem(LexemType.COMMA_DOT, ";")
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
                new Lexem(LexemType.KEYWORD, "array"),
                new Lexem(LexemType.ROUND_BRAKED_OPEN, "("),
                new Lexem(LexemType.TYPE, "int"),
                new Lexem(LexemType.COMMA, ","),
                new Lexem(LexemType.NUMBER, "5"),
                new Lexem(LexemType.ROUND_BRAKED_CLOSE, ")"),
                new Lexem(LexemType.VAR_IDENTIFIER, "arr"),
                new Lexem(LexemType.COMMA_DOT, ";"),
                new Lexem(LexemType.TYPE, "int"),
                new Lexem(LexemType.VAR_IDENTIFIER, "var"),
                new Lexem(LexemType.ASSIGNMENT_CONST, "="),
                new Lexem(LexemType.VAR_IDENTIFIER, "arr"),
                new Lexem(LexemType.ROUND_BRAKED_OPEN, "("),
                new Lexem(LexemType.NUMBER, "2"),
                new Lexem(LexemType.ROUND_BRAKED_CLOSE, ")"),
                new Lexem(LexemType.COMMA_DOT, ";")
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
                new Lexem(LexemType.VAR_IDENTIFIER, "a"),
                new Lexem(LexemType.COMPARE_SIGN, ">"),
                new Lexem(LexemType.VAR_IDENTIFIER, "b"),
                new Lexem(LexemType.COMMA_DOT, ";"),
                new Lexem(LexemType.VAR_IDENTIFIER, "c"),
                new Lexem(LexemType.COMPARE_SIGN, "<="),
                new Lexem(LexemType.NUMBER, "3"),
                new Lexem(LexemType.COMMA_DOT, ";")

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
                new Lexem(LexemType.VAR_IDENTIFIER, "a"),
                new Lexem(LexemType.BOOL_SIGN, "or"),
                new Lexem(LexemType.VAR_IDENTIFIER, "b"),
                new Lexem(LexemType.COMMA_DOT, ";"),
                new Lexem(LexemType.VAR_IDENTIFIER, "c"),
                new Lexem(LexemType.BOOL_SIGN, "eq"),
                new Lexem(LexemType.VAR_IDENTIFIER, "a"),
                new Lexem(LexemType.COMMA_DOT, ";")

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
                new Lexem(LexemType.KEYWORD, "if"),
                new Lexem(LexemType.ROUND_BRAKED_OPEN, "("),
                new Lexem(LexemType.VAR_IDENTIFIER, "a"),
                new Lexem(LexemType.COMPARE_SIGN, "<"),
                new Lexem(LexemType.NUMBER, "3"),
                new Lexem(LexemType.ROUND_BRAKED_CLOSE, ")"),
                new Lexem(LexemType.TWO_DOTS, ":"),
                new Lexem(LexemType.FIGURED_BRAKED_OPEN, "{"),
                new Lexem(LexemType.VAR_IDENTIFIER, "a"),
                new Lexem(LexemType.ASSIGNMENT_CONST, "="),
                new Lexem(LexemType.NUMBER, "3"),
                new Lexem(LexemType.COMMA_DOT, ";"),
                new Lexem(LexemType.VAR_IDENTIFIER, "b"),
                new Lexem(LexemType.ASSIGNMENT_CONST, "="),
                new Lexem(LexemType.VAR_IDENTIFIER, "a"),
                new Lexem(LexemType.COMMA_DOT, ";"),
                new Lexem(LexemType.FIGURED_BRAKED_CLOSE, "}"),
                new Lexem(LexemType.KEYWORD, "else"),
                new Lexem(LexemType.TWO_DOTS, ":"),
                new Lexem(LexemType.FIGURED_BRAKED_OPEN, "{"),
                new Lexem(LexemType.VAR_IDENTIFIER, "b"),
                new Lexem(LexemType.ASSIGNMENT_CONST, "="),
                new Lexem(LexemType.NUMBER, "1488"),
                new Lexem(LexemType.COMMA_DOT, ";"),
                new Lexem(LexemType.FIGURED_BRAKED_CLOSE, "}"),
                new Lexem(LexemType.VAR_IDENTIFIER, "a"),
                new Lexem(LexemType.ASSIGNMENT_CONST, "="),
                new Lexem(LexemType.NUMBER, "0"),
                new Lexem(LexemType.COMMA_DOT, ";")
        );
        Syntaxer syntaxer = new Syntaxer(lexemReader);
        OPS run = syntaxer.run();
        assertTrue(run.isValid());
        // TODO: Посчитать тут всё нормально
        //assertEquals(run.getSize(), 322);
    }
}