package norm.syntaxer;

import norm.lexer.Lexem;
import norm.lexer.LexemReader;
import norm.lexer.TokenType;

import java.util.*;

public class Syntaxer
{
    private LexemReader lexer;
    private Stack<Lexem> stack = new Stack<Lexem>();


    public Syntaxer(LexemReader lr)
    {
        lexer = lr;
    }

    public OPS run()
    {
        OPS ops = new OPS();
        Lexem lexem;
        lexem = lexer.readOne();
        while(lexem != null && !lexem.getType().equals(TokenType.EOF))
        {
            stack.push(lexem);
            lexem = lexer.readOne();
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
            public Lexem readOne() {
                return new Lexem().setType(TokenType.EOF).setValue("");
            }

            public List<Lexem> readMany(int count) {
                return null;
            }
        };
        Syntaxer sr = new Syntaxer(a);
    }
}
