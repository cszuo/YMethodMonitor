package edu.utd.s3.cszuo.methodmonitor.log;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.robv.android.xposed.XC_MethodHook;
import edu.utd.s3.cszuo.methodmonitor.log.transport.Base64;

/**
 * Created by cszuo on 1/7/17.
 */

public class HookedMethodCallLog extends JsonLog {
    static String KEY_FROM = "from";
    static String KEY_SIGNATURE = "signature";
    static String KEY_PARAM = "param";
    static String KEY_CLASS = "pclass";
    static String KEY_VALUE = "pvalue";
    static String KEY_PENDING = "pending";

    protected HookedMethodCallLog(String source) throws JSONException {
        super(source);
    }

    public HookedMethodCallLog(String packagename, String from, String signature, XC_MethodHook.MethodHookParam param, StringBuilder pending) {
        super(LogLevel.HOOKEDCALL, packagename);
        this.myput(this, KEY_FROM, from);
        this.myput(this, KEY_SIGNATURE, signature);
        this.addStackTrace();
        this.addMethodHookParam(param);
        this.addPending(pending);
    }

    private void addMethodHookParam(XC_MethodHook.MethodHookParam param) {
        JSONArray ja = new JSONArray();
        JSONObject jso;
        Object tmp;

        tmp = param.thisObject;
        jso = new JSONObject();
        this.myput(jso, KEY_CLASS, tmp == null ? "null" : tmp.getClass());
        this.myput(jso, KEY_VALUE, tmp == null ? "null" : trannsfer(tmp));
        ja.put(jso);

        tmp = param.getResult();
        jso = new JSONObject();
        this.myput(jso, KEY_CLASS, tmp == null ? "null" : tmp.getClass());
        this.myput(jso, KEY_VALUE, tmp == null ? "null" : trannsfer(tmp));
        ja.put(jso);


        for (Object obj : param.args) {
            jso = new JSONObject();
            this.myput(jso, KEY_CLASS, obj == null ? "null" : obj.getClass());
            this.myput(jso, KEY_VALUE, obj == null ? "null" : trannsfer(obj));
            ja.put(jso);
        }

        this.myput(this, KEY_PARAM, ja);
    }

    public Object trannsfer(Object obj) {
        if (obj instanceof char[])
            return new String((char[]) obj);
        if (obj instanceof byte[])
            return Base64.encodeToString((byte[])obj,Base64.NO_WRAP);
        return obj;
    }

    private void addPending(StringBuilder pending) {
        this.myput(this, KEY_PENDING, pending.toString());
    }

    public String getSignature() {
        return myget(this, KEY_SIGNATURE).toString();
    }

    public String getFrom() {
        return myget(this, KEY_FROM).toString();
    }

    public String getPending() {
        return myget(this, KEY_PENDING).toString();
    }

    public Object[][] getMethodHookParam() {
        JSONArray ja = (JSONArray) myget(this, KEY_PARAM);
        JSONObject jso = null;
        Object[][] obss = new Object[ja.length()][3];
        for (int i = 0; i < ja.length(); i++) {
            try {
                jso = ja.getJSONObject(i);
                obss[i][0] = i - 2;
                obss[i][1] = myget(jso, KEY_CLASS).toString();
                obss[i][2] = myget(jso, KEY_VALUE).toString();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        obss[0][0] = "this";
        obss[1][0] = "return";
        return obss;
    }

    public static HookedMethodCallLog parse(String source) throws JSONException {
        return new HookedMethodCallLog(source);
    }


    public static void log(String packagename, String from, String signature, XC_MethodHook.MethodHookParam param, StringBuilder pending) {
        new HookedMethodCallLog(packagename, from, signature, param, pending).print();
    }
}
