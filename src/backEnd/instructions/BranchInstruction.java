package backEnd.instructions;

public class BranchInstruction implements Instruction {
    private BranchType type;
    private String label;

    public BranchInstruction(BranchType type, String label) {
        this.type = type;
        checkLabel(label);
        this.label = label;
    }

    //TO-DO: checks if label exists
    private void checkLabel(String label) {

    }

    @Override
    public String toString(){
        return type + " " + label;
    }
}
