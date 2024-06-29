package evaluation;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import data.Lambda;
import data.OpType;
import data.Operation;
import data.Type;
import data.Value;

public class Evaluator2 {
    static final String dict = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!\"#$%&'()*+,-./:;<=>?@[\\]^_`|~ \n";
    final StringBuilder sb = new StringBuilder();
    Deque<Operation> ops = new ArrayDeque<>();
    final Map<Long, Operation> lambdas = new HashMap<>();
    
    public String parse(String s) {
        Deque<String> dq = new ArrayDeque<>();
        for (String str : s.split(" ")) {
            dq.offerLast(str);
        }

        while (dq.size() > 0) {
            String str = dq.pollFirst();
            Operation op = parseOperation(dq, str);
            ops.offerLast(op);
        }

        sb.append(ops.peekFirst().write()).append("\n");
        while (ops.size() > 0) {
            sb.append(ops.pollFirst().eval(null).toString()).append("\n");
        }

        return sb.toString();
    }


    public List<Operation> parseUnary(Deque<String> dq, String str) {
        List<Operation> op = new ArrayList<>();
        String next = dq.pollFirst();
        op.add(parseOperation(dq, next));
        return op;
    }


    public List<Operation> parseBinary(Deque<String> dq, String str) {
        List<Operation> op = new ArrayList<>();
        String next = dq.pollFirst();
        op.add(parseOperation(dq, next));
        next = dq.pollFirst();
        op.add(parseOperation(dq, next));            
        return op;

    }

    public List<Operation> parseIf(Deque<String> dq) {
        List<Operation> op = new ArrayList<>();
        String next = dq.pollFirst();
        op.add(parseOperation(dq, next));
        next = dq.pollFirst();
        op.add(parseOperation(dq, next));      
        next = dq.pollFirst();
        op.add(parseOperation(dq, next));        
        return op;
    }

    public List<Operation> parseLambda(Deque<String> dq, String str) {
        List<Operation> op = new ArrayList<>();
        String next = dq.pollFirst();
        op.add(parseOperation(dq, next));
        return op;
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

    public Operation parseOperation(Deque<String> dq, String str) {
        Operation res = null;
        switch(str.charAt(0)) {
            case 'S':
                res = new Operation(OpType.SELF, new Value(Type.STRING, parseString(str)),  null);
                break;
            case 'T':
                res = new Operation(OpType.SELF, new Value(Type.BOOLEAN, true),  null);
                break;
            case 'F':
                res = new Operation(OpType.SELF, new Value(Type.BOOLEAN, false),  null);
                break;
            case 'I':
                res = new Operation(OpType.SELF, new Value(Type.INTEGER, parseInt(str)),  null);
                break;
            case 'U':
                res = new Operation(OpType.UNARY, new Value(Type.STRING, str), parseUnary(dq, str));            
                break;
            case 'B':
                res = new Operation(OpType.BINARY, new Value(Type.OPERATION, str), parseBinary(dq, str));        
                break;
            case '?':
                res = new Operation(OpType.TERNARY, new Value(Type.STRING, str), parseIf(dq));         
                break;
            case 'L':
                res = new Operation(OpType.LAMBDA, new Value(Type.LAMBDA, parseInt(str)), parseLambda(dq, str));              
                break;     
            case 'v':
                res = new Operation(OpType.SELF, new Value(Type.VARIABLE, parseInt(str)), null);             
                break;
            default:
                break;
        }

        return res;
    }    
}
