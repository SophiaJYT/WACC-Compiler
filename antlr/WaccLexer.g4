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
LENGTH: 'len' ;
ORD: 'ord' ;
CHR: 'chr' ;

//assignment
ASSIGN: '=' ;

//binary-operators
MULTIPLY: '*' ;
DIVIDE: '/' ;
MOD: '%' ;
PLUS: '+' ;
MINUS: '-' ;
GREATER_THAN: '>' ;
LESS_THAN: '<' ;
GREATER_THAN_OR_EQUAL: '>=' ;
LESS_THAN_OR_EQUAL: '<=' ;
EQUAL: '==' ;
NOT_EQUAL: '!=' ;
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
FIRST: 'fst' ;
SECOND: 'snd' ;
NULL: 'null' ;

//brackets
OPEN_PARENTHESES: '(' ;
CLOSE_PARENTHESES: ')' ;
OPEN_SQUARE_BRACKET: '[' ;
CLOSE_SQUARE_BRACKET: ']' ;

//separators
SEMI_COLON: ';' ;
COMMA: ',' ;

//bool-values
TRUE: 'true' ;
FALSE: 'false' ;

//identifier
IDENTIFIER: [_a-zA-Z] [_a-zA-Z0-9]* ;

//comments
COMMENT: '#' ~([\r\n])* [\r\n] -> skip ;

//whitespace
WHITESPACE: [ \n\t\r] -> skip ;

//fragments for literals
fragment SINGLE_QUOTE: '\'' ;
fragment DOUBLE_QUOTE: '"' ;
fragment BACKSLASH: '\\' ;
fragment ESCAPE_CHAR: '0'
| 'b'
| 't'
| 'n'
| 'f'
| 'r'
| SINGLE_QUOTE
| DOUBLE_QUOTE
| BACKSLASH ;
fragment CHARACTER: ~('\\' | '\'' | '"') | BACKSLASH ESCAPE_CHAR ;

//literals
INTEGER: (PLUS | MINUS)? [0-9]+ ;
CHARACTER_LITERAL: SINGLE_QUOTE CHARACTER SINGLE_QUOTE ;
STRING_LITERAL: DOUBLE_QUOTE (CHARACTER)* DOUBLE_QUOTE ;
