// Generated from ./WaccParser.g4 by ANTLR 4.4
package antlr;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class WaccParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.4", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		PRINT=8, GREATER_THAN_OR_EQUAL=31, NEWPAIR=42, DO=15, CHR=22, MINUS=28, 
		WHITESPACE=56, GREATER_THAN=29, ELSE=12, LESS_THAN_OR_EQUAL=32, IF=10, 
		CLOSE_SQUARE_BRACKET=49, INTEGER=57, DONE=16, NULL=45, CHARACTER_LITERAL=58, 
		TRUE=52, IS=1, LESS_THAN=30, READ=4, NOT=19, AND=35, THEN=11, END=18, 
		MULTIPLY=24, SEMI_COLON=50, OPEN_SQUARE_BRACKET=48, EXIT=7, PLUS=27, CLOSE_PARENTHESES=47, 
		ORD=21, CALL=2, FI=13, PRINTLN=9, LENGTH=20, OPEN_PARENTHESES=46, CHAR=39, 
		BEGIN=17, ASSIGN=23, FREE=5, INT=37, COMMENT=55, DIVIDE=25, RETURN=6, 
		SKIP=3, IDENTIFIER=54, COMMA=51, MOD=26, OR=36, EQUAL=33, FIRST=43, BOOL=38, 
		NOT_EQUAL=34, STRING=40, WHILE=14, SECOND=44, FALSE=53, STRING_LITERAL=59, 
		PAIR=41;
	public static final String[] tokenNames = {
		"<INVALID>", "'is'", "'call'", "'skip'", "'read'", "'free'", "'return'", 
		"'exit'", "'print'", "'println'", "'if'", "'then'", "'else'", "'fi'", 
		"'while'", "'do'", "'done'", "'begin'", "'end'", "'!'", "'len'", "'ord'", 
		"'chr'", "'='", "'*'", "'/'", "'%'", "'+'", "'-'", "'>'", "'<'", "'>='", 
		"'<='", "'=='", "'!='", "'&&'", "'||'", "'int'", "'bool'", "'char'", "'string'", 
		"'pair'", "'newpair'", "'fst'", "'snd'", "'null'", "'('", "')'", "'['", 
		"']'", "';'", "','", "'true'", "'false'", "IDENTIFIER", "COMMENT", "WHITESPACE", 
		"INTEGER", "CHARACTER_LITERAL", "STRING_LITERAL"
	};
	public static final int
		RULE_prog = 0, RULE_func = 1, RULE_param_list = 2, RULE_param = 3, RULE_stat = 4, 
		RULE_assign_lhs = 5, RULE_assign_rhs = 6, RULE_arg_list = 7, RULE_pair_elem = 8, 
		RULE_type = 9, RULE_base_type = 10, RULE_array_type = 11, RULE_pair_type = 12, 
		RULE_pair_elem_type = 13, RULE_expr = 14, RULE_unary_oper = 15, RULE_binary_oper = 16, 
		RULE_ident = 17, RULE_array_elem = 18, RULE_int_liter = 19, RULE_bool_liter = 20, 
		RULE_char_liter = 21, RULE_str_liter = 22, RULE_array_liter = 23, RULE_pair_liter = 24, 
		RULE_comment = 25;
	public static final String[] ruleNames = {
		"prog", "func", "param_list", "param", "stat", "assign_lhs", "assign_rhs", 
		"arg_list", "pair_elem", "type", "base_type", "array_type", "pair_type", 
		"pair_elem_type", "expr", "unary_oper", "binary_oper", "ident", "array_elem", 
		"int_liter", "bool_liter", "char_liter", "str_liter", "array_liter", "pair_liter", 
		"comment"
	};

	@Override
	public String getGrammarFileName() { return "WaccParser.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public WaccParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class ProgContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(WaccParser.EOF, 0); }
		public List<FuncContext> func() {
			return getRuleContexts(FuncContext.class);
		}
		public FuncContext func(int i) {
			return getRuleContext(FuncContext.class,i);
		}
		public TerminalNode BEGIN() { return getToken(WaccParser.BEGIN, 0); }
		public TerminalNode END() { return getToken(WaccParser.END, 0); }
		public StatContext stat() {
			return getRuleContext(StatContext.class,0);
		}
		public ProgContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_prog; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WaccParserVisitor ) return ((WaccParserVisitor<? extends T>)visitor).visitProg(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ProgContext prog() throws RecognitionException {
		ProgContext _localctx = new ProgContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_prog);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(52); match(BEGIN);
			setState(56);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,0,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(53); func();
					}
					} 
				}
				setState(58);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,0,_ctx);
			}
			setState(59); stat(0);
			setState(60); match(END);
			setState(61); match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FuncContext extends ParserRuleContext {
		public TerminalNode CLOSE_PARENTHESES() { return getToken(WaccParser.CLOSE_PARENTHESES, 0); }
		public TerminalNode IS() { return getToken(WaccParser.IS, 0); }
		public Param_listContext param_list() {
			return getRuleContext(Param_listContext.class,0);
		}
		public IdentContext ident() {
			return getRuleContext(IdentContext.class,0);
		}
		public TerminalNode END() { return getToken(WaccParser.END, 0); }
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public StatContext stat() {
			return getRuleContext(StatContext.class,0);
		}
		public TerminalNode OPEN_PARENTHESES() { return getToken(WaccParser.OPEN_PARENTHESES, 0); }
		public FuncContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_func; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WaccParserVisitor ) return ((WaccParserVisitor<? extends T>)visitor).visitFunc(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FuncContext func() throws RecognitionException {
		FuncContext _localctx = new FuncContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_func);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(63); type(0);
			setState(64); ident();
			setState(65); match(OPEN_PARENTHESES);
			setState(67);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << INT) | (1L << BOOL) | (1L << CHAR) | (1L << STRING) | (1L << PAIR))) != 0)) {
				{
				setState(66); param_list();
				}
			}

			setState(69); match(CLOSE_PARENTHESES);
			setState(70); match(IS);
			setState(71); stat(0);
			setState(72); match(END);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Param_listContext extends ParserRuleContext {
		public List<ParamContext> param() {
			return getRuleContexts(ParamContext.class);
		}
		public List<TerminalNode> COMMA() { return getTokens(WaccParser.COMMA); }
		public ParamContext param(int i) {
			return getRuleContext(ParamContext.class,i);
		}
		public TerminalNode COMMA(int i) {
			return getToken(WaccParser.COMMA, i);
		}
		public Param_listContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_param_list; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WaccParserVisitor ) return ((WaccParserVisitor<? extends T>)visitor).visitParam_list(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Param_listContext param_list() throws RecognitionException {
		Param_listContext _localctx = new Param_listContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_param_list);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(74); param();
			setState(79);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(75); match(COMMA);
				setState(76); param();
				}
				}
				setState(81);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ParamContext extends ParserRuleContext {
		public IdentContext ident() {
			return getRuleContext(IdentContext.class,0);
		}
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public ParamContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_param; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WaccParserVisitor ) return ((WaccParserVisitor<? extends T>)visitor).visitParam(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParamContext param() throws RecognitionException {
		ParamContext _localctx = new ParamContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_param);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(82); type(0);
			setState(83); ident();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class StatContext extends ParserRuleContext {
		public TerminalNode ELSE() { return getToken(WaccParser.ELSE, 0); }
		public TerminalNode IF() { return getToken(WaccParser.IF, 0); }
		public TerminalNode FREE() { return getToken(WaccParser.FREE, 0); }
		public TerminalNode READ() { return getToken(WaccParser.READ, 0); }
		public TerminalNode FI() { return getToken(WaccParser.FI, 0); }
		public TerminalNode DONE() { return getToken(WaccParser.DONE, 0); }
		public TerminalNode RETURN() { return getToken(WaccParser.RETURN, 0); }
		public TerminalNode DO() { return getToken(WaccParser.DO, 0); }
		public Assign_rhsContext assign_rhs() {
			return getRuleContext(Assign_rhsContext.class,0);
		}
		public TerminalNode SKIP() { return getToken(WaccParser.SKIP, 0); }
		public StatContext stat(int i) {
			return getRuleContext(StatContext.class,i);
		}
		public TerminalNode BEGIN() { return getToken(WaccParser.BEGIN, 0); }
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public TerminalNode PRINT() { return getToken(WaccParser.PRINT, 0); }
		public TerminalNode THEN() { return getToken(WaccParser.THEN, 0); }
		public List<StatContext> stat() {
			return getRuleContexts(StatContext.class);
		}
		public TerminalNode WHILE() { return getToken(WaccParser.WHILE, 0); }
		public TerminalNode EXIT() { return getToken(WaccParser.EXIT, 0); }
		public TerminalNode ASSIGN() { return getToken(WaccParser.ASSIGN, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public IdentContext ident() {
			return getRuleContext(IdentContext.class,0);
		}
		public TerminalNode SEMI_COLON() { return getToken(WaccParser.SEMI_COLON, 0); }
		public TerminalNode PRINTLN() { return getToken(WaccParser.PRINTLN, 0); }
		public TerminalNode END() { return getToken(WaccParser.END, 0); }
		public Assign_lhsContext assign_lhs() {
			return getRuleContext(Assign_lhsContext.class,0);
		}
		public StatContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stat; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WaccParserVisitor ) return ((WaccParserVisitor<? extends T>)visitor).visitStat(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StatContext stat() throws RecognitionException {
		return stat(0);
	}

	private StatContext stat(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		StatContext _localctx = new StatContext(_ctx, _parentState);
		StatContext _prevctx = _localctx;
		int _startState = 8;
		enterRecursionRule(_localctx, 8, RULE_stat, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(126);
			switch (_input.LA(1)) {
			case SKIP:
				{
				setState(86); match(SKIP);
				}
				break;
			case INT:
			case BOOL:
			case CHAR:
			case STRING:
			case PAIR:
				{
				setState(87); type(0);
				setState(88); ident();
				setState(89); match(ASSIGN);
				setState(90); assign_rhs();
				}
				break;
			case FIRST:
			case SECOND:
			case IDENTIFIER:
				{
				setState(92); assign_lhs();
				setState(93); match(ASSIGN);
				setState(94); assign_rhs();
				}
				break;
			case READ:
				{
				setState(96); match(READ);
				setState(97); assign_lhs();
				}
				break;
			case FREE:
				{
				setState(98); match(FREE);
				setState(99); expr(0);
				}
				break;
			case RETURN:
				{
				setState(100); match(RETURN);
				setState(101); expr(0);
				}
				break;
			case EXIT:
				{
				setState(102); match(EXIT);
				setState(103); expr(0);
				}
				break;
			case PRINT:
				{
				setState(104); match(PRINT);
				setState(105); expr(0);
				}
				break;
			case PRINTLN:
				{
				setState(106); match(PRINTLN);
				setState(107); expr(0);
				}
				break;
			case IF:
				{
				setState(108); match(IF);
				setState(109); expr(0);
				setState(110); match(THEN);
				setState(111); stat(0);
				setState(112); match(ELSE);
				setState(113); stat(0);
				setState(114); match(FI);
				}
				break;
			case WHILE:
				{
				setState(116); match(WHILE);
				setState(117); expr(0);
				setState(118); match(DO);
				setState(119); stat(0);
				setState(120); match(DONE);
				}
				break;
			case BEGIN:
				{
				setState(122); match(BEGIN);
				setState(123); stat(0);
				setState(124); match(END);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(133);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,4,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new StatContext(_parentctx, _parentState);
					pushNewRecursionContext(_localctx, _startState, RULE_stat);
					setState(128);
					if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
					setState(129); match(SEMI_COLON);
					setState(130); stat(2);
					}
					} 
				}
				setState(135);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,4,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class Assign_lhsContext extends ParserRuleContext {
		public IdentContext ident() {
			return getRuleContext(IdentContext.class,0);
		}
		public Pair_elemContext pair_elem() {
			return getRuleContext(Pair_elemContext.class,0);
		}
		public Array_elemContext array_elem() {
			return getRuleContext(Array_elemContext.class,0);
		}
		public Assign_lhsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_assign_lhs; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WaccParserVisitor ) return ((WaccParserVisitor<? extends T>)visitor).visitAssign_lhs(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Assign_lhsContext assign_lhs() throws RecognitionException {
		Assign_lhsContext _localctx = new Assign_lhsContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_assign_lhs);
		try {
			setState(139);
			switch ( getInterpreter().adaptivePredict(_input,5,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(136); ident();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(137); array_elem();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(138); pair_elem();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Assign_rhsContext extends ParserRuleContext {
		public TerminalNode NEWPAIR() { return getToken(WaccParser.NEWPAIR, 0); }
		public TerminalNode CLOSE_PARENTHESES() { return getToken(WaccParser.CLOSE_PARENTHESES, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode COMMA() { return getToken(WaccParser.COMMA, 0); }
		public TerminalNode CALL() { return getToken(WaccParser.CALL, 0); }
		public IdentContext ident() {
			return getRuleContext(IdentContext.class,0);
		}
		public Pair_elemContext pair_elem() {
			return getRuleContext(Pair_elemContext.class,0);
		}
		public Array_literContext array_liter() {
			return getRuleContext(Array_literContext.class,0);
		}
		public Arg_listContext arg_list() {
			return getRuleContext(Arg_listContext.class,0);
		}
		public TerminalNode OPEN_PARENTHESES() { return getToken(WaccParser.OPEN_PARENTHESES, 0); }
		public Assign_rhsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_assign_rhs; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WaccParserVisitor ) return ((WaccParserVisitor<? extends T>)visitor).visitAssign_rhs(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Assign_rhsContext assign_rhs() throws RecognitionException {
		Assign_rhsContext _localctx = new Assign_rhsContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_assign_rhs);
		int _la;
		try {
			setState(159);
			switch (_input.LA(1)) {
			case NOT:
			case LENGTH:
			case ORD:
			case CHR:
			case MINUS:
			case NULL:
			case OPEN_PARENTHESES:
			case TRUE:
			case FALSE:
			case IDENTIFIER:
			case INTEGER:
			case CHARACTER_LITERAL:
			case STRING_LITERAL:
				enterOuterAlt(_localctx, 1);
				{
				setState(141); expr(0);
				}
				break;
			case OPEN_SQUARE_BRACKET:
				enterOuterAlt(_localctx, 2);
				{
				setState(142); array_liter();
				}
				break;
			case NEWPAIR:
				enterOuterAlt(_localctx, 3);
				{
				setState(143); match(NEWPAIR);
				setState(144); match(OPEN_PARENTHESES);
				setState(145); expr(0);
				setState(146); match(COMMA);
				setState(147); expr(0);
				setState(148); match(CLOSE_PARENTHESES);
				}
				break;
			case FIRST:
			case SECOND:
				enterOuterAlt(_localctx, 4);
				{
				setState(150); pair_elem();
				}
				break;
			case CALL:
				enterOuterAlt(_localctx, 5);
				{
				setState(151); match(CALL);
				setState(152); ident();
				setState(153); match(OPEN_PARENTHESES);
				setState(155);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << NOT) | (1L << LENGTH) | (1L << ORD) | (1L << CHR) | (1L << MINUS) | (1L << NULL) | (1L << OPEN_PARENTHESES) | (1L << TRUE) | (1L << FALSE) | (1L << IDENTIFIER) | (1L << INTEGER) | (1L << CHARACTER_LITERAL) | (1L << STRING_LITERAL))) != 0)) {
					{
					setState(154); arg_list();
					}
				}

				setState(157); match(CLOSE_PARENTHESES);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Arg_listContext extends ParserRuleContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(WaccParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(WaccParser.COMMA, i);
		}
		public Arg_listContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arg_list; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WaccParserVisitor ) return ((WaccParserVisitor<? extends T>)visitor).visitArg_list(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Arg_listContext arg_list() throws RecognitionException {
		Arg_listContext _localctx = new Arg_listContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_arg_list);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(161); expr(0);
			setState(166);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(162); match(COMMA);
				setState(163); expr(0);
				}
				}
				setState(168);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Pair_elemContext extends ParserRuleContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode SECOND() { return getToken(WaccParser.SECOND, 0); }
		public TerminalNode FIRST() { return getToken(WaccParser.FIRST, 0); }
		public Pair_elemContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pair_elem; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WaccParserVisitor ) return ((WaccParserVisitor<? extends T>)visitor).visitPair_elem(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Pair_elemContext pair_elem() throws RecognitionException {
		Pair_elemContext _localctx = new Pair_elemContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_pair_elem);
		try {
			setState(173);
			switch (_input.LA(1)) {
			case FIRST:
				enterOuterAlt(_localctx, 1);
				{
				setState(169); match(FIRST);
				setState(170); expr(0);
				}
				break;
			case SECOND:
				enterOuterAlt(_localctx, 2);
				{
				setState(171); match(SECOND);
				setState(172); expr(0);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TypeContext extends ParserRuleContext {
		public Pair_typeContext pair_type() {
			return getRuleContext(Pair_typeContext.class,0);
		}
		public TerminalNode OPEN_SQUARE_BRACKET() { return getToken(WaccParser.OPEN_SQUARE_BRACKET, 0); }
		public TerminalNode CLOSE_SQUARE_BRACKET() { return getToken(WaccParser.CLOSE_SQUARE_BRACKET, 0); }
		public Base_typeContext base_type() {
			return getRuleContext(Base_typeContext.class,0);
		}
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public TypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_type; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WaccParserVisitor ) return ((WaccParserVisitor<? extends T>)visitor).visitType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeContext type() throws RecognitionException {
		return type(0);
	}

	private TypeContext type(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		TypeContext _localctx = new TypeContext(_ctx, _parentState);
		TypeContext _prevctx = _localctx;
		int _startState = 18;
		enterRecursionRule(_localctx, 18, RULE_type, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(178);
			switch (_input.LA(1)) {
			case INT:
			case BOOL:
			case CHAR:
			case STRING:
				{
				setState(176); base_type();
				}
				break;
			case PAIR:
				{
				setState(177); pair_type();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(185);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,11,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new TypeContext(_parentctx, _parentState);
					pushNewRecursionContext(_localctx, _startState, RULE_type);
					setState(180);
					if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
					setState(181); match(OPEN_SQUARE_BRACKET);
					setState(182); match(CLOSE_SQUARE_BRACKET);
					}
					} 
				}
				setState(187);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,11,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class Base_typeContext extends ParserRuleContext {
		public TerminalNode BOOL() { return getToken(WaccParser.BOOL, 0); }
		public TerminalNode STRING() { return getToken(WaccParser.STRING, 0); }
		public TerminalNode CHAR() { return getToken(WaccParser.CHAR, 0); }
		public TerminalNode INT() { return getToken(WaccParser.INT, 0); }
		public Base_typeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_base_type; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WaccParserVisitor ) return ((WaccParserVisitor<? extends T>)visitor).visitBase_type(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Base_typeContext base_type() throws RecognitionException {
		Base_typeContext _localctx = new Base_typeContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_base_type);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(188);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << INT) | (1L << BOOL) | (1L << CHAR) | (1L << STRING))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			consume();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Array_typeContext extends ParserRuleContext {
		public TerminalNode OPEN_SQUARE_BRACKET() { return getToken(WaccParser.OPEN_SQUARE_BRACKET, 0); }
		public TerminalNode CLOSE_SQUARE_BRACKET() { return getToken(WaccParser.CLOSE_SQUARE_BRACKET, 0); }
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public Array_typeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_array_type; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WaccParserVisitor ) return ((WaccParserVisitor<? extends T>)visitor).visitArray_type(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Array_typeContext array_type() throws RecognitionException {
		Array_typeContext _localctx = new Array_typeContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_array_type);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(190); type(0);
			setState(191); match(OPEN_SQUARE_BRACKET);
			setState(192); match(CLOSE_SQUARE_BRACKET);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Pair_typeContext extends ParserRuleContext {
		public List<Pair_elem_typeContext> pair_elem_type() {
			return getRuleContexts(Pair_elem_typeContext.class);
		}
		public TerminalNode CLOSE_PARENTHESES() { return getToken(WaccParser.CLOSE_PARENTHESES, 0); }
		public TerminalNode PAIR() { return getToken(WaccParser.PAIR, 0); }
		public TerminalNode COMMA() { return getToken(WaccParser.COMMA, 0); }
		public Pair_elem_typeContext pair_elem_type(int i) {
			return getRuleContext(Pair_elem_typeContext.class,i);
		}
		public TerminalNode OPEN_PARENTHESES() { return getToken(WaccParser.OPEN_PARENTHESES, 0); }
		public Pair_typeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pair_type; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WaccParserVisitor ) return ((WaccParserVisitor<? extends T>)visitor).visitPair_type(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Pair_typeContext pair_type() throws RecognitionException {
		Pair_typeContext _localctx = new Pair_typeContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_pair_type);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(194); match(PAIR);
			setState(195); match(OPEN_PARENTHESES);
			setState(196); pair_elem_type();
			setState(197); match(COMMA);
			setState(198); pair_elem_type();
			setState(199); match(CLOSE_PARENTHESES);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Pair_elem_typeContext extends ParserRuleContext {
		public TerminalNode PAIR() { return getToken(WaccParser.PAIR, 0); }
		public Base_typeContext base_type() {
			return getRuleContext(Base_typeContext.class,0);
		}
		public Array_typeContext array_type() {
			return getRuleContext(Array_typeContext.class,0);
		}
		public Pair_elem_typeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pair_elem_type; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WaccParserVisitor ) return ((WaccParserVisitor<? extends T>)visitor).visitPair_elem_type(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Pair_elem_typeContext pair_elem_type() throws RecognitionException {
		Pair_elem_typeContext _localctx = new Pair_elem_typeContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_pair_elem_type);
		try {
			setState(204);
			switch ( getInterpreter().adaptivePredict(_input,12,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(201); base_type();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(202); array_type();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(203); match(PAIR);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExprContext extends ParserRuleContext {
		public Binary_operContext binary_oper() {
			return getRuleContext(Binary_operContext.class,0);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public Int_literContext int_liter() {
			return getRuleContext(Int_literContext.class,0);
		}
		public Str_literContext str_liter() {
			return getRuleContext(Str_literContext.class,0);
		}
		public Unary_operContext unary_oper() {
			return getRuleContext(Unary_operContext.class,0);
		}
		public Char_literContext char_liter() {
			return getRuleContext(Char_literContext.class,0);
		}
		public Bool_literContext bool_liter() {
			return getRuleContext(Bool_literContext.class,0);
		}
		public TerminalNode OPEN_PARENTHESES() { return getToken(WaccParser.OPEN_PARENTHESES, 0); }
		public Pair_literContext pair_liter() {
			return getRuleContext(Pair_literContext.class,0);
		}
		public TerminalNode CLOSE_PARENTHESES() { return getToken(WaccParser.CLOSE_PARENTHESES, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public IdentContext ident() {
			return getRuleContext(IdentContext.class,0);
		}
		public Array_elemContext array_elem() {
			return getRuleContext(Array_elemContext.class,0);
		}
		public ExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expr; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WaccParserVisitor ) return ((WaccParserVisitor<? extends T>)visitor).visitExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExprContext expr() throws RecognitionException {
		return expr(0);
	}

	private ExprContext expr(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExprContext _localctx = new ExprContext(_ctx, _parentState);
		ExprContext _prevctx = _localctx;
		int _startState = 28;
		enterRecursionRule(_localctx, 28, RULE_expr, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(221);
			switch ( getInterpreter().adaptivePredict(_input,13,_ctx) ) {
			case 1:
				{
				setState(207); unary_oper();
				setState(208); expr(3);
				}
				break;
			case 2:
				{
				setState(210); int_liter();
				}
				break;
			case 3:
				{
				setState(211); bool_liter();
				}
				break;
			case 4:
				{
				setState(212); char_liter();
				}
				break;
			case 5:
				{
				setState(213); str_liter();
				}
				break;
			case 6:
				{
				setState(214); pair_liter();
				}
				break;
			case 7:
				{
				setState(215); ident();
				}
				break;
			case 8:
				{
				setState(216); array_elem();
				}
				break;
			case 9:
				{
				setState(217); match(OPEN_PARENTHESES);
				setState(218); expr(0);
				setState(219); match(CLOSE_PARENTHESES);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(229);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,14,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new ExprContext(_parentctx, _parentState);
					pushNewRecursionContext(_localctx, _startState, RULE_expr);
					setState(223);
					if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
					setState(224); binary_oper();
					setState(225); expr(3);
					}
					} 
				}
				setState(231);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,14,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class Unary_operContext extends ParserRuleContext {
		public TerminalNode NOT() { return getToken(WaccParser.NOT, 0); }
		public TerminalNode ORD() { return getToken(WaccParser.ORD, 0); }
		public TerminalNode MINUS() { return getToken(WaccParser.MINUS, 0); }
		public TerminalNode LENGTH() { return getToken(WaccParser.LENGTH, 0); }
		public TerminalNode CHR() { return getToken(WaccParser.CHR, 0); }
		public Unary_operContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unary_oper; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WaccParserVisitor ) return ((WaccParserVisitor<? extends T>)visitor).visitUnary_oper(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Unary_operContext unary_oper() throws RecognitionException {
		Unary_operContext _localctx = new Unary_operContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_unary_oper);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(232);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << NOT) | (1L << LENGTH) | (1L << ORD) | (1L << CHR) | (1L << MINUS))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			consume();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Binary_operContext extends ParserRuleContext {
		public TerminalNode LESS_THAN_OR_EQUAL() { return getToken(WaccParser.LESS_THAN_OR_EQUAL, 0); }
		public TerminalNode NOT_EQUAL() { return getToken(WaccParser.NOT_EQUAL, 0); }
		public TerminalNode MULTIPLY() { return getToken(WaccParser.MULTIPLY, 0); }
		public TerminalNode MOD() { return getToken(WaccParser.MOD, 0); }
		public TerminalNode LESS_THAN() { return getToken(WaccParser.LESS_THAN, 0); }
		public TerminalNode OR() { return getToken(WaccParser.OR, 0); }
		public TerminalNode EQUAL() { return getToken(WaccParser.EQUAL, 0); }
		public TerminalNode GREATER_THAN_OR_EQUAL() { return getToken(WaccParser.GREATER_THAN_OR_EQUAL, 0); }
		public TerminalNode DIVIDE() { return getToken(WaccParser.DIVIDE, 0); }
		public TerminalNode AND() { return getToken(WaccParser.AND, 0); }
		public TerminalNode PLUS() { return getToken(WaccParser.PLUS, 0); }
		public TerminalNode MINUS() { return getToken(WaccParser.MINUS, 0); }
		public TerminalNode GREATER_THAN() { return getToken(WaccParser.GREATER_THAN, 0); }
		public Binary_operContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_binary_oper; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WaccParserVisitor ) return ((WaccParserVisitor<? extends T>)visitor).visitBinary_oper(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Binary_operContext binary_oper() throws RecognitionException {
		Binary_operContext _localctx = new Binary_operContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_binary_oper);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(234);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << MULTIPLY) | (1L << DIVIDE) | (1L << MOD) | (1L << PLUS) | (1L << MINUS) | (1L << GREATER_THAN) | (1L << LESS_THAN) | (1L << GREATER_THAN_OR_EQUAL) | (1L << LESS_THAN_OR_EQUAL) | (1L << EQUAL) | (1L << NOT_EQUAL) | (1L << AND) | (1L << OR))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			consume();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IdentContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(WaccParser.IDENTIFIER, 0); }
		public IdentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ident; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WaccParserVisitor ) return ((WaccParserVisitor<? extends T>)visitor).visitIdent(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IdentContext ident() throws RecognitionException {
		IdentContext _localctx = new IdentContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_ident);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(236); match(IDENTIFIER);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Array_elemContext extends ParserRuleContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public IdentContext ident() {
			return getRuleContext(IdentContext.class,0);
		}
		public List<TerminalNode> OPEN_SQUARE_BRACKET() { return getTokens(WaccParser.OPEN_SQUARE_BRACKET); }
		public TerminalNode OPEN_SQUARE_BRACKET(int i) {
			return getToken(WaccParser.OPEN_SQUARE_BRACKET, i);
		}
		public List<TerminalNode> CLOSE_SQUARE_BRACKET() { return getTokens(WaccParser.CLOSE_SQUARE_BRACKET); }
		public TerminalNode CLOSE_SQUARE_BRACKET(int i) {
			return getToken(WaccParser.CLOSE_SQUARE_BRACKET, i);
		}
		public Array_elemContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_array_elem; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WaccParserVisitor ) return ((WaccParserVisitor<? extends T>)visitor).visitArray_elem(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Array_elemContext array_elem() throws RecognitionException {
		Array_elemContext _localctx = new Array_elemContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_array_elem);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(238); ident();
			setState(243); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(239); match(OPEN_SQUARE_BRACKET);
					setState(240); expr(0);
					setState(241); match(CLOSE_SQUARE_BRACKET);
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(245); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,15,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Int_literContext extends ParserRuleContext {
		public TerminalNode INTEGER() { return getToken(WaccParser.INTEGER, 0); }
		public Int_literContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_int_liter; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WaccParserVisitor ) return ((WaccParserVisitor<? extends T>)visitor).visitInt_liter(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Int_literContext int_liter() throws RecognitionException {
		Int_literContext _localctx = new Int_literContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_int_liter);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(247); match(INTEGER);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Bool_literContext extends ParserRuleContext {
		public TerminalNode FALSE() { return getToken(WaccParser.FALSE, 0); }
		public TerminalNode TRUE() { return getToken(WaccParser.TRUE, 0); }
		public Bool_literContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_bool_liter; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WaccParserVisitor ) return ((WaccParserVisitor<? extends T>)visitor).visitBool_liter(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Bool_literContext bool_liter() throws RecognitionException {
		Bool_literContext _localctx = new Bool_literContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_bool_liter);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(249);
			_la = _input.LA(1);
			if ( !(_la==TRUE || _la==FALSE) ) {
			_errHandler.recoverInline(this);
			}
			consume();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Char_literContext extends ParserRuleContext {
		public TerminalNode CHARACTER_LITERAL() { return getToken(WaccParser.CHARACTER_LITERAL, 0); }
		public Char_literContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_char_liter; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WaccParserVisitor ) return ((WaccParserVisitor<? extends T>)visitor).visitChar_liter(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Char_literContext char_liter() throws RecognitionException {
		Char_literContext _localctx = new Char_literContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_char_liter);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(251); match(CHARACTER_LITERAL);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Str_literContext extends ParserRuleContext {
		public TerminalNode STRING_LITERAL() { return getToken(WaccParser.STRING_LITERAL, 0); }
		public Str_literContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_str_liter; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WaccParserVisitor ) return ((WaccParserVisitor<? extends T>)visitor).visitStr_liter(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Str_literContext str_liter() throws RecognitionException {
		Str_literContext _localctx = new Str_literContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_str_liter);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(253); match(STRING_LITERAL);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Array_literContext extends ParserRuleContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(WaccParser.COMMA); }
		public TerminalNode OPEN_SQUARE_BRACKET() { return getToken(WaccParser.OPEN_SQUARE_BRACKET, 0); }
		public TerminalNode CLOSE_SQUARE_BRACKET() { return getToken(WaccParser.CLOSE_SQUARE_BRACKET, 0); }
		public TerminalNode COMMA(int i) {
			return getToken(WaccParser.COMMA, i);
		}
		public Array_literContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_array_liter; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WaccParserVisitor ) return ((WaccParserVisitor<? extends T>)visitor).visitArray_liter(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Array_literContext array_liter() throws RecognitionException {
		Array_literContext _localctx = new Array_literContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_array_liter);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(255); match(OPEN_SQUARE_BRACKET);
			setState(264);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << NOT) | (1L << LENGTH) | (1L << ORD) | (1L << CHR) | (1L << MINUS) | (1L << NULL) | (1L << OPEN_PARENTHESES) | (1L << TRUE) | (1L << FALSE) | (1L << IDENTIFIER) | (1L << INTEGER) | (1L << CHARACTER_LITERAL) | (1L << STRING_LITERAL))) != 0)) {
				{
				setState(256); expr(0);
				setState(261);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(257); match(COMMA);
					setState(258); expr(0);
					}
					}
					setState(263);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(266); match(CLOSE_SQUARE_BRACKET);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Pair_literContext extends ParserRuleContext {
		public TerminalNode NULL() { return getToken(WaccParser.NULL, 0); }
		public Pair_literContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pair_liter; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WaccParserVisitor ) return ((WaccParserVisitor<? extends T>)visitor).visitPair_liter(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Pair_literContext pair_liter() throws RecognitionException {
		Pair_literContext _localctx = new Pair_literContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_pair_liter);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(268); match(NULL);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CommentContext extends ParserRuleContext {
		public TerminalNode COMMENT() { return getToken(WaccParser.COMMENT, 0); }
		public CommentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_comment; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof WaccParserVisitor ) return ((WaccParserVisitor<? extends T>)visitor).visitComment(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CommentContext comment() throws RecognitionException {
		CommentContext _localctx = new CommentContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_comment);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(270); match(COMMENT);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 4: return stat_sempred((StatContext)_localctx, predIndex);
		case 9: return type_sempred((TypeContext)_localctx, predIndex);
		case 14: return expr_sempred((ExprContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean stat_sempred(StatContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0: return precpred(_ctx, 1);
		}
		return true;
	}
	private boolean expr_sempred(ExprContext _localctx, int predIndex) {
		switch (predIndex) {
		case 2: return precpred(_ctx, 2);
		}
		return true;
	}
	private boolean type_sempred(TypeContext _localctx, int predIndex) {
		switch (predIndex) {
		case 1: return precpred(_ctx, 2);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3=\u0113\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\3\2\3\2\7\29\n\2\f\2\16\2<\13\2\3\2\3\2\3\2\3\2\3"+
		"\3\3\3\3\3\3\3\5\3F\n\3\3\3\3\3\3\3\3\3\3\3\3\4\3\4\3\4\7\4P\n\4\f\4\16"+
		"\4S\13\4\3\5\3\5\3\5\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3"+
		"\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6"+
		"\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\5\6\u0081\n\6\3\6\3\6\3\6"+
		"\7\6\u0086\n\6\f\6\16\6\u0089\13\6\3\7\3\7\3\7\5\7\u008e\n\7\3\b\3\b\3"+
		"\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\5\b\u009e\n\b\3\b\3\b\5"+
		"\b\u00a2\n\b\3\t\3\t\3\t\7\t\u00a7\n\t\f\t\16\t\u00aa\13\t\3\n\3\n\3\n"+
		"\3\n\5\n\u00b0\n\n\3\13\3\13\3\13\5\13\u00b5\n\13\3\13\3\13\3\13\7\13"+
		"\u00ba\n\13\f\13\16\13\u00bd\13\13\3\f\3\f\3\r\3\r\3\r\3\r\3\16\3\16\3"+
		"\16\3\16\3\16\3\16\3\16\3\17\3\17\3\17\5\17\u00cf\n\17\3\20\3\20\3\20"+
		"\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\5\20\u00e0"+
		"\n\20\3\20\3\20\3\20\3\20\7\20\u00e6\n\20\f\20\16\20\u00e9\13\20\3\21"+
		"\3\21\3\22\3\22\3\23\3\23\3\24\3\24\3\24\3\24\3\24\6\24\u00f6\n\24\r\24"+
		"\16\24\u00f7\3\25\3\25\3\26\3\26\3\27\3\27\3\30\3\30\3\31\3\31\3\31\3"+
		"\31\7\31\u0106\n\31\f\31\16\31\u0109\13\31\5\31\u010b\n\31\3\31\3\31\3"+
		"\32\3\32\3\33\3\33\3\33\2\5\n\24\36\34\2\4\6\b\n\f\16\20\22\24\26\30\32"+
		"\34\36 \"$&(*,.\60\62\64\2\6\3\2\'*\4\2\25\30\36\36\3\2\32&\3\2\66\67"+
		"\u0120\2\66\3\2\2\2\4A\3\2\2\2\6L\3\2\2\2\bT\3\2\2\2\n\u0080\3\2\2\2\f"+
		"\u008d\3\2\2\2\16\u00a1\3\2\2\2\20\u00a3\3\2\2\2\22\u00af\3\2\2\2\24\u00b4"+
		"\3\2\2\2\26\u00be\3\2\2\2\30\u00c0\3\2\2\2\32\u00c4\3\2\2\2\34\u00ce\3"+
		"\2\2\2\36\u00df\3\2\2\2 \u00ea\3\2\2\2\"\u00ec\3\2\2\2$\u00ee\3\2\2\2"+
		"&\u00f0\3\2\2\2(\u00f9\3\2\2\2*\u00fb\3\2\2\2,\u00fd\3\2\2\2.\u00ff\3"+
		"\2\2\2\60\u0101\3\2\2\2\62\u010e\3\2\2\2\64\u0110\3\2\2\2\66:\7\23\2\2"+
		"\679\5\4\3\28\67\3\2\2\29<\3\2\2\2:8\3\2\2\2:;\3\2\2\2;=\3\2\2\2<:\3\2"+
		"\2\2=>\5\n\6\2>?\7\24\2\2?@\7\2\2\3@\3\3\2\2\2AB\5\24\13\2BC\5$\23\2C"+
		"E\7\60\2\2DF\5\6\4\2ED\3\2\2\2EF\3\2\2\2FG\3\2\2\2GH\7\61\2\2HI\7\3\2"+
		"\2IJ\5\n\6\2JK\7\24\2\2K\5\3\2\2\2LQ\5\b\5\2MN\7\65\2\2NP\5\b\5\2OM\3"+
		"\2\2\2PS\3\2\2\2QO\3\2\2\2QR\3\2\2\2R\7\3\2\2\2SQ\3\2\2\2TU\5\24\13\2"+
		"UV\5$\23\2V\t\3\2\2\2WX\b\6\1\2X\u0081\7\5\2\2YZ\5\24\13\2Z[\5$\23\2["+
		"\\\7\31\2\2\\]\5\16\b\2]\u0081\3\2\2\2^_\5\f\7\2_`\7\31\2\2`a\5\16\b\2"+
		"a\u0081\3\2\2\2bc\7\6\2\2c\u0081\5\f\7\2de\7\7\2\2e\u0081\5\36\20\2fg"+
		"\7\b\2\2g\u0081\5\36\20\2hi\7\t\2\2i\u0081\5\36\20\2jk\7\n\2\2k\u0081"+
		"\5\36\20\2lm\7\13\2\2m\u0081\5\36\20\2no\7\f\2\2op\5\36\20\2pq\7\r\2\2"+
		"qr\5\n\6\2rs\7\16\2\2st\5\n\6\2tu\7\17\2\2u\u0081\3\2\2\2vw\7\20\2\2w"+
		"x\5\36\20\2xy\7\21\2\2yz\5\n\6\2z{\7\22\2\2{\u0081\3\2\2\2|}\7\23\2\2"+
		"}~\5\n\6\2~\177\7\24\2\2\177\u0081\3\2\2\2\u0080W\3\2\2\2\u0080Y\3\2\2"+
		"\2\u0080^\3\2\2\2\u0080b\3\2\2\2\u0080d\3\2\2\2\u0080f\3\2\2\2\u0080h"+
		"\3\2\2\2\u0080j\3\2\2\2\u0080l\3\2\2\2\u0080n\3\2\2\2\u0080v\3\2\2\2\u0080"+
		"|\3\2\2\2\u0081\u0087\3\2\2\2\u0082\u0083\f\3\2\2\u0083\u0084\7\64\2\2"+
		"\u0084\u0086\5\n\6\4\u0085\u0082\3\2\2\2\u0086\u0089\3\2\2\2\u0087\u0085"+
		"\3\2\2\2\u0087\u0088\3\2\2\2\u0088\13\3\2\2\2\u0089\u0087\3\2\2\2\u008a"+
		"\u008e\5$\23\2\u008b\u008e\5&\24\2\u008c\u008e\5\22\n\2\u008d\u008a\3"+
		"\2\2\2\u008d\u008b\3\2\2\2\u008d\u008c\3\2\2\2\u008e\r\3\2\2\2\u008f\u00a2"+
		"\5\36\20\2\u0090\u00a2\5\60\31\2\u0091\u0092\7,\2\2\u0092\u0093\7\60\2"+
		"\2\u0093\u0094\5\36\20\2\u0094\u0095\7\65\2\2\u0095\u0096\5\36\20\2\u0096"+
		"\u0097\7\61\2\2\u0097\u00a2\3\2\2\2\u0098\u00a2\5\22\n\2\u0099\u009a\7"+
		"\4\2\2\u009a\u009b\5$\23\2\u009b\u009d\7\60\2\2\u009c\u009e\5\20\t\2\u009d"+
		"\u009c\3\2\2\2\u009d\u009e\3\2\2\2\u009e\u009f\3\2\2\2\u009f\u00a0\7\61"+
		"\2\2\u00a0\u00a2\3\2\2\2\u00a1\u008f\3\2\2\2\u00a1\u0090\3\2\2\2\u00a1"+
		"\u0091\3\2\2\2\u00a1\u0098\3\2\2\2\u00a1\u0099\3\2\2\2\u00a2\17\3\2\2"+
		"\2\u00a3\u00a8\5\36\20\2\u00a4\u00a5\7\65\2\2\u00a5\u00a7\5\36\20\2\u00a6"+
		"\u00a4\3\2\2\2\u00a7\u00aa\3\2\2\2\u00a8\u00a6\3\2\2\2\u00a8\u00a9\3\2"+
		"\2\2\u00a9\21\3\2\2\2\u00aa\u00a8\3\2\2\2\u00ab\u00ac\7-\2\2\u00ac\u00b0"+
		"\5\36\20\2\u00ad\u00ae\7.\2\2\u00ae\u00b0\5\36\20\2\u00af\u00ab\3\2\2"+
		"\2\u00af\u00ad\3\2\2\2\u00b0\23\3\2\2\2\u00b1\u00b2\b\13\1\2\u00b2\u00b5"+
		"\5\26\f\2\u00b3\u00b5\5\32\16\2\u00b4\u00b1\3\2\2\2\u00b4\u00b3\3\2\2"+
		"\2\u00b5\u00bb\3\2\2\2\u00b6\u00b7\f\4\2\2\u00b7\u00b8\7\62\2\2\u00b8"+
		"\u00ba\7\63\2\2\u00b9\u00b6\3\2\2\2\u00ba\u00bd\3\2\2\2\u00bb\u00b9\3"+
		"\2\2\2\u00bb\u00bc\3\2\2\2\u00bc\25\3\2\2\2\u00bd\u00bb\3\2\2\2\u00be"+
		"\u00bf\t\2\2\2\u00bf\27\3\2\2\2\u00c0\u00c1\5\24\13\2\u00c1\u00c2\7\62"+
		"\2\2\u00c2\u00c3\7\63\2\2\u00c3\31\3\2\2\2\u00c4\u00c5\7+\2\2\u00c5\u00c6"+
		"\7\60\2\2\u00c6\u00c7\5\34\17\2\u00c7\u00c8\7\65\2\2\u00c8\u00c9\5\34"+
		"\17\2\u00c9\u00ca\7\61\2\2\u00ca\33\3\2\2\2\u00cb\u00cf\5\26\f\2\u00cc"+
		"\u00cf\5\30\r\2\u00cd\u00cf\7+\2\2\u00ce\u00cb\3\2\2\2\u00ce\u00cc\3\2"+
		"\2\2\u00ce\u00cd\3\2\2\2\u00cf\35\3\2\2\2\u00d0\u00d1\b\20\1\2\u00d1\u00d2"+
		"\5 \21\2\u00d2\u00d3\5\36\20\5\u00d3\u00e0\3\2\2\2\u00d4\u00e0\5(\25\2"+
		"\u00d5\u00e0\5*\26\2\u00d6\u00e0\5,\27\2\u00d7\u00e0\5.\30\2\u00d8\u00e0"+
		"\5\62\32\2\u00d9\u00e0\5$\23\2\u00da\u00e0\5&\24\2\u00db\u00dc\7\60\2"+
		"\2\u00dc\u00dd\5\36\20\2\u00dd\u00de\7\61\2\2\u00de\u00e0\3\2\2\2\u00df"+
		"\u00d0\3\2\2\2\u00df\u00d4\3\2\2\2\u00df\u00d5\3\2\2\2\u00df\u00d6\3\2"+
		"\2\2\u00df\u00d7\3\2\2\2\u00df\u00d8\3\2\2\2\u00df\u00d9\3\2\2\2\u00df"+
		"\u00da\3\2\2\2\u00df\u00db\3\2\2\2\u00e0\u00e7\3\2\2\2\u00e1\u00e2\f\4"+
		"\2\2\u00e2\u00e3\5\"\22\2\u00e3\u00e4\5\36\20\5\u00e4\u00e6\3\2\2\2\u00e5"+
		"\u00e1\3\2\2\2\u00e6\u00e9\3\2\2\2\u00e7\u00e5\3\2\2\2\u00e7\u00e8\3\2"+
		"\2\2\u00e8\37\3\2\2\2\u00e9\u00e7\3\2\2\2\u00ea\u00eb\t\3\2\2\u00eb!\3"+
		"\2\2\2\u00ec\u00ed\t\4\2\2\u00ed#\3\2\2\2\u00ee\u00ef\78\2\2\u00ef%\3"+
		"\2\2\2\u00f0\u00f5\5$\23\2\u00f1\u00f2\7\62\2\2\u00f2\u00f3\5\36\20\2"+
		"\u00f3\u00f4\7\63\2\2\u00f4\u00f6\3\2\2\2\u00f5\u00f1\3\2\2\2\u00f6\u00f7"+
		"\3\2\2\2\u00f7\u00f5\3\2\2\2\u00f7\u00f8\3\2\2\2\u00f8\'\3\2\2\2\u00f9"+
		"\u00fa\7;\2\2\u00fa)\3\2\2\2\u00fb\u00fc\t\5\2\2\u00fc+\3\2\2\2\u00fd"+
		"\u00fe\7<\2\2\u00fe-\3\2\2\2\u00ff\u0100\7=\2\2\u0100/\3\2\2\2\u0101\u010a"+
		"\7\62\2\2\u0102\u0107\5\36\20\2\u0103\u0104\7\65\2\2\u0104\u0106\5\36"+
		"\20\2\u0105\u0103\3\2\2\2\u0106\u0109\3\2\2\2\u0107\u0105\3\2\2\2\u0107"+
		"\u0108\3\2\2\2\u0108\u010b\3\2\2\2\u0109\u0107\3\2\2\2\u010a\u0102\3\2"+
		"\2\2\u010a\u010b\3\2\2\2\u010b\u010c\3\2\2\2\u010c\u010d\7\63\2\2\u010d"+
		"\61\3\2\2\2\u010e\u010f\7/\2\2\u010f\63\3\2\2\2\u0110\u0111\79\2\2\u0111"+
		"\65\3\2\2\2\24:EQ\u0080\u0087\u008d\u009d\u00a1\u00a8\u00af\u00b4\u00bb"+
		"\u00ce\u00df\u00e7\u00f7\u0107\u010a";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}