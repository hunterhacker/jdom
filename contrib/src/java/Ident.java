/*--

 $Id: Ident.java,v 1.2 2004/02/06 09:57:48 jhunter Exp $

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

 import java.util.zip.ZipFile;
import java.util.zip.ZipEntry;

import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;

import java.util.HashMap;
import java.util.Enumeration;

/**
 * <p>
 * The <code>Ident</code> class is a class which works like the 
 * ident(1) program used to display RCS keys stored in binary files,
 * but with the added facility of being able to look inside .zip/.jar 
 * files and check each one on the file contain within.
 * </p>
 *
 * <p>
 * Known issues: The class can get confused if a $ appears in the class
 * file where it's not a delimiter.
 *
 * @author Jools Enticknap
 */
public final class Ident {
    
    /** Starting character for an RCS string */
    private final static int KDELIM = '$';

    /** Delimiter withing the tags */
    private final static int VDELIM = ':';
    
    /** All the known RCS tags */
    private final static String[] rcsTags = {
        "Author", // The login name of the user who checked in the revision.

        "Date",   // The date and time the revision was checked in.

        "Header", // A standard header containing the full pathname of the RCS
                  // file, the revision number, the date and time, the author,
                  // the state, and the locker (if locked).
 
        "Id",     // Same as $Header: /home/cvspublic/jdom-contrib/src/java/Ident.java,v 1.2 2004/02/06 09:57:48 jhunter Exp $, except that the RCS filename is without 
                  // a path.
 
        "Locker", // The login name of the user who locked the revision (empty
                  // if not locked).
 
        "Log",    // The log message supplied during checkin.  For ident's  
                  // purposes, this is equivalent to $RCSfile: Ident.java,v $.
 
        "Name",   // The symbolic name used to check out the revision, if any.
        
        "RCSfile",// The name of the RCS file without a path.
 
        "Revision",// The revision number assigned to the revision.
 
        "Source", // The full pathname of the RCS file.
 
        "State"   // The state assigned to the revision with the -s option 
                  // of rcs(1) or ci(1).
    };
    
    /** A Map of all the tags for easy lookup */
    private final static HashMap tagMap;
    
    public static void main(String args[]) throws IOException {
        if (args.length > 0 ) {
            for (int i = 0; i < args.length; i++) {
                System.out.println(args[i]+":");
                if (isZip(args[i])) {
                    ZipFile zipFile = new ZipFile(args[i]);
                    Enumeration entries = zipFile.entries();
                    while (entries.hasMoreElements()) {
                        ZipEntry ze = (ZipEntry)entries.nextElement();
                        System.out.println("   ->"+ze.getName()+"<-");
                        scan(zipFile.getInputStream(ze));
                    }
                } else {
                    FileInputStream fis = new FileInputStream(args[i]);
                    scan(fis);
                }
            }
        } else {
            printUsage();
        }
    }

    private static void printUsage() {
        System.out.println(
          "This program prints RCS information about a class file.");
        System.out.println("Usage: java Ident [classfile.class | jarfile.jar]");
    }

    private static boolean isZip(String fileName) {
        return fileName.endsWith(".jar") ||
               fileName.endsWith(".zip");
    }

    private static void scan(InputStream is) throws IOException {
        int i = 0;
        StringBuffer sb = new StringBuffer();

        boolean inTag    = false;
        boolean validTag = false;
        
        while ((i = is.read()) != -1) {
            
            // We have found a starting tag.
            if (i == KDELIM) {
                // We are at the end of the 
                if (inTag) {
                    if (validTag) {
                        System.out.println("     $"+sb.toString()+"$");
                    }
                    validTag = inTag = false;
                    sb = new StringBuffer();
                } else {
                    inTag = true;
                    continue;
                }
            } else if (i == VDELIM) {
                if (inTag && !validTag) {
                    String tag = sb.toString();
                    if (tagMap.get(tag) == null) {
                        // Failed to match a tag, start again.
                        inTag = false;
                        validTag = false;
                        sb = new StringBuffer();
                    } else {
                        validTag = true;
                    }
                }
            } 
            
            if (inTag) {
                sb.append((char)i);
            }
        }
    }

    static {
        tagMap = new HashMap();
        for (int i = 0; i < rcsTags.length; i++) {
            tagMap.put(rcsTags[i],rcsTags[i]); 
        }
    }
}
