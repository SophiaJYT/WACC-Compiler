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
NEG: '-' ;
LEN: 'len' ;
ORD: 'ord' ;
CHR: 'chr' ;

//binary-operators
ASS: '=' ;
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

//quotes
SINGLE_QUOTE: '\'' ;
DOUBLE_QUOTE: '"' ;

//idents
DIGIT: '0'..'9' ;
LC_LETTER: 'a'..'z' ;
UC_LETTER: 'A'..'Z' ;
UNDERSCORE: '_' ;

//ascii
BACKSLASH: '\\' ;
NULL_CHAR: '0' ;
BS: 'b' ;
TAB: 't' ;
NL: 'n' ;
NP: 'f' ;
RET: 'r' ;

//comments
HASH: '#' ;

INTEGER: DIGIT+ ;
WS: [ \n\t\r] -> skip ;




