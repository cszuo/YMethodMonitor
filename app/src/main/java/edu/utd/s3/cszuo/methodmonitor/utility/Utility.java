package edu.utd.s3.cszuo.methodmonitor.utility;


import android.util.Base64;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import de.robv.android.xposed.callbacks.XC_LoadPackage;
import edu.utd.s3.cszuo.methodmonitor.log.ErrorLog;

/**
 * Created by cszuo on 1/6/17.
 */

public class Utility {
    public static Class<?> getClass(String classname, ClassLoader loader) {
        //if (classname.equals("int")) return int.class;
        //else if (classname.equals("String")) return String.class;
        //else if (classname.equals("boolean")) return boolean.class;
        try {
            return ClassUtils.forName(classname, loader);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public static Class<?>[] getClasses(XC_LoadPackage.LoadPackageParam loadPackageParam, String[] classnames, ClassLoader loader) {
        Class<?>[] args = new Class<?>[classnames.length];
        for (int i = 0; i < args.length; i++) {
            args[i] = Utility.getClass(classnames[i], loader);
            if (args[i] == null) {
                ErrorLog.log(loadPackageParam.packageName, "Class " + classnames[i] + " not found");
                return null;
            }
        }
        return args;
    }

    public static void dalayrun(final Runnable run, final int delay) {
        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                run.run();
            }
        }.start();
    }

    public static String file2String(File fl) {
        try {
            FileInputStream fin = new FileInputStream(fl);
            String ret = stream2String(fin);
            fin.close();
            return ret;
        } catch (IOException e) {
            return "";
        }
    }


    public static String stream2String(InputStream is) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream("".getBytes());
        try {
            KeyStore.getInstance("BKS").load(bais, new char[]{});
        } catch (Exception e) {
            e.printStackTrace();
        }


        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();

        return sb.toString();
    }
}
