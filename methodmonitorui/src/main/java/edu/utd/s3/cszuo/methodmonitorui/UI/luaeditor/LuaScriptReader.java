package edu.utd.s3.cszuo.methodmonitorui.UI.luaeditor;

import java.util.ArrayList;

import edu.utd.s3.cszuo.methodmonitorui.adb.DevicesDetector;
import edu.utd.s3.cszuo.methodmonitorui.adb.ShellExecutor;

/**
 * Created by cszuo on 1/12/17.
 */

public class LuaScriptReader extends Thread {
    ILuaReaderWatchcer watchcer;

    public LuaScriptReader(ILuaReaderWatchcer w) {
        this.watchcer = w;
    }

    public void run() {
        ArrayList<String> res = ShellExecutor.exec(new String[]{DevicesDetector.adb, "shell", "ls", LuaEditor.LUAPATH});
        watchcer.info(String.format("Found %s lua scripts:", res.size()));
        for (String str : res) {
            watchcer.info(String.format("Loading %s ", str));
            readLuaS(str);
        }
    }

    public void readLuaS(String name) {
        ArrayList<String> res = ShellExecutor.exec(new String[]{DevicesDetector.adb, "shell", "cat", LuaEditor.LUAPATH + '/' + name});
        StringBuilder sb = new StringBuilder();
        for (String str : res) {
            sb.append(str);
            sb.append('\n');
        }
        watchcer.newLuas(name, sb.toString());

    }
}
