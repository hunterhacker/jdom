/*--

 Copyright 2000 Brett McLaughlin & Jason Hunter. All rights reserved.

 Redistribution and use in source and binary forms, with or without modifica-
 tion, are permitted provided that the following conditions are met:

 1. Redistributions of source code must retain the above copyright notice,
    this list of conditions, and the following disclaimer.

 2. Redistributions in binary form must reproduce the above copyright notice,
    this list of conditions, the disclaimer that follows these conditions,
    and/or other materials provided with the distribution.

 3. The names "JDOM" and "Java Document Object Model" must not be used to
    endorse or promote products derived from this software without prior
    written permission. For written permission, please contact
    license@jdom.org.

 4. Products derived from this software may not be called "JDOM", nor may
    "JDOM" appear in their name, without prior written permission from the
    JDOM Project Management (pm@jdom.org).

 THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 FITNESS  FOR A PARTICULAR  PURPOSE ARE  DISCLAIMED.  IN NO  EVENT SHALL  THE
 JDOM PROJECT  OR ITS CONTRIBUTORS  BE LIABLE FOR  ANY DIRECT, INDIRECT,
 INCIDENTAL, SPECIAL,  EXEMPLARY, OR CONSEQUENTIAL  DAMAGES (INCLUDING, BUT
 NOT LIMITED TO, PROCUREMENT  OF SUBSTITUTE GOODS OR SERVICES; LOSS
 OF USE, DATA, OR  PROFITS; OR BUSINESS  INTERRUPTION)  HOWEVER CAUSED AND ON
 ANY  THEORY OF LIABILITY,  WHETHER  IN CONTRACT,  STRICT LIABILITY,  OR TORT
 (INCLUDING  NEGLIGENCE OR  OTHERWISE) ARISING IN  ANY WAY OUT OF THE  USE OF
 THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 This software  consists of voluntary contributions made  by many individuals
 on  behalf of the Java Document Object Model Project and was originally
 created by Brett McLaughlin <brett@jdom.org> and
 Jason Hunter <jhunter@jdom.org>. For more  information on the JDOM
 Project, please see <http://www.jdom.org/>.

 */

package org.jdom.test;

import java.lang.reflect.Method;
import java.lang.reflect.Constructor;

final class NameMangler {

	/** Default prefixes for various types */
	private static final String PREFIX_CLASS		= "TestCase";
	private static final String PREFIX_CONSTRUCTOR	= "testConstructor";
	private static final String PREFIX_METHOD		= "testMethod";

	/** No instances */
	private NameMangler() {}

	/**
	 * Creates a Mangled method name which represents the Method.
	 * 
	 * Given a method such as "Element getChild(String name, String uri )"
	 * 
	 * The following mangled method name will be created.
	 *
	 * 'prefix'_Element_getChild_String_String.
	 *
	 * @param prefix	The prefix to the method name.
	 * @param method	The Method to mangle.
	 * @return A mangled method name.
	 */
	public static String getMangledName( Method method, String prefix ) {
		StringBuffer buffer = new StringBuffer( prefix );
		

		// Create the prefix.
		buffer.append( "_" ).
			   append( chop(method.getReturnType().getName()) ).
			   append( "_" ).
			   append( chop(method.getName()) );
		
		// Add the method arguments, if any.
		Class[] params = method.getParameterTypes();
		if ( params.length > 0 ) {
			buffer.append( "_" ).
				   append( mangleParams( params ) );

		}

		return buffer.toString();
	}

	/**
	 * Create a mangled name using the default prefix.
	 */
	public static String getMangledName( Method method ) {
		return getMangledName( method, PREFIX_METHOD );
	}

	/**
	 * Create a mangled name form a Constructor.
	 *
	 */
	public static String getMangledName( Constructor con, String prefix ) {
		StringBuffer buffer = new StringBuffer( prefix );
		
		// Create the prefix
		buffer.append( "_").
			   append( chop(con.getName()) );

		// Add the Constructor arguments, if any.
		Class[] params = con.getParameterTypes();
		if ( params.length > 0 ) {
			buffer.append( "_" ).
				   append( mangleParams( params ) );
		}

		return buffer.toString();
	}

	public static String getMangledName( Constructor con ) {
		return getMangledName( con, PREFIX_CONSTRUCTOR );
	}

	/**
	 *
	 */
	public static String getMangledName( Class cls, String prefix ) {
		return new StringBuffer( prefix ).
					append( "_" ).
					append( chop( cls.getName() )).toString();
	}

	public static String getMangledName( Class cls ) {
		return getMangledName( cls, PREFIX_CLASS );
	}

	public static String chop( String full ) {
		int i = full.lastIndexOf( '.' );
		if ( i >0 ) {
			return full.substring( i+1 );
		}
		return full;
	}

	/**
	 * Creates a mangled String containing the classes passed.
	 *
	 * @param params	The classes to mangle.
	 */
	public static String mangleParams( Class[] params ) {
		StringBuffer buffer = new StringBuffer();

		for( int i=0; i< params.length; i++ ) {
			buffer.append( "_" ).
				   append( chop( params[i].getName() ));
		}

		return buffer.toString();
	}
}


