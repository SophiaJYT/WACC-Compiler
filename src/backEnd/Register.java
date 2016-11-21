package backEnd;

import backEnd.instructions.Operand;

public class Register implements Operand {

    private RegisterType register;

    public Register(RegisterType register) {
        this.register = register;
    }

    public RegisterType getType() {
        return register;
    }

    @Override
    public String toString() {
        return String.valueOf(register).toLowerCase();
    }
}
