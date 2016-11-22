package backEnd;


import antlr.WaccParser.*;
import antlr.WaccParserBaseVisitor;
import backEnd.instructions.*;
import frontEnd.Identifier;
import frontEnd.SymbolTable;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;

import java.util.*;

import static backEnd.instructions.BranchType.*;
import static backEnd.instructions.DataProcessingType.*;
import static backEnd.instructions.SingleDataTransferType.*;
import static backEnd.instructions.StackType.*;

public class CodeGenerator extends WaccParserBaseVisitor<Identifier> {

    private List<Integer> stackSpace;
    private SymbolTable<Identifier> head;
    private SymbolTable<Identifier> curr;
    private Deque<Instruction> instrs;
    private int labelIndex, currStackPos;

    private static int CHAR_SIZE = 1, BOOL_SIZE = 1, INT_SIZE = 4, STRING_SIZE = 4;

    private Register r0, r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, sp, lr, pc;

    public CodeGenerator() {
        head = new SymbolTable<>();
        curr = head;
        stackSpace = new ArrayList<>();
        instrs = new ArrayDeque<>();
        initialiseRegisters();
        labelIndex = 0;
    }

    private void initialiseRegisters() {
        r0 = new Register(RegisterType.R0);
        r1 = new Register(RegisterType.R1);
        r2 = new Register(RegisterType.R2);
        r3 = new Register(RegisterType.R3);
        r4 = new Register(RegisterType.R4);
        r5 = new Register(RegisterType.R5);
        r6 = new Register(RegisterType.R6);
        r7 = new Register(RegisterType.R7);
        r8 = new Register(RegisterType.R8);
        r9 = new Register(RegisterType.R9);
        r10 = new Register(RegisterType.R10);
        r11 = new Register(RegisterType.R11);
        r12 = new Register(RegisterType.R12);
        sp = new Register(RegisterType.SP);
        lr = new Register(RegisterType.LR);
        pc = new Register(RegisterType.PC);
    }

    //-------------------------UTILITY FUNCTIONS-----------------------------//


    private int getStackSize(List<Integer> stackSpace) {
        int result = 0;
        for (int space : stackSpace) {
            result += space;
        }
        return result;
    }

    private void addStackReserveInstrs(int totalSize) {
        instrs.addFirst(new DataProcessingInstruction<>(SUB, sp, sp, totalSize));
        instrs.addLast(new DataProcessingInstruction<>(ADD, sp, sp, totalSize));
    }

    private Label getNonFunctionLabel() {
        return new Label("L", labelIndex++, false);
    }

    private void visitFunction(Label label, ParserRuleContext ctx) {
        instrs.add(label);
        instrs.add(new StackInstruction(PUSH, lr));

        generateInstrs(ctx);

        if (label.getName().equals("main")) {
            instrs.add(new SingleDataTransferInstruction<>(LDR, r0, 0));
        }

        instrs.add(new StackInstruction(POP, pc));
        instrs.add(new Directive("ltorg"));
    }

    private void generateInstrs(ParserRuleContext ctx) {
        // Store instructions in scope in an empty deque
        Deque<Instruction> old = instrs;
        instrs = new ArrayDeque<>();


        visit(ctx);

        // Calculates amount of space required for the current scope
        int stackSize = getStackSize(stackSpace);

        // Add the instructions needed for storing all the local variables in the scope
        if (stackSize > 0) {
            addStackReserveInstrs(stackSize);
        }

        // Add the instructions and change the pointer to the original set of instructions
        old.addAll(instrs);
        instrs = old;
    }

    //------------------------------VISIT METHODS----------------------------//

    @Override
    public Identifier visitProg(@NotNull ProgContext ctx) {
        // .data (only created when we have a string literal or read/print(ln) statements)
        // (need to generate string literals here)

        instrs.add(new Directive("text"));
        instrs.add(new Directive("global main"));

        for (FuncDeclContext func : ctx.funcDecl()) {
            visitFuncDecl(func);
        }

        visitFunction(new Label("main"), ctx.stat());

        // Print instructions to standard output
        for (Instruction instr : instrs) {
            System.out.println(instr);
        }

        return null;
    }

    @Override
    public Identifier visitFuncDecl(@NotNull FuncDeclContext ctx) {
        // Need to change to function symbol table when visiting the function
        String funName = ctx.ident().getText();
        visitFunction(new Label(funName, null, true), ctx.stat());
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

        // This works because we know that the expression is an int literal
        Integer exitCode = Integer.parseInt(ctx.expr().getText());
        instrs.add(new SingleDataTransferInstruction<>(LDR, r4, exitCode));
        instrs.add(new DataProcessingInstruction<>(MOV, r0, r4));
        instrs.add(new BranchInstruction(BL, new Label("exit")));
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

        Label elseLabel = getNonFunctionLabel();
        Label fiLabel = getNonFunctionLabel();
        visitExpr(ctx.expr());
        instrs.add(new DataProcessingInstruction<>(CMP, r4, 0));
        instrs.add(new BranchInstruction(BEQ, elseLabel));
        visit(ctx.stat(0));
        instrs.add(new BranchInstruction(B, fiLabel));
        instrs.add(elseLabel);
        visit(ctx.stat(1));
        instrs.add(fiLabel);
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

        Label exitLabel = getNonFunctionLabel();
        Label loopLabel = getNonFunctionLabel();
        instrs.add(new BranchInstruction(B, exitLabel));
        instrs.add(loopLabel);
        visit(ctx.stat());
        instrs.add(exitLabel);
        visitExpr(ctx.expr());
        instrs.add(new DataProcessingInstruction<>(CMP, r4, 1));
        instrs.add(new BranchInstruction(BEQ, loopLabel));
        return null;
    }

    @Override
    public Identifier visitBeginEnd(@NotNull BeginEndContext ctx) {
        // Create new scopes for current symbol table and stack space
        curr = new SymbolTable<>(curr);
        List<Integer> old = stackSpace;
        stackSpace = new ArrayList<>();

        generateInstrs(ctx);

        // Exit the current scope for stack space and current symbol table
        stackSpace = old;
        curr = curr.encSymbolTable;
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
        // visitChildren(ctx);
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
        if (ctx.type() != null) {
            // Need to deal with array type somehow.
            return null;
        }
        visitChildren(ctx);
        return null;
    }

    @Override
    public Identifier visitBaseType(@NotNull BaseTypeContext ctx) {
        switch (ctx.getText()) {
            case "int":
                stackSpace.add(INT_SIZE);
                break;
            case "bool":
                stackSpace.add(BOOL_SIZE);
                break;
            case "char":
                stackSpace.add(CHAR_SIZE);
                break;
            case "string":
                stackSpace.add(STRING_SIZE);
                break;
        }
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
        if (ctx.binaryOper() != null) {
            return visitBinaryOper(ctx.binaryOper());
        }
        if (ctx.boolBinaryOper() != null) {
            return visitBoolBinaryOper(ctx.boolBinaryOper());
        }
        if (ctx.unaryOper() != null) {
            return visitUnaryOper(ctx.unaryOper());
        }
        return visitChildren(ctx);
    }

    @Override
    public Identifier visitBracketExpr(@NotNull BracketExprContext ctx) {
        // Need to have more priority on bracketed expressions.
        visitExpr(ctx.expr());
        return null;
    }

    @Override
    public Identifier visitUnaryOper(@NotNull UnaryOperContext ctx) {
        ExprContext e = (ExprContext) ctx.getParent();
        switch (ctx.getText()) {
            case "!":
                break;
            case "-":
                break;
            case "len":
                break;
            case "ord":
                break;
            case "chr":
                break;
        }
        return null;
    }

    @Override
    public Identifier visitBoolBinaryOper(@NotNull BoolBinaryOperContext ctx) {
        ExprContext e = (ExprContext) ctx.getParent();
        switch (ctx.getText()) {
            case "||":
                break;
            case "&&":
                break;
        }
        return null;
    }

    @Override
    public Identifier visitBinaryOper(@NotNull BinaryOperContext ctx) {
        ExprContext e = (ExprContext) ctx.getParent();
        switch (ctx.getText()) {
            case "*":
                break;
            case "/":
                break;
            case "%":
                break;
            case "+":
                break;
            case "-":
                break;
            case ">":
                break;
            case ">=":
                break;
            case "<":
                break;
            case "<=":
                break;
            case "==":
                break;
            case "!=":
                break;
        }
        return null;
    }

    @Override
    public Identifier visitIdent(@NotNull IdentContext ctx) {
        // Need to check this
        return curr.lookUpAll(ctx.getText());
    }

    @Override
    public Identifier visitArrayElem(@NotNull ArrayElemContext ctx) {
        return null;
    }

    @Override
    public Identifier visitIntLiter(@NotNull IntLiterContext ctx) {
        int intLiter = Integer.parseInt(ctx.getText());
        return new Identifier("" + intLiter);
    }

    @Override
    public Identifier visitBoolLiter(@NotNull BoolLiterContext ctx) {
        int boolLiter = ctx.getText().equals("true") ? 1 : 0;
        return new Identifier("" + boolLiter);
    }

    @Override
    public Identifier visitCharLiter(@NotNull CharLiterContext ctx) {
        char charLiter = ctx.getText().charAt(1);
        return new Identifier("" + charLiter);
    }

    @Override
    public Identifier visitStrLiter(@NotNull StrLiterContext ctx) {
        return null;
    }

    @Override
    public Identifier visitArrayLiter(@NotNull ArrayLiterContext ctx) {
        int length = 0;
        for (ExprContext e : ctx.expr()) {

        }
        instrs.add(new SingleDataTransferInstruction<>(LDR, r5, length));
        instrs.add(new SingleDataTransferInstruction<>(STR, r5, r4));
        instrs.add(new SingleDataTransferInstruction<>(STR, r4, sp));
        return null;
    }

    @Override
    public Identifier visitPairLiter(@NotNull PairLiterContext ctx) {
        return null;
    }

    @Override
    public Identifier visitChildren(@NotNull RuleNode ruleNode) {
        Identifier result = null;
        int n = ruleNode.getChildCount();
        for (int i = 0; i < n; i++) {
            ParseTree c = ruleNode.getChild(i);
            Identifier childResult = c.accept(this);
            if (childResult != null) {
                result = childResult;
            }
        }
        return result;
    }

}
