/*--

 Copyright (C) 2000-2004 Jason Hunter & Brett McLaughlin.
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions
 are met:

 1. Redistributions of source code must retain the above copyright
    notice, this list of conditions, and the following disclaimer.

 2. Redistributions in binary form must reproduce the above copyright
    notice, this list of conditions, and the disclaimer that follows
    these conditions in the documentation and/or other materials
    provided with the distribution.

 3. The name "JDOM" must not be used to endorse or promote products
    derived from this software without prior written permission.  For
    written permission, please contact <request_AT_jdom_DOT_org>.

 4. Products derived from this software may not be called "JDOM", nor
    may "JDOM" appear in their name, without prior written permission
    from the JDOM Project Management <request_AT_jdom_DOT_org>.

 In addition, we request (but do not require) that you include in the
 end-user documentation provided with the redistribution and/or in the
 software itself an acknowledgement equivalent to the following:
     "This product includes software developed by the
      JDOM Project (http://www.jdom.org/)."
 Alternatively, the acknowledgment may be graphical using the logos
 available at http://www.jdom.org/images/logos.

 THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED.  IN NO EVENT SHALL THE JDOM AUTHORS OR THE PROJECT
 CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 SUCH DAMAGE.

 This software consists of voluntary contributions made by many
 individuals on behalf of the JDOM Project and was originally
 created by Jason Hunter <jhunter_AT_jdom_DOT_org> and
 Brett McLaughlin <brett_AT_jdom_DOT_org>.  For more information
 on the JDOM Project, please see <http://www.jdom.org/>.

 */

package org.jdom2.contrib.beans;

import java.lang.reflect.*;
import java.util.*;

/**
 * Converts a String into a given object type.
 *
 * @author Alex Chaffee (alex@jguru.com)
 **/

@SuppressWarnings("javadoc")
public class StringConverter {

	public static interface Factory {
		public Object instantiate(String string);
	}

	public static class DateFactory implements Factory
	{
		@Override
		public Object instantiate(String string) {
			return DateUtils.parseDate(string);
		}
	}

	protected Map<Class<?>, Factory> factories = new HashMap<Class<?>, Factory>();

	public StringConverter() {
		try {
			factories.put( Class.forName("java.util.Date"),
					new DateFactory() );
		}
		catch (ClassNotFoundException e) {
			// do nothing
		}
	}

	public void setFactory(Class<?> type, Factory factory) {
		factories.put(type, factory);
	}

	protected static Class<?>[] argString = new Class[] { String.class };

	public Object parse(String string, Class<?> type)
	{
		// if it's a string, return it
		if (type == String.class) {
			return string;
		}

		// if we have a Factory for it
		Factory factory = factories.get(type);
		if (factory != null) {
			return factory.instantiate(string);
		}

		// if it's a primitive, convert to wrapper (???)
		if (type == short.class) type = Short.class;
		if (type == int.class) type = Integer.class;
		if (type == long.class) type = Long.class;
		if (type == boolean.class) type = Boolean.class;
		if (type == char.class) type = Character.class;
		if (type == byte.class) type = Byte.class;

		// last ditch: see if the class has a String Factory
		try {
			Constructor<?> c = type.getConstructor(argString);
			if (c != null) {
				return c.newInstance( new Object[] { string } );
			}
		}
		catch (NoSuchMethodException e) {
			// ignore & fall through
		}
		catch (Exception e) {
			System.err.println("Couldn't instantiate " + type + "(" + string + ")");
			e.printStackTrace();
		}

		return null;
	}
}
