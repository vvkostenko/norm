package norm.exception;

import norm.ops.OpsItem;

import java.util.Arrays;

/**
 * Created by akim on 18.01.18.
 */
public class IllegalOPSArgument extends Throwable {
    public IllegalOPSArgument(OpsItem operation, OpsItem.Type actual, OpsItem.Type[] seek) {
        super(String.format("Неверный аргумент для операции %s. Ожидание %s - получено  [%s]",
                operation.toString(),
                String.join(", ", Arrays.deepToString(seek)),
                actual
        ));
    }

    public IllegalOPSArgument(OpsItem operation, OpsItem.Type actual, OpsItem.Type seek) {
        super(String.format("Неверный аргумент для операции %s. Ожидание %s - получено  %s",
                operation.toString(),
                seek,
                actual
        ));
    }
}