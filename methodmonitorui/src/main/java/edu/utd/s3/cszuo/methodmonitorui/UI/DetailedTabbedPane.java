package edu.utd.s3.cszuo.methodmonitorui.UI;

import edu.utd.s3.cszuo.methodmonitor.log.HookedMethodCallLog;

import org.json.JSONArray;

import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;

/**
 * Created by cszuo on 1/8/17.
 */

public class DetailedTabbedPane extends JTabbedPane {
    HookedMethodCallLog hmclog;

    public void setHookedMethodCallLog(HookedMethodCallLog hmclog) {
        this.hmclog = hmclog;
        refreshUI();
        this.setTabPlacement(JTabbedPane.TOP);
    }

    private void refreshUI() {
        this.removeAll();

        JSONArray st = hmclog.getStackTrace();
        Object[][] sts = new Object[st.length()][2];
        String[] header = {"ID", "Method"};
        int[] stwidths = {50, 1000};
        for (int i = 0; i < sts.length; i++) {
            sts[i][0] = i + 1;
            sts[i][1] = st.get(i);
        }

        this.add("StackTrace", new JScrollPane(getTable(sts, header, stwidths)));

        Object[][] param = hmclog.getMethodHookParam();
        String[] names = {"ID", "Class", "Value"};
        int[] widths = {50, 300, 300};
        this.add("Parameters", new JScrollPane(getTable(param, names, widths)));


        JTextArea pending = new JTextArea();
        pending.setText(hmclog.getPending());
        this.add("Pending", new JScrollPane(pending));
    }

    public JTable getTable(Object[][] param, String[] header, int[] widths) {
        JTable mytable = new JTable();
        DefaultTableModel tablemodel = new DefaultTableModel(param, header);
        mytable.setModel(tablemodel);
        mytable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        for (int i = 0; i < widths.length; i++) {
            mytable.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }
        return mytable;
    }
}
