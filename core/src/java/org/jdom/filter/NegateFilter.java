/*--

 $Id: NegateFilter.java,v 1.1 2003/05/21 09:17:45 jhunter Exp $

 Copyright (C) 2000-2003 Jason Hunter & Brett McLaughlin.
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
    written permission, please contact <license AT jdom DOT org>.

 4. Products derived from this software may not be called "JDOM", nor
    may "JDOM" appear in their name, without prior written permission
    from the JDOM Project Management <pm AT jdom DOT org>.

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
 created by Jason Hunter <jhunter AT jdom DOT org> and
 Brett McLaughlin <brett AT jdom DOT org>.  For more information on
 the JDOM Project, please see <http://www.jdom.org/>.

 */

package org.jdom.filter;

/**
 * Filter that is the logical <b>negation</b> operation of another filter.
 *
 *
 * @author Bradley S. Huffman
 * @version $Revision: 1.1 $, $Date: 2003/05/21 09:17:45 $
 */
class NegateFilter extends AbstractFilter {

    private static final String CVS_ID = 
      "@(#) $RCSfile: NegateFilter.java,v $ $Revision: 1.1 $ $Date: 2003/05/21 09:17:45 $";

    // Underlying filter.
    private Filter filter;

    /**
     * This is the protected, no-args constructor standard in all JDOM
     * classes. It allows subclassers to get a raw instance with no
     * initialization.
     * <p>
     * It intentionally leaves all instance variables null, allowing a
     * lightweight subclass implementation.  The subclass is responsible for
     * ensuring all the get and set methods behave as documented.
     * </p>
     * <p>
     * When implementing a subclass which doesn't require full control over
     * variable initialization, be aware that simply calling super() (or
     * letting the compiler add the implicit super() call) will not
     * initialize the instance variables which will cause many of the methods
     * to throw a <code>NullPointerException</code>.  Therefore, the
     * constructor for these subclasses should call one of the public
     * constructors so variable initialization is handled automatically.
     * </p>
     */
    protected NegateFilter() {
    }

    /**
     * Match if the supplied filter <b>does not</b> match.
     *
     * @param filter filter to use.
     */
    public NegateFilter(Filter filter) {
        this.filter = filter;
    }

    public boolean matches(Object obj) {
        return filter.matches(obj) == false;
    }

    public Filter negate() {
        return filter;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj instanceof NegateFilter) {
            return filter.equals(((NegateFilter) obj).filter);
        }
        return false;
    }

    public int hashCode() {
        return ~filter.hashCode();
    }

    public String toString() {
        return new StringBuffer(64)
                   .append("[NegateFilter: ")
                   .append(filter.toString())
                   .append("]")
                   .toString();
    }
}
