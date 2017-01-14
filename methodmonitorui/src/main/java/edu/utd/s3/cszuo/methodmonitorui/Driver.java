package edu.utd.s3.cszuo.methodmonitorui;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import edu.utd.s3.cszuo.methodmonitorui.UI.Mainframe;
import edu.utd.s3.cszuo.methodmonitorui.UI.luaeditor.LuaScriptReader;

public class Driver {
    public static void main(String[] args) throws Exception {

        //UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");

        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, you can set the GUI to another look and feel.
        }

        //System.out.println("a");
        //new DevicesDetector().start();
        //new LogCatReader().start();
        //new LuaScriptReader(null).start();


        char[] a = new char[2];
        Object obj = a;
        System.out.println(obj.getClass());

        Mainframe mf = new Mainframe();
        mf.setVisible(true);
    }
}
