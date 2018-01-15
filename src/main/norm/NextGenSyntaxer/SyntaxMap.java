package norm.NextGenSyntaxer;

import norm.lexer.TokenType;

public class SyntaxMap {
    private static class NonTerm
    {
        String value;

        public NonTerm(String value) {
            this.value = value;
        }
    }

    private static class Term{
        TokenType type;
        String value;

        public Term(TokenType type, String value) {
            this.type = type;
            this.value = value;
        }
    }

    // Non-terminals
    NonTerm Start = new NonTerm("S");
    NonTerm Body = new NonTerm("B");
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
    Term empty = new Term(TokenType.EOF, "EOF");
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
}
