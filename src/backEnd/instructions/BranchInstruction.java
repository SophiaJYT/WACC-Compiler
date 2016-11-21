package backEnd.instructions;

public class BranchInstruction implements Instruction {
    private BranchType type;
    private Label label;

    public BranchInstruction(BranchType type, Label label) {
        this.type = type;
        checkLabel(label);
        this.label = label;
    }

    //TO-DO: checks if label exists
    private void checkLabel(Label label) {

    }

    @Override
    public String toString(){
        return type + " " + label;
    }
}
