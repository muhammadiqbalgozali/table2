package com.example.ilgozali.table_pizzana;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.ilgozali.table_pizzana.adapter.SliderUtlis;
import com.example.ilgozali.table_pizzana.adapter.ViewPagerAdapter;
import com.example.ilgozali.table_pizzana.api.ApiClient;
import com.example.ilgozali.table_pizzana.api.ApiService;
import com.example.ilgozali.table_pizzana.model.ModelData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    //varible pemanggil Delay Time untuk proses update data
    private static int TIME_DELAY = 1000;
    //variable panggil MainActivity class
    public static MainActivity ma;

    //variable untuk Data List
    private CostumAdapter adapterData;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private int curent;
    ProgressDialog progressDialog;

    //vriable swipe refresh
    private SwipeRefreshLayout swipeRefreshLayout;

    //variable Date
    TextView tx_date;

    //varible untuk Image
    RequestQueue rq;
    List<SliderUtlis> sliderImg;
    ViewPagerAdapter viewPagerAdapter;
    private static final String request_url = "http://117.53.46.24:3000/matprom";
    ViewPager viewPager;
    LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;

    //fungsi variable Handler
    private final Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //memanggil swiperefreshlayout
        swipeRefreshLayout = findViewById(R.id.sr_refreshtable);

        //memfungsikan swipe refresh layout di class iini dan memberikan warna pada swipe refresh layout
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent);

        // fungsi untuk Image
        viewPager = (ViewPager) findViewById(R.id.Halaman);
        sliderDotspanel = (LinearLayout) findViewById(R.id.SliderDots);
        rq = Volley.newRequestQueue(this);
        sliderImg = new ArrayList<>();

        //menekan bahwah lyout Main Activity pada class sini
        ma = this;

        //untuk memasukan dialog dari data ke MainActivity
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();


        //memanggil fungsi pada onRefresh
        onRefresh();
        //memanggil fungsi pada sendRequest
        sendRequest();

        //memanggil fungsi update



// memanggil fungsi tanggal ke id tv_tanggal dan membuat fungsi refresh tanggal
        tx_date = findViewById(R.id.tv_tanggal);

        final Handler showhandler = new Handler(getMainLooper());
        showhandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tx_date.setText(getDateTime());
                showhandler.postDelayed(this, 1000);
            }
        }, 10);

        //fungsikan view pager Image
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                for (int i = 0; i < dotscount; i++) {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new MyTimerTask(), 10000, 9000);


    }



//    private void getUpdate(int id,ModelData m) {
//        Call<ModelData>call = ApiService
//    }

    //mebuat fungsi request pada json untuk Image
    private void sendRequest() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, request_url, null, new com.android.volley.Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    SliderUtlis sliderUtlis = new SliderUtlis();

                    try {
                        JSONObject jsonObject = response.getJSONObject(i);

                        sliderUtlis.setSliderImageUrl(jsonObject.getString("url"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    sliderImg.add(sliderUtlis);

                }
                viewPagerAdapter = new ViewPagerAdapter(sliderImg, MainActivity.this);

                viewPager.setAdapter(viewPagerAdapter);

                dotscount = viewPagerAdapter.getCount();
                dots = new ImageView[dotscount];

                for (int i = 0; i < dotscount; i++) {

                    dots[i] = new ImageView(MainActivity.this);
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                    params.setMargins(8, 0, 8, 0);

                    sliderDotspanel.addView(dots[i], params);

                }

                dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.activity_dot));
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }

        });
        rq.add(jsonArrayRequest);
    }

    // membuat fungsi tanggal
    private String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd \n HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    //mengambil fungsi Recycle view untuk menampilakan datanya
    private void genereteDataList(List<ModelData> dataList) {
        recyclerView = findViewById(R.id.recycle);
        adapterData = new CostumAdapter(this, dataList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapterData);

        //Membuat Underline pada Setiap Item Didalam List
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.line));
        recyclerView.addItemDecoration(itemDecoration);
    }

    //membuat pemanggilan pada data model dan API
    public void dotheAutoRefresh() {
        swipeRefreshLayout.setRefreshing(false);
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<List<ModelData>> call = apiService.getDataTable();

        call.enqueue(new Callback<List<ModelData>>() {
            @Override
            public void onResponse(retrofit2.Call<List<ModelData>> call, Response<List<ModelData>> response) {
                progressDialog.dismiss();
                genereteDataList(response.body());
                Log.d("Data", "data :" + response.body().size());



            }

            @Override
            public void onFailure(retrofit2.Call<List<ModelData>> call, Throwable t) {

                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, "Data Tidak Masuk", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //memfungsikan refres data pada MainActivity
    @Override
    public void onRefresh() {
        final Runnable runnable = new Runnable() {

            @Override
            public void run() {
                dotheAutoRefresh();
                handler.postDelayed(this, 20000);
                 
            }
        };
        handler.postDelayed(runnable, TIME_DELAY);


    }

    //Time untuk Image
    public class MyTimerTask extends TimerTask {


        @Override
        public void run() {

            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < viewPagerAdapter.getCount(); i++) {
                        final int value = i;
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                viewPager.setCurrentItem(value, true);
                            }
                        });
                    }
                }
            };
            new Thread(runnable).start();
        }

    }

    //membuat fungsi untuk windows Full Screan pada class MainActivity
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            //memanggil fungsi dari hideSystemUI
            hideSystemUI();
        }
    }

    //fungsi hideSystemUI
    private void hideSystemUI() {
        //konvers get windows ke get decorview
        View coreView = getWindow().getDecorView();
        //mengaktifkan fungsi untuk menghilangkan navigasi dan status bar
        coreView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

    }
}

