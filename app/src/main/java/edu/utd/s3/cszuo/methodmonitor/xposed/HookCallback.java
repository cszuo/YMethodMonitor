package edu.utd.s3.cszuo.methodmonitor.xposed;

import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import edu.utd.s3.cszuo.methodmonitor.log.ErrorLog;
import edu.utd.s3.cszuo.methodmonitor.log.HookedMethodCallLog;
import edu.utd.s3.cszuo.methodmonitor.log.InfoLog;

/**
 * Created by cszuo on 1/7/17.
 */

public class HookCallback extends XC_MethodHook {

    LuaFunction lua_beforehook = null; // void beforehook(MethodHookParam param, StringBuilder pending)
    LuaFunction lua_afterhook = null; // void afterhook(MethodHookParam param, StringBuilder pending)
    XC_LoadPackage.LoadPackageParam loadPackageParam;
    String signature = "";

    public HookCallback(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        this.loadPackageParam = loadPackageParam;
    }


    public void setLuaBeforehook(LuaValue x) {
        lua_beforehook = x.checkfunction();
    }

    public void setLuaAfterhook(LuaValue x) {
        lua_afterhook = x.checkfunction();
    }

    @Override
    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
        if (lua_beforehook != null) {
            try {
                StringBuilder pending = new StringBuilder();
                lua_beforehook.call(CoerceJavaToLua.coerce(param), CoerceJavaToLua.coerce(pending));
                HookedMethodCallLog.log(loadPackageParam.packageName, "before", signature, param, pending);
            } catch (Exception e) {
                ErrorLog.log(loadPackageParam.packageName, e);
            }
        }
        super.beforeHookedMethod(param);
    }

    @Override
    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
        StringBuilder pending = new StringBuilder();
        if (lua_afterhook != null) {
            try {
                lua_afterhook.call(CoerceJavaToLua.coerce(param), CoerceJavaToLua.coerce(pending));
            } catch (Exception e) {
                ErrorLog.log(loadPackageParam.packageName, e);
            }
        }
        HookedMethodCallLog.log(loadPackageParam.packageName, "after", signature, param, pending);
        super.afterHookedMethod(param);
    }

    protected void hookMethod(List member, XC_MethodHook callback) {
        Member m;
        for (Object obj : member) {
            m = (Member) obj;
            InfoLog.log(loadPackageParam.packageName, "Hooked " + m.toString());
            XposedBridge.hookMethod(m, callback);
        }
    }
}
