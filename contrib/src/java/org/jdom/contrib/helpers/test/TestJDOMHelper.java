package org.jdom.contrib.helpers.test;

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

/**
 * Please put a description of your test here.
 * 
 * @author unascribed
 * @version 0.1
 */
import junit.framework.*;

import java.io.*;
import java.util.*;
import org.jdom.*;
import org.jdom.input.*;
import org.jdom.output.*;
import org.jdom.contrib.helpers.*;

public final class TestJDOMHelper extends junit.framework.TestCase {
    /**
     *  Construct a new instance. 
     */
    public TestJDOMHelper(String name) {
        super(name);
    }
    /**
     * The main method runs all the tests in the text ui
     */
    public static void main (String args[]) 
     {
        junit.textui.TestRunner.run(suite());
    }
    /**
     * This method is called before a test is executed.
     */
    public void setUp() {
    }
    /**
     * The suite method runs all the tests
     */
    public static Test suite () {
        TestSuite suite = new TestSuite(TestJDOMHelper.class);
        return suite;
    }
    /**
     * This method is called after a test is executed.
     */
    public void tearDown() {
    }

    public void test_sortElements() throws Exception {
        // Read the unsorted test document. 
        StringReader reader = new StringReader(UNSORTED);
        SAXBuilder builder = new SAXBuilder();
        Document testDoc = builder.build(reader);

        // Sort all the groups.
        List children = testDoc.getRootElement().getChildren();
        Iterator iter = children.iterator();
        while(iter.hasNext()) {
            Element child = (Element)iter.next();
            JDOMHelper.sortElements(child, new ElementNameComparator());
        }

        // Read the sorted reference document.
        reader = new StringReader(SORTED);
        builder = new SAXBuilder();
        Document referenceDoc = builder.build(reader);


//        new XMLOutputter().output(testDoc, System.out);
//        System.out.println("\n\n=================================\n\n");
//        new XMLOutputter().output(referenceDoc, System.out);

        // Compare the two.
        boolean success = matches(testDoc.getContent(), referenceDoc.getContent());
        assertTrue("sortElements failed", success);
    }
        
        // Compares two Elements by comparing their tag names.
        private static class ElementNameComparator implements Comparator {
            public int compare(Object o1, Object o2) {
                String s1 = ((Element)o1).getName();
                String s2 = ((Element)o2).getName();
                // (A real implementation might use java.text)
                return s1.compareTo(s2);
            }
        }

    // Returns true if the two contents lists are identical 
    // Elements, Attributes, and Text. Ignores all other items.
    private static boolean matches(List contents1, List contents2) {
        if (contents1.size() != contents2.size()) {
            return false;
        }

        Iterator iter1 = contents1.iterator();
        Iterator iter2 = contents2.iterator();
        while(iter1.hasNext()) {
            Object child1 = iter1.next();
            Object child2 = iter2.next();
            if (!child1.getClass().equals(child2.getClass())) {
                return false;
            }

            if (child1 instanceof Element) {
                Element element1 = (Element)child1;
                Element element2 = (Element)child2;
                if (!element1.getName().equals(element2.getName()))
                    return false;
                if (!matches(element1.getAttributes(), element2.getAttributes()))
                    return false;
                if (!matches(element1.getContent(), element2.getContent()))
                    return false;
            }

            if (child1 instanceof Attribute) {
                Attribute attr1 = (Attribute)child1;
                Attribute attr2 = (Attribute)child2;
                if (!attr1.getName().equals(attr2.getName()))
                    return false;
                if (!attr1.getValue().equals(attr2.getValue()))
                    return false;
            }

            if (child1 instanceof Text) {
                Text text1 = (Text)child1;
                Text text2 = (Text)child2;
                if (!text1.getText().equals(text2.getText()))
                    return false;
            }
        }

        return true;
    }

    private static final String UNSORTED = 
        "<root>\n" +
        "\n" +
        "    <group1>\n" +
        "        <yankee><child/><child/></yankee>\n" +
        "        <xray/>\n" +
        "        <bravo>data</bravo>\n" +
        "        <delta/>\n" +
        "        <charlie data='value'/>\n" +
        "        <zulu/>\n" +
        "        <alpha/>\n" +
        "    </group1>\n" +
        "\n" +
        "    <group2><yankee/>\n" +
        "        <alpha/>\n" +
        "        <delta/>\n" +
        "        <xray/>\n" +
        "        <bravo/></group2>\n" +
        "\n" +
        "    <group3><alpha/><delta/><charlie/><xray/><bravo/><yankee/><zulu/></group3>\n" +
        "\n" +
        "    <group4>\n" +
        "    </group4>\n" +
        "\n" +
        "    <group5>\n" +
        "        <alpha/>\n" +
        "    </group5>\n" +
        "\n" +
        "    <group6><alpha/></group6>\n" +
        "\n" +
        "    <group7></group7>\n" +
        "\n" +
        "</root>";

    private static final String SORTED = 
        "<root>\n" +
        "\n" +
        "    <group1>\n" +
        "        <alpha/>\n" +
        "        <bravo>data</bravo>\n" +
        "        <charlie data='value'/>\n" +
        "        <delta/>\n" +
        "        <xray/>\n" +
        "        <yankee><child/><child/></yankee>\n" +
        "        <zulu/>\n" +
        "    </group1>\n" +
        "\n" +
        "    <group2><alpha/>\n" +
        "        <bravo/>\n" +
        "        <delta/>\n" +
        "        <xray/>\n" +
        "        <yankee/></group2>\n" +
        "\n" +
        "    <group3><alpha/><bravo/><charlie/><delta/><xray/><yankee/><zulu/></group3>\n" +
        "\n" +
        "    <group4>\n" +
        "    </group4>\n" +
        "\n" +
        "    <group5>\n" +
        "        <alpha/>\n" +
        "    </group5>\n" +
        "\n" +
        "    <group6><alpha/></group6>\n" +
        "\n" +
        "    <group7></group7>\n" +
        "\n" +
        "</root>";
}
