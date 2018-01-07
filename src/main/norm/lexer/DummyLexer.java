package norm.lexer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DummyLexer implements LexemReader{

    ArrayList<Lexem> lexems = new ArrayList<Lexem>();
    int position = 0;

    public DummyLexer(Lexem ... lexems) {
        this.lexems.addAll(Arrays.asList(lexems));
    }

    public Lexem readOne() {
        if (position >= lexems.size()) {
            return new Lexem().setValue("").setType(LexemType.EOF);
        } else {
            return lexems.get(position++);
        }
    }

    public List<Lexem> readMany(int count) {
        throw new NotImplemented();
    }
}
