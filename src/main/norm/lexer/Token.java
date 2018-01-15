package norm.lexer;

public class Token {

    TokenType type;
    String value;

    public Token(){}

    public Token(TokenType type, String value)
    {
        this.type = type;
        this.value = value;
    }

    public TokenType getType() {
        return type;
    }

    public Token setType(TokenType type) {
        this.type = type;
        return this;
    }

    public String getValue() {
        return value;
    }

    public Token setValue(String value) {
        this.value = value;
        return this;
    }
}
