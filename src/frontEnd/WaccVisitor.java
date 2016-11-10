package frontEnd;

import antlr.WaccParser;
import antlr.WaccParser.*;
import antlr.WaccParserBaseVisitor;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;

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
        if (!visitExpr(ctx.expr()).equalsType(AllTypes.INT)) {
            error("Cannot exit with non-int value");
        }
        return visitChildren(ctx);
    }

    @Override
    public Type visitRead_lhs(@NotNull Read_lhsContext ctx) {
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
        Type type = st.lookUp(ctx.getText());
        if (type == null) {
            error("Variable doesn't exist");
        }
        return type;
    }

    @Override
    public Type visitIdent(@NotNull IdentContext ctx) {
        return st.lookUp(ctx.getText());
    }

    @Override
    public Type visitAssign_rhs(@NotNull Assign_rhsContext ctx) {
        // if (ctx is expression) {
        //      return visitExpr(expression);
        // }
        // if (ctx is array_liter) {
        //      ArrayLiterContext liter = (ArrayLiterContext) ctx;
        //      if (!liter.expr().isEmpty()) {
        //          return new ArrayType(visitExpr(liter.expr().getChild(0));
        //      }
        //      return null;
        // if (ctx is pair) {
        //      PairParantheses pair = (PairParantheses) ctx;
        //      return visitPairParantheses(pair);
        // if (ctx is pair_elem) {
        //      Pair_elemtype pt = (Pair_elemtype) ctx;
        //      return visitPair_elemType(pt);

        return null;
    }

    @Override
    public Type visitFreeExpr(@NotNull FreeExprContext ctx) {
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
        // Skip cannot be invalid semantically
        return visitChildren(ctx);
    }

    @Override
    public Type visitType(@NotNull TypeContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public Type visitInt_liter(@NotNull Int_literContext ctx) {
        long size = Long.parseLong(ctx.getText());
        if (size < Integer.MIN_VALUE || size > Integer.MAX_VALUE) {
            error("Integer value must be between -2^31 and 2^31 - 1");
        }
        return AllTypes.INT;
    }

    @Override
    public Type visitBase_type(@NotNull Base_typeContext ctx) {
        switch (ctx.getText()) {
            case "int":
                return AllTypes.INT;
            case "bool":
                return AllTypes.BOOLEAN;
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
            error("Variable " + var + " is already in use");
        }
        st.add(var, type);
        return visitChildren(ctx);
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
            error("Only ASCII printable characters allowed");
        }
        return AllTypes.CHAR;
    }

    @Override
    public Type visitInitialization(@NotNull InitializationContext ctx) {
        String var = ctx.ident().getText();
        Type type = visitIdent(ctx.ident());
        if (type != null) {
            error("Variable " + var + " is already in use");
        }
        type = visitType(ctx.type());
        st.add(var, type);
        Type rhs = visitAssign_rhs(ctx.assign_rhs());
        if (!type.equalsType(rhs)) {
            error("Type " + type + " does not match type" + rhs);
        }
        return visitChildren(ctx);
    }

    @Override
    public Type visitIfExpr(@NotNull IfExprContext ctx) {
        if (visitExpr(ctx.expr()) != AllTypes.BOOLEAN) {
            error("If condition must evaluate to a bool value");
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
                retT = AllTypes.BOOLEAN;
                break;
            case "&&":
            case "||":
                argTypes.add(AllTypes.BOOLEAN);
                retT = AllTypes.BOOLEAN;
                break;
            case "==":
            case "!=":
                retT = AllTypes.BOOLEAN;
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
        return visitChildren(ctx);
    }

    @Override
    public Type visitPair_elem(@NotNull Pair_elemContext ctx) {
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
        // Need to check lengths of parameter lists of call function and function declaration
        String funName = ctx.ident().getText();
        Type type = st.lookUp(funName);
        if (type == null) {
            error("Function " + funName + " doesn't exist");
        }
        Type[] paramList = st.lookUpParam(funName);
        int i = 0;
        if(paramList.length != ctx.arg_list().expr().size()) {
            error("Invalid number of arguments");
        }
        for (ExprContext e : ctx.arg_list().expr()) {
            Type callType = visitExpr(e);
            if(callType != paramList[i]) {
                error("Types don't match");
            } else {
                i++;
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
        return AllTypes.BOOLEAN;
    }

    @Override
    public Type visitUnary_oper(@NotNull Unary_operContext ctx) {
        Type argT = null, retT = null;
        switch (ctx.getText()) {
            case "!":
                argT = AllTypes.BOOLEAN;
                retT = AllTypes.BOOLEAN;
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
        return new PairType(visitExpr(ctx.expr(0)), visitExpr(ctx.expr(1)));
    }

    @Override
    public Type visitAssignment(@NotNull AssignmentContext ctx) {
        if (!visitAssign_lhs(ctx.assign_lhs()).equalsType(visitAssign_rhs(ctx.assign_rhs()))) {
            error("Left hand expression must have the same type as the right hand side");
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
        Type t = visitExpr(ctx.expr().get(0));
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
        return visitChildren(ctx);
    }

    @Override
    public Type visitPair_elem_type(@NotNull Pair_elem_typeContext ctx) {
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
        // Need to return a null type
        return null;
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
        Type[] paramList = new Type[ctx.param_list().param().size()];
        int i = 0;
        for (ParamContext param : ctx.param_list().param()) {
            paramList[i] = visitParam(param);
            i++;
        }
        String funName = ctx.ident().getText();
        Type funType = visitType(ctx.type());
        st.addFunction(funName, funType, paramList);
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
        return visitChildren(ctx);
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
        //     error "Expression must evaluate to a bool value";
        // }
        if (visitExpr(ctx.expr()) != AllTypes.BOOLEAN) {
            error("While condition must evaluate to a bool value");
        }
        return visitChildren(ctx);
    }

    @Override
    public Type visitStr_liter(@NotNull Str_literContext ctx) {
        return AllTypes.STRING;
    }

    @Override
    public Type visitComment(@NotNull CommentContext ctx) {
        // Skip
        return null;
    }

//    @Override
//    public Type visit(@NotNull ParseTree parseTree) {
//        return null;
//    }
//
    @Override
    public Type visitChildren(@NotNull RuleNode ruleNode) {
        for (ruleNode)
        return null;
    }
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
