package frontEnd;

import antlr.WaccParser;
import antlr.WaccParser.*;
import antlr.WaccParserBaseVisitor;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;

import java.util.ArrayList;
import java.util.List;

public class WaccVisitor extends WaccParserBaseVisitor<Type> {

    private final int SYNTAX_ERROR_CODE = 100, SEMANTIC_ERROR_CODE = 200;
    private final int ASCII_MAX_VALUE = 127;

    private List<String> semanticErrors;
    private SyntaxErrorListener listener;
    private SymbolTable<Type> head;
    private SymbolTable<Type> curr;

    public WaccVisitor(SyntaxErrorListener listener) {
        this.listener = listener;
        semanticErrors = new ArrayList<>();
    }

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

    @Override
    public Type visitExitExpr(@NotNull ExitExprContext ctx) {
        Type expected = visitExpr(ctx.expr());
        if (!expected.equalsType(AllTypes.INT)) {
            addSemanticError(ctx, "Cannot exit with non-int value");
        }
        return AllTypes.ANY;
    }

    @Override
    public Type visitRead_lhs(@NotNull Read_lhsContext ctx) {
        IdentContext ident = ctx.assign_lhs().ident();
        Type type = (ident != null) ? curr.lookUpAll(ident.getText())
                : visitAssign_lhs(ctx.assign_lhs());
        String var = ctx.assign_lhs().getText();
        if (type == null) {
            type = curr.lookUpAll(ctx.assign_lhs().ident().getText());
        }
        if (type == null) {
            addSemanticError(ctx, "Variable " + var + " has not been declared");
            return null;
        }
        if (!(type.equalsType(AllTypes.CHAR) || type.equalsType(AllTypes.INT))) {
            addSemanticError(ctx, "Variable " + var + " must be of type int or char");
        }
        return null;
    }

    @Override
    public Type visitArray_elem(@NotNull Array_elemContext ctx) {
        String var = ctx.ident().getText();
        for (ExprContext e : ctx.expr()) {
            if (e.int_liter() == null) {
                Type arrayIndex = visitExpr(e);
                if (arrayIndex == null) {
                    // It must be an identifier as a result.
                    arrayIndex = curr.lookUpAll(e.ident().getText());
                }
                if (!arrayIndex.equalsType(AllTypes.INT)) {
                    addSemanticError(ctx, "Must use an integer to access array element");
                    return AllTypes.ANY;
                }
            }
            // Just need to check the type, therefore 0 index will satisfy this.
            var = var + "[0]";
        }
        Type type = curr.lookUpAll(var);
        if (type == null) {
            if (curr.lookUpAll(ctx.ident().getText()).equalsType(AllTypes.STRING)) {
                return AllTypes.CHAR;
            }
            addSemanticError(ctx, "Array element '" + var + "' doesn't exist");
            return AllTypes.ANY;
        }
        for (ExprContext e : ctx.expr()) {
            Type arrayIndex = visitExpr(e);
            if (!arrayIndex.equalsType(AllTypes.INT)) {
                addSemanticError(ctx, "Must use an integer to access array element");
            }
        }
        return type;
    }

    @Override
    public Type visitAssign_lhs(@NotNull Assign_lhsContext ctx) {
        if (ctx.ident() != null) {
            return curr.lookUpAll(ctx.ident().getText());
        }
        return visitChildren(ctx);
    }

    @Override
    public Type visitIdent(@NotNull IdentContext ctx) {
        // Temporary fix as Ident covers too many cases.
        return null;
    }

    @Override
    public Type visitAssign_rhs(@NotNull Assign_rhsContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public Type visitFreeExpr(@NotNull FreeExprContext ctx) {
        String var = ctx.expr().getText();
        Type type = curr.lookUpAll(var);
        if (type == null) {
            addSemanticError(ctx, "Variable " + var + " has not been declared");
        }
        if (!(type instanceof ArrayType || type instanceof PairType)) {
            addSemanticError(ctx, "Variable must be a reference to an array or pair");
        }
        return null;
    }

    @Override
    public Type visitSkip(@NotNull SkipContext ctx) {
        // Skip cannot be invalid semantically
        return null;
    }

    @Override
    public Type visitType(@NotNull TypeContext ctx) {
        // Bit of a hack, but can't help it
        if (ctx.type() != null) {
            return new ArrayType(visitType(ctx.type()));
        }
        return visitChildren(ctx);
    }

    @Override
    public Type visitInt_liter(@NotNull Int_literContext ctx) {
        long size;
        try {
            size = Long.parseLong(ctx.getText());
        } catch (NumberFormatException e) {
            return AllTypes.INT;
        }
        long signedInt = (ctx.PLUS() != null) ? size : -1 * size;
        if (signedInt < Integer.MIN_VALUE || signedInt > Integer.MAX_VALUE) {
            listener.addSyntaxError(ctx, "Integer value must be between -2^31 and 2^31 - 1");
        }
        return AllTypes.INT;
    }

    @Override
    public Type visitBase_type(@NotNull Base_typeContext ctx) {
        switch (ctx.getText()) {
            case "int":
                return AllTypes.INT;
            case "bool":
                return AllTypes.BOOL;
            case "char":
                return AllTypes.CHAR;
            case "string":
                return AllTypes.STRING;
        }
        return null;
    }

    @Override
    public Type visitParam(@NotNull ParamContext ctx) {
        String var = ctx.ident().getText();
        Type type = curr.lookUp(var);
        if (type != null) {
            addSemanticError(ctx, "Variable " + var + " is already in use");
        }
        return visitType(ctx.type());
    }

    @Override
    public Type visitPair_type(@NotNull Pair_typeContext ctx) {
        Type lhs = visitPair_elem_type(ctx.pair_elem_type(0));
        Type rhs = visitPair_elem_type(ctx.pair_elem_type(1));
        return new PairType(lhs, rhs);
    }

    @Override
    public Type visitChar_liter(@NotNull Char_literContext ctx) {
        int c = ctx.getText().charAt(0);
        if (c > ASCII_MAX_VALUE) {
            listener.addSyntaxError(ctx, "Only ASCII printable characters allowed");
        }
        return AllTypes.CHAR;
    }

    @Override
    public Type visitInitialization(@NotNull InitializationContext ctx) {
        String var = ctx.ident().getText();
        Type expected = curr.lookUp(ctx.ident().getText());
        if (expected != null) {
            addSemanticError(ctx, "Variable " + var + " is already in use");
        }
        expected = visitType(ctx.type());
        curr.add(var, expected);
        Type actual = visitAssign_rhs(ctx.assign_rhs());
        if (addArrayElem(ctx, var, expected, actual)) {
            return null;
        }
        if (actual == AllTypes.ANY) {
            return expected;
        }
        if (!expected.equalsType(actual)) {
            addSemanticError(ctx, "Type " + expected + " does not match type " + actual);
        }
        return null;
    }

    private boolean addArrayElem(@NotNull ParserRuleContext ctx, String var, Type expected, Type actual) {
        if (expected instanceof ArrayType) {
            if (!actual.equalsType(expected)) {
                addSemanticError(ctx, "Right hand side does not match expected type '" + expected + "'");
                return true;
            }
            Type lhsElemType = ((ArrayType) expected).getElement();
            Type rhsElemType = ((ArrayType) actual).getElement();
            if (!lhsElemType.equalsType(rhsElemType)) {
                addSemanticError(ctx, "Type " + lhsElemType + " does not match type " + rhsElemType);
                return true;
            }
            var = var + "[0]";
            curr.add(var, rhsElemType);
            addArrayElem(ctx, var, rhsElemType, lhsElemType);
        }
        return false;
    }

    @Override
    public Type visitIfExpr(@NotNull IfExprContext ctx) {
        if (visitExpr(ctx.expr()) != AllTypes.BOOL) {
            addSemanticError(ctx, "If condition must evaluate to a bool value");
        }

        // Check that we have the right number of statements
        if (ctx.stat().size() <  2) {
            return null;
        }

        curr = new SymbolTable<>(curr);
        Type thenStat = visit(ctx.stat(0));
        curr = curr.encSymbolTable;

        curr = new SymbolTable<>(curr);
        Type elseStat = visit(ctx.stat(1));
        curr = curr.encSymbolTable;

        if (thenStat != null && elseStat != null) {
            if (thenStat.equalsType(elseStat)) {
                return thenStat;
            }
            addSemanticError(ctx, "Statements must have the same type (expected: "
                    + thenStat + ", actual: " + elseStat + ")");
        }
        return null;
    }

    @Override
    public Type visitBool_binary_oper(@NotNull Bool_binary_operContext ctx) {
        Type argT = AllTypes.BOOL;
        Type retT = AllTypes.BOOL;
        ExprContext e = (ExprContext) ctx.getParent();
        Type t1 = visitExpr(e.expr(0));
        Type t2 = visitExpr(e.expr(1));
        if (!t1.equalsType(t2)) {
            addSemanticError(ctx, "Type on left hand side '" + t1 +
                    "' does not match type on right hand side '" + t2 + "'");
        }
        if (!argT.equalsType(t1)) {
            addSemanticError(ctx, "Binary operator is not applicable for type " + t1);
        }
        return retT;
    }

    @Override
    public Type visitBinary_oper(@NotNull Binary_operContext ctx) {
        List<Type> argTypes = new ArrayList<>();
        Type retT = null;
        switch (ctx.getText()) {
            case "*":
            case "/":
            case "%":
            case "+":
            case "-":
                argTypes.add(AllTypes.INT);
                retT = AllTypes.INT;
                break;
            case ">":
            case ">=":
            case "<":
            case "<=":
                argTypes.add(AllTypes.INT);
                argTypes.add(AllTypes.CHAR);
                retT = AllTypes.BOOL;
                break;
            case "==":
            case "!=":
                argTypes.add(AllTypes.ANY);
                retT = AllTypes.BOOL;
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
            addSemanticError(ctx, "Binary operator is not applicable for type " + t1);
        }
        return retT;
    }

    @Override
    public Type visitExpr(@NotNull ExprContext ctx) {
        if (ctx.binary_oper() != null) {
            return visitBinary_oper(ctx.binary_oper());
        }
        if (ctx.unary_oper() != null) {
            return visitUnary_oper(ctx.unary_oper());
        }
        if (ctx.ident() != null) {
            Type varType = curr.lookUpAll(ctx.ident().getText());
            if (varType == null) {
                addSemanticError(ctx.ident(), "Variable '" + ctx.ident().getText() +
                        "' is not declared in this scope.");
                return AllTypes.ANY;
            }
            return varType;
        }
        return visitChildren(ctx);
    }

    @Override
    public Type visitPair_elem(@NotNull Pair_elemContext ctx) {
        String var = ctx.expr().getText();
        Type type = curr.lookUpAll(var);
        if (type == null) {
            addSemanticError(ctx, "Variable " + var + " doesn't exist");
        }
        PairType t = (PairType) type;
        type = (ctx.FIRST() != null) ? t.getLeft() : t.getRight();
        return type;
    }

    @Override
    public Type visitCallParantheses(@NotNull CallParanthesesContext ctx) {

        // Need to check lengths of parameter lists of call function and function declaration
        String funName = ctx.ident().getText();
        Type retType = head.lookUp("func:" + funName);
        if (retType == null) {
            addSemanticError(ctx, "Function " + funName + " doesn't exist");
        }

        Type[] paramList = head.lookUpParams(funName);
        int numOfArgs = 0;
        // If arg list exists, update numOfArgs to size of arg list
        if (ctx.arg_list() != null) {
            numOfArgs = ctx.arg_list().expr().size();
        }

        if (paramList.length != numOfArgs) {
            addSemanticError(ctx, "Invalid number of arguments");
            return retType;
        }
        for (int i = 0; i < numOfArgs; i++) {
            ExprContext e = ctx.arg_list().expr(i);
            Type argType = visitExpr(ctx.arg_list().expr(i));
            if (!argType.equalsType(paramList[i])) {
                addSemanticError(ctx, "Types don't match");
            }
        }
        return retType;
    }

    @Override
    public Type visitArray_type(@NotNull Array_typeContext ctx) {
        return new ArrayType(visitType(ctx.type()));
    }

    @Override
    public Type visitPrintExpr(@NotNull PrintExprContext ctx) {
        // Print should work with any expression.
        visitExpr(ctx.expr());
        return null;
    }

    @Override
    public Type visitBool_liter(@NotNull Bool_literContext ctx) {
        return AllTypes.BOOL;
    }

    @Override
    public Type visitUnary_oper(@NotNull Unary_operContext ctx) {
        Type argT = null, retT = null;
        switch (ctx.getText()) {
            case "!":
                argT = AllTypes.BOOL;
                retT = AllTypes.BOOL;
                break;
            case "-":
                argT = AllTypes.INT;
                retT = AllTypes.INT;
                break;
            case "len":
                argT = new ArrayType(AllTypes.ANY);
                retT = AllTypes.INT;
                break;
            case "ord":
                argT = AllTypes.CHAR;
                retT = AllTypes.INT;
                break;
            case "chr":
                argT = AllTypes.INT;
                retT = AllTypes.CHAR;
                break;
        }
        ExprContext e = (ExprContext) ctx.getParent();
        Type t = visitExpr(e.expr(0));
        if (!t.equalsType(argT)) {
            addSemanticError(ctx, "Unary operator is not applicable for type " + t);
        }
        return retT;
    }

    @Override
    public Type visitPairParantheses(@NotNull PairParanthesesContext ctx) {
        Type lhs = visitExpr(ctx.expr(0));
        Type rhs = visitExpr(ctx.expr(1));
        return new PairType(lhs, rhs);
    }

    @Override
    public Type visitAssignment(@NotNull AssignmentContext ctx) {
        Assign_lhsContext lhs = ctx.assign_lhs();
        Assign_rhsContext rhs = ctx.assign_rhs();
        if (lhs == null || rhs == null) {
            return null;
        }
        Type lhsType = visitAssign_lhs(ctx.assign_lhs());
        Type rhsType = visitAssign_rhs(ctx.assign_rhs());

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
    public Type visitPrintlnExpr(@NotNull PrintlnExprContext ctx) {
        // Same as PrintExpr
        visitExpr(ctx.expr());
        return null;
    }

    @Override
    public Type visitBracketExpr(@NotNull BracketExprContext ctx) {
        return visitExpr(ctx.expr());
    }

    @Override
    public Type visitArray_liter(@NotNull Array_literContext ctx) {
        int expSize = ctx.expr().size();
        if (expSize == 0) {
            return new ArrayType(AllTypes.ANY);
        }
        ExprContext exp = ctx.expr(0);
        Type t = visitExpr(exp);
        for (ExprContext e : ctx.expr()) {
            if (!t.equalsType(visitExpr(e))) {
                addSemanticError(ctx, "Array values must all be of the same type");
            }
        }
        return new ArrayType(t);
    }

    @Override
    public Type visitReturnExpr(@NotNull ReturnExprContext ctx) {
        // Returning a type here to help test function semantics
        return visitChildren(ctx);
    }

    @Override
    public Type visitPair_elem_type(@NotNull Pair_elem_typeContext ctx) {
        if (ctx.PAIR() != null) {
            return AllTypes.NULL;
        }
        return visitChildren(ctx);
    }

    @Override
    public Type visitProg(@NotNull ProgContext ctx) {
        // Initialise global symbol table and current symbol table
        head = new SymbolTable<>();
        curr = head;

        for (FuncContext func : ctx.func()) {
            String funName = func.ident().getText();
            Type funType = visitType(func.type());
            Type[] paramList;
            // Need to check if a parameter list even exists
            if (func.param_list() != null) {
                List<ParamContext> parameters = func.param_list().param();
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

        Type exitType = visitChildren(ctx);
        if (exitType != null && exitType != AllTypes.ANY) {
            addSemanticError(ctx, "Cannot return from the main function");
        }
        if (listener.hasSyntaxErrors()) {
            printErrors(listener.getSyntaxErrors(), SYNTAX_ERROR_CODE);
        }
        if (this.hasSemanticErrors()) {
            printErrors(semanticErrors, SEMANTIC_ERROR_CODE);
        }
        return null;
    }

    @Override
    public Type visitSemicolonStat(@NotNull SemicolonStatContext ctx) {
        StatContext stat1 = ctx.stat(0);
        StatContext stat2 = ctx.stat(1);
        Type endType = visit(stat1);
        if (endType != null && stat2 != null && curr != head) {
            listener.addSyntaxError(ctx, "Function has not ended with a return or exit statement");
            return endType;
        }
        return visit(stat2);
    }

    @Override
    public Type visitPair_liter(@NotNull Pair_literContext ctx) {
        return AllTypes.NULL;
    }

    @Override
    public Type visitParam_list(@NotNull Param_listContext ctx) {
        visitChildren(ctx);
        return null;
    }

    @Override
    public Type visitArg_list(@NotNull Arg_listContext ctx) {
        visitChildren(ctx);
        return null;
    }

    @Override
    public Type visitFunc(@NotNull FuncContext ctx) {
        String funName = ctx.ident().getText();
        Type funType = visitType(ctx.type());

        String[] varNames;
        // Need to check if a parameter list even exists
        if (ctx.param_list() != null) {
            List<ParamContext> parameters = ctx.param_list().param();
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
    public Type visitBeginEnd(@NotNull BeginEndContext ctx) {
        // Initialise a new symbol table, with access to previous symbol table contents
        curr = new SymbolTable<>(curr);
        visitChildren(ctx);
        curr = curr.encSymbolTable;
        return null;
    }

    @Override
    public Type visitWhileExpr(@NotNull WhileExprContext ctx) {
        Type expected = visitExpr(ctx.expr());
        if (expected == null) {
            return null;
        }
        if (!expected.equalsType(AllTypes.BOOL)) {
            addSemanticError(ctx, "While condition must evaluate to a bool value");
        }
        curr = new SymbolTable<>(curr);
        StatContext stat = ctx.stat();
        if (stat == null) {
            return null;
        }
        visitChildren(stat);
        curr = curr.encSymbolTable;
        return null;
    }

    @Override
    public Type visitStr_liter(@NotNull Str_literContext ctx) {
        return AllTypes.STRING;
    }

    @Override
    public Type visitComment(@NotNull CommentContext ctx) {
        return null;
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
