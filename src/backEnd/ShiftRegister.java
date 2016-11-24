package backEnd;

public class ShiftRegister extends Register{

    private Integer offset;
    private Character exclamation;

    public ShiftRegister(RegisterType register, Integer offset, Character exclamation) {
        super(register);
        this.offset = offset;
        if(exclamation != null) {
            this.exclamation = exclamation;
        }
    }

    @Override
    public String toString() {
//        if (exclamation == null) {
//            exclamation = ' ';
//        }
//        return "[" + super.toString() + ", #" + offset + "]" + exclamation;
        return super.toString() +
                ((offset == 0) ? "" : ", #" + offset)
                + ((exclamation == null) ? "" : exclamation);
    }
}
