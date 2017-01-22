package edu.utd.s3.cszuo.methodmonitor.log.transport;

import java.nio.charset.Charset;
import java.util.HashMap;

import edu.utd.s3.cszuo.methodmonitor.log.JsonLog;
import edu.utd.s3.cszuo.methodmonitorui.adb.ILogcatWatcher;

/**
 * Created by cszuo on 1/14/17.
 */

public class LogcatTransferLayer implements ITransportLayer, ILogcatWatcher {

    static String MARK = "XOXXOOXXXOOO";
    static int MAXSENDLEN = 3000;

    static LogcatTransferLayer jltl = new LogcatTransferLayer();

    IDatareceiver dr;

    public static LogcatTransferLayer getInstance() {
        return jltl;
    }

    private LogcatTransferLayer() {
    }


    @Override
    public void send(JsonLog jlog) {
        String str = Base64.encodeToString(jlog.toString().getBytes(Charset.forName("utf-8")), Base64.NO_WRAP);
        splitAndSend(str);
    }

    @Override
    public void setDataReceiver(IDatareceiver dr) {
        this.dr = dr;
    }

    private synchronized void splitAndSend(String str) {
        long uniq = System.currentTimeMillis();
        int len = str.length();

        int blen = (int) Math.ceil(((double) len) / ((double) MAXSENDLEN));

        String tmp;
        for (int slen, i = 0; i < blen; i++) {
            slen = Math.min(MAXSENDLEN, len - i * MAXSENDLEN);
            tmp = String.format("%s:%s:%s:%s:%s", MARK, uniq, blen, i, str.substring((i * MAXSENDLEN), slen + (i * MAXSENDLEN)));
            rawSend(tmp);
        }
    }

    private void rawSend(String str) {
        System.out.println(str);
    }

    HashMap<String, HashMap<String, String>> datas = new HashMap<String, HashMap<String, String>>();

    @Override
    public void newLine(String line) {
        String[] str;
        if (line.contains(MARK)) {
            line = line.substring(line.indexOf(MARK));
            str = line.split(":");
            newSplits(str);
        }
    }

    private void newSplits(String[] str) {
        String uniq = str[1], blen = str[2], index = str[3], sdata = str[4];
        if (!datas.containsKey(uniq)) {
            datas.put(uniq, new HashMap<String, String>());
        }
        datas.get(uniq).put(index, sdata);
        checkDone(uniq, Integer.parseInt(blen));
    }

    private void checkDone(String uniq, int blen) {
        HashMap<String, String> tmp = datas.get(uniq);
        if (tmp.size() == blen) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < blen; i++) {
                sb.append(tmp.get(i + ""));
            }
            try {
                String str = sb.toString();

                str = new String(Base64.decode(str, Base64.NO_WRAP));
                JsonLog jlog = JsonLog.parse(str);
                dr.newJsonLog(jlog);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
