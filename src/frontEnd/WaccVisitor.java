package frontEnd;

import antlr.WaccParser;
import antlr.WaccParser.*;
import antlr.WaccParserBaseVisitor;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static frontEnd.AllTypes.*;

public class WaccVisitor extends WaccParserBaseVisitor<Type> {

    private final int SYNTAX_ERROR_CODE = 100, SEMANTIC_ERROR_CODE = 200;
    private final int ASCII_MAX_VALUE = 127;

    private List<String> semanticErrors;
    private SyntaxErrorListener listener;
    private SymbolTable<Type> head;
    private SymbolTable<Type> curr;

    private boolean isInLoop;

    public WaccVisitor(SyntaxErrorListener listener) {
        this.listener = listener;
        semanticErrors = new ArrayList<>();
    }

    //-------------------------UTILITY FUNCTIONS-----------------------------//

    private boolean hasSemanticErrors() {
        return !semanticErrors.isEmpty();
    }

    private String getLineError(ParserRuleContext ctx){
        return ctx.getStart().getLine() + ":" + ctx.getStart().getCharPositionInLine();
    }

    private void addSemanticError(ParserRuleContext ctx, String msg) {
        semanticErrors.add("Semantic Error: Line " + getLineError(ctx) + " - " + msg);
    }

    private void printErrors(List<String> errors, int errorCode) {
        for (String msg : errors) {
            System.err.println(msg);
        }
        System.err.println("Exitcode: " + errorCode);
        System.exit(errorCode);
    }

    private boolean addArrayElem(@NotNull ParserRuleContext ctx, String var, Type expected, Type actual) {
        if (expected instanceof ArrayType) {
            if (!actual.equalsType(expected)) {
                addSemanticError(ctx, "Right hand side does not match expected type '" + expected + "'");
                return false;
            }
            Type lhsElemType = ((ArrayType) expected).getElement();
            Type rhsElemType = ((ArrayType) actual).getElement();
            if (!lhsElemType.equalsType(rhsElemType)) {
                addSemanticError(ctx, "Type '" + lhsElemType + "' does not match type '" + rhsElemType + "'");
                return false;
            }
            var = var + "[0]";
            curr.add(var, rhsElemType);
            addArrayElem(ctx, var, rhsElemType, lhsElemType);
        }
        return true;
    }

    //------------------------------VISIT METHODS----------------------------//

    @Override
    public Type visitProg(@NotNull ProgContext ctx) {
        // Initialise global symbol table and current symbol table
        head = new SymbolTable<>();
        curr = head;

        for (FuncDeclContext func : ctx.funcDecl()) {
            String funName = func.ident().getText();
            Type funType = visitType(func.type());
            Type[] paramList;
            // Need to check if a parameter list even exists
            if (func.paramList() != null) {
                List<ParamContext> parameters = func.paramList().param();
                paramList = new Type[parameters.size()];
                for (int i = 0; i < parameters.size(); i++) {
                    paramList[i] = visitParam(parameters.get(i));
                }
            } else {
                paramList = new Type[0];
            }
            if (head.lookUpFunc(funName) != null) {
                addSemanticError(ctx, "Function '" + funName + "' is already defined");
            }
            head.addFunction(funName, funType, paramList);
        }

        visitChildren(ctx);
        if (listener.hasSyntaxErrors()) {
            printErrors(listener.getSyntaxErrors(), SYNTAX_ERROR_CODE);
        }
        if (this.hasSemanticErrors()) {
            printErrors(semanticErrors, SEMANTIC_ERROR_CODE);
        }
        return null;
    }

    @Override
    public Type visitFuncDecl(@NotNull FuncDeclContext ctx) {
        String funName = ctx.ident().getText();
        Type funType = visitType(ctx.type());

        String[] varNames;
        // Need to check if a parameter list even exists
        if (ctx.paramList() != null) {
            List<ParamContext> parameters = ctx.paramList().param();
            varNames = new String[parameters.size()];
            for (int i = 0; i < parameters.size(); i++) {
                varNames[i] = parameters.get(i).ident().getText();
            }
        } else {
            varNames = new String[0];
        }

        SymbolTable<Type> old = curr;
        curr = head.lookUpFunc(funName);

        Type[] paramList = head.lookUpParams(funName);
        for (int i = 0; i < paramList.length; i++) {
            Type type = paramList[i];
            if (type == null) {
                return null;
            }
            String var = varNames[i];
            curr.add(var, type);
            addArrayElem(ctx, var, type, type);
            if (type instanceof ArrayType) {
                curr.add(var + "[0]", ((ArrayType) type).getElement());
            }
        }

        Type retType = visit(ctx.stat());

        if (retType == null) {
            listener.addSyntaxError(ctx, "Function '" + funName + "' does not have a return statement");
            return null;
        }

        if (!retType.equalsType(funType)) {
            addSemanticError(ctx, "Return type '" + retType + "' must match" +
                    " the function return type '" + funType + "'");
        }

        curr = old;

        return null;
    }

    @Override
    public Type visitParamList(@NotNull ParamListContext ctx) {
        visitChildren(ctx);
        return null;
    }


    @Override
    public Type visitParam(@NotNull ParamContext ctx) {
        String var = ctx.ident().getText();
        Type type = curr.lookUp(var);
        if (type != null) {
            addSemanticError(ctx, "Variable '" + var + "' is already in use");
        }
        return visitType(ctx.type());
    }

    @Override
    public Type visitSkip(@NotNull SkipContext ctx) {
        // Skip cannot be invalid semantically
        return null;
    }

    @Override
    public Type visitVarInit(@NotNull VarInitContext ctx) {
        String var = ctx.ident().getText();
        Type expected = curr.lookUp(ctx.ident().getText());
        if (expected != null) {
            addSemanticError(ctx, "Variable '" + var + "' is already in use");
        }
        Type actual = visitAssignRhs(ctx.assignRhs());
        expected = visitType(ctx.type());

        // Needed for nested pair type checking
        expected = replaceNullReferences(expected, actual, ctx);

        curr.add(var, expected);

        if (!addArrayElem(ctx, var, expected, actual)) {
            return null;
        }
        if (actual == ANY) {
            return expected;
        }
        if (!expected.equalsType(actual)) {
            addSemanticError(ctx, "Type '" + expected + "' does not match type '" + actual + "'");
        }
        return null;
    }

    private Type replaceNullReferences(Type expected, Type actual, ParserRuleContext ctx) {
        if (expected instanceof PairType) {
            if (actual instanceof PairType) {
                PairType expectedPair = (PairType) expected;
                PairType actualPair = (PairType) actual;
                Type lhs = replaceNullReferences(expectedPair.getLeft(), actualPair.getLeft(), ctx);
                Type rhs = replaceNullReferences(expectedPair.getRight(), actualPair.getRight(), ctx);
                return new PairType(lhs, rhs);
            }
            if (actual.equalsType(NULL)) {
                return expected;
            }
            addSemanticError(ctx, "Type '" + expected + "' does not match type '" + actual + "'");
        }
        if (expected.equalsType(NULL)) {
            if (expected.equalsType(actual)) {
                return actual;
            }
            addSemanticError(ctx, "Type '" + expected + "' does not match type '" + actual + "'");
        }
        return expected;
    }

    @Override
    public Type visitVarAssign(@NotNull VarAssignContext ctx) {
        AssignLhsContext lhs = ctx.assignLhs();
        AssignRhsContext rhs = ctx.assignRhs();
        if (lhs == null || rhs == null) {
            return null;
        }
        Type lhsType = visitAssignLhs(ctx.assignLhs());
        Type rhsType = visitAssignRhs(ctx.assignRhs());

        if (lhsType == null) {
            addSemanticError(ctx, "Variable '" + lhs.getText() +
                    "' is not declared in this scope");
            return null;
        }
        if (rhsType == null) {
            addSemanticError(ctx, "Variable '" + rhs.getText() +
                    "' is not declared in this scope");
            return null;
        }
        if (!lhsType.equalsType(rhsType)) {
            addSemanticError(ctx, "Left hand side '" + lhsType +
                    "' does not match with right hand side '" + rhsType + "'");
        }

        return null;
    }

    @Override
    public Type visitReadStat(@NotNull ReadStatContext ctx) {
        IdentContext ident = ctx.assignLhs().ident();
        Type type = (ident != null) ? curr.lookUpAll(ident.getText())
                : visitAssignLhs(ctx.assignLhs());
        String var = ctx.assignLhs().getText();
        if (type == null) {
            type = curr.lookUpAll(ctx.assignLhs().ident().getText());
        }
        if (type == null) {
            addSemanticError(ctx, "Variable '" + var + "' has not been declared");
            return null;
        }
        if (!(type.equalsType(CHAR) || type.equalsType(INT))) {
            addSemanticError(ctx, "Variable '" + var + "' must be of type 'int' or 'char'");
        }
        return null;
    }

    @Override
    public Type visitFreeStat(@NotNull FreeStatContext ctx) {
        String var = ctx.expr().getText();
        Type type = curr.lookUpAll(var);
        if (type == null) {
            addSemanticError(ctx, "Variable '" + var + "' has not been declared");
        }
        if (!type.equalsType(NULL)) {
            addSemanticError(ctx, "Variable '" + var + "' must be a reference to an array or pair");
        }
        return null;
    }

    @Override
    public Type visitReturnStat(@NotNull ReturnStatContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public Type visitExitStat(@NotNull ExitStatContext ctx) {
        Type expected = visitExpr(ctx.expr());
        if (!expected.equalsType(INT)) {
            addSemanticError(ctx, "Cannot exit with type '" + expected + "'");
        }
        return ANY;
    }

    @Override
    public Type visitPrintStat(@NotNull PrintStatContext ctx) {
        visitExpr(ctx.expr());
        return null;
    }

    @Override
    public Type visitPrintlnStat(@NotNull PrintlnStatContext ctx) {
        visitExpr(ctx.expr());
        return null;
    }

    @Override
    public Type visitIfStat(@NotNull IfStatContext ctx) {
        if (visitExpr(ctx.expr()) != BOOL) {
            addSemanticError(ctx, "If condition must evaluate to a bool value");
        }

        // Check that we have the right number of statements
        boolean hasElse = ctx.ELSE() != null;

        if (ctx.stat().size() < (hasElse ? 2 : 1)) {
            return null;
        }

        Type elseStat = null;

        curr = curr.startNewScope();
        Type thenStat = visit(ctx.stat(0));
        curr = curr.endCurrentScope();

        if (hasElse) {
            curr = curr.startNewScope();
            elseStat = visit(ctx.stat(1));
            curr = curr.endCurrentScope();
        }

        if (thenStat != null && elseStat != null) {
            if (thenStat.equalsType(elseStat)) {
                return thenStat;
            }
            addSemanticError(ctx, "Statements must have the same type (expected: '"
                    + thenStat + "', actual: '" + elseStat + "')");
        }
        return null;
    }

    private List<StatContext> initialiseStatList(StatContext... stats) {
        return Arrays.asList(stats);
    }

    private Type visitWhile(ParserRuleContext ctx, ExprContext expr,
                            List<StatContext> body, StatContext initialiser) {
        Type actual = visitExpr(expr);
        if (actual == null) {
            return null;
        }
        if (!actual.equalsType(BOOL)) {
            addSemanticError(ctx, "Invalid loop condition type (expected: '"
                    + BOOL + "', actual: '" + actual + "'");
        }
        curr = curr.startNewScope();
        if (initialiser != null) {
            String var = "";
            if (initialiser instanceof VarInitContext) {
                var = ((VarInitContext) initialiser).ident().getText();
            }
            if (initialiser instanceof VarAssignContext) {
                var = ((VarAssignContext) initialiser).assignLhs().getText();
            }
            Type type = curr.lookUpAll(var);
            curr.add(var, type);
        }
        isInLoop = true;
        for (StatContext stat : body) {
            if (stat == null) {
                return null;
            }
            visitChildren(stat);
        }
        isInLoop = false;
        curr = curr.endCurrentScope();
        return null;
    }

    @Override
    public Type visitWhileStat(@NotNull WhileStatContext ctx) {
        return visitWhile(ctx, ctx.expr(), initialiseStatList(ctx.stat()), null);
    }

    @Override
    public Type visitDoWhileStat(@NotNull DoWhileStatContext ctx) {
        return visitWhile(ctx, ctx.expr(), initialiseStatList(ctx.stat()), null);
    }

    @Override
    public Type visitForStat(@NotNull ForStatContext ctx) {
        StatContext stat = ctx.stat(0);
        if (!(stat instanceof VarInitContext || stat instanceof VarAssignContext)) {
            listener.addSyntaxError(ctx, "First statement in for loop must be an initialising statement");
            return null;
        }
        curr = curr.startNewScope();
        visit(stat);
        visitWhile(ctx, ctx.expr(), initialiseStatList(ctx.stat(2), ctx.stat(1)), stat);
        curr = curr.endCurrentScope();
        return null;
    }

    private void checkIfInLoop(@NotNull ParserRuleContext ctx) {
        if (!isInLoop) {
            String statement = ctx.getText();
            statement = ("" + statement.charAt(0)).toUpperCase().concat(statement.substring(1));
            listener.addSyntaxError(ctx, statement + " statement can only be used inside a loop statement");
        }
    }

    @Override
    public Type visitBreak(@NotNull BreakContext ctx) {
        checkIfInLoop(ctx);
        return null;
    }

    @Override
    public Type visitContinue(@NotNull ContinueContext ctx) {
        checkIfInLoop(ctx);
        return null;
    }

    @Override
    public Type visitBeginEnd(@NotNull BeginEndContext ctx) {
        // Initialise a new symbol table, with access to previous symbol table contents
        curr = curr.startNewScope();
        visitChildren(ctx);
        curr = curr.endCurrentScope();
        return null;
    }

    @Override
    public Type visitStatSequence(@NotNull StatSequenceContext ctx) {
        StatContext stat1 = ctx.stat(0);
        StatContext stat2 = ctx.stat(1);
        Type endType = visit(stat1);
        // Checks if there is any rubbish after the returning statement from a non-main function
        if (endType != null && stat2 != null && curr != head) {
            listener.addSyntaxError(ctx, "Function has not ended with a return or exit statement");
            return endType;
        }
        // Checks if an return expression has been called in the main function
        if (endType != null && endType != ANY && curr == head) {
            addSemanticError(ctx, "Cannot return from the 'main' function");
        }
        if (stat2 != null) {
            return visit(stat2);
        }
        return null;
    }

    @Override
    public Type visitAssignLhs(@NotNull AssignLhsContext ctx) {
        if (ctx.ident() != null) {
            return curr.lookUpAll(ctx.ident().getText());
        }
        return visitChildren(ctx);
    }

    @Override
    public Type visitAssignRhs(@NotNull AssignRhsContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public Type visitNewPair(@NotNull NewPairContext ctx) {
        ExprContext exprLhs = ctx.expr(0);
        ExprContext exprRhs = ctx.expr(1);
        Type lhs;
        Type rhs;
        if (exprLhs.ident() != null) {
            String var = exprLhs.ident().getText();
            lhs = curr.lookUpAll(var);
            if (lhs == null) {
                addSemanticError(ctx, "Variable '" + var + "' doesn't exist");
            }
        }
        if (exprRhs.ident() != null) {
            String var = exprRhs.ident().getText();
            rhs = curr.lookUpAll(var);
            if (rhs == null) {
                addSemanticError(ctx, "Variable '" + var + "' doesn't exist");
            }
        }
        lhs = visitExpr(exprLhs);
        rhs = visitExpr(exprRhs);
        return new PairType(lhs, rhs);
    }

    @Override
    public Type visitCallFunc(@NotNull CallFuncContext ctx) {
        // Need to check lengths of parameter lists of call function and function declaration
        String funName = ctx.ident().getText();
        Type retType = head.lookUp("func:" + funName);
        if (retType == null) {
            addSemanticError(ctx, "Function '" + funName + "' doesn't exist");
        }

        Type[] paramList = head.lookUpParams(funName);
        int numOfArgs = 0;
        // If arg list exists, update numOfArgs to size of arg list
        if (ctx.argList() != null) {
            numOfArgs = ctx.argList().expr().size();
        }

        if (paramList.length != numOfArgs) {
            addSemanticError(ctx, "Invalid number of arguments in function '" + funName + "' call");
            return retType;
        }
        for (int i = 0; i < numOfArgs; i++) {
            ExprContext e = ctx.argList().expr(i);
            Type argType = visitExpr(ctx.argList().expr(i));
            if (!argType.equalsType(paramList[i])) {
                addSemanticError(ctx, "Argument type '" + argType +
                        "' does not match parameter type '" + paramList[i] + "'");
            }
        }
        return retType;
    }

    @Override
    public Type visitArgList(@NotNull ArgListContext ctx) {
        visitChildren(ctx);
        return null;
    }

    @Override
    public Type visitPairElem(@NotNull PairElemContext ctx) {
        String var = ctx.expr().getText();
        Type type = curr.lookUpAll(var);
        if (type == null) {
            addSemanticError(ctx, "Variable '" + var + "' doesn't exist");
        }
        PairType t = (PairType) type;
        type = (ctx.FIRST() != null) ? t.getLeft() : t.getRight();
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
        if(ctx == null) {
            return ANY;
        }
        if (ctx.binaryOper() != null) {
            return visitBinaryOper(ctx.binaryOper());
        }
        if (ctx.unaryOper() != null) {
            return visitUnaryOper(ctx.unaryOper());
        }
        if (ctx.ident() != null) {
            Type varType = curr.lookUpAll(ctx.ident().getText());
            if (varType == null) {
                addSemanticError(ctx.ident(), "Variable '" + ctx.ident().getText() +
                        "' is not declared in this scope.");
                return ANY;
            }
            return varType;
        }
        return visitChildren(ctx);
    }

    @Override
    public Type visitBracketExpr(@NotNull BracketExprContext ctx) {
        return visitExpr(ctx.expr());
    }

    @Override
    public Type visitUnaryOper(@NotNull UnaryOperContext ctx) {
        Type argT = null, retT = null;
        switch (ctx.getText()) {
            case "!":
                argT = BOOL;
                retT = BOOL;
                break;
            case "-":
                argT = INT;
                retT = INT;
                break;
            case "len":
                argT = new ArrayType(ANY);
                retT = INT;
                break;
            case "ord":
                argT = CHAR;
                retT = INT;
                break;
            case "chr":
                argT = INT;
                retT = CHAR;
                break;
        }
        ExprContext e = (ExprContext) ctx.getParent();
        Type elemType = visitExpr(e.expr(0));
        if (!elemType.equalsType(argT)) {
            addSemanticError(ctx, "Unary operator is not applicable for type '" + elemType + "'");
        }
        return retT;
    }

    @Override
    public Type visitBoolBinaryOper(@NotNull BoolBinaryOperContext ctx) {
        Type argT = BOOL;
        Type retT = BOOL;
        ExprContext e = (ExprContext) ctx.getParent();
        Type t1 = visitExpr(e.expr(0));
        Type t2 = visitExpr(e.expr(1));
        if (!t1.equalsType(t2)) {
            addSemanticError(ctx, "Type on left hand side '" + t1 +
                    "' does not match type on right hand side '" + t2 + "'");
        }
        if (!argT.equalsType(t1)) {
            addSemanticError(ctx, "Binary operator is not applicable for type '" + t1 + "'");
        }
        return retT;
    }

    @Override
    public Type visitBinaryOper(@NotNull BinaryOperContext ctx) {
        List<Type> argTypes = new ArrayList<>();
        Type retT = null;
        switch (ctx.getText()) {
            case "*":
            case "/":
            case "%":
            case "+":
            case "-":
                argTypes.add(INT);
                retT = INT;
                break;
            case ">":
            case ">=":
            case "<":
            case "<=":
                argTypes.add(INT);
                argTypes.add(CHAR);
                retT = BOOL;
                break;
            case "==":
            case "!=":
                argTypes.add(ANY);
                retT = BOOL;
                break;
        }
        ExprContext e = (ExprContext) ctx.getParent();
        Type t1 = visitExpr(e.expr(0));
        Type t2 = visitExpr(e.expr(1));
        if (!t1.equalsType(t2)) {
            addSemanticError(ctx, "Type on left hand side '" + t1 +
                    "' does not match type on right hand side '" + t2 + "'");
        }
        boolean typeMatch = false;
        for (Type t : argTypes) {
            if (t1.equalsType(t)) {
                typeMatch = true;
                break;
            }
        }
        if (!typeMatch) {
            addSemanticError(ctx, "Binary operator is not applicable for type '" + t1 + "'");
        }
        return retT;
    }

    @Override
    public Type visitIdent(@NotNull IdentContext ctx) {
        // Ident covers too many cases, so better to check for it in other methods.
        return null;
    }

    @Override
    public Type visitArrayElem(@NotNull ArrayElemContext ctx) {
        String var = ctx.ident().getText();
        for (ExprContext e : ctx.expr()) {
            if (e.intLiter() == null) {
                Type arrayIndex = visitExpr(e);
                if (arrayIndex == null) {
                    // It must be an identifier as a result.
                    arrayIndex = curr.lookUpAll(e.ident().getText());
                }
                if (!arrayIndex.equalsType(INT)) {
                    addSemanticError(ctx, "Cannot use value of type '" + arrayIndex + "' to access array element");
                    return ANY;
                }
            }
            // Just need to check the type, therefore 0 index will satisfy this.
            var = var + "[0]";
        }
        Type type = curr.lookUpAll(var);
        if (type == null) {
            if (curr.lookUpAll(ctx.ident().getText()).equalsType(STRING)) {
                return CHAR;
            }
            addSemanticError(ctx, "Array element '" + var + "' doesn't exist");
            return ANY;
        }
        for (ExprContext e : ctx.expr()) {
            Type arrayIndex = visitExpr(e);
            if (!arrayIndex.equalsType(INT)) {
                addSemanticError(ctx, "Cannot use value of type '" + arrayIndex + "' to access array element");
            }
        }
        return type;
    }

    @Override
    public Type visitIntLiter(@NotNull IntLiterContext ctx) {long size;
        try {
            size = Long.parseLong(ctx.getText());
        } catch (NumberFormatException e) {
            return INT;
        }
        long signedInt = (ctx.PLUS() != null) ? size : -1 * size;
        if (signedInt < Integer.MIN_VALUE || signedInt > Integer.MAX_VALUE) {
            listener.addSyntaxError(ctx, "Integer value must be between -2^31 and 2^31 - 1");
        }
        return INT;
    }

    @Override
    public Type visitBoolLiter(@NotNull BoolLiterContext ctx) {
        return BOOL;
    }

    @Override
    public Type visitCharLiter(@NotNull CharLiterContext ctx) {
        int c = ctx.getText().charAt(0);
        if (c > ASCII_MAX_VALUE) {
            listener.addSyntaxError(ctx, "Only ASCII printable characters allowed");
        }
        return CHAR;
    }

    @Override
    public Type visitStrLiter(@NotNull StrLiterContext ctx) {
        return STRING;
    }

    @Override
    public Type visitArrayLiter(@NotNull ArrayLiterContext ctx) {
        int expSize = ctx.expr().size();
        if (expSize == 0) {
            return new ArrayType(ANY);
        }
        ExprContext exp = ctx.expr(0);
        Type elemType = visitExpr(exp);
        for (ExprContext e : ctx.expr()) {
            if (!elemType.equalsType(visitExpr(e))) {
                addSemanticError(ctx, "Array values must all be of type '" + elemType + "'");
            }
        }
        return new ArrayType(elemType);
    }

    @Override
    public Type visitPairLiter(@NotNull PairLiterContext ctx) {
        return NULL;
    }

    @Override
    public Type visitChildren(@NotNull RuleNode node) {
        Type result = null;
        int n = node.getChildCount();
        for (int i = 0; i < n; i++) {
            ParseTree c = node.getChild(i);
            Type childResult = c.accept(this);
            if (childResult != null) {
                result = childResult;
            }
        }
        return result;
    }

}
