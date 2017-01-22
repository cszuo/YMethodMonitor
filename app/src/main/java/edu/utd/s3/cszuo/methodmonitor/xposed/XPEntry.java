package edu.utd.s3.cszuo.methodmonitor.xposed;

import org.luaj.vm2.lib.jse.CoerceJavaToLua;

import java.io.File;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import edu.utd.s3.cszuo.methodmonitor.MethodMonitor;
import edu.utd.s3.cszuo.methodmonitor.log.InfoLog;
import edu.utd.s3.cszuo.methodmonitor.lua.LuaLoader;
import edu.utd.s3.cszuo.methodmonitor.lua.Xpb;
import edu.utd.s3.cszuo.methodmonitor.utility.Utility;

/**
 * Created by cszuo on 1/6/17.
 */

public class XPEntry implements IXposedHookLoadPackage {

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {


        //if (loadPackageParam.packageName == null || !loadPackageParam.packageName.equals("edu.utd.s3.cszuo.methodmonitor"))
        //    return;

        XposedBridge.log("loading " + loadPackageParam.packageName);

        LuaLoader lloader = new LuaLoader(loadPackageParam);
        lloader.set("loadPackageParam", CoerceJavaToLua.coerce(loadPackageParam));
        lloader.set("xpb", CoerceJavaToLua.coerce(new Xpb(loadPackageParam)));

        File destDir = new File(MethodMonitor.LUAPATH);
        String script;
        for (File f : destDir.listFiles()) {
            InfoLog.log(loadPackageParam.packageName, "Start to load and run " + f.getName());
            script = Utility.file2String(f);
            lloader.loadAndRun(f.getName(), script);
        }
        //TESTT.run(loadPackageParam);
/*
        lloader.loadAndRun("sss", "" +
                "function handleLoadPackage(loadPackageParam)\n" +
                "  xpb:print('aaa')\n" +
                "  xpb:print(loadPackageParam.packageName)\n" +
                "  xpb:print('aaa')\n" +
                "  cc = xpb:newHookMethodInstance()\n" +
                "  cc:hook(loadPackageParam,'edu.utd.s3.cszuo.methodmonitor.MethodMonitor','onCreate',{'android.os.Bundle'})\n" +
                "  cc:setLuaBeforehook(function(param, pending)\n" +
                "  xpb:print(param.thisObject)\n" +
                "  xpb:print(param)\n" +
                "  xpb:print('bbb')\n" +
                "  end\n)" +
                "  cc = xpb:newHookConstructorInstance()\n" +
                "  cc:hook(loadPackageParam,'edu.utd.s3.cszuo.methodmonitor.lua.Xpb',{})\n" +
                "  cc:setLuaBeforehook(function(param, pending)\n" +
                "  xpb:print(param.thisObject)\n" +
                "  xpb:print(param)\n" +
                "xpb:print('ccc')\n" +
                "end\n)" +
                "end\n");
*/
    }
}
