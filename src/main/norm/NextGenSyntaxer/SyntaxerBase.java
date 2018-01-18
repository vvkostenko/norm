package norm.NextGenSyntaxer;

import norm.lexer.Token;
import norm.ops.OPS;

import java.util.List;

public interface SyntaxerBase {
    void readOne();
    OPS generateTree(List<Token> tokens);
}
