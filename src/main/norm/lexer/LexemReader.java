package norm.lexer;

import norm.lexer.Lexem;

import java.io.InputStream;
import java.util.List;

public interface LexemReader {
    /**
     * Считывает одну лексему
     * @return считанную лексему  или null
     */
    Lexem readOne();

    /**
     * Считывает несколько лексем
     * @param count количество считанных лексем
     * @return коллекция лексем
     */
    List<Lexem> readMany(int count);
}
