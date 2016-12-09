package backEnd;

import antlr.WaccParser.*;
import antlr.WaccParserBaseVisitor;
import backEnd.instructions.*;
import frontEnd.*;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import utils.Identifier;
import utils.SymbolTable;

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
    private SymbolTable<Integer> stackSpace = new SymbolTable<>();
    private SymbolTable<Dictionary<String, Integer>> heapSpace = new SymbolTable<>();

    private Register heapPtr = null;

    private int heapPos = 0;
    private SymbolTable<Integer> arrayPositions = new SymbolTable<>();

    private int stackSize = 0, stackPos = 0, currVarPos = 0;
    private int funcSize = 0;

    private Stack<Register> freeRegisters = new Stack<>();

    private Deque<Instruction> instrs = new ArrayDeque<>();
    private int labelIndex = 0;

    private SymbolTable<Type> head = new SymbolTable<>();
    private SymbolTable<Type> curr = head;

    private Data data = new Data();
    private ExtraMethodGenerator methodGenerator = new ExtraMethodGenerator(data);

    private Label condLabel, exitLabel;

    private static int MAX_STACK_OFFSET = 1024, ARRAY_SIZE = 4, PAIR_SIZE = 4, NEWPAIR_SIZE = 8;
    private static int CHAR_SIZE = 1, BOOL_SIZE = 1, INT_SIZE = 4, STRING_SIZE = 4;

    private Register r0;
    private Register r1;
    private Register r2;
    private Register r3;
    private Register r4;
    private Register r5;
    private Register r6;
    private Register r7;
    private Register r8;
    private Register r9;
    private Register r10;
    private Register r11;
    private Register r12;
    private Register sp;
    private Register lr;
    private Register pc;

    public CodeGenerator() {
        initialiseRegisters();
        initialiseFreeRegisters();
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

    private void initialiseFreeRegisters() {
        freeRegisters.push(r12);
        freeRegisters.push(r11);
        freeRegisters.push(r10);
        freeRegisters.push(r9);
        freeRegisters.push(r8);
        freeRegisters.push(r7);
        freeRegisters.push(r6);
        freeRegisters.push(r5);
        freeRegisters.push(r4);
    }

    //-------------------------UTILITY FUNCTIONS-----------------------------//


    private Label getNonFunctionLabel() {
        return new Label("L", labelIndex++, false);
    }

    private Label getPrintLabel(Type type) {
        String label = "" + type;
        if (type.equalsType(NULL)) {
            label = "reference";
        }
        return new Label("p_print_" + label);
    }

    private void visitFunction(Label label, ParserRuleContext ctx) {
        instrs.add(label);
        instrs.add(new StackInstruction(PUSH, lr));

        // Store instructions in scope in an empty deque
        Deque<Instruction> old = instrs;
        instrs = new ArrayDeque<>();

        int oldStack = stackSize;
        int localStack = stackVisitor.visit(ctx);
        stackSize = localStack + stackPos;

        // Add the instructions needed for storing all the local variables in the scope
        addSubStackInstrs(localStack);

        visit(ctx);

        addAddStackInstrs(localStack);

        stackSize = oldStack;

        // Add the instructions and change the pointer to the original set of instructions
        old.addAll(instrs);
        instrs = old;

        instrs.add(new StackInstruction(POP, pc));
        instrs.add(new Directive("ltorg"));
    }

    private void visitNewScope(StatContext stat) {
        // Create new scopes for current symbol table and stack space
        curr = curr.startNewScope();
        stackSpace = stackSpace.startNewScope();
        arrayPositions = arrayPositions.startNewScope();

        // Keep reference to position of the stack pointer
        int oldStackPos = stackPos;

        // Extend the stack with the size of the new scope
        Integer scopeSize = stackVisitor.visit(stat);
        if (scopeSize == null) {
            scopeSize = 0;
        }
        if (stackPos != stackSize) {
            stackPos = stackSize;
        }
        stackSize += scopeSize;
        addSubStackInstrs(scopeSize);

        // Visit the statements in the new scope
        visit(stat);

        // Reset the stack and symbol tables to the original states
        addAddStackInstrs(scopeSize);
        stackSize -= scopeSize;
        stackPos = oldStackPos;

        arrayPositions = arrayPositions.endCurrentScope();
        stackSpace = stackSpace.endCurrentScope();
        curr = curr.endCurrentScope();
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

    private void storeResult(int offset, SingleDataTransferType storeType, Register src, Register dst) {
        if (offset == 0) {
            instrs.add(new SingleDataTransferInstruction<>(storeType, src, dst));
        } else {
            instrs.add(new SingleDataTransferInstruction<>(storeType, src,
                    new ShiftRegister(dst.getType(), offset, null)));
        }
    }

    private int getExprSize(Type type) {
        if (type.equalsType(INT)) {
            return INT_SIZE;
        }
        if (type.equalsType(BOOL)) {
            return BOOL_SIZE;
        }
        if (type.equalsType(CHAR)) {
            return CHAR_SIZE;
        }
        if (type.equalsType(STRING)) {
            return STRING_SIZE;
        }
        if (type instanceof ArrayType) {
            return ARRAY_SIZE;
        }
        if (type instanceof PairType) {
            return PAIR_SIZE;
        }
        return 0;
    }

    private Type getArrayElemType(Type lhs) {
        if (lhs instanceof ArrayType) {
            lhs = ((ArrayType) lhs).getElement();
        }
        if (lhs.equalsType(STRING)) {
            lhs = CHAR;
        }
        return lhs;
    }

    private void resetHeapPointer() {
        freeRegisters.push(heapPtr);
        heapPtr = null;
    }

    private void visitPrint(Type type) {
        if (type instanceof ArrayType || type instanceof PairType) {
            type = NULL;
        }
        Label printLabel = getPrintLabel(type);

        instrs.add(new DataProcessingInstruction<>(MOV, r0, freeRegisters.peek()));

        if (type.equalsType(CHAR)) {
            instrs.add(new BranchInstruction(BL, new Label("putchar")));
        } else {
            instrs.add(new BranchInstruction(BL, printLabel));
            methodGenerator.generatePrint(type, printLabel);
        }
    }

    private List<StatContext> initialiseStatList(StatContext... stats) {
        return Arrays.asList(stats);
    }

    private Type visitWhile(ExprContext expr, List<StatContext> stats, boolean isDoWhile) {
        Label loopLabel = getNonFunctionLabel();
        Label oldCond = condLabel;
        Label oldExit = exitLabel;
        condLabel = getNonFunctionLabel();
        exitLabel = getNonFunctionLabel();
        if (!isDoWhile) {
            instrs.add(new BranchInstruction(B, condLabel));
        }
        instrs.add(loopLabel);
        for (StatContext stat : stats) {
            visitNewScope(stat);
        }
        instrs.add(condLabel);
        visitExpr(expr);
        instrs.add(new DataProcessingInstruction<>(CMP, freeRegisters.peek(), 1));
        instrs.add(new BranchInstruction(BEQ, loopLabel));
        instrs.add(exitLabel);
        condLabel = oldCond;
        exitLabel = oldExit;
        return null;
    }

    //------------------------------VISIT METHODS----------------------------//

    @Override
    public Type visitProg(@NotNull ProgContext ctx) {
        // Add starting directives for program
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
        Deque<Instruction> extraMethods = methodGenerator.getExtraMethods();
        instrs.addAll(extraMethods);

        // Print instructions to standard output
        for (Instruction instr : instrs) {
            System.out.println(instr);
        }

        return null;
    }

    @Override
    public Type visitFuncDecl(@NotNull FuncDeclContext ctx) {
        String funName = ctx.ident().getText();

        stackPos = stackVisitor.visit(ctx.stat()) + 4;

        if (ctx.paramList() != null) {
            visitParamList(ctx.paramList());
        }
        visitFunction(new Label(funName, null, true), ctx.stat());
        stackPos = 0;
        return null;
    }

    @Override
    public Type visitParamList(@NotNull ParamListContext ctx) {
        for (ParamContext param : ctx.param()) {
            visitParam(param);
        }
        return null;
    }

    @Override
    public Type visitParam(@NotNull ParamContext ctx) {
        Type type = visitType(ctx.type());
        int size = STRING_SIZE;
        if (type.equalsType(CHAR) || type.equalsType(BOOL)) {
            size = CHAR_SIZE;
        }
        String varName = ctx.ident().getText();
        stackPos += size;
        stackSpace.add(varName, stackPos - size);
        curr.add(varName, type);
        return null;
    }

    @Override
    public Type visitSkip(@NotNull SkipContext ctx) {
        // Nothing to do for skip.
        return null;
    }

    @Override
    public Type visitVarInit(@NotNull VarInitContext ctx) {
        Type lhs = visitType(ctx.type());

        int size = getExprSize(lhs);
        stackPos += size;

        visitAssignRhs(ctx.assignRhs());
        Register result = freeRegisters.pop();

        int offset = stackSize - stackPos;
        String varName = ctx.ident().getText();
        stackSpace.add(varName, stackPos);
        curr.add(varName, lhs);

        if (lhs instanceof PairType) {
            Dictionary<String, Integer> pairElems = new Hashtable<>();
            heapPos = PAIR_SIZE;
            String fst = varName + ".fst";
            curr.add(fst, ((PairType) lhs).getLeft());
            pairElems.put(fst, heapPos - PAIR_SIZE);
            String snd = varName + ".snd";
            curr.add(snd, ((PairType) lhs).getRight());
            pairElems.put(snd, heapPos);
            heapSpace.add(varName, pairElems);
        }

        if (lhs instanceof ArrayType || lhs.equalsType(STRING)) {
            arrayPositions.add(varName, stackPos /*- ARRAY_SIZE*/);
        }

        SingleDataTransferType storeType = STR;

        if (lhs.equalsType(BOOL) || lhs.equalsType(CHAR)) {
            storeType = STRB;
        }

        storeResult(offset, storeType, result, sp);

        freeRegisters.push(result);

        if (ctx.assignRhs().pairElem() != null) {
            resetHeapPointer();
        }

        return null;
    }

    @Override
    public Type visitVarAssign(@NotNull VarAssignContext ctx) {

        visitAssignRhs(ctx.assignRhs());
        Register result = freeRegisters.pop();

        Type lhs;
        int offset;
        Register dst;
        PairElemContext pairElemLhs = ctx.assignLhs().pairElem();
        ArrayElemContext arrayElemLhs = ctx.assignLhs().arrayElem();

        if (pairElemLhs != null) {
            String pairName = pairElemLhs.expr().getText();
            String elem = (pairElemLhs.FIRST() != null) ? "fst" : "snd";
            String pairElem = pairName + "." + elem;
            lhs = curr.lookUpAll(pairElem);
            offset = 0;
            visitPairElem(pairElemLhs);
            dst = freeRegisters.peek();
            instrs.removeLast();
        } else if (arrayElemLhs != null) {
            lhs = curr.lookUpAll(arrayElemLhs.ident().getText());
            lhs = getArrayElemType(lhs);
            offset = 0;
            visitArrayElem(arrayElemLhs);
            dst = freeRegisters.peek();
            instrs.removeLast();
        } else {
            String ident = ctx.assignLhs().getText();
            lhs = curr.lookUpAll(ident);
            offset = stackSize - stackSpace.lookUpAll(ident);
            dst = sp;
        }

        SingleDataTransferType storeType = STR;

        if (lhs.equalsType(BOOL) || lhs.equalsType(CHAR)) {
            storeType = STRB;
        }

        storeResult(offset, storeType, result, dst);

        if (heapPtr != null) {
            resetHeapPointer();
        }

        freeRegisters.push(result);

        return null;

    }

    @Override
    public Type visitReadStat(@NotNull ReadStatContext ctx) {
        Type type = visitAssignLhs(ctx.assignLhs());

        Label readLabel = new Label("p_read_" + type);
        Register expr = freeRegisters.peek();
        instrs.add(new DataProcessingInstruction<>(ADD, expr, sp, currVarPos));
        instrs.add(new DataProcessingInstruction<>(MOV, r0, expr));
        instrs.add(new BranchInstruction(BL, readLabel));

        // Add the format specifier to data
        data.addFormatSpecifier(type);
        Label formatSpecifier = data.getFormatSpecifier(type);

        // Add read label instructions
        methodGenerator.generateRead(readLabel, formatSpecifier);

        return null;
    }

    @Override
    public Type visitFreeStat(@NotNull FreeStatContext ctx) {
        visitExpr(ctx.expr());
        instrs.add(new DataProcessingInstruction<>(MOV, r0, freeRegisters.peek()));
        Label freePair = new Label("p_free_pair");
        instrs.add(new BranchInstruction(BL, freePair));

        Identifier ident = new Identifier(STRING, "NullReferenceError: dereference a null reference\\n\\0");
        Label errorMsg = data.getMessageLocation(ident);

        methodGenerator.freePair(freePair, errorMsg);

        return null;
    }

    @Override
    public Type visitReturnStat(@NotNull ReturnStatContext ctx) {
        visitExpr(ctx.expr());
        instrs.add(new DataProcessingInstruction<>(MOV, r0, freeRegisters.peek()));
        return null;
    }

    @Override
    public Type visitExitStat(@NotNull ExitStatContext ctx) {
        visitExpr(ctx.expr());
        instrs.add(new DataProcessingInstruction<>(MOV, r0, freeRegisters.peek()));
        instrs.add(new BranchInstruction(BL, new Label("exit")));
        return null;
    }

    @Override
    public Type visitPrintStat(@NotNull PrintStatContext ctx) {
        visitPrint(visitExpr(ctx.expr()));
        return null;
    }

    @Override
    public Type visitPrintlnStat(@NotNull PrintlnStatContext ctx) {
        visitPrint(visitExpr(ctx.expr()));
        Label printLn = new Label("p_print_ln");
        instrs.add(new BranchInstruction(BL, printLn));
        Label newLineLabel = data.getMessageLocation(new Identifier(STRING, "\\0"));
        methodGenerator.generatePrintLn(printLn, newLineLabel);
        return null;
    }

    @Override
    public Type visitIfStat(@NotNull IfStatContext ctx) {
        boolean hasElse = ctx.ELSE() != null;
        Label fiLabel = getNonFunctionLabel();
        Label elseLabel = getNonFunctionLabel();
        visitExpr(ctx.expr());
        instrs.add(new DataProcessingInstruction<>(CMP, freeRegisters.peek(), 0));
        instrs.add(new BranchInstruction(BEQ, hasElse ? elseLabel : fiLabel));
        visitNewScope(ctx.stat(0));
        if (hasElse) {
            instrs.add(new BranchInstruction(B, fiLabel));
            instrs.add(elseLabel);
            visitNewScope(ctx.stat(1));
        }
        instrs.add(fiLabel);
        return null;
    }

    @Override
    public Type visitWhileStat(@NotNull WhileStatContext ctx) {
        return visitWhile(ctx.expr(), initialiseStatList(ctx.stat()), false);
    }

    @Override
    public Type visitDoWhileStat(@NotNull DoWhileStatContext ctx) {
        return visitWhile(ctx.expr(), initialiseStatList(ctx.stat()), true);
    }

    @Override
    public Type visitForStat(@NotNull ForStatContext ctx) {
        // Create new scopes for current symbol table and stack space
        curr = curr.startNewScope();
        stackSpace = stackSpace.startNewScope();
        arrayPositions = arrayPositions.startNewScope();

        // Keep reference to position of the stack pointer
        int oldStackPos = stackPos;

        // Extend the stack with the size of the new scope
        Integer scopeSize = stackVisitor.visit(ctx.stat(0));
        if (scopeSize == null) {
            scopeSize = 0;
        }
        if (stackPos != stackSize) {
            stackPos = stackSize;
        }
        stackSize += scopeSize;
        addSubStackInstrs(scopeSize);

        visit(ctx.stat(0));
        visitWhile(ctx.expr(), initialiseStatList(ctx.stat(2), ctx.stat(1)), false);

        // Reset the stack and symbol tables to the original states
        addAddStackInstrs(scopeSize);
        stackSize -= scopeSize;
        stackPos = oldStackPos;

        arrayPositions = arrayPositions.endCurrentScope();
        stackSpace = stackSpace.endCurrentScope();
        curr = curr.endCurrentScope();

        return null;
    }

    @Override
    public Type visitBreak(@NotNull BreakContext ctx) {
        instrs.add(new BranchInstruction(B, exitLabel));
        return null;
    }

    @Override
    public Type visitContinue(@NotNull ContinueContext ctx) {
        instrs.add(new BranchInstruction(B, condLabel));
        return null;
    }

    @Override
    public Type visitBeginEnd(@NotNull BeginEndContext ctx) {
        visitNewScope(ctx.stat());
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
        return visitChildren(ctx);
    }

    @Override
    public Type visitNewPair(@NotNull NewPairContext ctx) {
        instrs.add(new SingleDataTransferInstruction<>(LDR, r0, NEWPAIR_SIZE));
        instrs.add(new BranchInstruction(BL, new Label("malloc")));
        Register pair = freeRegisters.pop();
        instrs.add(new DataProcessingInstruction<>(MOV, pair, r0));
        Register var = freeRegisters.peek();

        Type type1 = visitExpr(ctx.expr(0));
        int size1 = INT_SIZE;
        if (type1.equalsType(CHAR) || type1.equalsType(BOOL)) {
            size1 = CHAR_SIZE;
        }
        instrs.add(new SingleDataTransferInstruction<>(LDR, r0, size1));
        instrs.add(new BranchInstruction(BL, new Label("malloc")));

        SingleDataTransferType storeType1 = STR;
        if (type1.equalsType(CHAR) || type1.equalsType(BOOL)) {
            storeType1 = STRB;
        }

        instrs.add(new SingleDataTransferInstruction<>(storeType1, var, r0));
        instrs.add(new SingleDataTransferInstruction<>(STR, r0, pair));


        Type type2 = visitExpr(ctx.expr(1));
        int size2 = INT_SIZE;
        if (type2.equalsType(CHAR) || type2.equalsType(BOOL)) {
            size2 = CHAR_SIZE;
        }
        instrs.add(new SingleDataTransferInstruction<>(LDR, r0, size2));
        instrs.add(new BranchInstruction(BL, new Label("malloc")));

        SingleDataTransferType storeType2 = STR;
        if (type2.equalsType(CHAR) || type2.equalsType(BOOL)) {
            storeType2 = STRB;
        }
        instrs.add(new SingleDataTransferInstruction<>(storeType2, var, r0));
        instrs.add(new SingleDataTransferInstruction<>(STR, r0,
                new ShiftRegister(pair.getType(), 4, null)));

        freeRegisters.push(pair);

        return null;
    }

    @Override
    public Type visitCallFunc(@NotNull CallFuncContext ctx) {
        String funName = ctx.ident().getText();
        if (ctx.argList() != null) {
            visitArgList(ctx.argList());
        }
        instrs.add(new BranchInstruction(BL, new Label(funName, null, true)));
        instrs.add(new DataProcessingInstruction<>(ADD, sp, sp, funcSize));
        instrs.add(new DataProcessingInstruction<>(MOV, freeRegisters.peek(), r0));
        funcSize = 0;
        return null;
    }

    @Override
    public Type visitArgList(@NotNull ArgListContext ctx) {
        List<ExprContext> exprs = ctx.expr();
        for (int i = exprs.size() - 1; i >= 0; i--) {
            ExprContext e = exprs.get(i);
            Type type = visitExpr(e);
            SingleDataTransferType storeType = STR;
            int size = INT_SIZE;
            if (type.equalsType(CHAR) || type.equalsType(BOOL)) {
                storeType = STRB;
                size = BOOL_SIZE;
            }
            funcSize += size;
            instrs.add(new SingleDataTransferInstruction<>(storeType, freeRegisters.peek(),
                    new ShiftRegister(SP, -size, '!')));
        }
        return null;
    }

    @Override
    public Type visitPairElem(@NotNull PairElemContext ctx) {
        String pairName = ctx.expr().getText();
        String elem = ctx.FIRST() != null ? "fst" : "snd";
        String pairElem = pairName + "." + elem;
        int pairOffset = stackSize - stackSpace.lookUpAll(pairName);
        int elemOffset = heapSpace.lookUpAll(pairName).get(pairElem);
        Register pair = freeRegisters.pop();
        Register dst = freeRegisters.peek();
        Type type = curr.lookUpAll(pairElem);


        instrs.add(new SingleDataTransferInstruction<>(LDR, pair,
                pairOffset == 0 ? sp : new ShiftRegister(SP, pairOffset, null)));

        Label checkNullPointer = new Label("p_check_null_pointer");
        Label nullReference = data.getMessageLocation(new Identifier(STRING,
                "NullReferenceError: dereference a null reference\\n\\0"));

        instrs.add(new DataProcessingInstruction<>(MOV, r0, pair));
        instrs.add(new BranchInstruction(BL, checkNullPointer));
        methodGenerator.checkNullPointer(checkNullPointer, nullReference);

        instrs.add(new SingleDataTransferInstruction<>(LDR, dst,
                elemOffset == 0 ? pair : new ShiftRegister(pair.getType(), elemOffset, null)));

        SingleDataTransferType loadType = LDR;

        if (type.equalsType(CHAR) || type.equalsType(BOOL)) {
            loadType = LDRSB;
        }
        instrs.add(new SingleDataTransferInstruction<>(loadType, dst, dst));

        heapPtr = pair;
        return type;
    }

    @Override
    public Type visitType(@NotNull TypeContext ctx) {
        if (ctx.type() != null) {
            return new ArrayType(visitType(ctx.type()));
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
        return new ArrayType(visitType(ctx.type()));
    }

    @Override
    public Type visitPairType(@NotNull PairTypeContext ctx) {
        Type lhs = visitPairElemType(ctx.pairElemType(0));
        Type rhs = visitPairElemType(ctx.pairElemType(1));
        return new PairType(lhs, rhs);
    }

    @Override
    public Type visitPairElemType(@NotNull PairElemTypeContext ctx) {
        if (ctx.PAIR() != null) {
            return NULL;
        }
        return visitChildren(ctx);
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
        visitExpr(ctx.expr());
        return null;
    }

    @Override
    public Type visitUnaryOper(@NotNull UnaryOperContext ctx) {
        ExprContext e = (ExprContext) ctx.getParent();
        ExprContext arg = e.expr(0);

        Register var = freeRegisters.peek();


        switch (ctx.getText()) {
            case "!":
                visitExpr(arg);
                instrs.add(new DataProcessingInstruction<>(EOR, var, var, 1));
                return BOOL;
            case "-":
                if (arg.intLiter() != null) {
                    String intVal = arg.intLiter().getText();
                    data.getMessageLocation(new Identifier(INT, intVal));
                    int intLiter = (int) (-1 * Long.parseLong(intVal));
                    instrs.add(new SingleDataTransferInstruction<>(LDR, freeRegisters.peek(), intLiter));
                    return INT;
                }
                Label overflowMsg = data.getMessageLocation(new Identifier(STRING,
                        "OverflowError: the result is too small/large to store" +
                                " in a 4-byte signed-integer.\\n"));
                Label overflow = new Label("p_throw_overflow_error");
                instrs.add(new DataProcessingInstruction<>(RSBS, var, var, 0));
                instrs.add(new BranchInstruction(BLVS, overflow));
                methodGenerator.throwOverflow(overflow, overflowMsg);
                return INT;
            case "len":
                visitExpr(arg);
                instrs.add(new SingleDataTransferInstruction<>(LDR, var, var));
                return INT;
            case "ord":
                visitExpr(arg);
                return INT;
            case "chr":
                visitExpr(arg);
                return CHAR;
        }
        return null;
    }

    @Override
    public Type visitBoolBinaryOper(@NotNull BoolBinaryOperContext ctx) {
        ExprContext e = (ExprContext) ctx.getParent();
        ExprContext arg1 = e.expr(0);
        ExprContext arg2 = e.expr(1);

        visitExpr(arg1);
        Register var1 = freeRegisters.pop();
        visitExpr(arg2);
        Register var2 = freeRegisters.peek();

        switch (ctx.getText()) {
            case "||":
                instrs.add(new DataProcessingInstruction<>(ORR, var1, var1, var2));
                break;
            case "&&":
                instrs.add(new DataProcessingInstruction<>(AND, var1, var1, var2));
                break;
        }

        freeRegisters.push(var1);

        return BOOL;
    }

    @Override
    public Type visitBinaryOper(@NotNull BinaryOperContext ctx) {
        ExprContext e = (ExprContext) ctx.getParent();
        ExprContext arg1 = e.expr(0);
        ExprContext arg2 = e.expr(1);

        visitExpr(arg1);
        Register var1 = freeRegisters.pop();
        visitExpr(arg2);
        Register var2 = freeRegisters.peek();
        Type retType = null;


        Label overflowMsg = data.getMessageLocation(new Identifier(STRING,
                "OverflowError: the result is too small/large to store" +
                        " in a 4-byte signed-integer.\\n"));
        Label overflow = new Label("p_throw_overflow_error");
        Label divOrModByZeroError = data.getMessageLocation(new Identifier(STRING,
                "DivideByZeroError: divide or modulo by zero\\n\\0"));
        Label divOrModByZero = new Label("p_check_divide_by_zero");


        switch (ctx.getText()) {
            case "*":
                instrs.add(new MultiplyInstruction(SMULL, var1, var2, var1, var2));
                instrs.add(new DataProcessingInstruction<>(CMP, var2, var1, new ShiftInstruction(ASR, 31)));
                instrs.add(new BranchInstruction(BLNE, overflow));
                methodGenerator.throwOverflow(overflow, overflowMsg);
                retType = INT;
                break;
            case "/":
                instrs.add(new DataProcessingInstruction<>(MOV, r0, var1));
                instrs.add(new DataProcessingInstruction<>(MOV, r1, var2));

                instrs.add(new BranchInstruction(BL, divOrModByZero));
                methodGenerator.checkDivByZero(divOrModByZero, divOrModByZeroError);

                instrs.add(new BranchInstruction(BL, new Label("__aeabi_idivmod")));
                instrs.add(new DataProcessingInstruction<>(MOV, var1, r0));
                instrs.add(new DataProcessingInstruction<>(MOV, r0, var1));
                retType = INT;
                break;
            case "%":
                instrs.add(new DataProcessingInstruction<>(MOV, r0, var1));
                instrs.add(new DataProcessingInstruction<>(MOV, r1, var2));

                instrs.add(new BranchInstruction(BL, divOrModByZero));
                methodGenerator.checkDivByZero(divOrModByZero, divOrModByZeroError);

                instrs.add(new BranchInstruction(BL, new Label("__aeabi_idivmod")));
                instrs.add(new DataProcessingInstruction<>(MOV, var1, r1));
                instrs.add(new DataProcessingInstruction<>(MOV, r0, var1));
                retType = INT;
                break;
            case "+":
                instrs.add(new DataProcessingInstruction<>(ADDS, var1, var1, var2));
                instrs.add(new BranchInstruction(BLVS, overflow));
                methodGenerator.throwOverflow(overflow, overflowMsg);
                retType = INT;
                break;
            case "-":
                instrs.add(new DataProcessingInstruction<>(SUBS, var1, var1, var2));
                instrs.add(new BranchInstruction(BLVS, overflow));
                methodGenerator.throwOverflow(overflow, overflowMsg);
                retType = INT;
                break;
            case ">":
                instrs.add(new DataProcessingInstruction<>(CMP, var1, var2));
                instrs.add(new DataProcessingInstruction<>(MOVGT, var1, 1));
                instrs.add(new DataProcessingInstruction<>(MOVLE, var1, 0));
                retType = BOOL;
                break;
            case ">=":
                instrs.add(new DataProcessingInstruction<>(CMP, var1, var2));
                instrs.add(new DataProcessingInstruction<>(MOVGE, var1, 1));
                instrs.add(new DataProcessingInstruction<>(MOVLT, var1, 0));
                retType = BOOL;
                break;
            case "<":
                instrs.add(new DataProcessingInstruction<>(CMP, var1, var2));
                instrs.add(new DataProcessingInstruction<>(MOVLT, var1, 1));
                instrs.add(new DataProcessingInstruction<>(MOVGE, var1, 0));
                retType = BOOL;
                break;
            case "<=":
                instrs.add(new DataProcessingInstruction<>(CMP, var1, var2));
                instrs.add(new DataProcessingInstruction<>(MOVLE, var1, 1));
                instrs.add(new DataProcessingInstruction<>(MOVGT, var1, 0));
                retType = BOOL;
                break;
            case "==":
                instrs.add(new DataProcessingInstruction<>(CMP, var1, var2));
                instrs.add(new DataProcessingInstruction<>(MOVEQ, var1, 1));
                instrs.add(new DataProcessingInstruction<>(MOVNE, var1, 0));
                retType = BOOL;
                break;
            case "!=":
                instrs.add(new DataProcessingInstruction<>(CMP, var1, var2));
                instrs.add(new DataProcessingInstruction<>(MOVNE, var1, 1));
                instrs.add(new DataProcessingInstruction<>(MOVEQ, var1, 0));
                retType = BOOL;
                break;
        }

        freeRegisters.push(var1);

        return retType;
    }

    @Override
    public Type visitIdent(@NotNull IdentContext ctx) {
        String ident = ctx.getText();
        currVarPos = stackSize - stackSpace.lookUpAll(ident);
        Type type = curr.lookUpAll(ident);
        SingleDataTransferType loadRegType = LDR;
        if (type.equalsType(CHAR) || type.equalsType(BOOL)) {
            loadRegType = LDRSB;
        }
        instrs.add(new SingleDataTransferInstruction<>(loadRegType,
                freeRegisters.peek(), new ShiftRegister(SP, currVarPos, null)));
        return type;
    }

    @Override
    public Type visitArrayElem(@NotNull ArrayElemContext ctx) {
        Register array = freeRegisters.pop();
        String arrayName = ctx.ident().getText();
        int offset = stackPos - arrayPositions.lookUpAll(arrayName);
        instrs.add(new DataProcessingInstruction<>(ADD, array, sp, offset));
        Type type = curr.lookUpAll(arrayName);
        for (ExprContext index : ctx.expr()) {
            type = getArrayElemType(type);
            visitExpr(index);
            instrs.add(new SingleDataTransferInstruction<>(LDR, array, array));

            instrs.add(new DataProcessingInstruction<>(MOV, r0, freeRegisters.peek()));
            instrs.add(new DataProcessingInstruction<>(MOV, r1, array));
            Label checkArray = new Label("p_check_array_bounds");
            Label negIndex = data.getMessageLocation(new Identifier(STRING,
                    "ArrayIndexOutOfBoundsError: negative index\\n\\0"));
            Label largeIndex = data.getMessageLocation(new Identifier(STRING,
                    "ArrayIndexOutOfBoundsError: index too large\\n\\0"));
            instrs.add(new BranchInstruction(BL, checkArray));
            methodGenerator.checkArrayBounds(checkArray, negIndex, largeIndex);

            instrs.add(new DataProcessingInstruction<>(ADD, array, array, 4));
            if (type.equalsType(BOOL) || type.equalsType(CHAR)) {
                instrs.add(new DataProcessingInstruction<>(ADD, array, array, freeRegisters.peek()));
            } else {
                instrs.add(new DataProcessingInstruction<>(ADD, array, array, freeRegisters.peek(),
                        new ShiftInstruction(LSL, 2)));
            }
        }
        SingleDataTransferType loadType = LDR;
        if (type.equalsType(BOOL) || type.equalsType(CHAR)) {
            loadType = LDRSB;
        }
        instrs.add(new SingleDataTransferInstruction<>(loadType, array, array));
        freeRegisters.push(array);
        return type;
    }

    @Override
    public Type visitIntLiter(@NotNull IntLiterContext ctx) {
        data.getMessageLocation(new Identifier(INT, ctx.getText()));
        int intLiter = Integer.parseInt(ctx.getText());
        instrs.add(new SingleDataTransferInstruction<>(LDR, freeRegisters.peek(), intLiter));
        return INT;
    }

    @Override
    public Type visitBoolLiter(@NotNull BoolLiterContext ctx) {
        String text = ctx.getText();
        int boolLiter = text.equals("true") ? 1 : 0;
        instrs.add(new DataProcessingInstruction<>(MOV, freeRegisters.peek(), boolLiter));
        return BOOL;
    }

    @Override
    public Type visitCharLiter(@NotNull CharLiterContext ctx) {
        String text = ctx.getText();
        char charLiter = text.charAt(1);
        if (charLiter == '\\') {
            charLiter = text.charAt(2);
        }
        instrs.add(new DataProcessingInstruction<>(MOV, freeRegisters.peek(), charLiter));
        return CHAR;
    }

    @Override
    public Type visitStrLiter(@NotNull StrLiterContext ctx) {
        String value = ctx.getText().substring(1, ctx.getText().length() - 1);
        Identifier ident = new Identifier(STRING, value);
        Label msg = data.getMessageLocation(ident);
        instrs.add(new SingleDataTransferInstruction<>(LDR, freeRegisters.peek(), msg));
        return STRING;
    }



    @Override
    public Type visitArrayLiter(@NotNull ArrayLiterContext ctx) {
        int arrayLength = ctx.expr().size();
        int exprSize = 0;
        int reserve = INT_SIZE;
        Type type = NULL;

        if (arrayLength != 0) {
            type = visitExpr(ctx.expr(0));
            instrs.removeLast();
            exprSize = getExprSize(type);
            reserve = INT_SIZE + arrayLength * exprSize;
        }

        instrs.add(new SingleDataTransferInstruction<>(LDR, r0, reserve));
        instrs.add(new BranchInstruction(BL, new Label("malloc")));
        Register array = freeRegisters.pop();
        instrs.add(new DataProcessingInstruction<>(MOV, array, r0));
        Register expr = freeRegisters.peek();

        int offset = INT_SIZE;
        SingleDataTransferType storeDataTransferType = STR;
        if (type.equalsType(CHAR) || type.equalsType(BOOL)) {
            storeDataTransferType = STRB;
        }

        for (int count = 0; count < arrayLength; count++) {
            ExprContext e = ctx.expr(count);
            visitExpr(e);
            instrs.add(new SingleDataTransferInstruction<>(storeDataTransferType, expr,
                    new ShiftRegister(array.getType(), offset + count * exprSize, null)));
        }
        instrs.add(new SingleDataTransferInstruction<>(LDR, expr, arrayLength));
        instrs.add(new SingleDataTransferInstruction<>(STR, expr, array));

        freeRegisters.push(array);

        return null;
    }

    @Override
    public Type visitPairLiter(@NotNull PairLiterContext ctx) {
        instrs.add(new SingleDataTransferInstruction<>(LDR, freeRegisters.peek(), 0));
        return NULL;
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
