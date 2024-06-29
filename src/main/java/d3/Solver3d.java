package d3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.IOUtils;

public class Solver3d {
    private static final Set<String> moveOperators = new HashSet<String>(Arrays.asList(new String[]{ "<", ">", "^", "v"}));
    private static final Set<String> binaryOperators = new HashSet<String>(Arrays.asList(new String[]{"+", "-", "*", "/", "%", "=", "#"})); 
    private static final String terminationOperator = "S";
    private static final String timeWrap = "@";

    private Set<int[]> terminators;
    private List<String[][]> statesHist;
    private int task;
    private String[][] baseState;
    private String[][] state;
    private int x;
    private int y;
    private boolean failed;
    private boolean resFound;
    private String res;

    

    public Solver3d(int task) {
        this.task = task;
        this.baseState = IOUtils.load3DModel(task);
        x = baseState.length;

        y = 0;
        terminators = new HashSet<>();
        for (int i = 0; i < x; ++i) {
            for (int j = 0; j < baseState[i].length; ++j) {
                if (baseState[i][j].equals(terminationOperator)) {
                    terminators.add(new int[]{i+1,j+1});
                }
            }
            y = Math.max(y, baseState[i].length);
        }
        x += 2;
        y += 2;

        statesHist = new ArrayList<>();
        failed = false;
        resFound = false;
    }

    public long solve(int A, int B) {
        resFound = false;
        long steps = 0;
        state = makeCopy(A, B);
        printState(state);
        statesHist.add(copyState(state));

        boolean isTerminated = false;
        while (!isTerminated) {
            ++steps;
            isTerminated = makeTurn();
            statesHist.add(copyState(state));

            printState(state);

            if (resFound) break;

            //if (steps > 5) break;
        }

        return failed ? 0 : (steps + 1) * x* y;
    }

    private boolean makeTurn() {
        List<Operator> list = new ArrayList<>();
        List<TimewrapOperator> timewraps = new ArrayList<>();
        for (int i = 0; i < x; ++i) {
            for (int j = 0; j < y; ++j) {
                if (moveOperators.contains(state[i][j])) {
                    list.add(new MoveOperator(state[i][j], i, j, state));
                } else if (binaryOperators.contains(state[i][j])) {
                    list.add(new BinaryOperator(state[i][j], i, j, state));                    
                } else if (state[i][j].equals(timeWrap)) {
                    TimewrapOperator tw = TimewrapOperator.get(i, j, state);
                    if (tw != null) {
                        timewraps.add(tw);
                    }
                }
            }
        }

        reduce(list);
        String val = terminationOperator;
        boolean terminate = false;
        for (int[] pos : terminators) {
            if (!state[pos[0]][pos[1]].equals(terminationOperator)) {
                resFound = true;
                res = state[pos[0]][pos[1]];
                if (val.equals(terminationOperator) || val.equals(state[pos[0]][pos[1]])) {
                    val = state[pos[0]][pos[1]];
                } else {
                    System.out.println("Different terminal values found!");
                    terminate = true;
                }

            }
        }
        if (terminate) return terminate;



        if (timewraps.size() > 0) {
            //printState(state);
            reverse(timewraps);
            //printState(state);
        }
    
        return false;
    }

    private boolean reduce(List<Operator> list) {
        boolean[][] updated = new boolean[state.length][];
        for (int i = 0; i < x; ++i) {
            updated[i] = new boolean[state[i].length];
        }

        for (Operator op: list) {
            if (op instanceof TimewrapOperator) {
                TimewrapOperator timewrap = (TimewrapOperator)op;
                //state = reverseState(timewrap.getDt());
            }
            op.perform(state, updated);
        }

        return false;
    }

    private boolean reverse(List<TimewrapOperator> timewraps) {
        boolean[][] updated = new boolean[state.length][];
        for (int i = 0; i < x; ++i) {
            updated[i] = new boolean[state[i].length];
        }

        int time = timewraps.get(0).getDt() + 1;
        state = statesHist.get(statesHist.size()-time);
        for (int i = 1; i <= time; ++i) {
            statesHist.remove(statesHist.size()-1);
        }
        for (TimewrapOperator op: timewraps) {
            if (op.getDt() + 1 != time) {
                System.out.println("Conficting timewraps.");
            }
            op.perform(state, updated);
        }

        return false;
    }


    private void printState(String[][] state) {
        for (int i = 0; i < x; ++i) {
            for (int j = 0; j < y; ++j) {
                System.out.print(state[i][j] + " ".repeat(5 - state[i][j].length()));
            }
            System.out.println();
        }
        System.out.println();
    }

    public String[][] copyState(String[][] state) {
        String[][] res = new String[x][y];
        for (int i = 0; i < x; ++i) {
            for (int j = 0; j < y; ++j) {
                res[i][j] = state[i][j];
            }
        }

        return res;
        
    }


    private String[][] makeCopy(int A, int B) {
        String[][] res = new String[x][y];
        Arrays.fill(res[0], ".");
        for (int i = 1; i < x-1; ++i) {
            res[i][0] = ".";
            for (int j = 1; j < y-1; ++j) {
                if (j-1 < baseState[i-1].length) {
                    if (baseState[i-1][j-1].equals("A")) {
                        res[i][j] = String.valueOf(A);
                    } else if (baseState[i-1][j-1].equals("B")) {
                        res[i][j] = String.valueOf(B);
                    } else 
                        res[i][j] = baseState[i-1][j-1];
                } else {
                    res[i][j] = ".";
                }
            }
            res[i][y-1] = ".";
        }
        Arrays.fill(res[x-1], ".");

        return res;
    }


}
