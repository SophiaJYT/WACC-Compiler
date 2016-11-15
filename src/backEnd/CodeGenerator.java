package backEnd;


import antlr.WaccParser.*;
import antlr.WaccParserBaseVisitor;
import backEnd.instructions.Instruction;
import frontEnd.Identifier;
import frontEnd.SymbolTable;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.RuleNode;

import java.util.List;

public class CodeGenerator extends WaccParserBaseVisitor<Instruction> {

    private SymbolTable<Identifier> head;
    private SymbolTable<Identifier> curr;
    List<Instruction> generatedCode;

    @Override
    public Instruction visitReadStat(@NotNull ReadStatContext ctx) {
        return null;
    }

    @Override
    public Instruction visitArgList(@NotNull ArgListContext ctx) {
        return null;
    }

    @Override
    public Instruction visitArrayLiter(@NotNull ArrayLiterContext ctx) {
        return null;
    }

    @Override
    public Instruction visitArrayElem(@NotNull ArrayElemContext ctx) {
        return null;
    }

    @Override
    public Instruction visitAssignRhs(@NotNull AssignRhsContext ctx) {
        return null;
    }

    @Override
    public Instruction visitPrintStat(@NotNull PrintStatContext ctx) {
        return null;
    }

    @Override
    public Instruction visitAssignLhs(@NotNull AssignLhsContext ctx) {
        return null;
    }

    @Override
    public Instruction visitUnaryOper(@NotNull UnaryOperContext ctx) {
        return null;
    }

    @Override
    public Instruction visitIdent(@NotNull IdentContext ctx) {
        return null;
    }

    @Override
    public Instruction visitSkip(@NotNull SkipContext ctx) {
        // Nothing to do for skip.
        return null;
    }

    @Override
    public Instruction visitPrintlnStat(@NotNull PrintlnStatContext ctx) {
        return null;
    }

    @Override
    public Instruction visitType(@NotNull TypeContext ctx) {
        return null;
    }

    @Override
    public Instruction visitIntLiter(@NotNull IntLiterContext ctx) {
        return null;
    }

    @Override
    public Instruction visitBaseType(@NotNull BaseTypeContext ctx) {
        return null;
    }

    @Override
    public Instruction visitPairLiter(@NotNull PairLiterContext ctx) {
        return null;
    }

    @Override
    public Instruction visitParam(@NotNull ParamContext ctx) {
        return null;
    }

    @Override
    public Instruction visitReturnStat(@NotNull ReturnStatContext ctx) {
        return null;
    }

    @Override
    public Instruction visitCharLiter(@NotNull CharLiterContext ctx) {
        return null;
    }

    @Override
    public Instruction visitVarInit(@NotNull VarInitContext ctx) {
        return null;
    }

    @Override
    public Instruction visitExpr(@NotNull ExprContext ctx) {
        return null;
    }

    @Override
    public Instruction visitPairElem(@NotNull PairElemContext ctx) {
        return null;
    }

    @Override
    public Instruction visitWhileStat(@NotNull WhileStatContext ctx) {
        return null;
    }

    @Override
    public Instruction visitIfStat(@NotNull IfStatContext ctx) {
        return null;
    }

    @Override
    public Instruction visitArrayType(@NotNull ArrayTypeContext ctx) {
        return null;
    }

    @Override
    public Instruction visitNewPair(@NotNull NewPairContext ctx) {
        return null;
    }

    @Override
    public Instruction visitBracketExpr(@NotNull BracketExprContext ctx) {
        return null;
    }

    @Override
    public Instruction visitExitStat(@NotNull ExitStatContext ctx) {
        //return branch instruction with "exit"
        return null;
    }

    @Override
    public Instruction visitBinaryOper(@NotNull BinaryOperContext ctx) {
        return null;
    }

    @Override
    public Instruction visitCallFunc(@NotNull CallFuncContext ctx) {
        return null;
    }

    @Override
    public Instruction visitProg(@NotNull ProgContext ctx) {
        return null;
    }

    @Override
    public Instruction visitVarAssign(@NotNull VarAssignContext ctx) {
        return null;
    }

    @Override
    public Instruction visitFreeStat(@NotNull FreeStatContext ctx) {
        return null;
    }

    @Override
    public Instruction visitBoolLiter(@NotNull BoolLiterContext ctx) {
        return null;
    }

    @Override
    public Instruction visitPairType(@NotNull PairTypeContext ctx) {
        return null;
    }

    @Override
    public Instruction visitPairElemType(@NotNull PairElemTypeContext ctx) {
        return null;
    }

    @Override
    public Instruction visitStrLiter(@NotNull StrLiterContext ctx) {
        return null;
    }

    @Override
    public Instruction visitBeginEnd(@NotNull BeginEndContext ctx) {
        return null;
    }

    @Override
    public Instruction visitFuncDecl(@NotNull FuncDeclContext ctx) {
        return null;
    }

    @Override
    public Instruction visitBoolBinaryOper(@NotNull BoolBinaryOperContext ctx) {
        return null;
    }

    @Override
    public Instruction visitParamList(@NotNull ParamListContext ctx) {
        return null;
    }

    @Override
    public Instruction visitStatSequence(@NotNull StatSequenceContext ctx) {
        return null;
    }

    @Override
    public Instruction visitChildren(@NotNull RuleNode ruleNode) {
        return null;
    }

}
