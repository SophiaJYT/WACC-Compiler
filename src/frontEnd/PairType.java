package frontEnd;

public class PairType implements Type {

    private Type lhs;
    private Type rhs;

    public PairType(Type lhs, Type rhs){
        this.lhs = lhs;
        this.rhs = rhs;
    }

    public Type getLeft(){
        return lhs;
    }

    public Type getRight(){
        return rhs;
    }

    @Override
    public String toString(){
        return "( " + getLeft().toString() + " , " + getRight().toString() + " )";
    }

    private boolean checkTypeIsPair(Type t) {
        return t instanceof PairType || t == AllTypes.NULL || t == AllTypes.ANY;
    }

    @Override
    public boolean equalsType(Type that){
//        if (that instanceof PairType) {
//            PairType thatPair=(PairType) that;
//            return ((this.getLeft().equalsType(thatPair.getLeft())) && this
//                    .getRight().equalsType(thatPair.getRight()));
//        }
        if (that instanceof PairType) {
            Type t1 = ((PairType) that).getLeft(), t2 = ((PairType) that).getRight();
            boolean result = true;
            result = (t1 instanceof PairType) ? checkTypeIsPair(lhs) : t1.equalsType(lhs);
            result = (t2 instanceof PairType) ? result && checkTypeIsPair(rhs)
                    : result && t2.equalsType(rhs);
            return result;
        }
        return that == AllTypes.NULL || that == AllTypes.ANY;
    }
}
