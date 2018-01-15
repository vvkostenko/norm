package norm.lexer2;

import norm.lexer.Token;
import norm.lexer.TokenType;

import java.util.*;

/**
 * Created by akim on 15.01.18.
 */
public class LexicalMatrix {

    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String[] KEYWORDS = {
            "and", "array",
            "else", "eq",
            "false",
            "if", "int",
            "neq",
            "or", "out",
            "sc",
            "true",
            "while"

    };

    private List<Rule> grammatic = new ArrayList<>();
    private Statistic statistic = null;
    public static final char EMPTY_CHAR = '\u03bb';

    public LexicalMatrix() {

    }

    public void init() {
        NonTerminal start = new NonTerminal("Start");

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
        addFinal(compare1, EMPTY_CHAR, TokenType.COMPARE_SIGN);
        addFinal(compare2, EMPTY_CHAR, TokenType.COMPARE_SIGN);

        //Цифры
        NonTerminal digit = new NonTerminal("Digit");
        for (int i = 0; i <= 9; i++) {
            Character ch = Character.forDigit(i, 10);
            add(start, ch, digit);
            add(digit, ch, digit);
        }
        addFinal(digit, EMPTY_CHAR, TokenType.NUMBER);

        // Пробелы
        add(start, '\n', start, () -> statistic.newline());
        add(start, '\u000b', start, () -> statistic.newSpace()); //Vertical tab
        add(start, '\r', start, () -> statistic.newSpace());
        add(start, '\t', start, () -> statistic.newSpace());
        add(start, ' ', start, () -> statistic.newSpace());
        add(start, '\u0085', start, () -> statistic.newline()); //NEXT Line NEL,
        add(start, '\u2028', start, () -> statistic.newline()); //Line Separator LS
        add(start, '\u2029', start, () -> statistic.newline()); //Paragraph separator PS

        char[] alphabet = ALPHABET.toCharArray();
        Set<Character> keywordsFirstLetters = new HashSet<>();
        for (String keyword : KEYWORDS) {
            keywordsFirstLetters.add(keyword.charAt(0));
        }

        for (String keyword : KEYWORDS) {
            char[] keywordChars = keyword.toCharArray();
            if (keywordChars.length == 1) {
                addFinal(new NonTerminal(keyword), keywordChars[0], TokenType.TYPE);
            } else {

            }
        }

    }

    private void add(NonTerminal start, char terminalChar, NonTerminal right, Runnable operation) {
    }

    private void add(NonTerminal left, char terminalChar, NonTerminal right) {

    }

    private void addFinal(NonTerminal nonTerminal, char terminalChar, TokenType finalToken) {

    }


}
