parser grammar BasicParser;

options {
  tokenVocab=BasicLexer;
}

prog: BEGIN (func)* stat END ;

func: type ident ORBRACKET (param_list)? CRBRACKET IS stat END ;

param_list: param (COMMA param)* ;

param: type ident ;

stat: SKIP
| type ident ASS assign_rhs
| assign_lhs ASS assign_rhs
| READ assign_lhs
| FREE expr
| RETURN expr
| EXIT expr
| PRINT expr
| PRINTLN expr
| IF expr THEN stat ELSE stat FI
| WHILE expr DO stat DONE
| BEGIN stat END
| stat SEMI_COLON stat ;

assign_lhs: ident
| array_elem
| pair_elem ;

assign_rhs: expr
| array_liter
| NEWPAIR ORBRACKET expr COMMA expr CRBRACKET
| pair_elem
| CALL ident ORBRACKET (arg_list)? CRBRACKET ;

arg_list: expr (COMMA expr)* ;

pair_elem: FST expr
| SND expr ;

type: base_type
| array_type
| pair_type ;

base_type: INT
| BOOL
| CHAR
| STRING ;

array_type: type OSBRACKET CSBRACKET ;

pair_type: PAIR ORBRACKET pair_elem_type COMMA pair_elem_type CRBRACKET ;

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
| ORBRACKET expr CRBRACKET ;

unary_oper: NOT
| NEG
| LEN
| ORD
| CHR ;

binary_oper: MUL
| DIV
| MOD
| ADD
| SUB
| GT
| LT
| GTE
| LTE
| EQ
| NEQ
| AND
| OR ;

ident: (UNDERSCORE | LC_LETTER | UC_LETTER) (UNDERSCORE | LC_LETTER | UC_LETTER | DIGIT)* ;

array_elem: ident (OSBRACKET expr CSBRACKET)+ ;

int_liter: (int_sign)? (DIGIT)+ ;

digit: DIGIT ;

int_sign: ADD | SUB ;

bool_liter: TRUE | FALSE ;

char_liter: SINGLE_QUOTE character SINGLE_QUOTE ;

str_liter: DOUBLE_QUOTE (character)* DOUBLE_QUOTE ;

character: ~(BACKSLASH | SINGLE_QUOTE | DOUBLE_QUOTE)
| BACKSLASH escaped_char ;

escaped_char: NULL_CHAR
| BS
| TAB
| NL
| NP
| RET
| DOUBLE_QUOTE
| SINGLE_QUOTE
| BACKSLASH ;

array_liter: OSBRACKET (expr (COMMA expr)*)? CSBRACKET ;

pair_liter: NULL ;

comment: HASH ~(NL) NL ;
