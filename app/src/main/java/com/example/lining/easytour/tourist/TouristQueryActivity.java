package com.example.lining.easytour.tourist;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.lining.easytour.R;
import com.example.lining.easytour.Refresh.GuideMainRefreshableView;
import com.example.lining.easytour.adapter.QueryArrayAdapter;
import com.example.lining.easytour.orders.Order;
import com.example.lining.easytour.orders.OrderActivity;
import com.example.lining.easytour.util.SerializableHashMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.lining.easytour.util.ToolUtil.daysBetween;

public class TouristQueryActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, GestureDetector.OnGestureListener {
    private static final int FINISHED = 0x8989;
    private static final int UNFINISHED = 0x9898;
    private ListView lv_to_be_finished;
    private Button TBDbutton,Fbutton;
    private List<Map<String,String>> orderResult;
    private List<Integer> finishedOrders;
    private List<Integer> unFinishedOrders;
    private  String guidename;
    private int flag;
    private GuideMainRefreshableView guideMainRefreshableView;
    private GestureDetector detector;
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tourist_query);
        Intent intent = getIntent();
        guidename = intent.getStringExtra("guidename");
        orderResult = new ArrayList<>();
        finishedOrders = new ArrayList<>();
        unFinishedOrders = new ArrayList<>();
        TBDbutton = findViewById(R.id.tourist_query_btn_to_be_finished);
        Fbutton = findViewById(R.id.tourist_query_btn_finished_order);
        new GetOwnOrders().execute(guidename);
        //显示Bar的返回按钮
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        //下拉刷新
        guideMainRefreshableView = (GuideMainRefreshableView) findViewById(R.id.tourist_query_rv);
        guideMainRefreshableView.listView = (ListView) findViewById(R.id.tourist_query_orders_listview);
        guideMainRefreshableView.setOnRefreshListener(new GuideMainRefreshableView.PullToRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    ListViewDataUpdate();/*更新列表数据*/
                } catch (Exception e) {
                    e.printStackTrace();
                }
                guideMainRefreshableView.finishRefreshing();
            }
        }, 0);

        detector = new GestureDetector(this);

    }

    private void ListViewDataUpdate() {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(flag == UNFINISHED){
            Intent intent = new Intent(TouristQueryActivity.this,OrderActivity.class);
            Map<String,String> map = orderResult.get(unFinishedOrders.get(position));
            SerializableHashMap serializableHashMap = new SerializableHashMap();
            serializableHashMap.setMap((HashMap<String, String>)map);
            Bundle bundle = new Bundle();
            bundle.putSerializable("message",serializableHashMap);
            bundle.putString("guidername",guidename);
            intent.putExtras(bundle);
            startActivity(intent);
        }

        if(flag == FINISHED){
            Intent intent = new Intent(TouristQueryActivity.this,OrderActivity.class);
            Map<String,String> map = orderResult.get(finishedOrders.get(position));
            SerializableHashMap serializableHashMap = new SerializableHashMap();
            serializableHashMap.setMap((HashMap<String, String>)map);
            Bundle bundle = new Bundle();
            bundle.putSerializable("message",serializableHashMap);
            bundle.putString("guidername",guidename);
            intent.putExtras(bundle);
            startActivity(intent);
        }


    }

    public void init(){
        lv_to_be_finished = findViewById(R.id.guide_orders_listview);

        flag = UNFINISHED;
        QueryArrayAdapter adapter = new QueryArrayAdapter(TouristQueryActivity.this,R.layout.order_item,getDataToBeFinished());
        lv_to_be_finished.setAdapter(adapter);
        lv_to_be_finished.setOnItemClickListener(this);
    }
    private List<Order> getDataFinished() {
        // TODO Auto-generated method stub
        List<Order> list= new ArrayList<Order>();
        for(int i=0;i<orderResult.size();i++){
            Map<String,String> map = orderResult.get(i);
            if(map.get("isDone").equals("Yes")){
                String days = daysBetween(map.get("begin_day"),map.get("end_day"));
                list.add(new Order(R.drawable.logotemp,map.get("place"),map.get("place_descript"),map.get("begin_day"),days+"days"));
                finishedOrders.add(i);
            }
        }
        return list;
    }
    private List<Order> getDataToBeFinished() {
        // TODO Auto-generated method stub
        List<Order> list= new ArrayList<Order>();
        for(int i=0;i<orderResult.size();i++){
            Map<String,String> map = orderResult.get(i);
            if(map.get("isDone").equals("No")){
                String days = daysBetween(map.get("begin_day"),map.get("end_day"));
                list.add(new Order(R.drawable.logotemp,map.get("place"),map.get("place_descript"),map.get("begin_day"),days+"days"));
                unFinishedOrders.add(i);
            }
        }
        return list;
    }


    public void btnToBeFinished(View view) {
        flag = UNFINISHED;
        TBDbutton.setBackgroundColor(getResources().getColor(R.color.colorButtonBlue));
        Fbutton.setBackgroundColor(getResources().getColor(R.color.colorButtonGray));
        QueryArrayAdapter adapter = new QueryArrayAdapter(TouristQueryActivity.this,R.layout.order_item,getDataToBeFinished());
        lv_to_be_finished.setAdapter(adapter);
    }

    public void btnFinishedOrder(View view) {
        flag = FINISHED;
        TBDbutton.setBackgroundColor(getResources().getColor(R.color.colorButtonGray));
        Fbutton.setBackgroundColor(getResources().getColor(R.color.colorButtonBlue));
        QueryArrayAdapter adapter = new QueryArrayAdapter(TouristQueryActivity.this,R.layout.order_item,getDataFinished());
        lv_to_be_finished.setAdapter(adapter);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }


    private class GetOwnOrders extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... strings) {
            String uri = "http://118.89.18.136/EasyTour/EasyTour-bk/getAcceptedOrders.php/";
            StringBuilder result = new StringBuilder();
            HttpPost httpRequest = new HttpPost(uri);
            List params = new ArrayList();
            params.add(new BasicNameValuePair("guidername", strings[0]));
            try {
                httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
                if (httpResponse.getStatusLine().getStatusCode() == 200) {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
                    for(String s=bufferedReader.readLine();s!=null;s=bufferedReader.readLine()){
                        result.append(s);
                    }
                }
                Log.i("GetOwnOrders","result----------->"+result.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject itemObject = jsonArray.getJSONObject(i);
                    Map<String,String> map = new HashMap<String, String>();
                    map.put("orderID",itemObject.getString("orderID"));
                    map.put("username",itemObject.getString("username"));
                    map.put("guidername",itemObject.getString("guidername"));
                    map.put("begin_day",itemObject.getString("begin_day"));
                    map.put("end_day",itemObject.getString("end_day"));
                    map.put("place",itemObject.getString("place"));
                    map.put("place_descript",itemObject.getString("place_descript"));
                    map.put("time_descript",itemObject.getString("time_descript"));
                    map.put("num_of_people",itemObject.getString("num_of_people"));
                    map.put("isDone",itemObject.getString("isDone"));
                    orderResult.add(map);
                }
                init();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }



}
