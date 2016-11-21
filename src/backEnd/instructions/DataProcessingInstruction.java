package backEnd.instructions;

import backEnd.Register;
import backEnd.RegisterType;

public class DataProcessingInstruction implements Instruction {

    private DataProcessingType type;
    private Register destination;
    private Register operand1;
    private Operand operand2;

    public DataProcessingInstruction
            (DataProcessingType type, Register destination, Operand operand) {

        if (type != DataProcessingType.MOV && type != DataProcessingType.CMP) {
            throw error(type);
        }

        this.type = type;
        this.destination = destination;
        this.operand2 = operand;

    }

    public DataProcessingInstruction
            (DataProcessingType type, Register destination, Register register, Operand operand) {

        if (type == DataProcessingType.MOV || type == DataProcessingType.CMP) {
            throw error(type);
        }

        this.type = type;
        this.destination = destination;
        this.operand1 = register;
        this.operand2 = operand;
    }

    //error method for incorrect instruction
    private IllegalArgumentException error(DataProcessingType type) {
        return new IllegalArgumentException("Incorrect arguments for instruction " + type);
    }

    @Override
    public String toString(){
        String strForOperand1 = "";
        if (operand1 != null) {
            strForOperand1 =  ", " + operand1;
        }

        return type + " " + destination + strForOperand1 + ", " + operand2;
    }

}
