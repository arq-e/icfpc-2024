package d3;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class MoveOperator extends Operator {
    private static final Set<String> moveOperators = new HashSet<String>(Arrays.asList(new String[]{ "<", ">", "^", "v"}));    
    private String op;
    private int x;
    private int y;
    private String value;
    private int [] inputPos;
    boolean isValid;

    public MoveOperator(String op, int x, int y, String[][] state) {
        this.op = op;
        this.x = x;
        this.y = y;
        
        inputPos = new int[]{x, y};
        switch (op) {
            case ">":
                inputPos[1] = y - 1;
                break;
            case "<":
                inputPos[1] = y + 1;
                break;
            case "^":
                inputPos[0] = x + 1;
                break;
            case "v":
                inputPos[0] = x - 1;
                break;        
            default:
                break;
        }
        
        if (isValid = inRange(inputPos, state)) {
            if (state[inputPos[0]][inputPos[1]].equals(".")) {
                isValid = false;
            } else {
                value = state[inputPos[0]][inputPos[1]];
            }

        }

    }

    public boolean perform(String[][] state, boolean[][] updated) {
        if (!isValid) return false;
        int[] outPos = new int[]{x, y};
        switch (op) {
            case ">":
                outPos[1] = y + 1;
                break;
            case "<":
                outPos[1] = y - 1;
                break;
            case "^":
                outPos[0] = x - 1;
                break;
            case "v":
                outPos[0] = x + 1;
                break;        
            default:
                break;
        }     
        
        if (inRange(outPos, state)) {
            if (updated[outPos[0]][outPos[1]]) {
                System.out.printf("Multiple modification attempts at x = %d, y = %d !\n", outPos[0],outPos[1]);
                return false;                
            }
            state[outPos[0]][outPos[1]] = value;
            state[inputPos[0]][inputPos[1]] = ".";
            updated[outPos[0]][outPos[1]] = true;
            return true;
        }

        return false;
    }

    public boolean isValid() {
        return isValid;
    }
}
