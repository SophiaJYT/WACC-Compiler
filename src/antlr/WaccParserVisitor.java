// Generated from ./WaccParser.g4 by ANTLR 4.4
package antlr;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link WaccParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface WaccParserVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link WaccParser#array_elem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArray_elem(@NotNull WaccParser.Array_elemContext ctx);
	/**
	 * Visit a parse tree produced by {@link WaccParser#assign_lhs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssign_lhs(@NotNull WaccParser.Assign_lhsContext ctx);
	/**
	 * Visit a parse tree produced by {@link WaccParser#ident}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdent(@NotNull WaccParser.IdentContext ctx);
	/**
	 * Visit a parse tree produced by {@link WaccParser#assign_rhs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssign_rhs(@NotNull WaccParser.Assign_rhsContext ctx);
	/**
	 * Visit a parse tree produced by {@link WaccParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType(@NotNull WaccParser.TypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link WaccParser#int_liter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInt_liter(@NotNull WaccParser.Int_literContext ctx);
	/**
	 * Visit a parse tree produced by {@link WaccParser#base_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBase_type(@NotNull WaccParser.Base_typeContext ctx);
	/**
	 * Visit a parse tree produced by {@link WaccParser#param}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParam(@NotNull WaccParser.ParamContext ctx);
	/**
	 * Visit a parse tree produced by {@link WaccParser#pair_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPair_type(@NotNull WaccParser.Pair_typeContext ctx);
	/**
	 * Visit a parse tree produced by {@link WaccParser#char_liter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitChar_liter(@NotNull WaccParser.Char_literContext ctx);
	/**
	 * Visit a parse tree produced by {@link WaccParser#binary_oper}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBinary_oper(@NotNull WaccParser.Binary_operContext ctx);
	/**
	 * Visit a parse tree produced by {@link WaccParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr(@NotNull WaccParser.ExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link WaccParser#array_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArray_type(@NotNull WaccParser.Array_typeContext ctx);
	/**
	 * Visit a parse tree produced by {@link WaccParser#stat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStat(@NotNull WaccParser.StatContext ctx);
	/**
	 * Visit a parse tree produced by {@link WaccParser#bool_liter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBool_liter(@NotNull WaccParser.Bool_literContext ctx);
	/**
	 * Visit a parse tree produced by {@link WaccParser#unary_oper}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnary_oper(@NotNull WaccParser.Unary_operContext ctx);
	/**
	 * Visit a parse tree produced by {@link WaccParser#array_liter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArray_liter(@NotNull WaccParser.Array_literContext ctx);
	/**
	 * Visit a parse tree produced by {@link WaccParser#pair_elem_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPair_elem_type(@NotNull WaccParser.Pair_elem_typeContext ctx);
	/**
	 * Visit a parse tree produced by {@link WaccParser#prog}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProg(@NotNull WaccParser.ProgContext ctx);
	/**
	 * Visit a parse tree produced by {@link WaccParser#pair_liter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPair_liter(@NotNull WaccParser.Pair_literContext ctx);
	/**
	 * Visit a parse tree produced by {@link WaccParser#param_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParam_list(@NotNull WaccParser.Param_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link WaccParser#arg_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArg_list(@NotNull WaccParser.Arg_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link WaccParser#func}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunc(@NotNull WaccParser.FuncContext ctx);
	/**
	 * Visit a parse tree produced by {@link WaccParser#pair_elem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPair_elem(@NotNull WaccParser.Pair_elemContext ctx);
	/**
	 * Visit a parse tree produced by {@link WaccParser#str_liter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStr_liter(@NotNull WaccParser.Str_literContext ctx);
	/**
	 * Visit a parse tree produced by {@link WaccParser#comment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComment(@NotNull WaccParser.CommentContext ctx);
}