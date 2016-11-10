package frontEnd;

/**
 * Created by da2215 on 08/11/16.
 */
public enum AllTypes implements Type {
    INT, BOOLEAN, CHAR, STRING;

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
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override
    public boolean equalsType(Type that) {
        if (that instanceof AllTypes) {
                return this == that;
        } else return false;
    }
}