package parsing;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

import data.Type;
import data.Value;
import evaluation.Binary;
import evaluation.Unary;

public class Parser {
    static final String dict = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!\"#$%&'()*+,-./:;<=>?@[\\]^_`|~ \n";
    

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

    public static String convertString(String s) {
        StringBuilder sb = new StringBuilder();
        for (char ch : s.toCharArray()) {
            char newC = (char)(dict.indexOf(ch) + 33);
            sb.append((char)(dict.indexOf(ch) + 33));
        }
        return "S"+ sb.toString();
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

    public static String convertInt(Long val) {
        StringBuilder sb = new StringBuilder();
        while (val > 0) {
            long bas = val % 94;
            sb.insert(0, (char) (bas + 33));
            val -= bas;
            val /= 94;
        }

        return "I" + sb.toString();
    }
}
