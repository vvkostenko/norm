package norm.lexer;

import norm.exception.LexemNotFound;
import norm.exception.LexemNotResponsed;

import java.util.List;

public interface LexemReader {
    /**
     * Считывает одну лексему
     * @return считанную лексему  или null
     */
    Token readOne() throws LexemNotResponsed, LexemNotFound;

    /**
     * Считывает несколько лексем
     * @param count количество считанных лексем
     * @return коллекция лексем
     */
    List<Token> readMany(int count) throws LexemNotFound, LexemNotResponsed;
}
