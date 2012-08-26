/*--

 Copyright (C) 2011-2012 Jason Hunter & Brett McLaughlin.
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

/**
 Support classes for building JDOM documents and content using SAX parsers.

 <h2>Introduction</h2>
 Skip to the <a href="#Examples">Examples</a> section for a quick bootstrap. 
 <p>
 The {@link org.jdom2.input.SAXBuilder} class parses input and produces JDOM
 output. It does this using three 'pillars' of functionality, which when combined
 constitute a 'parse'.
 <p>
 The three pillars are:
 <ol>
 <li>The SAX Parser - this is a 'third-party' parser such as Xerces.
 <li>The SAX Event Handler - which reads the data produced by the parser
 <li>The JDOMFactory - which converts the resulting data in to JDOM content
 </ol>
 There are many different ways of parsing the document from its input state
 (DocType-validating, etc.), and there are also different ways to interpret
 the SAX events. Finally there are different ways to produce JDOM Content using
 different implementations of the JDOMFactory.
 <p>
 SAXBuilder provides a central location where these three pillars are configured.
 Some configuration settings require coordinated changes to both the SAX parser
 and the SAX handler, and SAXBuilder ensures the coordination is maintained.

 <h2>Setting the Pillars</h2>
 SAXBuilder provides a number of different mechanisms for stipulating what the
 three pillars will be:
 <ul>
 <li>A Constructor: {@link org.jdom2.input.SAXBuilder#SAXBuilder(XMLReaderJDOMFactory, SAXHandlerFactory, org.jdom2.JDOMFactory)}
 <li>A default constructor {@link org.jdom2.input.SAXBuilder#SAXBuilder()}
 that chooses a non-validating JAXP sourced XMLReader Factory
 {@link org.jdom2.input.sax.XMLReaders#NONVALIDATING} which it
 mates with a Default {@link org.jdom2.input.sax.SAXHandler} factory, and the 
 {@link org.jdom2.DefaultJDOMFactory}. 
 <li>A number of other constructors that are mostly for backward-compatibility
 with JDOM 1.x. These other constructors affect what 
 {@link org.jdom2.input.sax.XMLReaderJDOMFactory} will be used but still use
 the default SAXHandler and JDOMFactory values.
 <li>Methods to change whatever was constructed:
 <ul>
 <li>{@link org.jdom2.input.SAXBuilder#setXMLReaderFactory(XMLReaderJDOMFactory)}
 <li>{@link org.jdom2.input.SAXBuilder#setSAXHandlerFactory(SAXHandlerFactory)}
 <li>{@link org.jdom2.input.SAXBuilder#setJDOMFactory(org.jdom2.JDOMFactory)}
 </ul>
 </ul>


 <h2>The XMLReaderJDOMFactory Pillar</h2>
 
 A brief history of XML Parsers in Java:<br>
 XML Parsers have been available in Java from essentially 'the beginning'. There
 have been a number different ways to access these parsers though:
 <ol>
 <li>Create the parser directly 'by name'.
 <li>Use the SAX (and later the SAX 2.0) API to locate a parser.
 <li>Use JAXP (versions 1, through 1.4) API to locate a parser.
 </ol>
 <p>
 In addition to the different ways of creating an XML parser, there have also
 been updates to the way the actual SAX parsing API is exposed to Java (the Java
 interface). The SAX specification was revised with version 2.0. The 'new' SAX
 version introduced the XMLReader concept, which replaces the XMLParser concept.
 These two concepts aim to accomplish the same goal, but do it in different
 ways.
 <p>
 JDOM 2.x requires an XMLReader (SAX 2.0) interface, thus your XML parser needs
 to be compatible with SAX 2.0 (for the XMLReader), but should be accessible
 through JAXP which is the more modern and flexible access system.
 <p>
 The purpose of the XMLReaderJDOMFactory Pillar is to give the SAXBuilder an
 XMLReader instance (a SAX 2.0 parser). To get an XMLReader the
 SAXBuilder delegates to the {@link org.jdom2.input.sax.XMLReaderJDOMFactory}
 by calling {@link org.jdom2.input.sax.XMLReaderJDOMFactory#createXMLReader()} 
 <p>
 XMLReader instances can be created in a few different ways, and also they
 can be set to perform the SAX parse in a number of different ways. The classes
 in this package are designed to make it easier and faster to locate the XMLReader
 that is suitable for the XML parsing you intend to do. At the same time, if the
 parsing you intend to do is outside the normal bounds of how JDOM is used, you
 still have the functionality to create a completely custom mechanism for setting
 the XMLReader for SAXBuilder.
 <p>
 There are two typical ways to specify and create an XMLReader
 instance: using JAXP, and using the SAX2.0 API. If necessary you can also create
 direct instances of XMLReader implementations using 'new' constructors, but
 each SAX implementation has different class names for their SAX drivers so doing
 raw constructors is not portable and not recommended.
 <p>
 Where possible it is recommended that you use the JAXP mechanism for obtaining
 XMLReaders because:
 <ul>
 <li>It is more 'modern'.
 <li>It provides a more consistent interface to different SAX implementations 
 <li>It provides cleaner and more portable support for validating using the
 {@link javax.xml.validation.Validator} mechanisms.
 <li>It allows you to create differently-configured 'factories' that
 create XMLReaders in a pre-specified format (SAX2.0 has a single global
 factory that creates raw XMLReader instances that then need to be
 re-configured for your task). 
 </ul>

 <h3>JAXP Factories</h3>
 JDOM exposes six factories that use JAXP to source XMLReaders. These factories
 cover almost all conditions under which you would want a SAX parser:
 <ol>
 <li>A simple non-validating SAX parser
 <li>A validating parser that uses the DOCTYPE references in the XML to validate
 against.
 <li>A validating parser that uses the XML Schema (XSD) references embedded in
 the XML to validate against.
 <li>A factory that uses a specific JAXP-based parser that can optionally
 validate using the DTD DocType.
 <li>A validating parser that uses an external Schema (XML Schema, Relax NG,
 etc.) to validate the XML against.
 <li>A special case of the Schema-validating factory that specialises in XML
 Schema (XSD) validation and provides an easy way to create validating
 XMLReaders based on single or multiple input XSD documents. 
 </ol>
 The first three are all relatively simple, and are available as members of the
 {@link org.jdom2.input.sax.XMLReaders} enumeration. These members
 are 'singletons' that can be used in a multi-threaded and concurrent way to
 provide XMLReaders that are configured correctly for the respective behaviour.
 <p>
 To parse with a specific (rather than the default) JAXP-based XML Parser
 you can use the {@link org.jdom2.input.sax.XMLReaderJAXPFactory}. This factory
 can optionally be set to do DTD validation during the parse.  
 <p>
 To validate using an arbitrary external Schema you can use the
 {@link org.jdom2.input.sax.XMLReaderSchemaFactory} to create an instance for
 the particular Schema you want to validate against. Because this requires an
 input Schema it cannot be constructed as a singleton like the others. There
 are constructors that allow you to use a specific (rather than the default)
 JAXP-compatible parser.
 <p>
 {@link org.jdom2.input.sax.XMLReaderXSDFactory} is a special case of
 XMLReaderSchemaFactory which internally uses an efficient mechanism to
 compile Schema instances from one or many input XSD documents which can come
 from multiple sources. There are constructors that allow you to use a specific
 (rather than the default) JAXP-compatible parser.

 <h3>SAX 2.0 Factory</h3>

 JDOM supports using the SAX 2.0 API for creating XMLReaders through using
 either the 'default' SAX 2.0 implementation or a particular SAX Driver class.
 SAX2.0 support is available by creating instances of the
 {@link org.jdom2.input.sax.XMLReaderSAX2Factory} class.
 <p>
 It should be noted that it is preferable to use JAXP in JDOM because it is a
 more flexible API that allows more portable code to be created. The JAXP
 interface in JDOM is also able to support a wider array of functionality
 out-of-the-box, but the same functionality would require SAX-implementation
 specific configuration.
 <p>
 JDOM does not provide a pre-configured way to do XML Schema validation through
 the SAX2.0 API though. The SAX 2.0 API does not expose a convenient way to
 configure different SAX implementations in a consistent way, so it is up to the
 JDOM user to wrap the XMLReaderSAX2Factory in such a way that it reconfigures
 the XMLReader to be appropriate for the task at hand.

 <h3>Custom Factories</h3>
 If your circumstances require it you can create your own implementation of the
 {@link org.jdom2.input.sax.XMLReaderJDOMFactory} to provide XMLReaders configured
 as you like them. It will probably be best if you wrap an existing implementation
 with your custom code though in order to get the best results fastest.
 <p>
 Note that the existing JDOM implementations described above all set the
 generated XMLReaders to be namespace-aware and to supply namespace-prefixes.
 Custom implementations should also ensure that this is set unless you absolutely
 know what you are doing.


 <h2>The SAXHandlerFactory Pillar</h2>

 The SAXHandler interprets the SAX calls and provides the information to the
 JDOMFactory to create JDOM content. SAXBuilder creates a SAXHandler from the
 {@link org.jdom2.input.sax.SAXHandlerFactory} pillar. It is unusual for a JDOM
 user to need to customise the manner in which this happens, but, in the event
 that you do you can create a subclass of the SAXHandler class, and then create
 an instance of the SAXHandlerFactory that returns new subclass instances.
 This new factory can become a pillar in SAXBuilder and supply custom SAXHandlers
 to the parse process.  


 <h2>The JDOMFactory Pillar</h2>

 There are a couple of reasons for changing the JDOMFactory pillar in SAXBuilder.
 The default JDOMFactory used is the {@link org.jdom2.DefaultJDOMFactory}. This
 factory validates the values being used to create JDOM content. There is also
 the {@link org.jdom2.UncheckedJDOMFactory} which does not validate the data, so
 it should only be used if you are absolutely certain that your SAX source can
 never provide illegal content. You may have other reasons for creating a custom
 JDOMFactory such as if you need to create custom versions of JDOM Content like
 a custom Element subclass. 

 <h2>Configuring the Pillars</h2>

 The JDOMFactory pillar is not configurable; you can only replace it entirely.
 The other two pillars are configurable though, but you should inspect the
 getters and setters on {@link org.jdom2.input.SAXBuilder} to identify what can
 (by default) be changed easily. Remember, if you have anything that needs to be
 customised beyond what SAXBuilder offers you can always replace a pillar with a
 custom implementation.

 <h2>Execution Model</h2>
 Once all the pillars are set and configured to your satisfaction you can 'build'
 a JDOM Document from a source. The actual parse process consists of a 'setup',
 'parse', and 'reset' phase.
 <p>
 The setup process involves obtaining an XMLReader from the XMLReaderJDOMFactory
 and a SAXHandler (configured to use the JDOMFactory) from the SAXHandlerFactory.
 These two instances are then configured to meet the settings specified on
 SAXBuilder, and once configured they are 'compiled' in to a SAXBuilderEngine.
 <p>
 The SAXBuilderEngine is a non-configurable 'embodiment' of the configuration of
 the SAXBuilder when the engine was created, and it contains the entire
 'workflow' necessary to parse the input in to JDOM content. Further, it is a
 guarantee that the XMLReader and SAXHandler instances in the SAXBuilderEngine
 are never shared with any other engine or entity (assuming that the respective
 factories never issue the same instances multiple times). There is no guarantee
 made for the JDOMFactory being unique for each SAXBuilderEngine, but JDOMFactory
 instances are supposed to be reentrant/thread-safe.
 <p>
 The 'parse' phase starts once the setup phase is complete and the
 SAXBuilderEngine has been created. The created engine is used to parse the input,
 and the resulting Document is returned to the client.
 <p>
 The 'reset' phase happens after the completion of the 'parse' phase, and it
 resets the SAXBuilderEngine to its initial state, ready to process the next
 parse request.

 <h2>Parser Reuse</h2>
 A large amount of the effort involved in parsing the document is actually the
 creation of the XMLReader and the SAXHandler instances, as well as applying the
 configuration to those instances (the 'setup' phase).
 <p>
 JDOM2 uses the new SAXBuilderEngine to represent the state of the SAXBuilder
 at the moment prior to the parse. SAXBuilder will then 'remember' and reuse this
 exact SAXBuilderEngine until something changes in the SAXBuilder configuration.
 As soon as the configuration changes in any way the engine will be forgotten and
 a new one will be created when the SAXBuilder next parses a document.
 <p>
 If you turn off parser reuse with
 {@link org.jdom2.input.SAXBuilder#setReuseParser(boolean)} then SAXBuilder will
 immediately forget the engine, and it will also forget it after each build (i.e.
 SAXBuilder will create a new SAXBuilderEngine each parse).
 <p>
 It follows then that as long as you do not change the SAXBuilder configuration
 then the SAXBuilder will always reuse the same SAXBuilderEngine. This is very
 efficient because there is no configuration management between parses, and the
 procedure completely eliminates the 'setup' component for all but the first
 parse. 

 <h2>Parser Pooling</h2>
 In order to facilitate Parser pooling it is useful to export the
 SAXBuilderEngine as a stand-alone reusable parser. At any time you can call
 {@link org.jdom2.input.SAXBuilder#buildEngine()} and you can get a newly
 created SAXBuilderEngine instance. The SAXBuilderEngine has the same 'build'
 methods as SAXBuilder, and these are exposed as the
 {@link org.jdom2.input.sax.SAXEngine} interface. Both SAXBuilder and 
 SAXBuilderEngine implement the SAXEngine interface. Thus, if you use Parser
 pools you can pool either the SAXBuilder or the SAXBuilderEngine in the same
 pool.
 <p>
 It is most likely though that what you will want to do is to create a single
 SAXBuilder that represents the configuration you want, and then you can use this
 single SAXBuilder to create multiple SAXEngines as you need them in the pool by
 calling the <code>buildEngine()</code> method.

 <h2>Examples</h2>
 <a name="Examples" />
 Create a simple SAXBuilder and parse a document:
 <p>
 <pre>
 SAXBuilder sb = new SAXBuilder();
 Document doc = sb.build(new File("file.xml"));
 </pre>
 <p>
 Create a DTD validating SAXBuilder and parse a document:
 <p>
 <pre>
 SAXBuilder sb = new SAXBuilder(XMLReaders.DTDVALIDATING);
 Document doc = sb.build(new File("file.xml"));
 </pre>
 Create an XSD (XML Schema) validating SAXBuilder using the XSD references inside
 the XML document and parse a document:
 <p>
 <pre>
 SAXBuilder sb = new SAXBuilder(XMLReaders.XSDVALIDATING);
 Document doc = sb.build(new File("file.xml"));
 </pre>
 <p>
 Create an XSD (XML Schema) validating SAXBuilder the hard way (see the next
 example for an easier way) using an external XSD and parse a document
 (see {@link org.jdom2.input.sax.XMLReaderSchemaFactory}):
 <p>
 <pre>
 SchemaFactory schemafac =
 SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
 Schema schema = schemafac.newSchema(new File("myschema.xsd"));
 XMLReaderJDOMFactory factory = new XMLReaderSchemaFactory(schema);
 SAXBuilder sb = new SAXBuilder(factory);
 Document doc = sb.build(new File("file.xml"));
 </pre>
 <p>
 Create an XSD (XML Schema) validating SAXBuilder the easy way
 (see {@link org.jdom2.input.sax.XMLReaderXSDFactory}):
 <p>
 <pre>
 File xsdfile = new File("myschema.xsd");
 XMLReaderJDOMFactory factory = new XMLReaderXSDFactory(xsdfile);
 SAXBuilder sb = new SAXBuilder(factory);
 Document doc = sb.build(new File("file.xml"));
 </pre>

 */
package org.jdom2.input.sax;

