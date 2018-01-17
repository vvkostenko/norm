package norm.NextGenSyntaxer;

import norm.lexer.TokenType;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

public class GreibachTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testGh() {
        SyntaxMap.NonTerm S = new SyntaxMap.NonTerm("S");
        SyntaxMap.NonTerm U = new SyntaxMap.NonTerm("U");
        SyntaxMap.NonTerm T = new SyntaxMap.NonTerm("T");
        SyntaxMap.NonTerm V = new SyntaxMap.NonTerm("V");
        SyntaxMap.NonTerm F = new SyntaxMap.NonTerm("F");

        SyntaxMap.Term plus = new SyntaxMap.Term(TokenType.ARIPHMETIC_CONSTANT, "+");
        SyntaxMap.Term mul = new SyntaxMap.Term(TokenType.ARIPHMETIC_CONSTANT, "*");
        SyntaxMap.Term a = new SyntaxMap.Term(TokenType.VAR_IDENTIFIER, "a");
        SyntaxMap.Term rbo = new SyntaxMap.Term(TokenType.ROUND_BRAKED_OPEN, "(");
        SyntaxMap.Term rbc = new SyntaxMap.Term(TokenType.ROUND_BRAKED_CLOSE, ")");
        SyntaxMap.Term empty = new SyntaxMap.Term(TokenType.EOF, "empty");

        SyntaxMap sampleRules = new SyntaxMap();

        sampleRules.add(S, T, U);
        sampleRules.add(U, plus, T, U);
        sampleRules.add(U, empty);
        sampleRules.add(T, F, V);
        sampleRules.add(V, mul, F, V);
        sampleRules.add(V, empty);
        sampleRules.add(F, rbo, S, rbc);
        sampleRules.add(F, a);

        HashMap<SyntaxMap.Element, List<Rule>> result = Greibach.convert(sampleRules.getRules());

        SyntaxMap testMap = new SyntaxMap();
        testMap.add(S, rbo, S, rbc, V, U);
        testMap.add(S, a, V, U);
        testMap.add(U, plus, T, U);
        testMap.add(U, empty);
        testMap.add(T, rbo, S, rbc, V);
        testMap.add(T, a, V);
        testMap.add(V, mul, F, V);
        testMap.add(V, empty);
        testMap.add(F, rbo, S, rbc);
        testMap.add(F, a);

        assertEquals(result.get(S), testMap.getRules().get(S));
        assertEquals(result.get(U), testMap.getRules().get(U));
        assertEquals(result.get(T), testMap.getRules().get(T));
        assertEquals(result.get(V), testMap.getRules().get(V));
        assertEquals(result.get(F), testMap.getRules().get(F));
    }
}