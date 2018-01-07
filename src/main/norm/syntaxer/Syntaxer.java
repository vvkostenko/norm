package norm.syntaxer;

import norm.lexer.Lexem;
import norm.lexer.LexReader;
import norm.lexer.LexemReader;
import norm.lexer.LexemType;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class Syntaxer
{
    private LexemReader lexer;
    private Stack<Lexem> stack;

    public Syntaxer(LexemReader lr)
    {
        lexer = lr;
    }
    public OPS run()
    {
        OPS ops = new OPS();
        Lexem lexem;
        lexem = lexer.readOne();
        while(lexem != null && !lexem.getType().equals(LexemType.EOF))
        {
            stack.push(lexem);
            lexem = lexer.readOne();
        }

        return ops;
    }

    public static void main(String[] args)
    {
        LexemReader a = new LexemReader() {
            public Lexem readOne() {
                return new Lexem().setType(LexemType.EOF).setValue("");
            }

            public List<Lexem> readMany(int count) {
                return null;
            }
        };
        Syntaxer sr = new Syntaxer(a);
    }
}
