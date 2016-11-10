package frontEnd;

import org.antlr.runtime.tree.ParseTree;

/**
 * Created by ab6015 on 10/11/16.
 */
public class ErrorClass {
    ParseTree ctx;

    public void throwErrors(ParseTree ctx, String errorMessage, int errorCode){
        System.err.println(errorMessage + "at line " + ctx.getLine() + ".");
        System.err.print("Exitcode: " + errorCode + ".");
        System.exit(errorCode);
    }
}