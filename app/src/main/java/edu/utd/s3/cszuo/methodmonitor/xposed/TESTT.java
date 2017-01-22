package edu.utd.s3.cszuo.methodmonitor.xposed;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by cszuo on 1/18/17.
 */

public class TESTT extends XC_MethodHook {

    public static void run(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        if (loadPackageParam.packageName.equals("com.starbucks.mobilecard"))
            XposedHelpers.findAndHookMethod("com.android.okhttp.internal.http.HttpsURLConnectionImpl", loadPackageParam.classLoader, "setSSLSocketFactory", javax.net.ssl.SSLSocketFactory.class, new TESTT());
    }

    @Override
    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
        param.args[0] = javax.net.ssl.SSLSocketFactory.getDefault();
        System.out.println("HttpsURLConnectionImpl SET!");
        super.beforeHookedMethod(param);
    }
}
