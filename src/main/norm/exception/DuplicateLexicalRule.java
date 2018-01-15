package norm.exception;

/**
 * Created by akim on 16.01.18.
 */
public class DuplicateLexicalRule extends RuntimeException {
    public DuplicateLexicalRule(String message) {
        super(message);
    }
}
