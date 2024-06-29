package d3;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class BinaryOperator extends Operator{
    private static final Set<String> binaryOperators = new HashSet<String>(Arrays.asList(new String[]{"+", "-", "*", "/", "%", "=", "#"}));     
    private String op;
    private int x;
    private int y;
    private Long value1;
    private Long value2;
    private int[] inPos1;
    private int[] inPos2;
    boolean isValid;

    public BinaryOperator(String op, int x, int y, String[][] state) {
        this.op = op;
        this.x = x;
        this.y = y;
        

        inPos1 = new int[]{x, y - 1};
        inPos2 = new int[]{x-1, y};
        
        if (isValid = inRange(inPos1, state) && inRange(inPos2, state)) {
            String s1 = state[inPos1[0]][inPos1[1]];
            String s2 = state[inPos2[0]][inPos2[1]];
            value1 = tryParse(s1);
            value2 = tryParse(s2);
            isValid = (value1 != null && value2 != null);
        }

    }

    public boolean perform(String[][] state, boolean[][] updated) {
        if (!isValid) return false;
        int[] pos1 = new int[]{x, y + 1};
        int[] pos2 = new int[]{x + 1, y};
        if (isValid && inRange(pos1, state) && inRange(pos2, state)) {
            String res1 = ".";
            String res2 = ".";
            switch (op) {
                case "+":
                    res1 = String.valueOf(value1 + value2);
                    res2 = res1;
                    break;
                case "-":
                    res1 = String.valueOf(value1 - value2);
                    res2 = res1;
                    break;
                case "*":
                    res1 = String.valueOf(value1 * value2);
                    res2 = res1;
                    break;
                case "/":
                    res1 = String.valueOf(value1 / value2);
                    res2 = res1;
                    break;   
                case "%":
                    res1 = String.valueOf(value1 % value2);
                    res2 = res1;
                    break;       
                case "=":
                    if (value1.longValue() != value2.longValue()) return false;
                    res1 = String.valueOf(value1);
                    res2 = res1;
                    break;   
                case "#":
                    if (value1.longValue() == value2.longValue()) return false;

                    res1 = String.valueOf(value2);
                    res2 = String.valueOf(value1);
                    break;                                  
                default:
                    break;
            }  
            if (updated[pos1[0]][pos1[1]]) {
                System.out.printf("Multiple modification attempts at x = %d, y = %d !\n", pos1[0], pos1[1]);
                return false;
            } else if (updated[pos2[0]][pos2[1]]){
                System.out.printf("Multiple modification attempts at x = %d, y = %d !\n", pos2[0], pos2[1]);
                return false;
            }
                
            state[inPos1[0]][inPos1[1]] = ".";
            state[inPos2[0]][inPos2[1]] = ".";

            state[pos1[0]][pos1[1]] = res1;
            state[pos2[0]][pos2[1]] = res2;
            updated[pos1[0]][pos1[1]] = true;
            updated[pos2[0]][pos2[1]] = true;
            
            return true;
        }


        return false;
    }

    public Long tryParse(String s) {
        try {
            return Long.parseLong(s);
        } catch(NumberFormatException e) {

        }

        return null;
    }

    public boolean isValid() {
        return isValid;
    }

}
