package com.example.myapplication;

import android.app.ListActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import androidx.annotation.NonNull;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RateListActivity extends ListActivity implements Runnable {    // 下面的布局代码需要注释掉

    Handler handler;
    ListAdapter adapter;
    private static final String TAG = "RateActivity";

    private String logDate = "";
    private final String DATE_SP_KEY = "lastRateDateStr";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_rate_list);

        SharedPreferences sp = getSharedPreferences("myrate", Context.MODE_PRIVATE);
        logDate = sp.getString(DATE_SP_KEY, "");
        Log.i("List", "lastRateDateStr=" + logDate);

        Thread t = new Thread(this);
        t.start();

        handler = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                if (msg.what == 5) {
                    List<String> retList = (List<String>) msg.obj;
                    adapter = new ArrayAdapter<String>(RateListActivity.this, android.R.layout.simple_list_item_1, retList);
                    setListAdapter(adapter);

                }
                super.handleMessage(msg);
            }
        };
    }

    @Override
    public void run() {
        Log.i(TAG, "run: ");
        List<String> ret = new ArrayList<String>();
        String curDateStr = (new SimpleDateFormat("yyyy-MM-dd")).format(new Date());
        Log.i("run", "curDateStr:" + curDateStr + " logDate:" + logDate);
        if (curDateStr.equals(logDate)) {
            //如果相等，则不从网络中获取数据
            Log.i("run", "日期相等，从数据库中获取数据");
            DBManager manager = new DBManager(this);
            for (RateItem item : manager.listAll()) {
                ret.add(item.getCurName() + "==>" + item.getCurRate());
            }

        } else {
            //如果不相等，则从网络中获取数据
            Log.i("run", "日期不相等，从网络中获取在线数据");

            //调用jsoup获取页面内容
            try {
                String url = "https://www.boc.cn/sourcedb/whpj/";
                Document doc = Jsoup.connect(url).get();
                Log.i(TAG, "run: title=" + doc.title());
                //Elements tables = doc.getElementsByTag("table");
                //Element table1 = tables.first();
                Element table1 = doc.getElementsByTag("table").get(1);
                Elements trs = table1.getElementsByTag("tr");
                List<RateItem> rateList = new ArrayList<RateItem>();
                for (Element tr : trs) {
                    Elements tds = tr.getElementsByTag("td");
                    if (tds.size() > 0) {
                        String str = tds.get(0).text();        //网页中的第一列，货币名称
                        String val = tds.get(5).text();         //网页中的第六列，折算价即汇率
                        Log.i(TAG, "run:" + str + "==>" + val);
                        ret.add(str + "==>" + val);
                        rateList.add(new RateItem(str, val));
                    }

                }
                //把数据写入到数据库中
                DBManager manager = new DBManager(this);
                manager.deleteAll();
                Log.i("db", "删除所有记录");
                manager.addAll(rateList);
                Log.i("db", "添加新纪录集");

                //更新记录日期
                SharedPreferences sp = getSharedPreferences("myrate", Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = sp.edit();
                edit.putString(DATE_SP_KEY, curDateStr);
                edit.commit();
                Log.i("run", "更新日期结束：" + curDateStr);

            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "run: " + e.toString());
            }
        }

        //发送信息
        Message msg = handler.obtainMessage(5, ret);
        msg.what = 5;
        handler.sendMessage(msg);

    }

   /* @Override
    public void onItemClick(AdapterView<?>parent, View view, int position, long id) {
        Object itemAtPosition =listView.getItemAtPosition(position);
        RateItem item =(RateItem)itemAtPosition;
        String titleStr=item.getCname();
        String detailStr=item.getCval();
        Log.i(TAG,"onItemClick:titleStr="+titleStr);
        Log.i(TAG,"onItemClick:detailStr="+detailStr);

        Intent calc=new Intent(this.RateCalcActivity.class);
        calc.putExtra("title",titleStr);
        calc.putExtra("rate",detailStr);
        startActivity(calc);
    }*/
   @Override
   public boolean onCreateOptionsMenu(Menu menu){
       getMenuInflater().inflate(R.menu.more,menu);
       return  true;
   }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){


            //测试数据库
            RateItem item1 = new RateItem("aaaa","123");
            DBManager manager = new DBManager(this);
            manager.add(item1);
            manager.add(new RateItem("bbbb","23.5"));
            Log.i(TAG, "onOptionsItemSelected: 写入数据完毕");

            //查询所有数据
            List<RateItem> testList = manager.listAll();
            for(RateItem i : testList){
                Log.i(TAG, "onOptionsItemSelected: 取出数据 [id= "+i.getId()+"]Name=" + i.getCurName() + "Rate=" + i.getCurRate());
            }
        return super.onOptionsItemSelected(item);
   }
}