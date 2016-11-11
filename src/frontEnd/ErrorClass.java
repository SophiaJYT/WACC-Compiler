package frontEnd;


import org.antlr.v4.runtime.ParserRuleContext;

public class ErrorClass {

    ParserRuleContext ctx;

    public String getLineError(ParserRuleContext ctx){
        return ctx.getStart().getLine() + ": " + ctx.getStart().getCharPositionInLine();
    }

    public void throwErrors(ParserRuleContext ctx, String errorMessage, int errorCode){

        System.err.println(errorMessage + "at line " + getLineError(ctx) + ".");
        System.err.print("Exitcode: " + errorCode + ".");
        System.exit(errorCode);
    }
}