package norm.lexer;

import norm.lexer.Lexem;

import java.io.InputStream;
import java.util.List;

public interface LexemReader {
    Lexem readOne(InputStream inputStream);
    List<Lexem> readOneWithNext(InputStream inputStream, int count);
    List<Lexem> readMany(InputStream inputStream, int count);
}
