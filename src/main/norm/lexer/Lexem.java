package norm.lexer;

public class Lexem {

    LexemType type;
    String value;

    public Lexem(){}

    public Lexem(LexemType _type, String _value)
    {
        type = _type;
        value = _value;
    }

    public LexemType getType() {
        return type;
    }

    public Lexem setType(LexemType type) {
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
