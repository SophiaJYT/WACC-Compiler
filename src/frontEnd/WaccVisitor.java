package frontEnd;

import antlr.WaccParser.*;
import antlr.WaccParserBaseVisitor;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.ArrayList;
import java.util.List;

public class WaccVisitor extends WaccParserBaseVisitor<Type> {

    private final int SYNTAX_ERROR_CODE = 100, SEMANTIC_ERROR_CODE = 200;
    private SymbolTable<Type> st;

    private void error(String msg) {
        System.err.println(msg);
        System.exit(SEMANTIC_ERROR_CODE);
    }

    @Override
    public Type visitExitExpr(@NotNull ExitExprContext ctx) {
        // if (!(ctx.expr() instanceof Type.INT)) {
        //     error "Semantic Error: Cannot exit with non-int value";
        // }
        if (!visitExpr(ctx.expr()).equalsType(AllTypes.INT)) {
            error("Cannot exit with non-int value");
        }
        return visitChildren(ctx);
    }

    @Override
    public Type visitRead_lhs(@NotNull Read_lhsContext ctx) {
        // type = lookup(ctx.assign_lhs().ident());
        // if (!(type instanceof Type.INT || type instanceof Type.CHAR)) {
        //     error "Variable must be of type int or char";
        // }
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
        // type = lookup(ctx.ident());
        // if (type == null) {
        //     error "Variable doesn't exist";
        // }
        // if (!(ctx.expr() instanceof Type.INT)) {
        //     error "Must use an integer to access array element"
        // }
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
        // type = lookup(ctx.ident());
        // if (type == null) {
        //     error "Variable doesn't exist";
        // }
        Type type = st.lookUp(ctx.getText());
        if (type == null) {
            error("Variable doesn't exist");
        }
        return null;
    }

    @Override
    public Type visitIdent(@NotNull IdentContext ctx) {
        // Skip
        return null;
    }

    @Override
    public Type visitAssign_rhs(@NotNull Assign_rhsContext ctx) {
        // Need to think about
        return null;
    }

    @Override
    public Type visitFreeExpr(@NotNull FreeExprContext ctx) {
        // type = lookup(ctx.expr().ident())
        // if (type == null) {
        //     error "Variable doesn't exist";
        // }
        // if (!(type instanceof Type.Pair || type instanceof Type.Array)) {
        //     error "Variable must be a reference to an array or pair";
        // }
        String var = ctx.expr().getText();
        Type type = st.lookUp(var);
        if (type == null) {
            error("Variable " + var + " has not been declared");
        }
        if (!(type.equalsType(AllTypes.PAIR) || type.equalsType(AllTypes.ARRAY))) {
            error("Variable must be a reference to an array or pair");
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
        // Nothing to check? (Maybe, Hopefully)
        return null;
    }

    @Override
    public Type visitInt_liter(@NotNull Int_literContext ctx) {
        // int size = getIntLiteral(ctx);
        // if (size < Integer.MIN_VALUE || size > Integer.MAX_VALUE) {
        //     error "Int value is too large";
        // }
        // return Type.Int;
        long size = Long.parseLong(ctx.getText());
        if (size < Integer.MIN_VALUE || size > Integer.MAX_VALUE) {
            error("Integer value is too large");
        }
        return AllTypes.INT;
    }

    @Override
    public Type visitBase_type(@NotNull Base_typeContext ctx) {
        // Nothing to check? (Maybe, Hopefully)
        return null;
    }

    @Override
    public Type visitParam(@NotNull ParamContext ctx) {
        // type = lookup(ctx.ident());
        // if (type != null) {
        //     error "Variable already in use";
        // }
        // symbolTable.put(ctx.ident(), ctx.type());
        return null;
    }

    @Override
    public Type visitPair_type(@NotNull Pair_typeContext ctx) {
        visitChildren(ctx);
        return null;
    }

    @Override
    public Type visitChar_liter(@NotNull Char_literContext ctx) {
        // Nothing to check? (Maybe, Hopefully)
        // return Type.Char;
        return AllTypes.CHAR;
    }

    @Override
    public Type visitInitialization(@NotNull InitializationContext ctx) {
        // type = lookup(ctx.ident());
        // if (type != null) {
        //     error "Variable already in use";
        // }
        // symbolTable.put(ctx.ident(), ctx.type());
        // visitAssign_rhs(ctx.assign_rhs());
        return null;
    }

    @Override
    public Type visitIfExpr(@NotNull IfExprContext ctx) {
        // if (evalType(ctx.expr()) != Type.Bool) {
        //     error "Expression must evaluate to a bool value";
        // }
        // visitChildren(ctx);
        if (visitExpr(ctx.expr()) != AllTypes.BOOLEAN) {
            error("If condition must evaluate to a bool value");
        }
        visitChildren(ctx);
        return null;
    }

    @Override
    public Type visitBinary_oper(@NotNull Binary_operContext ctx) {
        // We need to check the expression types on both sides of the binary expression.
        // Also check that binary operator is valid for those types.
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
    public Type visitCallParantheses(@NotNull CallParanthesesContext ctx) {
        return null;
    }

    @Override
    public Type visitArray_type(@NotNull Array_typeContext ctx) {
        // Nothing to check? (Maybe, Hopefully)
        return null;
    }

    @Override
    public Type visitPrintExpr(@NotNull PrintExprContext ctx) {
        return visitExpr(ctx.expr());
    }

    @Override
    public Type visitBool_liter(@NotNull Bool_literContext ctx) {
        // return Type.Bool;
        return AllTypes.BOOLEAN;
    }

    @Override
    public Type visitUnary_oper(@NotNull Unary_operContext ctx) {
        // Need to check type of unary operator matches expression type.
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
                argT = AllTypes.ARRAY;
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
        // return Type.Pair;
        visitChildren(ctx);
        return AllTypes.PAIR;
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
        return visitChildren(ctx);
    }

    @Override
    public Type visitArray_liter(@NotNull Array_literContext ctx) {
        // type t = visitExpr(ctx.getChild(0));
        // for (ExprContext c : ctx.expr()) {
        //     if (t != visitExpr(c)) {
        //         error "Values should all be of the same type";
        //     }
        // }
        // return Type.Array;
        Type t = visitExpr(ctx.expr().get(0));
        for (ExprContext e : ctx.expr()) {
            if (t != visitExpr(e)) {
                error("Array values must all be of the same type");
            }
        }
        return AllTypes.ARRAY;
    }

    @Override
    public Type visitReturnExpr(@NotNull ReturnExprContext ctx) {
        // Check that statement is inside a function, by looking at the first element
        // of the symbol tables from the current table up to the global table and checking
        // that we have at least one first element different from 'begin'
        return visitExpr(ctx.expr());
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
    public Type visitPairSecondExpr(@NotNull PairSecondExprContext ctx) {
        visitChildren(ctx);
        return null;
    }

    @Override
    public Type visitFunc(@NotNull FuncContext ctx) {
        // If no return statement in function, print a SYNTAX ERROR.
        // Need to check types of return expression and type in func declaration
        // Function identifier name must be unique (check with symbol table)
        // Initialise a new symbol table containing parameter variables and types
        visitChildren(ctx);
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
        //     error "Expression must evaluate to a bool value";
        // }
        if (visitExpr(ctx.expr()) != AllTypes.BOOLEAN) {
            error("While condition must evaluate to a bool value");
        }
        return visitChildren(ctx);
    }


    @Override
    public Type visitPairFirstExpr(@NotNull PairFirstExprContext ctx) {
        visitChildren(ctx);
        return null;
    }

    @Override
    public Type visitStr_liter(@NotNull Str_literContext ctx) {
        // return Type.String
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
//    @Override
//    public Type visitChildren(@NotNull RuleNode ruleNode) {
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
