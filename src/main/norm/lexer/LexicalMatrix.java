package norm.lexer;

import norm.exception.DuplicateLexicalRule;
import norm.exception.LexicalStateNotFound;

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
            for (Map.Entry<Character, Element> e : lexicalMatrix.get(state).entrySet()) {
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
        NEWLINE, SPACE
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
//            throw new LexicalStateNotFound(
//                    String.format("Lexical state '%s' not found with %c", state, character)
//            );
            return null;
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
        add(start, '<', compare1);
        add(start, '>', compare1);
        add(compare1, '=', compare2);
        addFinal(compare1, EMPTY_CHAR, TokenType.COMPARE_SIGN);
        addFinal(compare2, EMPTY_CHAR, TokenType.COMPARE_SIGN);

        //Цифры
        NonTerminal digit = new NonTerminal("Digit");
        for (int i = 0; i <= 9; i++) {
            Character ch = Character.forDigit(i, 10);
            add(start, ch, digit);
            add(digit, ch, digit);
            add(varIdentifier, ch, varIdentifier);
        }
        addFinal(digit, EMPTY_CHAR, TokenType.NUMBER);

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

        //Построение деревьев перехода
        generateKeywordsRules2(keywordDictionary.getAllKeywords(), start, 0);
        addFinal(varIdentifier, EMPTY_CHAR, TokenType.VAR_IDENTIFIER);
        Set<NonTerminal> generated = lexicalMatrix.keySet();
        for (String kw : keywordDictionary.getAllKeywords()) {
            if (!generated.contains(new NonTerminal(getKeywordTagName(kw))))
                throw new LexicalStateNotFound("Матрица ключевых слов построена не полностью. Не найдено правило для +" + getKeywordTagName(kw));
        }
        for (Character ch : ALPHABET) {
            if (!Character.isDigit(ch))
                add(varIdentifier, ch, varIdentifier);
        }
    }

    private void fillSpaces(NonTerminal nonTerminal) {
        // Пробелы
        add(nonTerminal, '\n', nonTerminal, Semantic.NEWLINE);
        add(nonTerminal, '\u000b', nonTerminal, Semantic.SPACE); //Vertical tab
        add(nonTerminal, '\r', nonTerminal, Semantic.SPACE);
        add(nonTerminal, '\t', nonTerminal, Semantic.SPACE);
        add(nonTerminal, ' ', nonTerminal, Semantic.SPACE);
        add(nonTerminal, '\u0085', nonTerminal, Semantic.NEWLINE); //NEXT Line NEL,
        add(nonTerminal, '\u2028', nonTerminal, Semantic.NEWLINE); //Line Separator LS
        add(nonTerminal, '\u2029', nonTerminal, Semantic.NEWLINE); //Paragraph separator PS
    }

    private void generateKeywordsRules2(Collection<String> group, NonTerminal nonTerminal, int position) {
        HashMap<Character, List<String>> nextGroups = new HashMap<>();
        boolean hasFinal = false;
        for (String kw : group) {
            if (position < kw.length()) {
                char ch = kw.charAt(position);
                if (nextGroups.containsKey(ch)) {
                    nextGroups.get(ch).add(kw);
                } else {
                    List<String> ksw = new ArrayList<>();
                    ksw.add(kw);
                    nextGroups.put(ch, ksw);
                }
            } else if (kw.length() == position) {
                hasFinal = true;
                addFinal(nonTerminal, EMPTY_CHAR, keywordDictionary.getType(kw));
            }
        }
        if (!hasFinal && !nonTerminal.equals(start))
            addFinal(nonTerminal, EMPTY_CHAR, TokenType.VAR_IDENTIFIER);

        Set<Character> usedCharacters = nextGroups.keySet();
        for (char ch : ALPHABET) {
            if (!usedCharacters.contains(ch)) {
                if (nonTerminal.equals(start) && Character.isDigit(ch))
                    continue;
                add(nonTerminal, ch, varIdentifier);
            }
        }

        for (Character ch : usedCharacters) {
            List<String> nextCollection = nextGroups.get(ch);
            NonTerminal nt = new NonTerminal(getKeywordTagName(nextCollection.get(0).substring(0, position + 1)));
            Collection<String> n = new ArrayList<>();
            add(nonTerminal, ch, nt);
            n.addAll(nextCollection);
            generateKeywordsRules2(n, nt, position + 1);
        }
    }

    private void add(NonTerminal left, Character terminalChar, NonTerminal right) {
        add(left, terminalChar, right, null);
    }

    private String getKeywordTagName(String name) {
        return String.format("kw[%s]", name);
    }


    private void add(NonTerminal left, char terminalChar, NonTerminal right, Semantic newline) {
        Map<Character, Element> map;
        if (lexicalMatrix.containsKey(left)) {
            map = lexicalMatrix.get(left);
        } else {
            map = new HashMap<>();
            lexicalMatrix.put(left, map);
            fillSpaces(left);
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
            lexicalMatrix.put(left, map);
            fillSpaces(left);
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
