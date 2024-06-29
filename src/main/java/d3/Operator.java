package d3;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public abstract class Operator {


    public abstract boolean perform(String[][] state, boolean[][] updated);

    public static boolean inRange(int[] pos, String[][] state) {
        return pos[0] >= 0 && pos[0] < state.length && pos[1] >= 0 && pos[1] < state[pos[0]].length;
    }

}
