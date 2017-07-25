package edu.utd.s3.cszuo.methodmonitor.xposed;

import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

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

        XposedBridge.log("HookMethodInstance " + classname + methodname);

        Class<?> mcla = Utility.getClass(classname, loadPackageParam.classLoader);
        if (mcla == null) {
            ErrorLog.log(loadPackageParam.packageName, "Class " + classname + " not found");
            return;
        }

        Class<?>[] args = Utility.getClasses(loadPackageParam, margs, loadPackageParam.classLoader);
        if (args == null) {
            return;
        }

        try {
            method = mcla.getDeclaredMethod(methodname, args);
            signature = method.toString();
        } catch (NoSuchMethodException e) {
            ErrorLog.log(loadPackageParam.packageName, "Method " + classname + ":" + methodname + " not found");
            for (Method m : mcla.getDeclaredMethods()) {
                ErrorLog.log(loadPackageParam.packageName, "Method " + m);
            }
            return;
        }
        hookMethod(Arrays.asList(method), this);
    }

    public HookMethodInstance hook(final XC_LoadPackage.LoadPackageParam loadPackageParam, final String classname, final String methodname, final String[] margs, int delay) {
        Utility.dalayrun(new Runnable() {
            @Override
            public void run() {
                hook(loadPackageParam, classname, methodname, margs);
            }
        }, delay);
        return this;
    }

    public HookMethodInstance hook(XC_LoadPackage.LoadPackageParam loadPackageParam, String classname, String methodname) {

        XposedBridge.log("HookMethodInstance " + classname + methodname);

        Class<?> mcla = Utility.getClass(classname, loadPackageParam.classLoader);
        if (mcla == null) {
            ErrorLog.log(loadPackageParam.packageName, "Class " + classname + " not found");
            return this;
        }

        ArrayList mm = new ArrayList();
        Method[] ms = mcla.getMethods();
        for (Method m : ms) {
            if (m.getName().equals(methodname)) {
                mm.add(m);
            }
        }
        signature = classname + " " + methodname;

        hookMethod(mm, this);

        return this;
    }

    public void hook(final XC_LoadPackage.LoadPackageParam loadPackageParam, final String classname, final String methodname, int delay) {
        Utility.dalayrun(new Runnable() {
            @Override
            public void run() {
                hook(loadPackageParam, classname, methodname);
            }
        }, delay);
    }
}
