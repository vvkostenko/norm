package norm;


import norm.NextGenSyntaxer.Rule;
import norm.NextGenSyntaxer.Syntax;
import norm.NextGenSyntaxer.SyntaxMap;
import norm.exception.IllegalOPSArgument;
import norm.lexer.LexemReader;
import norm.lexer.Token;
import norm.lexer.TokenType;
import norm.lexer.LexicalMatrix;
import norm.lexer.StringLexicalAnalyzer;
import norm.ops.OPS;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by akim on 18.01.18.
 */
public class Norm {

    public static void main(String[] args) {
        Norm norm = new Norm();
        do {
            System.out.println();
            System.out.println("Enter program by lines: ");
            Scanner sc = new Scanner(System.in);
            try {
                norm.runIt(sc.nextLine());
            }catch (Throwable e) {
                System.err.println(e.getMessage());
            }
        } while (true);
        //

    }

    private static final LexicalMatrix lexicalMatrix;
    private static final SyntaxMap syntaxMap;
    private static final HashMap<SyntaxMap.Element, List<Rule>> rules;

    static {
        lexicalMatrix = new LexicalMatrix();
        syntaxMap = new SyntaxMap();
        syntaxMap.init();
        rules = syntaxMap.getRules();

    }

    public void runIt(String programText) {
        LexemReader reader = new StringLexicalAnalyzer(lexicalMatrix, programText);
        Syntax s = new Syntax(rules);
        try {
            Token token = reader.readOne();
            LinkedList<Token> chain = new LinkedList<>();
            while (!token.getType().equals(TokenType.EOF)) {
                chain.addLast(token);
                token = reader.readOne();
            }
            System.out.println(chain);
            OPS ops = s.generateTree(chain);
            ops.start();
            System.out.println("Magazine top: " + ops.getMagazinTop());
        } catch (Exception | IllegalOPSArgument e) {
            e.printStackTrace();
            System.err.format("\n\n[ERROR] " + e.getMessage());
        }
    }
}
