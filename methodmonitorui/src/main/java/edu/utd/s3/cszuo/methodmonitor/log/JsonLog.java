package edu.utd.s3.cszuo.methodmonitor.log;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.utd.s3.cszuo.methodmonitor.log.transport.ITransportLayer;
import edu.utd.s3.cszuo.methodmonitor.log.transport.LogcatTransferLayer;

/**
 * Created by cszuo on 1/7/17.
 */

public class JsonLog extends JSONObject {

    static ITransportLayer jltl = LogcatTransferLayer.getInstance();

    static String KEY_PACKAGENAME = "packagename";
    static String KEY_STACKTRACE = "stacktrace";
    static String KEY_LOGLEVEL = "loglevel"; //0:system 1:INFO 2:ERROR
    static String KEY_MESSAGE = "message";
    static String KEY_THREAD = "thread";

    protected JsonLog(String source) throws JSONException {
        super(source);
    }

    public JsonLog(LogLevel level, String packagename) {

        myput(this, KEY_PACKAGENAME, packagename);
        myput(this, KEY_LOGLEVEL, level.getLevel());
        myput(this, KEY_THREAD, Thread.currentThread().getId() + "(" + Thread.currentThread().getName() + ")");
    }

    public String getPackageName() {
        return myget(this, KEY_PACKAGENAME).toString();
    }

    public int getLevel() {
        return (int) myget(this, KEY_LOGLEVEL);
    }

    public String getThread() {
        Object obj = myget(this, KEY_THREAD);
        if (obj != null)
            return obj.toString();
        else
            return "";
    }

    public JSONArray getStackTrace() {
        return (JSONArray) myget(this, KEY_STACKTRACE);
    }

    public JsonLog addStackTrace() {
        StackTraceElement[] st = Thread.currentThread().getStackTrace();
        JSONArray ja = new JSONArray();
        int i = 3;
        String str;
        for (StackTraceElement ste : st) {
            if (--i < 0) {
                str = ste.getClassName() + ":" + ste.getMethodName();//+ ":" + ste.getLineNumber();
                ja.put(str);
            }
        }
        myput(this, KEY_STACKTRACE, ja);
        return this;
    }

    public JsonLog addMessage(Object obj) {
        myput(this, KEY_MESSAGE, obj);
        return this;
    }

    public Object getMessage() {
        return myget(this, KEY_MESSAGE);
    }


    public void myput(JSONObject js, String key, Object ob) {
        try {
            js.put(key, ob);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Object myget(JSONObject js, String key) {
        try {
            return js.get(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static JsonLog parse(String source) throws JSONException {
        return new JsonLog(source);
    }

    public void print() {
        //System.out.println(String.format("%s%s", MARK, this.toString()));
        //System.out.println(String.format("%s%s", MARK, Base64.encodeToString(this.toString().getBytes(), Base64.NO_WRAP)));
        jltl.send(this);
    }
}





