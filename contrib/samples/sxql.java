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

import java.sql.*;
import java.io.*;
import java.util.*;

import org.jdom.*;
import org.jdom.output.*;
import org.jdom.contrib.input.ResultSetBuilder;

/**
 * A simple sample harness for JDOM ResultSetBuilder
 *
 * @author R.Sena (raff@aromatic.org)
 */
public class sxql {

  /**
   * Return a connection (i.e. from a connection pool)
   */
  public static Connection getConnection(String driver, String jdbcURL, 
                                     String user, String pass) 
                                            throws Exception {
    Class.forName(driver);
    return DriverManager.getConnection(jdbcURL, user, pass);
  }

  /**
   * Execute a SQL query and return the result as XML
   * (as a String. But this can be changed to return a DOM/SAX/JDOM tree,
   * to be used, for example, as input to an XSLT processor)
   */
  public static String query(Connection con, String query,
                         String root, String row, 
                         String ns, int maxRows, 
                         Vector attributes, Vector elements) 
                                throws Exception {
    // Execute SQL Query
    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery(query);

    // Create a ResultSetBuilder
    ResultSetBuilder builder = new ResultSetBuilder(rs);

    // Configure some parameters...

    if (root != null) {
      builder.setRootName(root);
    }

    if (row != null) {
      builder.setRowName(row);
    }

    if (ns != null) {
      String namespace = null;
      String url = null;
      int sep = ns.indexOf("/");

      if (sep > 0) {
        namespace = ns.substring(0, sep);
        url = ns.substring(sep+1);
        builder.setNamespace(Namespace.getNamespace(namespace, url));
      }
    }

    if (maxRows > 0) {
      builder.setMaxRows(maxRows);
    }

    for (int i=0; i < attributes.size(); i++) {
      String colName = (String) attributes.get(i);
      String attrName = null;

      if (colName.indexOf("/") >= 0) {
        String col = colName;
        int sep = col.indexOf("/");
        colName = col.substring(0, sep);
        attrName = col.substring(sep+1);
      }

      try {    // If it looks like an integer, is the column number
        int colNum = Integer.parseInt(colName);

        if (attrName == null) {
          builder.setAsAttribute(colNum);    // attrName = column Name
        }
        else {
          builder.setAsAttribute(colNum, attrName);
        }
      }
      catch (NumberFormatException e) {
        // Otherwise it's the column name
        if (attrName == null) {
          builder.setAsAttribute(colName); // attrName = column Name
        }
        else {
          builder.setAsAttribute(colName, attrName);
        }
      }
    }

    // Rename element
    for (int i=0; i < elements.size(); i++) {
      String colName = (String) elements.get(i);
      String elemName = null;

      if (colName.indexOf("/") >= 0) {
        String col = colName;
        int sep = col.indexOf("/");
        colName = col.substring(0, sep);
        elemName = col.substring(sep+1);
      }

      try {    // If it looks like an integer, is the column number
        int colNum = Integer.parseInt(colName);

        if (elemName != null) {  // It must have an element name
          builder.setAsElement(colNum, elemName);
        }
      }
      catch (NumberFormatException e) {
        // Otherwise it's the column name
        if (elemName != null) {  // It must have an element name
          builder.setAsElement(colName, elemName);
        }
      }
    }

    // Build a JDOM tree
    Document doc = builder.build();

    // Convert the result to XML (as String)
    XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    outputter.output(doc, output);
    return output.toString();
  }

  /**
   * Usage
   */
  public static void usage() {
    System.err.println(
"usage: sxql [--root=root-element] [--row=row-element]");
    System.err.println(
"            [--namespace=namespace/url] [--maxrows=max-rows]");
    System.err.println(
"            [--attribute=column/attr] [--element=column/element]");
    System.err.println(
"            driver url user pass query");
    System.err.println(
"where:");
    System.err.println(
"       --root:       set root element name (root-element)");
    System.err.println(
"       --row:        set row element name (root-element)");
    System.err.println(
"       --namespace:  set namespace (namespace, url)");
    System.err.println(
"       --maxrows:    set maximum number of rows (max-rows)");
    System.err.println(
"       --attribute:  column as attribute (column name/number, attribute-name)");
    System.err.println(
"       --element:    rename column (column name/number, element-name)");
    System.err.println(
"       driver:       driver class name");
    System.err.println(
"       url:          JDBC url");
    System.err.println(
"       user:         database user");
    System.err.println(
"       pass:         database password");
    System.err.println(
"       query:        SQL query");
  }

  /**
   * Main entry point
   */
  public static void main(String [] args) throws Exception {
    String root = null;
    String row = null;
    String ns = null;
    int maxRows = 0;
    Vector attributes = new Vector();
    Vector elements = new Vector();

    int i;

    // Read configuration parameters

    for (i=0; i < args.length; i++) {
      if (args[i].startsWith("--")) {
        if (args[i].startsWith("--attribute="))
        attributes.add(args[i].substring(12));
        else
        if (args[i].startsWith("--element="))
        elements.add(args[i].substring(10));
        else
        if (args[i].startsWith("--root="))
        root = args[i].substring(7);
        else
        if (args[i].startsWith("--row="))
        row = args[i].substring(6);
        else
        if (args[i].startsWith("--namespace="))
        ns = args[i].substring(12);
        else
        if (args[i].startsWith("--maxrows="))
        maxRows = Integer.parseInt(args[i].substring(10));
      }
      else {
        break;
      }
    }

    if (args.length - i != 5) {
      usage();
    }
    else {
      System.out.println(
        query(getConnection(args[i+0], args[i+1], args[i+2], args[i+3]),
              args[i+4], root, row, ns, maxRows, attributes, elements));
    }
  }
}
