package com.example.myapplication;

import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class FirstListActivity extends ListActivity implements Runnable {
    int msgWhat = 3;
    Handler handler;
    private final String TAG = "FirstListActivity";
    Document doc = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_first_list);

        /*List<String> list1 = new ArrayList<String>();
        for (int i = 1; i < 100; i++) {
            list1.add("item" + i);
        }
        String[] list_data = {"one", "two", "three", "four"};
        ListAdapter adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, list_data);
        setListAdapter(adapter);*/
        Thread t = new Thread(this);
       t.start();

        handler = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 5) {
                    List<String> List2 = (List<String>) msg.obj;
                    ListAdapter adapter = new ArrayAdapter<String>(FirstListActivity.this,
                            android.R.layout.simple_list_item_1, List2);
                    setListAdapter(adapter);
                    Log.i("handler", "reset list...");
                }
                super.handleMessage(msg);
            }
        };
        /*MyTask task=new MyTask();
       task.setHandler(handler);

        RateActivity rate=new RateActivity();
        rate.setHandler(handler);
        Thread t = new Thread(task);
        t.start();*/
    }
    @Override
    public void run() {
        Log.i("thread", "run.....");
        List<String> rateList = new ArrayList<String>();
        /*try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        try {
            String url = "https://www.boc.cn/sourcedb/whpj/ ";
            Document doc = Jsoup.connect(url).get();
            Log.i(TAG, "run:" + doc.title());

            Element table = doc.getElementsByTag("table").get(1);
            Elements trs = table.getElementsByTag("tr");
            for (Element tr : trs) {
                Elements tds = tr.getElementsByTag("td");
                if (tds.size() > 0) {
                    String str = tds.get(0).text();
                    String val = tds.get(5).text();

                    rateList.add(str + "=>" + val);
                    Log.i("tr", str + "=>" + val);
                }
            }

        } catch (MalformedURLException e) {
            Log.e("www", e.toString());
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("www", e.toString());
            e.printStackTrace();
        }
        Message msg = handler.obtainMessage(5);

        msg.obj = rateList;
        handler.sendMessage(msg);
        Log.i("thread","sendMessage.....");
    }
}