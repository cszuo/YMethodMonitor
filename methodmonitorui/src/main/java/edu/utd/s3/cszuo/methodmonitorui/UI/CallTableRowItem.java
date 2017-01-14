package edu.utd.s3.cszuo.methodmonitorui.UI;

import edu.utd.s3.cszuo.methodmonitor.log.HookedMethodCallLog;

import java.util.Vector;

/**
 * Created by cszuo on 1/8/17.
 */

public class CallTableRowItem extends Vector<Object> {
    HookedMethodCallLog hmclog;

    public CallTableRowItem(HookedMethodCallLog hmclog) {
        this.hmclog = hmclog;
    }

    public HookedMethodCallLog getHookedMethodCallLog(){
        return hmclog;
    }

    public Vector<Object> pack(int index) {
        this.clear();
        this.add(index);
        this.add(hmclog.getFrom());
        this.add(hmclog.getSignature());
        this.add(hmclog.getPending());
        return this;
    }
}
