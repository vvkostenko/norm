package norm.lexer;

public class Token {

    private static  final Token eof = new Token(TokenType.EOF,"");
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

    public static Token EOF() {
        return eof;
    }

    @Override
    public String toString() {
        return "Token{" +
                 type +
                ", " + value  +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Token)) return false;

        Token token = (Token) o;

        if (getType() != token.getType()) return false;
        return getValue() != null ? getValue().equals(token.getValue()) : token.getValue() == null;
    }

    @Override
    public int hashCode() {
        int result = getType().hashCode();
        result = 31 * result + (getValue() != null ? getValue().hashCode() : 0);
        return result;
    }
}
