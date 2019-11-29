package com.example.myapplication.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.myapplication.ParseInfo;
import com.example.myapplication.R;
import com.example.myapplication.adapter.DoctorListAdapter;
import com.example.myapplication.adapter.ParseDataAdapter;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class ParseActivity extends BasicActivity {

    final String TAG = "ParseActivity";

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home: {
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
    public String dataKey = "dOKubGl%2F9t0OcxveB36sAQWzYgKFyjxYZ2sTKHNGH3NjwT4w%2FRDAHyfZZQBSt8MizE5GnpTbBh5bJVG2mmKXBw%3D%3D";
    private String requestUrl;
    private ParseDataAdapter parseDataAdapter;
    private ArrayList<ParseInfo> parseList = null;
    private  ParseInfo care = null;
    private RecyclerView recyclerView;
    private   RelativeLayout loaderLayout;
    private int numrow = 10;
    private int num_plus = 0;
    private int middle = 0;
    int firstVisibleitemPosition, lastVisibleitemPosition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parse);
        setToolbarTitle("유기동물 정보");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = findViewById(R.id.recycler_view);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(parseDataAdapter);


        loaderLayout = findViewById(R.id.loaderLayout);
        loaderLayout.setVisibility(View.VISIBLE);

recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);


        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        int visibleitmeCount = layoutManager.getChildCount();
        int totalitemCount  = layoutManager.getItemCount();
         firstVisibleitemPosition = ((LinearLayoutManager)layoutManager).findFirstVisibleItemPosition();  //화면에 보이는 첫번째
         lastVisibleitemPosition = ((LinearLayoutManager)layoutManager).findLastVisibleItemPosition();  //화면에 보이는 마지
        middle = firstVisibleitemPosition - lastVisibleitemPosition;

        Log.e("로그",""+lastVisibleitemPosition);
        if(lastVisibleitemPosition == 9+num_plus){
            loaderLayout.setVisibility(View.VISIBLE);
            num_plus += 10;
            numrow += 10;
            MyAsyncTask myAsyncTask = new MyAsyncTask();
            myAsyncTask.execute();
        }
    }
});
//        AsyncTask
        MyAsyncTask myAsyncTask = new MyAsyncTask();
        myAsyncTask.execute();
    }

    public  class MyAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            requestUrl = "http://openapi.animal.go.kr/openapi/service/rest/abandonmentPublicSrvc/abandonmentPublic?serviceKey=" +dataKey
                    + "&bgnde=20190101&endde=20191102&upkind=417000&state=null&pageNo=1&numOfRows="+numrow+"&neuter_yn=Y";


            try {

                boolean c_careNm = false;
                boolean c_age =false;
                boolean c_location = false;
                boolean c_careTel = false;
                boolean c_filename = false;
                boolean c_sex = false;
                boolean c_weight = false;
                boolean c_specialmark = false;
                boolean c_kind = false;
                boolean c_place = false;
                URL url = new URL(requestUrl);
                InputStream is = url.openStream();
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser parser = factory.newPullParser();
                parser.setInput(new InputStreamReader(is, "UTF-8"));

                int eventType = parser.getEventType();

                while(eventType != XmlPullParser.END_DOCUMENT){
                    switch (eventType){
                        case XmlPullParser.START_DOCUMENT:
                            parseList = new ArrayList<>();
                            break;
                        case XmlPullParser.END_DOCUMENT:
                            break;
                        case XmlPullParser.END_TAG:
                            if(parser.getName().equals("item") && care != null) {
                                parseList.add(care);
                            }
                            break;
                        case XmlPullParser.START_TAG:
                            if(parser.getName().equals("item")){
                                care = new ParseInfo();
                            }
                            if (parser.getName().equals("careNm")) c_careNm = true;
                            if (parser.getName().equals("age")) c_age = true;
                            if (parser.getName().equals("careAddr")) c_location = true;
                            if (parser.getName().equals("careTel")) c_careTel = true;
                            if( parser.getName().equals("filename")) c_filename = true;
                            if (parser.getName().equals("sexCd")) c_sex = true;
                            if (parser.getName().equals("kindCd")) c_kind = true;
                            if (parser.getName().equals("specialMark")) c_specialmark = true;
                            if (parser.getName().equals("weight")) c_weight = true;
                            if (parser.getName().equals("happenPlace")) c_place = true;

                            break;
                        case XmlPullParser.TEXT:
                            if(c_careNm){
                                care.setCareNm(parser.getText());
                                c_careNm = false;
                            } else if(c_age) {
                                care.setAge(parser.getText());
                                c_age = false;
                            } else if (c_location) {
                                care.setCareAddr(parser.getText());
                                c_location = false;
                            } else if(c_careTel) {
                                care.setCareTel(parser.getText());
                                c_careTel = false;
                            } else if(c_sex) {
                                care.setSexCd(parser.getText());
                                c_sex = false;
                            }
                            else if(c_kind) {
                                care.setKindCd(parser.getText());
                                c_kind = false;
                            }
                            else if(c_specialmark) {
                                care.setSpecialMark(parser.getText());
                                c_specialmark = false;
                            }
                            else if(c_weight) {
                                care.setWeight(parser.getText());
                                c_weight = false;
                            }
                            else if(c_place) {
                                care.setHappenPlace(parser.getText());
                                c_place = false;
                            }
                            else if(c_filename) {
                                care.setFilename(parser.getText());
                                c_filename = false;
                            }

                            break;
                    }
                    eventType = parser.next();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            //어답터 연결
            final  int position = lastVisibleitemPosition;
            Log.e("로그","position"+ position);

            parseDataAdapter = new ParseDataAdapter(getApplicationContext(), parseList);
            recyclerView.setAdapter(parseDataAdapter);



                    recyclerView.scrollToPosition(position-3);
                    Log.e("로그","position"+ position);

            loaderLayout.setVisibility(View.GONE);
        }
    }
}