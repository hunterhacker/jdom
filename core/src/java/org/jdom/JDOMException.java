/*-- 

 $Id: JDOMException.java,v 1.7 2001/04/27 18:21:20 jhunter Exp $

 Copyright (C) 2000 Brett McLaughlin & Jason Hunter.
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
    written permission, please contact license@jdom.org.
 
 4. Products derived from this software may not be called "JDOM", nor
    may "JDOM" appear in their name, without prior written permission
    from the JDOM Project Management (pm@jdom.org).
 
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
 created by Brett McLaughlin <brett@jdom.org> and 
 Jason Hunter <jhunter@jdom.org>.  For more information on the 
 JDOM Project, please see <http://www.jdom.org/>.
 
 */

package org.jdom;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * <b><code>JDOMException</code></b>
 * <p>
 * This <code>Exception</code> subclass is the top level
 *   <code>Exception</code> that JDOM classes
 *   can throw.  It's subclasses add specificity to the 
 *   problems that can occur using JDOM, but this single
 *   <code>Exception</code> can be caught to handle all
 *   JDOM specific problems.
 * </p>
 * 
 * @author Brett McLaughlin
 * @author Jason Hunter
 * @version 1.0
 */
public class JDOMException extends Exception {

    private static final String CVS_ID = 
      "@(#) $RCSfile: JDOMException.java,v $ $Revision: 1.7 $ $Date: 2001/04/27 18:21:20 $ $Name:  $";

    /** A wrapped <code>Throwable</code> */
    protected Throwable rootCause;

    /**
     * <p>
     * This will create an <code>Exception</code>.
     * </p>
     */
    public JDOMException() {
        super("Error occurred in JDOM application.");
    }

    /**
     * <p>
     * This will create an <code>Exception</code> with the given message.
     * </p>
     *
     * @param message <code>String</code> message indicating
     *                the problem that occurred.
     */    
    public JDOMException(String message)  {
        super(message);
    }

    /**
     * <p>
     * This will create an <code>Exception</code> with the given message
     *   and wrap another <code>Exception</code>.  This is useful when
     *   the originating <code>Exception</code> should be held on to.
     * </p>
     *
     * @param message <code>String</code> message indicating
     *                the problem that occurred.
     * @param exception <code>Exception</code> that caused this
     *                  to be thrown.
     */    
    public JDOMException(String message, Throwable rootCause)  {
        super(message);    
        this.rootCause = rootCause;
    }    

    /**
     * <p>
     * This returns the message for the <code>Exception</code>. If
     *   there is a root cause, the message associated with the root
     *   <code>Exception</code> is appended.
     * </p>
     *
     * @return <code>String</code> - message for <code>Exception</code>.
     */
    public String getMessage() {
        if (rootCause != null) {
            return super.getMessage() + ": " + rootCause.getMessage();
        } else {
            return super.getMessage();
        }
    }

    /**
     * <p>
     * This prints the stack trace of the <code>Exception</code>. If
     *   there is a root cause, the stack trace of the root
     *   <code>Exception</code> is printed right after.
     * </p>
     */
    public void printStackTrace() {
        super.printStackTrace();
        if (rootCause != null) {
            System.err.print("Root cause: ");
            rootCause.printStackTrace();
        }
    }

    /**
     * <p>
     * This prints the stack trace of the <code>Exception</code> to the given
     *   PrintStream. If there is a root cause, the stack trace of the root
     *   <code>Exception</code> is printed right after.
     * </p>
     */
    public void printStackTrace(PrintStream s) {
        super.printStackTrace(s);
        if (rootCause != null) {
            s.print("Root cause: ");
            rootCause.printStackTrace(s);
        }
    }

    /**
     * <p>
     * This prints the stack trace of the <code>Exception</code> to the given
     *   PrintWriter. If there is a root cause, the stack trace of the root
     *   <code>Exception</code> is printed right after.
     * </p>
     */
    public void printStackTrace(PrintWriter w) {
        super.printStackTrace(w);
        if (rootCause != null) {
            w.print("Root cause: ");
            rootCause.printStackTrace(w);
        }
    }

    /**
     * <p>
     * This will return the root cause <code>Throwable</code>, or null
     *   if one does not exist.
     * </p>
     * 
     * @return <code>Throwable</code> - the wrapped <code>Throwable</code>.
     */
    public Throwable getRootCause()  {
        return rootCause;             
    }

}
