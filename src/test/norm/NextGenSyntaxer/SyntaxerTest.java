package norm.NextGenSyntaxer;

import norm.lexer.Token;
import norm.lexer.TokenType;
import norm.ops.OPS;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class SyntaxerTest {

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testOps()
    {
        SyntaxMap m = new SyntaxMap();
        m.init();
        HashMap<SyntaxMap.Element, List<Rule>> map = m.getRules();

        Syntax s = new Syntax(map);

        LinkedList<Token> chain = new LinkedList<>();
        chain.addAll(Arrays.asList(
                new Token(TokenType.KEYWORD, "if"),
                new Token(TokenType.BOOL_CONSTANT, "true"),
                new Token(TokenType.TWO_DOTS, ":"),
                new Token(TokenType.FIGURED_BRAKED_OPEN, "("),
                new Token(TokenType.VAR_IDENTIFIER, "a"),
                new Token(TokenType.ASSIGNMENT_CONST, "="),
                new Token(TokenType.VAR_IDENTIFIER, "b"),
                new Token(TokenType.COMMA_DOT, ";"),
                new Token(TokenType.FIGURED_BRAKED_CLOSE, "}"),
                new Token(TokenType.KEYWORD, "array"),
                new Token(TokenType.ROUND_BRAKED_OPEN, "("),
                new Token(TokenType.TYPE, "int"),
                new Token(TokenType.COMMA, ","),
                new Token(TokenType.NUMBER, "3"),
                new Token(TokenType.ROUND_BRAKED_CLOSE, ")"),
                new Token(TokenType.VAR_IDENTIFIER, "c"),
                new Token(TokenType.COMMA_DOT, ";")
                //new Token(TokenType.LAMBDA, "")
        ));

        OPS kek = s.generateTree(chain);
        assertNotNull(kek);
    }
}