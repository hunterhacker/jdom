
package org.jdom.contrib.xpath;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

public class Driver
{
    public static String VERSION = "0.9.1";

    public static void main(String[] args)
    {
        System.err.println("werken xpath -- " + VERSION);

        if (args.length != 1)
        {
            System.err.println("usage: Driver <testdata directory>");
            System.exit(1);
        }

        String[] filenames = new String[] { "w3c_examples.txt", "extra_xpaths.txt" };

        int i = 0;

        while (i < filenames.length)
        {
            File testdata = new File(args[0], filenames[i]);

            System.err.println("-------- testfile: " + testdata + " --------");
            try
            {
                FileReader fileReader = new FileReader(testdata);
                BufferedReader reader = new BufferedReader(fileReader);
                
                String testCase = null;
                
                while ( (testCase = reader.readLine()) != null)
                {
                    if ( (testCase.length() < 1)
                         ||
                         (testCase.charAt(0) == '#') )
                    {
                        continue;
                    }
                    System.err.println(" xpath: " + testCase);
                    XPath xpath = new XPath(testCase);
                    xpath.compile();
                }
            }
            catch (IOException ioe)
            {
                ioe.printStackTrace();
            }
            ++i;
        }
    }
}
