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

import org.jdom2.*;
import org.jdom2.output.*;
import java.util.*;
import java.beans.*;
    
@SuppressWarnings("javadoc")
public class TestIndexed implements java.io.Serializable {
    /**
	 * Default.
	 */
	private static final long serialVersionUID = 1L;

    private String name;
    private List<String> toppings = new ArrayList<String>();
    private int[] measurements = new int[]{36, 24, 38};

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTopping(int i) {
        return toppings.get(i);
    }

    public void setTopping(int i, String topping) {
        while (i >= toppings.size()) {
            toppings.add(null);
        }
        toppings.set(i, topping);
    }

    public String[] getTopping() {
        String[] a = new String[toppings.size()];
        for (int i = 0; i < toppings.size(); ++i) {
            a[i] = toppings.get(i);
        }
        return a;
    }

    public void setTopping(String[] x) {
        if (x != null)
            toppings = Arrays.asList(x);
    }

    public int[] getMeasurements() {
        return measurements;
    }

    public void setMeasurements(int[] measurements) {
        this.measurements = measurements;
    }

    @Override
	public String toString() {
    	StringBuilder buf = new StringBuilder();
        buf.append("TestIndexed[name='" + name + "'");
        for (int i = 0; i < toppings.size(); ++i) {
            buf.append(", topping=" + toppings.get(i));
        }
        buf.append(", measurements=");
        for (int i = 0; i < toppings.size(); ++i) {
            buf.append(" " + measurements[i]);
        }
        buf.append("]");
        return buf.toString();
    }

    public static void main(String[] args) throws java.beans.IntrospectionException, java.io.IOException, BeanMapperException {
        BeanMapper mapper = new BeanMapper();
        mapper.setBeanPackage("org.jdom2.contrib.beans");

        TestIndexed pizza = new TestIndexed();
        pizza.setName("Abominable");
        pizza.setTopping(0, "Anchovies");
        pizza.setTopping(1, "Steak Tartare");
        pizza.setTopping(2, "Raw Eggs");

        BeanInfo info = Introspector.getBeanInfo(pizza.getClass());
        PropertyDescriptor[] ps = info.getPropertyDescriptors();
        for (int i = 0; i < ps.length; ++i) {
            if (ps[i] instanceof IndexedPropertyDescriptor) {
                IndexedPropertyDescriptor p = (IndexedPropertyDescriptor) ps[i];
                System.out.println("Indexed property " + p.getName() + " " + p.getShortDescription());
                System.out.println("Type = " + p.getPropertyType());
                System.out.println("Getter = " + p.getReadMethod());
                System.out.println("Indexed Getter = " + p.getIndexedReadMethod());
            }
        }

        System.out.println("===== Testing toDocument()");
        Document doc = mapper.toDocument(pizza);
        XMLOutputter o = new XMLOutputter(Format.getPrettyFormat());
        o.output(doc, System.out);
        System.out.println();

        // test jdom->bean
        System.out.println("===== Testing toBean()");
        TestIndexed test2 = (TestIndexed) mapper.toBean(doc);
        System.out.println(test2);

        int[] test = new int[10];
        Class<?> a1 = test.getClass();
        Class<?> a2 = a1.getSuperclass();
        System.out.println("classes: " + a1 + " " + a2);
    }
}
