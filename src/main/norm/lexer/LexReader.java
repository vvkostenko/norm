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

    public Lexem readOne() {
        return null;
    }

    public List<Lexem> readMany(int count) {
        if (count < 0)
            throw new IllegalArgumentException("Количество считываемых лексем должно быть больше нуля");
        ArrayList<Lexem> list = new ArrayList<Lexem>(count);
        for (int i = 0; i < count; i++) {
            Lexem lexem = readOne();
            if (lexem == null) break;
            list.add(lexem);
            if (lexem.type.equals(LexemType.EOF))
                break;
        }
        return list;
    }
}
