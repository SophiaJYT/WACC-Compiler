package backEnd.instructions;

import backEnd.Register;

import java.util.ArrayList;
import java.util.List;

public class DataProcessingInstruction <T> implements Instruction {

    private DataProcessingType type;
    private Register destination;
    private Register operand1;
    private T operand2;
    private ShiftInstruction shiftInstruction;
    //list for the instructions that only have two arguments
    private List<DataProcessingType> list = new ArrayList<DataProcessingType>();

    public DataProcessingInstruction
            (DataProcessingType type, Register destination, T operand) {

        initializeList();

        if (!list.contains(type)) {
            throw errorInstr(type);
        }

        checkOperand(operand);

        this.type = type;
        this.destination = destination;
        this.operand2 = operand;

    }

    public DataProcessingInstruction
            (DataProcessingType type, Register destination, Register register, T operand) {

        initializeList();

        if (list.contains(type)) {
            throw errorInstr(type);
        }

        if(type == DataProcessingType.ADDS && !(operand instanceof Register)) {
            throw errorInstr(type);
        }
        checkOperand(operand);

        this.type = type;
        this.destination = destination;
        this.operand1 = register;
        this.operand2 = operand;
    }

    public DataProcessingInstruction(DataProcessingType type, Register destination,
                                     Register register, T operand, ShiftInstruction shiftInstruction) {
        initializeList();

        if (type != DataProcessingType.ADD) {
            throw errorInstr(type);
        }
        checkOperand(operand);

        this.type = type;
        this.destination = destination;
        this.operand1 = register;
        this.operand2 = operand;
        this.shiftInstruction = shiftInstruction;
    }

    @Override
    public String toString(){
        String strForOperand1 = "";
        if (operand1 != null) {
            strForOperand1 =  ", " + operand1;
        }

        String strForOperand2 = operand2.toString();
        if(operand2 instanceof Integer) {
            strForOperand2 = '#' + strForOperand2;
        }

        return type + " " + destination + strForOperand1 + ", " + strForOperand2
                + shiftInstruction != null? shiftInstruction.toString() :  "";
    }

    //makes sure the list is initialized only once
    private void initializeList() {
        if(list.isEmpty()) {
            addTypes();
        }
    }

    //adds types to the list
    private void addTypes() {
        list.add(DataProcessingType.CMP);
        list.add(DataProcessingType.MOV);
        list.add(DataProcessingType.MOVEQ);
        list.add(DataProcessingType.MOVGE);
        list.add(DataProcessingType.MOVGT);
        list.add(DataProcessingType.MOVLE);
        list.add(DataProcessingType.MOVLT);
        list.add(DataProcessingType.MOVNE);
    }

    //checks if operand2 is valid
    private void checkOperand(T operand) throws IllegalArgumentException {
        if (!(operand instanceof Integer) && !(operand instanceof Register)) {
            throw new  IllegalArgumentException("Instr: " + type +
                    " Operand has to be a register or number/expression");
        }
    }

    //error method for incorrect arguments for an instruction
    private IllegalArgumentException errorInstr(DataProcessingType type) {
        return new IllegalArgumentException("Incorrect arguments for instruction " + type);
    }

}
