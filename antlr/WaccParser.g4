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

assign_lhs: ident                                                       #identLHSAssign
| array_elem                                                            #arrayLHSElemAssign
| pair_elem                                                             #pairLHSElemAssign;

assign_rhs: expr                                                        #exprRHSAssign
| array_liter                                                           #arrayLitterRHSAssign
| NEWPAIR OPEN_PARENTHESES expr COMMA expr CLOSE_PARENTHESES            #pairParantheses
| pair_elem                                                             #pairElemRHSAssign
| CALL ident OPEN_PARENTHESES (arg_list)? CLOSE_PARENTHESES             #callParantheses;

arg_list: expr (COMMA expr)* ;

pair_elem: FIRST expr                                                   #pairFirstExpr
| SECOND expr                                                           #secondFirstExpr;

type: base_type                                                         #baseType
| type OPEN_SQUARE_BRACKET CLOSE_SQUARE_BRACKET                         #typeParantheses
| pair_type                                                             #pairType;

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

expr: int_liter                                                         #anInt
| bool_liter                                                            #aBool
| char_liter                                                            #aChar
| str_liter                                                             #aString
| pair_liter                                                            #aPair
| ident                                                                 #anIdent
| array_elem                                                            #anArrayElem
| unary_oper expr                                                       #unOp
| expr binary_oper expr                                                 #binOp
| OPEN_PARENTHESES expr CLOSE_PARENTHESES                               #bracketExpr;

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
