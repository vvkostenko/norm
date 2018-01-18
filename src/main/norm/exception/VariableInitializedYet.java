package norm.exception;

import norm.ops.OpsItem;

/**
 * Created by akim on 18.01.18.
 */
public class VariableInitializedYet extends RuntimeException {
    public VariableInitializedYet(String name) {
        super("Переменная " + name + " уже инициализирована");
    }
}
