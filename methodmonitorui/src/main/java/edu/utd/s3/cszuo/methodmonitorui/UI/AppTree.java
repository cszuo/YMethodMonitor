package edu.utd.s3.cszuo.methodmonitorui.UI;


import edu.utd.s3.cszuo.methodmonitor.log.JsonLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 * Created by cszuo on 1/8/17.
 */

public class AppTree extends JTree implements TreeSelectionListener {

    DefaultTreeModel dt;
    DefaultMutableTreeNode top;

    List<JsonLog> alllogs = new ArrayList<JsonLog>();
    HashMap<String, List<JsonLog>> logs = new HashMap<String, List<JsonLog>>();
    HashMap<String, DefaultMutableTreeNode> apps = new HashMap<String, DefaultMutableTreeNode>();

    IMessageReceiver mreceiver;

    String targetApp = null;

    public AppTree(IMessageReceiver mreceiver) {
        this.mreceiver = mreceiver;
        top = new DefaultMutableTreeNode("apps");
        dt = new DefaultTreeModel(top);
        this.setModel(dt);

        this.addTreeSelectionListener(this);
    }

    private void newApp(String pkgname) {
        DefaultMutableTreeNode app = new DefaultMutableTreeNode(pkgname);
        top.add(app);
        apps.put(pkgname, app);
    }

    private void refresh(String pkgname) {
        DefaultMutableTreeNode app = apps.get(pkgname);
        app.setUserObject(String.format("%s (%s)", pkgname, logs.get(pkgname).size()));
        dt.reload();
    }

    public void addLog(JsonLog jlog) {
        synchronized (alllogs) {
            alllogs.add(jlog);
        }
        String pkgname = jlog.getPackageName();
        if (!logs.containsKey(pkgname)) {
            logs.put(pkgname, new ArrayList<JsonLog>());
            newApp(pkgname);
        }
        synchronized (logs.get(pkgname)) {
            logs.get(pkgname).add(jlog);
        }
        refresh(pkgname);
        if (targetApp == null || targetApp.equals(pkgname))
            mreceiver.newMsg(jlog);
    }

    @Override
    public void valueChanged(TreeSelectionEvent treeSelectionEvent) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) this.getLastSelectedPathComponent();
        if (node == top) {
            targetApp = null;
        } else {
            for (String pkgname : apps.keySet()) {
                if (apps.get(pkgname) == node) {
                    targetApp = pkgname;
                    break;
                }
            }
        }
        List<JsonLog> fsend = null;
        mreceiver.clear();
        if (targetApp == null) {
            synchronized (alllogs) {
                for (JsonLog jlog : alllogs) {
                    mreceiver.newMsg(jlog);
                }
            }
        } else {
            synchronized (logs.get(targetApp)) {
                for (JsonLog jlog : logs.get(targetApp)) {
                    mreceiver.newMsg(jlog);
                }
            }
        }

    }
}
