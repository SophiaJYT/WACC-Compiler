package frontEnd;

import antlr.WaccLexer;
import antlr.WaccParser;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        WaccLexer lexer = new WaccLexer(new ANTLRInputStream(System.in));

        WaccParser parser = new WaccParser(new CommonTokenStream(lexer));

        parser.removeErrorListeners();

        parser.addErrorListener(new SyntaxErrorListener());

        ParseTree tree = parser.prog();

        WaccVisitor visitor = new WaccVisitor();

        visitor.visit(tree);
    }

}
