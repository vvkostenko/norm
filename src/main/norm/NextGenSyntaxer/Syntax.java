package norm.NextGenSyntaxer;

import norm.lexer.Token;
import norm.lexer.TokenType;

import java.util.*;

public class Syntax implements SyntaxerBase {

    HashMap<SyntaxMap.Element, List<Rule>> rules;
    Stack<SyntaxMap.Element> stack = new Stack<>();

    public Syntax(HashMap<SyntaxMap.Element, List<Rule>> rulesBase) {
        rules = Greibach.convert(rulesBase);
    }

    public void readOne() {

    }


    public Rule findRule(List<Rule> currentRules, Token nextSymbol) {
        Rule rule = new Rule();
        boolean fail = false;
        for (int i = 0; i < currentRules.size(); ++i) {
            TokenType curr = ((SyntaxMap.Term)currentRules.get(i).get(0)).getType();
            if (curr.equals(nextSymbol.getType())) {
                rule = currentRules.get(i);
                break;
            }

            if (i == currentRules.size() - 1)
                fail = true;
        }

        if (fail)
            return null;

        return rule;
    }

    public boolean generateTree(List<Token> tokens) {
        stack.push(new SyntaxMap.NonTerm("S"));

        while (!stack.empty()) {
            SyntaxMap.Element stackTop = stack.pop();
            Token nextSymbol = new Token();

            if(tokens.size() > 0)
                nextSymbol = tokens.get(0);

            if (!stackTop.isTerm()) {
                Rule currentRule = findRule(rules.get(stackTop), nextSymbol);

                if (currentRule == null)
                    currentRule = findRule(rules.get(stackTop), new Token(TokenType.LAMBDA, ""));

                if (currentRule == null)
                    throw new RuntimeException("Цепочка не распознана");

                for (int i = currentRule.size() - 1; i >= 0; --i)
                    stack.push(currentRule.get(i));
            } else if (stackTop.isTerm()) {
                TokenType type = ((SyntaxMap.Term) stackTop).getType();
                if(nextSymbol.getType() != null)
                    if (nextSymbol.getType().equals(type)) {
                        tokens.remove(0);
                } else {
                    throw new RuntimeException("Цепочка не распознана");
                }
            } else {
                throw new RuntimeException("Продам гараж");
            }
        }

        if (!stack.isEmpty() || !tokens.isEmpty())
            throw new RuntimeException("Цепочка не распознана");

        return true;
    }

    static public void main(String[] args) {
        SyntaxMap m = new SyntaxMap();
        m.init();
        HashMap<SyntaxMap.Element, List<Rule>> map = m.getRules();

        Syntax s = new Syntax(map);

        LinkedList<Token> chain = new LinkedList<>();
        chain.addAll(Arrays.asList(
                new Token(TokenType.VAR_IDENTIFIER, "a"),
                new Token(TokenType.ASSIGNMENT_CONST, "="),
                new Token(TokenType.VAR_IDENTIFIER, "b"),
                new Token(TokenType.ARIPHMETIC_CONSTANT, "+"),
                new Token(TokenType.VAR_IDENTIFIER, "c"),
                new Token(TokenType.COMMA_DOT, ";")
        ));

        boolean kek = s.generateTree(chain);
    }
}
