package backEnd.instructions;

public class BranchInstruction implements Instruction {
    private BranchType type;
    private String label;

    public BranchInstruction(BranchType type, String label) {
        this.type = type;
        this.label = label;
    }

    @Override
    public String toString(){
        return type + " " + label;
    }
}
