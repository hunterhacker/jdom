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
package org.jdom.contrib.xpath;

/**
 * Converts keywords into int codes as a helper utility for handlers.
 *
 * @author Michael Hinchey
 * @version 1.0
 */
public class Keywords {

  /**
   * @see #axisCode(String)
   * @see #nodetypeCode(String)
   */
  public static final int UNRECOGNIZED = 0;

  /**
   * @see #axisCode(String)
   */
  public static final int ANCESTOR = 2;
  /**
   * @see #axisCode(String)
   */
  public static final int ANCESTOR_OR_SELF = 3;
  /**
   * @see #axisCode(String)
   */
  public static final int ATTRIBUTE = 4;
  /**
   * @see #axisCode(String)
   */
  public static final int CHILD = 5;
  /**
   * @see #axisCode(String)
   */
  public static final int DESCENDANT = 6;
  /**
   * @see #axisCode(String)
   */
  public static final int DESCENDANT_OR_SELF = 7;
  /**
   * @see #axisCode(String)
   */
  public static final int FOLLOWING = 8;
  /**
   * @see #axisCode(String)
   */
  public static final int FOLLOWING_SIBLING = 9;
  /**
   * @see #axisCode(String)
   */
  public static final int NAMESPACE = 10;
  /**
   * @see #axisCode(String)
   */
  public static final int PARENT = 11;
  /**
   * @see #axisCode(String)
   */
  public static final int PRECEDING = 12;
  /**
   * @see #axisCode(String)
   */
  public static final int PRECEDING_SIBLING = 13;
  /**
   * @see #axisCode(String)
   */
  public static final int SELF = 14;

  /**
   * @see #nodetypeCode(String)
   */
  public static final int NODE = 15;
  /**
   * @see #nodetypeCode(String)
   */
  public static final int COMMENT = 16;
  /**
   * @see #nodetypeCode(String)
   */
  public static final int TEXT = 17;
  /**
   * @see #nodetypeCode(String)
   */
  public static final int PROCESSING_INSTRUCTION = 18;
  /**
   * @see #nodetypeCode(String)
   */
  public static final int ELEMENT = 19;

  /**
   *
   */
  public static int axisCode(final String axis) {
    if (axis == null) {
      // Should this be CHILD as a default? (hinchey)
      return UNRECOGNIZED;
    } else if (axis.equals("ancestor")) {
      return ANCESTOR;
    } else if (axis.equals("ancestor-or-self")) {
      return ANCESTOR_OR_SELF;
    } else if (axis.equals("attribute")) {
      return ATTRIBUTE;
    } else if (axis.equals("child")) {
      return CHILD;
    } else if (axis.equals("descendant")) {
      return DESCENDANT;
    } else if (axis.equals("descendant-or-self")) {
      return DESCENDANT_OR_SELF;
    } else if (axis.equals("following")) {
      return FOLLOWING;
    } else if (axis.equals("following-sibling")) {
      return FOLLOWING_SIBLING;
    } else if (axis.equals("namespace")) {
      return NAMESPACE;
    } else if (axis.equals("parent")) {
      return PARENT;
    } else if (axis.equals("preceding")) {
      return PRECEDING;
    } else if (axis.equals("preceding-sibling")) {
      return PRECEDING_SIBLING;
    } else if (axis.equals("self")) {
      return SELF;
    } else {
      return UNRECOGNIZED;
    }
  }

  /**
   * The XPath specification views element as the default nodetype,
   * so <code>nodetype == null</code> will return ELEMENT.
   */
  public static int nodetypeCode(final String nodetype) {
    if (nodetype == null) {
      return ELEMENT;
    } else if ("node".equals(nodetype)) {
      return NODE;
    } else if ("comment".equals(nodetype)) {
      return COMMENT;
    } else if ("text".equals(nodetype)) {
      return TEXT;
    } else if ("processing-instruction".equals(nodetype)) {
      return PROCESSING_INSTRUCTION;
    } else {
      return UNRECOGNIZED;
    }
  }

}

