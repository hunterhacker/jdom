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
package org.jdom.contrib.xpath.impl;

/**
 * Converts keywords into int codes as a helper utility for handlers.
 *
 * @author Michael Hinchey
 * @version 1.0
 */
public class Axis {

  public static final int UNRECOGNIZED = 0;
  public static final int ANCESTOR = 2;
  public static final int ANCESTOR_OR_SELF = 3;
  public static final int ATTRIBUTE = 4;
  public static final int CHILD = 5;
  public static final int DESCENDANT = 6;
  public static final int DESCENDANT_OR_SELF = 7;
  public static final int FOLLOWING = 8;
  public static final int FOLLOWING_SIBLING = 9;
  public static final int NAMESPACE = 10;
  public static final int PARENT = 11;
  public static final int PRECEDING = 12;
  public static final int PRECEDING_SIBLING = 13;
  public static final int SELF = 14;

  private String string;
  private int code;

  /**
   * Constructor.
   */
  public Axis(String axis) {
    string = axis;
    code = axisCode(axis);
  }

  /**
   * Return the string format of the axis.
   */
  public String getString() {
    return string;
  }

  /**
   * Return an int which can be compared to the constants in this class.
   */
  public int getCode() {
    return code;
  }

  /**
   * For debugging.
   */
  public String toString() {
    return "[Axis:" + string + "]";
  }

  /**
   * Note: axis==null returns CHILD.
   */
  private static int axisCode(final String axis) {
    if (axis == null) {
      return CHILD;
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

}
