package data;

public class Value {
    Object value;
    Type type;

    public Value(Type type, Object value) {
        this.value = value;
        this.type = type;
    }

    public void write(StringBuilder sb) {
        switch (this.type) {
            case INTEGER:
                sb.append((Long)this.value);
                break;
            case STRING:
                sb.append((String)this.value);
                break;
            case BOOLEAN:
                sb.append((Boolean)this.value);
                break;
            default:
                break;
        }
    }

    public Type getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }

    public String getString() {
        return (String) value;
    }

    public Long getInt() {
        return (Long) value;
    }

    public Boolean getBoolean() {
        return (Boolean) value;
    }

    public void update(Value newValue) {
        this.type = newValue.getType();
        this.value = newValue.getValue();
    }

    public String toString() {
        switch (this.type) {
            case INTEGER:
                return String.valueOf((Long)this.value);
            case STRING:
                return (String)this.value;
            case BOOLEAN:
                return String.valueOf((Boolean)this.value);
            case VARIABLE:
                return "x" + String.valueOf((Long)this.value);
            case OPERATION:
                String s = (String)this.value;
                if (s.length() == 0) {
                    return s;
                } else {
                    return s.substring(1);
                }
            case LAMBDA:
                return "l" + String.valueOf((Long)this.value) + "->";            
            default:
                return null;
        }        
    }

}
