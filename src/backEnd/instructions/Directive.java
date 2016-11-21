package backEnd.instructions;

public class Directive implements Instruction {

    private String directive;

    public Directive(String directive) {
        this.directive = directive;
    }

    @Override
    public String toString() {
        return "." + directive;
    }

}
