package backEnd.instructions;

import backEnd.Register;

public class StackInstruction {
    private StackType type;
    private Register register;

    public StackInstruction(StackType type, Register register) {
        this.type = type;
        checkRegister(register);
        this.register = register;
    }

    private void checkRegister(Register register) throws IllegalArgumentException {
//        if(register != correct Register type) {
//            throw new IllegalArgumentException();
//        }
    }

    @Override
    public String toString() {
        return type + " " + register;
    }
}
