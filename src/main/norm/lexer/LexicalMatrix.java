package norm.lexer;

import java.util.*;

/**
 * Created by akim on 12.01.18.
 */
public class LexicalMatrix {

    public static final char REVERSED_T = '\u22A5';
    public static final char EMPTY_CHAR = '\u03bb';
    public static final char EOF = '\u0003';

    private HashMap<State, RuleList> map = new HashMap<State, RuleList>();

    private void init(Statistic statistic) {
        Rule responsed = new Rule(State.RESPONSED).setEndRule(true);
        State start = new State("Start");
        Rule toStart = new Rule(start);

        //SIMPLE RULES
        add(start, '(', responsed, LexemType.ROUND_BRAKED_OPEN);
        add(start, ')', responsed, LexemType.ROUND_BRAKED_CLOSE);
        add(start, '{', responsed, LexemType.FIGURED_BRAKED_OPEN);
        add(start, '}', responsed, LexemType.FIGURED_BRAKED_CLOSE);
        add(start, ':', responsed, LexemType.TWO_DOTS);
        add(start, ';', responsed, LexemType.COMMA_DOT);
        add(start, ',', responsed, LexemType.COMMA);
        add(start, '=', responsed, LexemType.ASSIGNMENT_CONST);

        //ARIPHMETIC
        add(start, '+', responsed, LexemType.ARIPHMETIC_CONSTANT);
        add(start, '-', responsed, LexemType.ARIPHMETIC_CONSTANT);
        add(start, '*', responsed, LexemType.ARIPHMETIC_CONSTANT);
        add(start, '/', responsed, LexemType.ARIPHMETIC_CONSTANT);

        //COMPARE
        State cmp = new State("CMP");
        Rule cmpNonTerminal = new Rule(cmp);
        add(start, '<', cmpNonTerminal);
        add(start, '>', cmpNonTerminal);
        add(cmp, '=', responsed, LexemType.COMPARE_SIGN);
        add(cmp, EMPTY_CHAR, responsed, LexemType.COMPARE_SIGN);

        //NEWLINES
        Rule newLineRule = new Rule(start).setLexicalProgram(Rule.Programs.NEWLINE);
        add(start, '\n', newLineRule);
        add(start, '\u000b', toStart); //Vertical tab
        add(start, '\r', toStart);
        add(start, '\t', toStart);
        add(start, ' ', toStart);
        add(start, '\u0085', newLineRule); //NEXT Line NEL,
        add(start, '\u2028', newLineRule); //Line Separator LS
        add(start, '\u2029', newLineRule); //Paragraph separator PS


        // NUMBERS
        //TODO Убрать неодозначность
        State number = new State("Number");
        Rule numberRule = new Rule(number).setLexicalProgram(Rule.Programs.SAVECHAR);
        for (int i = 0; i <= 9; i++) {
            char ch = Character.forDigit(i, 10);
            add(start, ch, numberRule);
            add(number, ch, numberRule);
        }
        add(number, EMPTY_CHAR, responsed, LexemType.NUMBER);

        // KEYWORDS
        Collection<Character> keywordFirstLetterCollection = Arrays.asList(
                'a', 'i', 'w', 'e', 'o', 'n', 's', 't', 'f'
        );

        Collection<Character> alphabet = new ArrayList<>();
        for (char alph = 'a'; alph <= 'z'; alph++) {
            alphabet.add(alph);
            alphabet.add(Character.toUpperCase(alph));
        }

        State varIdentifier = new State("VI");
        Rule varIdentifierRule = new Rule(varIdentifier, Rule.Programs.SAVECHAR);

        // Keyword 'Array
        Rule array1 = new Rule(new State("A"), Rule.Programs.SAVECHAR);
        Rule array2 = new Rule(new State("Ar"), Rule.Programs.SAVECHAR);
        Rule array3 = new Rule(new State("Arr"), Rule.Programs.SAVECHAR);
        Rule array4 = new Rule(new State("Arra"), Rule.Programs.SAVECHAR);
        Rule array5 = new Rule(new State("Array"), Rule.Programs.SAVECHAR);
        add(start, 'a', array1);
        add(array1.getState(), 'r', array2);
        add(array2.getState(), 'r', array3);
        add(array3.getState(), 'a', array4);
        add(array4.getState(), 'y', array5);
        add(array5.getState(), EMPTY_CHAR, responsed, LexemType.KEYWORD);
        for (char c : alphabet) {
            add(array5.getState(), c, varIdentifierRule);
        }


    }

    private void add(State start, char terminal, Rule nonTerminal, LexemType type) {

    }

    private void add(State state, char terminal, Rule nonTerminal) {

    }

}
