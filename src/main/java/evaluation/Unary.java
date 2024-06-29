package evaluation;

import data.Type;
import data.Value;
import parsing.Parser;

public class Unary {
    
    public static Value perform(String op, Value val) {
        Value res = null;
        switch(op.charAt(1)) {
            case '-':
                res = new Value(Type.INTEGER, negation(val.getInt()));
                break;
            case '!':
                res = new Value(Type.BOOLEAN, not(val.getBoolean()));
                break;
            case '#':
                res = new Value(Type.INTEGER, strToInt(val.getString()));
                break;
            case '$':
                res = new Value(Type.STRING, intToStr(val.getInt()));
                break;
            default:
                break;       
        }

        return res;
    }

    public static long negation(long x) {
        return -x;
    }

    public static boolean not(boolean x) {
        return !x;
    }

    public static long strToInt(String s) {
        return Evaluator.parseInt(Parser.convertString(s));
    }

    public static String intToStr(long x) {
        return Evaluator.parseString(Parser.convertInt(x));
    }
}
