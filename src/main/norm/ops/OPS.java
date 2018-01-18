package norm.ops;

import javafx.scene.chart.NumberAxis;
import norm.exception.IllegalOPSArgument;
import norm.exception.NotInitializedException;
import norm.exception.VariableInitializedYet;
import norm.exception.VariableNotInitialized;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.*;

public class OPS {

    private Map<String, List<Integer>> arraysMap = new HashMap<>();
    private ManageOps manage = new ManageOps() {
        @Override
        public Integer getVariableValue(String name) {
            return OPS.this.getVariableValue(OpsItem.var(name)).intValue();
        }

        @Override
        public Integer getArrayValue(String name, Integer index) {
            return OPS.this.getVariableValue(OpsItem.arrayIndex(name, index)).intValue();
        }
    };
    private PrintStream outInputStream;
    private InputStream inputStream;

    public static void main(String[] args) throws Exception, IllegalOPSArgument {
        OPS ops = new OPS();
        int a = 5;
        int b = 10;
        ops.addRight(OpsItem.number(a));
        ops.addRight(OpsItem.number(b));
        ops.addRight(OpsItem.ariphmeticSign("+"));
        ops.move();
        ops.move();
        ops.move();
        ops.move();
        ops.getMagazinTop();
        Object top = ops.getMagazinTop();
        System.out.println("TOP: " + top);
    }

    private List<OpsItem> items = new ArrayList<>();
    private Stack<OpsItem> magazin = new Stack<>();
    private Map<String, Integer> labelsMap = new HashMap<>();
    private int position = -1;
    private boolean isValid = false;
    private HashMap<String, Integer> variablesMap = new HashMap<>();
    private Scope scope;


    /**
     * @return true, когда строка выполнена до конца
     * @throws Exception
     */
    public boolean move() throws Exception, IllegalOPSArgument {
        if (position < 0)
            throw new NotInitializedException("Польская строка пуста");
        else if (position < items.size()) {
            //Do nice work
            OpsItem current = items.get(position++);
            if (current.isOperand()) {
                System.out.println("[OPS] add operand: " + current);
                magazin.add(current);
            } else {
                Integer howOperandsNeed = current.getArgsCount();
                List<OpsItem> args = new ArrayList<>();
                for (int i = 0; i < howOperandsNeed; i++) {
                    if (magazin.empty())
                        throw new RuntimeException("В магазине не хватает операндов для выполнения операции: " + current);
                    OpsItem item = magazin.pop();
                    args.add(item);
                }

                execute(current, args);
            }
            return false;
        } else {
            return true;
        }
    }

    private void execute(OpsItem current, List<OpsItem> args) throws IllegalOPSArgument, Exception {
        System.out.format("Исполняем %s с %s\n", current, args);
        switch (current.getType()) {
            case JUMP_FALSE: {
                checkType(current, args.get(0).getType(), OpsItem.Type.BOOLEAN);
                checkType(current, args.get(1).getType(), OpsItem.Type.LABEL);
                Boolean arg = (Boolean) args.get(0).getValue();
                String label = (String) args.get(1).getValue();
                this.jumpTo(label, !arg);
                break;
            }
            case JUMP_TRUE: {
                checkType(current, args.get(0).getType(), OpsItem.Type.BOOLEAN);
                checkType(current, args.get(1).getType(), OpsItem.Type.LABEL);
                Boolean arg = (Boolean) args.get(0).getValue();
                String label = (String) args.get(1).getValue();
                this.jumpTo(label, arg);
                break;
            }
            case JUMP: {
                checkType(current, args.get(0).getType(), OpsItem.Type.LABEL);
                String label = (String) args.get(0).getValue();
                this.jumpTo(label, true);
                break;
            }

            case INIT_VAR: {
                checkType(current, args.get(0).getType(), OpsItem.Type.TYPE);
                checkType(current, args.get(1).getType(), OpsItem.Type.VAR_IDENTIFIER);

                String name = (String) args.get(1).getValue();
                String type = (String) args.get(0).getValue();
                this.initVariable(type, name);
                break;
            }

            case ARRAY_INDEX: {
                checkType(current, args.get(0).getType(), OpsItem.Type.VAR_IDENTIFIER);
                checkType(current, args.get(1).getType(), OpsItem.Type.NUMBER, OpsItem.Type.VAR_IDENTIFIER, OpsItem.Type.ARRAY_INDEX);
                this.arrayIndex(args.get(0).getValue().toString(), args.get(1));
                break;
            }

            case BOOL_COMPARING: {
                checkType(current, args.get(0).getType(), OpsItem.Type.BOOL_CMP_SIGN);
                checkType(current, args.get(1).getType(), OpsItem.Type.BOOLEAN);
                checkType(current, args.get(2).getType(), OpsItem.Type.BOOLEAN);

                this.boolCompare(args.get(0).getValue().toString(),
                        (Boolean) args.get(1).getValue(),
                        (Boolean) args.get(2).getValue());
                break;
            }

            case ARRAY_INIT: {
                checkType(current, args.get(0).getType(), OpsItem.Type.TYPE);
                checkType(current, args.get(1).getType(), OpsItem.Type.VAR_IDENTIFIER, OpsItem.Type.NUMBER, OpsItem.Type.ARRAY_INDEX);
                checkType(current, args.get(2).getType(), OpsItem.Type.VAR_IDENTIFIER);

                this.arrayInit(args.get(0).getValue().toString(),
                        args.get(1),
                        args.get(2).toString());
                break;
            }

            case NUMBER_COMPARING: {
                checkType(current, args.get(0).getType(), OpsItem.Type.ARIPHMETIC_SIGN);
                checkType(current, args.get(1).getType(), OpsItem.Type.NUMBER, OpsItem.Type.VAR_IDENTIFIER, OpsItem.Type.ARRAY_INDEX);
                checkType(current, args.get(2).getType(), OpsItem.Type.NUMBER, OpsItem.Type.VAR_IDENTIFIER, OpsItem.Type.ARRAY_INDEX);
                this.numberCompare(args.get(0).toString(), args.get(1), args.get(2));
                break;
            }

            case ARIPHMETIC_SIGN: {
                checkType(current, args.get(0).getType(), OpsItem.Type.NUMBER, OpsItem.Type.VAR_IDENTIFIER, OpsItem.Type.ARRAY_INDEX);
                checkType(current, args.get(1).getType(), OpsItem.Type.NUMBER, OpsItem.Type.VAR_IDENTIFIER, OpsItem.Type.ARRAY_INDEX);
                this.ariphmetic(current, args.get(0), args.get(1));
                break;
            }

            case PRINT: {
                checkType(current, args.get(0).getType(), OpsItem.Type.NUMBER, OpsItem.Type.VAR_IDENTIFIER, OpsItem.Type.ARRAY_INDEX);
                this.printIt(args.get(0));
                break;
            }

            case SCAN: {
                checkType(current, args.get(0).getType(), OpsItem.Type.VAR_IDENTIFIER, OpsItem.Type.ARRAY_INDEX);
                this.scan(args.get(0));
                break;
            }


            case ASSIGNMENT: {
                checkType(current, args.get(0).getType(), OpsItem.Type.VAR_IDENTIFIER, OpsItem.Type.ARRAY_INDEX);
                checkType(current, args.get(1).getType(), OpsItem.Type.NUMBER, OpsItem.Type.VAR_IDENTIFIER, OpsItem.Type.ARRAY_INDEX);
                this.assignment(args.get(0), args.get(1));
                break;
            }
        }

    }

    private void scan(OpsItem varId) {
        Scanner sc = new Scanner(inputStream);
        System.out.println("Start scanning: " + varId);
        while (!sc.hasNextInt()) {
            System.out.println("W");
        }
        Number number = null;
                number = sc.nextInt();
        System.out.println("Scanned " + varId);
        if (varId.getType().equals(OpsItem.Type.ARRAY_INDEX)) {
            this.putArrayIndexValue((ArrayIndex) varId.getValue(), number.intValue());
        } else {
            this.putVariableValue(varId.getValue().toString(), number.intValue());
        }
    }

    private void printIt(OpsItem item) {
        Number value = getNumber(item);
        if (outInputStream != null && value != null)
            outInputStream.println("Печать:" + value);
    }

    private void arrayInit(String type, OpsItem size, String variableName) {
        Number sizeValue = getNumber(size);
        Map<String, List<Integer>> arrays = getScope().arrays();
        if (arrays.containsKey(variableName) || getScope().variables().containsKey(variableName))
            throw new VariableInitializedYet(variableName);
        ArrayList<Integer> list = new ArrayList<Integer>(sizeValue.intValue());
        arrays.put(variableName, list);
    }

    private void ariphmetic(OpsItem current, OpsItem item, OpsItem item1) {

        Number left = getNumber(item);
        Number right = getNumber(item1);
        Number result = 0;
        System.out.println(left + current.getValue().toString() + right);
        switch (current.getValue().toString()) {
            case "+":
                result = left.intValue() + right.intValue();
                break;
            case "*":
                result = left.intValue() * right.intValue();
                break;
            case "/":
                result = left.intValue() / right.intValue();
                break;
            case "-":
                result = left.intValue() - right.intValue();
                break;

            case "%":
                result = left.intValue() % right.intValue();
                break;
        }

        if (item.getType().equals(OpsItem.Type.NUMBER)) {
            magazin.push(OpsItem.number(result.intValue()));
        } else {
            getScope().variables().put(item.getValue().toString(), result.intValue());
            magazin.push(item);
        }
    }

    private Number getVariableValue(OpsItem item1) {
        if (item1.getType().equals(OpsItem.Type.ARRAY_INDEX)) {
            ArrayIndex arrayIndex = (ArrayIndex) item1.getValue();
            Map<String, List<Integer>> arrays = getScope().arrays();
            if (arrays.containsKey(arrayIndex.getVar())) {
                int index = arrayIndex.getIndex();
                List<Integer> array = arrays.get(arrayIndex.getVar());
                if (array.size() > index) {
                    Integer result = array.get(index);
                    if (result == null) {
                        array.set(index, 0);
                        return 0;
                    } else {
                        return result;
                    }
                } else {
                    throw new ArrayIndexOutOfBoundsException("Вы вышли за границы массива " + arrayIndex.getVar() + "[" + index + "]");
                }
            } else {
                throw new VariableNotInitialized(arrayIndex.getVar(), "Массив");
            }
        } else {
            return getScope().variables().get(item1.getValue().toString());

        }
    }

    private void assignment(OpsItem varId, OpsItem right) {
        System.out.format("Присваивание %s=%s\n", varId, right.toString());
        Number number = getNumber(right);
        if (varId.getType().equals(OpsItem.Type.ARRAY_INDEX)) {
            this.putArrayIndexValue((ArrayIndex) varId.getValue(), number.intValue());
        } else {
            this.putVariableValue(varId.getValue().toString(), number.intValue());
        }

    }

    private void setArrayIndexValue(ArrayIndex arrayIndex, int intValue) {
        Map<String, List<Integer>> arrays = getScope().arrays();
        if (arrays.containsKey(arrayIndex.getVar())) {
            putArrayIndexValue(arrayIndex, intValue);
        } else {
            throw new VariableNotInitialized(arrayIndex.getVar(), "Массив");
        }
    }

    private void putArrayIndexValue(ArrayIndex arrayIndex, int intValue) {
        Map<String, List<Integer>> arrays = getScope().arrays();
        int index = arrayIndex.getIndex();
        List<Integer> array = arrays.get(arrayIndex.getVar());
        if (array.size() > index) {
            array.set(index, intValue);
        } else {
            throw new ArrayIndexOutOfBoundsException("Вы вышли за границы массива " + arrayIndex.getVar() + "[" + index + "]");
        }
    }

    private void setVariableValue(String varId, int value) {
        if (getScope().variables().containsKey(varId))
            putVariableValue(varId, value);
        else
            throw new VariableNotInitialized(varId);
    }

    private void putVariableValue(String varId, int value) {
        getScope().variables().put(varId, value);
    }

    private Number getNumber(OpsItem item) {
        return (item.getType().equals(OpsItem.Type.NUMBER)) ? (Number) item.getValue() : getVariableValue(item);
    }

    private void numberCompare(String sign, OpsItem item, OpsItem item1) {
        Number left = getNumber(item);
        Number right = getNumber(item1);
        System.out.format("%s %s %s\n", left, sign, right);
        switch (sign) {
            case ">":
                magazin.push(OpsItem.bool(left.intValue() > right.intValue()));
                break;
            case ">=":
                magazin.push(OpsItem.bool(left.intValue() >= right.intValue()));
                break;
            case "<":
                magazin.push(OpsItem.bool(left.intValue() < right.intValue()));
                break;
            case "<=":
                magazin.push(OpsItem.bool(left.intValue() <= right.intValue()));
                break;
        }
    }

    private void boolCompare(String sign, Boolean left, Boolean right) {
        System.out.format("%s %s %s\n", left, sign, right);
        switch (sign) {
            case "neq":
                magazin.push(OpsItem.bool(left != right));
                break;
            case "eq":
                magazin.push(OpsItem.bool(left != right));
                break;

            case "and":
                magazin.push(OpsItem.bool(left & right));
                break;

            case "or":
                magazin.push(OpsItem.bool(left || right));
                break;
        }
    }

    private void arrayIndex(String varId, OpsItem item) {
        System.out.format("Индекс %s=%s\n", varId, item.toString());
        Number index = getNumber(item);
        Map<String, List<Integer>> arrays = getScope().arrays();
        if (!arrays.containsKey(varId)) {
            throw new VariableNotInitialized(varId);
        }
        magazin.push(OpsItem.arrayIndex(varId, index.intValue()));
    }

    private OpsItem.Type checkType(OpsItem item, OpsItem.Type needed, OpsItem.Type... needleType) throws IllegalOPSArgument {
        OpsItem.Type firedType = null;
        for (OpsItem.Type type : needleType) {
            if (type.equals(needed)) {
                firedType = type;
                break;
            }
        }

        if (firedType == null) {
            throw new IllegalOPSArgument(item, needed, needleType);
        } else return firedType;
    }

    private void initVariable(String type, String name) throws Exception {
        System.out.format("Инициализация %s %s\n", type, name);
        if (scope.variables().containsKey(name))
            throw new Exception("Переменная уже объявлена");

        scope.variables().put(name, 0);
    }

    private void jumpTo(String label, Boolean doOrNot) {
        System.out.format("Прыжок к %s %s\n", label, doOrNot ? "произойдёт" : "не произойдёт");
        if (doOrNot) {
            Scope scope = getScope();
            if (!scope.labels().containsKey(label)) {
                throw new RuntimeException("Неизвестная метка: " + label);
            } else {
                Integer position = scope.labels().get(label);
                scope.setScopeForPosition(position);
                this.position = position;
            }
        }
    }

    public Scope getScope() {
        if (scope == null) {
            scope = new Scope() {

                @Override
                public Map<String, Integer> variables() {
                    return variablesMap;
                }

                @Override
                public Map<String, Integer> labels() {
                    return labelsMap;
                }

                @Override
                public void enterToNewScope() {

                }

                @Override
                public void exitFromCurrentScope() {

                }

                @Override
                public void setScopeForPosition(Integer position) {

                }

                @Override
                public Map<String, List<Integer>> arrays() {
                    return arraysMap;
                }

                @Override
                public ManageOps manage() {
                    return manage;
                }

                @Override
                public boolean containsVariable(String varName) {
                    return variables().containsKey(varName);
                }
            };
        }
        return scope;
    }


    public boolean isValid() {
        return isValid;
    }

    public int getSize() {
        return items.size();
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public OPS addRight(OpsItem item) {
        position = 0;
        items.add(item);
        return this;
    }

    public OPS addLeft(OpsItem item) {
        position = 0;
        items.add(0, item);
        return this;
    }

    public OPS put(int index, OpsItem item) {
        position = 0;
        items.add(index, item);
        return this;
    }

    public OPS remove(OpsItem item) {
        position = 0;
        items.remove(item);
        return this;
    }

    public Object getMagazinTop() {
        return magazin.peek().getValue();
    }

    public Object removeMagazinTop() {
        return magazin.peek().getValue();
    }

    public void start() throws Exception, IllegalOPSArgument {
        while (!this.move()) ;
    }

    public void setOutPrintStream(PrintStream out) {
        this.outInputStream = out;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }
}
