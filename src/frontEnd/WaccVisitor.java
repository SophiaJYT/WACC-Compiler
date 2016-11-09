package frontEnd;

import antlr.WaccParser;
import antlr.WaccParserBaseVisitor;
import org.antlr.v4.runtime.misc.NotNull;

public class WaccVisitor<T> extends WaccParserBaseVisitor<T> {

    @Override
    public T visitExitExpr(@NotNull WaccParser.ExitExprContext ctx) {
        // if (!(ctx.expr() instanceof Type.INT)) {
        //     error "Semantic Error: Cannot exit with non-int value";
        // }
        return null;
    }

    @Override
    public T visitRead_lhs(@NotNull WaccParser.Read_lhsContext ctx) {
        // type = lookup(ctx.assign_lhs().ident());
        // if (!(type instanceof Type.INT || type instanceof Type.CHAR)) {
        //     error "Variable must be of type int or char";
        // }

        return null;
    }

    @Override
    public T visitArray_elem(@NotNull WaccParser.Array_elemContext ctx) {
        // type = lookup(ctx.ident());
        // if (type == null) {
        //     error "Variable doesn't exist";
        // }
        // if (!(ctx.expr() instanceof Type.INT)) {
        //     error "Must use an integer to access array element"
        // }
        return null;
    }

    @Override
    public T visitAssign_lhs(@NotNull WaccParser.Assign_lhsContext ctx) {
        // type = lookup(ctx.ident());
        // if (type == null) {
        //     error "Variable doesn't exist";
        // }
        return null;
    }

    @Override
    public T visitIdent(@NotNull WaccParser.IdentContext ctx) {
        // Skip
        return null;
    }

    @Override
    public T visitAssign_rhs(@NotNull WaccParser.Assign_rhsContext ctx) {
        // Need to think about
        return null;
    }

    @Override
    public T visitFreeExpr(@NotNull WaccParser.FreeExprContext ctx) {
//        type = lookup(ctx.expr().ident())
//        if (type == null) {
//            error "Variable doesn't exist";
//        }
//        if (!(type instanceof Type.Pair || type instanceof Type.Array)) {
//            error "Variable must be a reference to an array or pair";
//        }
        return null;
    }

    @Override
    public T visitSkip(@NotNull WaccParser.SkipContext ctx) {
        // Skip cannot be invalid semantically
        return null;
    }

    @Override
    public T visitType(@NotNull WaccParser.TypeContext ctx) {
        // Nothing to check? (Maybe, Hopefully)
        return null;
    }

    @Override
    public T visitInt_liter(@NotNull WaccParser.Int_literContext ctx) {
        // int size = getIntLiteral(ctx);
        // if (size < Integer.MIN_VALUE || size > Integer.MAX_VALUE) {
        //     error "Int value is too large";
        // }
        // return Type.Int;
        return null;
    }

    @Override
    public T visitBase_type(@NotNull WaccParser.Base_typeContext ctx) {
        // Nothing to check? (Maybe, Hopefully)
        return null;
    }

    @Override
    public T visitParam(@NotNull WaccParser.ParamContext ctx) {
        // type = lookup(ctx.ident());
        // if (type != null) {
        //     error "Variable already in use";
        // }
        // symbolTable.put(ctx.ident(), ctx.type());
        return null;
    }

    @Override
    public T visitPair_type(@NotNull WaccParser.Pair_typeContext ctx) {
        visitChildren(ctx);
        return null;
    }

    @Override
    public T visitChar_liter(@NotNull WaccParser.Char_literContext ctx) {
        // Nothing to check? (Maybe, Hopefully)
        // return Type.Char;
        return null;
    }

    @Override
    public T visitInitialization(@NotNull WaccParser.InitializationContext ctx) {
        // type = lookup(ctx.ident());
        // if (type != null) {
        //     error "Variable already in use";
        // }
        // symbolTable.put(ctx.ident(), ctx.type());
        // visitAssign_rhs(ctx.assign_rhs());
        return null;
    }

    @Override
    public T visitIfExpr(@NotNull WaccParser.IfExprContext ctx) {
        // if (evalType(ctx.expr()) != Type.Bool) {
        //     error "Expression must evaluate to a bool value";
        // }
        // visitChildren(ctx);
        return null;
    }

    @Override
    public T visitBinary_oper(@NotNull WaccParser.Binary_operContext ctx) {
        // We need to check the expression types on both sides of the binary expression.
        // Also check that binary operator is valid for those types.
        return null;
    }

    @Override
    public T visitExpr(@NotNull WaccParser.ExprContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public T visitCallParantheses(@NotNull WaccParser.CallParanthesesContext ctx) {
        return null;
    }

    @Override
    public T visitArray_type(@NotNull WaccParser.Array_typeContext ctx) {
        // Nothing to check? (Maybe, Hopefully)
        return null;
    }

    @Override
    public T visitPrintExpr(@NotNull WaccParser.PrintExprContext ctx) {
        visitExpr(ctx.expr());
        return null;
    }

    @Override
    public T visitBool_liter(@NotNull WaccParser.Bool_literContext ctx) {
        // return Type.Bool;
        return null;
    }

    @Override
    public T visitUnary_oper(@NotNull WaccParser.Unary_operContext ctx) {
        // Need to check type of unary operator matches expression type.
        return null;
    }

    @Override
    public T visitPairParantheses(@NotNull WaccParser.PairParanthesesContext ctx) {
        // return Type.Pair;
        visitChildren(ctx);
        return null;
    }

    @Override
    public T visitAssignment(@NotNull WaccParser.AssignmentContext ctx) {
        visitAssign_rhs(ctx.assign_rhs());
        return null;
    }

    @Override
    public T visitPrintlnExpr(@NotNull WaccParser.PrintlnExprContext ctx) {
        visitExpr(ctx.expr());
        return null;
    }

    @Override
    public T visitBracketExpr(@NotNull WaccParser.BracketExprContext ctx) {
        visitExpr(ctx.expr());
        return null;
    }

    @Override
    public T visitArray_liter(@NotNull WaccParser.Array_literContext ctx) {
        // type t = visitExpr(ctx.getChild(0));
        // for (WaccParser.ExprContext c : ctx.expr()) {
        //     if (t != visitExpr(c)) {
        //         error "Values should all be of the same type";
        //     }
        // }
        // return Type.Array;
        return null;
    }

    @Override
    public T visitReturnExpr(@NotNull WaccParser.ReturnExprContext ctx) {
        // Check that statement is inside a function, by looking at the first element
        // of the symbol tables from the current table up to the global table and checking
        // that we have at least one first element different from 'begin'
        return null;
    }

    @Override
    public T visitPair_elem_type(@NotNull WaccParser.Pair_elem_typeContext ctx) {
        visitChildren(ctx);
        return null;
    }

    @Override
    public T visitProg(@NotNull WaccParser.ProgContext ctx) {
        // Initialise global symbol table
        visitChildren(ctx);
        return null;
    }

    @Override
    public T visitSemicolonStat(@NotNull WaccParser.SemicolonStatContext ctx) {
        visitChildren(ctx);
        return null;
    }

    @Override
    public T visitPair_liter(@NotNull WaccParser.Pair_literContext ctx) {
        // Can skip
        return null;
    }

    @Override
    public T visitParam_list(@NotNull WaccParser.Param_listContext ctx) {
        visitChildren(ctx);
        return null;
    }

    @Override
    public T visitArg_list(@NotNull WaccParser.Arg_listContext ctx) {
        visitChildren(ctx);
        return null;
    }

    @Override
    public T visitPairSecondExpr(@NotNull WaccParser.PairSecondExprContext ctx) {
        visitChildren(ctx);
        return null;
    }

    @Override
    public T visitFunc(@NotNull WaccParser.FuncContext ctx) {
        // If no return statement in function, print a SYNTAX ERROR.
        // Need to check types of return expression and type in func declaration
        // Function identifier name must be unique (check with symbol table)
        // Initialise a new symbol table containing parameter variables and types
        visitChildren(ctx);
        return null;
    }

    @Override
    public T visitBeginEnd(@NotNull WaccParser.BeginEndContext ctx) {
        // Initialise a new symbol table, with access to previous symbol table contents
        visitChildren(ctx);
        return null;
    }

    @Override
    public T visitWhileExpr(@NotNull WaccParser.WhileExprContext ctx) {
        // if (evalType(ctx.expr()) != Type.Bool) {
        //     error "Expression must evaluate to a bool value";
        // }
        visitChildren(ctx);
        return null;
    }


    @Override
    public T visitPairFirstExpr(@NotNull WaccParser.PairFirstExprContext ctx) {
        visitChildren(ctx);
        return null;
    }

    @Override
    public T visitStr_liter(@NotNull WaccParser.Str_literContext ctx) {
        // return Type.String
        return null;
    }

    @Override
    public T visitComment(@NotNull WaccParser.CommentContext ctx) {
        // Skip
        return null;
    }

//    @Override
//    public T visit(@NotNull ParseTree parseTree) {
//        return null;
//    }
//
//    @Override
//    public T visitChildren(@NotNull RuleNode ruleNode) {
//        return null;
//    }
//
//    @Override
//    public T visitTerminal(@NotNull TerminalNode terminalNode) {
//        return null;
//    }
//
//    @Override
//    public T visitErrorNode(@NotNull ErrorNode errorNode) {
//        return null;
//    }
}
