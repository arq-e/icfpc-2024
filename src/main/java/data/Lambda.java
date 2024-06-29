package data;

import java.util.HashMap;
import java.util.Map;

public class Lambda {
    private Map<Long, Value> params;

    public Lambda() {
        params = new HashMap<>();
    }

    public Map<Long, Value> getParams() {
        return params;
    }
}
