/*--

 Copyright (C) 2000-2004 Jason Hunter & Brett McLaughlin.
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

package org.jdom2.contrib.beans;

import java.io.File;
import java.io.IOException;
import java.util.*;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.input.sax.XMLReaderJDOMFactory;
import org.jdom2.input.sax.XMLReaderSAX2Factory;
import org.jdom2.output.XMLOutputter;

// todo:
// weak references and/or timeout cache
// load from URL (instead of just from file)
// pathname normalization (remove ./ and foo/../ and so forth)
// allow DOM builder or arbitrary builder

/**
 * A light wrapper on the JDOM library that you use to load in a file
 * and turn it into a JDOM Document. It also keeps a cache of
 * already-parsed files, and checks to see if they've changed on disk,
 * and reloads if they have.  (I know there's some sort of swap-out or
 * weak-reference stuff either in JDOM or coming soon, so this may be
 * a redundant feature.)
 * <p>
 *
 * <h3>Usage from Java:</h3>
 *
 * <pre>
 * JDOMBean jdom = new JDOMBean(); // or new JDOMBean("com.foo.saxparser")
 * jdom.setFileRoot("/path/to/my/xml/documents/");
 * Document doc = jdom.getDocument("foo.xml");
 * Element root = jdom.getRootElement("foo.xml");
 * </pre>
 *
 * <h3>Usage from JSP:</h3>
 * 
 * <pre>
 *  
 * &lt;jsp:useBean id="jdom" class="JDOMBean" scope="application"&gt;
 *  &lt;% jdom.setFileRoot(application.getRealPath("")); %&gt;
 * &lt;/jsp:useBean&gt;
 *  
 * or
 *  
 * &lt;jsp:useBean id="jdom" class="JDOMBean" scope="application"&gt;
 *  &lt;jsp:setProperty name="jdom" property="fileRoot"
 * +value='&lt;%=application.getRealPath("")%&gt;' /&gt;
 * &lt;/jsp:useBean&gt;
 *                                           
 * then 
 *  
 * &lt;%
 * Element root = jdom.getRootElement("foo.xml");
 * %&gt;
 * Bar: &lt;%=root.getChild("bar").getContent()%&gt;
 * </pre>
 *
 * @author Alex Chaffee [alex@jguru.com]
 **/
@SuppressWarnings("javadoc")
public class JDOMBean {

    /** Default SAX parser class to use */
    private static final String DEFAULT_PARSER =
        "org.apache.xerces.parsers.SAXParser";

    /** SAX parser class to use */
    private String parser;

    /** <code>{@link SAXBuilder}</code> instance to use */
    private SAXBuilder builder;
    
    /** file cache **/
    private Map<String, FileInfo> files = new HashMap<String, FileInfo>();

    /** where to locate files **/
    private File fileRoot;

    /**
     * default constructor, uses "org.apache.xerces.parsers.SAXParser"
     **/
    public JDOMBean() {
        setParser(DEFAULT_PARSER);
    }

    /**
     * @param parser <code>String</code> name of driver class to use.
     **/
    public JDOMBean(String parser) {
        setParser(parser);
    }
    
    /**
     * <p>
     * This will create an instance of <code>{@link SAXBuilder}</code>
     *   for use in the rest of this program.
     * </p>
     *
     * @param parser <code>String</code> name of SAX parser class to use.
     */
    public void setParser(String parser) {
        this.parser = parser;
        XMLReaderJDOMFactory fac = new XMLReaderSAX2Factory(false, parser);
        builder = new SAXBuilder(fac);
    }

    /**
     * @return name of SAX parser class being used
     **/
    public String getParser() {
        return parser;
    }

    /**
     * All files are fetched relative to this path
     * @param root the path (absolute or relative) to the document root
     **/
    public void setFileRoot(String root) {
        if (!root.endsWith("/")) {
            root = root + "/";
        }
        this.fileRoot = new File(root);
        System.out.println("fileroot=" + fileRoot);
    }

    /**
     * @return the path (absolute or relative) to the document root
     **/
    public String getFileRoot() {
        if (fileRoot == null) { 
        	return null;
        }
        return fileRoot.getAbsolutePath();
    }
    
    /**
     * Load a file, parse it with JDOM, return a org.jdom2.Document.
     * If the file has already been parsed, return the previously
     * cached object.  If the file has changed, ignore the previously
     * parsed version and reload.  <p>
     *
     * Note that this never unloads a document, so is unsuitable for
     * long-term server-side use for a constantly changing set of
     * files.  Todo: use weak references or cache timeouts.  <p>
     *
     * Also does not do secure checking on file requested, so if
     * there's no root, and the parameter starts with a "/", this
     * could conceivably access files you don't want accessed.  So be
     * careful out there.
     *
     * @param filename the file to load, relative to file root
     * @return a JDOM Document corresponding to the given filename
     **/
    public Document getDocument(String filename) throws JDOMException, IOException {
        FileInfo info = files.get(filename);
        File file = getFile(filename);
        if (info == null ||
            info.modified < file.lastModified())
        {
            Document doc = builder.build(file);     
            info = new FileInfo(filename, file.lastModified(), doc);
            files.put(filename, info);
        }
        return info.document;
    }

    /**
     * Convenience method, calls getDocument(filename).getRootElement()
     **/
    public Element getRootElement(String file) throws JDOMException, IOException {
        Document doc = getDocument(file);
        if (doc != null) return doc.getRootElement();
        return null;
    }

    private File getFile(String filename) {
        if (fileRoot == null) {
            return new File(filename);
        }
        return new File(fileRoot, filename);
    }
    
    /**
     * Information stored in the cache
     **/
    class FileInfo {
        String name;
        long modified;
        Document document;
        public FileInfo(String name, long modified, Document document) {
            this.name = name;
            this.modified = modified;
            this.document = document;
        }
    }

    // Usage: java JDOMBean [-parser com.foo.parser] file1.xml file2.xml
    // Fetches and prints files
    public static void main(String[] args) throws IOException, JDOMException {
        int i=0;
        JDOMBean bean;
        if (args[i].equals("-parser")) {
            ++i;
            bean = new JDOMBean(args[i]);
            i++;
        }
        else {
            bean = new JDOMBean();
        }

        XMLOutputter out = new XMLOutputter();
                
        for (; i<args.length; ++i) {
            Document doc = bean.getDocument(args[i]);
            out.output(doc, System.out);
            System.out.println();
        }       
    }
    
}

