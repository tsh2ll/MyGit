package com.example.myapplication;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public  class MyTask implements Runnable {
    private static final String TAG = "MyTask";
    Handler handler;

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void run() {
        Log.i(TAG, "run:......");
        List<String> ret = new ArrayList<String>();
        try {
            Thread.sleep(3000);
            String url = "http://www.usd-cny.com/bankofchina.htm";
            Document doc = Jsoup.connect(url).get();
            Log.i(TAG, "run:" + doc.title());

            Element table = doc.getElementsByTag("table").first();
            Elements trs = table.getElementsByTag("tr");
            for (Element tr : trs) {
                Elements tds = tr.getElementsByTag("td");
                if (tds.size() > 0) {
                    String str = tds.get(0).text();
                    String val = tds.get(5).text();
                    Log.i(TAG, str + "=>" + val);
                    ret.add(str + "=>" + val);
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            Log.e(TAG, "run:" + e.toString());
        }
        Message msg =handler.obtainMessage(9,ret);
        handler.sendMessage(msg);
    }


}