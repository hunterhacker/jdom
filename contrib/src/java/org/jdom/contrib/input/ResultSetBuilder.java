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

package org.jdom.contrib.input;

import java.io.*;
import java.sql.*;
import java.util.*;

import org.jdom.*;

/**
 * <p><code>ResultSetBuilder</code> builds a JDOM tree from a 
 * <code>java.sql.ResultSet</code>.  Many good ideas were leveraged from
 * SQLBuilder written from Jon Baer.</p>
 *
 * Notes:
 *   Uses name returned by rsmd.getColumnName(), not getColumnLabel() 
 *   because that is less likely to be a valid XML element title.
 *   Null values are given empty bodies.  Be aware that databases may 
 *   change the case of column names.  setAsXXX() methods are case
 *   insensitive on input column name.  Assign each one a proper output name
 *   if you're worried.  Only build() throws JDOMException.  Any exceptions 
 *   encountered in the set methods are thrown during the build().  
 *   The setAsXXX(String columnName, ...) methods do not verify that a column 
 *   with the given name actually exists.
 *   Still needs method-by-method Javadocs.
 * <p>
 * Issues: 
 *   Do attributes have to be added in a namespace?
 *
 * @author Jason Hunter
 * @author Jon Baer
 * @version 0.5
 */
public class ResultSetBuilder {
    
    ResultSet rs;
    ResultSetMetaData rsmd;
    SQLException exception = null;

    Map names = new HashMap();   // Maps original names to display names
    Map attribs = new HashMap(); // Maps original names to attrib or not

    Namespace ns = Namespace.NO_NAMESPACE;  // default to none
    int maxRows = Integer.MAX_VALUE;        // default to all
    String rootName = "result";
    String rowName = "entry";


    public ResultSetBuilder(ResultSet rs) {
      this.rs = rs;
      try {
        rsmd = rs.getMetaData();
      }
      catch (SQLException e) {
        // Hold the exception until build() is called
        exception = e;
      }
    }

    public Document build() throws JDOMException {
      if (exception != null) {
        throw new JDOMException("Database problem", exception);
      }

      try {
        int colCount = rsmd.getColumnCount();

        Element root = new Element(rootName, ns);
        Document doc = new Document(root);

        int rowCount = 0;
        while (rs.next() && (rowCount++ < maxRows)) {
          Element entry = new Element(rowName, ns);
          for (int col = 1; col <= colCount; col++) {
            String origName = rsmd.getColumnName(col);
            String name = lookupName(origName);
            String value = rs.getString(col);
            if (isAttribute(origName)) {
              entry.addAttribute(name, value);
            }
            else {
              Element child = new Element(name, ns);
              if (!rs.wasNull()) {
                child.setText(value);
              }
              entry.addContent(child);
            }
          }
          root.addContent(entry);
        }

        return doc;
      }
      catch (SQLException e) {
        throw new JDOMException("Database problem", e);
      }
    }

    private String lookupName(String origName) {
      String name = (String) names.get(origName.toLowerCase());
      if (name != null) {
        return name;
      }
      else {
        return origName;
      }
    }

    private boolean isAttribute(String origName) {
      Boolean val = (Boolean) attribs.get(origName.toLowerCase());
      if (val == Boolean.TRUE) {
        return true;
      }
      else {
        return false;
      }
    }

    public void setRootName(String rootName) {
      this.rootName = rootName;
    }

    public void setRowName(String rowName) {
      this.rowName = rowName;
    }

    public void setNamespace(Namespace ns) {
      this.ns = ns;
    }

    public void setMaxRows(int maxRows) {
      this.maxRows = maxRows;
    }

    public void setAsAttribute(String columnName) {
      attribs.put(columnName.toLowerCase(), Boolean.TRUE);
    }

    public void setAsAttribute(String columnName, String attribName) {
      attribs.put(columnName.toLowerCase(), Boolean.TRUE);
      names.put(columnName.toLowerCase(), attribName);
    }

    public void setAsAttribute(int columnNum) {
      try {
        String name = rsmd.getColumnName(columnNum).toLowerCase();
        attribs.put(name, Boolean.TRUE);
      }
      catch (SQLException e) {
        exception = e;
      }
    }

    public void setAsAttribute(int columnNum, String attribName) {
      try {
        String name = rsmd.getColumnName(columnNum).toLowerCase();
        attribs.put(name, Boolean.TRUE);
        names.put(name, attribName);
      }
      catch (SQLException e) {
        exception = e;
      }
    }

    public void setAsElement(String columnName, String elemName) {
      String name = columnName.toLowerCase();
      attribs.put(name, Boolean.FALSE);
      names.put(name, elemName);
    }

    public void setAsElement(int columnNum, String elemName) {
      try {
        String name = rsmd.getColumnName(columnNum).toLowerCase();
        attribs.put(name, Boolean.FALSE);
        names.put(name, elemName);
      }
      catch (SQLException e) {
        exception = e;
      }
    }

/*
    public void setAsIngore(String columnName) {
    }

    public void setAsIngore(int columnNum) {
    }
*/

}
