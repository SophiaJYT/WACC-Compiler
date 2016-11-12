package frontEnd;


import org.antlr.v4.runtime.ConsoleErrorListener;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

import java.util.ArrayList;
import java.util.List;

public class SyntaxErrorListener extends ConsoleErrorListener {

    private List<String> syntaxErrors;

    public SyntaxErrorListener() {
        syntaxErrors = new ArrayList<>();
    }

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer,
                            Object offendingSymbol, int line,
                            int charPositionInLine, String msg,
                            RecognitionException e) {
        syntaxErrors.add("Syntax Error: Line " + line +
                ": " + charPositionInLine + " - " + msg);
    }

    public boolean hasSyntaxErrors() {
        return !syntaxErrors.isEmpty();
    }

    private String getLineError(ParserRuleContext ctx){
        return ctx.getStart().getLine() + ":" + ctx.getStart().getCharPositionInLine();
    }

    public void addSyntaxError(ParserRuleContext ctx, String msg) {
        syntaxErrors.add("Syntax Error: Line " + getLineError(ctx) + " - " + msg);
    }

    public List<String> getSyntaxErrors() {
        return syntaxErrors;
    }

}