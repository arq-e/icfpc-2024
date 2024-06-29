package data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import evaluation.Binary;
import evaluation.Ternary;
import evaluation.Unary;

public class Operation {
    private OpType type;
    private Value op;
    private List<Operation> values;

    public Operation(OpType type, Value op, List<Operation> values) {
        if (values == null) {
            this.values = null;
        } else {
            this.values = new ArrayList<>(values);
        }

        this.op = op;
        this.type = type;
    }


    public Value eval(List<Variable> variables) {

        Value res = null;
        switch (type) {
            case UNARY:
                res = Unary.perform(op.getString(), values.get(0).eval(variables), variables);
                break;
            case BINARY:
                if (!op.getString().equals("B$")) {
                    res = Binary.perform(op.getString(), values.get(0).eval(variables), values.get(1).eval(variables), variables);
                } else {
                    if (variables == null) {
                        variables = new ArrayList<>();
                    }
                    Value second = values.get(1).eval(variables);  
                    variables.add(0, new Variable(second));
                    res = values.get(0).eval(variables);     
                }
                break; 
            case TERNARY:
                res = Ternary.perform(op.getString(), values.get(0).eval(variables), values.get(1).eval(variables), values.get(2).eval(variables), variables);
                break;   
            case LAMBDA:
                res = values.get(0).eval(variables);
                break;
            default:
                res = Unary.getSelf(op, variables);
                break;
        }

        return res;
    }

    public Value getOp() {
        return op;
    }

    public OpType getType() {
        return type;
    }

    public String write() {
        StringBuilder sb = new StringBuilder();
        sb.append(op.toString());
        if (type != OpType.SELF) 
            sb.append("(");
        if (values != null) {
            for (int i = 0; i < values.size(); ++i) {
                sb.append(values.get(i).write());
                if (i < values.size() - 1) {
                    sb.append(",");
                }

            }
        }
        if (type != OpType.SELF) 
            sb.append(")");
        return sb.toString();
    }


}
