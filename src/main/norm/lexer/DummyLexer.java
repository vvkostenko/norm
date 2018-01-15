package norm.lexer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DummyLexer implements LexemReader{

    ArrayList<Token> tokens = new ArrayList<Token>();
    int position = 0;

    public DummyLexer(Token... tokens) {
        this.tokens.addAll(Arrays.asList(tokens));
    }

    public Token readOne() {
        if (position >= tokens.size()) {
            return new Token().setValue("").setType(TokenType.EOF);
        } else {
            return tokens.get(position++);
        }
    }

    public List<Token> readMany(int count) {
        throw new NotImplemented();
    }
}
