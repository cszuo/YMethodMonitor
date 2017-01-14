package edu.utd.s3.cszuo.methodmonitor.log;

/**
 * Created by cszuo on 1/7/17.
 */

public class InfoLog extends JsonLog {
    public InfoLog(String packagename) {
        super(LogLevel.INFO, packagename);
    }

    public static void log(String packagename, String msg) {
        new InfoLog(packagename).addMessage(msg).print();
    }
}
