/*-- 

 $Id: ProcessingInstruction.java,v 1.18 2001/04/20 07:30:35 jhunter Exp $

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
 * @author Steven Gould
 * @version 1.0
 */

public class ProcessingInstruction implements Serializable, Cloneable {

    /** The target of the PI */
    protected String target;

    /** The data for the PI as a String */
    protected String rawData;

    /** The data for the PI in name/value pairs */
    protected Map mapData;

    /** Parent element, or null if none */
    protected Element parent;

    /** Document node if PI is outside the root element, or null if none */
    protected Document document;

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
        if ((reason = Verifier.checkProcessingInstructionTarget(target))
                                    != null) {
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
        if ((reason = Verifier.checkProcessingInstructionTarget(target))
                                    != null) {
            throw new IllegalTargetException(target, reason);
        }

        this.target = target;
        setData(data);
    }

    /**
     * <p>
     * This will return the parent of this <code>ProcessingInstruction</code>.
     *   If there is no parent, then this returns <code>null</code>.
     * </p>
     *
     * @return parent of this <code>ProcessingInstruction</code>
     */
    public Element getParent() {
        return parent;
    }

    /**
     * <p>
     * This will set the parent of this <code>ProcessingInstruction</code>.
     * </p>
     *
     * @param parent <code>Element</code> to be new parent.
     * @return this <code>ProcessingInstruction</code> modified.
     */
    protected ProcessingInstruction setParent(Element parent) {
        this.parent = parent;
        return this;
    }

    /**
     * <p>
     * This detaches the PI from its parent, or does nothing if the
     * PI has no parent.
     * </p>
     *
     * @return <code>ProcessingInstruction</code> - this 
     * <code>ProcessingInstruction</code> modified.
     */
    public ProcessingInstruction detach() {
        Element p = getParent();
        if (p != null) {
            p.removeContent(this);
        }
        else {
            Document d = getDocument();
            if (d != null) {
                d.removeContent(this);
            }
        }
        return this;
    }

    /**
     * <p>
     * This retrieves the owning <code>{@link Document}</code> for
     *   this PI, or null if not a currently a member of a
     *   <code>{@link Document}</code>.
     * </p>
     *
     * @return <code>Document</code> owning this PI, or null.
     */
    public Document getDocument() {
        if (document != null) {
            return document;
        }

        Element p = getParent();
        if (p != null) {
            return p.getDocument();
        }

        return null;
    }

    /**
     * <p>
     * This sets the <code>{@link Document}</code> parent of this PI.
     * </p>
     *
     * @param document <code>Document</code> parent
     * @return this <code>PI</code> modified
     */
    protected ProcessingInstruction setDocument(Document document) {
        this.document = document;
        return this;
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
        // XXX Need to validate the data
        // XXX Make sure the PI doesn't contain ?> internally, a la CDATA
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
        // XXX Need to validate the data
        this.rawData = toString(data);
        this.mapData = data;
        return this;
    }

    /**
     * <p>
     * This will return the value for a specific
     *   name/value pair on the PI.  If no such pair is
     *   found for this PI, null is returned.
     * </p>
     *
     * @param name <code>String</code> name of name/value pair
     *             to lookup value for.
     * @return <code>String</code> - value of name/value pair.
     */
    public String getValue(String name) {
        return (String)mapData.get(name);
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
        // XXX Need to validate the data
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
     *   This is separated to allow it to occur once and then be reused.
     * </p>
     *
     * @param rawData <code>String</code> PI data to parse
     */
    private Map parseData(String rawData) {
        // The parsing here is done largely "by hand" which means the code
        // gets a little tricky/messy.  The following conditions should 
        // now be handled correctly:
        //   <?pi href="http://hi/a=b"?>        Reads OK
        //   <?pi href = 'http://hi/a=b' ?>     Reads OK
        //   <?pi href\t = \t'http://hi/a-b'?>  Reads OK
        //   <?pi?>                             Empty Map
        //   <?pi id=22?>                       Empty Map
        //   <?pi id='22?>                      Empty Map

        Map data = new HashMap();

        // System.out.println("rawData: " + rawData);

        // The inputData variable holds the part of rawData left to parse
        String inputData = rawData.trim();

        // Iterate through the remaining inputData string
        while (!inputData.trim().equals("")) {
            //System.out.println("parseData() looking at: " + inputData);

            // Search for "name =", "name=" or "name1 name2..."
            String name = "";
            String value = "";
            int startName = 0;
            char previousChar = inputData.charAt(startName);
            int pos = 1;
            for (; pos<inputData.length(); pos++) {
                char currentChar = inputData.charAt(pos);
                if (currentChar == '=') {
                    name = inputData.substring(startName, pos).trim();
                    value = extractQuotedString(
                                     inputData.substring(pos+1).trim());
                    // A null value means a parse error and we return empty!
                    if (value == null) {
                        return new HashMap();
                    }
                    pos += value.length() + 1;  // skip over equals and value
                    break;
                }
                else if (Character.isWhitespace(previousChar)
                          && !Character.isWhitespace(currentChar)) {
                    startName = pos;
                }

                previousChar = currentChar;
            }

            // Remove the first pos characters; they have been processed
            inputData = inputData.substring(pos);

            // System.out.println("Extracted (name, value) pair: ("
            //                          + name + ", '" + value+"')");

            // If both a name and a value have been found, then add
            // them to the data Map
            if (name.length() > 0 && value != null) {
                //if (data.containsKey(name)) {
                    // A repeat, that's a parse error, so return a null map
                    //return new HashMap();
                //}
                //else {
                    data.put(name, value);
                //}
            }
        }

        return data;
    }

    /**
     * This is a helper routine, only used by parseData, to extract a
     * quoted String from the input parameter, rawData. A quoted string
     * can use either single or double quotes, but they must match up.
     * A singly quoted string can contain an unbalanced amount of double
     * quotes, or vice versa. For example, the String "JDOM's the best"
     * is valid as is 'JDOM"s the best'.
     *
     * @param rawData the input string from which a quoted string is to
     *                be extracted.
     * @return the first quoted string encountered in the input data. If
     *         no quoted string is found, then the empty string, "", is
     *         returned.
     * @see parseData
     */
    private String extractQuotedString(String rawData) {
        // Remembers whether we're actually in a quoted string yet
        boolean inQuotes = false;

        // Remembers which type of quoted string we're in
        char quoteChar = '"';

        // Stores the position of the first character inside
        //  the quoted string (i.e. the start of the return string)
        int start = 0;

        // Iterate through the input string looking for the start
        // and end of the quoted string
        for (int pos=0; pos < rawData.length(); pos++) {
            char currentChar = rawData.charAt(pos);
            if (currentChar=='"' || currentChar=='\'') {
                if (!inQuotes) {
                    // We're entering a quoted string
                    quoteChar = currentChar;
                    inQuotes = true;
                    start = pos+1;
                }
                else if (quoteChar == currentChar) {
                    // We're leaving a quoted string
                    inQuotes = false;
                    return rawData.substring(start, pos);
                }
                // Otherwise we've encountered a quote
                // inside a quote, so just continue
            }
        }

        // Should we throw an exception if no quoted string was found,
        // or simply return an empty string???
        return null;
    }
    
    /**
     * <p>
     *  This returns a <code>String</code> representation of the
     *    <code>ProcessingInstruction</code>, suitable for debugging. If the XML
     *    representation of the <code>ProcessingInstruction</code> is desired,
     *    <code>{@link #XMLOutputter.outputString(ProcessingInstruction}</code>
     *    should be used.
     * </p>
     *
     * @return <code>String</code> - information about the
     *         <code>ProcessingInstruction</code>
     */
    public String toString() {
        return new StringBuffer()
            .append("[ProcessingInstruction: ")
            .append(new org.jdom.output.XMLOutputter().outputString(this))
            .append("]")
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
        ProcessingInstruction pi = null;

        try {
             pi = (ProcessingInstruction) super.clone();
        } catch (CloneNotSupportedException ce) {
             // Can't happen
        }

        // target and rawdata are immutable and references copied by
        // Object.clone()

        // parent and document references copied by Object.clone(), so
        // must set to null

        pi.parent = null;
        pi.document = null;

        // Create a new Map object for the clone (since Map isn't Cloneable)
        if (mapData != null) {
            pi.mapData = parseData(rawData);
        }

        return pi;
    }

    /**
     * <p>
     *  This will return the <code>ProcessingInstruction</code> in XML format,
     *    usable in an XML document.
     * </p>
     *
     * @return <code>String</code> - the serialized form of the
     *         <code>ProcessingInstruction</code>.
     *
     * @deprecated Deprecated in Beta7, use
     * XMLOutputter.outputString(ProcessingInstruction) instead
     */
    public final String getSerializedForm() {
        // Return <?target data?> or if no data then just <?target?>
        if (!"".equals(rawData)) {
            return new StringBuffer()
                .append("<?")
                .append(target)
                .append(" ")
                .append(rawData)
                .append("?>")
                .toString();
        }
        else {
            return new StringBuffer()
                .append("<?")
                .append(target)
                .append("?>")
                .toString();
        }
    }
}
