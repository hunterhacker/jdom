
header
{
	package org.jdom.contrib.xpath.parser;
}

class XPathLexer extends Lexer;
	options
	{
		k = 3;
		importVocab=xpath;
	}

	tokens
	{
		KW_OR = "or";
		KW_AND = "and";
	}

WS
	:
		('\n' | ' ' | '\t' | '\r')+
		{
			$setType(Token.SKIP);
		}
	;

protected
DIGIT
	:
		('0'..'9')
	;

protected 
SINGLE_QUOTE_STRING
	:
		'\'' (~('\''))* '\''
	;

protected
DOUBLE_QUOTE_STRING
	:
		'"' (~('"'))* '"'
	;

LITERAL
	:
		SINGLE_QUOTE_STRING | DOUBLE_QUOTE_STRING
	;

NUMBER
	:
		(DIGIT)+ ('.' (DIGIT)+)?
	;

IDENTIFIER

	options
	{
		testLiterals=true;
	}

	: 	
		('a'..'z'|'A'..'Z') ('a'..'z'|'A'..'Z'|'-')*	
	;

LEFT_PAREN
	:	'('		;

RIGHT_PAREN	
	:	')'		;

LEFT_BRACKET
	:	'['		;

RIGHT_BRACKET	
	:	']'		;
	
PIPE
	:	'|'		;

DOT
	:	'.'		;

DOT_DOT
	:	".."	;

AT
	:	'@'		;

COMMA
	:	','		;

DOUBLE_COLON
	:	"::"	;

SLASH
	:	'/'		;

DOUBLE_SLASH
	:	'/' '/'	;

PLUS
	:	'+'		;

MINUS
	:	'-'		;

EQUALS
	:	'='		;

NOT_EQUALS
	:	"!="	;

LT
	:	'<'		;

LTE
	:	"<="	;

GT
	:	'>'		;

GTE
	:	">="	;

STAR
	:	'*'		;
