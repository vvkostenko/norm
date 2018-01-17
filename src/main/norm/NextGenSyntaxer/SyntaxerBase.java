package norm.NextGenSyntaxer;

import norm.lexer.Token;

import java.util.List;

public interface SyntaxerBase {
    void readOne();
    boolean generateTree(List<Token> tokens);
}
