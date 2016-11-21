package backEnd;

public class Register {

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
