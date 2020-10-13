grammar ICSS;

//--- PARSER: ---

/*
 * Een declarationBlock mag leeg zijn
 * Variable assignments kunnen overal in de styelesheet
 *
 * Een if en else moeten brackets hebben, dus geen else if () {}
 *
 */

stylesheet: (variableAssignment | stylerule)* EOF;

// Variables
variableAssignment: variableReference ASSIGNMENT_OPERATOR expression SEMICOLON;
variableReference: CAPITAL_IDENT;

// Stylerules
stylerule: selector OPEN_BRACE declarationBlock CLOSE_BRACE;

declarationBlock: (ifClause | declaration | variableAssignment)*;
declaration: propertyName COLON expression SEMICOLON;
propertyName: LOWER_IDENT;

// If else
ifClause: IF BOX_BRACKET_OPEN expression BOX_BRACKET_CLOSE OPEN_BRACE declarationBlock CLOSE_BRACE elseClause?;
elseClause: ELSE OPEN_BRACE declarationBlock CLOSE_BRACE;

// Expressions
expression: expression MUL expression |
            expression (PLUS | MIN) expression |
            variableReference |
            literal;

operation: multiplyOperation | addSubstractOperation;
multiplyOperation: (variableReference | literal) MUL expression;
addSubstractOperation: expression (PLUS | MIN) expression;

// Low level
selector: idSelector | classSelector | tagSelector;
idSelector: ID_IDENT;
classSelector: CLASS_IDENT;
tagSelector: LOWER_IDENT;

literal: boolLiteral | colorLiteral | percentageLiteral | pixelLiteral | scalarLiteral;
boolLiteral: TRUE | FALSE;
colorLiteral: COLOR;
percentageLiteral: PERCENTAGE;
pixelLiteral: PIXELSIZE;
scalarLiteral: SCALAR;


//--- LEXER: ---

// IF support:
IF: 'if';
ELSE: 'else';
BOX_BRACKET_OPEN: '[';
BOX_BRACKET_CLOSE: ']';


// Literals
TRUE: 'TRUE';
FALSE: 'FALSE';
PIXELSIZE: [0-9]+ 'px';
PERCENTAGE: [0-9]+ '%';
SCALAR: [0-9]+;


// Color value takes precedence over id idents
COLOR: '#' [0-9a-f] [0-9a-f] [0-9a-f] [0-9a-f] [0-9a-f] [0-9a-f];

// Specific identifiers for id's and css classes
ID_IDENT: '#' [a-z0-9\-]+;
CLASS_IDENT: '.' [a-z0-9\-]+;

// General identifiers
LOWER_IDENT: [a-z] [a-z0-9\-]*;
CAPITAL_IDENT: [A-Z] [A-Za-z0-9_]*;

// All whitespace is skipped
WS: [ \t\r\n]+ -> skip;

//
OPEN_BRACE: '{';
CLOSE_BRACE: '}';
SEMICOLON: ';';
COLON: ':';
PLUS: '+';
MIN: '-';
MUL: '*';
ASSIGNMENT_OPERATOR: ':=';
