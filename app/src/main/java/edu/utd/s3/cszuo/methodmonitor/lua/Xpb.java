package edu.utd.s3.cszuo.methodmonitor.lua;

import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import edu.utd.s3.cszuo.methodmonitor.xposed.HookConstructorInstance;
import edu.utd.s3.cszuo.methodmonitor.xposed.HookMethodInstance;

/**
 * Created by cszuo on 1/6/17.
 */


public class Xpb {
    XC_LoadPackage.LoadPackageParam loadPackageParam;

    public Xpb(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        this.loadPackageParam = loadPackageParam;
    }

    public HookMethodInstance newHookMethodInstance() {
        return new HookMethodInstance(loadPackageParam);
    }

    public HookConstructorInstance newHookConstructorInstance() {
        return new HookConstructorInstance(loadPackageParam);
    }

    public String getClassName(Object obj) {
        return obj.getClass().getName().toString();
    }

    public void print(Object x) {
        XposedBridge.log("let's print " + x);
    }

}
