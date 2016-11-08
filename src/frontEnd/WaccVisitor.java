package frontEnd;

import antlr.WaccParser;
import antlr.WaccParserVisitor;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;

/**
 * Created by rn1815 on 08/11/16.
 */
public class WaccVisitor<T> implements WaccParserVisitor<T> {


    @Override
    public T visitRead_lhs(@NotNull WaccParser.Read_lhsContext ctx) {
        return null;
    }

    @Override
    public T visitArray_elem(@NotNull WaccParser.Array_elemContext ctx) {
        return null;
    }

    @Override
    public T visitIdent(@NotNull WaccParser.IdentContext ctx) {
        return null;
    }

    @Override
    public T visitAnInt(@NotNull WaccParser.AnIntContext ctx) {
        return null;
    }

    @Override
    public T visitArrayLHSElemAssign(@NotNull WaccParser.ArrayLHSElemAssignContext ctx) {
        return null;
    }

    @Override
    public T visitArrayLitterRHSAssign(@NotNull WaccParser.ArrayLitterRHSAssignContext ctx) {
        return null;
    }

    @Override
    public T visitChar_liter(@NotNull WaccParser.Char_literContext ctx) {
        return null;
    }

    @Override
    public T visitBinary_oper(@NotNull WaccParser.Binary_operContext ctx) {
        return null;
    }

    @Override
    public T visitABool(@NotNull WaccParser.ABoolContext ctx) {
        return ctx.bool_liter().FALSE();
    }

    @Override
    public T visitPrintExpr(@NotNull WaccParser.PrintExprContext ctx) {
        return null;
    }

    @Override
    public T visitSecondFirstExpr(@NotNull WaccParser.SecondFirstExprContext ctx) {
        return null;
    }

    @Override
    public T visitAChar(@NotNull WaccParser.ACharContext ctx) {
        return null;
    }

    @Override
    public T visitPairLHSElemAssign(@NotNull WaccParser.PairLHSElemAssignContext ctx) {
        return null;
    }

    @Override
    public T visitBool_liter(@NotNull WaccParser.Bool_literContext ctx) {
        return null;
    }

    @Override
    public T visitAString(@NotNull WaccParser.AStringContext ctx) {
        return null;
    }

    @Override
    public T visitAPair(@NotNull WaccParser.APairContext ctx) {
        return null;
    }

    @Override
    public T visitArray_liter(@NotNull WaccParser.Array_literContext ctx) {
        return null;
    }

    @Override
    public T visitPair_elem_type(@NotNull WaccParser.Pair_elem_typeContext ctx) {
        return null;
    }

    @Override
    public T visitBeginEnd(@NotNull WaccParser.BeginEndContext ctx) {
        return null;
    }

    @Override
    public T visitPairElemRHSAssign(@NotNull WaccParser.PairElemRHSAssignContext ctx) {
        return null;
    }

    @Override
    public T visitIdentLHSAssign(@NotNull WaccParser.IdentLHSAssignContext ctx) {
        return null;
    }

    @Override
    public T visitExitExpr(@NotNull WaccParser.ExitExprContext ctx) {
        return null;
    }

    @Override
    public T visitExprRHSAssign(@NotNull WaccParser.ExprRHSAssignContext ctx) {
        return null;
    }

    @Override
    public T visitFreeExpr(@NotNull WaccParser.FreeExprContext ctx) {
        return null;
    }

    @Override
    public T visitSkip(@NotNull WaccParser.SkipContext ctx) {
        return null;
    }

    @Override
    public T visitTypeParantheses(@NotNull WaccParser.TypeParanthesesContext ctx) {
        return null;
    }

    @Override
    public T visitBaseType(@NotNull WaccParser.BaseTypeContext ctx) {
        return null;
    }

    @Override
    public T visitInt_liter(@NotNull WaccParser.Int_literContext ctx) {
        return null;
    }

    @Override
    public T visitBase_type(@NotNull WaccParser.Base_typeContext ctx) {
        return null;
    }

    @Override
    public T visitParam(@NotNull WaccParser.ParamContext ctx) {
        return null;
    }

    @Override
    public T visitPair_type(@NotNull WaccParser.Pair_typeContext ctx) {
        return null;
    }

    @Override
    public T visitInitialization(@NotNull WaccParser.InitializationContext ctx) {
        return null;
    }

    @Override
    public T visitIfExpr(@NotNull WaccParser.IfExprContext ctx) {
        return null;
    }

    @Override
    public T visitCallParantheses(@NotNull WaccParser.CallParanthesesContext ctx) {
        return null;
    }

    @Override
    public T visitArray_type(@NotNull WaccParser.Array_typeContext ctx) {
        return null;
    }

    @Override
    public T visitBinOp(@NotNull WaccParser.BinOpContext ctx) {
        return null;
    }

    @Override
    public T visitUnary_oper(@NotNull WaccParser.Unary_operContext ctx) {
        return null;
    }

    @Override
    public T visitPairParantheses(@NotNull WaccParser.PairParanthesesContext ctx) {
        return null;
    }

    @Override
    public T visitAssignment(@NotNull WaccParser.AssignmentContext ctx) {
        return null;
    }

    @Override
    public T visitPrintlnExpr(@NotNull WaccParser.PrintlnExprContext ctx) {
        return null;
    }

    @Override
    public T visitAnIdent(@NotNull WaccParser.AnIdentContext ctx) {
        return null;
    }

    @Override
    public T visitBracketExpr(@NotNull WaccParser.BracketExprContext ctx) {
        return null;
    }

    @Override
    public T visitReturnExpr(@NotNull WaccParser.ReturnExprContext ctx) {
        return null;
    }

    @Override
    public T visitAnArrayElem(@NotNull WaccParser.AnArrayElemContext ctx) {
        return null;
    }

    @Override
    public T visitUnOp(@NotNull WaccParser.UnOpContext ctx) {
        return null;
    }

    @Override
    public T visitProg(@NotNull WaccParser.ProgContext ctx) {
        return null;
    }

    @Override
    public T visitSemicolonStat(@NotNull WaccParser.SemicolonStatContext ctx) {
        return null;
    }

    @Override
    public T visitPair_liter(@NotNull WaccParser.Pair_literContext ctx) {
        return null;
    }

    @Override
    public T visitParam_list(@NotNull WaccParser.Param_listContext ctx) {
        return null;
    }

    @Override
    public T visitArg_list(@NotNull WaccParser.Arg_listContext ctx) {
        return null;
    }

    @Override
    public T visitPairType(@NotNull WaccParser.PairTypeContext ctx) {
        return null;
    }

    @Override
    public T visitFunc(@NotNull WaccParser.FuncContext ctx) {
        return null;
    }

    @Override
    public T visitWhileExpr(@NotNull WaccParser.WhileExprContext ctx) {
        return null;
    }

    @Override
    public T visitPairFirstExpr(@NotNull WaccParser.PairFirstExprContext ctx) {
        return null;
    }

    @Override
    public T visitStr_liter(@NotNull WaccParser.Str_literContext ctx) {
        return null;
    }

    @Override
    public T visitComment(@NotNull WaccParser.CommentContext ctx) {
        return null;
    }

    @Override
    public T visit(@NotNull ParseTree parseTree) {
        return null;
    }

    @Override
    public T visitChildren(@NotNull RuleNode ruleNode) {
        return null;
    }

    @Override
    public T visitTerminal(@NotNull TerminalNode terminalNode) {
        return null;
    }

    @Override
    public T visitErrorNode(@NotNull ErrorNode errorNode) {
        return null;
    }
}
