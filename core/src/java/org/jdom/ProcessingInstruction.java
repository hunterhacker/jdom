/*-- 

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
 DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
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

import java.io.Serializable;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;

/**
 * <p>
 * <code>ProcessingInstruction</code> defines behavior for an
 *   XML processing instruction, modeled in Java.  Methods
 *   allow the user to obtain the target of the PI as well as its data.
 *   The data can always be accessed as a String, and where appropriate
 *   can be retrieved as name/value pairs.
 * </p>
 *
 * @author Brett McLaughlin
 * @author Jason Hunter
 * @version 1.0
 */

public class ProcessingInstruction implements Serializable, Cloneable {

    /** The target of the PI */
    protected String target;

    /** The data for the PI as a String */
    protected String rawData;

    /** The data for the PI in name/value pairs */
    protected Map mapData;

    /**
     * <p>
     * Default, no-args constructor for implementations
     *   to use if needed.
     * </p>
     */
    protected ProcessingInstruction() { }

    /**
     * <p>
     * This will create a new <code>ProcessingInstruction</code>
     *   with the specified target and data.
     * </p>
     *
     * @param target <code>String</code> target of PI.
     * @param data <code>Map</code> data for PI, in
     *             name/value pairs
     */
    public ProcessingInstruction(String target, Map data) {
        String reason;
        if ((reason = Verifier.checkProcessingInstructionTarget(target)) != null) {
            throw new IllegalTargetException(target, reason);
        }

        this.target = target;
        setData(data);
    }

    /**
     * <p>
     * This will create a new <code>ProcessingInstruction</code>
     *   with the specified target and data.
     * </p>
     *
     * @param target <code>String</code> target of PI.
     * @param rawData <code>String</code> data for PI.
     */
    public ProcessingInstruction(String target, String data) {
        String reason;
        if ((reason = Verifier.checkProcessingInstructionTarget(target)) != null) {
            throw new IllegalTargetException(target, reason);
        }

        this.target = target;
        setData(data);
    }

    /**
     * <p>
     * This will retrieve the target of the PI.
     * </p>
     *
     * @return <code>String</code> - target of PI.
     */
    public String getTarget() {
        return target;
    }

    /**
     * <p>
     * This will return the raw data from all instructions.
     * </p>
     *
     * @return <code>String</code> - data of PI.
     */
    public String getData() {
        return rawData;
    }

    /**
     * <p>
     * This will set the raw data for the PI.
     * </p>
     *
     * @param rawData <code>String</code> data of PI.
     * @return <code>ProcessingInstruction</code> - this PI modified.
     */
    public ProcessingInstruction setData(String data) {
        this.rawData = data;
        this.mapData = parseData(data);
        return this;
    }

    /**
     * <p>
     * This will set the name/value pairs within the passed
     *   <code>Map</code> as the pairs for the data of
     *   this PI.  The keys should be the pair name
     *   and the values should be the pair values.
     * </p>
     *
     * @return <code>ProcessingInstruction</code> - modified PI.
     */
    public ProcessingInstruction setData(Map data) {
        this.rawData = toString(data);
        this.mapData = data;
        return this;
    }

    /**
     * <p>
     * This will return the value for a specific
     *   name/value pair on the PI.  If no such pair is
     *   found for this PI, an empty <code>String</code>
     *   will result.
     * </p>
     *
     * @param name <code>String</code> name of name/value pair
     *             to lookup value for.
     * @return <code>String</code> - value of name/value pair.
     */
    public String getValue(String name) {
        if (mapData.containsKey(name)) {
            return (String)mapData.get(name);
        }

        return "";
    }

    /**
     * <p>
     * This will set the value for the specified name/value
     *   pair.  If no matching pair is found, the supplied
     *   pair is added to the PI data.
     * </p>
     *
     * @param name <code>String</code> name of pair.
     * @param value <code>String</code> value for pair.
     * @return <code>ProcessingInstruction</code> this PI modified.
     */
    public ProcessingInstruction setValue(String name,
                                          String value) {

        mapData.put(name, value);
        rawData = toString(mapData);
        return this;
    }

    /**
     * <p>
     * This will remove the name/value pair with the specified
     *   name.
     * </p>
     *
     * @return <code>boolean</code> - whether the requested
     *         instruction was removed.
     */
    public boolean removeValue(String name) {
        if ((mapData.remove(name)) != null) {
            rawData = toString(mapData);
            return true;
        }

        return false;
    }

    /**
     * <p>
     * This will convert the Map to a string representation.
     * </p>
     *
     * @param mapData <code>Map</code> PI data to convert
     */
    private String toString(Map mapData) {
        StringBuffer rawData = new StringBuffer();

        Iterator i = mapData.keySet().iterator();
        while (i.hasNext()) {
            String name = (String)i.next();
            String value = (String)mapData.get(name);
            rawData.append(name)
                   .append("=\"")
                   .append(value)
                   .append("\" ");
        }
        // Remove last space
        rawData.setLength(rawData.length() - 1);

        return rawData.toString();
    }

    /**
     * <p>
     * This will parse and load the instructions for the PI.
     *   This is separated to allow it to occur once and then
     *   be reused.
     * </p>
     *
     * @param rawData <code>String</code> PI data to parse
     */
    private Map parseData(String rawData) {
        Map data = new HashMap();

        // Break up name/value pairs
        StringTokenizer s =
            new StringTokenizer(rawData);

        // Iterate through the pairs
        while (s.hasMoreTokens()) {
            // Now break up pair on the = and (" or ') characters
            StringTokenizer t =
                new StringTokenizer(s.nextToken(), "='\"");

            if (t.countTokens() >= 2) {
                String name = t.nextToken();
                String value = t.nextToken();

                data.put(name, value);
            }
        }

        return data;
    }

    /**
     * <p>
     *  This returns a <code>String</code> representation of the
     *    <code>ProcessingInstruction</code>, suitable for debugging. If the XML
     *    representation of the <code>ProcessingInstruction</code> is desired,
     *    <code>{@link #getSerializedForm}</code> should be used.
     * </p>
     *
     * @return <code>String</code> - information about the
     *         <code>ProcessingInstruction</code>
     */
    public String toString() {
        return new StringBuffer()
            .append("[Processing Instruction: ")
            .append(getSerializedForm())
            .append("]")
            .toString();
    }

    /**
     * <p>
     *  This will return the <code>Comment</code> in XML format,
     *    usable in an XML document.
     * </p>
     *
     * @return <code>String</code> - the serialized form of the
     *         <code>Comment</code>.
     */
    public final String getSerializedForm() {
        return new StringBuffer()
            .append("<?")
            .append(target)
            .append(" ")
            .append(rawData)
            .append("?>")
            .toString();
    }

    /**
     * <p>
     *  This tests for equality of this <code>ProcessingInstruction</code>
     *    to the supplied <code>Object</code>.
     * </p>
     *
     * @param ob <code>Object</code> to compare to.
     * @return <code>boolean</code> - whether the
     *         <code>ProcessingInstruction</code> is equal to the supplied
     *         <code>Object</code>.
     */
    public final boolean equals(Object ob) {
        return (ob == this);
    }

    /**
     * <p>
     *  This returns the hash code for this <code>ProcessingInstruction</code>.
     * </p>
     *
     * @return <code>int</code> - hash code.
     */
    public final int hashCode() {
        return super.hashCode();
    }

    /**
     * <p>
     *  This will return a clone of this <code>ProcessingInstruction</code>.
     * </p>
     *
     * @return <code>Object</code> - clone of this 
     * <code>ProcessingInstruction</code>.
     */
    public Object clone() {
        return new ProcessingInstruction(target, rawData);
    }
}


