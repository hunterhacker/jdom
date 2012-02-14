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

import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.Reader;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;

import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.XMLReader;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.XMLFilterImpl;

import org.iso_relax.verifier.Verifier;
import org.iso_relax.verifier.VerifierFactory;
import org.iso_relax.verifier.VerifierConfigurationException;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.output.SAXOutputter;
import org.jdom2.output.JDOMLocator;

/**
 * The compiled representation of a schema definition capable of
 * performing in-memory validation of JDOM documents and elements.
 * <p>
 * This class relies on
 * <a href="http://iso-relax.sourceforge.net/JARV/">JARV</a> (Java
 * API for RELAX Verifiers) and requires an implementation of this
 * API at runtime, such as Sun's
 * <a href="http://wwws.sun.com/software/xml/developers/multischema/">Multi-Schema
 * Validator</a>.</p>
 * <p>
 * To validate a document against a W3C XML Schema definition:</p>
 * <pre>
 * import org.jdom2.contrib.schema.Schema;
 *
 *    String uri = &lt;The URL of the schema document&gt;;
 *    Document doc = &lt;a JDOM document&gt;;
 *
 *    Schema schema = Schema.parse(uri, Schema.W3C_XML_SCHEMA);
 *    List errors = schema.validate(doc);
 *    if (errors != null) {
 *      // Validation errors
 *      for (Iterator i=errors.iterator(); i.hasNext(); ) {
 *        ValidationError e = (ValidationError)(i.next());
 *        System.out.println(e);
 *      }
 *    }
 *    // Else: No error, document is valid.
 * </pre>
 * <p>
 * The current limitations are those of JARV, i&#46;e&#46; no support for
 * validating a document against multiple schemas. This can be work around 
 * for elements (calling validate(Element) on another Schema) but not for 
 * attributes.</p>
 *
 * @author Laurent Bihanic
 */
public class Schema {

    /**
     * Type for W3C XML Schema definitions.
     */
    public final static Type W3C_XML_SCHEMA =
            new Type("W3C XML Schema", "http://www.w3.org/2001/XMLSchema");
    /**
     * Type for RELAX NG schema definitions.
     */
    public final static Type RELAX_NG =
            new Type("RELAX NG", "http://relaxng.org/ns/structure/0.9");
    /**
     * Type for RELAX Core schema definitions.
     */
    public final static Type RELAX_CORE =
            new Type("RELAX Core", "http://www.xml.gr.jp/xmlns/relaxCore");
    /**
     * Type for RELAX Namespace schema definitions.
     */
    public final static Type RELAX_NAMESPACE =
            new Type("RELAX Namespace",
                     "http://www.xml.gr.jp/xmlns/relaxNamespace");
    /**
     * Type for TREX schema definitions.
     */
    public final static Type TREX =
            new Type("TREX", "http://www.thaiopensource.com/trex");

    /**
     * The URI of the schema document, if known.
     */
    private final String uri;

    /**
     * The schema type.
     */
    private final Type type;

    /**
     * The JARV compiled schema.
     */
    private final org.iso_relax.verifier.Schema compiledSchema;

    /**
     * Compiles a schema definition.
     *
     * @param  source   the SAX input source to read the schema
     *                  definition from.
     * @param  type     the schema type.
     *
     * @throws JDOMException   if the schema document can not be
     *                         parsed according to the specfied type.
     * @throws IOException     if an I/O error occurred while reading
     *                         the schema document.
     */
    private Schema(InputSource source, Type type)
            throws JDOMException, IOException {

        if ((source == null) || (type == null)) {
            throw new IllegalArgumentException("source/type/compiledSchema");
        }
        this.uri = source.getSystemId();
        this.type = type;

        try {
            VerifierFactory vf = VerifierFactory.newInstance(type.getLanguage());

            this.compiledSchema = vf.compileSchema(source);
        }
        catch (IOException e) {
            throw e;
        }
        catch (Exception e) {
            throw new JDOMException("Failed to parse schema \"" + this.uri +
                                    "\": " + e.getMessage(), e);
        }
    }

    /**
     * Returns the location of the schema document, if known.
     *
     * @return the location of the schema document or
     *         <code>null</code> if inknown.
     */
    public String getURI() {
        return this.uri;
    }

    /**
     * Returns the schema type.
     *
     * @return the schema type.
     */
    public Type getType() {
        return this.type;
    }

    /**
     * Allocates an JARV <code>Verifier</code> object for
     * validating against this schema.
     *
     * @return an JARV <code>Verifier</code> configured with this
     *         schema.
     *
     * @throws JDOMException   if the verifier allocation failed.
     */
    private Verifier newVerifier() throws JDOMException {
        try {
            return this.compiledSchema.newVerifier();
        }
        catch (VerifierConfigurationException e) {
            throw new JDOMException(
                    "Failed to allocate schema verifier: " + e.getMessage(), e);
        }
    }

    /**
     * Validates a JDOM document against this schema.
     *
     * @param  doc   the JDOM document to validate.
     *
     * @return a list of {@link ValidationError} objects or
     *         <code>null</code> if the document is compliant with
     *         this schema.
     *
     * @throws JDOMException   if errors were encountered that
     *                         prevented the validation to proceed.
     */
    public List<ValidationError> validate(Document doc) throws JDOMException {
        ValidationErrorHandler errorHandler = new ValidationErrorHandler();
        try {
            Verifier verifier = this.newVerifier();
            verifier.setErrorHandler(errorHandler);

            errorHandler.setContentHandler(verifier.getVerifierHandler());
            new SAXOutputter(errorHandler).output(doc);
        }
        catch (SAXException e) { /* Fatal validation error encountered. */
        }

        // Retrieve validation errors, if any.
        return errorHandler.getErrors();
    }

    /**
     * Validates a JDOM element against this schema.
     *
     * @param  element   the JDOM element to validate.
     *
     * @return a list of {@link ValidationError} objects or
     *         <code>null</code> if the element is compliant with
     *         this schema.
     *
     * @throws JDOMException   if errors were encountered that
     *                         prevented the validation to proceed.
     */
    public List<ValidationError> validate(Element element) throws JDOMException {
        ValidationErrorHandler errorHandler = new ValidationErrorHandler();
        try {
            Verifier verifier = this.newVerifier();
            verifier.setErrorHandler(errorHandler);

            List<Element> nodes = new ArrayList<Element>();
            nodes.add(element);

            errorHandler.setContentHandler(verifier.getVerifierHandler());
            new SAXOutputter(errorHandler).output(nodes);
        }
        catch (SAXException e) { /* Fatal validation error encountered. */
        }

        // Retrieve validation errors, if any.
        return errorHandler.getErrors();
    }

    /**
     * Parses a schema definition located at the specified URI
     * according to the specified schema type and returns a compiled
     * schema object.
     *
     * @param  uri    the location of the schema document.
     * @param  type   the schema type.
     *
     * @return the compiled schema.
     *
     * @throws JDOMException   if the schema document can not be
     *                         parsed according to the specfied type.
     * @throws IOException     if an I/O error occurred while reading
     *                         the schema document.
     */
    public static Schema parse(String uri, Type type)
            throws JDOMException, IOException {
        return parse(new InputSource(uri), type);
    }

    /**
     * Parses a schema definition from the specified byte stream
     * according to the specified schema type and returns a compiled
     * schema object.
     *
     * @param  byteStream   the byte stream to read the schema
     *                      definition from.
     * @param  type         the schema type.
     * @param  uri          the location of the schema document
     *                      (optional).
     *
     * @return the compiled schema.
     *
     * @throws JDOMException   if the schema document can not be
     *                         parsed according to the specfied type.
     * @throws IOException     if an I/O error occurred while reading
     *                         the schema document.
     */
    public static Schema parse(InputStream byteStream, Type type, String uri)
            throws JDOMException, IOException {
        InputSource source = new InputSource(byteStream);
        source.setSystemId(uri);

        return parse(source, type);
    }

    /**
     * Parses a schema definition from the specified character stream
     * according to the specified schema type and returns a compiled
     * schema object.
     *
     * @param  reader   the character stream to read the schema
     *                  definition from.
     * @param  type     the schema type.
     * @param  uri      the location of the schema document
     *                  (optional).
     *
     * @return the compiled schema.
     *
     * @throws JDOMException   if the schema document can not be
     *                         parsed according to the specfied type.
     * @throws IOException     if an I/O error occurred while reading
     *                         the schema document.
     */
    public static Schema parse(Reader reader, Type type, String uri)
            throws JDOMException, IOException {
        InputSource source = new InputSource(reader);
        source.setSystemId(uri);

        return parse(source, type);
    }

    /**
     * Parses a schema definition from the specified file
     * according to the specified schema type and returns a compiled
     * schema object.
     *
     * @param  file   the file to read the schema definition from.
     * @param  type   the schema type.
     *
     * @return the compiled schema.
     *
     * @throws JDOMException   if the schema document can not be
     *                         parsed according to the specfied type.
     * @throws IOException     if an I/O error occurred while reading
     *                         the schema document.
     */
    public static Schema parse(File file, Type type)
            throws JDOMException, IOException {
        InputSource source = new InputSource(new FileInputStream(file));
        source.setSystemId(file.getAbsolutePath());

        return parse(source, type);
    }

    /**
     * Parses a schema definition from the specified SAX input source
     * according to the specified schema type and returns a compiled
     * schema object.
     *
     * @param  source   the SAX inout source to read the schema
     *                  definition from.
     * @param  type     the schema type.
     *
     * @return the compiled schema.
     *
     * @throws JDOMException   if the schema document can not be
     *                         parsed according to the specfied type.
     * @throws IOException     if an I/O error occurred while reading
     *                         the schema document.
     */
    public static Schema parse(InputSource source, Type type)
            throws JDOMException, IOException {
        return new Schema(source, type);
    }


    /**
     * A SAX XML filter implementation to capture the document locator
     * and make all validation errors and warnings available once the
     * validation is complete.
     */
    private static final class ValidationErrorHandler extends XMLFilterImpl {
        /** The list of validation errors. */
        private List<ValidationError> errors = new LinkedList<ValidationError>();
        /** The JDOM locator object provided by SAXOutputter. */
        private JDOMLocator locator = null;

        /**
         * Constructs a new ValidationErrorHandler XML filter with no
         * parent.
         */
        public ValidationErrorHandler() {
            super();
        }

        /**
         * Constructs a new ValidationErrorHandler XML filter with the
         * specified parent.
         *
         * @param  parent   the parent XMLReader or XMLFilter.
         */
        @SuppressWarnings("unused")
		public ValidationErrorHandler(XMLReader parent) {
            super(parent);
        }

        /**
         * Returns the list of validation errors reported during
         * document validation.
         *
         * @return the list of validation errors or <code>null</code>
         *         if the document is valid.
         */
        public List<ValidationError> getErrors() {
            return (this.errors.size() == 0) ? null : this.errors;
        }

        /**
         * Returns the JDOM node currently being ouputted by
         * SAXOuputter.
         *
         * @return the current JDOM node.
         */
        private Object getCurrentNode() {
            return (this.locator != null) ? this.locator.getNode() : null;
        }

        /**
         * <i>[ContentHandler interface support]</i> Sets the locator
         * object for locating the origin of SAX document events.
         *
         * @param  locator   an object that can return the location of
         *                   any SAX document event.
         */
        @Override
		public void setDocumentLocator(Locator locator) {
            if (locator instanceof JDOMLocator) {
                this.locator = (JDOMLocator) locator;
            }
        }

        /**
         * <i>[ErrorHandler interface support]</i> Receives
         * notification of a non-recoverable error.
         *
         * @param  e   the error information encapsulated in a SAX
         *             parse exception.
         *
         * @throws SAXException   any SAX exception, possibly wrapping
         *                        another exception.
         */
        @Override
		public void fatalError(SAXParseException e) throws SAXException {
            this.errors.add(new ValidationError(ValidationError.FATAL,
                                                e.getMessage(), this.getCurrentNode()));
            throw e;
        }

        /**
         * <i>[ErrorHandler interface support]</i> Receives
         * notification of a recoverable error.
         *
         * @param  e   the error information encapsulated in a SAX
         *             parse exception.
         *
         * @throws SAXException   any SAX exception, possibly wrapping
         *                        another exception.
         */
        @Override
		public void error(SAXParseException e) throws SAXException {
            this.errors.add(new ValidationError(ValidationError.ERROR,
                                                e.getMessage(), this.getCurrentNode()));
        }

        /**
         * <i>[ErrorHandler interface support]</i> Receives
         * notification of a warning.
         *
         * @param  e   the warning information encapsulated in a SAX
         *             parse exception.
         *
         * @throws SAXException   any SAX exception, possibly wrapping
         *                        another exception.
         */
        @Override
		public void warning(SAXParseException e) throws SAXException {
            this.errors.add(new ValidationError(ValidationError.WARNING,
                                                e.getMessage(), this.getCurrentNode()));
        }
    }


    /**
     * Class to support type-safe enumeration design pattern to
     * represent schema types
     */
    public static final class Type {
        /** Schema type name. */
        private final String name;
        /** JARV schema type identifier. */
        private final String language;

        /**
         * Type constructor, private on purpose.
         *
         * @param  name       the schema type printable name.
         * @param  language   the unique identifier for the schema
         *                    type (URI).
         */
        protected Type(String name, String language) {
            this.name = name;
            this.language = language;
        }

        /**
         * Returns the printable name of this schema type.
         *
         * @return the schema type name.
         */
        public String getName() {
            return this.name;
        }

        /**
         * Returns the URI that uniquemy identifies this schema type.
         *
         * @return the schema type identifier.
         */
        public String getLanguage() {
            return this.language;
        }

        /**
         * Returns a unique identifier for this type.
         *
         * @return a unique identifier for this type.
         *
         * @see java.lang.Object#hashCode()
         */
        @Override
		public int hashCode() {
            return this.language.hashCode();
        }

        /**
         * Returns a string representation of this type suitable for
         * debugging and diagnosis.
         *
         * @return a string representation of this type.
         *
         * @see java.lang.Object#toString()
         */
        @Override
		public String toString() {
            return this.language;
        }

        /**
         * Tests for type equality. This is only necessary to handle
         * cases where two <code>Type</code> objects are loaded by
         * different class loaders.
         *
         * @param  o   the object compared for equality to this type.
         *
         * @return <code>true</code> if and only if <code>o</code>
         *         represents the same type as this object.
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

