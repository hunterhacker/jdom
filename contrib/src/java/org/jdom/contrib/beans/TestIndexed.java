package org.jdom.contrib.beans;
import org.jdom.*;
import org.jdom.output.*;
import org.jdom.input.*;
import java.util.*;
import java.util.List;
import java.beans.*;
import java.lang.reflect.*;
    
public class TestIndexed implements java.io.Serializable {
    private String name;
    private List toppings = new ArrayList();
    private int[] measurements = new int[] { 36,24,38 };

    public String getName() {
	return name;
    }
    public void setName(String name) {
	this.name = name;
    }

    public String getTopping(int i) {
	return (String)toppings.get(i);
    }

    public void setTopping(int i, String topping) {
	while (i >= toppings.size()) {
	    toppings.add(null);
	}	    
	toppings.set(i, topping);
    }

    public String[] getTopping() {
	String[] a = new String[toppings.size()];
	for (int i=0; i<toppings.size(); ++i) {
	    a[i] = (String)toppings.get(i);
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
    
    public String toString() {
	StringBuffer buf = new StringBuffer();
	buf.append("TestIndexed[name='" + name + "'" );
	for (int i=0; i<toppings.size(); ++i) {
	    buf.append(", topping=" + toppings.get(i));
	}
	buf.append(", measurements=");
	for (int i=0; i<toppings.size(); ++i) {
	    buf.append(" " + measurements[i]);
	}
	buf.append("]");
	return buf.toString();
    }

    public static void main(String[] args) throws java.beans.IntrospectionException, java.io.IOException, BeanMapperException {

	BeanMapper mapper = new BeanMapper();
	mapper.setBeanPackage("org.jdom.contrib.beans");
	
	TestIndexed pizza = new TestIndexed();
	pizza.setName("Abominable");
	pizza.setTopping(0, "Anchovies");
	pizza.setTopping(1, "Steak Tartare");
	pizza.setTopping(2, "Raw Eggs");
	
	BeanInfo info = Introspector.getBeanInfo(pizza.getClass());
	PropertyDescriptor[] ps = info.getPropertyDescriptors();
	for (int i=0; i<ps.length; ++i) {
	    if (ps[i] instanceof IndexedPropertyDescriptor) {
		IndexedPropertyDescriptor p = (IndexedPropertyDescriptor)ps[i];
		System.out.println("Indexed property " +p.getName() + " " + p.getShortDescription());
		System.out.println("Type = " + p.getPropertyType());
		System.out.println("Getter = " + p.getReadMethod());
		System.out.println("Indexed Getter = " + p.getIndexedReadMethod());
	    }		    
	}
	
	System.out.println("===== Testing toDocument()");
	Document doc = mapper.toDocument(pizza);
	XMLOutputter o = new XMLOutputter("  ", true);
	o.output(doc, System.out);
	System.out.println();
	
	// test jdom->bean
	System.out.println("===== Testing toBean()");
	TestIndexed test2 = (TestIndexed)mapper.toBean(doc);
	System.out.println(test2);

	int[] test = new int[10];
	Class a1 = test.getClass();
	Class a2 = a1.getSuperclass();
	System.out.println("classes: " + a1 + " " + a2);
    }
}
