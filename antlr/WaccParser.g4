parser grammar WaccParser;

options {
  tokenVocab=WaccLexer;
}

prog: BEGIN (func)* stat END EOF ;

func: type ident OPEN_PARENTHESES (param_list)? CLOSE_PARENTHESES IS stat END ;

param_list: param (COMMA param)* ;

param: type ident ;

stat: SKIP                                                              #skip
| type ident ASSIGN assign_rhs                                          #initialization
| assign_lhs ASSIGN assign_rhs                                          #assignment
| READ assign_lhs                                                       #read_lhs
| FREE expr                                                             #freeExpr
| RETURN expr                                                           #returnExpr
| EXIT expr                                                             #exitExpr
| PRINT expr                                                            #printExpr
| PRINTLN expr                                                          #printlnExpr
| IF expr THEN stat ELSE stat FI                                        #ifExpr
| WHILE expr DO stat DONE                                               #whileExpr
| BEGIN stat END                                                        #beginEnd
| stat SEMI_COLON stat                                                  #semicolonStat;

assign_lhs: ident
| array_elem
| pair_elem ;

assign_rhs: expr
| array_liter
| pairParantheses
| pair_elem
| callParantheses ;

pairParantheses: NEWPAIR OPEN_PARENTHESES expr COMMA expr CLOSE_PARENTHESES ;

callParantheses: CALL ident OPEN_PARENTHESES (arg_list)? CLOSE_PARENTHESES ;

arg_list: expr (COMMA expr)* ;

pair_elem: FIRST expr
| SECOND expr ;

type: base_type
| type OPEN_SQUARE_BRACKET CLOSE_SQUARE_BRACKET
| pair_type ;

base_type: INT
| BOOL
| CHAR
| STRING ;

array_type: type OPEN_SQUARE_BRACKET CLOSE_SQUARE_BRACKET ;

pair_type: PAIR OPEN_PARENTHESES pair_elem_type COMMA pair_elem_type
           CLOSE_PARENTHESES ;

pair_elem_type: base_type
| array_type
| PAIR ;

expr: int_liter
| bool_liter
| char_liter
| str_liter
| pair_liter
| ident
| array_elem
| unary_oper expr
| expr binary_oper expr
| bracketExpr ;

bracketExpr: OPEN_PARENTHESES expr CLOSE_PARENTHESES ;

unary_oper: NOT
| MINUS
| LENGTH
| ORD
| CHR ;

binary_oper: MULTIPLY
| DIVIDE
| MOD
| PLUS
| MINUS
| GREATER_THAN
| LESS_THAN
| GREATER_THAN_OR_EQUAL
| LESS_THAN_OR_EQUAL
| EQUAL
| NOT_EQUAL
| AND
| OR ;

ident: IDENTIFIER ;

array_elem: ident (OPEN_SQUARE_BRACKET expr CLOSE_SQUARE_BRACKET)+ ;

int_liter: INTEGER ;

bool_liter: TRUE
| FALSE ;

char_liter: CHARACTER_LITERAL ;

str_liter: STRING_LITERAL ;

array_liter: OPEN_SQUARE_BRACKET (expr (COMMA expr)*)? CLOSE_SQUARE_BRACKET ;

pair_liter: NULL;

comment: COMMENT;
