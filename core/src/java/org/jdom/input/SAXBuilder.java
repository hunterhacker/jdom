/*-- 

 $Id: SAXBuilder.java,v 1.45 2001/05/09 07:14:23 jhunter Exp $

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

package org.jdom.input;

import java.io.*;
import java.lang.reflect.*;
import java.net.*;
import java.util.*;

import org.jdom.*;

import org.xml.sax.*;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * <p><code>SAXBuilder</code> builds a JDOM tree using SAX.</p>
 *
 * @author Brett McLaughlin
 * @author Jason Hunter
 * @author Dan Schaffer
 * @author Philip Nelson
 * @version 1.0
 */
public class SAXBuilder {

    private static final String CVS_ID = 
      "@(#) $RCSfile: SAXBuilder.java,v $ $Revision: 1.45 $ $Date: 2001/05/09 07:14:23 $ $Name:  $";

    /** 
     * Default parser class to use. This is used when no other parser
     * is given and JAXP isn't available.
     */
    private static final String DEFAULT_SAX_DRIVER =
        "org.apache.xerces.parsers.SAXParser";

    /** Whether validation should occur */
    private boolean validate;

    /** Whether expansion of entities should occur */
    private boolean expand = true;

    /** Adapter class to use */
    private String saxDriverClass;

    /** ErrorHandler class to use */
    private ErrorHandler saxErrorHandler = null;
 
    /** EntityResolver class to use */
    private EntityResolver saxEntityResolver = null;

    /** DTDHandler class to use */
    private DTDHandler saxDTDHandler = null;

    /** XMLFilter instance to use */
    private XMLFilter saxXMLFilter = null;
 
    /**
     * <p>
     * Creates a new SAXBuilder which will attempt to first locate
     * a parser via JAXP, then will try to use a set of default 
     * SAX Drivers. The underlying parser will not validate.
     * </p>
     */
    public SAXBuilder() {
        this(false);
    }

    /**
     * <p>
     * Creates a new SAXBuilder which will attempt to first locate
     * a parser via JAXP, then will try to use a set of default 
     * SAX Drivers. The underlying parser will validate or not
     * according to the given parameter.
     * </p>
     *
     * @param validate <code>boolean</code> indicating if
     *                 validation should occur.
     */
    public SAXBuilder(boolean validate) {
        this.validate = validate;
    }

    /**
     * <p>
     * Creates a new SAXBuilder using the specified SAX parser.
     * The underlying parser will not validate.
     * </p>
     *
     * @param saxDriverClass <code>String</code> name of SAX Driver
     *                       to use for parsing.
     */
    public SAXBuilder(String saxDriverClass) {
        this(saxDriverClass, false);
    }

    /**
     * <p>
     * Creates a new SAXBuilder using the specified SAX parser.
     * The underlying parser will validate or not
     * according to the given parameter.
     * </p>
     *
     * @param saxDriverClass <code>String</code> name of SAX Driver
     *                       to use for parsing.
     * @param validate <code>boolean</code> indicating if
     *                 validation should occur.
     */
    public SAXBuilder(String saxDriverClass, boolean validate) {
        this.saxDriverClass = saxDriverClass;
        this.validate = validate;
    }

    /**
     * <p>
     * This sets validation for the builder.
     * </p>
     *
     * @param validate <code>boolean</code> indicating whether validation 
     * should occur.
     */
    public void setValidation(boolean validate) {
        this.validate = validate;
    }

    /**
     * <p>
     * This sets custom ErrorHandler for the <code>Builder</code>.
     * </p>
     *
     * @param errorHandler <code>ErrorHandler</code>
     */
    public void setErrorHandler(ErrorHandler errorHandler) {
        saxErrorHandler = errorHandler;
    }

    /**
     * <p>
     * This sets custom EntityResolver for the <code>Builder</code>.
     * </p>
     *
     * @param entityResolver <code>EntityResolver</code>
     */
    public void setEntityResolver(EntityResolver entityResolver) {
        saxEntityResolver = entityResolver;
    }

    /**
     * <p>
     * This sets custom DTDHandler for the <code>Builder</code>.
     * </p>
     *
     * @param dtdHandler <code>DTDHandler</code>
     */
    public void setDTDHandler(DTDHandler dtdHandler) {
        saxDTDHandler = dtdHandler;
    }

    /**
     * <p>
     * This sets custom XMLFilter for the <code>Builder</code>.
     * </p>
     *
     * @param xmlFilter <code>XMLFilter</code>
     */
    public void setXMLFilter(XMLFilter xmlFilter) {
        saxXMLFilter = xmlFilter;
    }

    /**
     * <p>
     * This builds a document from the supplied
     *   input source.
     * </p>
     *
     * @param in <code>InputSource</code> to read from.
     * @return <code>Document</code> - resultant Document object.
     * @throws JDOMException when errors occur in parsing.
     */
    protected Document build(InputSource in) throws JDOMException {

        Document doc = new Document((Element)null);

        try {
            XMLReader parser = null;
            if (saxDriverClass != null) {
                // The user knows that they want to use a particular class
                parser = XMLReaderFactory.createXMLReader(saxDriverClass);
                // System.out.println("using specific " + saxDriverClass);
            } else {
                // Try using JAXP...
                // Note we need JAXP 1.1, and if JAXP 1.0 is all that's
                // available then the getXMLReader call fails and we skip
                // to the hard coded default parser
                try {
                    Class factoryClass = 
                        Class.forName("javax.xml.parsers.SAXParserFactory");

                    // factory = SAXParserFactory.newInstance();
                    Method newParserInstance = 
                        factoryClass.getMethod("newInstance", null);
                    Object factory = newParserInstance.invoke(null, null);

                    // factory.setValidating(validate);
                    Method setValidating = 
                        factoryClass.getMethod("setValidating", 
                                               new Class[]{boolean.class});
                    setValidating.invoke(factory, 
                                         new Object[]{new Boolean(validate)});

                    // jaxpParser = factory.newSAXParser();
                    Method newSAXParser = 
                        factoryClass.getMethod("newSAXParser", null);
                    Object jaxpParser  = newSAXParser.invoke(factory, null);

                    // parser = jaxpParser.getXMLReader();
                    Class parserClass = jaxpParser.getClass();
                    Method getXMLReader = 
                        parserClass.getMethod("getXMLReader", null);
                    parser = (XMLReader)getXMLReader.invoke(jaxpParser, null);
                    saxDriverClass = parser.getClass().getName();

                    // System.out.println("Using jaxp " +
                    //   parser.getClass().getName());
                } catch (ClassNotFoundException e) {
                    //e.printStackTrace();
                } catch (InvocationTargetException e) {
                    //e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    //e.printStackTrace();
                }
            }

            // Check to see if we got a parser yet, if not, try to use a
            // hard coded default
            if (parser == null) {
                parser = XMLReaderFactory.createXMLReader(DEFAULT_SAX_DRIVER);
                // System.out.println("using default " + DEFAULT_SAX_DRIVER);
                saxDriverClass = parser.getClass().getName();
            }

            // Install optional filter
            if (saxXMLFilter != null) {
                // Connect filter chain to parser
                XMLFilter root = saxXMLFilter;
                while (root.getParent() instanceof XMLFilter) {
                    root = (XMLFilter)root.getParent();
                }
                root.setParent(parser);

                // Read from filter
                parser = saxXMLFilter;
            }

            SAXHandler contentHandler = new SAXHandler(doc);
            contentHandler.setExpandEntities(expand);  // pass thru behavior

            parser.setContentHandler(contentHandler);

            if (saxEntityResolver != null) {
                parser.setEntityResolver(saxEntityResolver);
            }

            if (saxDTDHandler != null) {
                parser.setDTDHandler(saxDTDHandler);
            }

            boolean lexicalReporting = false;
            try {
                parser.setProperty("http://xml.org/sax/handlers/LexicalHandler",
                                   contentHandler);
                lexicalReporting = true;
            } catch (SAXNotSupportedException e) {
                // No lexical reporting available
            } catch (SAXNotRecognizedException e) {
                // No lexical reporting available
            }

            // Some parsers use alternate property for lexical handling (grr...)
            if (!lexicalReporting) {
                try {
                    parser.setProperty(
                        "http://xml.org/sax/properties/lexical-handler",
                        contentHandler);
                    lexicalReporting = true;
                } catch (SAXNotSupportedException e) {
                    // No lexical reporting available
                } catch (SAXNotRecognizedException e) {
                    // No lexical reporting available
                }
            }

            // Try setting the DeclHandler if entity expansion is off
            if (!expand) {
                try {
                    parser.setProperty(
                        "http://xml.org/sax/properties/declaration-handler",
                        contentHandler);
                } catch (SAXNotSupportedException e) {
                    // No lexical reporting available
                } catch (SAXNotRecognizedException e) {
                    // No lexical reporting available
                }
            }

            // Set validation
            try {
                parser.setFeature(
                    "http://xml.org/sax/features/validation", validate);
                parser.setFeature(
                    "http://xml.org/sax/features/namespaces", true);
                parser.setFeature(
                    "http://xml.org/sax/features/namespace-prefixes", false);
                if (saxErrorHandler != null) {
                     parser.setErrorHandler(saxErrorHandler);
                } else {
                     parser.setErrorHandler(new BuilderErrorHandler());
                }
            }
            catch (SAXNotRecognizedException e) {
                // No validation available
                if (validate) {
                    throw new JDOMException(
                        "Validation feature not recognized by " + 
                        saxDriverClass);
                }
            }
            catch (SAXNotSupportedException e) {
                // No validation available
                if (validate) {
                    throw new JDOMException(
                        "Validation not supported by " + saxDriverClass);
                }
            }

/*
            // Set entity expansion
            try {
                //Crimson doesn't support this feature but does report it 
                // as true so...
                if (parser.getFeature("http://xml.org/sax/features/external-general-entities") != expand) { 
                    parser.setFeature("http://xml.org/sax/features/external-general-entities", expand);
                }

            }
            catch (SAXNotRecognizedException e) {
                // No entity expansion available
                throw new JDOMException(
                  "Entity expansion feature not recognized by " + 
                  saxDriverClass);
            }
            catch (SAXNotSupportedException e) {
                // No entity expansion available
                throw new JDOMException(
                  "Entity expansion feature not supported by " +
                  saxDriverClass);
            }
*/
            
            parser.parse(in);

            return doc;
        }
        catch (Exception e) {
            if (e instanceof SAXParseException) {
                SAXParseException p = (SAXParseException)e;
                String systemId = p.getSystemId();
                if (systemId != null) {
                    throw new JDOMException("Error on line " + 
                              p.getLineNumber() + " of document "
                              + systemId, e);
                } else {
                    throw new JDOMException("Error on line " +
                              p.getLineNumber(), e);
                }
            } else {
                throw new JDOMException("Error in building", e);
            }
        }
    }

    /**
     * <p>
     * This builds a document from the supplied
     *   input stream.
     * </p>
     *
     * @param in <code>InputStream</code> to read from.
     * @return <code>Document</code> - resultant Document object.
     * @throws JDOMException when errors occur in parsing.
     */
    public Document build(InputStream in) throws JDOMException {
        return build(new InputSource(in));
    }

    /**
     * <p>
     * This builds a document from the supplied
     *   filename.
     * </p>
     *
     * @param file <code>File</code> to read from.
     * @return <code>Document</code> - resultant Document object.
     * @throws JDOMException when errors occur in parsing.
     */
    public Document build(File file) throws JDOMException {
        try {
            URL url = fileToURL(file);
            return build(url);
        } catch (MalformedURLException e) {
            throw new JDOMException("Error in building", e);
        }
    }

    /**
     * <p>
     * This builds a document from the supplied
     *   URL.
     * </p>
     *
     * @param url <code>URL</code> to read from.
     * @return <code>Document</code> - resultant Document object.
     * @throws JDOMException when errors occur in parsing.
     */
    public Document build(URL url) throws JDOMException {
        String systemID = url.toExternalForm();
        return build(new InputSource(systemID));
    }

    /**
     * <p>
     * This builds a document from the supplied
     *   input stream.
     * </p>
     *
     * @param in <code>InputStream</code> to read from.
     * @param systemId base for resolving relative URIs
     * @return <code>Document</code> - resultant Document object.
     * @throws JDOMException when errors occur in parsing.
     */
    public Document build(InputStream in, String systemId)
        throws JDOMException {

        InputSource src = new InputSource(in);
        src.setSystemId(systemId);
        return build(src);
    }

    /**
     * <p>
     * This builds a document from the supplied
     *   Reader.
     * </p>
     *
     * @param in <code>Reader</code> to read from.
     * @return <code>Document</code> - resultant Document object.
     * @throws JDOMException when errors occur in parsing.
     */
    public Document build(Reader characterStream) throws JDOMException {
        return build(new InputSource(characterStream));
    }

    /**
     * <p>
     * This builds a document from the supplied
     *   Reader.
     * </p>
     *
     * @param in <code>Reader</code> to read from.
     * @param systemId base for resolving relative URIs
     * @return <code>Document</code> - resultant Document object.
     * @throws JDOMException when errors occur in parsing.
     */
    public Document build(Reader characterStream, String SystemId)
        throws JDOMException {

        InputSource src = new InputSource(characterStream);
        src.setSystemId(SystemId);
        return build(src);
    }

    /**
     * <p>
     * This builds a document from the supplied
     *   URI.
     * </p>
     * @param systemId URI for the input
     * @return <code>Document</code> - resultant Document object.
     * @throws JDOMException when errors occur in parsing.
     */
    public Document build(String systemId) throws JDOMException {
        return build(new InputSource(systemId));
    }

    /**
     * Imitation of File.toURL(), a JDK 1.2 method, reimplemented 
     * here to work with JDK 1.1.
     *
     * @see java.io.File
     *
     * @param f the file to convert
     * @return the file path converted to a file: URL
     */
    protected URL fileToURL(File f) throws MalformedURLException {
        String path = f.getAbsolutePath();
        if (File.separatorChar != '/') {
            path = path.replace(File.separatorChar, '/');
        }
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        if (!path.endsWith("/") && f.isDirectory()) {
            path = path + "/";
        }
        return new URL("file", "", path);
    }

    /**
     * <p>
     * This sets whether or not to expand entities for the builder.
     * A true means to expand entities as normal content.  A false means to
     * leave entities unexpanded as <code>EntityRef</code> objects.  The 
     * default is true.
     * </p>
     *
     * @param expand <code>boolean</code> indicating whether entity expansion 
     * should occur.
     */
    public void setExpandEntities(boolean expand) {
        this.expand = expand;
    }
}
