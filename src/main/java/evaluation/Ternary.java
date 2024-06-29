package evaluation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import data.Type;
import data.Value;
import data.Variable;

public class Ternary {
    public static Value perform(String op, Value condition, Value x1, Value x2, List<Variable> variables) { 

        if (condition.getType() == Type.VARIABLE && variables != null) {
            for (Variable var : variables) {
                if (!var.isTaken() || condition.getInt() == var.getBounded()) {
                    var.take(condition.getInt());
                    condition.update(var.getValue());
                    break;
                }
            }
        }    
        if (x1.getType() == Type.VARIABLE && variables != null) {
            for (Variable var : variables) {
                if (!var.isTaken() || x1.getInt() == var.getBounded()) {
                    var.take(x1.getInt());
                    x1.update(var.getValue());
                    break;
                }
            }
        }      
        if (x2.getType() == Type.VARIABLE && variables != null) {
            for (Variable var : variables) {
                if (!var.isTaken() || x2.getInt() == var.getBounded()) {
                    var.take(x2.getInt());
                    x2.update(var.getValue());
                }
            }
        }    
        if (condition.getBoolean()) {
            return x1;
        } else {
            return x2;
        }    
    } 
}
