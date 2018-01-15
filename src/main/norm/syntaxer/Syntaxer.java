package norm.syntaxer;

import norm.lexer.Token;
import norm.lexer.LexemReader;
import norm.lexer.TokenType;

import java.util.*;

public class Syntaxer
{
    private LexemReader lexer;
    private Stack<Token> stack = new Stack<Token>();

    public Syntaxer(LexemReader lr)
    {
        lexer = lr;
    }

    public void HandleLexem(Token token)
    {
    }

    public OPS run()
    {
        OPS ops = new OPS();
        Token token;
        token = lexer.readOne();
        while(token != null && !token.getType().equals(TokenType.EOF))
        {
            stack.push(token);
            HandleLexem(lexer.readOne());
        }

        return ops;
    }

    public static void AddToOps()
    {
        // TODO: Надо бы запилить контейнер с лексемами и т.д. (свитчи, то сё)
    }

    public static void main(String[] args)
    {
        LexemReader a = new LexemReader() {
            public Token readOne() {
                return new Token().setType(TokenType.EOF).setValue("");
            }

            public List<Token> readMany(int count) {
                return null;
            }
        };
        Syntaxer sr = new Syntaxer(a);
    }
}
