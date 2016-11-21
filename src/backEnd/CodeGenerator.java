package backEnd;


import antlr.WaccParser.*;
import antlr.WaccParserBaseVisitor;
import backEnd.instructions.*;
import frontEnd.Identifier;
import frontEnd.SymbolTable;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.RuleNode;

import java.util.List;

public class CodeGenerator extends WaccParserBaseVisitor<Identifier> {

    private SymbolTable<Identifier> head;
    private SymbolTable<Identifier> curr;
    // private List<Instruction> generatedCode;

    @Override
    public Identifier visitProg(@NotNull ProgContext ctx) {
        // PUSH {lr}
        // visitChildren(ctx);
        // LDR r0, =0
        // POP {pc}
        return null;
    }

    @Override
    public Identifier visitFuncDecl(@NotNull FuncDeclContext ctx) {
        return null;
    }

    @Override
    public Identifier visitParamList(@NotNull ParamListContext ctx) {
        return null;
    }

    @Override
    public Identifier visitParam(@NotNull ParamContext ctx) {
        return null;
    }

    @Override
    public Identifier visitSkip(@NotNull SkipContext ctx) {
        // Nothing to do for skip.
        return null;
    }

    @Override
    public Identifier visitVarInit(@NotNull VarInitContext ctx) {
        return null;
    }

    @Override
    public Identifier visitVarAssign(@NotNull VarAssignContext ctx) {
        return null;
    }

    @Override
    public Identifier visitReadStat(@NotNull ReadStatContext ctx) {
        // Need to branch to a "BL read" statement, depending on the type of ctx.assignLhs()
        return null;
    }

    @Override
    public Identifier visitFreeStat(@NotNull FreeStatContext ctx) {
        return null;
    }

    @Override
    public Identifier visitReturnStat(@NotNull ReturnStatContext ctx) {
        return null;
    }

    @Override
    public Identifier visitExitStat(@NotNull ExitStatContext ctx) {
        // LDR r4, =(int_liter)
        // MOV r0, r4
        // BL exit
        return null;
    }

    @Override
    public Identifier visitPrintStat(@NotNull PrintStatContext ctx) {
        // Need to branch to a "BL print" statement, depending on the type of ctx.expr()
        // Instruction printInstr = new BranchInstruction(BranchType.BL,
        //        "print" + visitExpr(ctx.expr()).getName());

        return null;
    }

    @Override
    public Identifier visitPrintlnStat(@NotNull PrintlnStatContext ctx) {
        // Same as visitPrintStat, except we have to append '\n' to the string
        // Instruction printInstr = new BranchInstruction(BranchType.BL,
        //        "print" + visitExpr(ctx.expr().getName()));
        // Need to
        return null;
    }

    @Override
    public Identifier visitIfStat(@NotNull IfStatContext ctx) {
        return null;
    }

    @Override
    public Identifier visitWhileStat(@NotNull WhileStatContext ctx) {
        return null;
    }

    @Override
    public Identifier visitBeginEnd(@NotNull BeginEndContext ctx) {
        return null;
    }

    @Override
    public Identifier visitStatSequence(@NotNull StatSequenceContext ctx) {
        return null;
    }

    @Override
    public Identifier visitAssignLhs(@NotNull AssignLhsContext ctx) {
        return null;
    }

    @Override
    public Identifier visitAssignRhs(@NotNull AssignRhsContext ctx) {
        return null;
    }

    @Override
    public Identifier visitNewPair(@NotNull NewPairContext ctx) {
        return null;
    }

    @Override
    public Identifier visitCallFunc(@NotNull CallFuncContext ctx) {
        return null;
    }

    @Override
    public Identifier visitArgList(@NotNull ArgListContext ctx) {
        return null;
    }

    @Override
    public Identifier visitPairElem(@NotNull PairElemContext ctx) {
        return null;
    }

    @Override
    public Identifier visitType(@NotNull TypeContext ctx) {
        return null;
    }

    @Override
    public Identifier visitBaseType(@NotNull BaseTypeContext ctx) {
        return null;
    }

    @Override
    public Identifier visitArrayType(@NotNull ArrayTypeContext ctx) {
        return null;
    }

    @Override
    public Identifier visitPairType(@NotNull PairTypeContext ctx) {
        return null;
    }

    @Override
    public Identifier visitPairElemType(@NotNull PairElemTypeContext ctx) {
        return null;
    }

    @Override
    public Identifier visitExpr(@NotNull ExprContext ctx) {
        return null;
    }

    @Override
    public Identifier visitBracketExpr(@NotNull BracketExprContext ctx) {
        return null;
    }

    @Override
    public Identifier visitUnaryOper(@NotNull UnaryOperContext ctx) {
        return null;
    }

    @Override
    public Identifier visitBoolBinaryOper(@NotNull BoolBinaryOperContext ctx) {
        return null;
    }

    @Override
    public Identifier visitBinaryOper(@NotNull BinaryOperContext ctx) {
        return null;
    }

    @Override
    public Identifier visitIdent(@NotNull IdentContext ctx) {
        return null;
    }

    @Override
    public Identifier visitArrayElem(@NotNull ArrayElemContext ctx) {
        return null;
    }

    @Override
    public Identifier visitIntLiter(@NotNull IntLiterContext ctx) {
        return null;
    }

    @Override
    public Identifier visitBoolLiter(@NotNull BoolLiterContext ctx) {
        return null;
    }

    @Override
    public Identifier visitCharLiter(@NotNull CharLiterContext ctx) {
        return null;
    }

    @Override
    public Identifier visitStrLiter(@NotNull StrLiterContext ctx) {
        return null;
    }

    @Override
    public Identifier visitArrayLiter(@NotNull ArrayLiterContext ctx) {
        return null;
    }

    @Override
    public Identifier visitPairLiter(@NotNull PairLiterContext ctx) {
        return null;
    }

    @Override
    public Identifier visitChildren(@NotNull RuleNode ruleNode) {
        return null;
    }

}
