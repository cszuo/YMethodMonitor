package edu.utd.s3.cszuo.methodmonitorui;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import edu.utd.s3.cszuo.methodmonitorui.UI.Mainframe;

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


        Class arg17 = ClassUtils.forName("[C", ClassLoader.getSystemClassLoader());
        Object obj = arg17;
        System.out.println(obj);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Mainframe mf = new Mainframe();
                mf.setVisible(true);
            }
        });
    }
}
