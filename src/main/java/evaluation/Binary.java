package evaluation;

import java.util.List;
import java.util.Map;

import data.Type;
import data.Value;
import data.Variable;

public class Binary {
    
    public static Value perform(String op, Value x1, Value x2, List<Variable> variables) {
        Value res = null;

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
                    break;
                }
            }
        }            
        
        switch(op.charAt(1)) {
            case '+':
                res = new Value(Type.INTEGER, sum(x1.getInt(), x2.getInt()));
                break;
            case '-':
                res = new Value(Type.INTEGER, subtract(x1.getInt(), x2.getInt()));    
                break;
            case '*':
                res = new Value(Type.INTEGER, multiply(x1.getInt(), x2.getInt()));    
                break;
            case '/':
                res = new Value(Type.INTEGER, divide(x1.getInt(), x2.getInt()));    
                break;                              
            case '%':
                res = new Value(Type.INTEGER, modulo(x1.getInt(), x2.getInt()));   
                break;
            case '<':
                res = new Value(Type.BOOLEAN, isSmaller(x1.getInt(), x2.getInt()));   
                break;
            case '>':
                res = new Value(Type.BOOLEAN, isBigger(x1.getInt(), x2.getInt()));   
                break;   
            case '=':
                res = new Value(Type.BOOLEAN, equal(x1.getInt(), x2.getInt()));   
                break;                             
            case '|':
                res = new Value(Type.BOOLEAN, or(x1.getBoolean(), x2.getBoolean()));      
                break;          
            case '&':
                res = new Value(Type.BOOLEAN, and(x1.getBoolean(), x2.getBoolean()));      
                break;
            case '.':
                res = new Value(Type.STRING, concat(x1.getString(), x2.getString()));     
                break;
            case 'T':
                res = new Value(Type.STRING, takeFirst(x1.getInt(), x2.getString())); 
                break;             
            case 'D':
                res = new Value(Type.STRING, dropFirst(x1.getInt(), x2.getString())); 
                break;
            case '$':
                res = performBullShit(x1, x2);   
                break;           
            default:
                break;  
        }

        return res;
    }

    public static long sum(long x, long y) {
        return x + y;
    }

    public static long subtract(long x, long y) {
        return x - y;
    }

    public static long multiply(long x, long y) {
        return x * y;
    }

    public static long divide(long x, long y) {
        return x / y;
    }

    public static long modulo(long x, long y) {
        return x % y;
    }

    public static boolean isSmaller(long x, long y) {
        return x < y;
    }

    public static boolean isBigger(long x, long y) {
        return x > y;
    }

    public static boolean equal(long x, long y) {
        return x == y;
    }

    public static boolean or(boolean x, boolean y) {
        return x | y;
    }

    public static boolean and(boolean x, boolean y) {
        return x & y;
    }

    public static String concat(String s1, String s2) {
        return s1 + s2;
    }

    public static String takeFirst(long idx, String s) {
        return s.substring(0,(int)idx);
    }

    public static String dropFirst(long idx, String s) {
        return s.substring((int)idx);
    }

    public static Value performBullShit(Value x, Value y) {
        return x.getLambda().apply(y);
    } 
}
