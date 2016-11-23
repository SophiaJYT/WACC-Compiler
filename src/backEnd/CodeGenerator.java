package backEnd;


import antlr.WaccParser.*;
import antlr.WaccParserBaseVisitor;
import backEnd.instructions.*;
import frontEnd.*;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;

import java.util.*;

import static backEnd.RegisterType.*;
import static backEnd.instructions.BranchType.*;
import static backEnd.instructions.DataProcessingType.*;
import static backEnd.instructions.SingleDataTransferType.*;
import static backEnd.instructions.StackType.*;

public class CodeGenerator extends WaccParserBaseVisitor<Identifier> {

    private List<Integer> stackSpace = new ArrayList<>();
    private Deque<Instruction> instrs = new ArrayDeque<>();
    private Deque<Instruction> printInstrs = new ArrayDeque<>();
    private int labelIndex = 0;

    private SymbolTable<Identifier> head = new SymbolTable<>();
    private SymbolTable<Identifier> curr = head;

    private Data data = new Data();

    private Dictionary<Type, Label> printLabels = new Hashtable<>();

    private static int CHAR_SIZE = 1, BOOL_SIZE = 1, INT_SIZE = 4, STRING_SIZE = 4;

    private Register r0, r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, sp, lr, pc;

    private int stackPointer = 1024;

    public CodeGenerator() {
        initialiseRegisters();
    }

    private void initialiseRegisters() {
        r0 = new Register(R0);
        r1 = new Register(R1);
        r2 = new Register(R2);
        r3 = new Register(R3);
        r4 = new Register(R4);
        r5 = new Register(R5);
        r6 = new Register(R6);
        r7 = new Register(R7);
        r8 = new Register(R8);
        r9 = new Register(R9);
        r10 = new Register(R10);
        r11 = new Register(R11);
        r12 = new Register(R12);
        sp = new Register(SP);
        lr = new Register(LR);
        pc = new Register(PC);
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

    private Label getPrintLabel(Type type) {
        return new Label("p_print_" + type);
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

        // Add messages to the front of the program
        Deque<Instruction> messages = data.getData();
        messages.addAll(instrs);
        instrs = messages;

        // Add print labels to end of the program
        instrs.addAll(printInstrs);

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

        instrs.add(new SingleDataTransferInstruction<>(LDR, r4, ctx.assignRhs()));

        //TO-DO: find the size of assign_rhs and decide how to calculate the value of stackPointer
        int offset = getStackSize(stackSpace) ;// - size_of_assignRhs

        if(offset == 0) {
            instrs.add(new SingleDataTransferInstruction<>(STR, r4, sp));
        } else {
            instrs.add(new SingleDataTransferInstruction<>(STR, r4,
                    new ShiftRegister(sp.getType(), offset, null)));
        }

        return null;
    }

    @Override
    public Identifier visitVarAssign(@NotNull VarAssignContext ctx) {

        // Should theoretically be the same as visitVarInit (not 100% sure yet)

        instrs.add(new SingleDataTransferInstruction<>(LDR, r4, ctx.assignRhs()));

        //TO-DO: find the size of assign_rhs and decide how to calculate the value of stackPointer
        int offset = getStackSize(stackSpace) ;// - size_of_assignRhs

        if(offset == 0) {
            instrs.add(new SingleDataTransferInstruction<>(STR, r4, sp));
        } else {
            instrs.add(new SingleDataTransferInstruction<>(STR, r4,
                    new ShiftRegister(sp.getType(), offset, null)));
        }

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
        visitExpr(ctx.expr());
        instrs.add(new DataProcessingInstruction<>(MOV, r0, r4));
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
        return visitPrint(ctx.expr(), false);
    }

    @Override
    public Identifier visitPrintlnStat(@NotNull PrintlnStatContext ctx) {
        return visitPrint(ctx.expr(), true);
    }

    private void visitPrintLn() {
        Label printLn = new Label("p_print_ln");
        instrs.add(new BranchInstruction(BL, printLn));

        // Check if println label exists
        if (printLabels.get(AllTypes.ANY) != null) {
            return;
        }

        Label newLineLabel = data.getMessageLocation(new Identifier(AllTypes.STRING, "\\0"));
        printInstrs.add(printLn);
        printInstrs.add(new StackInstruction(PUSH, lr));
        printInstrs.add(new SingleDataTransferInstruction<>(LDR, r0, newLineLabel));
        printInstrs.add(new DataProcessingInstruction<>(ADD, r0, r0, 4));
        printInstrs.add(new BranchInstruction(BL, new Label("puts")));
        printInstrs.add(new DataProcessingInstruction<>(MOV, r0, 0));
        printInstrs.add(new BranchInstruction(BL, new Label("fflush")));
        printInstrs.add(new StackInstruction(POP, pc));
        printLabels.put(AllTypes.ANY, printLn);
    }

    private Identifier visitPrint(@NotNull ExprContext ctx, boolean isPrintLn) {
        Identifier ident = visitExpr(ctx);
        Type type = (ident == null) ? AllTypes.INT : ident.getType();
        Label printLabel = getPrintLabel(type);

        instrs.add(new DataProcessingInstruction<>(MOV, r0, r4));

        if (type.equalsType(AllTypes.CHAR)) {
            instrs.add(new BranchInstruction(BL, new Label("putchar")));
        } else {
            instrs.add(new BranchInstruction(BL, printLabel));

            // Check if the print label already exists
            if (printLabels.get(type) != null) {
                if (isPrintLn) {
                    visitPrintLn();
                }
                return null;
            }

            printLabels.put(type, printLabel);
            printInstrs.add(printLabel);
            printInstrs.add(new StackInstruction(PUSH, lr));

            if (type.equalsType(AllTypes.INT)) {
                Label formatSpecifier = data.getFormatSpecifier(AllTypes.INT);
                printInstrs.add(new DataProcessingInstruction<>(MOV, r1, r0));
                printInstrs.add(new SingleDataTransferInstruction<>(LDR, r0, formatSpecifier));
            }

            if (type.equalsType(AllTypes.STRING)) {
                Label formatSpecifier = data.getFormatSpecifier(AllTypes.STRING);
                printInstrs.add(new SingleDataTransferInstruction<>(LDR, r1, r0));

                // Unsure about this instruction
                printInstrs.add(new DataProcessingInstruction<>(ADD, r2, r0, 4));

                printInstrs.add(new SingleDataTransferInstruction<>(LDR, r0, formatSpecifier));
            }

            if (type.equalsType(AllTypes.BOOL)) {
                printInstrs.add(new DataProcessingInstruction<>(CMP, r0, 0));
                Label trueLabel = data.getMessageLocation(new Identifier(AllTypes.BOOL, "true\\0"));
                printInstrs.add(new SingleDataTransferInstruction<>(LDRNE, r0, trueLabel));
                Label falseLabel = data.getMessageLocation(new Identifier(AllTypes.BOOL, "false\\0"));
                printInstrs.add(new SingleDataTransferInstruction<>(LDREQ, r0, falseLabel));
            }

            // Unsure about this instruction
            printInstrs.add(new DataProcessingInstruction<>(ADD, r0, r0, 4));

            printInstrs.add(new BranchInstruction(BL, new Label("printf")));
            printInstrs.add(new DataProcessingInstruction<>(MOV, r0, 0));
            printInstrs.add(new BranchInstruction(BL, new Label("fflush")));
            printInstrs.add(new StackInstruction(POP, pc));
        }

        if (isPrintLn) {
            visitPrintLn();
        }

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
        ExprContext arg = e.expr(0);

        //TO-DO: Find out what to return

        switch (ctx.getText()) {
            case "!":
                // Generate not boolean instructions
                if(arg.boolLiter() != null) {
                    instrs.add(new SingleDataTransferInstruction<>(LDRSB, r4, sp));
                    instrs.add(new DataProcessingInstruction<>(EOR, r4, r4, 1));
                    instrs.add(new DataProcessingInstruction<>(MOV, r0, r4));
                    return null;
                }
                return visitExpr(arg);
            case "-":
                // Generate negative integer instructions
                if (arg.intLiter() != null) {
//                    Integer value = Integer.parseInt(e.getText());
//                    instrs.add(new SingleDataTransferInstruction<>(LDR, r4, value));
//                    return new Identifier(AllTypes.INT, e.getText());

                    instrs.add(new SingleDataTransferInstruction<>(LDR, r4, sp));
                    instrs.add(new DataProcessingInstruction<>(RSBS, r4, r4, 0));
                    instrs.add(new BranchInstruction(BLVS, new Label("p_throw_overflow_error", null, false)));
                    instrs.add(new DataProcessingInstruction<>(MOV, r0, r4));
                    // return
                }
                return visitExpr(arg);
            case "len":
                // Generate len string instructions
                if (arg.strLiter() != null) {
                    instrs.add(new SingleDataTransferInstruction<>(LDR, r4, sp));
                    instrs.add(new SingleDataTransferInstruction<>(LDR, r4, r4));
                    instrs.add(new DataProcessingInstruction<>(MOV, r0, r4));
                    //return
                }
                return visitExpr(arg);
            case "ord":
                // Generate ord character instructions
                if(arg.charLiter() != null) {
                    instrs.add(new SingleDataTransferInstruction<>(LDRSB, r4, sp));
                    instrs.add(new DataProcessingInstruction<>(MOV, r0, r4));
                    // return
                }
                return visitExpr(arg);
            case "chr":
                // Generate chr integer instructions
                if(arg.intLiter() != null) {
                    instrs.add(new SingleDataTransferInstruction<>(LDR, r4, sp));
                    instrs.add(new DataProcessingInstruction<>(MOV, r0, r4));
                    // return
                }
                return visitExpr(arg);
        }
        return null;
    }

    @Override
    public Identifier visitBoolBinaryOper(@NotNull BoolBinaryOperContext ctx) {
        ExprContext e = (ExprContext) ctx.getParent();
        ExprContext arg1 = e.expr(0);
        ExprContext arg2 = e.expr(1);

        switch (ctx.getText()) {
            case "||":
                //WHAT DOES THE METHOD boolLiter DO? 'cause I'm not sure, and I think I need it
                if(arg1.boolLiter() != null && arg2.boolLiter() != null) {
                    instrs.add(new DataProcessingInstruction<>(ORR, r4, r4, r5));
                    instrs.add(new DataProcessingInstruction<>(MOV, r0, r4));
                }

                //for a || b
//                LDRSB r4, [sp, #1]
//                LDRSB r5, [sp]
//                ORR r4, r4, r5
//                MOV r0, r4

                //for a || true
//                LDRSB r4, [sp, #1]
//                MOV r5, #1
//                ORR r4, r4, r5
//                MOV r0, r4

                //for b || false
//                LDRSB r4, [sp]
//                MOV r5, #0
//                ORR r4, r4, r5
//                MOV r0, r4


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
        data.getMessageLocation(new Identifier(AllTypes.INT, ctx.getText()));
        int intLiter = Integer.parseInt(ctx.getText());
        instrs.add(new SingleDataTransferInstruction<>(LDR, r4, intLiter));
        return new Identifier(AllTypes.INT, "" + intLiter);
    }

    @Override
    public Identifier visitBoolLiter(@NotNull BoolLiterContext ctx) {
        String text = ctx.getText();
        int boolLiter = text.equals("true") ? 1 : 0;
        instrs.add(new DataProcessingInstruction<>(MOV, r4, boolLiter));
        return new Identifier(AllTypes.BOOL, text);
    }

    @Override
    public Identifier visitCharLiter(@NotNull CharLiterContext ctx) {
        String text = ctx.getText();
        char charLiter = text.charAt(1);
        instrs.add(new DataProcessingInstruction<>(MOV, r4, charLiter));
        return new Identifier(AllTypes.CHAR, text);
    }

    @Override
    public Identifier visitStrLiter(@NotNull StrLiterContext ctx) {
        String value = ctx.getText().substring(1, ctx.getText().length() - 1);
        Identifier ident = new Identifier(AllTypes.STRING, value);
        Label msg = data.getMessageLocation(ident);
        instrs.add(new SingleDataTransferInstruction<>(LDR, r4, msg));
        return ident;
    }

    @Override
    public Identifier visitArrayLiter(@NotNull ArrayLiterContext ctx) {
        int length = 0;
        for (ExprContext e : ctx.expr()) {
            // TODO: Store array elements in symbol table and total heap space
        }
        // Storing length of array
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
