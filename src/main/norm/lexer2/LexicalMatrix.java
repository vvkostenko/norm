package norm.lexer2;

import norm.exception.DuplicateLexicalRule;
import norm.exception.LexicalStateNotFound;
import norm.lexer.TokenType;

import java.util.*;

/**
 * Created by akim on 15.01.18.
 */
public class LexicalMatrix {

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(" === Lexical Matrix ===\n");
        for (NonTerminal state : lexicalMatrix.keySet()) {
            sb.append(state).append(" => {");
            for(Map.Entry<Character, Element>e : lexicalMatrix.get(state).entrySet()) {
                sb.append(e.getKey()).append("=").append(e.getValue()).append(", ");
            }
            sb.append("},\n");
        }

        sb.append(" ================ \n");
        return sb.toString();
    }

    public static void main(String[] args) {
        LexicalMatrix lm = new LexicalMatrix();
        System.out.println(lm);
    }

    public static class Element {
        private final TokenType finalType;
        private final NonTerminal nextState;
        private final char character;
        private final Semantic semantic;

        public Element(char terminalChar, NonTerminal right, Semantic semantic) {
            this.character = terminalChar;
            this.nextState = right;
            finalType = null;
            this.semantic = semantic;
        }

        public Element(TokenType finalToken, char terminalChar) {
            this.finalType = finalToken;
            this.character = terminalChar;
            this.nextState = null;
            semantic = null;
        }

        public NonTerminal getNextState() {
            return nextState;
        }

        public boolean isFinal() {
            return finalType != null;
        }

        public TokenType getFinalType() {
            return finalType;
        }

        public char getCharacter() {
            return character;
        }

        public Semantic getSemantic() {
            return semantic;
        }

        @Override
        public String toString() {
            return "{" +
                    "T=" + finalType +
                    ", N=" + nextState +
                    ", C=" + character +
                    ", S=" + semantic +
                    '}';
        }
    }

    public enum Semantic {
        NEWLINE, NEWSPACE
    }

    private static final char[] ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();

    private Map<NonTerminal, Map<Character, Element>> lexicalMatrix = new HashMap<>();
    private KeywordDictionary keywordDictionary = new KeywordDictionary();
    public static final char EMPTY_CHAR = '\u03bb';

    public LexicalMatrix() {
        init();
    }

    public Element get(NonTerminal state, char character) {
        if (!lexicalMatrix.containsKey(state)) {
            throw new LexicalStateNotFound(
                    String.format("Lexical state '%s' not found with %c", state, character)
            );
        } else {
            Map<Character, Element> map = lexicalMatrix.get(state);
            return map.getOrDefault(character, null);
        }
    }

    public Map<NonTerminal, Map<Character, Element>> getLexicalMatrix() {
        return lexicalMatrix;
    }

    public LexicalMatrix setLexicalMatrix(Map<NonTerminal, Map<Character, Element>> lexicalMatrix) {
        this.lexicalMatrix = lexicalMatrix;
        return this;
    }

    public NonTerminal getStart() {
        return start;
    }

    private static final NonTerminal start = new NonTerminal("Start");
    private static final NonTerminal varIdentifier = new NonTerminal("VI");

    private void init() {

        //Простые правила
        addFinal(start, '+', TokenType.ARIPHMETIC_CONSTANT);
        addFinal(start, '-', TokenType.ARIPHMETIC_CONSTANT);
        addFinal(start, '*', TokenType.ARIPHMETIC_CONSTANT);
        addFinal(start, '/', TokenType.ARIPHMETIC_CONSTANT);
        addFinal(start, '%', TokenType.ARIPHMETIC_CONSTANT);
        addFinal(start, '(', TokenType.ROUND_BRAKED_OPEN);
        addFinal(start, ')', TokenType.ROUND_BRAKED_CLOSE);
        addFinal(start, '{', TokenType.FIGURED_BRAKED_OPEN);
        addFinal(start, '}', TokenType.FIGURED_BRAKED_CLOSE);
        addFinal(start, ':', TokenType.TWO_DOTS);
        addFinal(start, ';', TokenType.COMMA_DOT);
        addFinal(start, ',', TokenType.COMMA);
        addFinal(start, '=', TokenType.ASSIGNMENT_CONST);

        //Сравнения
        NonTerminal compare1 = new NonTerminal("Cmp");
        NonTerminal compare2 = new NonTerminal("CmpEq");
        add(start, '<', compare1, Semantic.NEWLINE);
        add(start, '>', compare1, Semantic.NEWLINE);
        addFinal(compare1, EMPTY_CHAR, TokenType.COMPARE_SIGN);
        addFinal(compare2, EMPTY_CHAR, TokenType.COMPARE_SIGN);

        //Цифры
        NonTerminal digit = new NonTerminal("Digit");
        for (int i = 0; i <= 9; i++) {
            Character ch = Character.forDigit(i, 10);
            add(start, ch, digit, Semantic.NEWLINE);
            add(digit, ch, digit, Semantic.NEWLINE);
        }
        addFinal(digit, EMPTY_CHAR, TokenType.NUMBER);

        // Пробелы
        add(start, '\n', start, Semantic.NEWLINE);
        add(start, '\u000b', start, Semantic.NEWSPACE); //Vertical tab
        add(start, '\r', start, Semantic.NEWSPACE);
        add(start, '\t', start, Semantic.NEWSPACE);
        add(start, ' ', start, Semantic.NEWSPACE);
        add(start, '\u0085', start, Semantic.NEWLINE); //NEXT Line NEL,
        add(start, '\u2028', start, Semantic.NEWLINE); //Line Separator LS
        add(start, '\u2029', start, Semantic.NEWLINE); //Paragraph separator PS

        // Служебные слова
        keywordDictionary.add("if", TokenType.KEYWORD);
        keywordDictionary.add("int", TokenType.TYPE);
        keywordDictionary.add("and", TokenType.BOOL_CONSTANT);
        keywordDictionary.add("array", TokenType.KEYWORD);
        keywordDictionary.add("neq", TokenType.BOOL_SIGN);
        keywordDictionary.add("eq", TokenType.BOOL_SIGN);
        keywordDictionary.add("else", TokenType.KEYWORD);
        keywordDictionary.add("sc", TokenType.KEYWORD);
        keywordDictionary.add("or", TokenType.BOOL_SIGN);
        keywordDictionary.add("out", TokenType.KEYWORD);
        keywordDictionary.add("while", TokenType.KEYWORD);
        keywordDictionary.add("true", TokenType.BOOL_CONSTANT);
        keywordDictionary.add("false", TokenType.BOOL_CONSTANT);


        //Имена переменных
        Set<Character> firstKeywordChars = keywordDictionary.getFirstChars();
        //Построение деревьев перехода
        for (Character firstChar : firstKeywordChars) {
            String name = getKeywordTagName(String.valueOf(firstChar));
            NonTerminal generatedTerminal = new NonTerminal(name);
            // S -> cA
            add(start, firstChar, generatedTerminal, Semantic.NEWLINE);
            Set<String> keywords = keywordDictionary.getFor(firstChar);
            this.fillKeywords(keywords, generatedTerminal, 1, String.valueOf(firstChar));
        }

        for (char ch : ALPHABET) {
            if (!firstKeywordChars.contains(ch) && !Character.isDigit(ch)) {
                add(start, ch, varIdentifier, Semantic.NEWLINE);
            }
        }
    }

    private String getKeywordTagName(String name) {
        return String.format("kw[%s]", name);
    }

    private void fillKeywords(Set<String> keywords, NonTerminal parentTerminal, int seek, String name) {
        Collection<Character> charsForPostion = new ArrayList<>(keywords.size());
        Set<String> nextKeywords = new HashSet<>(keywords.size());
        boolean hasFinal = false;
        for (String k : keywords) {
            if (k.length() > seek + 1) {
                nextKeywords.add(k);
                charsForPostion.add(k.charAt(seek));
            } else {
                hasFinal = true;
                addFinal(parentTerminal, EMPTY_CHAR, keywordDictionary.getType(k));
            }
        }

        if (!hasFinal) {
            addFinal(parentTerminal, EMPTY_CHAR, TokenType.VAR_IDENTIFIER);
        }

        for (char alpha : ALPHABET) {
            if (!charsForPostion.contains(alpha))
                add(parentTerminal, alpha, varIdentifier, Semantic.NEWLINE);
        }

        for (Character ch : charsForPostion) {
            String nextName = name + ch;
            NonTerminal nonTerminal = new NonTerminal(getKeywordTagName(nextName));
            add(parentTerminal, ch, nonTerminal, Semantic.NEWLINE);
            fillKeywords(nextKeywords, nonTerminal, seek + 1, nextName);
        }
    }


    private void add(NonTerminal left, char terminalChar, NonTerminal right, Semantic newline) {
        Map<Character, Element> map;
        if (lexicalMatrix.containsKey(left)) {
            map = lexicalMatrix.get(left);
        } else {
            map = new HashMap<>();
            lexicalMatrix.put(left,map);
        }

        if (map.containsKey(terminalChar)) {
            throw new DuplicateLexicalRule(String.format("Duplicate lexical rule %s -> %s `%s` [%s]",
                    left.toString(),
                    String.valueOf(terminalChar),
                    right,
                    newline == null ? "" : newline.toString())
            );
        } else {
            map.put(terminalChar, new Element(terminalChar, right, newline));
        }
    }

    private void addFinal(NonTerminal left, char terminalChar, TokenType finalToken) {
        Map<Character, Element> map;
        if (lexicalMatrix.containsKey(left)) {
            map = lexicalMatrix.get(left);
        } else {
            map = new HashMap<>();
            lexicalMatrix.put(left,map);
        }

        if (map.containsKey(terminalChar)) {
            throw new DuplicateLexicalRule(
                    String.format("Duplicate final lexical rule %s -> %c \u22a5(%s)",
                            left.toString(),
                            terminalChar,
                            finalToken.toString())
            );
        } else {
            map.put(terminalChar, new Element(finalToken, terminalChar));
        }
    }


}
