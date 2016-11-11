package frontEnd;

/**
 * Created by da2215 on 09/11/16.
 */
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

    @Override
    public boolean equalsType(Type that){
        if (that instanceof PairType) {
            PairType thatPair=(PairType) that;
            return ((this.getLeft().equalsType(thatPair.getLeft())) && this
                    .getRight().equalsType(thatPair.getRight()));
        }
        return that == AllTypes.NULL;
    }
}
