package frontEnd;

public enum AllTypes implements Type {
    INT, BOOL, CHAR, STRING, NULL, ANY;

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
            case NULL:
                return "null";
            case ANY:
                return "";
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override
    public boolean equalsType(Type that) {
        if (that instanceof AllTypes) {
            return this == that || this == ANY || that == ANY;
        }
        return this == ANY || this == NULL && (that instanceof ArrayType || that instanceof PairType);
    }
}