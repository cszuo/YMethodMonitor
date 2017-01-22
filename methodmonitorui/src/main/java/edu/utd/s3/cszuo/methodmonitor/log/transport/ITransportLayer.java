package edu.utd.s3.cszuo.methodmonitor.log.transport;

import edu.utd.s3.cszuo.methodmonitor.log.JsonLog;

/**
 * Created by cszuo on 1/15/17.
 */

public interface ITransportLayer {
    public void send(JsonLog jlog);
    public void setDataReceiver(IDatareceiver dr);
}
