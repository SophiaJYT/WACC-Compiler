package frontEnd;

import antlr.WaccParser.*;
import antlr.WaccParserBaseVisitor;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.ArrayList;
import java.util.List;

public class WaccVisitor extends WaccParserBaseVisitor<Type> {

    private final int SYNTAX_ERROR_CODE = 100, SEMANTIC_ERROR_CODE = 200;
    private final int ASCII_MAX_VALUE = 127;
    private SymbolTable<Type> st;

    private void error(String msg) {
        System.err.println(msg);
        System.exit(SEMANTIC_ERROR_CODE);
    }

    @Override
    public Type visitExitExpr(@NotNull ExitExprContext ctx) {
        System.out.println(ctx.getText());
        if (!visitExpr(ctx.expr()).equalsType(AllTypes.INT)) {
            error("Cannot exit with non-int value");
        }
        return visitChildren(ctx);
    }

    @Override
    public Type visitRead_lhs(@NotNull Read_lhsContext ctx) {
        System.out.println(ctx.getText());
        String var = ctx.assign_lhs().getText();
        Type type = st.lookUp(var);
        if (type == null) {
            error("Variable " + var + " has not been declared");
        }
        if (!(type.equalsType(AllTypes.CHAR) || type.equalsType(AllTypes.INT))) {
            error("Variable must be of type int or char");
        }
        return null;
    }

    @Override
    public Type visitArray_elem(@NotNull Array_elemContext ctx) {
        System.out.println(ctx.getText());
        Type type = st.lookUp(ctx.getText());
        if (type == null) {
            error("Array element doesn't exist");
        }
        for (ExprContext e : ctx.expr()) {
            if (!visitExpr(e).equalsType(AllTypes.INT)) {
                error("Must use an integer to access array element");
            }
        }
        return type;
    }

    @Override
    public Type visitAssign_lhs(@NotNull Assign_lhsContext ctx) {
        System.out.println(ctx.getText());
        Type type = st.lookUp(ctx.getText());
        if (type == null) {
            error("Variable doesn't exist");
        }
        return type;
    }

    @Override
    public Type visitIdent(@NotNull IdentContext ctx) {
        System.out.println(ctx.getText());
        return st.lookUp(ctx.getText());
    }

    @Override
    public Type visitAssign_rhs(@NotNull Assign_rhsContext ctx) {
        System.out.println(ctx.getText());
        return visitChildren(ctx);
    }

    @Override
    public Type visitFreeExpr(@NotNull FreeExprContext ctx) {
        System.out.println(ctx.getText());
        String var = ctx.expr().getText();
        Type type = st.lookUp(var);
        if (type == null) {
            error("Variable " + var + " has not been declared");
        }
        if (!(type instanceof ArrayType || type instanceof PairType)) {
            error("Variable must be a reference to an array or pair");
        }
        return null;
    }

    @Override
    public Type visitSkip(@NotNull SkipContext ctx) {
        System.out.println(ctx.getText());
        // Skip cannot be invalid semantically
        return visitChildren(ctx);
    }

    @Override
    public Type visitType(@NotNull TypeContext ctx) {
        System.out.println(ctx.getText());
        return visitChildren(ctx);
    }

    @Override
    public Type visitInt_liter(@NotNull Int_literContext ctx) {
        System.out.println(ctx.getText());
        long size = Long.parseLong(ctx.getText());
        if (size < Integer.MIN_VALUE || size > Integer.MAX_VALUE) {
            error("Integer value must be between -2^31 and 2^31 - 1");
        }
        return AllTypes.INT;
    }

    @Override
    public Type visitBase_type(@NotNull Base_typeContext ctx) {
        System.out.println(ctx.getText());
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
        System.out.println(ctx.getText());
        return null;
    }

    @Override
    public Type visitParam(@NotNull ParamContext ctx) {
        System.out.println(ctx.getText());
        String var = ctx.ident().getText();
        Type type = st.lookUp(var);
        if (type != null) {
            error("Variable " + var + " is already in use");
        }
        type = visitType(ctx.type());
        st.add(var, type);
        System.out.println(st);
        return visitChildren(ctx);
    }

    @Override
    public Type visitPair_type(@NotNull Pair_typeContext ctx) {
        System.out.println(ctx.getText());
        return new PairType(visitPair_elem_type(ctx.pair_elem_type(0)),
                visitPair_elem_type(ctx.pair_elem_type(1)));
    }

    @Override
    public Type visitChar_liter(@NotNull Char_literContext ctx) {
        System.out.println(ctx.getText());
        int c = ctx.getText().charAt(0);
        if (c > ASCII_MAX_VALUE) {
            error("Only ASCII printable characters allowed");
        }
        return AllTypes.CHAR;
    }

    @Override
    public Type visitInitialization(@NotNull InitializationContext ctx) {
        System.out.println(ctx.getText());
        String var = ctx.ident().getText();
        Type type = visitIdent(ctx.ident());
        if (type != null) {
            error("Variable " + var + " is already in use");
        }
        type = visitType(ctx.type());
        st.add(var, type);
        Type rhs = visitAssign_rhs(ctx.assign_rhs());
        if (!type.equalsType(rhs)) {
            error("Type " + type + " does not match type " + rhs);
        }
        return visitChildren(ctx);
    }

    @Override
    public Type visitIfExpr(@NotNull IfExprContext ctx) {
        System.out.println(ctx.getText());
        if (visitExpr(ctx.expr()) != AllTypes.BOOL) {
            error("If condition must evaluate to a bool value");
        }
        return visitChildren(ctx);
    }

    @Override
    public Type visitBinary_oper(@NotNull Binary_operContext ctx) {
        System.out.println(ctx.getText());
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
                retT = AllTypes.BOOL;
                break;
        }
        ExprContext e = (ExprContext) ctx.getParent();
        Type t1 = visitExpr(e.expr(0));
        Type t2 = visitExpr(e.expr(1));
        if (!t1.equalsType(t2)) {
            error("Types of both expression must be the same");
        }
        if (argTypes.isEmpty()) {
            return retT;
        }
        boolean typeMatch = false;
        for (Type t : argTypes) {
            if (t1.equalsType(t)) {
                typeMatch = true;
                break;
            }
        }
        if (!typeMatch) {
            error("Binary operator is not applicable for type " + t1);
        }
        return retT;
    }

    @Override
    public Type visitExpr(@NotNull ExprContext ctx) {
        System.out.println(ctx.getText());
        return visitChildren(ctx);
    }

    @Override
    public Type visitPair_elem(@NotNull Pair_elemContext ctx) {
        System.out.println(ctx.getText());
        String var = ctx.expr().getText();
        Type type = st.lookUp(var);
        if (type == null) {
            error("Expression " + var + " is either ");
        }
        //if (ctx.expr())
        return visitExpr(ctx.expr());
    }

    @Override
    public Type visitCallParantheses(@NotNull CallParanthesesContext ctx) {
        System.out.println(ctx.getText());
        // Need to check lengths of parameter lists of call function and function declaration
        String funName = ctx.ident().getText();
        Type type = st.lookUp("func:" + funName);
        if (type == null) {
            error("Function " + funName + " doesn't exist");
        }
        Type[] paramList = st.lookUpParam(funName);
        int numOfArgs = 0;
        // If arg list exists, update numOfArgs to size of arg list
        if (ctx.arg_list() != null) {
            numOfArgs = ctx.arg_list().expr().size();
        }
        if (paramList.length != numOfArgs) {
            error("Invalid number of arguments");
        }
        for (int i = 0; i < numOfArgs; i++) {
            Type callType = visitExpr(ctx.arg_list().expr(i));
            if (!callType.equalsType(paramList[i])) {
                error("Types don't match");
            }
        }
        System.out.println(type);
        return type;
    }

    @Override
    public Type visitArray_type(@NotNull Array_typeContext ctx) {
        System.out.println(ctx.getText());
        return new ArrayType(visitType(ctx.type()));
    }

    @Override
    public Type visitPrintExpr(@NotNull PrintExprContext ctx) {
        System.out.println(ctx.getText());
        return visitChildren(ctx);
    }

    @Override
    public Type visitBool_liter(@NotNull Bool_literContext ctx) {
        System.out.println(ctx.getText());
        return AllTypes.BOOL;
    }

    @Override
    public Type visitUnary_oper(@NotNull Unary_operContext ctx) {
        System.out.println(ctx.getText());
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
            error("Unary operator is not applicable for type " + t);
        }
        return retT;
    }

    @Override
    public Type visitPairParantheses(@NotNull PairParanthesesContext ctx) {
        System.out.println(ctx.getText());
        return new PairType(visitExpr(ctx.expr(0)), visitExpr(ctx.expr(1)));
    }

    @Override
    public Type visitAssignment(@NotNull AssignmentContext ctx) {
        System.out.println(ctx.getText());
        if (!visitAssign_lhs(ctx.assign_lhs()).equalsType(visitAssign_rhs(ctx.assign_rhs()))) {
            error("Left hand expression must have the same type as the right hand side");
        }
        return visitChildren(ctx);
    }

    @Override
    public Type visitPrintlnExpr(@NotNull PrintlnExprContext ctx) {
        System.out.println(ctx.getText());
        return visitChildren(ctx);
    }

    @Override
    public Type visitBracketExpr(@NotNull BracketExprContext ctx) {
        System.out.println(ctx.getText());
        return visitExpr(ctx.expr());
    }

    @Override
    public Type visitArray_liter(@NotNull Array_literContext ctx) {
        System.out.println(ctx.getText());
        ExprContext exp = ctx.expr().get(0);
        if (exp == null) {
            return new ArrayType(AllTypes.ANY);
        }
        Type t = visitExpr(exp);
        for (ExprContext e : ctx.expr()) {
            if (t != visitExpr(e)) {
                error("Array values must all be of the same type");
            }
        }
        return new ArrayType(t);
    }

    @Override
    public Type visitReturnExpr(@NotNull ReturnExprContext ctx) {
        // Check that statement is inside a function, by looking at the first element
        // of the symbol tables from the current table up to the global table and checking
        // that we have at least one first element different from 'begin'
        System.out.println(ctx.getText());
        return visitChildren(ctx);
    }

    @Override
    public Type visitPair_elem_type(@NotNull Pair_elem_typeContext ctx) {
        System.out.println(ctx.getText());
        return visitChildren(ctx);
    }

    @Override
    public Type visitProg(@NotNull ProgContext ctx) {
        System.out.println(ctx.getText());
        // Initialise global symbol table
        st = new SymbolTable<>();
        return visitChildren(ctx);
    }

    @Override
    public Type visitSemicolonStat(@NotNull SemicolonStatContext ctx) {
        System.out.println(ctx.getText());
        return visitChildren(ctx);
    }

    @Override
    public Type visitPair_liter(@NotNull Pair_literContext ctx) {
        System.out.println(ctx.getText());
        // Need to return a null type
        return null;
    }

    @Override
    public Type visitParam_list(@NotNull Param_listContext ctx) {
        System.out.println(ctx.getText());
        visitChildren(ctx);
        return null;
    }

    @Override
    public Type visitArg_list(@NotNull Arg_listContext ctx) {
        System.out.println(ctx.getText());
        visitChildren(ctx);
        return null;
    }

    @Override
    public Type visitFunc(@NotNull FuncContext ctx) {
        System.out.println(ctx.getText());
        String funName = ctx.ident().getText();
        Type funType = visitType(ctx.type());
        Type[] paramList;
        // Need to check if a parameter list even exists
        if (ctx.param_list() != null) {
            paramList = new Type[ctx.param_list().param().size()];
            int i = 0;
            for (ParamContext param : ctx.param_list().param()) {
                paramList[i] = visitParam(param);
                i++;
                System.out.println(i);
            }
        } else {
            paramList = new Type[0];
        }
        st.addFunction(funName, funType, paramList);

        SymbolTable<Type> old = st;
        st = st.funcTables.get(funName);

        StatContext stat = ctx.stat();
        while (stat.children.get(1).getText().equals(";")) {
            stat = (StatContext) stat.children.get(stat.children.size() - 1);
        }
        if (!stat.children.get(0).getText().equals("return")) {
            error("Function does not have a return statement");
        }
        ReturnExprContext returnStat = (ReturnExprContext) stat;
        if (!visitExpr(returnStat.expr()).equalsType(funType)) {
            error("Return type of '" + returnStat.expr().getText() + "' must match the function return type");
        }

        st = old;
        System.out.println(st);

        return visitChildren(ctx);
    }

    @Override
    public Type visitBeginEnd(@NotNull BeginEndContext ctx) {
        System.out.println(ctx.getText());
        // Initialise a new symbol table, with access to previous symbol table contents
        st = new SymbolTable<>(st);
        visitChildren(ctx);
        st = st.encSymbolTable;
        return null;
    }

    @Override
    public Type visitWhileExpr(@NotNull WhileExprContext ctx) {
        System.out.println(ctx.getText());
        // if (evalType(ctx.expr()) != Type.Bool) {
        //     error "Expression must evaluate to a bool value";
        // }
        if (visitExpr(ctx.expr()) != AllTypes.BOOL) {
            error("While condition must evaluate to a bool value");
        }
        return visitChildren(ctx);
    }

    @Override
    public Type visitStr_liter(@NotNull Str_literContext ctx) {
        System.out.println(ctx.getText());
        return AllTypes.STRING;
    }

    @Override
    public Type visitComment(@NotNull CommentContext ctx) {
        System.out.println(ctx.getText());
        // Skip
        return null;
    }

//    @Override
//    public Type visit(@NotNull ParseTree parseTree) {
//        return null;
//    }
//
//    @Override
//    public Type visitChildren(@NotNull RuleNode ruleNode) {
//        for (ruleNode)
//        return null;
//    }
//
//    @Override
//    public Type visitTerminal(@NotNull TerminalNode terminalNode) {
//        return null;
//    }
//
//    @Override
//    public Type visitErrorNode(@NotNull ErrorNode errorNode) {
//        return null;
//    }
}
