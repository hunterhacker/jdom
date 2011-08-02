/*--

 $Id: JAXPParserFactory.java,v 1.6 2007/11/10 05:29:00 jhunter Exp $

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

package org.jdom.input;

import java.util.*;

import javax.xml.parsers.*;

import org.jdom.*;
import org.xml.sax.*;

/**
 * A non-public utility class to allocate JAXP SAX parsers.
 *
 * @version $Revision: 1.6 $, $Date: 2007/11/10 05:29:00 $
 * @author  Laurent Bihanic
 */
class JAXPParserFactory {               // package protected

    private static final String CVS_ID =
      "@(#) $RCSfile: JAXPParserFactory.java,v $ $Revision: 1.6 $ $Date: 2007/11/10 05:29:00 $ $Name:  $";

    /** JAXP 1.2 schema language property id. */
    private static final String JAXP_SCHEMA_LANGUAGE_PROPERTY =
       "http://java.sun.com/xml/jaxp/properties/schemaLanguage";

    /** JAXP 1.2 schema location property id. */
    private static final String JAXP_SCHEMA_LOCATION_PROPERTY =
       "http://java.sun.com/xml/jaxp/properties/schemaSource";

    /**
     * Private constructor to forbid allocating instances of this utility
     * class.
     */
    private JAXPParserFactory() {
        // Never called.
    }

    /* Implementor's note regarding createParser() design: The features and
    properties are normally set in SAXBuilder, but we take them in
    createParser() as well because some features or properties may need to be
    applied during the JAXP parser construction. Today, for example, properties
    is used as it's the only way to configure schema validation: JAXP defines
    schema validation properties but SAX does not. This reflects in the Apache
    Xerces implementation where the SAXParser implementation supports the JAXP
    properties but the XMLReader does not. Hence, configuring schema validation
    must be done on the SAXParser object which is only visible in
    JAXParserFactory. Features is also passed in case some future JAXP release
    defines JAXP-specific features.
     */

    /**
     * Creates a SAX parser allocated through the configured JAXP SAX
     * parser factory.
     *
     * @param  validating   whether a validating parser is requested.
     * @param  features     the user-defined SAX features.
     * @param  properties   the user-defined SAX properties.
     *
     * @return a configured XMLReader.
     *
     * @throws JDOMException if any error occurred when allocating or
     *                       configuring the JAXP SAX parser.
     */
    public static XMLReader createParser(boolean validating,
                          Map features, Map properties) throws JDOMException {
        try {
            SAXParser parser = null;

            // Allocate and configure JAXP SAX parser factory.
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setValidating(validating);
            factory.setNamespaceAware(true);

            try {
                // Allocate parser.
                parser = factory.newSAXParser();
            }
            catch (ParserConfigurationException e) {
                throw new JDOMException("Could not allocate JAXP SAX Parser", e);
            }

            // Set user-defined JAXP properties (if any)
            setProperty(parser, properties, JAXP_SCHEMA_LANGUAGE_PROPERTY);
            setProperty(parser, properties, JAXP_SCHEMA_LOCATION_PROPERTY);

            // Return configured SAX XMLReader.
            return parser.getXMLReader();
        }
        catch (SAXException e) {
            throw new JDOMException("Could not allocate JAXP SAX Parser", e);
        }
    }

    /**
     * Sets a property on a JAXP SAX parser object if and only if it
     * is declared in the user-defined properties.
     *
     * @param  parser       the JAXP SAX parser to configure.
     * @param  properties   the user-defined SAX properties.
     * @param  name         the name of the property to set.
     *
     * @throws JDOMException if any error occurred while configuring
     *                       the property.
     */
    private static void setProperty(SAXParser parser,
                        Map properties, String name) throws JDOMException {
        try {
            if (properties.containsKey(name)) {
                parser.setProperty(name, properties.get(name));
            }
        }
        catch (SAXNotSupportedException e) {
            throw new JDOMException(
                name + " property not supported for JAXP parser " +
                parser.getClass().getName());
        }
        catch (SAXNotRecognizedException e) {
            throw new JDOMException(
                name + " property not recognized for JAXP parser " +
                parser.getClass().getName());
        }
    }
}

