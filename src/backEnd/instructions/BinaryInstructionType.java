package backEnd.instructions;

public enum BinaryInstructionType {
    ADD, SUB, MUL, AND, ORR, EOR, RSB, MOV;

    @Override
    public String toString(){
        switch(this){
            case ADD: return "ADD";
            case SUB: return "SUB";
            case MUL: return "MUL";
            case AND: return "AND";
            case ORR: return "ORR";
            case EOR: return "EOR";
            case RSB: return "RSB";
            case MOV: return "MOV";
            default: return null;
        }
    }
}
