package edu.utd.s3.cszuo.methodmonitorui.UI;

import edu.utd.s3.cszuo.methodmonitor.log.JsonLog;

/**
 * Created by cszuo on 1/8/17.
 */

public interface IMessageReceiver {
    public void clear();
    public void newMsg(JsonLog jlog);
}
