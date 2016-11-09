package frontEnd;

/**
 * Created by da2215 on 08/11/16.
 */
public class Identifier {

    private String name;

    public Identifier (String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    @Override
    public String toString(){
        return "identifier: " + name;
    }
}
