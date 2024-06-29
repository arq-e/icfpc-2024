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

    public String getString() {
        return (String) value;
    }

    public Long getInt() {
        return (Long) value;
    }

    public Boolean getBoolean() {
        return (Boolean) value;
    }

}
