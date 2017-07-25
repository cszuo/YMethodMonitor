package edu.utd.s3.cszuo.methodmonitor.lua;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.luaj.vm2.lib.jse.JsePlatform;

import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import edu.utd.s3.cszuo.methodmonitor.log.ErrorLog;
import edu.utd.s3.cszuo.methodmonitor.log.InfoLog;

/**
 * Created by cszuo on 1/6/17.
 */

public class LuaLoader {
    XC_LoadPackage.LoadPackageParam loadPackageParam;

    Globals globals = null;
    static String FUNCTION_ISTARGET = "isTarget"; // boolean isTarget(String packagename)
    static String FUNCTION_LOADPAKG = "handleLoadPackage"; // void handleLoadPackage(LoadPackageParam loadPackageParam)

    public LuaLoader(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        this.loadPackageParam = loadPackageParam;
        globals = JsePlatform.standardGlobals();
    }

    public void set(String key, LuaValue value) {
        globals.set(key, value);
    }

    public void loadAndRun(String sname, String script) {
        LuaValue chunk = null;
cd
        try{
            chunk = globals.load(script, sname);
        }catch(Exception e) {
            String log = "Script: " + sname + " load error: " + e;
            ErrorLog.log(loadPackageParam.packageName, log);
            XposedBridge.log(loadPackageParam.packageName + ": " + log);
            return;
        }

        chunk.call();

        LuaValue f_isTarget = globals.get(FUNCTION_ISTARGET);
        LuaValue f_handleLoadPackage = globals.get(FUNCTION_LOADPAKG);

        if (f_handleLoadPackage == LuaValue.NIL || !f_handleLoadPackage.checkclosure().p.source.toString().equals(sname)) {
            InfoLog.log(loadPackageParam.packageName, "Not found " + FUNCTION_LOADPAKG + ", exit.");
            return;
        }
        if (f_isTarget == LuaValue.NIL || !f_isTarget.checkclosure().p.source.toString().equals(sname)) {
            InfoLog.log(loadPackageParam.packageName, "Not found " + FUNCTION_ISTARGET + ", will hook all package.");
            f_handleLoadPackage.call(CoerceJavaToLua.coerce(loadPackageParam));
            InfoLog.log(loadPackageParam.packageName, "Successfully hook " + loadPackageParam.packageName);
        } else {
            LuaValue ist = f_isTarget.call(LuaValue.valueOf(loadPackageParam.packageName));
            if (ist.isboolean() && ist.checkboolean()) {
                InfoLog.log(loadPackageParam.packageName, "It is my target: " + loadPackageParam.packageName);
                f_handleLoadPackage.call(CoerceJavaToLua.coerce(loadPackageParam));
                InfoLog.log(loadPackageParam.packageName, "Successfully hook " + loadPackageParam.packageName);
            } else {
                InfoLog.log(loadPackageParam.packageName, "It is not target: " + loadPackageParam.packageName);
            }
        }


        //XposedBridge.log("MyAdd:" + globals.get("b") + globals.get("b").checkclosure().p.source);
        //XposedBridge.log("MyAdd:" + (globals.get("vb") == LuaValue.NIL));
        //XposedBridge.log("MyAdd:" + (globals.get("b") == LuaValue.NIL));
        //MyAdd.call(LuaValue.valueOf("cccccccccccc"));
    }

    public void print() {
        XposedBridge.log("LuaLoader print");
    }
}
