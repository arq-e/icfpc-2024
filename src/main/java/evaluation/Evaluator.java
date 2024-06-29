package evaluation;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

import data.DataUnit;
import data.Lambda;
import data.Type;
import data.Value;
import parsing.Parser;

public class Evaluator {
    static final String dict = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!\"#$%&'()*+,-./:;<=>?@[\\]^_`|~ \n";
    final StringBuilder sb = new StringBuilder();
    
    public String eval(String s) {
        Deque<String> dq = new ArrayDeque<>();
        for (String str : s.split(" ")) {
            dq.offerLast(str);
        }

        while (dq.size() > 0) {
            String str = dq.pollFirst();
            Value val = decode(dq, str);
            val.write(sb);
        }
        return sb.toString();
    }


    public Value applyUnaryOperation(Deque<String> dq, String str) {
        String next = dq.pollFirst();
        return Unary.perform(str, decode(dq, next));
    }


    public Value applyBinaryOperation(Deque<String> dq, String str) {
        if (str.charAt(1) != '$' ) {
            Value first = decode(dq, dq.pollFirst());
            Value second = decode(dq, dq.pollFirst());
            return Binary.perform(str, first, second);
        } else {
            Lambda lambda = new Lambda();
            while (dq.peek().charAt(0) == 'L') {
                long val = parseInt(dq.pollFirst());
                lambda.getParams().put(val, null);
            }
            return evalLambda(dq, lambda);
        }

    }

    public Value evalIf(Deque<String> dq) {
        Value condition = decode(dq, dq.pollFirst());
        Value first = decode(dq, dq.pollFirst());
        Value second = decode(dq, dq.pollFirst());
        if (condition.getBoolean()) {
            return first;
        } else {
            return second;
        }
    }

    public Value evalLambda(Deque<String> dq, Lambda lambda) {
        return decode(dq, dq.pollFirst());
    }

    public Value assignLambda(Deque<String> dq) {
        //return decode(dq, dq.pollFirst());
        return null;
    }

    public static String parseString(String s) {
        StringBuilder sb = new StringBuilder();
        for (char ch : s.substring(1).toCharArray()) {
            int pos = (int) ch;
            if (pos < 33) {
                System.out.println(ch);
            }
            sb.append(dict.charAt(ch - 33));
        }

        return sb.toString();
    }

    public static long parseInt(String s) {
        long res = 0;
        long base = 94;
        for (char ch : s.substring(1).toCharArray()) {
            res *= base;
            res += (int)(ch - 33);
        }
        return res;
    }

    public Value decode(Deque<String> dq, String str) {
        Value res = null;
        switch(str.charAt(0)) {
            case 'S':
                res = new Value(Type.STRING,  parseString(str));
                break;
            case 'T':
                res = new Value(Type.BOOLEAN,  true);
                break;
            case 'F':
                res = new Value(Type.BOOLEAN,  false);
                break;
            case 'I':
                res = new Value(Type.INTEGER,  parseInt(str));
                break;
            case 'U':
                res = applyUnaryOperation(dq, str);
                break;
            case 'B':
                res = applyBinaryOperation(dq, str);
                break;
            case '?':
                res = evalIf(dq);
                break;
            case 'L':
                res = evalLambda(dq, null);
                break;     
            case 'v':
                res = assignLambda(dq);
                break;
            default:
                break;
        }

        return res;
    }


}
