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

	import org.jdom.contrib.xpath.XPathExpr;
	import org.jdom.contrib.xpath.impl.*;
}

class XPathRecognizer extends Parser;
	options
	{
		k = 2;
		exportVocab=xpath;
	}

	// ----------------------------------------
	// Helpful methods

	{
		private XPathExpr makeBinaryExpr(BinaryExpr.Op op, XPathExpr lhs, XPathExpr rhs)
		{
			if ( op == null ) {
				return lhs;
			}

			return new BinaryExpr(op, lhs, rhs);
		}
	}

xpath returns [XPathExpr expr]
	{
		expr = null;
	}
	:
		expr=union_expr
	;

location_path returns [LocationPath path]
	{
		path = null;
	}
	:
			path=absolute_location_path
		|	path=relative_location_path
	;

absolute_location_path returns [LocationPath path]
	{
		path = null;
	}
	:
		(	SLASH^
		|	DOUBLE_SLASH^
		)
		(	(STAR|IDENTIFIER)=>
			path=relative_location_path
		)?

		{
			if ( path == null ) {
				path = new LocationPath();
			}

			path.isAbsolute(true);
		}
	;

relative_location_path returns [LocationPath path]
	{
		path = new LocationPath();

		Step step = null;
	}
	:
		step=step
		{
			path.addStep(step);
		}
		(	(	SLASH^
			|	DOUBLE_SLASH^
			) 	step=step
				{
					path.addStep(step);
				}
		)*
	;

step returns [Step step]
	{
	    step             = null;
		String axis      = "child";
		String localName = null;
		String prefix    = "";
		String nodeType  = "element";
		Predicate pred   = null;
	}
	:
		(	localName=abbr_step
			{
					step = new Step(axis, nodeType, prefix, localName);
			}
		|
			(	(IDENTIFIER|AT)=> axis=axis
			|
			)
			(	(	ns:IDENTIFIER COLON 
					{
						prefix = ns.getText();
					}
				)? id:IDENTIFIER
				{
					localName = id.getText();
				}
			|	STAR
				{
					localName = "*";
				}
			)
			{
				step = new Step(axis, nodeType, prefix, localName);
			}
			(
				pred=predicate
				{
					step.addPredicate(pred);
				}
			|	function_call
			|	// default simple case
			)
		)
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
predicate returns [Predicate pred]
	{
		pred = null;
	}
	:
		LEFT_BRACKET^ pred=predicate_expr RIGHT_BRACKET!
	;

// .... production [9] ....
//
predicate_expr returns [Predicate pred]
	{
		pred = null;
		XPathExpr expr = null;
	}
	:
		expr=expr
		{
			pred = new Predicate(expr);
		}
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
expr returns [XPathExpr expr]
	{
		expr = null;
	}
	:
		expr=or_expr
	;

// .... production [15] ....
//
primary_expr returns [XPathExpr expr]
	{
		expr = null;
	}
	:
			variable_reference
		|	LEFT_PAREN! expr=expr RIGHT_PAREN!
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
	{
		XPathExpr expr = null;
	}
	:
		expr=argument ( COMMA expr=argument )*
	;

// .... production [17] ....
//
argument returns [XPathExpr expr]
	{
		expr = null;
	}
	:
		expr=expr
	;

// ----------------------------------------
//		Section 3.3
//			Node-sets
// ----------------------------------------

// .... production [18] ....
//
union_expr returns [XPathExpr expr]
	{
		expr = null;

		PathExpr lhs = null;
		PathExpr rhs = null;
		BinaryExpr.Op op = null;
	}
	:
		lhs=path_expr
		( 	PIPE! 			{ op=BinaryExpr.Op.UNION; }
			rhs=path_expr
		)*

		{
			expr = makeBinaryExpr(op, lhs, rhs);
		}
	;

// .... production [19] ....
//

path_expr returns [PathExpr expr]
	{
		expr = null;
		XPathExpr filt = null;
	}
	:
			expr=location_path
		|	filt=filter_expr ( expr=absolute_location_path )?
	;

// .... production [20] ....
//
filter_expr returns [ XPathExpr expr ]
	{
		expr = null;
		Predicate pred = null;
	}
	:
		expr=primary_expr ( pred=predicate )*
	;


// ----------------------------------------
//		Section 3.4
//			Booleans
// ----------------------------------------

// .... production [21] ....
//
or_expr returns [XPathExpr expr]
	{
		expr = null;
		XPathExpr lhs = null;
		XPathExpr rhs = null;
		BinaryExpr.Op op = null;
	}
	:
		lhs=and_expr (	KW_OR^				{ op = BinaryExpr.Op.OR; }
						rhs=and_expr )?
		{
			expr = makeBinaryExpr(op, lhs, rhs);
		}
	;

// .... production [22] ....
//
and_expr returns [XPathExpr expr]
	{
		expr = null;
		XPathExpr lhs = null;
		XPathExpr rhs = null;
		BinaryExpr.Op op = null;
	}
	:
		lhs=equality_expr (	KW_AND^ 			{ op = BinaryExpr.Op.AND; }
							rhs=equality_expr )?

		{
			expr = makeBinaryExpr(op, lhs, rhs);
		}
	;

// .... production [23] ....
//
equality_expr returns [XPathExpr expr]
	{
		expr = null;
		XPathExpr lhs = null;
		XPathExpr rhs = null;
		BinaryExpr.Op op = null;
	}
	:
		lhs=relational_expr (	(	EQUALS^		{ op = BinaryExpr.Op.EQUAL; }
								|	NOT_EQUALS^	{ op = BinaryExpr.Op.NOT_EQUAL; }
								)
								rhs=relational_expr
							)?
		{
			expr = makeBinaryExpr(op, lhs, rhs);
		}
	;

// .... production [24] ....
//
relational_expr returns [XPathExpr expr]
	{
		expr = null;
		XPathExpr lhs = null;
		XPathExpr rhs = null;
		BinaryExpr.Op op = null;

	}
	:
		lhs=additive_expr	(	(	LT^		{ op = BinaryExpr.Op.LT; }
								|	GT^		{ op = BinaryExpr.Op.GT; }
								|	LTE^	{ op = BinaryExpr.Op.LT_EQUAL; }
								|	GTE^	{ op = BinaryExpr.Op.GT_EQUAL; }
								)
								rhs=additive_expr
							)?
		{
			expr = makeBinaryExpr(op, lhs, rhs);
		}
	;

// ----------------------------------------
//		Section 3.5
//			Numbers
// ----------------------------------------

// .... production [25] ....
//
additive_expr returns [XPathExpr expr]
	{
		expr = null;
		XPathExpr lhs = null;
		XPathExpr rhs = null;
		BinaryExpr.Op op = null;
	}
	:
		lhs=mult_expr	(	(	PLUS^	{ op = BinaryExpr.Op.PLUS; }
							|	MINUS^	{ op = BinaryExpr.Op.MINUS; }
							)
							rhs=mult_expr
						)?

		{
			expr = makeBinaryExpr(op, lhs, rhs);
		}
	;

// .... production [26] ....
//
mult_expr returns [XPathExpr expr]
	{
		expr = null;
		XPathExpr lhs = null;
		XPathExpr rhs = null;
		BinaryExpr.Op op = null;
	}
	:
		lhs=unary_expr	(	(	STAR^	{ op = BinaryExpr.Op.MULTIPLY; }
							|	DIV^	{ op = BinaryExpr.Op.DIV; }
							|	MOD^	{ op = BinaryExpr.Op.MOD; }
							)
							rhs=unary_expr
						)?

		{
			expr = makeBinaryExpr(op, lhs, rhs);
		}
	;

// .... production [27] ....
//
unary_expr returns [XPathExpr expr]
	{
		expr = null;
	}
	:
			expr=union_expr
		|
			MINUS expr=unary_expr
			{
				expr = new NegativeUnaryExpr(expr);
			}
	;
