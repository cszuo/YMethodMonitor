package edu.utd.s3.cszuo.methodmonitor.log;

/**
 * Created by cszuo on 1/6/17.
 */

public class ErrorLog extends JsonLog {
    public ErrorLog(String packagename) {
        super(LogLevel.ERROR, packagename);
    }

    public static void log(String packagename, String msg) {
        new ErrorLog(packagename).addMessage(msg).print();
    }

    public static void log(String packagename, Exception e) {
        log(packagename, e.toString());
    }
}
