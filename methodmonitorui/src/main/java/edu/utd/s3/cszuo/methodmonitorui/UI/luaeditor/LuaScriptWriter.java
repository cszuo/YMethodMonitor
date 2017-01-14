package edu.utd.s3.cszuo.methodmonitorui.UI.luaeditor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.tree.DefaultMutableTreeNode;

import edu.utd.s3.cszuo.methodmonitorui.adb.DevicesDetector;
import edu.utd.s3.cszuo.methodmonitorui.adb.ShellExecutor;

/**
 * Created by cszuo on 1/12/17.
 */

public class LuaScriptWriter extends Thread {
    ILuaWriterWatcher watchcer;
    List<Map.Entry<String, String>> luass = new ArrayList<Map.Entry<String, String>>();

    public LuaScriptWriter(ILuaWriterWatcher w, HashMap<DefaultMutableTreeNode, String> tmpl) {
        this.watchcer = w;
        String name, content;
        for (Map.Entry<DefaultMutableTreeNode, String> entry : tmpl.entrySet()) {
            name = (String) entry.getKey().getUserObject();
            content = entry.getValue();
            luass.add(new AbstractMap.SimpleEntry<String, String>(name, content));
        }
    }

    public void run() {
        String name, content;
        for (Map.Entry<String, String> entry : luass) {
            name = entry.getKey();
            content = entry.getValue();
            watchcer.info(String.format("Writing %s ", name));
            writeLuaS(name, content);
        }
        watchcer.afterWriteLuas();
    }

    public void writeLuaS(String name, String content) {
        File file = null;
        try {
            file = File.createTempFile("xposed-", "-lua");
            writeFile(file, content);
            pushFile(file, name, content);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                file.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void writeFile(File file, String content) throws IOException {
        FileOutputStream out = new FileOutputStream(file);
        out.write(content.getBytes(Charset.forName("UTF-8")));
        out.close();
    }

    public void pushFile(File file, String name, String content) {
        ArrayList<String> res = ShellExecutor.exec(new String[]{DevicesDetector.adb, "push", file.getAbsolutePath(), LuaEditor.LUAPATH + '/' + name});
        StringBuilder sb = new StringBuilder();
        for (String str : res) {
            sb.append(str);
            sb.append('\n');
        }
        watchcer.info(sb.toString().trim());
    }
}
