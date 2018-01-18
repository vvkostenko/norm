package norm.lexer;

/**
 * Created by akim on 15.01.18.
 */
public class NonTerminal {
    private final String value;

    public NonTerminal(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NonTerminal)) return false;

        NonTerminal that = (NonTerminal) o;

        return value != null ? value.equals(that.value) : that.value == null;
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }
}
