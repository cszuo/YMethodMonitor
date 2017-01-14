package edu.utd.s3.cszuo.methodmonitorui.adb;

import java.util.ArrayList;

/**
 * Created by cszuo on 1/7/17.
 */

public class DevicesDetector extends Thread {
    public static String adb = "/home/cszuo/Android/Sdk/platform-tools/adb";
    //public static String adb = "adb";

    ArrayList<IDevicesWatcher> dws = new ArrayList<IDevicesWatcher>();

    public void regDevicesWatcher(IDevicesWatcher dw) {
        dws.add(dw);
    }

    private void notifyNewDevices(String dname) {
        for (IDevicesWatcher dw : dws) {
            dw.newDevices(dname);
        }
    }

    public void run() {
        ArrayList<String> res = ShellExecutor.exec(new String[]{DevicesDetector.adb, "devices"});
        for (String str : res) {
            str = str.trim();
            if (str.endsWith("device")) {
                str = str.substring(0, str.length() - 7).trim();
                System.out.println("Found device: " + str);
                notifyNewDevices(str);
            }
        }
        System.out.println("e");
    }
}
