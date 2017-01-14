package edu.utd.s3.cszuo.methodmonitorui.UI.luaeditor;


import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by cszuo on 1/12/17.
 */

public class LuaEditor extends JPanel implements ILuaWriterWatcher, ILuaReaderWatchcer, TreeSelectionListener, DocumentListener {
    public final static String LUAPATH = "/data/data/edu.utd.s3.cszuo.methodmonitor/lua";
    JTree luas;
    DefaultTreeModel dt;
    DefaultMutableTreeNode top;

    HashMap<DefaultMutableTreeNode, String> luass = new HashMap<DefaultMutableTreeNode, String>();

    JTextArea logtextArea;
    RSyntaxTextArea codeArea;

    DocumentBuilderFactory db = DocumentBuilderFactory.newInstance();

    DefaultMutableTreeNode targetS = null;

    public LuaEditor() {
        this.setLayout(new BorderLayout(0, 0));
        JToolBar tooBar = initMenuBar();

        codeArea = new RSyntaxTextArea(20, 60);
        codeArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_LUA);
        codeArea.setCodeFoldingEnabled(true);

        RTextScrollPane sp = new RTextScrollPane(codeArea);

        luas = new JTree();
        top = new DefaultMutableTreeNode("Scripts");
        dt = new DefaultTreeModel(top);

        luas.setModel(dt);
        luas.addTreeSelectionListener(this);
        final JSplitPane codeArest = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(luas), sp);

        logtextArea = new JTextArea();
        logtextArea.setEditable(false);
        final JSplitPane logArest = new JSplitPane(JSplitPane.VERTICAL_SPLIT, codeArest, new JScrollPane(logtextArea));

        this.add(tooBar, BorderLayout.NORTH);
        this.add(logArest, BorderLayout.CENTER);

        this.addComponentListener(new ComponentAdapter() {
            boolean did = false;

            public void componentResized(ComponentEvent e) {
                if (!did) {
                    logArest.setDividerLocation(0.7);
                    codeArest.setDividerLocation(250);
                    did = true;
                }
            }
        });
    }

    public void loadLuas() {
        luass.clear();
        top.removeAllChildren();
        dt.reload();
        targetS = null;
        new LuaScriptReader(LuaEditor.this).start();
    }

    public JToolBar initMenuBar() {

        JToolBar toolBar = new JToolBar("Still draggable");
        toolBar.setFloatable(false);
        toolBar.addSeparator();

        JButton bread = new JButton("Load All Scripts");
        bread.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadLuas();
            }
        });
        toolBar.add(bread);
        toolBar.addSeparator();

        JButton bwrite = new JButton("Write All Scripts");
        bwrite.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new LuaScriptWriter(LuaEditor.this, luass).start();
            }
        });

        toolBar.add(bwrite);
        toolBar.addSeparator();
        return toolBar;
    }

    SimpleDateFormat dateformat = new SimpleDateFormat("HH:mm:ss ");
    @Override
    public void info(String str) {
        str = String.format("[%s] info: %s\n", dateformat.format(new Date()), str);

        logtextArea.append(str);
    }

    @Override
    public void afterWriteLuas() {
        loadLuas();
    }

    @Override
    public void newLuas(String name, String content) {
        DefaultMutableTreeNode script = new DefaultMutableTreeNode(name);
        luass.put(script, content);
        top.add(script);
        dt.reload();
    }

    @Override
    public void valueChanged(TreeSelectionEvent treeSelectionEvent) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) luas.getLastSelectedPathComponent();
        if (node == top) {

        } else {
            if (luass.containsKey(node)) {
                targetS = node;
                codeArea.getDocument().removeDocumentListener(this);
                codeArea.setText(luass.get(node));
                codeArea.getDocument().addDocumentListener(this);
            }
        }
    }

    @Override
    public void insertUpdate(DocumentEvent documentEvent) {

    }

    @Override
    public void removeUpdate(DocumentEvent documentEvent) {

    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        if (targetS != null) {
            luass.put(targetS, codeArea.getText());
        }
        System.out.println("changedUpdate! " + e.getType());
    }
}
