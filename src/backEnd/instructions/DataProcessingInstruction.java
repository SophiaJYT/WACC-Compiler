package backEnd.instructions;

import backEnd.Register;

public class DataProcessingInstruction <T> implements Instruction {

    private DataProcessingType type;
    private Register destination;
    private Register operand1;
    private T operand2;

    public DataProcessingInstruction
            (DataProcessingType type, Register destination, T operand) {

        if (type != DataProcessingType.MOV && type != DataProcessingType.CMP) {
            throw errorInstr(type);
        }
        checkOperand(operand);
        this.type = type;
        this.destination = destination;
        this.operand2 = operand;

    }

    public DataProcessingInstruction
            (DataProcessingType type, Register destination, Register register, T operand) {

        if (type == DataProcessingType.MOV || type == DataProcessingType.CMP) {
            throw errorInstr(type);
        }
        checkOperand(operand);
        this.type = type;
        this.destination = destination;
        this.operand1 = register;
        this.operand2 = operand;
    }

    private void checkOperand(T operand) throws IllegalArgumentException {
        if (!(operand instanceof Integer) && !(operand instanceof Register)
                && !(operand instanceof Character)) {
            throw new  IllegalArgumentException("Instr: " + type +
                    " Operand has to be a register or number/expression");
        }
    }

    //error method for incorrect instruction
    private IllegalArgumentException errorInstr(DataProcessingType type) {
        return new IllegalArgumentException("Incorrect arguments for instruction " + type);
    }

    @Override
    public String toString(){
        String strForOperand1 = "";
        if (operand1 != null) {
            strForOperand1 =  ", " + operand1;
        }

        String strForOperand2 = operand2.toString();
        if (operand2 instanceof Integer) {
            strForOperand2 = '#' + strForOperand2;
        }
        if (operand2 instanceof Character) {
            strForOperand2 = "#\'" + strForOperand2 + "\'";
        }

        return type + " " + destination + strForOperand1 + ", " + strForOperand2;
    }

}
