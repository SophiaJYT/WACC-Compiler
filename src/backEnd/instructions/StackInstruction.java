package backEnd.instructions;

import backEnd.Register;
import backEnd.RegisterType;

public class StackInstruction {
    private StackType type;
    private Register register;

    public StackInstruction(StackType type, Register register) {
        this.type = type;
        checkRegister(type, register);
        this.register = register;
    }

    //Checks if the instruction is associated with the right type of register
    private void checkRegister(StackType type, Register register) throws IllegalArgumentException {
        RegisterType regType = register.getType();
        if(type == StackType.POP) {
            if (regType != RegisterType.PC && regType != RegisterType.R0) {
                throw new IllegalArgumentException();
            }
        }

        if(type == StackType.PUSH) {
            if (regType != RegisterType.LR && regType != RegisterType.R0) {
                throw new IllegalArgumentException();
            }
        }
    }

    @Override
    public String toString() {
        return type + " " + "{" + register + "}";
    }
}
