package edu.utd.s3.cszuo.methodmonitorui.UI.luaeditor;

import java.util.HashMap;


import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;

import edu.utd.s3.cszuo.methodmonitorui.adb.DevicesDetector;
import edu.utd.s3.cszuo.methodmonitorui.adb.ShellExecutor;

/**
 * Created by cszuo on 7/24/17.
 */

public class LuaScriptCreator extends Thread{
    LuaEditor luae;
    public LuaScriptCreator(LuaEditor luae) {
        this.luae = luae;
    }

    public void run(){
        String name = JOptionPane.showInputDialog("Please input script file name?","newscript.lua");
        if(name != null && name.length()>0){
            ShellExecutor.exec(new String[]{DevicesDetector.adb, "shell", "touch", LuaEditor.LUAPATH + '/' + name});
            luae.loadLuas();
        }

    }

}
