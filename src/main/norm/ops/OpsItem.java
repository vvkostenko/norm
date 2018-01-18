package norm.ops;

/**
 * Created by akim on 18.01.18.
 */
public class OpsItem {

    private int argsCount = -1;

    @Override
    public String toString() {
        return "{" + type + "(" + argsCount + "), " + value + "}";
    }

    public static OpsItem var(String name) {
        OpsItem item = new OpsItem();
        item.setType(Type.VAR_IDENTIFIER);
        item.setValue(name);
        return item;
    }

    public static OpsItem number(int number) {
        OpsItem item = new OpsItem();
        item.setType(Type.NUMBER);
        item.setValue(Integer.valueOf(number));
        return item;
    }

    public static OpsItem bool(boolean value) {
        OpsItem item = new OpsItem();
        item.setType(Type.BOOLEAN);
        item.setValue(Boolean.valueOf(value));
        return item;
    }

    public static OpsItem label(String name) {
        OpsItem item = new OpsItem();
        item.setType(Type.LABEL);
        item.setValue(name);
        return item;
    }

    public static OpsItem operation(Type type) {
        OpsItem item = new OpsItem();
        item.setType(type);
        switch (type) {
            default:
                throw new IllegalArgumentException("Неизвестный типа операции: " + type);
            case BOOL_COMPARING:
            case ARIPHMETIC_SIGN:
            case ARRAY_INIT:
                item.argsCount = 3;
            case NUMBER_COMPARING:
            case ARRAY_INDEX:
            case INIT_VAR:
            case ASSIGNMENT:
            case JUMP_TRUE:
                item.argsCount = 2;
                break;
            case JUMP:
                item.argsCount = 1;
                break;


        }
        return item;
    }

    private OpsItem() {
    }

    public boolean isOperand() {
        return argsCount == -1;
    }

    public static OpsItem ariphmeticSign(String sign) {
        return new OpsItem().setType(Type.ARIPHMETIC_SIGN).setArgsCount(2).setValue(sign);
    }

    public static OpsItem arrayIndex(String varId, Integer index) {
        return new OpsItem().setType(Type.ARRAY_INDEX).setValue(
                new ArrayIndex(varId,index)
        );
    }

    public static OpsItem assignment() {
        return new OpsItem().setType(Type.ASSIGNMENT).setValue("=");
    }


    public enum Type {
        // Operands
        VAR_IDENTIFIER,
        NUMBER,
        BOOLEAN,
        LABEL,
        TYPE,


        BOOL_CMP_SIGN, // sign
        ARIPHMETIC_SIGN, // sign

        //Operations
        BOOL_COMPARING, // 2 arg
        NUMBER_COMPARING, // 2 arg
        ARRAY_INDEX, //2 arg
        INIT_VAR,  // 2 arg
        ASSIGNMENT, // 2
        JUMP, //1 arg (название метки)
        JUMP_TRUE, // 2 arg,
        JUMP_FALSE, // 2 arg
        ARRAY_INIT,
    }

    public Type getType() {
        return type;
    }

    public OpsItem setType(Type type) {
        this.type = type;
        return this;
    }

    public Object getValue() {
        return value;
    }

    public OpsItem setValue(Object value) {
        this.value = value;
        return this;
    }

    public int getArgsCount() {
        return argsCount;
    }

    public OpsItem setArgsCount(int argsCount) {
        this.argsCount = argsCount;
        return this;
    }

    private Type type;
    /**
     * В зависимости от типа создаются сюда разные значения.
     */
    private Object value;
}
