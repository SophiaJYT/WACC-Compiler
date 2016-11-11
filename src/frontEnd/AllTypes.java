package frontEnd;

/**
 * Created by da2215 on 08/11/16.
 */
public enum AllTypes implements Type {
    INT, BOOL, CHAR, STRING, ANY;

    @Override
    public String toString() throws IllegalArgumentException {
        switch (this) {
            case INT:
                return "int";
            case BOOL:
                return "bool";
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
                return this == that || that == ANY;
        }
        return this == ANY;
    }
}