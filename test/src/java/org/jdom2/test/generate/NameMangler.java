package org.jdom.test.generate;

/*-- 

 Copyright (C) 2000 Brett McLaughlin & Jason Hunter. All rights reserved.
 
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

import java.lang.reflect.Method;
import java.lang.reflect.Constructor;


/**
 * <code>NameMangler</code> is designed to take a name and mangle it into 
 * something a little more useful.
 *
 * @author Jools Enticknap
 * @version 1.00
 */
final class NameMangler {

	/**
	 * If the class is in one of these packages just ignore them as it
	 * will be pretty obvious where they came from.
	 */
	private static String[] packageIgnore = {
		"java.lang",
		"java.util",
		"java.net",
		"java.io"
	};

	/** No instances */
	private NameMangler() {}
	/**
	 * Return a name which represents the supplied constructor.
	 *
	 * @param ctor   The constructor object to mangle.
	 * @param buffer The buffer to place the result into.
	 * @return A StringBuffer containing the mangled name.
	 */
	static StringBuffer getMangledName(Constructor ctor, StringBuffer buffer) {
		// Add parameters if there are any.
		Class[] argTypes = ctor.getParameterTypes();
		if (argTypes.length>0) {
			getNormalized(argTypes, buffer);
		}

		return buffer;
	}
	/**
	 * Return a string representation of the <code>Method</code> object.
	 *
	 * @param method	The method to use for the mangling.
	 * @param buffer	The buffer to append the mangled name to.
	 * @return A buffer with the mangled name appended.
	 */
	static StringBuffer getMangledName(Method method, StringBuffer buffer) {
		// Get the return type in
		Class retType = method.getReturnType();	
		getNormalized(retType, buffer).append('_');

		// Get the name of the method.
		buffer.append(method.getName());

		// Add parameters if there are any.
		Class[] argTypes = method.getParameterTypes();
		if (argTypes.length>0) {
			getNormalized(argTypes, buffer);
		}

		return buffer;
	}
	/**
	 * Normalize a set of classes and put the output into a supplied buffer.
	 *
	 * @param classes An array of class names to normalize
	 * @param buffer  The target for the strings.
	 * @return A StringBuffer containg the normalized class names.
	 */
	static StringBuffer getNormalized(Class[] classes, StringBuffer buffer) {
		buffer.append('_');

		for (int i = 0; i<classes.length; i++) {
			getNormalized(classes[i], buffer);
			if (i+1 < classes.length) {
				buffer.append('_');
			}
		}
		
		return buffer;
	}
	/**
	 * Normalize a class and put the output into a supplied buffer.
	 *
	 * @param classes An array of class names to normalize
	 * @param buffer  The target for the strings.
	 * @return A StringBuffer containg the normalized class names.
	 */
	static StringBuffer getNormalized(Class cls, StringBuffer buffer) {
		String name = cls.getName();

		// Check for arrays, pesky things......
		// Why of why do we end up with JNI signatures for arrays ?
		if (name.startsWith("[") && name.endsWith(";") ) {
			name=name.substring(2);
			name=name.substring(0,name.length()-1);
			buffer.append("Array");
		}

		int offset;
		boolean hasPackage = !((offset=name.lastIndexOf('.')) < 0);
		// Check package name, and normalize it.
		if (hasPackage) {
			String clsName = name.substring(offset+1);
			String packageName = name.substring(0,offset);
			
			boolean ignorePackage = false;
			for (int i=0; i<packageIgnore.length; i++) {
				if (packageName.equals(packageIgnore[i])) {
					ignorePackage = true;
					break;
				}
			}

			if (!ignorePackage) {
				packageName.toCharArray();
				char c[] = packageName.toCharArray();
				boolean upper = true;
				for (int i=0; i<c.length; i++) {
					if (c[i] == '.') {
						upper = true;
						continue;
					}
					if (upper) {
						buffer.append(Character.toUpperCase(c[i]));
						upper = false;
					} else {
						buffer.append(c[i]);
					}
				}
			}

			buffer.append(clsName);
		} else {
			buffer.append(name);
		}

		return buffer;
	}
}
