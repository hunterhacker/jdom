package org.jdom.contrib.beans;
import org.jdom.*;
import org.jdom.output.*;
import org.jdom.input.*;
import java.util.*;

public class TestBean implements java.io.Serializable {
    private String name;
    private int age;
    private Date birthdate;
    private TestBean friend;

    public String getName() {
	return name;
    }
    public int getAge() {
	return age;
    }
    public Date getBirthdate() {
	return birthdate;
    }
    public TestBean getFriend() {
	return friend;
    }

    public void setName(String name) {
	this.name = name;
    }
    public void setAge(int age) {
	this.age = age;
    }
    public void setBirthdate(Date birthdate) {
	this.birthdate = birthdate;
    }
    public void setFriend(TestBean friend) {
	this.friend = friend;
    }

    public String toString() {
	return "TestBean[name='" + name + "', age=" + age + ", birthdate=" + birthdate + ", friend=" + friend + "]";
    }


    // Test
        
    public static void main(String[] args) throws java.beans.IntrospectionException, java.io.IOException {

	try {
	    BeanMapper mapper = new BeanMapper();
	    mapper.addMapping("birthdate", "dob");	// element mapping
	    mapper.addMapping("age", "dob", "age");	// attribute mapping

	    mapper.setBeanPackage("org.jdom.contrib.beans");
	
	    // test bean->jdom

	    TestBean alex = new TestBean();
	    alex.setName("Alex");
	    alex.setAge(31);
	    alex.setBirthdate(new Date(69, 7, 8));

	    TestBean amy = new TestBean();
	    amy.setName("Amy");
	    amy.setAge(25);
	    amy.setBirthdate(new Date(75, 4, 1));

	    alex.setFriend(amy);
	    
	    Document doc = mapper.toDocument(alex);
	    XMLOutputter o = new XMLOutputter("  ", true);
	    o.output(doc, System.out);
	    System.out.println();

	    // test jdom->bean
	    TestBean test2 = (TestBean)mapper.toBean(doc);
	    System.out.println(test2);
	}
	catch (BeanMapperException e) {
	    e.printStackTrace();
	}
    }	
    
}
