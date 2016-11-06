lexer grammar WaccLexer;

//functions
IS: 'is' ;
CALL: 'call' ;

//statement-keywords
SKIP: 'skip' ;
READ: 'read' ;
FREE: 'free' ;
RETURN: 'return' ;
EXIT: 'exit' ;
PRINT: 'print' ;
PRINTLN: 'println' ;
IF: 'if' ;
THEN: 'then' ;
ELSE: 'else' ;
FI: 'fi' ;
WHILE: 'while' ;
DO: 'do' ;
DONE: 'done' ;
BEGIN: 'begin' ;
END: 'end' ;

//unary-operators
NOT: '!' ;
LEN: 'len' ;
ORD: 'ord' ;
CHR: 'chr' ;

//assignment
ASS: '=' ;

//binary-operators
MUL: '*' ;
DIV: '/' ;
MOD: '%' ;
ADD: '+' ;
SUB: '-' ;
GT: '>' ;
LT: '<' ;
GTE: '>=' ;
LTE: '<=' ;
EQ: '==' ;
NEQ: '!=' ;
AND: '&&' ;
OR: '||' ;

//types
INT: 'int' ;
BOOL: 'bool' ;
CHAR: 'char' ;
STRING: 'string' ;

//pair
PAIR: 'pair' ;
NEWPAIR: 'newpair' ;
FST: 'fst' ;
SND: 'snd' ;
NULL: 'null' ;

//brackets
ORBRACKET: '(' ;
CRBRACKET: ')' ;
OSBRACKET: '[' ;
CSBRACKET: ']' ;

//separators
SEMI_COLON: ';' ;
COMMA: ',' ;

//bool-values
TRUE: 'true' ;
FALSE: 'false' ;

//ident
IDENT: [_a-zA-Z] [_a-zA-Z0-9]* ;

//comments
COMMENT: '#' ~([\r\n])* [\r\n] -> skip ;

WS: [ \n\t\r] -> skip ;

//fragments for literals
fragment SINGLE_QUOTE: '\'' ;
fragment DOUBLE_QUOTE: '"' ;
fragment BACKSLASH: '\\' ;
fragment ESC_CHAR: '0'
| 'b'
| 't'
| 'n'
| 'f'
| 'r'
| SINGLE_QUOTE
| DOUBLE_QUOTE
| BACKSLASH ;
fragment CHARACTER: ~('\\' | '\'' | '"') | BACKSLASH ESC_CHAR ;

//literals
INTEGER: [0-9]+ ;
CHAR_LITER: SINGLE_QUOTE CHARACTER SINGLE_QUOTE ;
STR_LITER: DOUBLE_QUOTE (CHARACTER)* DOUBLE_QUOTE ;
