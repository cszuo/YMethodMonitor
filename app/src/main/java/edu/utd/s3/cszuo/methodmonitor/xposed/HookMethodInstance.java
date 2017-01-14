package edu.utd.s3.cszuo.methodmonitor.xposed;

import java.lang.reflect.Method;

import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import edu.utd.s3.cszuo.methodmonitor.log.ErrorLog;
import edu.utd.s3.cszuo.methodmonitor.log.InfoLog;
import edu.utd.s3.cszuo.methodmonitor.utility.Utility;

/**
 * Created by cszuo on 1/6/17.
 */

public class HookMethodInstance extends HookCallback {
    Method method = null;

    public HookMethodInstance(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        super(loadPackageParam);
    }

    public void hook(XC_LoadPackage.LoadPackageParam loadPackageParam, String classname, String methodname, String[] margs) {
        hook(loadPackageParam, classname, methodname, margs, 0);
    }

    public void hook(XC_LoadPackage.LoadPackageParam loadPackageParam, String classname, String methodname, String[] margs, int delay) {
        XposedBridge.log("HookMethodInstance " + classname + methodname);


        Class<?> mcla = Utility.getClass(classname, loadPackageParam.classLoader);
        if (mcla == null) {
            ErrorLog.log(loadPackageParam.packageName, "Class " + classname + " not found");
            return;
        }

        Class<?>[] args = Utility.getClasses(loadPackageParam, margs, loadPackageParam.classLoader);
        if (args == null)
            return;

        try {
            //Method[] ms = mcla.getMethods();
            //for (Method m : ms)
            //    InfoLog.log(loadPackageParam.packageName, m.toString());
            method = mcla.getMethod(methodname, args);
            signature = method.toString();
        } catch (NoSuchMethodException e) {
            ErrorLog.log(loadPackageParam.packageName, "Method " + classname + ":" + methodname + " not found");
            return;
        }

        hook(method, delay);
    }

    public void hook(final Method method, int delay) {

        if (delay == 0) {
            XposedBridge.hookMethod(method, this);
        } else {
            Utility.dalayrun(new Runnable() {
                @Override
                public void run() {
                    XposedBridge.hookMethod(method, HookMethodInstance.this);
                }
            }, delay);

        }
    }
}
