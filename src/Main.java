import antlr.WaccLexer;
import antlr.WaccParser;
import frontEnd.SyntaxErrorListener;
import frontEnd.WaccVisitor;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        WaccLexer lexer = new WaccLexer(new ANTLRInputStream(System.in));

        WaccParser parser = new WaccParser(new CommonTokenStream(lexer));

        parser.removeErrorListeners();

        SyntaxErrorListener listener = new SyntaxErrorListener();

        parser.addErrorListener(listener);

        ParseTree tree = parser.prog();

        WaccVisitor visitor = new WaccVisitor(listener);

        visitor.visit(tree);
    }

}
