package backEnd;


import antlr.WaccParser.*;
import antlr.WaccParserBaseVisitor;
import backEnd.instructions.Instruction;
import frontEnd.Identifier;
import frontEnd.SymbolTable;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.RuleNode;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Enumeration;

public class CodeGenerator extends WaccParserBaseVisitor<Identifier> {

    private SymbolTable<Integer> stackSpace;
    private SymbolTable<Identifier> head;
    private SymbolTable<Identifier> curr;

    private Deque<Instruction> instrs;

    private static int CHAR_SIZE = 1, BOOL_SIZE = 1, INT_SIZE = 4, STRING_SIZE = 4;

    public CodeGenerator() {
        head = new SymbolTable<>();
        curr = head;
        stackSpace = new SymbolTable<>();
        instrs = new ArrayDeque<>();
    }

    //-------------------------UTILITY FUNCTIONS-----------------------------//

    private int getStackSize(Enumeration<Integer> e) {
        int result = 0;
        while (e.hasMoreElements()) {
            result += e.nextElement();
        }
        return result;
    }

    //------------------------------VISIT METHODS----------------------------//

    @Override
    public Identifier visitProg(@NotNull ProgContext ctx) {
        // .data
        // (need to generate string literals here)

        // Need to use varTypes symbolTable from frontEnd
        // to generate total size to allocate on stack

        // int stackSize = getStackSize(stackSpace.getValues());

        // .text
        // .global main
        // main:
        // (need to create indentation after each label)
        //   PUSH {lr}

        // stackSpace = new SymbolTable<>(stackSpace);

        // visitChildren(ctx);

        // Calculates amount of space required for the current scope
        // int stackSize = getStackSize(stackSpace.getValues());

        // instrs.addFirst(SUB sp, sp, #stackSize);
        // instrs.addLast(ADD sp, sp, #stackSize);

        // stackSpace = stackSpace.encSymbolTable;

        //   LDR r0, =0
        //   POP {pc}
        return null;
    }

    @Override
    public Identifier visitFuncDecl(@NotNull FuncDeclContext ctx) {
        // Have to add .ltorg at the end of each function (INCLUDING MAIN)
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

        // LDR r4, =(value_of_assignRhs)
        // int offset = total_size - size_of_assignRhs
        // if (offset == 0) {
        //     STR r4, [sp]
        // } else {
        //     STR r4, [sp, #offset]
        // }

        return null;
    }

    @Override
    public Identifier visitVarAssign(@NotNull VarAssignContext ctx) {

        // Should theoretically be the same as visitVarInit (not 100% sure yet)

        // LDR r4, =(value_of_assignRhs)
        // int offset = total_size - size_of_assignRhs
        // if (offset == 0) {
        //     STR r4, [sp]
        // } else {
        //     STR r4, [sp, #offset]
        // }

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
        // visitExpr(ctx.expr());
        // CMP r4, #0
        // BEQ else_label
        // visit(ctx.stat(0));
        // B fi_label

        // else_label:
        // visit(ctx.stat(1));

        // fi_label:
        return null;
    }

    @Override
    public Identifier visitWhileStat(@NotNull WhileStatContext ctx) {
        // B exit_label
        // loop_label:
        //   visit(ctx.stat());
        // exit_label:
        //   visitExpr(ctx.expr());
        //   CMP r4, #1
        //   BEQ loop_label
        return null;
    }

    @Override
    public Identifier visitBeginEnd(@NotNull BeginEndContext ctx) {
        // curr = new SymbolTable<>(curr);
        // stackSpace = new SymbolTable<>(stackSpace);

        // Deque<Instruction> old = instrs;
        // instrs = new ArrayDeque<>();

        // visitChildren(ctx);

        // Calculates amount of space required for the current scope
        // int stackSize = getStackSize(stackSpace.getValues());

        // instrs.addFirst(SUB sp, sp, #stackSize);
        // instrs.addLast(ADD sp, sp, #stackSize);
        // old.addAll(instrs);
        // instrs = old;

        // stackSpace = stackSpace.encSymbolTable;
        // curr = curr.encSymbolTable;
        return null;
    }

    @Override
    public Identifier visitStatSequence(@NotNull StatSequenceContext ctx) {
        visitChildren(ctx);
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
        // if (ctx.getText().equals("true")) {
        //     return #1;
        // } else {
        //     return #0;
        // }
        return null;
    }

    @Override
    public Identifier visitCharLiter(@NotNull CharLiterContext ctx) {
        // return new Identifier("#" + ctx.getText());
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
