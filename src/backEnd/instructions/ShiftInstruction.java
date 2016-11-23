package backEnd.instructions;

public class ShiftInstruction implements Instruction{

    private ShiftType type;
    private Integer amount;

    public ShiftInstruction(ShiftType type, Integer amount) {
        this.type = type;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return type + " #" + amount;
    }
}
