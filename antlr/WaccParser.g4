parser grammar WaccParser;

options {
  tokenVocab=WaccLexer;
}

prog: BEGIN (funcDecl)* stat END EOF ;

funcDecl: type ident OPEN_PARENTHESES (paramList)? CLOSE_PARENTHESES IS stat END ;

paramList: param (COMMA param)* ;

param: type ident ;

stat: SKIP                                                              #skip
| type ident ASSIGN assignRhs                                           #varInit
| assignLhs ASSIGN assignRhs                                            #varAssign
| READ assignLhs                                                        #readStat
| FREE expr                                                             #freeStat
| RETURN expr                                                           #returnStat
| EXIT expr                                                             #exitStat
| PRINT expr                                                            #printStat
| PRINTLN expr                                                          #printlnStat
| IF expr THEN stat (ELSE stat)? FI                                     #ifStat
| WHILE expr DO stat DONE                                               #whileStat
//| DO stat WHILE expr DONE?                                              #doWhileStat
//| FOR OPEN_PARENTHESES stat expr stat CLOSE_PARANTHESES stat            #forStat
| BEGIN stat END                                                        #beginEnd
| stat SEMI_COLON stat                                                  #statSequence;

assignLhs: ident
| arrayElem
| pairElem ;

assignRhs: expr
| arrayLiter
| newPair
| pairElem
| callFunc ;

newPair: NEWPAIR OPEN_PARENTHESES expr COMMA expr CLOSE_PARENTHESES ;

callFunc: CALL ident OPEN_PARENTHESES (argList)? CLOSE_PARENTHESES ;

argList: expr (COMMA expr)* ;

pairElem: FIRST expr
| SECOND expr ;

type: baseType
| type OPEN_SQUARE_BRACKET CLOSE_SQUARE_BRACKET
| pairType ;

baseType: INT
| BOOL
| CHAR
| STRING ;

arrayType: type OPEN_SQUARE_BRACKET CLOSE_SQUARE_BRACKET ;

pairElemType: baseType
| arrayType
| PAIR
| pairType ;

pairType: PAIR OPEN_PARENTHESES pairElemType COMMA pairElemType
           CLOSE_PARENTHESES ;

expr: intLiter
| boolLiter
| charLiter
| strLiter
| pairLiter
| ident
| arrayElem
| expr binaryOper expr
| expr boolBinaryOper expr
| unaryOper expr
| bracketExpr ;

bracketExpr: OPEN_PARENTHESES expr CLOSE_PARENTHESES ;

unaryOper: NOT
| MINUS
| LENGTH
| ORD
| CHR ;

boolBinaryOper: AND
| OR ;

binaryOper: MULTIPLY
| DIVIDE
| MOD
| PLUS
| MINUS
| GREATER_THAN
| LESS_THAN
| GREATER_THAN_OR_EQUAL
| LESS_THAN_OR_EQUAL
| EQUAL
| NOT_EQUAL ;

ident: IDENTIFIER ;

arrayElem: ident (OPEN_SQUARE_BRACKET expr CLOSE_SQUARE_BRACKET)+ ;

intLiter: (PLUS | MINUS)? INTEGER ;

boolLiter: TRUE
| FALSE ;

charLiter: CHARACTER_LITERAL ;

strLiter: STRING_LITERAL ;

arrayLiter: OPEN_SQUARE_BRACKET (expr (COMMA expr)*)? CLOSE_SQUARE_BRACKET ;

pairLiter: NULL;
