package norm.lexer;

import norm.exception.LexemNotFound;
import norm.exception.LexemNotResponsed;

import java.util.ArrayList;
import java.util.List;

public class StringLexicalAnalyzer implements LexemReader {

    private final LexicalMatrix lexicalMatrix;
    private final String src;
    private int position = 0;

    public StringLexicalAnalyzer(LexicalMatrix lexicalMatrix, String src) {
        this.lexicalMatrix = lexicalMatrix;
        this.src = src;
    }

    @Override
    public Token readOne() throws LexemNotResponsed, LexemNotFound {
        if (position >= src.length())
            return Token.EOF();
        else {
            StringBuilder sb = new StringBuilder();
            NonTerminal currentState = lexicalMatrix.getStart();
            System.out.println("Begin state: " + currentState);
            do {
                char currentChar = src.charAt(position);
                position++;
                System.out.println("Try to find " + currentState + "(" + currentChar + ")");
                LexicalMatrix.Element nextStep = lexicalMatrix.get(currentState, currentChar);
                LexicalMatrix.Element testEmpty = lexicalMatrix.get(currentState, LexicalMatrix.EMPTY_CHAR);
                System.out.println("Found: " + nextStep);
                if (nextStep == null) {
                    sb.append(currentChar);
                    if (testEmpty == null)
                        throw new LexemNotFound("Lexem not found: " + sb.toString());
                    else {
                        System.out.println("FOUND empty rule");
                        position--;
                        if (testEmpty.isFinal()) {
                            String result = sb.toString();
                            return new Token(testEmpty.getFinalType(), result.substring(0, Math.max(0, result.length() - 1)));
                        } else {
                            currentState = testEmpty.getNextState();
                        }
                    }
                } else {
                    if (nextStep.getSemantic() != null) {
                        if (!nextStep.getSemantic().equals(LexicalMatrix.Semantic.NEWLINE)
                                && !nextStep.getSemantic().equals(LexicalMatrix.Semantic.SPACE)) {
                            sb.append(currentChar);
                        } else {
                            if (testEmpty != null && testEmpty.isFinal()) {
                                return new Token(testEmpty.getFinalType(), sb.toString());
                            }
                        }
                    } else {
                        sb.append(currentChar);
                    }
                    if (nextStep.isFinal()) {
                        return new Token(nextStep.getFinalType(), sb.toString());
                    } else {
                        currentState = nextStep.getNextState();
                        System.out.println("Next state: " + currentState);
                    }
                }
            } while (position < src.length());

            System.out.println("Last chanse to find " + currentState + "(" + LexicalMatrix.EMPTY_CHAR + ")");
            LexicalMatrix.Element nextStep = lexicalMatrix.get(currentState, LexicalMatrix.EMPTY_CHAR);
            System.out.println("Last chanse: " + nextStep);
            if (nextStep != null && nextStep.isFinal()) {
                return new Token(nextStep.getFinalType(), sb.toString());
            } else
                return Token.EOF();
        }
    }

    @Override
    public List<Token> readMany(int count) throws LexemNotFound, LexemNotResponsed {
        List<Token> list = new ArrayList<>();
        for (int i = 0; i < count; i++)
            list.add(readOne());
        return list;
    }
}
