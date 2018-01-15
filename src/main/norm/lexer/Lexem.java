package norm.lexer;

public class Lexem {

    TokenType type;
    String value;

    public Lexem(){}

    public Lexem(TokenType _type, String _value)
    {
        type = _type;
        value = _value;
    }

    public TokenType getType() {
        return type;
    }

    public Lexem setType(TokenType type) {
        this.type = type;
        return this;
    }

    public String getValue() {
        return value;
    }

    public Lexem setValue(String value) {
        this.value = value;
        return this;
    }
}
