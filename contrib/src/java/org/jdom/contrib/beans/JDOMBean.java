// Early draft of class from Alex Chaffee <guru@edamame.stinky.com>.
// He'll be submitting with better docs and an Apache license.

package org.jdom.contrib.beans;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.*;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;


public class JDOMBean {

    /** Default SAX Driver class to use */
    private static final String DEFAULT_SAX_DRIVER_CLASS =
        "org.apache.xerces.parsers.SAXParser";

    /** SAX Driver Class to use */
    private String saxDriverClass = DEFAULT_SAX_DRIVER_CLASS;

    /** <code>{@link SAXBuilder}</code> instance to use */
    private SAXBuilder builder;

    /** file cache **/
    private Map files = new HashMap();

    /** where to locate files **/
    private File fileRoot;

    public JDOMBean() {
        init();
    }

    public JDOMBean(String saxDriverClass) {
        this.saxDriverClass = saxDriverClass;
        init();
    }

    /**
     * <p>
     * This will create an instance of <code>{@link SAXBuilder}</code>
     *   for use in the rest of this program.
     * </p>
     *
     * @param saxDriverClass <code>String</code> name of driver class to use.
     */
    public void init() {
        builder = new SAXBuilder(saxDriverClass);
    }

    // todo: pathname normalization (remove ./ and foo/../ and so forth)
    public Document getDocument(String filename) throws JDOMException {
        FileInfo info = (FileInfo) files.get(filename);
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

    public Element getRootElement(String file) throws JDOMException {
        Document doc = getDocument(file);
        if (doc != null) return doc.getRootElement();
        return null;
    }

    public void setFileRoot(String root) {
        if (!root.endsWith("/")) {
            root = root + "/";
        }
        this.fileRoot = new File(root);
    }

    public String getFileRoot() {
        if (fileRoot == null) return null;
        else return fileRoot.getAbsolutePath();
    }

    private File getFile(String filename) {
        File file;
        if (fileRoot == null) {
            return new File(filename);
        }
        else {
            return new File(fileRoot, filename);
        }
    }

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

    // test
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
        }
    }

}

