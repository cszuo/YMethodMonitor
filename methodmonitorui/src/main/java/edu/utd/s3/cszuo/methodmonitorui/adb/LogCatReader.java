package edu.utd.s3.cszuo.methodmonitorui.adb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by cszuo on 1/8/17.
 */

public class LogCatReader extends Thread {

    ArrayList<ILogcatWatcher> lws = new ArrayList<ILogcatWatcher>();

    public void regLogcatWatcher(ILogcatWatcher lw) {
        lws.add(lw);
    }

    private void notifyNewLine(String line) {
        for (ILogcatWatcher lw : lws) {
            lw.newLine(line);
        }
    }

    String dname = null;

    public LogCatReader(String dname) {
        this.dname = dname;
    }

    public void run() {
        Process p = null;
        InputStream istream = null;
        InputStreamReader isreader = null;
        BufferedReader breader = null;
        try {
            p = Runtime.getRuntime().exec(new String[]{DevicesDetector.adb, "-s", dname, "logcat"});
            istream = p.getInputStream();
            isreader = new InputStreamReader(istream);
            breader = new BufferedReader(isreader);

            String line = null;
            while ((line = breader.readLine()) != null) {
                notifyNewLine(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                breader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                isreader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                istream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                p.destroy();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
