/*-- 

 Copyright (C) 2001-2004 Jason Hunter & Brett McLaughlin.
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
 created by Jason Hunter <jhunter_AT_jdom_DOT_org> and
 Brett McLaughlin <brett_AT_jdom_DOT_org>.  For more information
 on the JDOM Project, please see <http://www.jdom.org/>.

 */

// Explicitly left in the default package

import java.io.*;
import java.util.*;
import java.util.jar.*;
import java.util.zip.*;

import org.jdom.*;
import org.jdom.input.*;

/**
 * This class is not part of the core JDOM API, but implements the main
 * method executed when the JDOM JAR is invoked directly. For example,
 * to "run" the JDOM JAR directly use a command line such as the following:
 *
 * <pre>
 *   java -jar jdom.jar
 * </pre>
 *
 * When run like this, the output will display product and version
 * information, a brief description of JDOM and the authors, all as
 * specified in the file META-INF/info.xml contained within the JAR.
 * If this file does not exist in the JAR, then the JAR has been
 * incorrectly built.  Any exceptions will pop the call stack and end the
 * program with a message describing the error.
 *
 * @see http://java.sun.com/docs/books/tutorial/jar/
 *
 * @author Steven Gould <steven.gouldATcgiusa.com>
 */
public class JDOMAbout {
    /**
     * The main(!) method executed when this JAR file is run. Outputs
     * information about the JAR, as extracted from the META-INF/info.xml
     * file.
     * @param args Ignored. 
     */
    public static void main(String args[]) throws Exception {
        JDOMAbout.Info info = new JDOMAbout().new Info();

        // Shortcut for info.title (because it's used so much)
        String title = info.title;

        // Output product information
        System.out.println(title + " version " + info.version);
        System.out.println("Copyright " + info.copyright);
        System.out.println();
        
        // Display product/JAR "description"
        System.out.println(info.description);
        System.out.println();
        
        // Iterate through authors, outputting each in turn
        System.out.println("Authors:");
        Iterator it = info.authors.iterator();
        while (it.hasNext()) {
            Author author = (Author)it.next();
        
            // Output the author's name
            System.out.print("  " + author.name);
                
            // If the author has an e-mail address, output it too
            if (author.email == null) {
                System.out.println();
            } else {
                System.out.println(" <" + author.email + ">");
            }
        }
        System.out.println();
    
        // Display "license" information
        System.out.println(title + " license:");
        System.out.println(info.license);
        System.out.println();
    
        // Display "support" information
        System.out.println(title + " support:");
        System.out.println(info.support);
        System.out.println();
    
        // Display "license" information
        System.out.println(title + " web site: " + info.website);
        System.out.println();
    }

    /**
     * This class encapsulates the locating of, and reading of, the JAR
     * information file. This file should be named META-INF/info.xml. The
     * default constructor initializes all members variables from the
     * content of the info.xml file.
     *
     * <p>This class has been separated out for a couple of reasons:
     * <ol>
     *  <li>So that the presentation of the data in info.xml can be
     *      separated out from the reading of the file - enabling a "looser"
     *      coupling between the data and presentation.</li>
     *  <li>Just in case we later decide to make this information
     *      publically accessible. If this happens we'll probably want to
     *      implement "get" methods for each of the member variables. For
     *      now, it was decided that doing so may be too much of an overhead
     *      for an internal class.</li>
     * </ol>
     */
    private class Info {
        /** The product title, or product name. */
        String title;

        /**
         * The product/JAR version number - or string. This can contain
         * characters.
         */
        String version;

        /** The JAR copyright message. */
        String copyright;

        /** A description of the contents of this JAR. */
        String description;

        /** A list of authors of this product/JAR. */
        List authors;

        /** The product/JAR license information. */
        String license;

        /** Product/JAR support information. */
        String support;

        /** The main web site for this product/JAR. */
        String website;
    
        /**
         * Constructor for the Info class - initializes all member
         * variables with values taken from the JAR information file.
         * The JAR information file should be called META-INF/info.xml
         * and be contained within the JAR file.
         * Any exceptions are passed to the caller.
         */
        Info() throws Exception {
            final String INFO_FILENAME = "META-INF/info.xml";

            SAXBuilder builder = new SAXBuilder();
        
            // Find the INFO_FILENAME file in any JAR in the classpath
            // Use a brute-force search
            JarFile jarFile = null;
            ZipEntry zipEntry = null;

            String classpath = System.getProperty("java.class.path");
            StringTokenizer tokenizer = new StringTokenizer(classpath, ";:");
            while (tokenizer.hasMoreTokens() && zipEntry == null) {
              String token = tokenizer.nextToken();

              try {
                // Try to create a JarFile representation of the JAR
                jarFile = new JarFile(token);
            
                // Get a reference to the info.xml entry in the JAR
                zipEntry = jarFile.getEntry(INFO_FILENAME);
              }
              catch (Exception e) {
              }
            }
            
            if (zipEntry == null) {
              throw new FileNotFoundException(INFO_FILENAME + 
                " not found; it should be within the JDOM JAR but isn't");
            }

            // Get input stream for reading info.xml file
            InputStream in = jarFile.getInputStream(zipEntry);
            
            // Using JDOM, read the info.xml file
            Document doc = builder.build(in);
            Element root = doc.getRootElement();

            title = root.getChildTextTrim("title");
            version = root.getChildTextTrim("version");
            copyright = root.getChildTextTrim("copyright");
            description = root.getChildTextTrim("description");
            license = root.getChildTextTrim("license");
            support = root.getChildTextTrim("support");
            website = root.getChildTextTrim("web-site");

            List authorElements = root.getChildren("author");
            authors = new LinkedList();
            Iterator it = authorElements.iterator();
            while (it.hasNext()) {
                Element element = (Element)it.next();
                    
                Author author = new Author();
                author.name = element.getChildTextTrim("name");
                author.email = element.getChildTextTrim("e-mail");

                authors.add(author);
            }
        }
    }

    /**
     * A helper class that stores information about a single Author. By
     * using this class, the Info class can store Author information
     * without any dependence on JDOM and XML elements.
     */
    private class Author {
        /** The name of this author. */
        String name;

        /** The e-mail address of this author. */
        String email;
    }
}
