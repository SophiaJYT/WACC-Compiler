package utils;

import frontEnd.Type;

public class Identifier {

    private String val;
    private Type type;

    public Identifier(Type type, String val){
        this.val = val;
        this.type = type;
    }

    public String getVal(){
        return val;
    }

    public Type getType() {
        return type;
    }

    @Override
    public String toString(){
        return "identifier: " + val;
    }

}
