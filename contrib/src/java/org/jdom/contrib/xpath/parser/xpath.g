/*

This is an antlr (www.antlr.org) grammar specifying
a recognizer/parser/lexer group compliant with the
W3C Recommendation known as:

	XML Path Language (XPath)
	Version 1.0
	W3C Recommendation 16 November 1999

	http://www.w3c.org/TR/1999/REC-xpath-19991116

It is annotated with both implementation comments,
and references to the XPath standard's production
rules for recognizing parts of an xpath.

 author: bob mcwhirter (bob@werken.com)
license: BSD/MIT

*/

header
{
	package org.jdom.contrib.xpath.parser;
	
	import org.jdom.contrib.xpath.XPathHandler;
	import org.jdom.contrib.xpath.XPathPredicateHandler;

	import org.jdom.contrib.xpath.NoOpXPathHandler;
	import org.jdom.contrib.xpath.NoOpXPathPredicateHandler;

	import java.util.Stack;
}

class XPathRecognizer extends Parser;

	options 
	{
		k = 2;
		exportVocab=xpath;
	}

	{

		private Stack _pathHandlers = new Stack();
		private Stack _predicateHandlers = new Stack();

		private XPathHandler currentPathHandler()
		{
			return (XPathHandler) _pathHandlers.peek();
		}

		private XPathPredicateHandler currentPredicateHandler()
		{
			return (XPathPredicateHandler) _predicateHandlers.peek();
		}

		private void pushPathHandler(XPathHandler pathHandler)
		{
			_pathHandlers.push(pathHandler);
		}

		private void popPathHandler()
		{
			_pathHandlers.pop();
		}

		private void pushPredicateHandler(XPathPredicateHandler predHandler)
		{
			_predicateHandlers.push(predHandler);
		}

		private void popPredicateHandler()
		{
			_predicateHandlers.pop();
		}

		public void setHandler(XPathHandler pathHandler)
		{
			pushPathHandler(pathHandler);
		}

	}

xpath 
	:
		{
			currentPathHandler().startParsingXPath();
		}
		union_expr
		{
			currentPathHandler().endParsingXPath();
		}
	;

location_path
	:
			absolute_location_path
		|	relative_location_path
	;

absolute_location_path
	:
		{
			currentPathHandler().absolute();
		}
		(	SLASH^
		|	DOUBLE_SLASH^
		)
		( (STAR|IDENTIFIER)=> relative_location_path )?
	;

relative_location_path
	:
		step 
		(	(	SLASH^
			|	DOUBLE_SLASH^
			) step 
		)*
	;

step
	{
		String axisName = null;
		String prefix = null;
		String localName = null;
	}
	:
		{
			currentPathHandler().startStep();
		}
		(	 localName=abbr_step
		|	
			(	(IDENTIFIER|AT)=> axisName=axis
			| 
			)	
						(	id:IDENTIFIER 
							{
								localName = id.getText();
							}
						|	STAR
							{
								localName = "*";
							}
						)	
						(	
							{
								currentPathHandler().nameTest(axisName, 
												prefix, 
												localName);
							}
							predicate	
						|	function_call 
							{
								// FIXME
							}
						|	// default simple case
							{
								currentPathHandler().nameTest(axisName, 
												prefix, 
												localName);
							}
						)
		)
		{
			currentPathHandler().endStep();
		}
	;

axis returns [String axisName]
	{
		axisName = null;
	}
	:
		(	id:IDENTIFIER DOUBLE_COLON^
			{
				axisName = id.getText();
			}
		// FIXME
		|	AT
		)
	;

// ----------------------------------------
//		Section 2.4
//			Predicates
// ----------------------------------------

// .... production [8] ....
//
predicate
	:
		{
			pushPredicateHandler( currentPathHandler().startPredicate() );
		}
		LEFT_BRACKET^ predicate_expr RIGHT_BRACKET!
		{
			currentPathHandler().endPredicate();
			popPredicateHandler();
		}
	;

// .... production [9] ....
//
predicate_expr
	:
		expr
	;

// .... production [12] ....
//
abbr_step returns [String name]
	{
		name = null;
	}
	:
		(	DOT^		{ name = "."; }
		|	DOT_DOT^	{ name = ".."; }
		)
	;

// .... production [13] ....
//
abbr_axis_specifier
	:
		( AT )?
	;


// ----------------------------------------
//		Section 3
//			Expressions
// ----------------------------------------

// ----------------------------------------
//		Section 3.1
//			Basics
// ----------------------------------------

// .... production [14] ....
//
expr
	:
		or_expr
	;

// .... production [15] ....
//
primary_expr 
	:
			variable_reference
		|	LEFT_PAREN! expr RIGHT_PAREN!
		|	literal
		|	number
		//|	IDENTIFIER LEFT_PAREN RIGHT_PAREN
	;

literal
	:
		LITERAL^
	;

number
	:
		NUMBER^
	;

variable_reference
	:
		DOLLAR_SIGN^ IDENTIFIER
	;

// ----------------------------------------
//		Section 3.2
//			Function Calls	
// ----------------------------------------

// .... production [16] ....
//
function_call
	:
		LEFT_PAREN^ ( arg_list )? RIGHT_PAREN!
	;

// .... production [16.1] ....
//
arg_list
	:
		argument ( COMMA argument )*
	;

// .... production [17] ....
//
argument
	:
		expr
	;

// ----------------------------------------
//		Section 3.3
//			Node-sets
// ----------------------------------------

// .... production [18] ....
//
union_expr
	:
		{
			// FIXME push handlers?
			currentPathHandler().startPath();
		}
		path_expr 
		{
			currentPathHandler().endPath();
		}
		( 	PIPE! 
			{
				currentPathHandler().startPath();
			}
			path_expr 
			{
				currentPathHandler().endPath();
			}
		)*
	;

// .... production [19] ....
//

path_expr
	:
			location_path
		|	filter_expr ( absolute_location_path )?
	;

// .... production [20] ....
//
filter_expr
	:
		primary_expr ( predicate )*
	;


// ----------------------------------------
//		Section 3.4
//			Booleans
// ----------------------------------------

// .... production [21] ....
//
or_expr
	:
		and_expr (	KW_OR^ 		
					{	
						currentPredicateHandler().or(); 
					}
					and_expr )?
	;

// .... production [22] ....
//
and_expr
	:
		equality_expr (	KW_AND^ 
						{
							currentPredicateHandler().and();
						}
						equality_expr )?
	;

// .... production [23] ....
//
equality_expr
	:
		relational_expr (	(	EQUALS^ 
								{
									currentPredicateHandler().equals();
								}
							|	NOT_EQUALS^
								{
									currentPredicateHandler().notEquals();
								}
							) 
							relational_expr
						)?
	;

// .... production [24] ....
//
relational_expr
	:
		additive_expr	(	(	LT^
								{
									currentPredicateHandler().lessThan();
								}
							|	GT^
								{
									currentPredicateHandler().greaterThan();
								}
							|	LTE^
								{
									currentPredicateHandler().lessThanEquals();
								}
							|	GTE^
								{
									currentPredicateHandler().greaterThanEquals();
								}
							)
							additive_expr
						)?
	;

// ----------------------------------------
//		Section 3.5
//			Numbers
// ----------------------------------------

// .... production [25] ....
//
additive_expr
	:
		mult_expr	(	(	PLUS^
							{
								currentPredicateHandler().plus();
							}
						|	MINUS^
							{
								currentPredicateHandler().minus();
							}
						)
						mult_expr
					)?
	;

// .... production [26] ....
//
mult_expr
	:
		unary_expr	(	(	STAR^
							{
								currentPredicateHandler().multiply();
							}
						|	DIV^
							{
								currentPredicateHandler().divide();
							}
						|	MOD^
							{
								currentPredicateHandler().modulo();
							}
						)
						unary_expr
					)?
	;

// .... production [27] ....
//
unary_expr
	:
			union_expr 
		|	
			{
				currentPredicateHandler().negative();
			}
			MINUS unary_expr 
	;
