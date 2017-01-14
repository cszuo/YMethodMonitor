package edu.utd.s3.cszuo.methodmonitor.log;

/**
 * Created by cszuo on 1/7/17.
 */

public enum LogLevel {
    SYS(0), INFO(1), ERROR(2), HOOKEDCALL(3);
    private int level;

    private LogLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return this.level;
    }

}
