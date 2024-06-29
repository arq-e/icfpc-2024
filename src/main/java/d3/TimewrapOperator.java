package d3;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TimewrapOperator extends Operator{   
    private int x;
    private int y;
    private Long dx;
    private Long dy;
    private Long val;
    private Long dt;
    private int [] inputPos;
    boolean isValid;

    private TimewrapOperator(int x, int y, String[][] state) {
        this.x = x;
        this.y = y;
        
        this.dy = tryParse(state[x][y - 1]);
        this.dx = tryParse(state[x][y + 1]);
        this.dt = tryParse(state[x + 1][y]);
        this.val = tryParse(state[x - 1][y]);
        
        isValid = (dx != null && dy != null && dt != null && val != null);

    }

    static TimewrapOperator get(int x, int y, String[][] state) {
        TimewrapOperator op = new TimewrapOperator(x, y, state);
        if (op.isValid) {
            return op;
        } else {
            return null;
        }
    }

    public boolean perform(String[][] state, boolean[][] updated) {
        if (!isValid) return false;
        int[] outPos = new int[]{x - (int)dx.longValue(), y - (int)dy.longValue()};
        
        if (inRange(outPos, state)) {
            if (updated[outPos[0]][outPos[1]]) {
                System.out.printf("Multiple timewrap modification attempts at x = %d, y = %d !\n", outPos[0],outPos[1]);
                return false;                
            }
            state[outPos[0]][outPos[1]] = String.valueOf(val.longValue());
            return true;
        }

        return false;
    } 

    public int getDt() {
        return (int)dt.longValue();
    }

    public Long tryParse(String s) {
        try {
            return Long.parseLong(s);
        } catch(NumberFormatException e) {

        }

        return null;
    }

}
