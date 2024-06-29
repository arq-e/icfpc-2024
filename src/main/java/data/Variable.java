package data;

import java.util.List;

public class Variable {
    private Value value;
    private boolean isTaken;
    private long bounded;

    Variable(Value value) {
        this.value = value;
        isTaken = false;
    }

    public void take(long bounded) {
        isTaken = true;

        this.bounded = bounded;
    }

    public void free() {
        isTaken = false;
        bounded = -1;
    }

    public boolean isTaken() {
        return isTaken;
    }

    public Value getValue() {
        return value;
    }

    public void update(Value value) {
        this.value = value;
    }

    public long getBounded() {
        return bounded;
    }
    
}
