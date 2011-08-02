/*-- 

 $Id: JDOMException.java,v 1.26 2008/12/10 00:59:51 jhunter Exp $

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

package org.jdom;

import java.io.*;
import java.lang.reflect.*;
import java.sql.*;

import org.xml.sax.*;

/**
 * The top level exception that JDOM classes can throw. Its subclasses add
 * specificity to the problems that can occur using JDOM. This single exception
 * can be caught to handle all JDOM specific problems (some methods may throw
 * {@link java.io.IOException} and such).
 *
 * @version $Revision: 1.26 $, $Date: 2008/12/10 00:59:51 $
 * @author  Brett McLaughlin
 * @author  Jason Hunter
 */
public class JDOMException extends Exception {

    private static final String CVS_ID = 
      "@(#) $RCSfile: JDOMException.java,v $ $Revision: 1.26 $ $Date: 2008/12/10 00:59:51 $ $Name:  $";

    /** A wrapped <code>Throwable</code> */
    private Throwable cause;

    /**
     * This will create an <code>Exception</code>.
     */
    public JDOMException() {
        super("Error occurred in JDOM application.");
    }

    /**
     * This will create an <code>Exception</code> with the given message.
     *
     * @param message <code>String</code> message indicating
     *                the problem that occurred.
     */    
    public JDOMException(String message)  {
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
    public JDOMException(String message, Throwable cause)  {
        super(message);    
        this.cause = cause;
    }    

    /** 
     * Intializes the cause of this exception to be the specified value.
     *
     * @param cause <code>Throwable</code> that caused this
     *                  to be thrown.
     * @return a pointer to this throwable
     */
    // Created to match the JDK 1.4 Throwable method.
    public Throwable initCause(Throwable cause)  {
        this.cause = cause;
        return this;
    }    

    /**
     * This returns the message for the <code>Exception</code>. If
     * there are one or more nested exceptions, their messages
     * are appended.
     *
     * @return <code>String</code> - message for <code>Exception</code>.
     */
    public String getMessage() {
        // Get this exception's message.
        String msg = super.getMessage();

        Throwable parent = this;
        Throwable child;
        
        // Look for nested exceptions.
        while((child = getNestedException(parent)) != null) {
            // Get the child's message.
            String msg2 = child.getMessage();
            
            // Special case: If a SAXException has no message of its own, but has a 
            // nested exception, then it returns the nested exception's message as its
            // message. We don't want to add that message twice.
            if (child instanceof SAXException) {
                Throwable grandchild = ((SAXException)child).getException();
                // If the SAXException tells us that it's message is identical to 
                // its nested exception's message, then we skip it, so we don't
                // add it twice.
                if (grandchild != null && msg2 != null && msg2.equals(grandchild.getMessage())) {
                    msg2 = null;
                }
            }

            // If we found a message for the child exception, we append it.
            if (msg2 != null) {
                if (msg != null) {
                    msg += ": " + msg2;
                } else {
                    msg = msg2;
                }
            }

            // Any nested JDOMException will append its own children,
            // so we need to break out of here.
            if (child instanceof JDOMException) {
                break;
            }
            parent = child;
        }

        // Return the completed message.
        return msg;
    }

    /**
     * This prints the stack trace of the <code>Exception</code>. If
     * there is a root cause, the stack trace of the root
     * <code>Exception</code> is printed right after.
     */
    public void printStackTrace() {
        // Print the stack trace for this exception.
        super.printStackTrace();

        Throwable parent = this;
        Throwable child;

        // Print the stack trace for each nested exception.
        while((child = getNestedException(parent)) != null) {
            System.err.print("Caused by: ");
            child.printStackTrace();
            // Any nested JDOMException will print its own children,
            // so we need to break out of here.
            if (child instanceof JDOMException) {
                break;
            }
            parent = child;
        }
    }

    /**
     * Prints the stack trace of the <code>Exception</code> to the given
     * PrintStream. If there is a root cause, the stack trace of the root
     * <code>Exception</code> is printed right after.
     *
     * @param s PrintStream to print to
     */
    public void printStackTrace(PrintStream s) {
        // Print the stack trace for this exception.
        super.printStackTrace(s);

        Throwable parent = this;
        Throwable child;

        // Print the stack trace for each nested exception.
        while((child = getNestedException(parent)) != null) {
            s.print("Caused by: ");
            child.printStackTrace(s);
            // Any nested JDOMException will print its own children,
            // so we need to break out of here.
            if (child instanceof JDOMException) {
                break;
            }
            parent = child;
        }
    }

    /**
     * Prints the stack trace of the <code>Exception</code> to the given
     * PrintWriter. If there is a root cause, the stack trace of the root
     * <code>Exception</code> is printed right after.
     *
     * @param w PrintWriter to print to
     */
    public void printStackTrace(PrintWriter w) {
        // Print the stack trace for this exception.
        super.printStackTrace(w);

        Throwable parent = this;
        Throwable child;

        // Print the stack trace for each nested exception.
        while((child = getNestedException(parent)) != null) {
            w.print("Caused by: ");
            child.printStackTrace(w);
            // Any nested JDOMException will print its own children,
            // so we need to break out of here.
            if (child instanceof JDOMException) {
                break;
            }
            parent = child;
        }
    }

    /**
     * This will return the root cause <code>Throwable</code>, or null
     * if one does not exist.
     * 
     * @return <code>Throwable</code> - the wrapped <code>Throwable</code>.
     */
    public Throwable getCause()  {
        return cause;             
    }

    // If this Throwable has a nested (child) exception, then we return it.
    // Otherwise we return null.
    private static Throwable getNestedException(Throwable parent) {
        if (parent instanceof JDOMException) {
            return ((JDOMException)parent).getCause();
        }
        
        if (parent instanceof SAXException) {
            return ((SAXException)parent).getException();
        }
        
        if (parent instanceof SQLException) {
            return ((SQLException)parent).getNextException();
        }
        
        if (parent instanceof InvocationTargetException) {
            return ((InvocationTargetException)parent).getTargetException();
        }
        
        if (parent instanceof ExceptionInInitializerError) {
            return ((ExceptionInInitializerError)parent).getException();
        }
        
        // The RMI classes are not present in Android's Dalvik VM, so we use reflection to access them.

        Throwable nestedException = getNestedExceptionFromField(parent, "java.rmi.RemoteException", "detail");
        if (nestedException != null) {
            return nestedException;
        }
        
        // These classes are not part of standard JDK 1.1 or 1.2, so again we use reflection to access them.

        nestedException = getNestedException(parent, "javax.naming.NamingException", "getRootCause");
        if (nestedException != null) {
            return nestedException;
        }
        
        nestedException = getNestedException(parent, "javax.servlet.ServletException", "getRootCause");
        if (nestedException != null) {
            return nestedException;
        }

        return null;
    }

    // This method uses reflection to obtain the nest exception of a Throwable. We use reflection
    // because the desired class may not exist in the currently-running VM.
    private static Throwable getNestedException(
                                 Throwable parent, String className, String methodName) {
        try {
            // See if this Throwable is of the desired type, by using isAssignableFrom().
            Class testClass = Class.forName(className);
            Class objectClass = parent.getClass();
            if (testClass.isAssignableFrom(objectClass)) {
                // Use reflection to call the specified method.
                Class[] argClasses = new Class[0];
                Method method = testClass.getMethod(methodName, argClasses);
                Object[] args = new Object[0];
                return (Throwable)method.invoke(parent, args);
            }
        }
        catch(Exception ex) {
            // Most likely, the desired class is not available in this VM. That's fine.
            // Even if it's caused by something else, we don't want to display an error
            // here, since we're already in the process of trying to display the original
            // error - another error here will just confuse things.
        }

        return null;
    }

    // This method is similar to getNestedException() except it looks for a field instead
    // of a method.
    private static Throwable getNestedExceptionFromField(
                                 Throwable parent, String className, String fieldName) {
        try {
            // See if this Throwable is of the desired type, by using isAssignableFrom().
            Class testClass = Class.forName(className);
            Class objectClass = parent.getClass();
            if (testClass.isAssignableFrom(objectClass)) {
                // Use reflection to call the specified method.
                Class[] argClasses = new Class[0];
                Field field = testClass.getField(fieldName);
                return (Throwable)field.get(parent);
            }
        }
        catch(Exception ex) {
            // Most likely, the desired class is not available in this VM. That's fine.
            // Could be that the named field isn't of type Throwable, but that should happen
            // with proper call usage.
            // Even if it's caused by something else, we don't want to display an error
            // here, since we're already in the process of trying to display the original
            // error - another error here will just confuse things.
        }

        return null;
    }
}
