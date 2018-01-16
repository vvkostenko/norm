package norm.lexer2;

import norm.exception.LexemNotFound;
import norm.exception.LexemNotResponsed;
import norm.lexer.LexemReader;
import norm.lexer.Token;
import norm.lexer.TokenType;

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
                sb.append(currentChar);
                LexicalMatrix.Element nextStep = lexicalMatrix.get(currentState, currentChar);
                System.out.println("Found: " + nextStep);
                if (nextStep == null) {
                    LexicalMatrix.Element testEmpty = lexicalMatrix.get(currentState, LexicalMatrix.EMPTY_CHAR);
                    if (testEmpty == null)
                        throw new LexemNotFound("Lexem not found: " + sb.toString());
                    else {
                        System.out.println("FOUND empty rule");
                        position--;
                        if (testEmpty.isFinal()) {
                            return new Token(testEmpty.getFinalType(),sb.toString());
                        } else {
                            currentState = testEmpty.getNextState();
                        }
                    }
                } else {
                    if (nextStep.isFinal()) {
                        return new Token(nextStep.getFinalType(), sb.toString());
                    } else {
                        currentState = nextStep.getNextState();
                        System.out.println("Next state: " + currentState);
                    }
                }
            } while (position < src.length());

            throw new LexemNotResponsed("Lexem not responsed: " + sb.toString());
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
