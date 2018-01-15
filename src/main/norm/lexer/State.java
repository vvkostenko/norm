package norm.lexer;

/**
 * Created by akim on 12.01.18.
 */
public class State {
    public static final State RESPONSED = new State(
            String.valueOf(LexicalMatrix.REVERSED_T)
    );

    private String value = "";

    public State(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public State setValue(String value) {
        this.value = value;
        return this;
    }
}
