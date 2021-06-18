package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class List3Activity extends AppCompatActivity {
    Handler handler;
    private ArrayList<HashMap<String, String>> listItems; // 存放文字、图片信息
    private SimpleAdapter listItemAdapter; // 适配器
    private int msgWhat = 9;
    ListView listView;

    private static final String TAG = "List3Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list3);
        listView = findViewById((R.id.mylist3));
        ProgressBar progressBar = findViewById(R.id.progressBar);

        /*progressBar.setVisibility(View.GONE);
        ListView.setVisibility(View.VISIBLE);
        ArrayList<HashMap<String,String>>listItems=new ArrayList<HashMap<String,String>>();
        for(int i=0;i<10;i++){
            HashMap<String,String>map =new HashMap<String,String>();
            map.put("ItemTItile","Rate: "+i);
            map.put("ItemDetail","detail"+i);
            listItems.add(map);
        }
        listItemAdapter=new SimpleAdapter(this,
                listItems,R.layout.list_item,
                new String[]{"ItemTitle","ItemDetail"},
                new int[]{R.id.itemTitle,R.id.itemDetail});
                }
                ListItems.add(map);
         */

        Handler handler = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                if (msg.what == 9) {
                    ArrayList<HashMap<String, String>> retList = (ArrayList<HashMap<String, String>>) msg.obj;
                    /*SimpleAdapter adapter=new SimpleAdapter(List3Activity.this,
                   retList,R.layout.list_item,
                            new String[]{"ItemTitle","ItemDetail"},
                            new int[]{R.id.itemTitle,R.id.itemDetail}
                            );*/
                    MyAdapter adapter = new MyAdapter(List3Activity.this, R.layout.list_item, retList);
                    listView.setAdapter(adapter);

                    progressBar.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                }

                super.handleMessage(msg);
            }
        };

        listView.setOnItemClickListener(this::onItemClick);
        MapTask task = new MapTask();
        task.setHandler(handler);

        Thread t = new Thread(task);
        t.start();
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Log.i(TAG, "onItemClick: parent=" + parent);
        Log.i(TAG, "onItemClick: view=" + view);
        Log.i(TAG, "onItemClick: position=" + position);
        Log.i(TAG, "onItemClick: id=" + id);

        Object itemAtPosition = listView.getItemAtPosition(position);
        HashMap<String, String> map = (HashMap<String, String>) itemAtPosition;
        String titleStr = map.get("ItemTitle");
        String detailStr = map.get("ItemDetail");
        Log.i(TAG, "onItemClick: titleStr=" + titleStr);
        Log.i(TAG, "onItemClick: detailStr=" + detailStr);

        TextView title = (TextView) view.findViewById(R.id.itemTitle);
        TextView detail = (TextView) view.findViewById(R.id.itemDetail);
        String title2 = String.valueOf(title.getText());
        String detail2 = String.valueOf(detail.getText());
        Log.i(TAG, "onItemClick: title2=" + title2);
        Log.i(TAG, "onItemClick: detail2=" + detail2);

        Intent rateCalc = new Intent(this, RateCalcActivity.class);
        rateCalc.putExtra("title", titleStr);
        rateCalc.putExtra("rate", Float.parseFloat(detailStr));
        startActivity(rateCalc);
    }


    /*@Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Object itemAtPosition = listView.getItemAtPosition(position);
        HashMap<String, String> map = (HashMap<String, String>) itemAtPosition;
        String titleStr = map.get("ItemTitle");
        String detailStr = map.get("ItemDetail");
        Log.i(TAG, "onItemClick:titleStr=" + titleStr);
        Log.i(TAG, "onItemClick:detailStr=" + detailStr);

    }*/

}