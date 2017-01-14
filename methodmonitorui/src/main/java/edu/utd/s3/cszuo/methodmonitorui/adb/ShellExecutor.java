package edu.utd.s3.cszuo.methodmonitorui.adb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by cszuo on 1/7/17.
 */

public class ShellExecutor {
    public static ArrayList<String> exec(String[] shellCommand) {
        ArrayList<String> ls = new ArrayList<String>();
        Process p = null;
        BufferedReader reader = null;
        try {
            p = Runtime.getRuntime().exec(shellCommand);

            reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                //System.out.println(line);
                ls.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                p.destroy();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ls;
    }
}
