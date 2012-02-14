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

@SuppressWarnings("javadoc")
public class TestBean implements java.io.Serializable {
    /**
	 * Default.
	 */
	private static final long serialVersionUID = 1L;

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

	@Override
	public String toString() {
		return "TestBean[name='" + name + "', age=" + age + ", birthdate=" + birthdate + ", friend=" + friend + "]";
	}


	// Test

	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws java.io.IOException {

		try {
			BeanMapper mapper = new BeanMapper();
			mapper.addMapping("birthdate", "dob");	// element mapping
			mapper.addMapping("age", "dob", "age");	// attribute mapping

			mapper.setBeanPackage("org.jdom2.contrib.beans");

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
			XMLOutputter o = new XMLOutputter(Format.getPrettyFormat());
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
