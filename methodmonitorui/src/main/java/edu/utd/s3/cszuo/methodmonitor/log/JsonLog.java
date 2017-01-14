package edu.utd.s3.cszuo.methodmonitor.log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by cszuo on 1/7/17.
 */

public class JsonLog extends JSONObject {
    static String MARK = "XOXXOOXXXOOO";
    static String KEY_PACKAGENAME = "packagename";
    static String KEY_STACKTRACE = "stacktrace";
    static String KEY_LOGLEVEL = "loglevel"; //0:system 1:INFO 2:ERROR
    static String KEY_MESSAGE = "message";

    protected JsonLog(String source) throws JSONException {
        super(source);
    }

    public JsonLog(LogLevel level, String packagename) {

        myput(this, KEY_PACKAGENAME, packagename);
        myput(this, KEY_LOGLEVEL, level.getLevel());
    }

    public String getPackageName() {
        return myget(this, KEY_PACKAGENAME).toString();
    }

    public int getLevel() {
        return (int) myget(this, KEY_LOGLEVEL);
    }

    public JSONArray getStackTrace() {
        return (JSONArray) myget(this, KEY_STACKTRACE);
    }

    public JsonLog addStackTrace() {
        StackTraceElement[] st = Thread.currentThread().getStackTrace();
        JSONArray ja = new JSONArray();
        int i = 3;
        for (StackTraceElement ste : st) {
            if (--i < 0) {
                ja.put(ste.toString());
            }
        }
        myput(this, KEY_STACKTRACE, ja);
        return this;
    }

    public JsonLog addMessage(Object obj) {
        myput(this, KEY_MESSAGE, obj);
        return this;
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
        System.out.println(String.format("%s%s", MARK, this.toString()));
        //System.out.println(String.format("%s%s", MARK, Base64.encodeToString(this.toString().getBytes(), Base64.NO_WRAP)));
    }
}





