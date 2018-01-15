package norm.lexer;

import java.util.List;

public interface LexemReader {
    /**
     * Считывает одну лексему
     * @return считанную лексему  или null
     */
    Token readOne();

    /**
     * Считывает несколько лексем
     * @param count количество считанных лексем
     * @return коллекция лексем
     */
    List<Token> readMany(int count);
}
