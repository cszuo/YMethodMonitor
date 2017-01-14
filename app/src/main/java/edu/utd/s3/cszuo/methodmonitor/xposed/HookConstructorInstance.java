package edu.utd.s3.cszuo.methodmonitor.xposed;

import java.lang.reflect.Constructor;

import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import edu.utd.s3.cszuo.methodmonitor.utility.Utility;
import edu.utd.s3.cszuo.methodmonitor.log.ErrorLog;

/**
 * Created by cszuo on 1/6/17.
 */

public class HookConstructorInstance extends HookCallback {
    Constructor<?> constructor = null;

    public HookConstructorInstance(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        super(loadPackageParam);
    }

    public void hook(XC_LoadPackage.LoadPackageParam loadPackageParam, String classname, String[] margs) {
        hook(loadPackageParam, classname, margs, 0);
    }

    public void hook(XC_LoadPackage.LoadPackageParam loadPackageParam, String classname, String[] margs, int delay) {
        XposedBridge.log("HookConstructorInstance " + classname);


        Class<?> mcla = Utility.getClass(classname, loadPackageParam.classLoader);
        if (mcla == null) {
            ErrorLog.log(loadPackageParam.packageName, "Class " + classname + " not found");
            return;
        }

        Class<?>[] args = Utility.getClasses(loadPackageParam, margs, loadPackageParam.classLoader);
        if (args == null)
            return;

        try {
            constructor = mcla.getConstructor(args);
            signature = constructor.toString();
        } catch (NoSuchMethodException e) {
            ErrorLog.log(loadPackageParam.packageName, "constructor " + classname + " not found");
            return;
        }

        hook(constructor, delay);
    }

    public void hook(final Constructor<?> constructor, int delay) {

        if (delay == 0) {
            XposedBridge.hookMethod(constructor, this);
        } else {
            Utility.dalayrun(new Runnable() {
                @Override
                public void run() {
                    XposedBridge.hookMethod(constructor, HookConstructorInstance.this);
                }
            }, delay);

        }
    }
}
