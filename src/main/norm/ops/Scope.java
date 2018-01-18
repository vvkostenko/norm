package norm.ops;

import java.util.List;
import java.util.Map;

/**
 * Created by akim on 18.01.18.
 */
public interface Scope {
    Map<String, Integer> variables();

    Map<String, Integer> labels();

    void enterToNewScope();

    void exitFromCurrentScope();

    void setScopeForPosition(Integer position);

    Map<String, List<Integer>> arrays();

    ManageOps manage();

    boolean containsVariable(String varName);
}
