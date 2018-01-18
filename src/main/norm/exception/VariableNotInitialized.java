package norm.exception;

/**
 * Created by akim on 18.01.18.
 */
public class VariableNotInitialized extends RuntimeException {
    public VariableNotInitialized(String varId) {
        super(makeStr(varId, "Переменная"));
    }

    public VariableNotInitialized(String varId, String firstWord) {
        super(makeStr(varId, firstWord));
    }

    private static String makeStr(String varId, String firstWord) {
        return firstWord + " " + varId + " не инстанцировано";
    }
}
