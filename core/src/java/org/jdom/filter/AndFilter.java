/*--

 $Id: AndFilter.java,v 1.3 2004/02/06 09:28:31 jhunter Exp $

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

package org.jdom.filter;

/**
 * Allow two filters to be chained together with a logical
 * <b>and</b> operation.
 *
 * @author Bradley S. Huffman
 * @version $Revision: 1.3 $, $Date: 2004/02/06 09:28:31 $
 */
final class AndFilter extends AbstractFilter {

    private static final String CVS_ID = 
      "@(#) $RCSfile: AndFilter.java,v $ $Revision: 1.3 $ $Date: 2004/02/06 09:28:31 $";

    // Filter for left side of logical <b>and</b>.
    private Filter left;

    // Filter for right side of logical <b>and</b>.
    private Filter right;

    /**
     * Match if only both supplied filters match.
     *
     * @param left left side of logical <b>and</b>
     * @param right right side of logical <b>and</b>
     * @throws IllegalArgumentException if either supplied filter is null
     */
    public AndFilter(Filter left, Filter right) {
        if ((left == null) || (right == null)) {
            throw new IllegalArgumentException("null filter not allowed");
        }
        this.left = left;
        this.right = right;
    }

    public boolean matches(Object obj) {
        return left.matches(obj) && right.matches(obj);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj instanceof AndFilter) {
            AndFilter filter = (AndFilter) obj;
            if ((left.equals(filter.left)  && right.equals(filter.right)) ||
                (left.equals(filter.right) && right.equals(filter.left))) {
                    return true;
            }
        }
        return false;
    }

    public int hashCode() {
        return (31 * left.hashCode()) + right.hashCode();
    }

    public String toString() {
        return new StringBuffer(64)
                   .append("[AndFilter: ")
                   .append(left.toString())
                   .append(",\n")
                   .append("            ")
                   .append(right.toString())
                   .append("]")
                   .toString();
    }
}
