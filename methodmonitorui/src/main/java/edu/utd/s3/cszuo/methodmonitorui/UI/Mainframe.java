package edu.utd.s3.cszuo.methodmonitorui.UI;

import edu.utd.s3.cszuo.methodmonitor.log.transport.ITransportLayer;
import edu.utd.s3.cszuo.methodmonitor.log.transport.LogcatTransferLayer;
import edu.utd.s3.cszuo.methodmonitorui.UI.luaeditor.LuaEditor;
import edu.utd.s3.cszuo.methodmonitorui.adb.DevicesDetector;
import edu.utd.s3.cszuo.methodmonitorui.adb.IDevicesWatcher;
import edu.utd.s3.cszuo.methodmonitorui.adb.LogCatReader;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

/**
 * Created by cszuo on 1/8/17.
 */

public class Mainframe extends JFrame implements IDevicesWatcher {
    JPanel contentPane;
    JTabbedPane tabpane;

    RawLogCatPane rcatpane;
    CallMonitor cmonitorpane;
    LuaEditor luaspane;

    JMenu cdevices = new JMenu("Connected Devices");
    LogCatReader lreader = null;

    ITransportLayer transportlayer;

    public Mainframe() {
        initUI();
        transportlayer = LogcatTransferLayer.getInstance();
        transportlayer.setDataReceiver(cmonitorpane);
    }

    public void initUI() {
        setTitle("MethodMonitor");// 设置窗体标题
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// 默认关闭方式
        Dimension displaySize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize((int) displaySize.getWidth(), (int) displaySize.getHeight());// 设置窗体大小
        contentPane = new JPanel();// 创建内容面板
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);// 设置内容面板
        tabpane = new JTabbedPane();
        contentPane.add(tabpane, BorderLayout.CENTER);// 添加标签控件到窗体

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Devices");
        JMenuItem menuItem = new JMenuItem("Refresh Devices");

        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cdevices.removeAll();
                DevicesDetector dd = new DevicesDetector();
                dd.regDevicesWatcher(Mainframe.this);
                dd.start();
            }
        });
        menu.add(menuItem);
        menu.add(cdevices);
        menuBar.add(menu);
        this.setJMenuBar(menuBar);

        tabpane.addTab("Call Monitor", (cmonitorpane = new CallMonitor()));
        tabpane.addTab("Raw Logcat", (rcatpane = new RawLogCatPane()));
        tabpane.addTab("Lua Script", (luaspane = new LuaEditor()));


    }

    @Override
    public void newDevices(final String dname) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JMenuItem menuItem = new JMenuItem(dname);
                menuItem.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        lreader = new LogCatReader(dname);
                        //lreader.regLogcatWatcher(Mainframe.this);
                        lreader.regLogcatWatcher(LogcatTransferLayer.getInstance());
                        lreader.regLogcatWatcher(rcatpane);
                        lreader.start();
                    }
                });
                cdevices.add(menuItem);
            }
        });
    }
}
