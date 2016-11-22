package backEnd.instructions;

import backEnd.Register;

public class MultiplyInstruction implements Instruction {

    private MultiplyInstructionType type;
    private Register destination;
    private Register operand1;
    private Register operand2;
    private Register operand3;

    public MultiplyInstruction(MultiplyInstructionType type, Register destination,
                               Register operand1, Register operand2, Register operand3) {
        this.type = type;
        this.destination = destination;
        this.operand1 = operand1;
        this.operand2 = operand2;
        this.operand3 = operand3;
    }

    @Override
    public String toString() {
        return type + " " + destination + ", " + operand1 + ", " + operand2 + ", " + operand3;
    }
}
