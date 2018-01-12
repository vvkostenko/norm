package norm.lexer;

/**
 * Created by akim on 12.01.18.
 */
public class Rule {
    public enum Programs {
        NEWLINE,
        SAVECHAR
    }
    private State nonTerminal;
    private Programs lexicalProgram;
    boolean isEndRule = false;

    public Rule setEndRule(boolean endRule) {
        isEndRule = endRule;
        return this;
    }

    public Rule(State nonTerminal) {
        this.nonTerminal = nonTerminal;
    }

    public Rule(State nonTerminal, Programs lexicalProgram) {
        this.nonTerminal = nonTerminal;
        this.lexicalProgram = lexicalProgram;
    }

    public Rule setLexicalProgram(Programs lexicalProgram) {
        this.lexicalProgram = lexicalProgram;
        return this;
    }

    public State getState() {
        return nonTerminal;
    }

    public Rule setNonTerminal(State nonTerminal) {
        this.nonTerminal = nonTerminal;
        return this;
    }
}
