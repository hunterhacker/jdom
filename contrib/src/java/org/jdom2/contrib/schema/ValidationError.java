/*--

 Copyright (C) 2003-2004 Jason Hunter & Brett McLaughlin.
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

package org.jdom2.contrib.schema;

/**
 * A ValidationError object encapsulates a schema validation error or
 * warning.
 *
 * @author Laurent Bihanic
 */
public class ValidationError {
    /** The severity for warnings. */
    public final static Severity WARNING = new Severity(0);
    /** The severity for recoverable validation errors. */
    public final static Severity ERROR = new Severity(1);
    /** The severity for non-recoverable validation errors. */
    public final static Severity FATAL = new Severity(2);

    /**
     * The error severity.
     */
    private final Severity severity;

    /**
     * The detailed error message.
     */
    private final String message;

    /**
     * The JDOM node that caused the error.
     */
    private final Object node;

    /**
     * Creates a new validation error.
     *
     * @param  severity   the error severity.
     * @param  message    the detailed error message.
     */
    public ValidationError(Severity severity, String message) {
        this(severity, message, null);
    }

    /**
     * Creates a new validation error.
     *
     * @param  severity   the error severity.
     * @param  message    the detailed error message.
     * @param  node       the JDOM node that caused the error.
     */
    public ValidationError(Severity severity, String message, Object node) {
        this.severity = severity;
        this.message = message;
        this.node = node;
    }

    /**
     * Returns the severity of this error.
     *
     * @return the severity of this error.
     */
    public Severity getSeverity() {
        return this.severity;
    }

    /**
     * Returns the detailed error message.
     *
     * @return the detailed error message.
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * Returns the JDOM node that caused the error.
     *
     * @return the JDOM node that caused the error.
     */
    public Object getNode() {
        return this.node;
    }

    /**
     * Returns a string representation of this error suitable for
     * debugging and diagnosis.
     *
     * @return a string representation of this error.
     *
     * @see java.lang.Object#toString()
     */
    @Override
	public String toString() {
    	StringBuilder buf = new StringBuilder();

        buf.append('[');
        if (this.severity == WARNING) {
            buf.append("WARNING");
        }
        else if (this.severity == ERROR) {
            buf.append("ERROR");
        }
        else if (this.severity == FATAL) {
            buf.append("FATAL");
        }
        buf.append("] message: \"").append(this.getMessage());

        if (this.getNode() != null) {
            buf.append("\", location: \"").append(this.getNode().toString());
        }
        buf.append("\"");

        return buf.toString();
    }


    /**
     * Class to support type-safe enumeration design pattern to
     * represent severity levels of validation errors.
     */
    public static final class Severity {
        /** The severity of the error. */
        private final int level;

        /**
         * Severity constructor, private on purpose.
         *
         * @param  level   the severity level.
         */
        protected Severity(int level) {
            this.level = level;
        }

        /**
         * Returns a unique identifier for this severity.
         *
         * @return a unique identifier for this severity.
         *
         * @see java.lang.Object#hashCode()
         */
        @Override
		public int hashCode() {
            return this.level;
        }

        /**
         * Returns a string representation of this severity suitable
         * for debugging and diagnosis.
         *
         * @return a string representation of this severity.
         *
         * @see java.lang.Object#toString()
         */
        @Override
		public String toString() {
            return "[" + this.getClass().getName() + "] " + this.level;
        }

        /**
         * Tests for severity equality. This is only necessary to
         * handle cases where two <code>Type</code> objects are loaded
         * by different class loaders.
         *
         * @param  o   the object compared for equality to this
         *             severity.
         *
         * @return <code>true</code> if and only if <code>o</code>
         *         represents the same severity as this object.
         *
         * @see java.lang.Object#equals(Object)
         */
        @Override
		public boolean equals(Object o) {
            return ((o == this) ||
                    ((o != null) && (this.hashCode() == o.hashCode()) &&
                    (this.getClass().getName().equals(o.getClass().getName()))));
        }
    }
}

