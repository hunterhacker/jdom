/*-- 

 Copyright 2000 Brett McLaughlin & Jason Hunter. All rights reserved.
 
 Redistribution and use in source and binary forms, with or without modifica-
 tion, are permitted provided that the following conditions are met:
 
 1. Redistributions of source code must retain the above copyright notice,
    this list of conditions, and the following disclaimer.
 
 2. Redistributions in binary form must reproduce the above copyright notice,
    this list of conditions, the disclaimer that follows these conditions,
    and/or other materials provided with the distribution.
 
 3. The names "JDOM" and "Java Document Object Model" must not be used to
    endorse or promote products derived from this software without prior
    written permission. For written permission, please contact
    license@jdom.org.
 
 4. Products derived from this software may not be called "JDOM", nor may
    "JDOM" appear in their name, without prior written permission from the
    JDOM Project Management (pm@jdom.org).
 
 THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 FITNESS  FOR A PARTICULAR  PURPOSE ARE  DISCLAIMED.  IN NO  EVENT SHALL  THE
 JDOM PROJECT  OR ITS CONTRIBUTORS  BE LIABLE FOR  ANY DIRECT, INDIRECT, 
 INCIDENTAL, SPECIAL,  EXEMPLARY, OR CONSEQUENTIAL  DAMAGES (INCLUDING, BUT 
 NOT LIMITED TO, PROCUREMENT  OF SUBSTITUTE GOODS OR SERVICES; LOSS
 OF USE, DATA, OR  PROFITS; OR BUSINESS  INTERRUPTION)  HOWEVER CAUSED AND ON
 ANY  THEORY OF LIABILITY,  WHETHER  IN CONTRACT,  STRICT LIABILITY,  OR TORT
 (INCLUDING  NEGLIGENCE OR  OTHERWISE) ARISING IN  ANY WAY OUT OF THE  USE OF
 THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 
 This software  consists of voluntary contributions made  by many individuals
 on  behalf of the Java Document Object Model Project and was originally 
 created by Brett McLaughlin <brett@jdom.org> and 
 Jason Hunter <jhunter@jdom.org>. For more  information on the JDOM 
 Project, please see <http://www.jdom.org/>.
 
 */

import java.io.*;
import java.sql.*;
import java.util.*;

import org.jdom.*;
import org.jdom.output.*;

import org.jdom.contrib.input.ResultSetBuilder;

public class ResultSetBuilderDemo {
    
  // SQL tables copied from the Servlets.com ISP listing application

  static final String PREP = 
    "create table rsbd ( " +
    "id int PRIMARY KEY, " +
    "name varchar(255) NOT NULL, " +
    "home_url varchar(255) NULL, " +
    "contact_email varchar(255) NULL, " +
    "contact_phone varchar(255) NULL, " +
    "location varchar(255) NULL, " +
    "comments long varchar NULL, " +
    "free_hosting boolean NULL, " +
    "state_flag tinyint NOT NULL, " + // submitted, rejected, live, dead
    "submitter_email varchar(255) NULL, " + // not displayed
    "created_time timestamp NOT NULL, " +
    "modified_time timestamp NOT NULL " +
    ")";

  static final String FILL =
    "insert into rsbd (id, name, home_url, contact_email, " +
    "contact_phone, comments, free_hosting, state_flag, created_time, " +
    "modified_time) values (2, 'sphere', null, 'info@sphere', " +
    "'1234', 'cool', true, 10, " +
    "{ts '1999-02-09 20:11:11.123455'}, " +
    "{ts '1999-03-21 22:11:11.123455'})";

  public static void main(String[] args) throws Exception {
    // Tested against Cloudscape database that comes with the J2EE ref impl
    Class.forName("COM.cloudscape.core.JDBCDriver");
    Connection con =
      DriverManager.getConnection("jdbc:cloudscape:rsbd;create=true");

    // Create and fill commands, needed only on the first run
    Statement prep = con.createStatement();
    prep.executeUpdate(PREP);

    Statement fill = con.createStatement();
    fill.executeUpdate(FILL);

    Namespace ns = Namespace.getNamespace("xhtml", "http://w3.org/etc");

    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery(
      "select id, name, home_url || contact_phone from rsbd");
    ResultSetBuilder builder = new ResultSetBuilder(rs);
    builder.setAsElement(3, "num3");
    //builder.setNamespace(ns);
    //builder.setAsElement("id", "newid");
    //builder.setAsElement("home_url", "newhome_url");
    //builder.setAsElement(4, "some4");
    //builder.setAsAttribute(4, "some4");
    //builder.setAsAttribute("state_flag");
    builder.setAsAttribute("created_time", "ctime");
    Document doc = builder.build();
    XMLOutputter outputter = new XMLOutputter();
    outputter.output(doc, System.out);
  }
}
