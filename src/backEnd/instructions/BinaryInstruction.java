package backEnd.instructions;

import backEnd.Register;

public class BinaryInstruction implements Instruction{

    private BinaryType type;
    private Register register1;
    private Register register2;

    public BinaryInstruction(BinaryType type, Register register1, Register register2){
        this.type = type;
        this.register1 = register1;
        this.register2 = register2;
    }

    @Override
    public String toString(){
        return type + " " + register1 + ", " + register2;
    }

}
