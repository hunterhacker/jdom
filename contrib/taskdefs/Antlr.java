
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import java.io.File;
import java.io.InputStream;
import java.io.BufferedInputStream;

import java.util.Arrays;

public class Antlr extends Task
{

    private File _grammar = null;

    public Antlr()
    {

    }

    public void setGrammar(String grammar)
    {
        _grammar = new File(grammar);
    }

    public void execute()
        throws BuildException
    {

        try
        {
            File outputDir = _grammar.getParentFile();

            String command = "java antlr.Tool " + _grammar.getName();
            
            String myos = System.getProperty("os.name");

            if (myos.toLowerCase().indexOf("windows") >= 0) {
                if (!outputDir.equals(project.resolveFile(".")))
                    command = "cmd /c cd " + outputDir + " && " + command;
            } else {

                String ant = project.getProperty("ant.home");
                if (ant == null)
                {
                    throw new BuildException("Property 'ant.home' not found");
                }

                String antRun = project.resolveFile(ant + "/bin/antRun").toString();
                
                command = antRun + " " + outputDir + " " + command;
            }

            Process pid = Runtime.getRuntime().exec(command);
            pid.waitFor();

            if (pid.exitValue() != 0)
            {
                throw new BuildException("antlr failed: " + pid.exitValue());
            }
        }
        catch (Exception e)
        {
            throw new BuildException(e);
        }
            
    }

}

