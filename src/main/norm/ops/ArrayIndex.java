package norm.ops;

/**
 * Created by akim on 18.01.18.
 */
public class ArrayIndex {
    private final Integer index;
    private final String var;

    public ArrayIndex(String varId, Integer index) {
        this.var = varId;
        this.index = index;
    }

    public Integer getIndex() {
        return index;
    }

    public String getVar() {
        return var;
    }
}
