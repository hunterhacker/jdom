/*--

 Copyright (C) 2011 Jason Hunter & Brett McLaughlin.
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

package org.jdom2.input.stax;

import org.jdom2.Namespace;

/**
 * This DefaultStAXFilter includes all content and prunes nothing.
 * <p>
 * Override this class to make adjustments to get the results you need.
 * 
 * @see StAXFilter
 * 
 * @author Rolf Lear
 */
public class DefaultStAXFilter implements StAXFilter {

	@Override
	public boolean includeDocType() {
		return true;
	}

	@Override
	public boolean includeElement(final int depth, final String name, final Namespace ns) {
		return true;
	}

	@Override
	public String includeComment(final int depth, final String comment) {
		return comment;
	}

	@Override
	public boolean includeEntityRef(final int depth, final String name) {
		return true;
	}

	@Override
	public String includeCDATA(final int depth, final String text) {
		return text;
	}

	@Override
	public String includeText(final int depth, final String text) {
		return text;
	}

	@Override
	public boolean includeProcessingInstruction(final int depth, final String target) {
		return true;
	}

	@Override
	public boolean pruneElement(final int depth, final String name, final Namespace ns) {
		return false;
	}

	@Override
	public String pruneComment(final int depth, final String comment) {
		return comment;
	}

	@Override
	public boolean pruneEntityRef(final int depth, final String name) {
		return false;
	}

	@Override
	public String pruneCDATA(final int depth, final String text) {
		return text;
	}

	@Override
	public String pruneText(final int depth, final String text) {
		return text;
	}

	@Override
	public boolean pruneProcessingInstruction(final int depth, final String target) {
		return false;
	}

}
