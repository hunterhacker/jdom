package org.jdom2.test.cases.output;

/* Please run replic.pl on me ! */
/**
 * Please put a description of your test here.
 * 
 * @author unascribed
 * @version 0.1
 */

import org.jdom2.*;
import java.util.*;
import org.jdom2.output.*;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import static org.junit.Assert.*;

public final class TestDOMOutputter {

    /**
     * The main method runs all the tests in the text ui
     */
    public static void main (String args[]) 
     {
        JUnitCore.runClasses(TestDOMOutputter.class);
    }

    @Test
    public void test_ForceNamespaces() throws JDOMException {
         Document doc = new Document();
         Element root = new Element("root");
         Element el = new Element("a");
         el.setText("abc");
         root.addContent(el);
         doc.setRootElement(root);

         DOMOutputter out = new DOMOutputter();
         out.setForceNamespaceAware(true);
         org.w3c.dom.Document dom = out.output(doc);
         org.w3c.dom.Element domel = dom.getDocumentElement();
         //System.out.println("Dom impl: "+ domel.getClass().getName());
         assertNotNull(domel.getLocalName()); 
    }
}
