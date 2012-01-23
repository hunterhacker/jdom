/*-- 

 Copyright (C) 2000-2007 Jason Hunter & Brett McLaughlin.
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

package org.jdom2;


/**
 * An general unchecked exception that JDOM classes can throw. There are a
 * number of more specialised exceptions that JDOM throws which are subclasses
 * of this class.
 * <p>
 * JDOM2 has changed a number of methods that used to throw the checked
 * {@link JDOMException} in JDOM 1.x to instead throw a specialised unchecked
 * exception. These changes introduce a compatibility problem: existing code
 * could catch the JDOMException, but that exception is no longer thrown,
 * leading to unreachable code blocks.
 * <p>
 * In all instances where existing JDOM 1.x code was revised in JDOM2 to throw
 * an unchecked exception, the new unchecked exception is a subclass of this
 * JDOMRuntimeException. As a result, where unreachable blocks arise in a 
 * JDOM2 migration process, one alternative is to simply change the failing
 * <code>catch (JDOMException ...)</code> to be
 * <code>catch (JDOMRuntimeException ...)</code>
 * <p>
 *
 * @author  Rolf Lear
 */
public class JDOMRuntimeException extends RuntimeException {

	/**
	 * This will create an <code>Exception</code>.
	 */
	public JDOMRuntimeException() {
		super("Unchecked exception occurred in JDOM library.");
	}

	/**
	 * This will create an <code>Exception</code> with the given message.
	 *
	 * @param message <code>String</code> message indicating
	 *                the problem that occurred.
	 */    
	public JDOMRuntimeException(String message)  {
		super(message);
	}

	/**
	 * This will create an <code>Exception</code> with the given message
	 * and wrap another <code>Exception</code>.  This is useful when
	 * the originating <code>Exception</code> should be held on to.
	 *
	 * @param message <code>String</code> message indicating
	 *                the problem that occurred.
	 * @param cause <code>Throwable</code> that caused this
	 *                  to be thrown.
	 */    
	public JDOMRuntimeException(String message, Throwable cause)  {
		super(message, cause);    
	}    

}
