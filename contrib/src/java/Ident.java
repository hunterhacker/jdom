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
 
        "Id",     // Same as $Header: /home/cvspublic/jdom-contrib/src/java/Ident.java,v 1.1 2001/06/16 03:51:50 jhunter Exp $, except that the RCS filename is without 
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
