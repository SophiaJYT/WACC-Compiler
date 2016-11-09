package frontEnd;

/**
 * Created by da2215 on 08/11/16.
 */
public enum AllTypes implements Type {
    INT, BOOLEAN, CHAR, STRING, ARRAY, PAIR;

    @Override
    public String toString() throws IllegalArgumentException {
        switch (this) {
            case INT:
                return "int";
            case BOOLEAN:
                return "boolean";
            case CHAR:
                return "char";
            case STRING:
                return "string";
            case ARRAY:
                return "array";
            case PAIR:
                return "pair";
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override
    public boolean equalsType(Type that) {
        if (that instanceof Type) {
            if (this == INT || this == BOOLEAN || this == CHAR || this == STRING) {
                return this == that;
            } else return this.equalsType(that);
        } else return false;
    }
}