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
import static backEnd.instructions.MultiplyInstructionType.*;
import static backEnd.instructions.ShiftType.*;
import static backEnd.instructions.SingleDataTransferType.*;
import static backEnd.instructions.StackType.*;
import static frontEnd.AllTypes.*;

public class CodeGenerator extends WaccParserBaseVisitor<Type> {

    private StackVisitor stackVisitor = new StackVisitor();
    private Dictionary<String, Integer> stackSpace = new Hashtable<>();
    private int stackSize = 0, stackPos = 0, currVarPos = 0;

    private Deque<Instruction> instrs = new ArrayDeque<>();
    private Deque<Instruction> printInstrs = new ArrayDeque<>();
    private int labelIndex = 0;

    private SymbolTable<Type> head = new SymbolTable<>();
    private SymbolTable<Type> curr = head;

    private Data data = new Data();

    private Dictionary<Type, Label> printLabels = new Hashtable<>();

    private static int MAX_STACK_OFFSET = 1024;
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

    private void addSubStackInstrs(int size) {
        if (size > 0) {
            int spaceReserved = (size < MAX_STACK_OFFSET) ? size : MAX_STACK_OFFSET;
            instrs.add(new DataProcessingInstruction<>(SUB, sp, sp, spaceReserved));
            addSubStackInstrs(size - MAX_STACK_OFFSET);
        }
    }

    private void addAddStackInstrs(int size) {
        if (size > 0) {
            int spaceReserved = (size < MAX_STACK_OFFSET) ? size : MAX_STACK_OFFSET;
            addAddStackInstrs(size - MAX_STACK_OFFSET);
            instrs.add(new DataProcessingInstruction<>(ADD, sp, sp, spaceReserved));
        }
    }

    private void generateInstrs(ParserRuleContext ctx) {
        // Store instructions in scope in an empty deque
        Deque<Instruction> old = instrs;
        instrs = new ArrayDeque<>();


        visit(ctx);

        // Add the instructions and change the pointer to the original set of instructions
        old.addAll(instrs);
        instrs = old;
    }

    //------------------------------VISIT METHODS----------------------------//

    @Override
    public Type visitProg(@NotNull ProgContext ctx) {
        // .data (only created when we have a string literal or read/print(ln) statements)
        // (need to generate string literals here)

        instrs.add(new Directive("text"));
        instrs.add(new Directive("global main"));

        for (FuncDeclContext func : ctx.funcDecl()) {
            visitFuncDecl(func);
        }

        instrs.add(new Label("main"));
        instrs.add(new StackInstruction(PUSH, lr));

        // Calculates amount of space required for the global scope
        stackSize = stackVisitor.visit(ctx);

        // Add the instructions needed for storing all the local variables in the scope
        addSubStackInstrs(stackSize);

        visit(ctx.stat());

        addAddStackInstrs(stackSize);

        instrs.add(new SingleDataTransferInstruction<>(LDR, r0, 0));
        instrs.add(new StackInstruction(POP, pc));
        instrs.add(new Directive("ltorg"));

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
    public Type visitFuncDecl(@NotNull FuncDeclContext ctx) {
        // Need to change to function symbol table when visiting the function
        String funName = ctx.ident().getText();
        visitFunction(new Label(funName, null, true), ctx.stat());
        return null;
    }

    @Override
    public Type visitParamList(@NotNull ParamListContext ctx) {
        return null;
    }

    @Override
    public Type visitParam(@NotNull ParamContext ctx) {
        return null;
    }

    @Override
    public Type visitSkip(@NotNull SkipContext ctx) {
        // Nothing to do for skip.
        return null;
    }

    @Override
    public Type visitVarInit(@NotNull VarInitContext ctx) {

        //instrs.add(new SingleDataTransferInstruction<>(LDR, r4, ctx.assignRhs()));
        Type lhs = visitType(ctx.type());

        if (lhs.equalsType(INT)) {
            stackPos += INT_SIZE;
        }
        if (lhs.equalsType(BOOL)) {
            stackPos += BOOL_SIZE;
        }
        if (lhs.equalsType(CHAR)) {
            stackPos += CHAR_SIZE;
        }
        if (lhs.equalsType(STRING)) {
            stackPos += STRING_SIZE;
        }

        visitAssignRhs(ctx.assignRhs());

        //TO-DO: find the size of assign_rhs and decide how to calculate the value of stackPointer
        int offset = stackSize - stackPos;// - size_of_assignRhs
        String varName = ctx.ident().getText();
        stackSpace.put(varName, offset);
        curr.add(varName, lhs);

        SingleDataTransferType storeType = STR;

        if (lhs.equalsType(BOOL) || lhs.equalsType(CHAR)) {
            storeType = STRB;
        }

        if(offset == 0) {
            instrs.add(new SingleDataTransferInstruction<>(storeType, r4, sp));
        } else {
            instrs.add(new SingleDataTransferInstruction<>(storeType, r4,
                    new ShiftRegister(SP, offset, null)));
        }

        return null;
    }

    @Override
    public Type visitVarAssign(@NotNull VarAssignContext ctx) {


        Type lhs = visitAssignLhs(ctx.assignLhs());
        // Should theoretically be the same as visitVarInit (not 100% sure yet)
        visitAssignRhs(ctx.assignRhs());

//        instrs.add(new SingleDataTransferInstruction<>(LDR, r4, ctx.assignRhs()));

        //TO-DO: find the size of assign_rhs and decide how to calculate the value of stackPointer

        // Note: Will NOT work for arrays/pairs yet
        String id = ctx.assignLhs().getText();
        int offset = stackSpace.get(id);// - size_of_assignRhs

        SingleDataTransferType storeType = STR;

        if (lhs.equalsType(BOOL) || lhs.equalsType(CHAR)) {
            storeType = STRB;
        }

        if (offset == 0) {
            instrs.add(new SingleDataTransferInstruction<>(storeType, r4, sp));
        } else {
            instrs.add(new SingleDataTransferInstruction<>(storeType, r4,
                    new ShiftRegister(sp.getType(), offset, null)));
        }

        return null;
    }

    @Override
    public Type visitReadStat(@NotNull ReadStatContext ctx) {
        // Need to branch to a "BL read" statement, depending on the type of ctx.assignLhs()
        Type type = visitAssignLhs(ctx.assignLhs());

        Label readLabel = new Label("p_read_" + type);
        // TODO: Check this is valid
        instrs.add(new DataProcessingInstruction<>(ADD, r4, sp, currVarPos));
        instrs.add(new DataProcessingInstruction<>(MOV, r0, r4));
        instrs.add(new BranchInstruction(BL, readLabel));

        // Add the format specifier to data
        data.addFormatSpecifier(type);
        Label formatSpecifier = data.getFormatSpecifier(type);

        // Add read label instructions
        printInstrs.add(readLabel);
        printInstrs.add(new StackInstruction(PUSH, lr));
        printInstrs.add(new DataProcessingInstruction<>(MOV, r1, r0));
        printInstrs.add(new SingleDataTransferInstruction<>(LDR, r0, formatSpecifier));
        printInstrs.add(new DataProcessingInstruction<>(ADD, r0, r0, 4));
        printInstrs.add(new BranchInstruction(BL, new Label("scanf")));
        printInstrs.add(new StackInstruction(POP, pc));

        return null;
    }

    @Override
    public Type visitFreeStat(@NotNull FreeStatContext ctx) {
        return null;
    }

    @Override
    public Type visitReturnStat(@NotNull ReturnStatContext ctx) {
        visitExpr(ctx.expr());
        instrs.add(new DataProcessingInstruction<>(MOV, r0, r4));
        return null;
    }

    @Override
    public Type visitExitStat(@NotNull ExitStatContext ctx) {
        // LDR r4, =(int_liter)
        // MOV r0, r4
        // BL exit

        // This works because we know that the expression is an int literal

        visitExpr(ctx.expr());
        instrs.add(new DataProcessingInstruction<>(MOV, r0, r4));
        instrs.add(new BranchInstruction(BL, new Label("exit")));
        return null;
    }

    @Override
    public Type visitPrintStat(@NotNull PrintStatContext ctx) {
        return visitPrint(ctx.expr(), false);
    }

    @Override
    public Type visitPrintlnStat(@NotNull PrintlnStatContext ctx) {
        return visitPrint(ctx.expr(), true);
    }

    private void visitPrintLn() {
        Label printLn = new Label("p_print_ln");
        instrs.add(new BranchInstruction(BL, printLn));

        // Check if println label exists
        if (printLabels.get(ANY) != null) {
            return;
        }

        Label newLineLabel = data.getMessageLocation(new Identifier(STRING, "\\0"));
        printInstrs.add(printLn);
        printInstrs.add(new StackInstruction(PUSH, lr));
        printInstrs.add(new SingleDataTransferInstruction<>(LDR, r0, newLineLabel));
        printInstrs.add(new DataProcessingInstruction<>(ADD, r0, r0, 4));
        printInstrs.add(new BranchInstruction(BL, new Label("puts")));
        printInstrs.add(new DataProcessingInstruction<>(MOV, r0, 0));
        printInstrs.add(new BranchInstruction(BL, new Label("fflush")));
        printInstrs.add(new StackInstruction(POP, pc));
        printLabels.put(ANY, printLn);
    }

    private Type visitPrint(@NotNull ExprContext ctx, boolean isPrintLn) {
        Type type = visitExpr(ctx);
        Label printLabel = getPrintLabel(type);

        instrs.add(new DataProcessingInstruction<>(MOV, r0, r4));

        if (type.equalsType(CHAR)) {
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

            if (type.equalsType(INT)) {
                Label formatSpecifier = data.getFormatSpecifier(INT);
                printInstrs.add(new DataProcessingInstruction<>(MOV, r1, r0));
                printInstrs.add(new SingleDataTransferInstruction<>(LDR, r0, formatSpecifier));
            }

            if (type.equalsType(STRING)) {
                Label formatSpecifier = data.getFormatSpecifier(STRING);
                printInstrs.add(new SingleDataTransferInstruction<>(LDR, r1, r0));

                // Unsure about this instruction
                printInstrs.add(new DataProcessingInstruction<>(ADD, r2, r0, 4));

                printInstrs.add(new SingleDataTransferInstruction<>(LDR, r0, formatSpecifier));
            }

            if (type.equalsType(BOOL)) {
                printInstrs.add(new DataProcessingInstruction<>(CMP, r0, 0));
                Label trueLabel = data.getMessageLocation(new Identifier(BOOL, "true\\0"));
                printInstrs.add(new SingleDataTransferInstruction<>(LDRNE, r0, trueLabel));
                Label falseLabel = data.getMessageLocation(new Identifier(BOOL, "false\\0"));
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
    public Type visitIfStat(@NotNull IfStatContext ctx) {
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
    public Type visitWhileStat(@NotNull WhileStatContext ctx) {
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
    public Type visitBeginEnd(@NotNull BeginEndContext ctx) {
        // Create new scopes for current symbol table and stack space
//        List<Integer> old = stackSpace;
//        stackSpace = new ArrayList<>();
        curr = new SymbolTable<>(curr);
        Dictionary<String, Integer> oldStack = stackSpace;
        stackSpace = new Hashtable<>();
        int oldSize = stackSize;

        stackSize = stackVisitor.visit(ctx.stat());

        addSubStackInstrs(stackSize);

        visit(ctx.stat());

        addAddStackInstrs(stackSize);

        stackSpace = oldStack;
        stackSize = oldSize;
        curr = curr.encSymbolTable;

//        generateInstrs(ctx);

        // Exit the current scope for stack space and current symbol table
//        stackSpace = old;
        return null;
    }

    @Override
    public Type visitStatSequence(@NotNull StatSequenceContext ctx) {
        visitChildren(ctx);
        return null;
    }

    @Override
    public Type visitAssignLhs(@NotNull AssignLhsContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public Type visitAssignRhs(@NotNull AssignRhsContext ctx) {
        // visitChildren(ctx);
        return visitChildren(ctx);
    }

    @Override
    public Type visitNewPair(@NotNull NewPairContext ctx) {
        return null;
    }

    @Override
    public Type visitCallFunc(@NotNull CallFuncContext ctx) {
        return null;
    }

    @Override
    public Type visitArgList(@NotNull ArgListContext ctx) {
        return null;
    }

    @Override
    public Type visitPairElem(@NotNull PairElemContext ctx) {
        return null;
    }

    @Override
    public Type visitType(@NotNull TypeContext ctx) {
        if (ctx.type() != null) {
            // Need to deal with array type somehow.
            return null;
        }
        return visitChildren(ctx);
    }

    @Override
    public Type visitBaseType(@NotNull BaseTypeContext ctx) {
        switch (ctx.getText()) {
            case "int":
                return INT;
            case "bool":
                return BOOL;
            case "char":
                return CHAR;
            case "string":
                return STRING;
        }
        return null;
    }

    @Override
    public Type visitArrayType(@NotNull ArrayTypeContext ctx) {
        return null;
    }

    @Override
    public Type visitPairType(@NotNull PairTypeContext ctx) {
        return null;
    }

    @Override
    public Type visitPairElemType(@NotNull PairElemTypeContext ctx) {
        return null;
    }

    @Override
    public Type visitExpr(@NotNull ExprContext ctx) {
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
    public Type visitBracketExpr(@NotNull BracketExprContext ctx) {
        // Need to have more priority on bracketed expressions.
        visitExpr(ctx.expr());
        return null;
    }

    @Override
    public Type visitUnaryOper(@NotNull UnaryOperContext ctx) {
        ExprContext e = (ExprContext) ctx.getParent();
        ExprContext arg = e.expr(0);

        visitExpr(arg);

        switch (ctx.getText()) {
            case "!":
                instrs.add(new DataProcessingInstruction<>(EOR, r4, r4, 1));
                return BOOL;
            case "-":
                instrs.add(new DataProcessingInstruction<>(RSBS, r4, r4, 0));
            case "len":
//                // Generate len string instructions
//                if (arg.strLiter() != null) {
//                    instrs.add(new SingleDataTransferInstruction<>(LDR, r4, sp));
//                    instrs.add(new SingleDataTransferInstruction<>(LDR, r4, r4));
//                    instrs.add(new DataProcessingInstruction<>(MOV, r0, r4));
//                    return INT;
//                }
//                return visitExpr(arg);
                break;
            case "ord":
                return INT;
            case "chr":
                return CHAR;
        }
        return null;
    }

    @Override
    public Type visitBoolBinaryOper(@NotNull BoolBinaryOperContext ctx) {
        ExprContext e = (ExprContext) ctx.getParent();
        ExprContext arg1 = e.expr(0);
        ExprContext arg2 = e.expr(1);

        visitExpr(arg2);
        instrs.add(new DataProcessingInstruction<>(MOV, r5, r4));
        visitExpr(arg1);

        switch (ctx.getText()) {
            case "||":
                instrs.add(new DataProcessingInstruction<>(ORR, r4, r4, r5));
                break;
            case "&&":
                instrs.add(new DataProcessingInstruction<>(AND, r4, r4, r5));
                break;
        }

        return BOOL;
    }

    @Override
    public Type visitBinaryOper(@NotNull BinaryOperContext ctx) {
        ExprContext e = (ExprContext) ctx.getParent();
        ExprContext arg1 = e.expr(0);
        ExprContext arg2 = e.expr(1);

        visitExpr(arg2);
        instrs.add(new DataProcessingInstruction<>(MOV, r5, r4));
        visitExpr(arg1);

        switch (ctx.getText()) {
            case "*":
                instrs.add(new MultiplyInstruction(SMULL, r4, r5, r4, r5));
                instrs.add(new DataProcessingInstruction<>(CMP, r5, r4, new ShiftInstruction(ASR, 31)));
                return INT;
            case "/":
                instrs.add(new DataProcessingInstruction<>(MOV, r0, r4));
                instrs.add(new DataProcessingInstruction<>(MOV, r1, r5));
                instrs.add(new BranchInstruction(BL, new Label("__aeabi_idivmod")));
                instrs.add(new DataProcessingInstruction<>(MOV, r4, r0));
                instrs.add(new DataProcessingInstruction<>(MOV, r0, r4));
                return INT;
            case "%":
                instrs.add(new DataProcessingInstruction<>(MOV, r0, r4));
                instrs.add(new DataProcessingInstruction<>(MOV, r1, r5));
                instrs.add(new BranchInstruction(BL, new Label("__aeabi_idivmod")));
                instrs.add(new DataProcessingInstruction<>(MOV, r4, r1));
                instrs.add(new DataProcessingInstruction<>(MOV, r0, r4));
                return INT;
            case "+":
                instrs.add(new DataProcessingInstruction<>(ADDS, r4, r4, r5));
                return INT;
            case "-":
                instrs.add(new DataProcessingInstruction<>(SUBS, r4, r4, r5));
                return INT;
            case ">":
                instrs.add(new DataProcessingInstruction<>(CMP, r4, r5));
                instrs.add(new DataProcessingInstruction<>(MOVGT, r4, 1));
                instrs.add(new DataProcessingInstruction<>(MOVLE, r4, 0));
                return BOOL;
            case ">=":
                instrs.add(new DataProcessingInstruction<>(CMP, r4, r5));
                instrs.add(new DataProcessingInstruction<>(MOVGE, r4, 1));
                instrs.add(new DataProcessingInstruction<>(MOVLT, r4, 0));
                return BOOL;
            case "<":
                instrs.add(new DataProcessingInstruction<>(CMP, r4, r5));
                instrs.add(new DataProcessingInstruction<>(MOVLT, r4, 1));
                instrs.add(new DataProcessingInstruction<>(MOVGE, r4, 0));
                return BOOL;
            case "<=":
                instrs.add(new DataProcessingInstruction<>(CMP, r4, r5));
                instrs.add(new DataProcessingInstruction<>(MOVLE, r4, 1));
                instrs.add(new DataProcessingInstruction<>(MOVGT, r4, 0));
                return BOOL;
            case "==":
                instrs.add(new DataProcessingInstruction<>(CMP, r4, r5));
                instrs.add(new DataProcessingInstruction<>(MOVEQ, r4, 1));
                instrs.add(new DataProcessingInstruction<>(MOVNE, r4, 0));
                return BOOL;
            case "!=":
                instrs.add(new DataProcessingInstruction<>(CMP, r4, r5));
                instrs.add(new DataProcessingInstruction<>(MOVNE, r4, 1));
                instrs.add(new DataProcessingInstruction<>(MOVEQ, r4, 0));
                return BOOL;
        }
        return null;
    }

    @Override
    public Type visitIdent(@NotNull IdentContext ctx) {
        // Need to check this
        //return curr.lookUpAll(ctx.getText());
        currVarPos = stackSpace.get(ctx.getText());
        Type type = curr.lookUpAll(ctx.getText());
        SingleDataTransferType loadRegType = LDR;
        if (type.equalsType(CHAR) || type.equalsType(BOOL)) {
            loadRegType = LDRSB;
        }
        instrs.add(new SingleDataTransferInstruction<>(loadRegType, r4,
                new ShiftRegister(SP, currVarPos, null)));
        return type;
    }

    @Override
    public Type visitArrayElem(@NotNull ArrayElemContext ctx) {
        return null;
    }

    @Override
    public Type visitIntLiter(@NotNull IntLiterContext ctx) {
        data.getMessageLocation(new Identifier(INT, ctx.getText()));
        int intLiter = Integer.parseInt(ctx.getText());
        instrs.add(new SingleDataTransferInstruction<>(LDR, r4, intLiter));
        return INT;
    }

    @Override
    public Type visitBoolLiter(@NotNull BoolLiterContext ctx) {
        String text = ctx.getText();
        int boolLiter = text.equals("true") ? 1 : 0;
        instrs.add(new DataProcessingInstruction<>(MOV, r4, boolLiter));
        return BOOL;
    }

    @Override
    public Type visitCharLiter(@NotNull CharLiterContext ctx) {
        String text = ctx.getText();
        char charLiter = text.charAt(1);
        if (charLiter == '\\') {
            charLiter = text.charAt(2);
        }
        instrs.add(new DataProcessingInstruction<>(MOV, r4, charLiter));
        return CHAR;
    }

    @Override
    public Type visitStrLiter(@NotNull StrLiterContext ctx) {
        String value = ctx.getText().substring(1, ctx.getText().length() - 1);
        Identifier ident = new Identifier(STRING, value);
        Label msg = data.getMessageLocation(ident);
        instrs.add(new SingleDataTransferInstruction<>(LDR, r4, msg));
        return STRING;
    }

    @Override
    public Type visitArrayLiter(@NotNull ArrayLiterContext ctx) {
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
    public Type visitPairLiter(@NotNull PairLiterContext ctx) {
        return null;
    }

    @Override
    public Type visitChildren(@NotNull RuleNode ruleNode) {
        Type result = null;
        int n = ruleNode.getChildCount();
        for (int i = 0; i < n; i++) {
            ParseTree c = ruleNode.getChild(i);
            Type childResult = c.accept(this);
            if (childResult != null) {
                result = childResult;
            }
        }
        return result;
    }

}
