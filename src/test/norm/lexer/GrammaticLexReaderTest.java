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

        Lexem eofLexem = reader.readOne();
        assertEquals(eofLexem.type, LexemType.EOF);
        assertEquals(eofLexem.value, "");

        Lexem repearEOF = reader.readOne();
        assertEquals(repearEOF.type, LexemType.EOF);
        assertEquals(repearEOF.value, "");
    }

    @Test
    public void boolSigns() {
        List<String> bools = Arrays.asList("<", ">", "<=", ">=", "neq", "and", "or", "eq");

        for (String lexemString : bools) {
            LexReader reader = new LexReader(new StringStream(lexemString));
            Lexem lexem = reader.readOne();
            assertEquals(lexem.type, LexemType.BOOL_SIGN);
            assertEquals(lexem.value, lexemString);
            //проверяем что всё полностью считал
            lexem = reader.readOne();
            assertEquals(lexem.type, LexemType.EOF);
            assertEquals(lexem.value, "");
        }
    }

    @Test
    public void spaces() {
        List<String> bools = Arrays.asList(" ", "   ", "\n", "\t");

        for (String lexemString : bools) {
            LexReader reader = new LexReader(new StringStream(lexemString));
            Lexem lexem = reader.readOne();
            assertEquals(lexem.type, LexemType.SPACE);
            assertEquals(lexem.value, lexemString);
            //проверяем что всё полностью считал
            lexem = reader.readOne();
            assertEquals(lexem.type, LexemType.EOF);
            assertEquals(lexem.value, "");
        }
    }

    @Test
    public void types() {
        List<String> bools = Arrays.asList("int");

        for (String lexemString : bools) {
            LexReader reader = new LexReader(new StringStream(lexemString));
            Lexem lexem = reader.readOne();
            assertEquals(lexem.type, LexemType.TYPE);
            assertEquals(lexem.value, lexemString);
            //проверяем что всё полностью считал
            lexem = reader.readOne();
            assertEquals(lexem.type, LexemType.EOF);
            assertEquals(lexem.value, "");
        }
    }

    @Test
    public void boolConstants() {
        List<String> bools = Arrays.asList("true", "false");

        for (String lexemString : bools) {
            LexReader reader = new LexReader(new StringStream(lexemString));
            Lexem lexem = reader.readOne();
            assertEquals(lexem.type, LexemType.BOOL_CONSTANT);
            assertEquals(lexem.value, lexemString);
            //проверяем что всё полностью считал
            lexem = reader.readOne();
            assertEquals(lexem.type, LexemType.EOF);
            assertEquals(lexem.value, "");
        }
    }

    @Test
    public void ariphmeticConstants() {
        List<String> bools = Arrays.asList("+", "-", "*", "/");

        for (String lexemString : bools) {
            LexReader reader = new LexReader(new StringStream(lexemString));
            Lexem lexem = reader.readOne();
            assertEquals(lexem.type, LexemType.ARIPHMETIC_CONSTANT);
            assertEquals(lexem.value, lexemString);
            //проверяем что всё полностью считал
            lexem = reader.readOne();
            assertEquals(lexem.type, LexemType.EOF);
            assertEquals(lexem.value, "");
        }
    }

    @Test
    public void roundBrakeds() {
        LexReader reader = new LexReader(new StringStream("("));
        Lexem lexem = reader.readOne();
        assertEquals(lexem.type, LexemType.ROUND_BRAKED_OPEN);
        assertEquals(lexem.value, "(");

        //проверяем что всё полностью считал
        lexem = reader.readOne();
        assertEquals(lexem.type, LexemType.EOF);
        assertEquals(lexem.value, "");

        reader = new LexReader(new StringStream(")"));
        lexem = reader.readOne();
        assertEquals(lexem.type, LexemType.ROUND_BRAKED_CLOSE);
        assertEquals(lexem.value, ")");

        //проверяем что всё полностью считал
        lexem = reader.readOne();
        assertEquals(lexem.type, LexemType.EOF);
        assertEquals(lexem.value, "");

        reader = new LexReader(new StringStream("{"));
        lexem = reader.readOne();
        assertEquals(lexem.type, LexemType.FIGURED_BRAKED_OPEN);
        assertEquals(lexem.value, "{");

        //проверяем что всё полностью считал
        lexem = reader.readOne();
        assertEquals(lexem.type, LexemType.EOF);
        assertEquals(lexem.value, "");

        reader = new LexReader(new StringStream("}"));
        lexem = reader.readOne();
        assertEquals(lexem.type, LexemType.FIGURED_BRAKED_CLOSE);
        assertEquals(lexem.value, "}");

        //проверяем что всё полностью считал
        lexem = reader.readOne();
        assertEquals(lexem.type, LexemType.EOF);
        assertEquals(lexem.value, "");
    }

    @Test
    public void keywords() {
        List<String> bools = Arrays.asList("array", "if", "while", "else");

        for (String lexemString : bools) {
            LexReader reader = new LexReader(new StringStream(lexemString));
            Lexem lexem = reader.readOne();
            assertEquals(lexem.type, LexemType.KEYWORD);
            assertEquals(lexem.value, lexemString);
            //проверяем что всё полностью считал
            lexem = reader.readOne();
            assertEquals(lexem.type, LexemType.EOF);
            assertEquals(lexem.value, "");
        }
    }

    @Test
    public void dots() {
        LexReader reader = new LexReader(new StringStream(":"));
        Lexem lexem = reader.readOne();
        assertEquals(lexem.type, LexemType.TWO_DOTS);
        assertEquals(lexem.value, ":");

        //проверяем что всё полностью считал
        lexem = reader.readOne();
        assertEquals(lexem.type, LexemType.EOF);
        assertEquals(lexem.value, "");

        reader = new LexReader(new StringStream(";"));
        lexem = reader.readOne();
        assertEquals(lexem.type, LexemType.COMMA_DOT);
        assertEquals(lexem.value, ";");

        //проверяем что всё полностью считал
        lexem = reader.readOne();
        assertEquals(lexem.type, LexemType.EOF);
        assertEquals(lexem.value, "");
    }

    @Test
    public void nonFloatNumbers() {
        LexReader reader = new LexReader(new StringStream("123321223"));
        Lexem lexem = reader.readOne();
        assertEquals(lexem.type, LexemType.NUMBER);
        assertEquals(lexem.value, "123321223");
    }

    @Test
    public void variableIdentifier() {
        LexReader reader = new LexReader(new StringStream("aaadasdaasdqzzxc"));
        Lexem lexem = reader.readOne();
        assertEquals(lexem.type, LexemType.VAR_IDENTIFIER);
        assertEquals(lexem.value, "aaadasdaasdqzzxc");

        reader = new LexReader(new StringStream("aaad123123asd"));
        Lexem letterDigit = reader.readOne();
        assertEquals(letterDigit.type, LexemType.VAR_IDENTIFIER);
        assertEquals(letterDigit.value, "aaad123123asd");;
    }
}