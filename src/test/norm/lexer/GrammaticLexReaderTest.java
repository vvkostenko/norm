package norm.lexer;

import org.junit.Test;
import utils.StringStream;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class GrammaticLexReaderTest {
    private static int EOF = -1;

    @Test
    public void testAddEOFWhenStreamEnded() {
        LexReader reader = new LexReader(new InputStream() {
            @Override
            public int read() throws IOException {
                return EOF;
            }
        });

        Token eofToken = reader.readOne();
        assertEquals(eofToken.type, TokenType.LAMBDA);
        assertEquals(eofToken.value, "");

        Token repearEOF = reader.readOne();
        assertEquals(repearEOF.type, TokenType.LAMBDA);
        assertEquals(repearEOF.value, "");
    }

    @Test
    public void boolSigns() {
        List<String> bools = Arrays.asList("<", ">", "<=", ">=", "neq", "and", "or", "eq");

        for (String lexemString : bools) {
            LexReader reader = new LexReader(new StringStream(lexemString));
            Token token = reader.readOne();
            assertEquals(token.type, TokenType.BOOL_SIGN);
            assertEquals(token.value, lexemString);
            //проверяем что всё полностью считал
            token = reader.readOne();
            assertEquals(token.type, TokenType.LAMBDA);
            assertEquals(token.value, "");
        }
    }

    @Test
    public void spaces() {
        List<String> bools = Arrays.asList(" ", "   ", "\n", "\t");

        for (String lexemString : bools) {
            LexReader reader = new LexReader(new StringStream(lexemString));
            Token token = reader.readOne();
            assertEquals(token.value, lexemString);
            //проверяем что всё полностью считал
            token = reader.readOne();
            assertEquals(token.type, TokenType.LAMBDA);
            assertEquals(token.value, "");
        }
    }

    @Test
    public void types() {
        List<String> bools = Arrays.asList("int");

        for (String lexemString : bools) {
            LexReader reader = new LexReader(new StringStream(lexemString));
            Token token = reader.readOne();
            assertEquals(token.type, TokenType.TYPE);
            assertEquals(token.value, lexemString);
            //проверяем что всё полностью считал
            token = reader.readOne();
            assertEquals(token.type, TokenType.LAMBDA);
            assertEquals(token.value, "");
        }
    }

    @Test
    public void boolConstants() {
        List<String> bools = Arrays.asList("true", "false");

        for (String lexemString : bools) {
            LexReader reader = new LexReader(new StringStream(lexemString));
            Token token = reader.readOne();
            assertEquals(token.type, TokenType.BOOL_CONSTANT);
            assertEquals(token.value, lexemString);
            //проверяем что всё полностью считал
            token = reader.readOne();
            assertEquals(token.type, TokenType.LAMBDA);
            assertEquals(token.value, "");
        }
    }

    @Test
    public void ariphmeticConstants() {
        List<String> bools = Arrays.asList("+", "-", "*", "/");

        for (String lexemString : bools) {
            LexReader reader = new LexReader(new StringStream(lexemString));
            Token token = reader.readOne();
            assertEquals(token.type, TokenType.ARIPHMETIC_CONSTANT);
            assertEquals(token.value, lexemString);
            //проверяем что всё полностью считал
            token = reader.readOne();
            assertEquals(token.type, TokenType.LAMBDA);
            assertEquals(token.value, "");
        }
    }

    @Test
    public void roundBrakeds() {
        LexReader reader = new LexReader(new StringStream("("));
        Token token = reader.readOne();
        assertEquals(token.type, TokenType.ROUND_BRAKED_OPEN);
        assertEquals(token.value, "(");

        //проверяем что всё полностью считал
        token = reader.readOne();
        assertEquals(token.type, TokenType.LAMBDA);
        assertEquals(token.value, "");

        reader = new LexReader(new StringStream(")"));
        token = reader.readOne();
        assertEquals(token.type, TokenType.ROUND_BRAKED_CLOSE);
        assertEquals(token.value, ")");

        //проверяем что всё полностью считал
        token = reader.readOne();
        assertEquals(token.type, TokenType.LAMBDA);
        assertEquals(token.value, "");

        reader = new LexReader(new StringStream("{"));
        token = reader.readOne();
        assertEquals(token.type, TokenType.FIGURED_BRAKED_OPEN);
        assertEquals(token.value, "{");

        //проверяем что всё полностью считал
        token = reader.readOne();
        assertEquals(token.type, TokenType.LAMBDA);
        assertEquals(token.value, "");

        reader = new LexReader(new StringStream("}"));
        token = reader.readOne();
        assertEquals(token.type, TokenType.FIGURED_BRAKED_CLOSE);
        assertEquals(token.value, "}");

        //проверяем что всё полностью считал
        token = reader.readOne();
        assertEquals(token.type, TokenType.LAMBDA);
        assertEquals(token.value, "");
    }

    @Test
    public void keywords() {
        List<String> bools = Arrays.asList("array", "if", "while", "else");

        for (String lexemString : bools) {
            LexReader reader = new LexReader(new StringStream(lexemString));
            Token token = reader.readOne();
            assertEquals(token.type, TokenType.KEYWORD);
            assertEquals(token.value, lexemString);
            //проверяем что всё полностью считал
            token = reader.readOne();
            assertEquals(token.type, TokenType.LAMBDA);
            assertEquals(token.value, "");
        }
    }

    @Test
    public void dots() {
        LexReader reader = new LexReader(new StringStream(":"));
        Token token = reader.readOne();
        assertEquals(token.type, TokenType.TWO_DOTS);
        assertEquals(token.value, ":");

        //проверяем что всё полностью считал
        token = reader.readOne();
        assertEquals(token.type, TokenType.LAMBDA);
        assertEquals(token.value, "");

        reader = new LexReader(new StringStream(";"));
        token = reader.readOne();
        assertEquals(token.type, TokenType.COMMA_DOT);
        assertEquals(token.value, ";");

        //проверяем что всё полностью считал
        token = reader.readOne();
        assertEquals(token.type, TokenType.LAMBDA);
        assertEquals(token.value, "");
    }

    @Test
    public void nonFloatNumbers() {
        LexReader reader = new LexReader(new StringStream("123321223"));
        Token token = reader.readOne();
        assertEquals(token.type, TokenType.NUMBER);
        assertEquals(token.value, "123321223");
    }

    @Test
    public void variableIdentifier() {
        LexReader reader = new LexReader(new StringStream("aaadasdaasdqzzxc"));
        Token token = reader.readOne();
        assertEquals(token.type, TokenType.VAR_IDENTIFIER);
        assertEquals(token.value, "aaadasdaasdqzzxc");

        reader = new LexReader(new StringStream("aaad123123asd"));
        Token letterDigit = reader.readOne();
        assertEquals(letterDigit.type, TokenType.VAR_IDENTIFIER);
        assertEquals(letterDigit.value, "aaad123123asd");;
    }
}