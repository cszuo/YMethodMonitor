package edu.utd.s3.cszuo.methodmonitorui.UI;

import edu.utd.s3.cszuo.methodmonitorui.adb.ILogcatWatcher;
import edu.utd.s3.cszuo.methodmonitor.log.HookedMethodCallLog;
import edu.utd.s3.cszuo.methodmonitor.log.JsonLog;
import edu.utd.s3.cszuo.methodmonitor.log.LogLevel;

import java.awt.BorderLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.RowSorter;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 * Created by cszuo on 1/8/17.
 */

public class CallMonitor extends JPanel implements ILogcatWatcher, IMessageReceiver, ListSelectionListener {
    AppTree apps;
    JTable mytable;
    DefaultTableModel tablemodel;
    DetailedTabbedPane detailtabpane;
    JTextArea logtextArea;

    public CallMonitor() {
        this.setLayout(new BorderLayout(0, 0));
        JPanel mainpane = new JPanel();
        mainpane.setLayout(new BorderLayout(0, 0));


        mytable = initUI_Table();
        mainpane.add(new JScrollPane(mytable), BorderLayout.CENTER);

        detailtabpane = new DetailedTabbedPane();

        final JSplitPane tabArest = new JSplitPane(JSplitPane.VERTICAL_SPLIT, mainpane, detailtabpane);

        apps = new AppTree(this);
        JScrollPane sapps = new JScrollPane(apps, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        final JSplitPane treeArest = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sapps, tabArest);


        logtextArea = new JTextArea();
        logtextArea.setEditable(false);
        JScrollPane slogs = new JScrollPane(logtextArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        final JSplitPane logArest = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                treeArest, slogs);
        this.add(logArest);


        this.addComponentListener(new ComponentAdapter() {
            boolean did = false;

            public void componentResized(ComponentEvent e) {
                if (!did) {
                    logArest.setDividerLocation(0.7);
                    treeArest.setDividerLocation(250);
                    did = true;
                }
            }
        });
    }

    public JTable initUI_Table() {
        JTable mytable = new JTable();
        String[] names = {"ID", "From", "Signature", "Pending"};
        int[] widths = {50, 150, 1300, 150};
        tablemodel = new DefaultTableModel(null, names);
        mytable.setModel(tablemodel);
        mytable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        for (int i = 0; i < widths.length; i++) {
            mytable.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }
        RowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tablemodel);
        mytable.setRowSorter(sorter);

        mytable.getSelectionModel().addListSelectionListener(this);

        return mytable;
    }

    @Override
    public void newLine(String line) {
        if (line.contains("XOXXOOXXXOOO")) {
            line = line.substring(line.indexOf("XOXXOOXXXOOO") + 12);
            try {
                JsonLog jlog = JsonLog.parse(line);
                apps.addLog(jlog);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void clear() {
        logtextArea.setText("");
        tablemodel.getDataVector().removeAllElements();
        tablemodel.fireTableDataChanged();
    }

    @Override
    public void newMsg(JsonLog jlog) {
        logtextArea.append(jlog.toString() + "\n");
        if (jlog.getLevel() == LogLevel.HOOKEDCALL.getLevel()) {
            HookedMethodCallLog hmclog = HookedMethodCallLog.parse(jlog.toString());
            CallTableRowItem ritem = new CallTableRowItem(hmclog);
            ritem.pack(tablemodel.getRowCount());
            tablemodel.addRow(ritem);
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent listSelectionEvent) {
        if (!listSelectionEvent.getValueIsAdjusting()) {
            try {
                CallTableRowItem item = (CallTableRowItem) tablemodel.getDataVector().get(mytable.getSelectedRow());
                detailtabpane.setHookedMethodCallLog(item.getHookedMethodCallLog());
            } catch (ArrayIndexOutOfBoundsException e) {
            }
        }
    }
}
