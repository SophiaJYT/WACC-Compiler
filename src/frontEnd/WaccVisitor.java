package frontEnd;

import antlr.WaccParser.*;
import antlr.WaccParserBaseVisitor;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.ArrayList;
import java.util.List;

public class WaccVisitor extends WaccParserBaseVisitor<Type> {

    private final int SYNTAX_ERROR_CODE = 100, SEMANTIC_ERROR_CODE = 200;
    private final int ASCII_MAX_VALUE = 127;
    private SymbolTable<Type> st;

    public String getLineError(ParserRuleContext ctx){
        return ctx.getStart().getLine() + ": " + ctx.getStart().getCharPositionInLine();
    }

    public void throwErrors(ParserRuleContext ctx, String throwErrorsMessage, int throwErrorsCode){
        System.err.println(throwErrorsMessage + "at line " + getLineError(ctx) + ".");
        System.err.print("Exitcode: " + throwErrorsCode + ".");
        System.exit(throwErrorsCode);
    }

    @Override
    public Type visitExitExpr(@NotNull ExitExprContext ctx) {
        if (!visitExpr(ctx.expr()).equalsType(AllTypes.INT)) {
            throwErrors(ctx, "Cannot exit with non-int value", SEMANTIC_ERROR_CODE);
        }
        return visitChildren(ctx);
    }

    @Override
    public Type visitRead_lhs(@NotNull Read_lhsContext ctx) {
        Type type = visitAssign_lhs(ctx.assign_lhs());
        if (type == null) {
            throwErrors(ctx, "Variable has not been declared", SEMANTIC_ERROR_CODE);
        }
        if (!(type.equalsType(AllTypes.CHAR) || type.equalsType(AllTypes.INT))) {
            throwErrors(ctx, "Variable must be of type int or char", SEMANTIC_ERROR_CODE);
        }
        return null;
    }

    @Override
    public Type visitArray_elem(@NotNull Array_elemContext ctx) {
        Type type = st.lookUp(ctx.getText());
        if (type == null) {
            throwErrors(ctx, "Array element doesn't exist", SEMANTIC_ERROR_CODE);
        }
        for (ExprContext e : ctx.expr()) {
            if (!visitExpr(e).equalsType(AllTypes.INT)) {
                throwErrors(ctx, "Must use an integer to access array element", SEMANTIC_ERROR_CODE);
            }
        }
        return type;
    }

    @Override
    public Type visitAssign_lhs(@NotNull Assign_lhsContext ctx) {
        Type type;
        if (ctx.pair_elem() != null) {
            type = visitPair_elem(ctx.pair_elem());
        } else {
            type = st.lookupAll(ctx.getText());
            if (type == null) {
                throwErrors(ctx, "Variable doesn't exist", SEMANTIC_ERROR_CODE);
            }
        }
        return type;
    }

    @Override
    public Type visitIdent(@NotNull IdentContext ctx) {
        return st.lookUp(ctx.getText());
    }

    @Override
    public Type visitAssign_rhs(@NotNull Assign_rhsContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public Type visitFreeExpr(@NotNull FreeExprContext ctx) {
        String var = ctx.expr().getText();
        Type type = st.lookUp(var);
        if (type == null) {
            throwErrors(ctx, "Variable " + var + " has not been declared", SEMANTIC_ERROR_CODE);
        }
        if (!(type instanceof ArrayType || type instanceof PairType)) {
            throwErrors(ctx, "Variable must be a reference to an array or pair", SEMANTIC_ERROR_CODE);
        }
        return null;
    }

    @Override
    public Type visitSkip(@NotNull SkipContext ctx) {
        // Skip cannot be invalid semantically
        return visitChildren(ctx);
    }

    @Override
    public Type visitType(@NotNull TypeContext ctx) {
        if (ctx.type() != null) {
            return visitType(ctx.type());
        }
        return visitChildren(ctx);
    }

    @Override
    public Type visitInt_liter(@NotNull Int_literContext ctx) {
        long size = Long.parseLong(ctx.getText());
        if (size < Integer.MIN_VALUE || size > Integer.MAX_VALUE) {
            throwErrors(ctx, "Integer value must be between -2^31 and 2^31 - 1", SYNTAX_ERROR_CODE);
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
        Type type = st.lookUp(var);
        if (type != null) {
            throwErrors(ctx, "Variable " + var + " is already in use", SEMANTIC_ERROR_CODE);
        }
        return visitType(ctx.type());
    }

    @Override
    public Type visitPair_type(@NotNull Pair_typeContext ctx) {
        return new PairType(visitPair_elem_type(ctx.pair_elem_type(0)),
                visitPair_elem_type(ctx.pair_elem_type(1)));
    }

    @Override
    public Type visitChar_liter(@NotNull Char_literContext ctx) {
        int c = ctx.getText().charAt(0);
        if (c > ASCII_MAX_VALUE) {
            throwErrors(ctx, "Only ASCII printable characters allowed", SYNTAX_ERROR_CODE);
        }
        return AllTypes.CHAR;
    }

    @Override
    public Type visitInitialization(@NotNull InitializationContext ctx) {
        String var = ctx.ident().getText();
        Type type = visitIdent(ctx.ident());
        if (type != null) {
            throwErrors(ctx, "Variable " + var + " is already in use", SEMANTIC_ERROR_CODE);
        }
        type = visitType(ctx.type());
        st.add(var, type);
        Type rhs = visitAssign_rhs(ctx.assign_rhs());
        if (rhs == AllTypes.ANY) {
            return type;
        }
        if (!type.equalsType(rhs)) {
            throwErrors(ctx, "Type " + type + " does not match type " + rhs, SEMANTIC_ERROR_CODE);
        }
        return visitChildren(ctx);
    }

    @Override
    public Type visitIfExpr(@NotNull IfExprContext ctx) {
        if (visitExpr(ctx.expr()) != AllTypes.BOOL) {
            throwErrors(ctx, "If condition must evaluate to a bool value", SEMANTIC_ERROR_CODE);
        }
        return visitChildren(ctx);
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
            case "&&":
            case "||":
                argTypes.add(AllTypes.BOOL);
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
            throwErrors(ctx, "Types of both expressions must be the same", SEMANTIC_ERROR_CODE);
        }
        boolean typeMatch = false;
        for (Type t : argTypes) {
            if (t1.equalsType(t)) {
                typeMatch = true;
                break;
            }
        }
        if (!typeMatch) {
            throwErrors(ctx, "Binary operator is not applicable for type " + t1, SEMANTIC_ERROR_CODE);
        }
        return retT;
    }

    @Override
    public Type visitExpr(@NotNull ExprContext ctx) {
        if (ctx.binary_oper() != null) {
            return visitBinary_oper(ctx.binary_oper());
        }
        return visitChildren(ctx);
    }

    @Override
    public Type visitPair_elem(@NotNull Pair_elemContext ctx) {
        String var = ctx.expr().getText();
        Type type = st.lookUp(var);
        if (type == null) {
            throwErrors(ctx, "Variable doesn't exist", SEMANTIC_ERROR_CODE);
        }
        PairType t = (PairType) type;
        type = (ctx.FIRST() != null) ? t.getLeft() : t.getRight();
        return type;
    }

    @Override
    public Type visitCallParantheses(@NotNull CallParanthesesContext ctx) {
        // Need to check lengths of parameter lists of call function and function declaration
        String funName = ctx.ident().getText();
        Type type = st.lookUp("func:" + funName);
        if (type == null) {
            throwErrors(ctx, "Function " + funName + " doesn't exist", SEMANTIC_ERROR_CODE);
        }
        Type[] paramList = st.lookUpParam(funName);
        int numOfArgs = 0;
        // If arg list exists, update numOfArgs to size of arg list
        if (ctx.arg_list() != null) {
            numOfArgs = ctx.arg_list().expr().size();
        }
        if (paramList.length != numOfArgs) {
            throwErrors(ctx, "Invalid number of arguments", SEMANTIC_ERROR_CODE);
        }
        for (int i = 0; i < numOfArgs; i++) {
            Type callType = visitExpr(ctx.arg_list().expr(i));
            if (!callType.equalsType(paramList[i])) {
                throwErrors(ctx, "Types don't match", SEMANTIC_ERROR_CODE);
            }
        }
        return type;
    }

    @Override
    public Type visitArray_type(@NotNull Array_typeContext ctx) {
        return new ArrayType(visitType(ctx.type()));
    }

    @Override
    public Type visitPrintExpr(@NotNull PrintExprContext ctx) {
        return visitChildren(ctx);
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
                argT = new ArrayType(null);
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
            throwErrors(ctx, "Unary operator is not applicable for type " + t, SEMANTIC_ERROR_CODE);
        }
        return retT;
    }

    @Override
    public Type visitPairParantheses(@NotNull PairParanthesesContext ctx) {
        return new PairType(visitExpr(ctx.expr(0)), visitExpr(ctx.expr(1)));
    }

    @Override
    public Type visitAssignment(@NotNull AssignmentContext ctx) {
        if (!visitAssign_lhs(ctx.assign_lhs()).equalsType(visitAssign_rhs(ctx.assign_rhs()))) {
            throwErrors(ctx, "Left hand expression must have the same type as the right hand side", SEMANTIC_ERROR_CODE);
        }
        return visitChildren(ctx);
    }

    @Override
    public Type visitPrintlnExpr(@NotNull PrintlnExprContext ctx) {
        return visitChildren(ctx);
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
        ExprContext exp = ctx.expr().get(0);
        Type t = visitExpr(exp);
        for (ExprContext e : ctx.expr()) {
            if (t != visitExpr(e)) {
                throwErrors(ctx, "Array values must all be of the same type", SEMANTIC_ERROR_CODE);
            }
        }
        return t;
    }

    @Override
    public Type visitReturnExpr(@NotNull ReturnExprContext ctx) {
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
        // Initialise global symbol table
        st = new SymbolTable<>();
        return visitChildren(ctx);
    }

    @Override
    public Type visitSemicolonStat(@NotNull SemicolonStatContext ctx) {
        return visitChildren(ctx);
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

        Type[] paramList;
        String[] varNames = null;
        // Need to check if a parameter list even exists
        if (ctx.param_list() != null) {
            List<ParamContext> parameters = ctx.param_list().param();
            paramList = new Type[parameters.size()];
            varNames = new String[parameters.size()];
            int i = 0;
            for (ParamContext param : parameters) {
                paramList[i] = visitParam(param);
                varNames[i] = param.ident().getText();
                i++;
            }
        } else {
            paramList = new Type[0];
        }


        st.addFunction(funName, funType, paramList);

        SymbolTable<Type> old = st;
        st = st.lookUpFunc(funName);

        if (varNames == null) {
            for (int i = 0; i < paramList.length; i++) {
                st.add(varNames[i], paramList[i]);
            }
        }

        StatContext stat = ctx.stat();
        visitChildren(stat);

        while (stat.children.get(1).getText().equals(";")) {
            stat = (StatContext) stat.children.get(stat.children.size() - 1);
        }
        if (!stat.children.get(0).getText().equals("return")) {
            throwErrors(ctx, "Function does not have a return statement", SEMANTIC_ERROR_CODE);
        }
        ReturnExprContext returnStat = (ReturnExprContext) stat;
        if (!visitExpr(returnStat.expr()).equalsType(funType)) {
            throwErrors(ctx, "Return type of '" + returnStat.expr().getText() + "' must match the function return type", SEMANTIC_ERROR_CODE);
        }

        st = old;

        return null;
    }

    @Override
    public Type visitBeginEnd(@NotNull BeginEndContext ctx) {
        // Initialise a new symbol table, with access to previous symbol table contents
        st = new SymbolTable<>(st);
        visitChildren(ctx);
        st = st.encSymbolTable;
        return null;
    }

    @Override
    public Type visitWhileExpr(@NotNull WhileExprContext ctx) {
        // if (evalType(ctx.expr()) != Type.Bool) {
        //     throwErrors "Expression must evaluate to a bool value";
        // }
        if (visitExpr(ctx.expr()) != AllTypes.BOOL) {
            throwErrors(ctx, "While condition must evaluate to a bool value", SEMANTIC_ERROR_CODE);
        }
        return visitChildren(ctx);
    }

    @Override
    public Type visitStr_liter(@NotNull Str_literContext ctx) {
        return AllTypes.STRING;
    }

    @Override
    public Type visitComment(@NotNull CommentContext ctx) {
        return null;
    }

}
