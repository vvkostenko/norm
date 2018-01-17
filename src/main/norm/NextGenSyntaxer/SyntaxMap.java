package norm.NextGenSyntaxer;

import norm.lexer.TokenType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SyntaxMap {
    public interface Element {
        String value();

        boolean isTerm();
    }

    public static class NonTerm implements Element {
        String value;

        public NonTerm(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "NonTerm{"
                    + value +
                    '}';
        }

        @Override
        public String value() {
            return value;
        }

        @Override
        public boolean isTerm() {
            return false;
        }
    }

    public static class Term implements Element {
        TokenType type;
        String value;

        public Term(TokenType type, String value) {
            this.type = type;
            this.value = value;
        }

        @Override
        public String toString() {
            return "Term{" +
                    "" + type +
                    "," + value +
                    '}';
        }

        @Override
        public String value() {
            return value;
        }

        @Override
        public boolean isTerm() {
            return true;
        }
    }

    HashMap<Element, List<Rule>> rules = new HashMap<>();

    public HashMap<Element, List<Rule>> getRules() {
        return rules;
    }

    // Non-terminals
    NonTerm Start = new NonTerm("S");
    NonTerm Body = new NonTerm("B");
    NonTerm MegaBody = new NonTerm("MB");
    NonTerm Variative = new NonTerm("V");
    NonTerm Expression = new NonTerm("E");
    NonTerm Loop = new NonTerm("L");
    NonTerm Comparative = new NonTerm("C");
    NonTerm MegaCopmparative = new NonTerm("MC");
    NonTerm Assigment = new NonTerm("A");
    NonTerm Variable = new NonTerm("VAR");
    NonTerm Value = new NonTerm("VAL");
    NonTerm CIFRA = new NonTerm("CIFRA");
    NonTerm AriphOp = new NonTerm("AriphOp");
    NonTerm BoolExpression = new NonTerm("BE");
    NonTerm CMP = new NonTerm("CMP");
    NonTerm SIGA = new NonTerm("SIGA");
    NonTerm SIGB = new NonTerm("SIGB");
    NonTerm SIGC = new NonTerm("SIGC");
    NonTerm VarIdentifier = new NonTerm("VI");
    NonTerm BoolConst = new NonTerm("BC");
    NonTerm MegaBoolExpr = new NonTerm("MBE");
    NonTerm MegaAriphOp = new NonTerm("MAO");

    // Terminals
    Term whileKeyword = new Term(TokenType.KEYWORD, "while");
    Term arrayKeyword = new Term(TokenType.KEYWORD, "array");
    Term scKeyword = new Term(TokenType.KEYWORD, "sc");
    Term outKeyword = new Term(TokenType.KEYWORD, "out");
    Term roundBracOpen = new Term(TokenType.ROUND_BRAKED_OPEN, "(");
    Term roundBracClosed = new Term(TokenType.ROUND_BRAKED_CLOSE, ")");
    Term figuredBracOpen = new Term(TokenType.FIGURED_BRAKED_OPEN, "{");
    Term figuredBracClosed = new Term(TokenType.FIGURED_BRAKED_CLOSE, "}");
    Term ifKeyword = new Term(TokenType.KEYWORD, "if");
    Term elseKeyword = new Term(TokenType.KEYWORD, "else");
    Term empty = new Term(TokenType.LAMBDA, "LAMBDA");
    Term ravno = new Term(TokenType.ASSIGNMENT_CONST, "=");
    Term doubleDot = new Term(TokenType.TWO_DOTS, ":");
    Term commaDot = new Term(TokenType.COMMA_DOT, ";");
    Term plus = new Term(TokenType.ASSIGNMENT_CONST, "+");
    Term minus = new Term(TokenType.ASSIGNMENT_CONST, "-");
    Term mod = new Term(TokenType.ASSIGNMENT_CONST, "%");
    Term umnojito = new Term(TokenType.ASSIGNMENT_CONST, "*");
    Term razdelitto = new Term(TokenType.ASSIGNMENT_CONST, "/");
    Term or = new Term(TokenType.BOOL_SIGN, "or");
    Term and = new Term(TokenType.BOOL_SIGN, "and");
    Term eq = new Term(TokenType.BOOL_SIGN, "eq");
    Term neq = new Term(TokenType.BOOL_SIGN, "neq");
    Term more = new Term(TokenType.COMPARE_SIGN, ">");
    Term less = new Term(TokenType.COMPARE_SIGN, "<");
    Term moreRavno = new Term(TokenType.COMPARE_SIGN, ">=");
    Term lessRavno = new Term(TokenType.COMPARE_SIGN, "<=");
    Term trueKeyword = new Term(TokenType.BOOL_CONSTANT, "true");
    Term falseKeyword = new Term(TokenType.BOOL_CONSTANT, "false");
    Term VI = new Term(TokenType.VAR_IDENTIFIER, "");
    Term NUM = new Term(TokenType.NUMBER, "");
    Term intKeyword = new Term(TokenType.TYPE, "int");
    Term comma = new Term(TokenType.COMMA, ",");

    public void init() {
        add(Start, Body);

        add(Body, Variative, MegaBody);

        add(MegaBody, Variative, MegaBody);
        add(MegaBody, empty);

        add(Variative, Assigment);
        add(Variative, Loop);
        add(Variative, Comparative);
        add(Variative, Expression);

        add(Expression, scKeyword, VarIdentifier);
        add(Expression, outKeyword, VarIdentifier);
        add(Expression, outKeyword, NUM);
        add(Expression, arrayKeyword, roundBracOpen, intKeyword, comma, CIFRA, roundBracClosed, VI, commaDot);

        add(Loop, whileKeyword, BoolExpression, doubleDot, figuredBracOpen, Body, figuredBracClosed);

        add(Comparative, ifKeyword, BoolExpression, doubleDot, figuredBracOpen, Body, figuredBracClosed, MegaCopmparative);

        add(MegaCopmparative, empty);
        add(MegaCopmparative, elseKeyword, doubleDot, figuredBracOpen, Body, figuredBracClosed);

        add(Assigment, Variable, ravno, Value, commaDot);

        add(Variable, VI);
        add(Variable, VI, roundBracOpen, Variable, roundBracClosed);

        add(Value, CIFRA);
        add(Value, AriphOp);

        add(CIFRA, NUM);
        add(CIFRA, Variable);

        add(AriphOp, CIFRA, SIGA, CIFRA, MegaAriphOp);
        add(AriphOp, roundBracOpen, AriphOp, roundBracClosed, MegaAriphOp);

        add(MegaAriphOp, SIGA, AriphOp, MegaAriphOp);
        add(MegaAriphOp, empty);

        add(BoolExpression, BoolConst, MegaBoolExpr);
        add(BoolExpression, CMP, MegaBoolExpr);
        add(BoolExpression, roundBracOpen, BoolExpression, roundBracClosed, MegaBoolExpr);

        add(MegaBoolExpr, SIGB, BoolExpression, MegaBoolExpr);
        add(MegaBoolExpr, empty);

        add(CMP, Value, SIGC, Value);

        add(SIGA, plus);
        add(SIGA, minus);
        add(SIGA, razdelitto);
        add(SIGA, umnojito);
        add(SIGA, mod);

        add(SIGB, or);
        add(SIGB, and);
        add(SIGB, eq);
        add(SIGB, neq);

        add(SIGC, more);
        add(SIGC, less);
        add(SIGC, moreRavno);
        add(SIGC, lessRavno);

        add(BoolConst, trueKeyword);
        add(BoolConst, falseKeyword);
    }

    public void add(Element body, Element... list) {
        if (rules.containsKey(body)) {
            rules.get(body).add(
                    new Rule().addAll(Arrays.asList(list))
            );
        } else {
            List<Rule> rulesList = new ArrayList<>();
            rulesList.add(new Rule().addAll(Arrays.asList(list)));
            rules.put(body, rulesList);
        }
    }

}
