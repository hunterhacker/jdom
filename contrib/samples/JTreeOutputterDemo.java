/*--

 Copyright (C) 2000 Brett McLaughlin & Jason Hunter. All rights reserved.

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

import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.filechooser.*;

import org.jdom.Document;
import org.jdom.input.SAXBuilder;
import org.jdom.contrib.output.JTreeOutputter;

/**
 * <p><code>JTreeOutputterDemo</code> demonstrates how to
 *   build a JTree (in Swing) from a JDOM <code>{@link Document}</code>.
 * </p>
 *
 * @author Jon Baer
 * @author Brett McLaughlin
 * @version 1.0
 */
public class JTreeOutputterDemo implements ActionListener {

    public JFrame frame;
    public Document doc;
    public DefaultMutableTreeNode root;
    public JTreeOutputter outputter;
    public JTree tree;
    public JScrollPane scrollPane;
    public SAXBuilder saxBuilder;
    public JMenuItem openFile, openURL, openSQL, exitMenu;
    public JButton openButton, reloadButton, exitButton, aboutButton;

    public static void main(String[] args) {
        new JTreeOutputterDemo();
    }

    public JTreeOutputterDemo() {

        frame = new JFrame(" JDOM Viewer 1.0");
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        openFile = new JMenuItem("Open XML File");
        openFile.addActionListener(this);
        openURL = new JMenuItem("Open URL Stream");
        openURL.addActionListener(this);
        openSQL = new JMenuItem("Query Database");
        openSQL.addActionListener(this);
        exitMenu = new JMenuItem("Exit");
        exitMenu.addActionListener(this);
        menu.add(openFile);
        menu.add(openURL);
        menu.add(new JSeparator());
        menu.add(openSQL);
        menu.add(new JSeparator());
        menu.add(exitMenu);
        menuBar.add(menu);
        frame.setJMenuBar(menuBar);

        openButton = new JButton("Open");
        openButton.addActionListener(this);
        reloadButton = new JButton("Reload");
        reloadButton.addActionListener(this);
        exitButton = new JButton("Exit");
        exitButton.addActionListener(this);
        aboutButton = new JButton("About");
        aboutButton.addActionListener(this);
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(openButton);
        buttonPanel.add(reloadButton);
        buttonPanel.add(exitButton);
        buttonPanel.add(aboutButton);

        root = new DefaultMutableTreeNode("JDOM");

        outputter = new JTreeOutputter(true);

        tree = new JTree(root);

        saxBuilder = new SAXBuilder();

        scrollPane = new JScrollPane();
        scrollPane.getViewport().add(tree);

        frame.setSize(400,400);
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add("Center", scrollPane);
        frame.getContentPane().add("South", buttonPanel);

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                System.exit(0);
            }
        });

        frame.setVisible(true);

    }

    public void actionPerformed(ActionEvent e) {
        // Open File
        if (e.getSource() == openButton || e.getSource() == openFile) {
            doFile();
        }
        // Open URL
        if (e.getSource() == openURL) {
            doURL();
        }
        // Query Database
        if (e.getSource() == openSQL) {
            doSQL();
        }
        // Exit
        if (e.getSource() == exitButton || e.getSource() == exitMenu) {
            System.exit(0);
        }
    }

    public void doFile() {
                JFileChooser fc = new JFileChooser();
                fc.setDialogTitle("Select an XML File");
                int returnVal = fc.showDialog(frame, "Load XML");
                if (returnVal == 0) {
                        try {
                                doc = saxBuilder.build(fc.getSelectedFile());
                        } catch (Exception e) {e.printStackTrace();}
                        outputter.output(doc, root);
                }
    }

    public void doURL() {
        URLDialog urlDialog = new URLDialog(frame);
        if (urlDialog.getURL() != null) {
            try {
                doc = saxBuilder.build(new URL(urlDialog.getURL()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            outputter.output(doc, root);
        }
    }

    public void doSQL() {
    }

}

class URLDialog extends JDialog implements ActionListener {

    public String url;
    public JTextField urlField;
    public JButton okButton, cancelButton;

    public URLDialog(Frame frame) {
        super(frame, "Enter A URL", true);
        urlField = new JTextField("http://");
        JPanel buttonPanel = new JPanel();
        okButton = new JButton("OK");
        okButton.addActionListener(this);
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add("North", urlField);
        getContentPane().add("South", buttonPanel);
        setSize(400, 150);
        setVisible(true);
    }

    public String getURL() {
        return this.url;
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == okButton) {
            this.url = urlField.getText(); setVisible(false);
        }
        if (e.getSource() == cancelButton) {
            setVisible(false);
        }
    }

}
