package frontEnd;

public class ArrayType implements Type {

    private Type element;

    public ArrayType(Type element) {
        this.element = element;
    }

    public Type getElement() {
        return element;
    }

    @Override
    public String toString() {
        return element.toString() + "[]";
    }

    @Override
    public boolean equalsType(Type that) {
        if (that instanceof ArrayType) {
            ArrayType thatArray = (ArrayType) that;
            return this.getElement().equalsType(thatArray.getElement());
        }
        return that == AllTypes.NULL ||
               that == AllTypes.ANY ||
               that == AllTypes.STRING && this.equalsType(new ArrayType(AllTypes.CHAR)) ;
    }
}
