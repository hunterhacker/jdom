
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
        File grammarDir = this.grammar.getParentFile();
        String grammar = this.grammar.getName();

        setDir(grammarDir.toString());
        setArgs(grammar);

        String userDir = System.getProperty("user.dir");

        setJvmargs("-Duser.dir=" + userDir);

        String classpath = System.getProperty("java.class.path");
        setClasspath(new Path(classpath));

        super.execute();
    }

}

