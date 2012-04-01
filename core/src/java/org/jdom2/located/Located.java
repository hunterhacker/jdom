/*-- 

 Copyright (C) 2012 Jason Hunter & Brett McLaughlin.
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

package org.jdom2.located;

/**
 * Implementations of this class know about their location (line and column).
 * <p>
 * While it would seem intuitive that this represents the location where the
 * content starts, in fact, if the data is populated by a SAX parser the line
 * and column values represent the <strong>end</strong> of the SAX
 * <strong>event</strong>.
 * <p>
 * SAX parsers may vary, but it typically means the
 * character after the last character for Text and CDATA values, the character
 * after EntityRef, Comment, and ProcessingInstruction data, and the character
 * after the opening tag for Element content. For DocType content, it appears
 * that Xerces is inconsistent in the location, with the location being set at
 * what appears to be the start of the internal subset data (if any).
 * <p>
 * Finally, remember that the column value counts characters, and thus, if you
 * have tab-indented values, the tab counts as a single character (regardless of
 * how much it indents).
 * 
 * @author Rolf Lear
 *
 */
public interface Located {
	/**
	 * Get the line number
	 * @return the line number
	 */
	public int getLine();
	/**
	 * Get the column (character on the line).
	 * @return the column
	 */
	public int getColumn();
	
	/**
	 * Set the line number
	 * @param line the line.
	 */
	public void setLine(int line);
	/**
	 * Set the column (character on the line).
	 * @param col The column
	 */
	public void setColumn(int col);
}
