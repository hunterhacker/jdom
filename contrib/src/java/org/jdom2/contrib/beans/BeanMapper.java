/*--

 Copyright (C) 2000-2004 Jason Hunter & Brett McLaughlin & Alex Chaffee.
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

import java.lang.reflect.*;
import java.util.*;
import java.beans.*;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Attribute;
import org.jdom2.Namespace;

/**
 * Maps a JavaBean to an XML tree and vice versa.  (Yes, it's yet
 * another XML Data Binding solution.)  Given a JavaBean, it will
 * produce a JDOM tree whose elements correspond to the bean's
 * property values.  Given a JDOM tree, it will return a new instance
 * of that bean whose properties have been set using the corresponding
 * values in the JDOM tree.  <p>
 *
 * By default, it assumes each element maps to a property of the same
 * name, subject to normal capitalization rules.  That is, an element
 * &lt;foo&gt; will map to the methods setFoo and getFoo.  You can
 * change this behavior by calling the various <code>addMapping</code>
 * methods.  For instance, to map a an element &lt;date&gt; to the
 * property "birthDate" (using methods setBirthDate and getBirthDate),
 * call <pre>mapper.addMapping("birthDate", "date");</pre> You can
 * also map a property to an attribute, either of a child or of the
 * parent (see JavaDoc for
 * <a href="#addMapping(java.lang.String, java.lang.String, java.lang.String)">
 * addMapping(String property, String element, String attribute)</a>
 * for details). <p>
 *
 * During Bean -> JDOM conversion, if a BeanInfo object is found, it
 * will be respected. See JavaDoc for java.beans.Introspector. <p>
 *
 * If a given property, element, or attribute is to be skipped
 * (ignored) during conversion, call the appropriate ignore method
 * (ignoreProperty, ignoreElement, or ignoreAttribute).  This is also
 * appropriate if your bean has multiple accessors (properties) for
 * the same underlying data. <p>
 *
 * Support for Namespaces is rudimentary at best and has not been
 * well-tested; if you specify a Namespace then all created elements
 * and attributes will be in that namespace (or all elements not in
 * that namespace will be skipped, for JDOM->Bean mapping). <p>
 *
 * If a bean property type is a Java array, its items will be mapped
 * to multiple child elements of the bean element (multiple children
 * at the base level, not one child).  This is to provide an easier
 * transition for XML documents whose elements contain multiple
 * children with the same name.<p>
 *
 * Please try this out on your own beans.  If there is a case that
 * fails to do what you like (for instance, properties with custom
 * class types), let me know and I'll try to work it out. <p>
 *
 * <pre>TODO:
 * support list properties (collections other than Java arrays)
 * allow lists/arrays to map to either scattered elements or a single nested element
 * sort XML elements in some order other than random BeanInfo order
 * support for BeanInfoSearchPaths
 * multiple packages searched for beans
 * better support for Namespaces (hard)
 * allow stringconverter to map object -> string (not just string -> object)
 * id/idref support for cyclical references
 * more type converters
 * custom property converters
 * known issue: inner class bean can't be found jdom->bean (workaround: define mapping containing class name)
 * </pre>
 *
 * <h2>Example:</h2>
 * <h3>TestBean</h3>
 * <pre>
 * public class TestBean implements java.io.Serializable {
 *    public String getName() { ... }
 *    public int getAge() { ... }
 *    public Date getBirthdate() { ... }
 *    public TestBean getFriend() { ... }
 *    public void setName(String name) { ... }
 *    public void setAge(int age) { ... }
 *    public void setBirthdate(Date birthdate) { ... }
 *    public void setFriend(TestBean friend) { ... }
 *    public String toString() { ... }
 * }
 * </pre>
 * <h3>XML Representation</h3>
 * <pre>&nbsp;&lt;testBean&gt;
 * &nbsp;&nbsp;&lt;dob age="31"&gt;Fri Aug 08 00:00:00 EDT 1969&lt;/dob&gt;
 * &nbsp;&nbsp;&lt;name&gt;Alex&lt;/name&gt;
 * &nbsp;&nbsp;&lt;friend&gt;
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;testBean&gt;
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;dob age="25"&gt;Thu May 01 00:00:00 EDT 1975&lt;/dob&gt;
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;name&gt;Amy&lt;/name&gt;
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;/testBean&gt;
 * &nbsp;&nbsp;&lt;/friend&gt;
 * &lt;/testBean&gt;
 * </pre>
 * <h3>Mapping code</h3>
 * <pre> BeanMapper mapper = new BeanMapper();
 * mapper.addMapping("birthdate", "dob");        // element mapping
 * mapper.addMapping("age", "dob", "age");        // attribute mapping
 * mapper.setBeanPackage("org.jdom2.contrib.beans");
 * </pre>
 * <h3>Converting Bean to JDOM</h3>
 * <pre>Document doc = mapper.toDocument(alex);</pre>
 * <h3>Converting JDOM to Bean</h3>
 * <pre>TestBean alex = mapper.toBean(doc);</pre>
 *
 * @author Alex Chaffee (alex@jguru.com)
 **/

@SuppressWarnings("javadoc")
public class BeanMapper {

	protected String beanPackage;
    protected Namespace namespace;
    protected boolean ignoreMissingProperties = false;
    protected boolean ignoreNullProperties = true;
    protected List<Mapping> mappings = new ArrayList<Mapping>();
    protected StringConverter stringconverter = new StringConverter();
    
    /**
     * Default constructor.  If you are only doing bean -> XML
     * mapping, you may use the mapper immediately.  Otherwise, you
     * must call setBeanPackage.
     **/
    public BeanMapper()
    {
    }
    
    // Properties

    /**
     * @param beanPackage the name of the package in which to find the 
     * JavaBean classes to instantiate
     **/
    public void setBeanPackage(String beanPackage)
    {
        this.beanPackage = beanPackage;
    }
    
    /**
     * Use this namespace when creating the XML element and all
     * child elements.
     **/
    public void setNamespace(Namespace namespace)
    {
        this.namespace = namespace;
    }

    /**
     * Get the object responsible for converting a string to a known
     * type.  Once you get this, you can call its setFactory() method
     * to add a converter for a custom type (for which toString() does
     * not suffice).  The default string converter has a factory that
     * recognizes several date formats (including ISO8601 -
     * e.g. "2000-09-18 18:51:22-0600" or some substring thereof).
     **/
    public StringConverter getStringConverter() {
        return stringconverter;
    }

    /**
     * Set a custom string converter.
     **/
    public void setStringConverter(StringConverter stringconverter) {
        this.stringconverter = stringconverter;
    }
        
    /**
     * In mapping from Bean->JDOM, if we encounter an property with a 
     * null value, should
     * we ignore it or add an empty child element/attribute (default: true)?
     * @param b true = ignore, false = empty element
     **/
    public void setIgnoreNullProperties(boolean b) {
        ignoreNullProperties = b;
    }

    /**
     * In mapping from JDOM->Bean, if we encounter an element or
     * attribute without a corresponding property in the bean, should
     * we ignore it or throw an exception (default: false)?
     * @param b true = ignore, false = throw exception
     **/
    public void setIgnoreMissingProperties(boolean b) {
        ignoreMissingProperties = b;
    }


    // Bean -> JDOM Mapping
    
    /**
     * Converts the given bean to a JDOM Document.
     * @param bean the bean from which to extract values
     **/
    public Document toDocument(Object bean) throws BeanMapperException {
        return toDocument(bean, null);
    }

    /**
     * Converts the given bean to a JDOM Document.
     * @param bean the bean from which to extract values
     * @param name the name of the root element (null => use bean class name)
     **/
    public Document toDocument(Object bean, String elementName) 
                      throws BeanMapperException {
        Element root = toElement(bean, elementName);
        Document doc = new Document(root);
        return doc;
    }

    /**
     * Converts the given bean to a JDOM Element.  
     * @param bean the bean from which to extract values
     * @param elementName the name of the element (null => use bean class name)
     **/
    public Element toElement(Object bean) throws BeanMapperException
    {
        return toElement(bean, null);
    }
    
    /**
     * Converts the given bean to a JDOM Element.  
     * @param bean the bean from which to extract values
     * @param elementName the name of the element (null => use bean class name)
     **/
    public Element toElement(Object bean, String elementName) 
                     throws BeanMapperException {
        BeanInfo info;
        try {
            // cache this?
            info = Introspector.getBeanInfo(bean.getClass());
        }
        catch (IntrospectionException e) {
            throw new BeanMapperException("Mapping bean " + bean, e);
        }
        
        // create element
        Element element;
        String beanname;
        if (elementName != null) {
            element = createElement(elementName);
        }
        else {
            Class<?> beanclass = info.getBeanDescriptor().getBeanClass();
            beanname = unpackage(beanclass.getName());
            element = createElement(beanname);
        }
        
        // get all properties, set as child-elements
        PropertyDescriptor[] properties = info.getPropertyDescriptors();
        for (int i=0; i<properties.length; ++i) {
            PropertyDescriptor prop = properties[i];
            String propertyName = prop.getName();

            Method method = prop.getReadMethod();

            // hack to skip Object.getClass
            if (method.getName().equals("getClass") &&
                prop.getPropertyType().getName().equals("java.lang.Class"))
                continue;

            // skip ignored properties
            if (isIgnoredProperty(propertyName))
                continue;
            
            // do we have a mapping for this property?
            Mapping mapping = getMappingForProperty(propertyName);

            // get the value
            if (method.getParameterTypes().length != 0)
                // if this getter takes parameters, ignore it
                continue;

            Object valueObject = null;            
            try {
                Object[] args = new Object[0];        // make static?
                valueObject = method.invoke(bean, args);
            }
            catch (java.lang.IllegalAccessException e) {
                throw new BeanMapperException("Mapping " + propertyName, e);
            }
            catch (java.lang.reflect.InvocationTargetException e) {
                throw new BeanMapperException("Mapping " + propertyName, e);
            }

            // convert it to a string or element or list
            Object value = convertValue(valueObject);

            if (value == null && ignoreNullProperties) {
                debug("Ignoring null " + propertyName);
                continue;
            }
            
            String childElementName;
            
            if (mapping == null) {
                childElementName = propertyName;
            }
            else
                childElementName = mapping.element;
            
            // get existing element, or create it.
            
            // we must look for an existing element even when setting
            // an element value, since it may have been subject to an
            // attribute mapping, and thus already created (without
            // any content, but with an attribute).

            Element child = null;
            if (childElementName != null) {
                if (namespace != null)
                    child = element.getChild(childElementName, namespace);
                else {
                    child = element.getChild(childElementName);
                }
                if (child == null) {
                    child = createElement(childElementName);
                    element.addContent(child);
                }
            }

            if (mapping == null || mapping.attribute == null) {
                // set as element
                setElementValue(propertyName, childElementName, 
                                element, child, value);
            }
            
            else {
                try {
                    // set as attribute
                    if (value == null)
                        value = "";  // no such thing as null attribute in XML
                    if (childElementName == null)  // add attr to parent
                        element.setAttribute(mapping.attribute, (String)value);
                    else                           // add attr to child
                        child.setAttribute(mapping.attribute, (String)value);
                }
                catch (ClassCastException e) {
                    throw new BeanMapperException(
                      "Can't set type " + value.getClass() + " as attribute");
                }
            }
        }
        
        // todo: sort in mapping order
        return element;
    } // toElement

    /**
     * convert property value (returned from getter) to a string or
     * element or list
     **/
    protected Object convertValue(Object value) throws BeanMapperException
    {
        if (value == null)
            return null;
        Object result;
        Class<?> type = value.getClass();
        String classname = type.getName();

        // todo: allow per-type callback to convert (if toString() is
        // inadequate) -- extend stringconverter?
        if (classname.startsWith("java.lang.") ||
            classname.equals("java.util.Date")
            )
        {
            result = value.toString();
        }
        else if (type.isArray()) {
            // it's an array - use java.lang.reflect.Array to extract
            // items (or wrappers thereof)            
            List<Object> list = new ArrayList<Object>();
            for (int i=0; i<Array.getLength(value); ++i) {
                Object item = Array.get(value, i);
                list.add( convertValue(item) );        // recurse
            }
            result = list;
        }
        else
        {
            // treat it like a bean
            // recursive call to mapper
            debug("Recurse on bean " + classname + "=" + value);
            result = toElement(value);
        }

        return result;
    }        // convertValue

    protected void setElementValue(String propertyName, 
                                   String elementName,
                                   Element parent,
                                   Element child,
                                   Object value) throws BeanMapperException {
        debug("setElementValue(" + propertyName + "," +
                           elementName + "," +
                           child.getName() + "," +
                           value + ")");
                           
        if (value == null) {
            // do nothing
        }
        else if (value instanceof Element) {
            child.addContent((Element)value);
        }
        else if (value instanceof String) {
            child.setText((String)value);
        }
        else if (value instanceof List) {
            for (Iterator<?> it = ((List<?>)value).iterator();
                 it.hasNext(); )
            {
                Object item = it.next();
                if (child == null) {
                    child = createElement(elementName);
                    parent.addContent(child);
                }
                setElementValue(propertyName, elementName, parent, child, item);
                // this'll be weird if it's an array of arrays
                child = null;
            }
        }
        else
            throw new BeanMapperException(
            "Unknown result type for property " + propertyName + ": " + value);
    }
    
    // JDOM -> Bean
    
    /**
     * Converts the given JDOM Document to a bean
     * @param document the document from which to extract values
     **/
    public Object toBean(Document document) throws BeanMapperException {
        return toBean(document.getRootElement());
    }

    public Object toBean(Element element) throws BeanMapperException {

        Object bean = instantiateBean(element.getName());
        
        Mapping mapping;
        String propertyName;

        Set<String> alreadySet = new HashSet<String>();
        
        // map Attributes of parent first
        if (element.hasAttributes()) {
	        for (Attribute attribute : element.getAttributes()) {
	            debug("Mapping " + attribute);
	            mapping = getMappingForAttribute(null, attribute.getName());
	            propertyName = (mapping==null) ?
	                            attribute.getName() : mapping.property;
	            setProperty(bean, propertyName, attribute.getValue());
	        }
        }

        // map child Elements
        //debug(element.toString() + " has " + children.size() + " children");
        for (Element child : element.getChildren()) {
            debug("Mapping " + child);

            mapping = getMappingForElement(child.getName());
            propertyName = (mapping==null) ? child.getName() : mapping.property;
            
            // set bean property from element
            PropertyDescriptor property =
                findPropertyDescriptor(bean, propertyName);
            if (property != null) {
                if (!alreadySet.contains(child.getName())) {
                    if (property.getPropertyType().isArray())
                        setProperty(bean, property, element, child);
                    else
                        setProperty(bean, property, element, child);
                }
            }

            // Now map all attributes of this child
            for (Attribute attribute : child.getAttributes()) {
                debug("Mapping " + attribute);
                mapping = getMappingForAttribute(child.getName(), 
                                                 attribute.getName());
                propertyName = (mapping==null) ?
                                attribute.getName() : mapping.property;
                setProperty(bean, propertyName, attribute.getValue());
            } // for attributes

            alreadySet.add(child.getName());
            
        } // for children

        return bean;
    } // toBean


    /**
     * return a fresh new object of the appropriate bean type for
     * the given element name.
     * @return the bean
     **/
    protected Object instantiateBean(String elementName)
                                throws BeanMapperException {
        // todo: search multiple packages
        String className = null;
        Class<?> beanClass;
        try {
            Mapping mapping = getMappingForElement(elementName);
            if (mapping != null &&
                mapping.type != null) {
                beanClass = mapping.type;
            }
            else {
                className = getBeanClassName(beanPackage, elementName);
                beanClass = Class.forName(className);
            }
            Object bean = beanClass.newInstance();
            return bean;
        }
        catch (ClassNotFoundException e) {
            throw new BeanMapperException("Class " + className + 
                " not found instantiating " + elementName + 
                " - maybe you need to add a mapping, or add a bean package", e);
        }
        catch (Exception e) {
            throw new BeanMapperException("Instantiating " + elementName, e);
        }
    }

    protected String getBeanClassName(String pbeanPackage, String elementName) {
        return (pbeanPackage == null ? "" : (pbeanPackage + ".")) +
            Character.toUpperCase(elementName.charAt(0)) +
            elementName.substring(1);
    }

    protected PropertyDescriptor findPropertyDescriptor(Object bean,
                                                        String propertyName)
                                     throws BeanMapperException {
        try {
            // cache this?
            BeanInfo info = Introspector.getBeanInfo(bean.getClass());
            PropertyDescriptor[] properties = info.getPropertyDescriptors();
            for (int i=0; i<properties.length; ++i) {
                PropertyDescriptor prop = properties[i];
                if (prop.getName().equals(propertyName))
                    return prop;
            }
        }
        catch (Exception e) {
            throw new BeanMapperException("Finding property " + propertyName + 
                                          " for bean " + bean.getClass(), e);
        }
        if (ignoreMissingProperties) {
            return null;
        }
		throw new BeanMapperException("Missing property: " + 
		    propertyName + " in bean " + bean.getClass() + ": " + bean);
    } // findPropertyDescriptor


    /**
     * set a property in the bean
     * @param bean the bean to modify
     * @param propertyName the name of the property to set
     * @param value the new value
     * @return true if successful, false if property not found
     **/
    protected boolean setProperty(Object bean, String propertyName,
                                  Object value) throws BeanMapperException {
        return setProperty(bean, findPropertyDescriptor(bean, propertyName),
                           null, value);
    }

    /**
     * set a property in the bean
     * @param bean the bean to modify
     * @param property the property to set
     * @param value the new value
     * @return true if successful, false if property not found
     **/
    protected boolean setProperty(Object bean, PropertyDescriptor property,
                                  Element parent, Object value)
                         throws BeanMapperException {
        if (property == null)
            return false;

        debug("setProperty: bean=" + bean + " property=" + 
               property.getName() + " value=" + value);
        try {
            // convert the value to the right type
            Object valueObject;            
            if (property.getPropertyType().isArray()) {
                // build array based on children of this name
                Element child = (Element)value;
                List<Element> children = parent.getChildren(child.getName());
                valueObject = buildArray(property, children);
            }
            else {
                // normal property
                valueObject = convertJDOMValue(value, 
                                               property.getPropertyType());
            }

            // find and invoke the setter
            Method setter = property.getWriteMethod();
            Class<?>[] params = setter.getParameterTypes();
            if (params.length > 1)
                throw new BeanMapperException(
                    "Setter takes multiple parameters: " + bean.getClass() + 
                    "." + setter.getName());

            Class<?> param = params[0];
            if (param != property.getPropertyType())
                debug("Weird: setter takes " + param + ", property is " + 
                       property.getPropertyType());

            debug("Invoking setter: " + setter.getName() + 
                  "(" + valueObject + ")");
            setter.invoke(bean, new Object[] { valueObject });

            return true;
        }
        catch (BeanMapperException e) {
            throw e;
        }
        catch (Exception e) {
            throw new BeanMapperException("Setting property " + 
                property.getName() + "=" + value + " in " + bean.getClass(), e);
        }
    }

    protected Object convertString(String value, Class<?> type) {
        if (value == null)
            return null;
        if (type == String.class)
            return value;
		return stringconverter.parse(value, type);
    }                

    protected Object convertJDOMValue(Object value, Class<?> type)
                            throws BeanMapperException {
        Object valueObject;
                
        // Null value
        if (value == null)
            valueObject = null;

        // String value
        else if (value instanceof String) {
            valueObject = convertString((String)value, type);
        }

        // Element value
        else if (value instanceof Element) {
            Element element = (Element)value;

            // if the setter actually takes a JDOM element, pass it
            if (type == Element.class)
                valueObject = value;

            // no children, must be a text node
            else if (element.getChildren().isEmpty())
            {
                valueObject = convertString(element.getText(), type);
            }

            // we have to convert it into a bean                
            else if (element.getChildren().size() == 1) {
                    
                // Make a recursive call to this BeanMapper to map
                // the child element
                    
                // Note that toBean could return a subclass of the
                // property type, so just let it figure out the
                // right type

                valueObject = toBean(element.getChildren().get(0));
            }
            else {
                // element with multiple children -- must be an
                // array property
                throw new BeanMapperException(
                    "Mapping of multiple child elements not implemented for " +
                    element.getName());
            }
        }
        else {
            throw new BeanMapperException("Can't map unknown type: " + 
                                          value.getClass() + "=" + value);
        }
        return valueObject;
    } // convert JDOMValue

    /**
     * @return an array of the appropriate type
     **/
    protected Object buildArray(PropertyDescriptor property, List<Element> children)
                          throws BeanMapperException {
        Class<?> arrayClass = property.getPropertyType();

        Class<?> itemClass = arrayClass.getComponentType();            

        if (itemClass == null) {
            throw new BeanMapperException("Can't instantiate array of type " +
            arrayClass);
        }
        
        // use java.lang.reflect.Array
        Object array = Array.newInstance(itemClass, children.size());

        // fill it
        for (int i = 0; i<children.size(); ++i) {
            Element child = children.get(i);
            Object value = convertJDOMValue(child, itemClass);
            debug( itemClass + "[" + i + "]=" + value );
            Array.set(array, i, value);
        }

        return array;
    }


    public static Class<?> getArrayItemClass(Class<?> arrayClass)
                                throws ClassNotFoundException {
        Class<?> itemClass;
        
        // there seems to be no way to get the item class from an
        // array class object, so parse the name
        String arrayClassName = arrayClass.getName();
        debug("Parsing array classname: " + arrayClassName);
        if (arrayClassName.equals("[B")) {
            itemClass = byte.class;
        }
        else if (arrayClassName.equals("[C")) {
            itemClass = char.class;
        }
        else if (arrayClassName.equals("[D")) {
            itemClass = double.class;
        }
        else if (arrayClassName.equals("[F")) {
            itemClass = float.class;
        }
        else if (arrayClassName.equals("[I")) {
            itemClass = int.class;
        }
        else if (arrayClassName.equals("[J")) {
            itemClass = long.class;
        }
        else if (arrayClassName.equals("[S")) {
            itemClass = short.class;
        }
        else if (arrayClassName.equals("[Z")) {
            itemClass = boolean.class;
        }
        else if (arrayClassName.startsWith("[L")) {
            itemClass = Class.forName(
                arrayClassName.substring(2, arrayClassName.length()-1));
        }
        else
            itemClass = null;
        return itemClass;
    }

    // Mappings  

    /**
     * Map a property name to an XML element
     * @param property the name of the property
     * @param element the name of the element
     **/
    public void addMapping(String property, String element) {
        addMapping(new Mapping(property, null, element, null));
    }

    /**
     * Map a property name to an attribute in an XML element.  If
     * element is null, it's an attribute on the parent element
     * (e.g. &lt;myBean id="123"&gt;); otherwise, it's an
     * attribute on a child element (e.g. &lt;myBean&gt;&lt;thing
     * id="123"&gt;).
     * @param property the name of the property
     * @param element the name of the element containing the attribute. 
     *        null => parent element
     * @param attribute the name of the attribute. null => set as element 
     **/
    public void addMapping(String property, String element, String attribute) {
        addMapping(new Mapping(property, null, element, attribute));
    }

    /**
     * Map a property name to an element or an attribute. Can also
     * specify which bean class to instantiate (applies to JDOM->Bean
     * mapping).
     *
     * @param property the name of the property. null => look for property 
     * with same name as the element
     * @param type Always convert this element name to this class. 
     * null => look for a bean with the same name as the element
     * @param element the name of the element containing the attribute. 
     * null => parent element
     * @param attribute the name of the attribute. null => set as element 
     **/
    public void addMapping(String property, Class<?> type,
                           String element, String attribute) {
        addMapping(new Mapping(property, type, element, attribute));
    }
    
    public void addMapping(Mapping mapping) {
        mappings.add(mapping);
    }

    public Mapping getMappingForProperty(String property) {
        for (Mapping m : mappings) {
            if (m.property != null && m.property.equals(property)) {
                return m;
            }
        }
        return null;
    }
    
    public Mapping getMappingForElement(String element) {
        for (Mapping m : mappings) {
            if (m.element.equals(element)) {
                return m;
            }
        }
        return null;
    }
    
    public Mapping getMappingForAttribute(String element, String attribute) {
        for (Mapping m : mappings) {
            if (m.element != null &&
                m.attribute != null &&
                m.element.equals(element) &&
                m.attribute.equals(attribute))
            {
                return m;
            }
        }
        return null;
    }
    
    public class Mapping {
        public String property;
        public Class<?> type;
        public String element;
        public String attribute;

        /**
         * @param property the name of the property. null => look for 
         * property with same name as the element
         * @param type Always convert this element name to this class. 
         * null => look for a bean with the same name as the element
         * @param element the name of the element containing the attribute. 
         * null => parent element
         * @param attribute the name of the attribute. null => set as element 
         **/
        public Mapping(String property, Class<?> type,
                       String element, String attribute) {
            this.property = property;
            this.type = type;
            this.element = element;
            this.attribute = attribute;
        }
        

    }

    // Hiding

    protected Set<String> ignoredProperties = new HashSet<String>();
    protected Set<String> ignoredElements = new HashSet<String>();
    protected Set<String> ignoredAttributes = new HashSet<String>();
    
    public void ignoreProperty(String property) {
        ignoredProperties.add(property);
    }

    public boolean isIgnoredProperty(String property) {
        return ignoredProperties.contains(property);
    }
    
    public void ignoreElement(String element) {
        ignoredElements.add(element);
    }
    
    public boolean isIgnoredElement(String element) {
        return ignoredElements.contains(element);
    }

    static protected String toAttributeString(String element,
                                              String attribute) {
        return (element == null ? "." : element) +
            "/@" + attribute;
    }
    
    public void ignoreAttribute(String element, String attribute) {
        ignoredAttributes.add(toAttributeString(element, attribute));
    }
    
    public boolean isIgnoredAttribute(String element, String attribute) {
        return ignoredAttributes.contains(
                   toAttributeString(element, attribute));
    }
    
    // Utilities

    protected Element createElement(String elementName) {
        return namespace == null ? new Element(elementName) :
            new Element(elementName, namespace);
    }
    
    protected static String unpackage(String classname) {                
        int dot = Math.max(classname.lastIndexOf("."), 
                           classname.lastIndexOf("$"));
        if (dot > -1) {
            classname = classname.substring(dot+1);
        }
        classname = Introspector.decapitalize(classname);
        return classname;
    }

    public static int debug = 0;
    protected static void debug(String msg) {
        if (debug > 0)
            System.err.println("BeanMapper: " + msg);
    }
}

