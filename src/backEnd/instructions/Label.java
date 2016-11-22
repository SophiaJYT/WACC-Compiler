package backEnd.instructions;


public class Label implements Instruction{

    private String label;
    private Integer index;
    private boolean isFunction;

    public Label(String label){
        this.label = label;
    }

    public Label(String label, Integer index, boolean isFunction){
        if (!isFunction) {
            this.index = index;
            this.label = label;
        }
        this.label = "f_" + label;
        this.isFunction = true;
    }

    public int getIndex(){
        return index;
    }

    public String getName(){
        if(isFunction) {
            return "f_";
        } else {
            return label;
        }
    }

    @Override
    public String toString(){
        if(index != null) {
            return label + index + ":\n";
        }
        return  label + ":\n";
    }


}
