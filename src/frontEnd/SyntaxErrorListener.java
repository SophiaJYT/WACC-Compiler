package frontEnd;


import org.antlr.v4.runtime.ConsoleErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

public class SyntaxErrorListener extends ConsoleErrorListener {

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer,
                            Object offendingSymbol, int line,
                            int charPositionInLine, String msg,
                            RecognitionException e) {
        System.err.println("Syntax Error: Line " + line +
                " at position " + charPositionInLine + ": " + msg);
        System.exit(100);
    }

}