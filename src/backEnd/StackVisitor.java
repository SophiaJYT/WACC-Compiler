package backEnd;

import antlr.WaccParser.*;
import antlr.WaccParserBaseVisitor;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;

public class StackVisitor extends WaccParserBaseVisitor<Integer> {

    private static int CHAR_SIZE = 1;
    private static int BOOL_SIZE = 1;
    private static int INT_SIZE = 4;
    private static int STRING_SIZE = 4;
    private static int ARRAY_SIZE = 4;
    private static int PAIR_SIZE = 4;

    @Override
    public Integer visitProg(@NotNull ProgContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public Integer visitVarInit(@NotNull VarInitContext ctx) {
        return visitType(ctx.type());
    }

    @Override
    public Integer visitStatSequence(@NotNull StatSequenceContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public Integer visitType(@NotNull TypeContext ctx) {
        if (ctx.type() != null) {
            return ARRAY_SIZE;
        }
        return visitChildren(ctx);
    }

    @Override
    public Integer visitBaseType(@NotNull BaseTypeContext ctx) {
        switch (ctx.getText()) {
            case "int":
                return INT_SIZE;
            case "bool":
                return BOOL_SIZE;
            case "char":
                return CHAR_SIZE;
            case "string":
                return STRING_SIZE;
        }
        return null;
    }

    @Override
    public Integer visitPairType(@NotNull PairTypeContext ctx) {
        return PAIR_SIZE;
    }

    // Ignore stack size in different scopes, as we are only interested in global scope //

    @Override
    public Integer visitFuncDecl(@NotNull FuncDeclContext ctx) {
        return null;
    }

    @Override
    public Integer visitIfStat(@NotNull IfStatContext ctx) {
        return null;
    }

    @Override
    public Integer visitWhileStat(@NotNull WhileStatContext ctx) {
        return null;
    }

    @Override
    public Integer visitBeginEnd(@NotNull BeginEndContext ctx) {
        return null;
    }

    // Need to make sure that we only store the sizes of the outermost types //

    @Override
    public Integer visitArrayType(@NotNull ArrayTypeContext ctx) {
        return null;
    }

    @Override
    public Integer visitPairElemType(@NotNull PairElemTypeContext ctx) {
        return null;
    }

    @Override
    public Integer visitChildren(@NotNull RuleNode ruleNode) {
        Integer result = 0;
        int n = ruleNode.getChildCount();
        for (int i = 0; i < n; i++) {
            ParseTree c = ruleNode.getChild(i);
            Integer childResult = c.accept(this);
            if (childResult != null) {
                result += childResult;
            }
        }
        return result;
    }

}