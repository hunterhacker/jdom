
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Java;
import org.apache.tools.ant.Path;

import java.io.File;

public class Antlr extends Java
{
    private final static String ANTLR_TOOL = "antlr.Tool";

    private File grammar = null;

    public Antlr()
    {
        setClassname(ANTLR_TOOL);
        setFork("true");
    }

    public void setGrammar(String grammar)
    {
        this.grammar = new File(grammar);
    }

    public void execute()
        throws BuildException
    {
        String grammarDir = getParentFor(this.grammar);
        String grammar = this.grammar.getName();

        setDir(grammarDir);
        setArgs(grammar);

        String userDir = System.getProperty("user.dir");

        setJvmargs("-Duser.dir=" + userDir);

        String classpath = System.getProperty("java.class.path");
        setClasspath(new Path(classpath));

        super.execute();
    }

    // A reimplemention of File.getParent() that works on Windows even 
    // though we're using a path separator is "/".  
    // The JDK 1.2 method File.getParentFile() works great (see the CVS
    // archives) but we reimplement this way to add support for JDK 1.1.
    // (jhunter)
    private String getParentFor(File f) {
        String path = f.getPath();
        path = path.replace('\\', '/');
        int index = path.lastIndexOf("/");
        if (index < 0) {
            return null;
        }
        if (!f.isAbsolute() || (path.indexOf("/") != index)) {
            return path.substring(0, index);
        }
        if (index < path.length() - 1) {
            return path.substring(0, index + 1);
        }
        return null;
    }

}

