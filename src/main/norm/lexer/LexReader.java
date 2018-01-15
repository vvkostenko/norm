package norm.lexer;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LexReader implements LexemReader {

    private Scanner sc;

    public LexReader(InputStream is) {
        sc = new Scanner(is);
    }

    public Token readOne() {
        return null;
    }

    public List<Token> readMany(int count) {
        if (count < 0)
            throw new IllegalArgumentException("Количество считываемых лексем должно быть больше нуля");
        ArrayList<Token> list = new ArrayList<Token>(count);
        for (int i = 0; i < count; i++) {
            Token token = readOne();
            if (token == null) break;
            list.add(token);
            if (token.type.equals(TokenType.EOF))
                break;
        }
        return list;
    }
}
