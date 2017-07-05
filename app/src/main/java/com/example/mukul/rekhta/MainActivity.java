package com.example.mukul.rekhta;

import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.FloatMath;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mukul.rekhta.POJO.dataBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pl.polidea.view.ZoomView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    Toolbar toolbar;

    DrawerLayout drawer;
    LinearLayout content;
    ScaleGestureDetector scaleGD;
    Toast toast;
    ProgressBar progress;
    LinearLayout contain;

ProgressBar progress1;

    PointF start = new PointF();
    PointF mid = new PointF();
    float oldDist = 1f;


    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;


    //TwoDScrollView scroll;

    TextView title, author;

    ScrollView scView;

    Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();

    LinearLayout container;

    int flag = 1;

    private float mScale = 1f;
    private ScaleGestureDetector mScaleDetector;
    GestureDetector gestureDetector;


    LinearLayout gh1, gh2, gh3, gh4, gh5, gh6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        contain = (LinearLayout)findViewById(R.id.contain);


        progress1 = (ProgressBar)findViewById(R.id.progress1);

        //container = (LinearLayout)findViewById(R.id.container);

        content = (LinearLayout) findViewById(R.id.content);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        title = (TextView) findViewById(R.id.title);
        author = (TextView) findViewById(R.id.author);
        scView = (ScrollView)findViewById(R.id.scview);

//        scrollContainer = (LinearLayout)findViewById(R.id.scroll_container);




        /*scView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                int totalHeight = scView.getChildAt(0).getHeight();

                float d = (scrollY/totalHeight) * 100;

                progress1.setProgress((int)d);

            }
        });*/


        gh1 = (LinearLayout) findViewById(R.id.ghz1);
        gh2 = (LinearLayout) findViewById(R.id.ghz2);
        gh3 = (LinearLayout) findViewById(R.id.ghz3);
        gh4 = (LinearLayout) findViewById(R.id.ghz4);
        gh5 = (LinearLayout) findViewById(R.id.ghz5);
        gh6 = (LinearLayout) findViewById(R.id.ghz6);

        progress = (ProgressBar) findViewById(R.id.progress);

        toast = Toast.makeText(this, null, Toast.LENGTH_SHORT);

        setSupportActionBar(toolbar);

        try {
            getSupportActionBar().setDisplayShowTitleEnabled(false);

        } catch (NullPointerException e1) {
            e1.printStackTrace();
        }


        drawer = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();










        content.removeAllViews();

        progress.setVisibility(View.VISIBLE);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://14.140.111.4:20002")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AllAPIs cr = retrofit.create(AllAPIs.class);


        Call<dataBean> call = cr.getData("1", "827D3643-BCC4-410B-B1B9-00008EBCA797");

        call.enqueue(new Callback<dataBean>() {
            @Override
            public void onResponse(Call<dataBean> call, Response<dataBean> response) {

                title.setText(response.body().getR().getCT());
                title.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

                author.setText(response.body().getR().getPoet().getPN());
                author.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

                String ddata = response.body().getR().getCR();

                try {
                    JSONObject jsonObject = new JSONObject(ddata);


                    JSONArray paraArray = jsonObject.getJSONArray("P");

                    LinearLayout con = new LinearLayout(MainActivity.this);
                    con.setOrientation(LinearLayout.VERTICAL);
                    con.setGravity(Gravity.CENTER_HORIZONTAL);

                    for (int i = 0; i < paraArray.length(); i++) {


                        JSONObject pobj = paraArray.getJSONObject(i);
                        JSONArray lineArray = pobj.getJSONArray("L");

                        final LinearLayout para = new LinearLayout(MainActivity.this);
                        para.setOrientation(LinearLayout.VERTICAL);
                        para.setGravity(Gravity.CENTER_HORIZONTAL);
                        para.setPadding(5, 10, 5, 10);

                        for (int j = 0; j < lineArray.length(); j++) {

                            JSONObject lobj = lineArray.getJSONObject(j);

                            JSONArray wordArray = lobj.getJSONArray("W");

                            //String line = "";

                            final LinearLayout line = new LinearLayout(MainActivity.this);
                            line.setOrientation(LinearLayout.HORIZONTAL);
                            line.setGravity(Gravity.CENTER_HORIZONTAL);
                            line.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                            line.setPadding(5, 10, 5, 10);

                            for (int k = 0; k < wordArray.length(); k++) {

                                JSONObject wordwrap = wordArray.getJSONObject(k);

                                final String wo = wordwrap.getString("W");

                                final TextView word = new TextView(MainActivity.this);
                                word.setPadding(5, 0, 5, 0);
                                word.setBackgroundColor(Color.TRANSPARENT);
                                word.setText(wo);
                                word.setTextSize(16);
                                word.setTextColor(Color.BLACK);





                                word.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        toast.setText("word: " + wo);
                                        toast.show();
                                    }
                                });


                                line.addView(word);

                            }

                            final int maxwidth = getWindowManager().getDefaultDisplay().getWidth();

                            final int[] wordwidth = {0};

                            Log.d("asdCount" , String.valueOf(line.getChildCount()));

                            for (int m = 0 ; m < line.getChildCount() ; m++)
                            {

                                final View v = line.getChildAt(m);


                                final int finalM = m;
                                v.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                                    @Override
                                    public void onGlobalLayout() {
                                        v.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                                        wordwidth[0] = wordwidth[0] + v.getWidth();

                                        Log.d("asd" , String.valueOf(v.getWidth()));

                                        if (finalM == line.getChildCount()-1)
                                        {

                                            int emptySpace = maxwidth - wordwidth[0];

                                            Log.d("wordWidth" , String.valueOf(wordwidth[0]));

                                            Log.d("empty" , String.valueOf(emptySpace));

                                            float space = emptySpace / (line.getChildCount() + 1);

                                            float netspace = space / 2;

                                            for (int m = 0 ; m < line.getChildCount() ; m++)
                                            {


                                                View v = line.getChildAt(m);

                                                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                                                layoutParams.setMargins((int)netspace , 0 , (int)netspace , 0);

                                                //v.setPadding((int)netspace , 0 , (int)netspace , 0);

                                                v.setLayoutParams(layoutParams);

                                            }

//                            line.setPadding((int)netspace , 0 , (int)netspace , 0);



                                        }

                                    }
                                });




                            }

                            Log.d("maxWidth" , String.valueOf(maxwidth));

                            para.addView(line);


                            /*int emptySpace = maxwidth - wordwidth[0];

                            Log.d("wordWidth" , String.valueOf(wordwidth[0]));

                            Log.d("empty" , String.valueOf(emptySpace));

                            float space = emptySpace / (line.getChildCount() + 1);

                            float netspace = space / 2;

                            for (int m = 0 ; m < line.getChildCount() ; m++)
                            {


                                    View v = line.getChildAt(m);
                                    v.setPadding((int)netspace , 0 , (int)netspace , 0);




                            }

//                            line.setPadding((int)netspace , 0 , (int)netspace , 0);

                            para.addView(line);*/


                            Log.d("asdasd", line + "\n");

                        }

                        final int finalI = i;
                        para.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View view) {

                                toast.setText("para pressed: " + String.valueOf(finalI + 1));

                                toast.show();
                                return true;
                            }
                        });

                        con.addView(para);
                        Space ap = new Space(MainActivity.this);
                        ap.setMinimumHeight(30);
                        con.addView(ap);

                        Log.d("asdasd", "\n");

                    }


                    content.addView(con);

                    /*container.removeAllViews();

                    ZoomView zv = new ZoomView(MainActivity.this);

                    zv.addView(scroll);

                    zv.setNestedScrollingEnabled(false);

                    container.addView(zv);*/


                    progress.setVisibility(View.GONE);


                    flag = 1;

                } catch (JSONException e) {
                    e.printStackTrace();
                    progress.setVisibility(View.GONE);
                }


                Log.d("asdasd", ddata);

                String formattedData = ddata.replaceAll("\\/", ddata);

                Log.d("asdasd", formattedData);

            }

            @Override
            public void onFailure(Call<dataBean> call, Throwable throwable) {

                progress.setVisibility(View.GONE);

            }
        });


        gh1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content.removeAllViews();
                progress.setVisibility(View.VISIBLE);

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://14.140.111.4:20002")
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                AllAPIs cr = retrofit.create(AllAPIs.class);


                Call<dataBean> call = cr.getData("1", "827D3643-BCC4-410B-B1B9-00008EBCA797");

                call.enqueue(new Callback<dataBean>() {
                    @Override
                    public void onResponse(Call<dataBean> call, Response<dataBean> response) {


                        try {


                            title.setText(response.body().getR().getCT());
                            title.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

                            author.setText(response.body().getR().getPoet().getPN());
                            author.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

                            String ddata = response.body().getR().getCR();


                            JSONObject jsonObject = new JSONObject(ddata);


                            JSONArray paraArray = jsonObject.getJSONArray("P");

                            LinearLayout con = new LinearLayout(MainActivity.this);
                            con.setOrientation(LinearLayout.VERTICAL);
                            con.setGravity(Gravity.CENTER_HORIZONTAL);

                            for (int i = 0; i < paraArray.length(); i++) {


                                JSONObject pobj = paraArray.getJSONObject(i);
                                JSONArray lineArray = pobj.getJSONArray("L");

                                LinearLayout para = new LinearLayout(MainActivity.this);
                                para.setOrientation(LinearLayout.VERTICAL);
                                para.setGravity(Gravity.CENTER_HORIZONTAL);
                                para.setPadding(5, 10, 5, 10);

                                for (int j = 0; j < lineArray.length(); j++) {

                                    JSONObject lobj = lineArray.getJSONObject(j);

                                    JSONArray wordArray = lobj.getJSONArray("W");

                                    //String line = "";

                                    final LinearLayout line = new LinearLayout(MainActivity.this);
                                    line.setOrientation(LinearLayout.HORIZONTAL);
                                    line.setGravity(Gravity.CENTER_HORIZONTAL);
                                    line.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                                    line.setPadding(5, 10, 5, 10);

                                    for (int k = 0; k < wordArray.length(); k++) {

                                        JSONObject wordwrap = wordArray.getJSONObject(k);

                                        final String wo = wordwrap.getString("W");

                                        final TextView word = new TextView(MainActivity.this);
                                        word.setPadding(5, 0, 5, 0);
                                        word.setBackgroundColor(Color.TRANSPARENT);
                                        word.setText(wo);
                                        word.setTextSize(16);
                                        word.setTextColor(Color.BLACK);





                                        word.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                toast.setText("word: " + wo);
                                                toast.show();
                                            }
                                        });


                                        line.addView(word);

                                    }

                                    final int maxwidth = getWindowManager().getDefaultDisplay().getWidth();

                                    final int[] wordwidth = {0};

                                    Log.d("asdCount" , String.valueOf(line.getChildCount()));

                                    for (int m = 0 ; m < line.getChildCount() ; m++)
                                    {

                                        final View v = line.getChildAt(m);


                                        final int finalM = m;
                                        v.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                                            @Override
                                            public void onGlobalLayout() {
                                                v.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                                                wordwidth[0] = wordwidth[0] + v.getWidth();

                                                Log.d("asd" , String.valueOf(v.getWidth()));

                                                if (finalM == line.getChildCount()-1)
                                                {

                                                    int emptySpace = maxwidth - wordwidth[0];

                                                    Log.d("wordWidth" , String.valueOf(wordwidth[0]));

                                                    Log.d("empty" , String.valueOf(emptySpace));

                                                    float space = emptySpace / (line.getChildCount() + 1);

                                                    float netspace = space / 2;

                                                    for (int m = 0 ; m < line.getChildCount() ; m++)
                                                    {


                                                        View v = line.getChildAt(m);

                                                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                                                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                                                        layoutParams.setMargins((int)netspace , 0 , (int)netspace , 0);

                                                        //v.setPadding((int)netspace , 0 , (int)netspace , 0);

                                                        v.setLayoutParams(layoutParams);

                                                    }

//                            line.setPadding((int)netspace , 0 , (int)netspace , 0);



                                                }

                                            }
                                        });




                                    }

                                    Log.d("maxWidth" , String.valueOf(maxwidth));

                                    para.addView(line);


                                    Log.d("asdasd", line + "\n");

                                }

                                final int finalI = i;
                                para.setOnLongClickListener(new View.OnLongClickListener() {
                                    @Override
                                    public boolean onLongClick(View view) {

                                        toast.setText("para pressed: " + String.valueOf(finalI + 1));

                                        toast.show();
                                        return true;
                                    }
                                });

                                con.addView(para);
                                Space ap = new Space(MainActivity.this);
                                ap.setMinimumHeight(30);
                                con.addView(ap);
                                Log.d("asdasd", "\n");

                            }


                            content.addView(con);

                            progress.setVisibility(View.GONE);

                            flag = 1;

                        } catch (Exception e) {
                            e.printStackTrace();
                            progress.setVisibility(View.GONE);
                        }


                    }

                    @Override
                    public void onFailure(Call<dataBean> call, Throwable throwable) {

                        progress.setVisibility(View.GONE);

                    }
                });


                drawer.closeDrawer(GravityCompat.START);

            }
        });


        gh2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content.removeAllViews();
                progress.setVisibility(View.VISIBLE);

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://14.140.111.4:20002")
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                AllAPIs cr = retrofit.create(AllAPIs.class);


                Call<dataBean> call = cr.getData("1", "FC75E922-FD23-4510-B8AE-00263E506E1E");

                call.enqueue(new Callback<dataBean>() {
                    @Override
                    public void onResponse(Call<dataBean> call, Response<dataBean> response) {


                        try {

                            title.setText(response.body().getR().getCT());
                            title.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

                            author.setText(response.body().getR().getPoet().getPN());
                            author.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

                            String ddata = response.body().getR().getCR();

                            JSONObject jsonObject = new JSONObject(ddata);


                            JSONArray paraArray = jsonObject.getJSONArray("P");

                            LinearLayout con = new LinearLayout(MainActivity.this);
                            con.setOrientation(LinearLayout.VERTICAL);
                            con.setGravity(Gravity.START);

                            for (int i = 0; i < paraArray.length(); i++) {


                                JSONObject pobj = paraArray.getJSONObject(i);
                                JSONArray lineArray = pobj.getJSONArray("L");

                                LinearLayout para = new LinearLayout(MainActivity.this);
                                para.setOrientation(LinearLayout.VERTICAL);
                                para.setGravity(Gravity.START);
                                para.setPadding(5, 10, 5, 10);

                                for (int j = 0; j < lineArray.length(); j++) {

                                    JSONObject lobj = lineArray.getJSONObject(j);

                                    JSONArray wordArray = lobj.getJSONArray("W");

                                    //String line = "";

                                    final LinearLayout line = new LinearLayout(MainActivity.this);
                                    line.setOrientation(LinearLayout.HORIZONTAL);
                                    line.setGravity(Gravity.START);
                                    line.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                                    line.setPadding(5, 10, 5, 10);

                                    for (int k = 0; k < wordArray.length(); k++) {

                                        JSONObject wordwrap = wordArray.getJSONObject(k);

                                        final String wo = wordwrap.getString("W");

                                        final TextView word = new TextView(MainActivity.this);
                                        word.setPadding(5, 0, 5, 0);
                                        word.setBackgroundColor(Color.TRANSPARENT);
                                        word.setText(wo);
                                        word.setTextSize(16);
                                        word.setTextColor(Color.BLACK);





                                        word.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                toast.setText("word: " + wo);
                                                toast.show();
                                            }
                                        });


                                        line.addView(word);

                                    }

                                    final int maxwidth = getWindowManager().getDefaultDisplay().getWidth();

                                    final int[] wordwidth = {0};

                                    Log.d("asdCount" , String.valueOf(line.getChildCount()));

                                    for (int m = 0 ; m < line.getChildCount() ; m++)
                                    {

                                        final View v = line.getChildAt(m);


                                        final int finalM = m;
                                        v.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                                            @Override
                                            public void onGlobalLayout() {
                                                v.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                                                wordwidth[0] = wordwidth[0] + v.getWidth();

                                                Log.d("asd" , String.valueOf(v.getWidth()));

                                                if (finalM == line.getChildCount()-1)
                                                {

                                                    int emptySpace = maxwidth - wordwidth[0];

                                                    Log.d("wordWidth" , String.valueOf(wordwidth[0]));

                                                    Log.d("empty" , String.valueOf(emptySpace));

                                                    float space = emptySpace / (line.getChildCount() + 1);

                                                    float netspace = space / 2;

                                                    for (int m = 0 ; m < line.getChildCount() ; m++)
                                                    {


                                                        View v = line.getChildAt(m);

                                                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                                                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                                                        layoutParams.setMargins((int)netspace , 0 , (int)netspace , 0);

                                                        //v.setPadding((int)netspace , 0 , (int)netspace , 0);

                                                        v.setLayoutParams(layoutParams);

                                                    }

//                            line.setPadding((int)netspace , 0 , (int)netspace , 0);



                                                }

                                            }
                                        });




                                    }

                                    Log.d("maxWidth" , String.valueOf(maxwidth));
                                    para.addView(line);


                                    Log.d("asdasd", line + "\n");

                                }

                                final int finalI = i;
                                para.setOnLongClickListener(new View.OnLongClickListener() {
                                    @Override
                                    public boolean onLongClick(View view) {

                                        toast.setText("para pressed: " + String.valueOf(finalI + 1));

                                        toast.show();
                                        return true;
                                    }
                                });

                                con.addView(para);
                                Space ap = new Space(MainActivity.this);
                                ap.setMinimumHeight(30);
                                con.addView(ap);
                                Log.d("asdasd", "\n");

                            }


                            content.addView(con);

                            progress.setVisibility(View.GONE);

                            flag = 2;

                        } catch (Exception e) {
                            e.printStackTrace();
                            progress.setVisibility(View.GONE);
                        }


                    }

                    @Override
                    public void onFailure(Call<dataBean> call, Throwable throwable) {

                        progress.setVisibility(View.GONE);

                    }
                });


                drawer.closeDrawer(GravityCompat.START);

            }
        });


        gh3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content.removeAllViews();
                progress.setVisibility(View.VISIBLE);

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://14.140.111.4:20002")
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                AllAPIs cr = retrofit.create(AllAPIs.class);


                Call<dataBean> call = cr.getData("1", "5566AE8C-00A1-4AAE-8CDFCA09DB728342");

                call.enqueue(new Callback<dataBean>() {
                    @Override
                    public void onResponse(Call<dataBean> call, Response<dataBean> response) {


                        try {

                            title.setText(response.body().getR().getCT());
                            title.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

                            author.setText(response.body().getR().getPoet().getPN());
                            author.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

                            String ddata = response.body().getR().getCR();


                            JSONObject jsonObject = new JSONObject(ddata);


                            JSONArray paraArray = jsonObject.getJSONArray("P");

                            LinearLayout con = new LinearLayout(MainActivity.this);
                            con.setOrientation(LinearLayout.VERTICAL);
                            con.setGravity(Gravity.CENTER_HORIZONTAL);

                            for (int i = 0; i < paraArray.length(); i++) {


                                JSONObject pobj = paraArray.getJSONObject(i);
                                JSONArray lineArray = pobj.getJSONArray("L");

                                LinearLayout para = new LinearLayout(MainActivity.this);
                                para.setOrientation(LinearLayout.VERTICAL);
                                para.setGravity(Gravity.CENTER_HORIZONTAL);
                                para.setPadding(5, 10, 5, 10);

                                for (int j = 0; j < lineArray.length(); j++) {

                                    JSONObject lobj = lineArray.getJSONObject(j);

                                    JSONArray wordArray = lobj.getJSONArray("W");

                                    //String line = "";

                                    final LinearLayout line = new LinearLayout(MainActivity.this);
                                    line.setOrientation(LinearLayout.HORIZONTAL);
                                    line.setGravity(Gravity.CENTER_HORIZONTAL);
                                    line.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                                    line.setPadding(5, 10, 5, 10);

                                    for (int k = 0; k < wordArray.length(); k++) {

                                        JSONObject wordwrap = wordArray.getJSONObject(k);

                                        final String wo = wordwrap.getString("W");

                                        final TextView word = new TextView(MainActivity.this);
                                        word.setPadding(5, 0, 5, 0);
                                        word.setBackgroundColor(Color.TRANSPARENT);
                                        word.setText(wo);
                                        word.setTextSize(16);
                                        word.setTextColor(Color.BLACK);





                                        word.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                toast.setText("word: " + wo);
                                                toast.show();
                                            }
                                        });


                                        line.addView(word);

                                    }

                                    final int maxwidth = getWindowManager().getDefaultDisplay().getWidth();

                                    final int[] wordwidth = {0};

                                    Log.d("asdCount" , String.valueOf(line.getChildCount()));

                                    for (int m = 0 ; m < line.getChildCount() ; m++)
                                    {

                                        final View v = line.getChildAt(m);


                                        final int finalM = m;
                                        v.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                                            @Override
                                            public void onGlobalLayout() {
                                                v.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                                                wordwidth[0] = wordwidth[0] + v.getWidth();

                                                Log.d("asd" , String.valueOf(v.getWidth()));

                                                if (finalM == line.getChildCount()-1)
                                                {

                                                    int emptySpace = maxwidth - wordwidth[0];

                                                    Log.d("wordWidth" , String.valueOf(wordwidth[0]));

                                                    Log.d("empty" , String.valueOf(emptySpace));

                                                    float space = emptySpace / (line.getChildCount() + 1);

                                                    float netspace = space / 2;

                                                    for (int m = 0 ; m < line.getChildCount() ; m++)
                                                    {


                                                        View v = line.getChildAt(m);

                                                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                                                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                                                        layoutParams.setMargins((int)netspace , 0 , (int)netspace , 0);

                                                        //v.setPadding((int)netspace , 0 , (int)netspace , 0);

                                                        v.setLayoutParams(layoutParams);

                                                    }

//                            line.setPadding((int)netspace , 0 , (int)netspace , 0);



                                                }

                                            }
                                        });




                                    }

                                    Log.d("maxWidth" , String.valueOf(maxwidth));
                                    para.addView(line);


                                    Log.d("asdasd", line + "\n");

                                }

                                final int finalI = i;
                                para.setOnLongClickListener(new View.OnLongClickListener() {
                                    @Override
                                    public boolean onLongClick(View view) {

                                        toast.setText("para pressed: " + String.valueOf(finalI + 1));

                                        toast.show();
                                        return true;
                                    }
                                });

                                con.addView(para);
                                Space ap = new Space(MainActivity.this);
                                ap.setMinimumHeight(30);
                                con.addView(ap);
                                Log.d("asdasd", "\n");

                            }


                            content.addView(con);

                            progress.setVisibility(View.GONE);

                            flag = 3;

                        } catch (Exception e) {
                            e.printStackTrace();
                            progress.setVisibility(View.GONE);
                        }


                    }

                    @Override
                    public void onFailure(Call<dataBean> call, Throwable throwable) {

                        progress.setVisibility(View.GONE);

                    }
                });


                drawer.closeDrawer(GravityCompat.START);

            }
        });

        gh4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content.removeAllViews();
                progress.setVisibility(View.VISIBLE);

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://14.140.111.4:20002")
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                AllAPIs cr = retrofit.create(AllAPIs.class);


                Call<dataBean> call = cr.getData("1", "CE328811-2C4C-42D1-B685-A9ADC9D97EF3");

                call.enqueue(new Callback<dataBean>() {
                    @Override
                    public void onResponse(Call<dataBean> call, Response<dataBean> response) {


                        try {

                            title.setText(response.body().getR().getCT());
                            title.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

                            author.setText(response.body().getR().getPoet().getPN());
                            author.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

                            String ddata = response.body().getR().getCR();


                            JSONObject jsonObject = new JSONObject(ddata);


                            JSONArray paraArray = jsonObject.getJSONArray("P");

                            LinearLayout con = new LinearLayout(MainActivity.this);
                            con.setOrientation(LinearLayout.VERTICAL);
                            con.setGravity(Gravity.END);

                            for (int i = 0; i < paraArray.length(); i++) {


                                JSONObject pobj = paraArray.getJSONObject(i);
                                JSONArray lineArray = pobj.getJSONArray("L");

                                LinearLayout para = new LinearLayout(MainActivity.this);
                                para.setOrientation(LinearLayout.VERTICAL);
                                para.setGravity(Gravity.END);
                                para.setPadding(5, 10, 5, 10);

                                for (int j = 0; j < lineArray.length(); j++) {

                                    JSONObject lobj = lineArray.getJSONObject(j);

                                    JSONArray wordArray = lobj.getJSONArray("W");

                                    //String line = "";

                                    final LinearLayout line = new LinearLayout(MainActivity.this);
                                    line.setOrientation(LinearLayout.HORIZONTAL);
                                    line.setGravity(Gravity.END);
                                    line.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                                    line.setPadding(5, 10, 5, 10);

                                    for (int k = 0; k < wordArray.length(); k++) {

                                        JSONObject wordwrap = wordArray.getJSONObject(k);

                                        final String wo = wordwrap.getString("W");

                                        final TextView word = new TextView(MainActivity.this);
                                        word.setPadding(5, 0, 5, 0);
                                        word.setBackgroundColor(Color.TRANSPARENT);
                                        word.setText(wo);
                                        word.setTextSize(16);
                                        word.setTextColor(Color.BLACK);





                                        word.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                toast.setText("word: " + wo);
                                                toast.show();
                                            }
                                        });


                                        line.addView(word);

                                    }

                                    final int maxwidth = getWindowManager().getDefaultDisplay().getWidth();

                                    final int[] wordwidth = {0};

                                    Log.d("asdCount" , String.valueOf(line.getChildCount()));

                                    for (int m = 0 ; m < line.getChildCount() ; m++)
                                    {

                                        final View v = line.getChildAt(m);


                                        final int finalM = m;
                                        v.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                                            @Override
                                            public void onGlobalLayout() {
                                                v.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                                                wordwidth[0] = wordwidth[0] + v.getWidth();

                                                Log.d("asd" , String.valueOf(v.getWidth()));

                                                if (finalM == line.getChildCount()-1)
                                                {

                                                    int emptySpace = maxwidth - wordwidth[0];

                                                    Log.d("wordWidth" , String.valueOf(wordwidth[0]));

                                                    Log.d("empty" , String.valueOf(emptySpace));

                                                    float space = emptySpace / (line.getChildCount() + 1);

                                                    float netspace = space / 2;

                                                    for (int m = 0 ; m < line.getChildCount() ; m++)
                                                    {


                                                        View v = line.getChildAt(m);

                                                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                                                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                                                        layoutParams.setMargins((int)netspace , 0 , (int)netspace , 0);

                                                        //v.setPadding((int)netspace , 0 , (int)netspace , 0);

                                                        v.setLayoutParams(layoutParams);

                                                    }

//                            line.setPadding((int)netspace , 0 , (int)netspace , 0);



                                                }

                                            }
                                        });




                                    }

                                    Log.d("maxWidth" , String.valueOf(maxwidth));
                                    para.addView(line);


                                    Log.d("asdasd", line + "\n");

                                }

                                final int finalI = i;
                                para.setOnLongClickListener(new View.OnLongClickListener() {
                                    @Override
                                    public boolean onLongClick(View view) {

                                        toast.setText("para pressed: " + String.valueOf(finalI + 1));

                                        toast.show();
                                        return true;
                                    }
                                });

                                con.addView(para);
                                Space ap = new Space(MainActivity.this);
                                ap.setMinimumHeight(30);
                                con.addView(ap);
                                Log.d("asdasd", "\n");

                            }


                            content.addView(con);

                            progress.setVisibility(View.GONE);

                            flag = 4;

                        } catch (Exception e) {
                            e.printStackTrace();
                            progress.setVisibility(View.GONE);
                        }


                    }

                    @Override
                    public void onFailure(Call<dataBean> call, Throwable throwable) {

                        progress.setVisibility(View.GONE);

                    }
                });


                drawer.closeDrawer(GravityCompat.START);

            }
        });

        gh5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content.removeAllViews();
                progress.setVisibility(View.VISIBLE);

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://14.140.111.4:20002")
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                AllAPIs cr = retrofit.create(AllAPIs.class);


                Call<dataBean> call = cr.getData("1", "B86C1FBE-2C5A-4A75-BFCE-007580A1451D");

                call.enqueue(new Callback<dataBean>() {
                    @Override
                    public void onResponse(Call<dataBean> call, Response<dataBean> response) {

                        try {

                            title.setText(response.body().getR().getCT());
                            title.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

                            author.setText(response.body().getR().getPoet().getPN());
                            author.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

                            String ddata = response.body().getR().getCR();

                            JSONObject jsonObject = new JSONObject(ddata);


                            JSONArray paraArray = jsonObject.getJSONArray("P");

                            LinearLayout con = new LinearLayout(MainActivity.this);
                            con.setOrientation(LinearLayout.VERTICAL);
                            con.setGravity(Gravity.START);

                            for (int i = 0; i < paraArray.length(); i++) {


                                JSONObject pobj = paraArray.getJSONObject(i);
                                JSONArray lineArray = pobj.getJSONArray("L");

                                LinearLayout para = new LinearLayout(MainActivity.this);
                                para.setOrientation(LinearLayout.VERTICAL);
                                para.setGravity(Gravity.START);
                                para.setPadding(5, 10, 5, 10);

                                for (int j = 0; j < lineArray.length(); j++) {

                                    JSONObject lobj = lineArray.getJSONObject(j);

                                    JSONArray wordArray = lobj.getJSONArray("W");

                                    //String line = "";

                                    final LinearLayout line = new LinearLayout(MainActivity.this);
                                    line.setOrientation(LinearLayout.HORIZONTAL);
                                    line.setGravity(Gravity.START);
                                    line.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                                    line.setPadding(5, 10, 5, 10);

                                    for (int k = 0; k < wordArray.length(); k++) {

                                        JSONObject wordwrap = wordArray.getJSONObject(k);

                                        final String wo = wordwrap.getString("W");

                                        final TextView word = new TextView(MainActivity.this);
                                        word.setPadding(5, 0, 5, 0);
                                        word.setBackgroundColor(Color.TRANSPARENT);
                                        word.setText(wo);
                                        word.setTextSize(16);
                                        word.setTextColor(Color.BLACK);





                                        word.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                toast.setText("word: " + wo);
                                                toast.show();
                                            }
                                        });


                                        line.addView(word);

                                    }

                                    final int maxwidth = getWindowManager().getDefaultDisplay().getWidth();

                                    final int[] wordwidth = {0};

                                    Log.d("asdCount" , String.valueOf(line.getChildCount()));

                                    for (int m = 0 ; m < line.getChildCount() ; m++)
                                    {

                                        final View v = line.getChildAt(m);


                                        final int finalM = m;
                                        v.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                                            @Override
                                            public void onGlobalLayout() {
                                                v.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                                                wordwidth[0] = wordwidth[0] + v.getWidth();

                                                Log.d("asd" , String.valueOf(v.getWidth()));

                                                if (finalM == line.getChildCount()-1)
                                                {

                                                    int emptySpace = maxwidth - wordwidth[0];

                                                    Log.d("wordWidth" , String.valueOf(wordwidth[0]));

                                                    Log.d("empty" , String.valueOf(emptySpace));

                                                    float space = emptySpace / (line.getChildCount() + 1);

                                                    float netspace = space / 2;

                                                    for (int m = 0 ; m < line.getChildCount() ; m++)
                                                    {


                                                        View v = line.getChildAt(m);

                                                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                                                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                                                        layoutParams.setMargins((int)netspace , 0 , (int)netspace , 0);

                                                        //v.setPadding((int)netspace , 0 , (int)netspace , 0);

                                                        v.setLayoutParams(layoutParams);

                                                    }

//                            line.setPadding((int)netspace , 0 , (int)netspace , 0);



                                                }

                                            }
                                        });




                                    }

                                    Log.d("maxWidth" , String.valueOf(maxwidth));

                                    para.addView(line);


                                    Log.d("asdasd", line + "\n");

                                }

                                final int finalI = i;
                                para.setOnLongClickListener(new View.OnLongClickListener() {
                                    @Override
                                    public boolean onLongClick(View view) {

                                        toast.setText("para pressed: " + String.valueOf(finalI + 1));

                                        toast.show();
                                        return true;
                                    }
                                });

                                con.addView(para);
                                Space ap = new Space(MainActivity.this);
                                ap.setMinimumHeight(30);
                                con.addView(ap);
                                Log.d("asdasd", "\n");

                            }


                            content.addView(con);

                            progress.setVisibility(View.GONE);

                            flag = 5;

                        } catch (Exception e) {
                            e.printStackTrace();
                            progress.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onFailure(Call<dataBean> call, Throwable throwable) {

                        progress.setVisibility(View.GONE);

                    }
                });


                drawer.closeDrawer(GravityCompat.START);

            }
        });

        gh6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content.removeAllViews();
                progress.setVisibility(View.VISIBLE);

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://14.140.111.4:20002")
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                AllAPIs cr = retrofit.create(AllAPIs.class);


                Call<dataBean> call = cr.getData("1", "EEBC8F15-6904-49A9-BFE4-6E76E71E28BB");

                call.enqueue(new Callback<dataBean>() {
                    @Override
                    public void onResponse(Call<dataBean> call, Response<dataBean> response) {


                        try {

                            title.setText(response.body().getR().getCT());
                            title.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

                            author.setText(response.body().getR().getPoet().getPN());
                            author.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

                            String ddata = response.body().getR().getCR();

                            JSONObject jsonObject = new JSONObject(ddata);


                            JSONArray paraArray = jsonObject.getJSONArray("P");

                            LinearLayout con = new LinearLayout(MainActivity.this);
                            con.setOrientation(LinearLayout.VERTICAL);
                            con.setGravity(Gravity.CENTER_HORIZONTAL);

                            for (int i = 0; i < paraArray.length(); i++) {


                                JSONObject pobj = paraArray.getJSONObject(i);
                                JSONArray lineArray = pobj.getJSONArray("L");

                                LinearLayout para = new LinearLayout(MainActivity.this);
                                para.setOrientation(LinearLayout.VERTICAL);
                                para.setGravity(Gravity.CENTER_HORIZONTAL);
                                para.setPadding(5, 10, 5, 10);

                                for (int j = 0; j < lineArray.length(); j++) {

                                    JSONObject lobj = lineArray.getJSONObject(j);

                                    JSONArray wordArray = lobj.getJSONArray("W");

                                    //String line = "";

                                    final LinearLayout line = new LinearLayout(MainActivity.this);
                                    line.setOrientation(LinearLayout.HORIZONTAL);
                                    line.setGravity(Gravity.CENTER_HORIZONTAL);
                                    line.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                                    line.setPadding(5, 10, 5, 10);

                                    for (int k = 0; k < wordArray.length(); k++) {

                                        JSONObject wordwrap = wordArray.getJSONObject(k);

                                        final String wo = wordwrap.getString("W");

                                        final TextView word = new TextView(MainActivity.this);
                                        word.setPadding(5, 0, 5, 0);
                                        word.setBackgroundColor(Color.TRANSPARENT);
                                        word.setText(wo);
                                        word.setTextSize(16);
                                        word.setTextColor(Color.BLACK);





                                        word.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                toast.setText("word: " + wo);
                                                toast.show();
                                            }
                                        });


                                        line.addView(word);

                                    }

                                    final int maxwidth = getWindowManager().getDefaultDisplay().getWidth();

                                    final int[] wordwidth = {0};

                                    Log.d("asdCount" , String.valueOf(line.getChildCount()));

                                    for (int m = 0 ; m < line.getChildCount() ; m++)
                                    {

                                        final View v = line.getChildAt(m);


                                        final int finalM = m;
                                        v.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                                            @Override
                                            public void onGlobalLayout() {
                                                v.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                                                wordwidth[0] = wordwidth[0] + v.getWidth();

                                                Log.d("asd" , String.valueOf(v.getWidth()));

                                                if (finalM == line.getChildCount()-1)
                                                {

                                                    int emptySpace = maxwidth - wordwidth[0];

                                                    Log.d("wordWidth" , String.valueOf(wordwidth[0]));

                                                    Log.d("empty" , String.valueOf(emptySpace));

                                                    float space = emptySpace / (line.getChildCount() + 1);

                                                    float netspace = space / 2;

                                                    for (int m = 0 ; m < line.getChildCount() ; m++)
                                                    {


                                                        View v = line.getChildAt(m);

                                                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                                                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                                                        layoutParams.setMargins((int)netspace , 0 , (int)netspace , 0);

                                                        //v.setPadding((int)netspace , 0 , (int)netspace , 0);

                                                        v.setLayoutParams(layoutParams);

                                                    }

//                            line.setPadding((int)netspace , 0 , (int)netspace , 0);



                                                }

                                            }
                                        });




                                    }

                                    Log.d("maxWidth" , String.valueOf(maxwidth));


                                    para.addView(line);


                                    Log.d("asdasd", line + "\n");

                                }

                                final int finalI = i;
                                para.setOnLongClickListener(new View.OnLongClickListener() {
                                    @Override
                                    public boolean onLongClick(View view) {

                                        toast.setText("para pressed: " + String.valueOf(finalI + 1));

                                        toast.show();
                                        return true;
                                    }
                                });

                                con.addView(para);
                                Space ap = new Space(MainActivity.this);
                                ap.setMinimumHeight(30);
                                con.addView(ap);
                                Log.d("asdasd", "\n");

                            }


                            content.addView(con);

                            progress.setVisibility(View.GONE);

                            flag = 6;

                        } catch (Exception e) {
                            e.printStackTrace();
                            progress.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onFailure(Call<dataBean> call, Throwable throwable) {

                        progress.setVisibility(View.GONE);

                    }
                });


                drawer.closeDrawer(GravityCompat.START);

            }
        });



        //gestureDetector = new GestureDetector(this, new GestureListener());

// animation for scalling
        /*mScaleDetector = new ScaleGestureDetector(this, new ScaleGestureDetector.SimpleOnScaleGestureListener()
        {
            @Override
            public boolean onScale(ScaleGestureDetector detector)
            {
                float scale = 1 - detector.getScaleFactor();



                float prevScale = mScale;
                mScale += scale;

                if (mScale < 0.1f) // Minimum scale condition:
                    mScale = 0.1f;

                if (mScale > 10f) // Maximum scale condition:
                    mScale = 10f;
                ScaleAnimation scaleAnimation = new ScaleAnimation(1f / prevScale, 1f / mScale, 1f / prevScale, 1f / mScale, detector.getFocusX(), detector.getFocusY());
                scaleAnimation.setDuration(0);
                scaleAnimation.setFillAfter(true);



                scroll.setLayoutParams(new LinearLayout.LayoutParams((int)detector.getCurrentSpanX() , (int)detector.getCurrentSpanY()));

                scroll.startAnimation(scaleAnimation);



                return true;
            }
        });*/


// step 3: override dispatchTouchEvent()


        //com.example.mukul.rekhta.ZoomView zv = new com.example.mukul.rekhta.ZoomView(MainActivity.this);






    }


    /*@Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        super.dispatchTouchEvent(event);
        mScaleDetector.onTouchEvent(event);
        gestureDetector.onTouchEvent(event);
        return gestureDetector.onTouchEvent(event);
    }
*/


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);


        return true;


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.en) {

            if (flag == 1) {

                content.removeAllViews();

                progress.setVisibility(View.VISIBLE);

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://14.140.111.4:20002")
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                AllAPIs cr = retrofit.create(AllAPIs.class);


                Call<dataBean> call = cr.getData("1", "827D3643-BCC4-410B-B1B9-00008EBCA797");

                call.enqueue(new Callback<dataBean>() {
                    @Override
                    public void onResponse(Call<dataBean> call, Response<dataBean> response) {

                        title.setText(response.body().getR().getCT());
                        title.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

                        author.setText(response.body().getR().getPoet().getPN());
                        author.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

                        String ddata = response.body().getR().getCR();

                        try {
                            JSONObject jsonObject = new JSONObject(ddata);


                            JSONArray paraArray = jsonObject.getJSONArray("P");

                            LinearLayout con = new LinearLayout(MainActivity.this);
                            con.setOrientation(LinearLayout.VERTICAL);
                            con.setGravity(Gravity.CENTER_HORIZONTAL);

                            for (int i = 0; i < paraArray.length(); i++) {


                                JSONObject pobj = paraArray.getJSONObject(i);
                                JSONArray lineArray = pobj.getJSONArray("L");

                                LinearLayout para = new LinearLayout(MainActivity.this);
                                para.setOrientation(LinearLayout.VERTICAL);
                                para.setGravity(Gravity.CENTER_HORIZONTAL);
                                para.setPadding(5, 10, 5, 10);

                                for (int j = 0; j < lineArray.length(); j++) {

                                    JSONObject lobj = lineArray.getJSONObject(j);

                                    JSONArray wordArray = lobj.getJSONArray("W");

                                    //String line = "";

                                    final LinearLayout line = new LinearLayout(MainActivity.this);
                                    line.setOrientation(LinearLayout.HORIZONTAL);
                                    line.setGravity(Gravity.CENTER_HORIZONTAL);
                                    line.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                                    line.setPadding(5, 10, 5, 10);

                                    for (int k = 0; k < wordArray.length(); k++) {

                                        JSONObject wordwrap = wordArray.getJSONObject(k);

                                        final String wo = wordwrap.getString("W");

                                        final TextView word = new TextView(MainActivity.this);
                                        word.setPadding(5, 0, 5, 0);
                                        word.setBackgroundColor(Color.TRANSPARENT);
                                        word.setText(wo);
                                        word.setTextSize(16);
                                        word.setTextColor(Color.BLACK);





                                        word.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                toast.setText("word: " + wo);
                                                toast.show();
                                            }
                                        });


                                        line.addView(word);

                                    }

                                    final int maxwidth = getWindowManager().getDefaultDisplay().getWidth();

                                    final int[] wordwidth = {0};

                                    Log.d("asdCount" , String.valueOf(line.getChildCount()));

                                    for (int m = 0 ; m < line.getChildCount() ; m++)
                                    {

                                        final View v = line.getChildAt(m);


                                        final int finalM = m;
                                        v.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                                            @Override
                                            public void onGlobalLayout() {
                                                v.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                                                wordwidth[0] = wordwidth[0] + v.getWidth();

                                                Log.d("asd" , String.valueOf(v.getWidth()));

                                                if (finalM == line.getChildCount()-1)
                                                {

                                                    int emptySpace = maxwidth - wordwidth[0];

                                                    Log.d("wordWidth" , String.valueOf(wordwidth[0]));

                                                    Log.d("empty" , String.valueOf(emptySpace));

                                                    float space = emptySpace / (line.getChildCount() + 1);

                                                    float netspace = space / 2;

                                                    for (int m = 0 ; m < line.getChildCount() ; m++)
                                                    {


                                                        View v = line.getChildAt(m);

                                                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                                                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                                                        layoutParams.setMargins((int)netspace , 0 , (int)netspace , 0);

                                                        //v.setPadding((int)netspace , 0 , (int)netspace , 0);

                                                        v.setLayoutParams(layoutParams);

                                                    }

//                            line.setPadding((int)netspace , 0 , (int)netspace , 0);



                                                }

                                            }
                                        });




                                    }

                                    Log.d("maxWidth" , String.valueOf(maxwidth));

                                    para.addView(line);


                                    Log.d("asdasd", line + "\n");

                                }

                                final int finalI = i;
                                para.setOnLongClickListener(new View.OnLongClickListener() {
                                    @Override
                                    public boolean onLongClick(View view) {

                                        toast.setText("para pressed: " + String.valueOf(finalI + 1));

                                        toast.show();
                                        return true;
                                    }
                                });

                                con.addView(para);
                                Space ap = new Space(MainActivity.this);
                                ap.setMinimumHeight(30);
                                con.addView(ap);
                                Log.d("asdasd", "\n");

                            }


                            content.addView(con);

                            progress.setVisibility(View.GONE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progress.setVisibility(View.GONE);
                        }


                        Log.d("asdasd", ddata);

                        String formattedData = ddata.replaceAll("\\/", ddata);

                        Log.d("asdasd", formattedData);

                    }

                    @Override
                    public void onFailure(Call<dataBean> call, Throwable throwable) {

                        progress.setVisibility(View.GONE);

                    }
                });

            } else if (flag == 2) {
                content.removeAllViews();
                progress.setVisibility(View.VISIBLE);

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://14.140.111.4:20002")
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                AllAPIs cr = retrofit.create(AllAPIs.class);


                Call<dataBean> call = cr.getData("1", "FC75E922-FD23-4510-B8AE-00263E506E1E");

                call.enqueue(new Callback<dataBean>() {
                    @Override
                    public void onResponse(Call<dataBean> call, Response<dataBean> response) {


                        try {

                            title.setText(response.body().getR().getCT());
                            title.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

                            author.setText(response.body().getR().getPoet().getPN());
                            author.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

                            String ddata = response.body().getR().getCR();

                            JSONObject jsonObject = new JSONObject(ddata);


                            JSONArray paraArray = jsonObject.getJSONArray("P");

                            LinearLayout con = new LinearLayout(MainActivity.this);
                            con.setOrientation(LinearLayout.VERTICAL);
                            con.setGravity(Gravity.START);

                            for (int i = 0; i < paraArray.length(); i++) {


                                JSONObject pobj = paraArray.getJSONObject(i);
                                JSONArray lineArray = pobj.getJSONArray("L");

                                LinearLayout para = new LinearLayout(MainActivity.this);
                                para.setOrientation(LinearLayout.VERTICAL);
                                para.setGravity(Gravity.START);
                                para.setPadding(5, 10, 5, 10);

                                for (int j = 0; j < lineArray.length(); j++) {

                                    JSONObject lobj = lineArray.getJSONObject(j);

                                    JSONArray wordArray = lobj.getJSONArray("W");

                                    //String line = "";

                                    final LinearLayout line = new LinearLayout(MainActivity.this);
                                    line.setOrientation(LinearLayout.HORIZONTAL);
                                    line.setGravity(Gravity.START);
                                    line.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                                    line.setPadding(5, 10, 5, 10);

                                    for (int k = 0; k < wordArray.length(); k++) {

                                        JSONObject wordwrap = wordArray.getJSONObject(k);

                                        final String wo = wordwrap.getString("W");

                                        final TextView word = new TextView(MainActivity.this);
                                        word.setPadding(5, 0, 5, 0);
                                        word.setBackgroundColor(Color.TRANSPARENT);
                                        word.setText(wo);
                                        word.setTextSize(16);
                                        word.setTextColor(Color.BLACK);





                                        word.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                toast.setText("word: " + wo);
                                                toast.show();
                                            }
                                        });


                                        line.addView(word);

                                    }

                                    final int maxwidth = getWindowManager().getDefaultDisplay().getWidth();

                                    final int[] wordwidth = {0};

                                    Log.d("asdCount" , String.valueOf(line.getChildCount()));

                                    for (int m = 0 ; m < line.getChildCount() ; m++)
                                    {

                                        final View v = line.getChildAt(m);


                                        final int finalM = m;
                                        v.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                                            @Override
                                            public void onGlobalLayout() {
                                                v.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                                                wordwidth[0] = wordwidth[0] + v.getWidth();

                                                Log.d("asd" , String.valueOf(v.getWidth()));

                                                if (finalM == line.getChildCount()-1)
                                                {

                                                    int emptySpace = maxwidth - wordwidth[0];

                                                    Log.d("wordWidth" , String.valueOf(wordwidth[0]));

                                                    Log.d("empty" , String.valueOf(emptySpace));

                                                    float space = emptySpace / (line.getChildCount() + 1);

                                                    float netspace = space / 2;

                                                    for (int m = 0 ; m < line.getChildCount() ; m++)
                                                    {


                                                        View v = line.getChildAt(m);

                                                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                                                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                                                        layoutParams.setMargins((int)netspace , 0 , (int)netspace , 0);

                                                        //v.setPadding((int)netspace , 0 , (int)netspace , 0);

                                                        v.setLayoutParams(layoutParams);

                                                    }

//                            line.setPadding((int)netspace , 0 , (int)netspace , 0);



                                                }

                                            }
                                        });




                                    }

                                    Log.d("maxWidth" , String.valueOf(maxwidth));


                                    para.addView(line);


                                    Log.d("asdasd", line + "\n");

                                }

                                final int finalI = i;
                                para.setOnLongClickListener(new View.OnLongClickListener() {
                                    @Override
                                    public boolean onLongClick(View view) {

                                        toast.setText("para pressed: " + String.valueOf(finalI + 1));

                                        toast.show();
                                        return true;
                                    }
                                });

                                con.addView(para);
                                Space ap = new Space(MainActivity.this);
                                ap.setMinimumHeight(30);
                                con.addView(ap);

                                Log.d("asdasd", "\n");

                            }


                            content.addView(con);

                            progress.setVisibility(View.GONE);

                            flag = 2;

                        } catch (Exception e) {
                            e.printStackTrace();
                            progress.setVisibility(View.GONE);
                        }


                    }

                    @Override
                    public void onFailure(Call<dataBean> call, Throwable throwable) {

                        progress.setVisibility(View.GONE);

                    }
                });
            } else if (flag == 3) {
                content.removeAllViews();
                progress.setVisibility(View.VISIBLE);

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://14.140.111.4:20002")
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                AllAPIs cr = retrofit.create(AllAPIs.class);


                Call<dataBean> call = cr.getData("1", "5566AE8C-00A1-4AAE-8CDFCA09DB728342");

                call.enqueue(new Callback<dataBean>() {
                    @Override
                    public void onResponse(Call<dataBean> call, Response<dataBean> response) {


                        try {

                            title.setText(response.body().getR().getCT());
                            title.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

                            author.setText(response.body().getR().getPoet().getPN());
                            author.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

                            String ddata = response.body().getR().getCR();


                            JSONObject jsonObject = new JSONObject(ddata);


                            JSONArray paraArray = jsonObject.getJSONArray("P");

                            LinearLayout con = new LinearLayout(MainActivity.this);
                            con.setOrientation(LinearLayout.VERTICAL);
                            con.setGravity(Gravity.CENTER_HORIZONTAL);

                            for (int i = 0; i < paraArray.length(); i++) {


                                JSONObject pobj = paraArray.getJSONObject(i);
                                JSONArray lineArray = pobj.getJSONArray("L");

                                LinearLayout para = new LinearLayout(MainActivity.this);
                                para.setOrientation(LinearLayout.VERTICAL);
                                para.setGravity(Gravity.CENTER_HORIZONTAL);
                                para.setPadding(5, 10, 5, 10);

                                for (int j = 0; j < lineArray.length(); j++) {

                                    JSONObject lobj = lineArray.getJSONObject(j);

                                    JSONArray wordArray = lobj.getJSONArray("W");

                                    //String line = "";

                                    final LinearLayout line = new LinearLayout(MainActivity.this);
                                    line.setOrientation(LinearLayout.HORIZONTAL);
                                    line.setGravity(Gravity.CENTER_HORIZONTAL);
                                    line.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                                    line.setPadding(5, 10, 5, 10);

                                    for (int k = 0; k < wordArray.length(); k++) {

                                        JSONObject wordwrap = wordArray.getJSONObject(k);

                                        final String wo = wordwrap.getString("W");

                                        final TextView word = new TextView(MainActivity.this);
                                        word.setPadding(5, 0, 5, 0);
                                        word.setBackgroundColor(Color.TRANSPARENT);
                                        word.setText(wo);
                                        word.setTextSize(16);
                                        word.setTextColor(Color.BLACK);





                                        word.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                toast.setText("word: " + wo);
                                                toast.show();
                                            }
                                        });


                                        line.addView(word);

                                    }

                                    final int maxwidth = getWindowManager().getDefaultDisplay().getWidth();

                                    final int[] wordwidth = {0};

                                    Log.d("asdCount" , String.valueOf(line.getChildCount()));

                                    for (int m = 0 ; m < line.getChildCount() ; m++)
                                    {

                                        final View v = line.getChildAt(m);


                                        final int finalM = m;
                                        v.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                                            @Override
                                            public void onGlobalLayout() {
                                                v.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                                                wordwidth[0] = wordwidth[0] + v.getWidth();

                                                Log.d("asd" , String.valueOf(v.getWidth()));

                                                if (finalM == line.getChildCount()-1)
                                                {

                                                    int emptySpace = maxwidth - wordwidth[0];

                                                    Log.d("wordWidth" , String.valueOf(wordwidth[0]));

                                                    Log.d("empty" , String.valueOf(emptySpace));

                                                    float space = emptySpace / (line.getChildCount() + 1);

                                                    float netspace = space / 2;

                                                    for (int m = 0 ; m < line.getChildCount() ; m++)
                                                    {


                                                        View v = line.getChildAt(m);

                                                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                                                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                                                        layoutParams.setMargins((int)netspace , 0 , (int)netspace , 0);

                                                        //v.setPadding((int)netspace , 0 , (int)netspace , 0);

                                                        v.setLayoutParams(layoutParams);

                                                    }

//                            line.setPadding((int)netspace , 0 , (int)netspace , 0);



                                                }

                                            }
                                        });




                                    }

                                    Log.d("maxWidth" , String.valueOf(maxwidth));


                                    para.addView(line);


                                    Log.d("asdasd", line + "\n");

                                }

                                final int finalI = i;
                                para.setOnLongClickListener(new View.OnLongClickListener() {
                                    @Override
                                    public boolean onLongClick(View view) {

                                        toast.setText("para pressed: " + String.valueOf(finalI + 1));

                                        toast.show();
                                        return true;
                                    }
                                });

                                con.addView(para);
                                Space ap = new Space(MainActivity.this);
                                ap.setMinimumHeight(30);
                                con.addView(ap);

                                Log.d("asdasd", "\n");

                            }


                            content.addView(con);

                            progress.setVisibility(View.GONE);

                            flag = 3;

                        } catch (Exception e) {
                            e.printStackTrace();
                            progress.setVisibility(View.GONE);
                        }


                    }

                    @Override
                    public void onFailure(Call<dataBean> call, Throwable throwable) {

                        progress.setVisibility(View.GONE);

                    }
                });

            } else if (flag == 4) {
                content.removeAllViews();
                progress.setVisibility(View.VISIBLE);

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://14.140.111.4:20002")
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                AllAPIs cr = retrofit.create(AllAPIs.class);


                Call<dataBean> call = cr.getData("1", "CE328811-2C4C-42D1-B685-A9ADC9D97EF3");

                call.enqueue(new Callback<dataBean>() {
                    @Override
                    public void onResponse(Call<dataBean> call, Response<dataBean> response) {


                        try {

                            title.setText(response.body().getR().getCT());
                            title.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

                            author.setText(response.body().getR().getPoet().getPN());
                            author.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

                            String ddata = response.body().getR().getCR();


                            JSONObject jsonObject = new JSONObject(ddata);


                            JSONArray paraArray = jsonObject.getJSONArray("P");

                            LinearLayout con = new LinearLayout(MainActivity.this);
                            con.setOrientation(LinearLayout.VERTICAL);
                            con.setGravity(Gravity.END);

                            for (int i = 0; i < paraArray.length(); i++) {


                                JSONObject pobj = paraArray.getJSONObject(i);
                                JSONArray lineArray = pobj.getJSONArray("L");

                                LinearLayout para = new LinearLayout(MainActivity.this);
                                para.setOrientation(LinearLayout.VERTICAL);
                                para.setGravity(Gravity.END);
                                para.setPadding(5, 10, 5, 10);

                                for (int j = 0; j < lineArray.length(); j++) {

                                    JSONObject lobj = lineArray.getJSONObject(j);

                                    JSONArray wordArray = lobj.getJSONArray("W");

                                    //String line = "";

                                    final LinearLayout line = new LinearLayout(MainActivity.this);
                                    line.setOrientation(LinearLayout.HORIZONTAL);
                                    line.setGravity(Gravity.END);
                                    line.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                                    line.setPadding(5, 10, 5, 10);

                                    for (int k = 0; k < wordArray.length(); k++) {

                                        JSONObject wordwrap = wordArray.getJSONObject(k);

                                        final String wo = wordwrap.getString("W");

                                        final TextView word = new TextView(MainActivity.this);
                                        word.setPadding(5, 0, 5, 0);
                                        word.setBackgroundColor(Color.TRANSPARENT);
                                        word.setText(wo);
                                        word.setTextSize(16);
                                        word.setTextColor(Color.BLACK);





                                        word.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                toast.setText("word: " + wo);
                                                toast.show();
                                            }
                                        });


                                        line.addView(word);

                                    }

                                    final int maxwidth = getWindowManager().getDefaultDisplay().getWidth();

                                    final int[] wordwidth = {0};

                                    Log.d("asdCount" , String.valueOf(line.getChildCount()));

                                    for (int m = 0 ; m < line.getChildCount() ; m++)
                                    {

                                        final View v = line.getChildAt(m);


                                        final int finalM = m;
                                        v.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                                            @Override
                                            public void onGlobalLayout() {
                                                v.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                                                wordwidth[0] = wordwidth[0] + v.getWidth();

                                                Log.d("asd" , String.valueOf(v.getWidth()));

                                                if (finalM == line.getChildCount()-1)
                                                {

                                                    int emptySpace = maxwidth - wordwidth[0];

                                                    Log.d("wordWidth" , String.valueOf(wordwidth[0]));

                                                    Log.d("empty" , String.valueOf(emptySpace));

                                                    float space = emptySpace / (line.getChildCount() + 1);

                                                    float netspace = space / 2;

                                                    for (int m = 0 ; m < line.getChildCount() ; m++)
                                                    {


                                                        View v = line.getChildAt(m);

                                                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                                                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                                                        layoutParams.setMargins((int)netspace , 0 , (int)netspace , 0);

                                                        //v.setPadding((int)netspace , 0 , (int)netspace , 0);

                                                        v.setLayoutParams(layoutParams);

                                                    }

//                            line.setPadding((int)netspace , 0 , (int)netspace , 0);



                                                }

                                            }
                                        });




                                    }

                                    Log.d("maxWidth" , String.valueOf(maxwidth));


                                    para.addView(line);


                                    Log.d("asdasd", line + "\n");

                                }

                                final int finalI = i;
                                para.setOnLongClickListener(new View.OnLongClickListener() {
                                    @Override
                                    public boolean onLongClick(View view) {

                                        toast.setText("para pressed: " + String.valueOf(finalI + 1));

                                        toast.show();
                                        return true;
                                    }
                                });

                                con.addView(para);
                                Space ap = new Space(MainActivity.this);
                                ap.setMinimumHeight(30);
                                con.addView(ap);
                                Log.d("asdasd", "\n");

                            }


                            content.addView(con);

                            progress.setVisibility(View.GONE);

                            flag = 4;

                        } catch (Exception e) {
                            e.printStackTrace();
                            progress.setVisibility(View.GONE);
                        }


                    }

                    @Override
                    public void onFailure(Call<dataBean> call, Throwable throwable) {

                        progress.setVisibility(View.GONE);

                    }
                });
            } else if (flag == 5) {
                content.removeAllViews();
                progress.setVisibility(View.VISIBLE);

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://14.140.111.4:20002")
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                AllAPIs cr = retrofit.create(AllAPIs.class);


                Call<dataBean> call = cr.getData("1", "B86C1FBE-2C5A-4A75-BFCE-007580A1451D");

                call.enqueue(new Callback<dataBean>() {
                    @Override
                    public void onResponse(Call<dataBean> call, Response<dataBean> response) {

                        try {

                            title.setText(response.body().getR().getCT());
                            title.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

                            author.setText(response.body().getR().getPoet().getPN());
                            author.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

                            String ddata = response.body().getR().getCR();

                            JSONObject jsonObject = new JSONObject(ddata);


                            JSONArray paraArray = jsonObject.getJSONArray("P");

                            LinearLayout con = new LinearLayout(MainActivity.this);
                            con.setOrientation(LinearLayout.VERTICAL);
                            con.setGravity(Gravity.START);

                            for (int i = 0; i < paraArray.length(); i++) {


                                JSONObject pobj = paraArray.getJSONObject(i);
                                JSONArray lineArray = pobj.getJSONArray("L");

                                LinearLayout para = new LinearLayout(MainActivity.this);
                                para.setOrientation(LinearLayout.VERTICAL);
                                para.setGravity(Gravity.START);
                                para.setPadding(5, 10, 5, 10);

                                for (int j = 0; j < lineArray.length(); j++) {

                                    JSONObject lobj = lineArray.getJSONObject(j);

                                    JSONArray wordArray = lobj.getJSONArray("W");

                                    //String line = "";

                                    final LinearLayout line = new LinearLayout(MainActivity.this);
                                    line.setOrientation(LinearLayout.HORIZONTAL);
                                    line.setGravity(Gravity.START);
                                    line.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                                    line.setPadding(5, 10, 5, 10);

                                    for (int k = 0; k < wordArray.length(); k++) {

                                        JSONObject wordwrap = wordArray.getJSONObject(k);

                                        final String wo = wordwrap.getString("W");

                                        final TextView word = new TextView(MainActivity.this);
                                        word.setPadding(5, 0, 5, 0);
                                        word.setBackgroundColor(Color.TRANSPARENT);
                                        word.setText(wo);
                                        word.setTextSize(16);
                                        word.setTextColor(Color.BLACK);





                                        word.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                toast.setText("word: " + wo);
                                                toast.show();
                                            }
                                        });


                                        line.addView(word);

                                    }

                                    final int maxwidth = getWindowManager().getDefaultDisplay().getWidth();

                                    final int[] wordwidth = {0};

                                    Log.d("asdCount" , String.valueOf(line.getChildCount()));

                                    for (int m = 0 ; m < line.getChildCount() ; m++)
                                    {

                                        final View v = line.getChildAt(m);


                                        final int finalM = m;
                                        v.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                                            @Override
                                            public void onGlobalLayout() {
                                                v.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                                                wordwidth[0] = wordwidth[0] + v.getWidth();

                                                Log.d("asd" , String.valueOf(v.getWidth()));

                                                if (finalM == line.getChildCount()-1)
                                                {

                                                    int emptySpace = maxwidth - wordwidth[0];

                                                    Log.d("wordWidth" , String.valueOf(wordwidth[0]));

                                                    Log.d("empty" , String.valueOf(emptySpace));

                                                    float space = emptySpace / (line.getChildCount() + 1);

                                                    float netspace = space / 2;

                                                    for (int m = 0 ; m < line.getChildCount() ; m++)
                                                    {


                                                        View v = line.getChildAt(m);

                                                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                                                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                                                        layoutParams.setMargins((int)netspace , 0 , (int)netspace , 0);

                                                        //v.setPadding((int)netspace , 0 , (int)netspace , 0);

                                                        v.setLayoutParams(layoutParams);

                                                    }

//                            line.setPadding((int)netspace , 0 , (int)netspace , 0);



                                                }

                                            }
                                        });




                                    }

                                    Log.d("maxWidth" , String.valueOf(maxwidth));


                                    para.addView(line);


                                    Log.d("asdasd", line + "\n");

                                }

                                final int finalI = i;
                                para.setOnLongClickListener(new View.OnLongClickListener() {
                                    @Override
                                    public boolean onLongClick(View view) {

                                        toast.setText("para pressed: " + String.valueOf(finalI + 1));

                                        toast.show();
                                        return true;
                                    }
                                });

                                con.addView(para);
                                Space ap = new Space(MainActivity.this);
                                ap.setMinimumHeight(30);
                                con.addView(ap);
                                Log.d("asdasd", "\n");

                            }


                            content.addView(con);

                            progress.setVisibility(View.GONE);

                            flag = 5;

                        } catch (Exception e) {
                            e.printStackTrace();
                            progress.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onFailure(Call<dataBean> call, Throwable throwable) {

                        progress.setVisibility(View.GONE);

                    }
                });
            } else if (flag == 6) {
                content.removeAllViews();
                progress.setVisibility(View.VISIBLE);

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://14.140.111.4:20002")
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                AllAPIs cr = retrofit.create(AllAPIs.class);


                Call<dataBean> call = cr.getData("1", "EEBC8F15-6904-49A9-BFE4-6E76E71E28BB");

                call.enqueue(new Callback<dataBean>() {
                    @Override
                    public void onResponse(Call<dataBean> call, Response<dataBean> response) {


                        try {

                            title.setText(response.body().getR().getCT());
                            title.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

                            author.setText(response.body().getR().getPoet().getPN());
                            author.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

                            String ddata = response.body().getR().getCR();

                            JSONObject jsonObject = new JSONObject(ddata);


                            JSONArray paraArray = jsonObject.getJSONArray("P");

                            LinearLayout con = new LinearLayout(MainActivity.this);
                            con.setOrientation(LinearLayout.VERTICAL);
                            con.setGravity(Gravity.CENTER_HORIZONTAL);

                            for (int i = 0; i < paraArray.length(); i++) {


                                JSONObject pobj = paraArray.getJSONObject(i);
                                JSONArray lineArray = pobj.getJSONArray("L");

                                LinearLayout para = new LinearLayout(MainActivity.this);
                                para.setOrientation(LinearLayout.VERTICAL);
                                para.setGravity(Gravity.CENTER_HORIZONTAL);
                                para.setPadding(5, 10, 5, 10);

                                for (int j = 0; j < lineArray.length(); j++) {

                                    JSONObject lobj = lineArray.getJSONObject(j);

                                    JSONArray wordArray = lobj.getJSONArray("W");

                                    //String line = "";

                                    final LinearLayout line = new LinearLayout(MainActivity.this);
                                    line.setOrientation(LinearLayout.HORIZONTAL);
                                    line.setGravity(Gravity.CENTER_HORIZONTAL);
                                    line.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                                    line.setPadding(5, 10, 5, 10);

                                    for (int k = 0; k < wordArray.length(); k++) {

                                        JSONObject wordwrap = wordArray.getJSONObject(k);

                                        final String wo = wordwrap.getString("W");

                                        final TextView word = new TextView(MainActivity.this);
                                        word.setPadding(5, 0, 5, 0);
                                        word.setBackgroundColor(Color.TRANSPARENT);
                                        word.setText(wo);
                                        word.setTextSize(16);
                                        word.setTextColor(Color.BLACK);





                                        word.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                toast.setText("word: " + wo);
                                                toast.show();
                                            }
                                        });


                                        line.addView(word);

                                    }

                                    final int maxwidth = getWindowManager().getDefaultDisplay().getWidth();

                                    final int[] wordwidth = {0};

                                    Log.d("asdCount" , String.valueOf(line.getChildCount()));

                                    for (int m = 0 ; m < line.getChildCount() ; m++)
                                    {

                                        final View v = line.getChildAt(m);


                                        final int finalM = m;
                                        v.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                                            @Override
                                            public void onGlobalLayout() {
                                                v.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                                                wordwidth[0] = wordwidth[0] + v.getWidth();

                                                Log.d("asd" , String.valueOf(v.getWidth()));

                                                if (finalM == line.getChildCount()-1)
                                                {

                                                    int emptySpace = maxwidth - wordwidth[0];

                                                    Log.d("wordWidth" , String.valueOf(wordwidth[0]));

                                                    Log.d("empty" , String.valueOf(emptySpace));

                                                    float space = emptySpace / (line.getChildCount() + 1);

                                                    float netspace = space / 2;

                                                    for (int m = 0 ; m < line.getChildCount() ; m++)
                                                    {


                                                        View v = line.getChildAt(m);

                                                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                                                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                                                        layoutParams.setMargins((int)netspace , 0 , (int)netspace , 0);

                                                        //v.setPadding((int)netspace , 0 , (int)netspace , 0);

                                                        v.setLayoutParams(layoutParams);

                                                    }

//                            line.setPadding((int)netspace , 0 , (int)netspace , 0);



                                                }

                                            }
                                        });




                                    }

                                    Log.d("maxWidth" , String.valueOf(maxwidth));


                                    para.addView(line);


                                    Log.d("asdasd", line + "\n");

                                }

                                final int finalI = i;
                                para.setOnLongClickListener(new View.OnLongClickListener() {
                                    @Override
                                    public boolean onLongClick(View view) {

                                        toast.setText("para pressed: " + String.valueOf(finalI + 1));

                                        toast.show();
                                        return true;
                                    }
                                });

                                con.addView(para);
                                Space ap = new Space(MainActivity.this);
                                ap.setMinimumHeight(30);
                                con.addView(ap);
                                Log.d("asdasd", "\n");

                            }


                            content.addView(con);

                            progress.setVisibility(View.GONE);

                            flag = 6;

                        } catch (Exception e) {
                            e.printStackTrace();
                            progress.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onFailure(Call<dataBean> call, Throwable throwable) {

                        progress.setVisibility(View.GONE);

                    }
                });
            }


        } else if (id == R.id.hi) {

            if (flag == 1) {

                content.removeAllViews();

                progress.setVisibility(View.VISIBLE);

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://14.140.111.4:20002")
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                AllAPIs cr = retrofit.create(AllAPIs.class);


                Call<dataBean> call = cr.getData("2", "827D3643-BCC4-410B-B1B9-00008EBCA797");

                call.enqueue(new Callback<dataBean>() {
                    @Override
                    public void onResponse(Call<dataBean> call, Response<dataBean> response) {

                        title.setText(response.body().getR().getCT());
                        title.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

                        author.setText(response.body().getR().getPoet().getPN());
                        author.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

                        String ddata = response.body().getR().getCR();

                        try {
                            JSONObject jsonObject = new JSONObject(ddata);


                            JSONArray paraArray = jsonObject.getJSONArray("P");

                            LinearLayout con = new LinearLayout(MainActivity.this);
                            con.setOrientation(LinearLayout.VERTICAL);
                            con.setGravity(Gravity.CENTER_HORIZONTAL);

                            for (int i = 0; i < paraArray.length(); i++) {


                                JSONObject pobj = paraArray.getJSONObject(i);
                                JSONArray lineArray = pobj.getJSONArray("L");

                                LinearLayout para = new LinearLayout(MainActivity.this);
                                para.setOrientation(LinearLayout.VERTICAL);
                                para.setGravity(Gravity.CENTER_HORIZONTAL);
                                para.setPadding(5, 10, 5, 10);

                                for (int j = 0; j < lineArray.length(); j++) {

                                    JSONObject lobj = lineArray.getJSONObject(j);

                                    JSONArray wordArray = lobj.getJSONArray("W");

                                    //String line = "";

                                    final LinearLayout line = new LinearLayout(MainActivity.this);
                                    line.setOrientation(LinearLayout.HORIZONTAL);
                                    line.setGravity(Gravity.CENTER_HORIZONTAL);
                                    line.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                                    line.setPadding(5, 10, 5, 10);

                                    for (int k = 0; k < wordArray.length(); k++) {

                                        JSONObject wordwrap = wordArray.getJSONObject(k);

                                        final String wo = wordwrap.getString("W");

                                        final TextView word = new TextView(MainActivity.this);
                                        word.setPadding(5, 0, 5, 0);
                                        word.setBackgroundColor(Color.TRANSPARENT);
                                        word.setText(wo);
                                        word.setTextSize(16);
                                        word.setTextColor(Color.BLACK);





                                        word.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                toast.setText("word: " + wo);
                                                toast.show();
                                            }
                                        });


                                        line.addView(word);

                                    }

                                    final int maxwidth = getWindowManager().getDefaultDisplay().getWidth();

                                    final int[] wordwidth = {0};

                                    Log.d("asdCount" , String.valueOf(line.getChildCount()));

                                    for (int m = 0 ; m < line.getChildCount() ; m++)
                                    {

                                        final View v = line.getChildAt(m);


                                        final int finalM = m;
                                        v.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                                            @Override
                                            public void onGlobalLayout() {
                                                v.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                                                wordwidth[0] = wordwidth[0] + v.getWidth();

                                                Log.d("asd" , String.valueOf(v.getWidth()));

                                                if (finalM == line.getChildCount()-1)
                                                {

                                                    int emptySpace = maxwidth - wordwidth[0];

                                                    Log.d("wordWidth" , String.valueOf(wordwidth[0]));

                                                    Log.d("empty" , String.valueOf(emptySpace));

                                                    float space = emptySpace / (line.getChildCount() + 1);

                                                    float netspace = space / 2;

                                                    for (int m = 0 ; m < line.getChildCount() ; m++)
                                                    {


                                                        View v = line.getChildAt(m);

                                                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                                                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                                                        layoutParams.setMargins((int)netspace , 0 , (int)netspace , 0);

                                                        //v.setPadding((int)netspace , 0 , (int)netspace , 0);

                                                        v.setLayoutParams(layoutParams);

                                                    }

//                            line.setPadding((int)netspace , 0 , (int)netspace , 0);



                                                }

                                            }
                                        });




                                    }

                                    Log.d("maxWidth" , String.valueOf(maxwidth));


                                    para.addView(line);


                                    Log.d("asdasd", line + "\n");

                                }

                                final int finalI = i;
                                para.setOnLongClickListener(new View.OnLongClickListener() {
                                    @Override
                                    public boolean onLongClick(View view) {

                                        toast.setText("para pressed: " + String.valueOf(finalI + 1));

                                        toast.show();
                                        return true;
                                    }
                                });

                                con.addView(para);
                                Space ap = new Space(MainActivity.this);
                                ap.setMinimumHeight(30);
                                con.addView(ap);
                                Log.d("asdasd", "\n");

                            }


                            content.addView(con);

                            progress.setVisibility(View.GONE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progress.setVisibility(View.GONE);
                        }


                        Log.d("asdasd", ddata);

                        String formattedData = ddata.replaceAll("\\/", ddata);

                        Log.d("asdasd", formattedData);

                    }

                    @Override
                    public void onFailure(Call<dataBean> call, Throwable throwable) {

                        progress.setVisibility(View.GONE);

                    }
                });

            } else if (flag == 2) {
                content.removeAllViews();
                progress.setVisibility(View.VISIBLE);

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://14.140.111.4:20002")
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                AllAPIs cr = retrofit.create(AllAPIs.class);


                Call<dataBean> call = cr.getData("2", "FC75E922-FD23-4510-B8AE-00263E506E1E");

                call.enqueue(new Callback<dataBean>() {
                    @Override
                    public void onResponse(Call<dataBean> call, Response<dataBean> response) {


                        try {

                            title.setText(response.body().getR().getCT());
                            title.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

                            author.setText(response.body().getR().getPoet().getPN());
                            author.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

                            String ddata = response.body().getR().getCR();

                            JSONObject jsonObject = new JSONObject(ddata);


                            JSONArray paraArray = jsonObject.getJSONArray("P");

                            LinearLayout con = new LinearLayout(MainActivity.this);
                            con.setOrientation(LinearLayout.VERTICAL);
                            con.setGravity(Gravity.START);

                            for (int i = 0; i < paraArray.length(); i++) {


                                JSONObject pobj = paraArray.getJSONObject(i);
                                JSONArray lineArray = pobj.getJSONArray("L");

                                LinearLayout para = new LinearLayout(MainActivity.this);
                                para.setOrientation(LinearLayout.VERTICAL);
                                para.setGravity(Gravity.START);
                                para.setPadding(5, 10, 5, 10);

                                for (int j = 0; j < lineArray.length(); j++) {

                                    JSONObject lobj = lineArray.getJSONObject(j);

                                    JSONArray wordArray = lobj.getJSONArray("W");

                                    //String line = "";

                                    final LinearLayout line = new LinearLayout(MainActivity.this);
                                    line.setOrientation(LinearLayout.HORIZONTAL);
                                    line.setGravity(Gravity.START);
                                    line.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                                    line.setPadding(5, 10, 5, 10);

                                    for (int k = 0; k < wordArray.length(); k++) {

                                        JSONObject wordwrap = wordArray.getJSONObject(k);

                                        final String wo = wordwrap.getString("W");

                                        final TextView word = new TextView(MainActivity.this);
                                        word.setPadding(5, 0, 5, 0);
                                        word.setBackgroundColor(Color.TRANSPARENT);
                                        word.setText(wo);
                                        word.setTextSize(16);
                                        word.setTextColor(Color.BLACK);





                                        word.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                toast.setText("word: " + wo);
                                                toast.show();
                                            }
                                        });


                                        line.addView(word);

                                    }

                                    final int maxwidth = getWindowManager().getDefaultDisplay().getWidth();

                                    final int[] wordwidth = {0};

                                    Log.d("asdCount" , String.valueOf(line.getChildCount()));

                                    for (int m = 0 ; m < line.getChildCount() ; m++)
                                    {

                                        final View v = line.getChildAt(m);


                                        final int finalM = m;
                                        v.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                                            @Override
                                            public void onGlobalLayout() {
                                                v.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                                                wordwidth[0] = wordwidth[0] + v.getWidth();

                                                Log.d("asd" , String.valueOf(v.getWidth()));

                                                if (finalM == line.getChildCount()-1)
                                                {

                                                    int emptySpace = maxwidth - wordwidth[0];

                                                    Log.d("wordWidth" , String.valueOf(wordwidth[0]));

                                                    Log.d("empty" , String.valueOf(emptySpace));

                                                    float space = emptySpace / (line.getChildCount() + 1);

                                                    float netspace = space / 2;

                                                    for (int m = 0 ; m < line.getChildCount() ; m++)
                                                    {


                                                        View v = line.getChildAt(m);

                                                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                                                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                                                        layoutParams.setMargins((int)netspace , 0 , (int)netspace , 0);

                                                        //v.setPadding((int)netspace , 0 , (int)netspace , 0);

                                                        v.setLayoutParams(layoutParams);

                                                    }

//                            line.setPadding((int)netspace , 0 , (int)netspace , 0);



                                                }

                                            }
                                        });




                                    }

                                    Log.d("maxWidth" , String.valueOf(maxwidth));

                                    para.addView(line);


                                    Log.d("asdasd", line + "\n");

                                }

                                final int finalI = i;
                                para.setOnLongClickListener(new View.OnLongClickListener() {
                                    @Override
                                    public boolean onLongClick(View view) {

                                        toast.setText("para pressed: " + String.valueOf(finalI + 1));

                                        toast.show();
                                        return true;
                                    }
                                });

                                con.addView(para);
                                Space ap = new Space(MainActivity.this);
                                ap.setMinimumHeight(30);
                                con.addView(ap);
                                Log.d("asdasd", "\n");

                            }


                            content.addView(con);

                            progress.setVisibility(View.GONE);

                            flag = 2;

                        } catch (Exception e) {
                            e.printStackTrace();
                            progress.setVisibility(View.GONE);
                        }


                    }

                    @Override
                    public void onFailure(Call<dataBean> call, Throwable throwable) {

                        progress.setVisibility(View.GONE);

                    }
                });
            } else if (flag == 3) {
                content.removeAllViews();
                progress.setVisibility(View.VISIBLE);

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://14.140.111.4:20002")
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                AllAPIs cr = retrofit.create(AllAPIs.class);


                Call<dataBean> call = cr.getData("2", "5566AE8C-00A1-4AAE-8CDFCA09DB728342");

                call.enqueue(new Callback<dataBean>() {
                    @Override
                    public void onResponse(Call<dataBean> call, Response<dataBean> response) {


                        try {

                            title.setText(response.body().getR().getCT());
                            title.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

                            author.setText(response.body().getR().getPoet().getPN());
                            author.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

                            String ddata = response.body().getR().getCR();


                            JSONObject jsonObject = new JSONObject(ddata);


                            JSONArray paraArray = jsonObject.getJSONArray("P");

                            LinearLayout con = new LinearLayout(MainActivity.this);
                            con.setOrientation(LinearLayout.VERTICAL);
                            con.setGravity(Gravity.CENTER_HORIZONTAL);

                            for (int i = 0; i < paraArray.length(); i++) {


                                JSONObject pobj = paraArray.getJSONObject(i);
                                JSONArray lineArray = pobj.getJSONArray("L");

                                LinearLayout para = new LinearLayout(MainActivity.this);
                                para.setOrientation(LinearLayout.VERTICAL);
                                para.setGravity(Gravity.CENTER_HORIZONTAL);
                                para.setPadding(5, 10, 5, 10);

                                for (int j = 0; j < lineArray.length(); j++) {

                                    JSONObject lobj = lineArray.getJSONObject(j);

                                    JSONArray wordArray = lobj.getJSONArray("W");

                                    //String line = "";

                                    final LinearLayout line = new LinearLayout(MainActivity.this);
                                    line.setOrientation(LinearLayout.HORIZONTAL);
                                    line.setGravity(Gravity.CENTER_HORIZONTAL);
                                    line.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                                    line.setPadding(5, 10, 5, 10);

                                    for (int k = 0; k < wordArray.length(); k++) {

                                        JSONObject wordwrap = wordArray.getJSONObject(k);

                                        final String wo = wordwrap.getString("W");

                                        final TextView word = new TextView(MainActivity.this);
                                        word.setPadding(5, 0, 5, 0);
                                        word.setBackgroundColor(Color.TRANSPARENT);
                                        word.setText(wo);
                                        word.setTextSize(16);
                                        word.setTextColor(Color.BLACK);





                                        word.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                toast.setText("word: " + wo);
                                                toast.show();
                                            }
                                        });


                                        line.addView(word);

                                    }

                                    final int maxwidth = getWindowManager().getDefaultDisplay().getWidth();

                                    final int[] wordwidth = {0};

                                    Log.d("asdCount" , String.valueOf(line.getChildCount()));

                                    for (int m = 0 ; m < line.getChildCount() ; m++)
                                    {

                                        final View v = line.getChildAt(m);


                                        final int finalM = m;
                                        v.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                                            @Override
                                            public void onGlobalLayout() {
                                                v.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                                                wordwidth[0] = wordwidth[0] + v.getWidth();

                                                Log.d("asd" , String.valueOf(v.getWidth()));

                                                if (finalM == line.getChildCount()-1)
                                                {

                                                    int emptySpace = maxwidth - wordwidth[0];

                                                    Log.d("wordWidth" , String.valueOf(wordwidth[0]));

                                                    Log.d("empty" , String.valueOf(emptySpace));

                                                    float space = emptySpace / (line.getChildCount() + 1);

                                                    float netspace = space / 2;

                                                    for (int m = 0 ; m < line.getChildCount() ; m++)
                                                    {


                                                        View v = line.getChildAt(m);

                                                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                                                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                                                        layoutParams.setMargins((int)netspace , 0 , (int)netspace , 0);

                                                        //v.setPadding((int)netspace , 0 , (int)netspace , 0);

                                                        v.setLayoutParams(layoutParams);

                                                    }

//                            line.setPadding((int)netspace , 0 , (int)netspace , 0);



                                                }

                                            }
                                        });




                                    }

                                    Log.d("maxWidth" , String.valueOf(maxwidth));

                                    para.addView(line);


                                    Log.d("asdasd", line + "\n");

                                }

                                final int finalI = i;
                                para.setOnLongClickListener(new View.OnLongClickListener() {
                                    @Override
                                    public boolean onLongClick(View view) {

                                        toast.setText("para pressed: " + String.valueOf(finalI + 1));

                                        toast.show();
                                        return true;
                                    }
                                });

                                con.addView(para);
                                Space ap = new Space(MainActivity.this);
                                ap.setMinimumHeight(30);
                                con.addView(ap);
                                Log.d("asdasd", "\n");

                            }


                            content.addView(con);

                            progress.setVisibility(View.GONE);

                            flag = 3;

                        } catch (Exception e) {
                            e.printStackTrace();
                            progress.setVisibility(View.GONE);
                        }


                    }

                    @Override
                    public void onFailure(Call<dataBean> call, Throwable throwable) {

                        progress.setVisibility(View.GONE);

                    }
                });

            } else if (flag == 4) {
                content.removeAllViews();
                progress.setVisibility(View.VISIBLE);

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://14.140.111.4:20002")
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                AllAPIs cr = retrofit.create(AllAPIs.class);


                Call<dataBean> call = cr.getData("2", "CE328811-2C4C-42D1-B685-A9ADC9D97EF3");

                call.enqueue(new Callback<dataBean>() {
                    @Override
                    public void onResponse(Call<dataBean> call, Response<dataBean> response) {


                        try {

                            title.setText(response.body().getR().getCT());
                            title.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

                            author.setText(response.body().getR().getPoet().getPN());
                            author.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

                            String ddata = response.body().getR().getCR();


                            JSONObject jsonObject = new JSONObject(ddata);


                            JSONArray paraArray = jsonObject.getJSONArray("P");

                            LinearLayout con = new LinearLayout(MainActivity.this);
                            con.setOrientation(LinearLayout.VERTICAL);
                            con.setGravity(Gravity.END);

                            for (int i = 0; i < paraArray.length(); i++) {


                                JSONObject pobj = paraArray.getJSONObject(i);
                                JSONArray lineArray = pobj.getJSONArray("L");

                                LinearLayout para = new LinearLayout(MainActivity.this);
                                para.setOrientation(LinearLayout.VERTICAL);
                                para.setGravity(Gravity.END);
                                para.setPadding(5, 10, 5, 10);

                                for (int j = 0; j < lineArray.length(); j++) {

                                    JSONObject lobj = lineArray.getJSONObject(j);

                                    JSONArray wordArray = lobj.getJSONArray("W");

                                    //String line = "";

                                    final LinearLayout line = new LinearLayout(MainActivity.this);
                                    line.setOrientation(LinearLayout.HORIZONTAL);
                                    line.setGravity(Gravity.END);
                                    line.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                                    line.setPadding(5, 10, 5, 10);

                                    for (int k = 0; k < wordArray.length(); k++) {

                                        JSONObject wordwrap = wordArray.getJSONObject(k);

                                        final String wo = wordwrap.getString("W");

                                        final TextView word = new TextView(MainActivity.this);
                                        word.setPadding(5, 0, 5, 0);
                                        word.setBackgroundColor(Color.TRANSPARENT);
                                        word.setText(wo);
                                        word.setTextSize(16);
                                        word.setTextColor(Color.BLACK);





                                        word.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                toast.setText("word: " + wo);
                                                toast.show();
                                            }
                                        });


                                        line.addView(word);

                                    }

                                    final int maxwidth = getWindowManager().getDefaultDisplay().getWidth();

                                    final int[] wordwidth = {0};

                                    Log.d("asdCount" , String.valueOf(line.getChildCount()));

                                    for (int m = 0 ; m < line.getChildCount() ; m++)
                                    {

                                        final View v = line.getChildAt(m);


                                        final int finalM = m;
                                        v.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                                            @Override
                                            public void onGlobalLayout() {
                                                v.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                                                wordwidth[0] = wordwidth[0] + v.getWidth();

                                                Log.d("asd" , String.valueOf(v.getWidth()));

                                                if (finalM == line.getChildCount()-1)
                                                {

                                                    int emptySpace = maxwidth - wordwidth[0];

                                                    Log.d("wordWidth" , String.valueOf(wordwidth[0]));

                                                    Log.d("empty" , String.valueOf(emptySpace));

                                                    float space = emptySpace / (line.getChildCount() + 1);

                                                    float netspace = space / 2;

                                                    for (int m = 0 ; m < line.getChildCount() ; m++)
                                                    {


                                                        View v = line.getChildAt(m);

                                                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                                                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                                                        layoutParams.setMargins((int)netspace , 0 , (int)netspace , 0);

                                                        //v.setPadding((int)netspace , 0 , (int)netspace , 0);

                                                        v.setLayoutParams(layoutParams);

                                                    }

//                            line.setPadding((int)netspace , 0 , (int)netspace , 0);



                                                }

                                            }
                                        });




                                    }

                                    Log.d("maxWidth" , String.valueOf(maxwidth));


                                    para.addView(line);


                                    Log.d("asdasd", line + "\n");

                                }

                                final int finalI = i;
                                para.setOnLongClickListener(new View.OnLongClickListener() {
                                    @Override
                                    public boolean onLongClick(View view) {

                                        toast.setText("para pressed: " + String.valueOf(finalI + 1));

                                        toast.show();
                                        return true;
                                    }
                                });

                                con.addView(para);
                                Space ap = new Space(MainActivity.this);
                                ap.setMinimumHeight(30);
                                con.addView(ap);
                                Log.d("asdasd", "\n");

                            }


                            content.addView(con);

                            progress.setVisibility(View.GONE);

                            flag = 4;

                        } catch (Exception e) {
                            e.printStackTrace();
                            progress.setVisibility(View.GONE);
                        }


                    }

                    @Override
                    public void onFailure(Call<dataBean> call, Throwable throwable) {

                        progress.setVisibility(View.GONE);

                    }
                });
            } else if (flag == 5) {
                content.removeAllViews();
                progress.setVisibility(View.VISIBLE);

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://14.140.111.4:20002")
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                AllAPIs cr = retrofit.create(AllAPIs.class);


                Call<dataBean> call = cr.getData("2", "B86C1FBE-2C5A-4A75-BFCE-007580A1451D");

                call.enqueue(new Callback<dataBean>() {
                    @Override
                    public void onResponse(Call<dataBean> call, Response<dataBean> response) {

                        try {

                            title.setText(response.body().getR().getCT());
                            title.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

                            author.setText(response.body().getR().getPoet().getPN());
                            author.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

                            String ddata = response.body().getR().getCR();

                            JSONObject jsonObject = new JSONObject(ddata);


                            JSONArray paraArray = jsonObject.getJSONArray("P");

                            LinearLayout con = new LinearLayout(MainActivity.this);
                            con.setOrientation(LinearLayout.VERTICAL);
                            con.setGravity(Gravity.START);

                            for (int i = 0; i < paraArray.length(); i++) {


                                JSONObject pobj = paraArray.getJSONObject(i);
                                JSONArray lineArray = pobj.getJSONArray("L");

                                LinearLayout para = new LinearLayout(MainActivity.this);
                                para.setOrientation(LinearLayout.VERTICAL);
                                para.setGravity(Gravity.START);
                                para.setPadding(5, 10, 5, 10);

                                for (int j = 0; j < lineArray.length(); j++) {

                                    JSONObject lobj = lineArray.getJSONObject(j);

                                    JSONArray wordArray = lobj.getJSONArray("W");

                                    //String line = "";

                                    final LinearLayout line = new LinearLayout(MainActivity.this);
                                    line.setOrientation(LinearLayout.HORIZONTAL);
                                    line.setGravity(Gravity.START);
                                    line.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                                    line.setPadding(5, 10, 5, 10);

                                    for (int k = 0; k < wordArray.length(); k++) {

                                        JSONObject wordwrap = wordArray.getJSONObject(k);

                                        final String wo = wordwrap.getString("W");

                                        final TextView word = new TextView(MainActivity.this);
                                        word.setPadding(5, 0, 5, 0);
                                        word.setBackgroundColor(Color.TRANSPARENT);
                                        word.setText(wo);
                                        word.setTextSize(16);
                                        word.setTextColor(Color.BLACK);





                                        word.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                toast.setText("word: " + wo);
                                                toast.show();
                                            }
                                        });


                                        line.addView(word);

                                    }

                                    final int maxwidth = getWindowManager().getDefaultDisplay().getWidth();

                                    final int[] wordwidth = {0};

                                    Log.d("asdCount" , String.valueOf(line.getChildCount()));

                                    for (int m = 0 ; m < line.getChildCount() ; m++)
                                    {

                                        final View v = line.getChildAt(m);


                                        final int finalM = m;
                                        v.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                                            @Override
                                            public void onGlobalLayout() {
                                                v.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                                                wordwidth[0] = wordwidth[0] + v.getWidth();

                                                Log.d("asd" , String.valueOf(v.getWidth()));

                                                if (finalM == line.getChildCount()-1)
                                                {

                                                    int emptySpace = maxwidth - wordwidth[0];

                                                    Log.d("wordWidth" , String.valueOf(wordwidth[0]));

                                                    Log.d("empty" , String.valueOf(emptySpace));

                                                    float space = emptySpace / (line.getChildCount() + 1);

                                                    float netspace = space / 2;

                                                    for (int m = 0 ; m < line.getChildCount() ; m++)
                                                    {


                                                        View v = line.getChildAt(m);

                                                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                                                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                                                        layoutParams.setMargins((int)netspace , 0 , (int)netspace , 0);

                                                        //v.setPadding((int)netspace , 0 , (int)netspace , 0);

                                                        v.setLayoutParams(layoutParams);

                                                    }

//                            line.setPadding((int)netspace , 0 , (int)netspace , 0);



                                                }

                                            }
                                        });




                                    }

                                    Log.d("maxWidth" , String.valueOf(maxwidth));


                                    para.addView(line);


                                    Log.d("asdasd", line + "\n");

                                }

                                final int finalI = i;
                                para.setOnLongClickListener(new View.OnLongClickListener() {
                                    @Override
                                    public boolean onLongClick(View view) {

                                        toast.setText("para pressed: " + String.valueOf(finalI + 1));

                                        toast.show();
                                        return true;
                                    }
                                });

                                con.addView(para);
                                Space ap = new Space(MainActivity.this);
                                ap.setMinimumHeight(30);
                                con.addView(ap);
                                Log.d("asdasd", "\n");

                            }


                            content.addView(con);

                            progress.setVisibility(View.GONE);

                            flag = 5;

                        } catch (Exception e) {
                            e.printStackTrace();
                            progress.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onFailure(Call<dataBean> call, Throwable throwable) {

                        progress.setVisibility(View.GONE);

                    }
                });
            } else if (flag == 6) {
                content.removeAllViews();
                progress.setVisibility(View.VISIBLE);

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://14.140.111.4:20002")
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                AllAPIs cr = retrofit.create(AllAPIs.class);


                Call<dataBean> call = cr.getData("2", "EEBC8F15-6904-49A9-BFE4-6E76E71E28BB");

                call.enqueue(new Callback<dataBean>() {
                    @Override
                    public void onResponse(Call<dataBean> call, Response<dataBean> response) {


                        try {

                            title.setText(response.body().getR().getCT());
                            title.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

                            author.setText(response.body().getR().getPoet().getPN());
                            author.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

                            String ddata = response.body().getR().getCR();

                            JSONObject jsonObject = new JSONObject(ddata);


                            JSONArray paraArray = jsonObject.getJSONArray("P");

                            LinearLayout con = new LinearLayout(MainActivity.this);
                            con.setOrientation(LinearLayout.VERTICAL);
                            con.setGravity(Gravity.CENTER_HORIZONTAL);

                            for (int i = 0; i < paraArray.length(); i++) {


                                JSONObject pobj = paraArray.getJSONObject(i);
                                JSONArray lineArray = pobj.getJSONArray("L");

                                LinearLayout para = new LinearLayout(MainActivity.this);
                                para.setOrientation(LinearLayout.VERTICAL);
                                para.setGravity(Gravity.CENTER_HORIZONTAL);
                                para.setPadding(5, 10, 5, 10);

                                for (int j = 0; j < lineArray.length(); j++) {

                                    JSONObject lobj = lineArray.getJSONObject(j);

                                    JSONArray wordArray = lobj.getJSONArray("W");

                                    //String line = "";

                                    final LinearLayout line = new LinearLayout(MainActivity.this);
                                    line.setOrientation(LinearLayout.HORIZONTAL);
                                    line.setGravity(Gravity.CENTER_HORIZONTAL);
                                    line.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                                    line.setPadding(5, 10, 5, 10);

                                    for (int k = 0; k < wordArray.length(); k++) {

                                        JSONObject wordwrap = wordArray.getJSONObject(k);

                                        final String wo = wordwrap.getString("W");

                                        final TextView word = new TextView(MainActivity.this);
                                        word.setPadding(5, 0, 5, 0);
                                        word.setBackgroundColor(Color.TRANSPARENT);
                                        word.setText(wo);
                                        word.setTextSize(16);
                                        word.setTextColor(Color.BLACK);





                                        word.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                toast.setText("word: " + wo);
                                                toast.show();
                                            }
                                        });


                                        line.addView(word);

                                    }

                                    final int maxwidth = getWindowManager().getDefaultDisplay().getWidth();

                                    final int[] wordwidth = {0};

                                    Log.d("asdCount" , String.valueOf(line.getChildCount()));

                                    for (int m = 0 ; m < line.getChildCount() ; m++)
                                    {

                                        final View v = line.getChildAt(m);


                                        final int finalM = m;
                                        v.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                                            @Override
                                            public void onGlobalLayout() {
                                                v.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                                                wordwidth[0] = wordwidth[0] + v.getWidth();

                                                Log.d("asd" , String.valueOf(v.getWidth()));

                                                if (finalM == line.getChildCount()-1)
                                                {

                                                    int emptySpace = maxwidth - wordwidth[0];

                                                    Log.d("wordWidth" , String.valueOf(wordwidth[0]));

                                                    Log.d("empty" , String.valueOf(emptySpace));

                                                    float space = emptySpace / (line.getChildCount() + 1);

                                                    float netspace = space / 2;

                                                    for (int m = 0 ; m < line.getChildCount() ; m++)
                                                    {


                                                        View v = line.getChildAt(m);

                                                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                                                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                                                        layoutParams.setMargins((int)netspace , 0 , (int)netspace , 0);

                                                        //v.setPadding((int)netspace , 0 , (int)netspace , 0);

                                                        v.setLayoutParams(layoutParams);

                                                    }

//                            line.setPadding((int)netspace , 0 , (int)netspace , 0);



                                                }

                                            }
                                        });




                                    }

                                    Log.d("maxWidth" , String.valueOf(maxwidth));


                                    para.addView(line);


                                    Log.d("asdasd", line + "\n");

                                }

                                final int finalI = i;
                                para.setOnLongClickListener(new View.OnLongClickListener() {
                                    @Override
                                    public boolean onLongClick(View view) {

                                        toast.setText("para pressed: " + String.valueOf(finalI + 1));

                                        toast.show();
                                        return true;
                                    }
                                });

                                con.addView(para);
                                Space ap = new Space(MainActivity.this);
                                ap.setMinimumHeight(30);
                                con.addView(ap);
                                Log.d("asdasd", "\n");

                            }


                            content.addView(con);

                            progress.setVisibility(View.GONE);

                            flag = 6;

                        } catch (Exception e) {
                            e.printStackTrace();
                            progress.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onFailure(Call<dataBean> call, Throwable throwable) {

                        progress.setVisibility(View.GONE);

                    }
                });
            }


        } else if (id == R.id.ur) {

            if (flag == 1) {


                content.removeAllViews();

                progress.setVisibility(View.VISIBLE);

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://14.140.111.4:20002")
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                AllAPIs cr = retrofit.create(AllAPIs.class);


                Call<dataBean> call = cr.getData("3", "827D3643-BCC4-410B-B1B9-00008EBCA797");

                call.enqueue(new Callback<dataBean>() {
                    @Override
                    public void onResponse(Call<dataBean> call, Response<dataBean> response) {

                        title.setText(response.body().getR().getCT());
                        title.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

                        author.setText(response.body().getR().getPoet().getPN());
                        author.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

                        String ddata = response.body().getR().getCR();

                        try {
                            JSONObject jsonObject = new JSONObject(ddata);


                            JSONArray paraArray = jsonObject.getJSONArray("P");

                            LinearLayout con = new LinearLayout(MainActivity.this);
                            con.setOrientation(LinearLayout.VERTICAL);
                            con.setGravity(Gravity.CENTER_HORIZONTAL);

                            for (int i = 0; i < paraArray.length(); i++) {


                                JSONObject pobj = paraArray.getJSONObject(i);
                                JSONArray lineArray = pobj.getJSONArray("L");

                                LinearLayout para = new LinearLayout(MainActivity.this);
                                para.setOrientation(LinearLayout.VERTICAL);
                                para.setGravity(Gravity.CENTER_HORIZONTAL);
                                para.setPadding(5, 10, 5, 10);

                                for (int j = 0; j < lineArray.length(); j++) {

                                    JSONObject lobj = lineArray.getJSONObject(j);

                                    JSONArray wordArray = lobj.getJSONArray("W");

                                    //String line = "";

                                    final LinearLayout line = new LinearLayout(MainActivity.this);
                                    line.setOrientation(LinearLayout.HORIZONTAL);
                                    line.setGravity(Gravity.CENTER_HORIZONTAL);
                                    line.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                                    line.setPadding(5, 10, 5, 10);

                                    for (int k = 0; k < wordArray.length(); k++) {

                                        JSONObject wordwrap = wordArray.getJSONObject(k);

                                        final String wo = wordwrap.getString("W");

                                        final TextView word = new TextView(MainActivity.this);
                                        word.setPadding(5, 0, 5, 0);
                                        word.setBackgroundColor(Color.TRANSPARENT);
                                        word.setText(wo);
                                        word.setTextSize(16);
                                        word.setTextColor(Color.BLACK);





                                        word.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                toast.setText("word: " + wo);
                                                toast.show();
                                            }
                                        });


                                        line.addView(word);

                                    }

                                    final int maxwidth = getWindowManager().getDefaultDisplay().getWidth();

                                    final int[] wordwidth = {0};

                                    Log.d("asdCount" , String.valueOf(line.getChildCount()));

                                    for (int m = 0 ; m < line.getChildCount() ; m++)
                                    {

                                        final View v = line.getChildAt(m);


                                        final int finalM = m;
                                        v.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                                            @Override
                                            public void onGlobalLayout() {
                                                v.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                                                wordwidth[0] = wordwidth[0] + v.getWidth();

                                                Log.d("asd" , String.valueOf(v.getWidth()));

                                                if (finalM == line.getChildCount()-1)
                                                {

                                                    int emptySpace = maxwidth - wordwidth[0];

                                                    Log.d("wordWidth" , String.valueOf(wordwidth[0]));

                                                    Log.d("empty" , String.valueOf(emptySpace));

                                                    float space = emptySpace / (line.getChildCount() + 1);

                                                    float netspace = space / 2;

                                                    for (int m = 0 ; m < line.getChildCount() ; m++)
                                                    {


                                                        View v = line.getChildAt(m);

                                                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                                                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                                                        layoutParams.setMargins((int)netspace , 0 , (int)netspace , 0);

                                                        //v.setPadding((int)netspace , 0 , (int)netspace , 0);

                                                        v.setLayoutParams(layoutParams);

                                                    }

//                            line.setPadding((int)netspace , 0 , (int)netspace , 0);



                                                }

                                            }
                                        });




                                    }

                                    Log.d("maxWidth" , String.valueOf(maxwidth));


                                    para.addView(line);


                                    Log.d("asdasd", line + "\n");

                                }

                                final int finalI = i;
                                para.setOnLongClickListener(new View.OnLongClickListener() {
                                    @Override
                                    public boolean onLongClick(View view) {

                                        toast.setText("para pressed: " + String.valueOf(finalI + 1));

                                        toast.show();
                                        return true;
                                    }
                                });

                                con.addView(para);
                                Space ap = new Space(MainActivity.this);
                                ap.setMinimumHeight(30);
                                con.addView(ap);
                                Log.d("asdasd", "\n");

                            }


                            content.addView(con);

                            progress.setVisibility(View.GONE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progress.setVisibility(View.GONE);
                        }


                        Log.d("asdasd", ddata);

                        String formattedData = ddata.replaceAll("\\/", ddata);

                        Log.d("asdasd", formattedData);

                    }

                    @Override
                    public void onFailure(Call<dataBean> call, Throwable throwable) {

                        progress.setVisibility(View.GONE);

                    }
                });
            } else if (flag == 2) {
                content.removeAllViews();
                progress.setVisibility(View.VISIBLE);

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://14.140.111.4:20002")
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                AllAPIs cr = retrofit.create(AllAPIs.class);


                Call<dataBean> call = cr.getData("3", "FC75E922-FD23-4510-B8AE-00263E506E1E");

                call.enqueue(new Callback<dataBean>() {
                    @Override
                    public void onResponse(Call<dataBean> call, Response<dataBean> response) {


                        try {

                            title.setText(response.body().getR().getCT());
                            title.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

                            author.setText(response.body().getR().getPoet().getPN());
                            author.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

                            String ddata = response.body().getR().getCR();

                            JSONObject jsonObject = new JSONObject(ddata);


                            JSONArray paraArray = jsonObject.getJSONArray("P");

                            LinearLayout con = new LinearLayout(MainActivity.this);
                            con.setOrientation(LinearLayout.VERTICAL);
                            con.setGravity(Gravity.START);

                            for (int i = 0; i < paraArray.length(); i++) {


                                JSONObject pobj = paraArray.getJSONObject(i);
                                JSONArray lineArray = pobj.getJSONArray("L");

                                LinearLayout para = new LinearLayout(MainActivity.this);
                                para.setOrientation(LinearLayout.VERTICAL);
                                para.setGravity(Gravity.START);
                                para.setPadding(5, 10, 5, 10);

                                for (int j = 0; j < lineArray.length(); j++) {

                                    JSONObject lobj = lineArray.getJSONObject(j);

                                    JSONArray wordArray = lobj.getJSONArray("W");

                                    //String line = "";

                                    final LinearLayout line = new LinearLayout(MainActivity.this);
                                    line.setOrientation(LinearLayout.HORIZONTAL);
                                    line.setGravity(Gravity.START);
                                    line.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                                    line.setPadding(5, 10, 5, 10);

                                    for (int k = 0; k < wordArray.length(); k++) {

                                        JSONObject wordwrap = wordArray.getJSONObject(k);

                                        final String wo = wordwrap.getString("W");

                                        final TextView word = new TextView(MainActivity.this);
                                        word.setPadding(5, 0, 5, 0);
                                        word.setBackgroundColor(Color.TRANSPARENT);
                                        word.setText(wo);
                                        word.setTextSize(16);
                                        word.setTextColor(Color.BLACK);





                                        word.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                toast.setText("word: " + wo);
                                                toast.show();
                                            }
                                        });


                                        line.addView(word);

                                    }

                                    final int maxwidth = getWindowManager().getDefaultDisplay().getWidth();

                                    final int[] wordwidth = {0};

                                    Log.d("asdCount" , String.valueOf(line.getChildCount()));

                                    for (int m = 0 ; m < line.getChildCount() ; m++)
                                    {

                                        final View v = line.getChildAt(m);


                                        final int finalM = m;
                                        v.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                                            @Override
                                            public void onGlobalLayout() {
                                                v.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                                                wordwidth[0] = wordwidth[0] + v.getWidth();

                                                Log.d("asd" , String.valueOf(v.getWidth()));

                                                if (finalM == line.getChildCount()-1)
                                                {

                                                    int emptySpace = maxwidth - wordwidth[0];

                                                    Log.d("wordWidth" , String.valueOf(wordwidth[0]));

                                                    Log.d("empty" , String.valueOf(emptySpace));

                                                    float space = emptySpace / (line.getChildCount() + 1);

                                                    float netspace = space / 2;

                                                    for (int m = 0 ; m < line.getChildCount() ; m++)
                                                    {


                                                        View v = line.getChildAt(m);

                                                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                                                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                                                        layoutParams.setMargins((int)netspace , 0 , (int)netspace , 0);

                                                        //v.setPadding((int)netspace , 0 , (int)netspace , 0);

                                                        v.setLayoutParams(layoutParams);

                                                    }

//                            line.setPadding((int)netspace , 0 , (int)netspace , 0);



                                                }

                                            }
                                        });




                                    }

                                    Log.d("maxWidth" , String.valueOf(maxwidth));


                                    para.addView(line);


                                    Log.d("asdasd", line + "\n");

                                }

                                final int finalI = i;
                                para.setOnLongClickListener(new View.OnLongClickListener() {
                                    @Override
                                    public boolean onLongClick(View view) {

                                        toast.setText("para pressed: " + String.valueOf(finalI + 1));

                                        toast.show();
                                        return true;
                                    }
                                });

                                con.addView(para);
                                Space ap = new Space(MainActivity.this);
                                ap.setMinimumHeight(30);
                                con.addView(ap);
                                Log.d("asdasd", "\n");

                            }


                            content.addView(con);

                            progress.setVisibility(View.GONE);

                            flag = 2;

                        } catch (Exception e) {
                            e.printStackTrace();
                            progress.setVisibility(View.GONE);
                        }


                    }

                    @Override
                    public void onFailure(Call<dataBean> call, Throwable throwable) {

                        progress.setVisibility(View.GONE);

                    }
                });
            } else if (flag == 3) {
                content.removeAllViews();
                progress.setVisibility(View.VISIBLE);

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://14.140.111.4:20002")
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                AllAPIs cr = retrofit.create(AllAPIs.class);


                Call<dataBean> call = cr.getData("3", "5566AE8C-00A1-4AAE-8CDFCA09DB728342");

                call.enqueue(new Callback<dataBean>() {
                    @Override
                    public void onResponse(Call<dataBean> call, Response<dataBean> response) {


                        try {

                            title.setText(response.body().getR().getCT());
                            title.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

                            author.setText(response.body().getR().getPoet().getPN());
                            author.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

                            String ddata = response.body().getR().getCR();


                            JSONObject jsonObject = new JSONObject(ddata);


                            JSONArray paraArray = jsonObject.getJSONArray("P");

                            LinearLayout con = new LinearLayout(MainActivity.this);
                            con.setOrientation(LinearLayout.VERTICAL);
                            con.setGravity(Gravity.CENTER_HORIZONTAL);

                            for (int i = 0; i < paraArray.length(); i++) {


                                JSONObject pobj = paraArray.getJSONObject(i);
                                JSONArray lineArray = pobj.getJSONArray("L");

                                LinearLayout para = new LinearLayout(MainActivity.this);
                                para.setOrientation(LinearLayout.VERTICAL);
                                para.setGravity(Gravity.CENTER_HORIZONTAL);
                                para.setPadding(5, 10, 5, 10);

                                for (int j = 0; j < lineArray.length(); j++) {

                                    JSONObject lobj = lineArray.getJSONObject(j);

                                    JSONArray wordArray = lobj.getJSONArray("W");

                                    //String line = "";

                                    final LinearLayout line = new LinearLayout(MainActivity.this);
                                    line.setOrientation(LinearLayout.HORIZONTAL);
                                    line.setGravity(Gravity.CENTER_HORIZONTAL);
                                    line.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                                    line.setPadding(5, 10, 5, 10);

                                    for (int k = 0; k < wordArray.length(); k++) {

                                        JSONObject wordwrap = wordArray.getJSONObject(k);

                                        final String wo = wordwrap.getString("W");

                                        final TextView word = new TextView(MainActivity.this);
                                        word.setPadding(5, 0, 5, 0);
                                        word.setBackgroundColor(Color.TRANSPARENT);
                                        word.setText(wo);
                                        word.setTextSize(16);
                                        word.setTextColor(Color.BLACK);





                                        word.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                toast.setText("word: " + wo);
                                                toast.show();
                                            }
                                        });


                                        line.addView(word);

                                    }

                                    final int maxwidth = getWindowManager().getDefaultDisplay().getWidth();

                                    final int[] wordwidth = {0};

                                    Log.d("asdCount" , String.valueOf(line.getChildCount()));

                                    for (int m = 0 ; m < line.getChildCount() ; m++)
                                    {

                                        final View v = line.getChildAt(m);


                                        final int finalM = m;
                                        v.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                                            @Override
                                            public void onGlobalLayout() {
                                                v.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                                                wordwidth[0] = wordwidth[0] + v.getWidth();

                                                Log.d("asd" , String.valueOf(v.getWidth()));

                                                if (finalM == line.getChildCount()-1)
                                                {

                                                    int emptySpace = maxwidth - wordwidth[0];

                                                    Log.d("wordWidth" , String.valueOf(wordwidth[0]));

                                                    Log.d("empty" , String.valueOf(emptySpace));

                                                    float space = emptySpace / (line.getChildCount() + 1);

                                                    float netspace = space / 2;

                                                    for (int m = 0 ; m < line.getChildCount() ; m++)
                                                    {


                                                        View v = line.getChildAt(m);

                                                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                                                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                                                        layoutParams.setMargins((int)netspace , 0 , (int)netspace , 0);

                                                        //v.setPadding((int)netspace , 0 , (int)netspace , 0);

                                                        v.setLayoutParams(layoutParams);

                                                    }

//                            line.setPadding((int)netspace , 0 , (int)netspace , 0);



                                                }

                                            }
                                        });




                                    }

                                    Log.d("maxWidth" , String.valueOf(maxwidth));


                                    para.addView(line);


                                    Log.d("asdasd", line + "\n");

                                }

                                final int finalI = i;
                                para.setOnLongClickListener(new View.OnLongClickListener() {
                                    @Override
                                    public boolean onLongClick(View view) {

                                        toast.setText("para pressed: " + String.valueOf(finalI + 1));

                                        toast.show();
                                        return true;
                                    }
                                });

                                con.addView(para);
                                Space ap = new Space(MainActivity.this);
                                ap.setMinimumHeight(30);
                                con.addView(ap);
                                Log.d("asdasd", "\n");

                            }


                            content.addView(con);

                            progress.setVisibility(View.GONE);

                            flag = 3;

                        } catch (Exception e) {
                            e.printStackTrace();
                            progress.setVisibility(View.GONE);
                        }


                    }

                    @Override
                    public void onFailure(Call<dataBean> call, Throwable throwable) {

                        progress.setVisibility(View.GONE);

                    }
                });

            } else if (flag == 4) {
                content.removeAllViews();
                progress.setVisibility(View.VISIBLE);

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://14.140.111.4:20002")
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                AllAPIs cr = retrofit.create(AllAPIs.class);


                Call<dataBean> call = cr.getData("3", "CE328811-2C4C-42D1-B685-A9ADC9D97EF3");

                call.enqueue(new Callback<dataBean>() {
                    @Override
                    public void onResponse(Call<dataBean> call, Response<dataBean> response) {


                        try {

                            title.setText(response.body().getR().getCT());
                            title.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

                            author.setText(response.body().getR().getPoet().getPN());
                            author.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

                            String ddata = response.body().getR().getCR();


                            JSONObject jsonObject = new JSONObject(ddata);


                            JSONArray paraArray = jsonObject.getJSONArray("P");

                            LinearLayout con = new LinearLayout(MainActivity.this);
                            con.setOrientation(LinearLayout.VERTICAL);
                            con.setGravity(Gravity.START);

                            for (int i = 0; i < paraArray.length(); i++) {


                                JSONObject pobj = paraArray.getJSONObject(i);
                                JSONArray lineArray = pobj.getJSONArray("L");

                                LinearLayout para = new LinearLayout(MainActivity.this);
                                para.setOrientation(LinearLayout.VERTICAL);
                                para.setGravity(Gravity.START);
                                para.setPadding(5, 10, 5, 10);

                                for (int j = 0; j < lineArray.length(); j++) {

                                    JSONObject lobj = lineArray.getJSONObject(j);

                                    JSONArray wordArray = lobj.getJSONArray("W");

                                    //String line = "";

                                    final LinearLayout line = new LinearLayout(MainActivity.this);
                                    line.setOrientation(LinearLayout.HORIZONTAL);
                                    line.setGravity(Gravity.START);
                                    line.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                                    line.setPadding(5, 10, 5, 10);

                                    for (int k = 0; k < wordArray.length(); k++) {

                                        JSONObject wordwrap = wordArray.getJSONObject(k);

                                        final String wo = wordwrap.getString("W");

                                        final TextView word = new TextView(MainActivity.this);
                                        word.setPadding(5, 0, 5, 0);
                                        word.setBackgroundColor(Color.TRANSPARENT);
                                        word.setText(wo);
                                        word.setTextSize(16);
                                        word.setTextColor(Color.BLACK);





                                        word.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                toast.setText("word: " + wo);
                                                toast.show();
                                            }
                                        });


                                        line.addView(word);

                                    }

                                    final int maxwidth = getWindowManager().getDefaultDisplay().getWidth();

                                    final int[] wordwidth = {0};

                                    Log.d("asdCount" , String.valueOf(line.getChildCount()));

                                    for (int m = 0 ; m < line.getChildCount() ; m++)
                                    {

                                        final View v = line.getChildAt(m);


                                        final int finalM = m;
                                        v.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                                            @Override
                                            public void onGlobalLayout() {
                                                v.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                                                wordwidth[0] = wordwidth[0] + v.getWidth();

                                                Log.d("asd" , String.valueOf(v.getWidth()));

                                                if (finalM == line.getChildCount()-1)
                                                {

                                                    int emptySpace = maxwidth - wordwidth[0];

                                                    Log.d("wordWidth" , String.valueOf(wordwidth[0]));

                                                    Log.d("empty" , String.valueOf(emptySpace));

                                                    float space = emptySpace / (line.getChildCount() + 1);

                                                    float netspace = space / 2;

                                                    for (int m = 0 ; m < line.getChildCount() ; m++)
                                                    {


                                                        View v = line.getChildAt(m);

                                                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                                                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                                                        layoutParams.setMargins((int)netspace , 0 , (int)netspace , 0);

                                                        //v.setPadding((int)netspace , 0 , (int)netspace , 0);

                                                        v.setLayoutParams(layoutParams);

                                                    }

//                            line.setPadding((int)netspace , 0 , (int)netspace , 0);



                                                }

                                            }
                                        });




                                    }

                                    Log.d("maxWidth" , String.valueOf(maxwidth));


                                    para.addView(line);


                                    Log.d("asdasd", line + "\n");

                                }

                                final int finalI = i;
                                para.setOnLongClickListener(new View.OnLongClickListener() {
                                    @Override
                                    public boolean onLongClick(View view) {

                                        toast.setText("para pressed: " + String.valueOf(finalI + 1));

                                        toast.show();
                                        return true;
                                    }
                                });

                                con.addView(para);
                                Space ap = new Space(MainActivity.this);
                                ap.setMinimumHeight(30);
                                con.addView(ap);
                                Log.d("asdasd", "\n");

                            }


                            content.addView(con);

                            progress.setVisibility(View.GONE);

                            flag = 4;

                        } catch (Exception e) {
                            e.printStackTrace();
                            progress.setVisibility(View.GONE);
                        }


                    }

                    @Override
                    public void onFailure(Call<dataBean> call, Throwable throwable) {

                        progress.setVisibility(View.GONE);

                    }
                });
            } else if (flag == 5) {
                content.removeAllViews();
                progress.setVisibility(View.VISIBLE);

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://14.140.111.4:20002")
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                AllAPIs cr = retrofit.create(AllAPIs.class);


                Call<dataBean> call = cr.getData("3", "B86C1FBE-2C5A-4A75-BFCE-007580A1451D");

                call.enqueue(new Callback<dataBean>() {
                    @Override
                    public void onResponse(Call<dataBean> call, Response<dataBean> response) {

                        try {

                            title.setText(response.body().getR().getCT());
                            title.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

                            author.setText(response.body().getR().getPoet().getPN());
                            author.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

                            String ddata = response.body().getR().getCR();

                            JSONObject jsonObject = new JSONObject(ddata);


                            JSONArray paraArray = jsonObject.getJSONArray("P");

                            LinearLayout con = new LinearLayout(MainActivity.this);
                            con.setOrientation(LinearLayout.VERTICAL);
                            con.setGravity(Gravity.START);

                            for (int i = 0; i < paraArray.length(); i++) {


                                JSONObject pobj = paraArray.getJSONObject(i);
                                JSONArray lineArray = pobj.getJSONArray("L");

                                LinearLayout para = new LinearLayout(MainActivity.this);
                                para.setOrientation(LinearLayout.VERTICAL);
                                para.setGravity(Gravity.START);
                                para.setPadding(5, 10, 5, 10);

                                for (int j = 0; j < lineArray.length(); j++) {

                                    JSONObject lobj = lineArray.getJSONObject(j);

                                    JSONArray wordArray = lobj.getJSONArray("W");

                                    //String line = "";

                                    final LinearLayout line = new LinearLayout(MainActivity.this);
                                    line.setOrientation(LinearLayout.HORIZONTAL);
                                    line.setGravity(Gravity.START);
                                    line.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                                    line.setPadding(5, 10, 5, 10);

                                    for (int k = 0; k < wordArray.length(); k++) {

                                        JSONObject wordwrap = wordArray.getJSONObject(k);

                                        final String wo = wordwrap.getString("W");

                                        final TextView word = new TextView(MainActivity.this);
                                        word.setPadding(5, 0, 5, 0);
                                        word.setBackgroundColor(Color.TRANSPARENT);
                                        word.setText(wo);
                                        word.setTextSize(16);
                                        word.setTextColor(Color.BLACK);





                                        word.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                toast.setText("word: " + wo);
                                                toast.show();
                                            }
                                        });


                                        line.addView(word);

                                    }

                                    final int maxwidth = getWindowManager().getDefaultDisplay().getWidth();

                                    final int[] wordwidth = {0};

                                    Log.d("asdCount" , String.valueOf(line.getChildCount()));

                                    for (int m = 0 ; m < line.getChildCount() ; m++)
                                    {

                                        final View v = line.getChildAt(m);


                                        final int finalM = m;
                                        v.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                                            @Override
                                            public void onGlobalLayout() {
                                                v.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                                                wordwidth[0] = wordwidth[0] + v.getWidth();

                                                Log.d("asd" , String.valueOf(v.getWidth()));

                                                if (finalM == line.getChildCount()-1)
                                                {

                                                    int emptySpace = maxwidth - wordwidth[0];

                                                    Log.d("wordWidth" , String.valueOf(wordwidth[0]));

                                                    Log.d("empty" , String.valueOf(emptySpace));

                                                    float space = emptySpace / (line.getChildCount() + 1);

                                                    float netspace = space / 2;

                                                    for (int m = 0 ; m < line.getChildCount() ; m++)
                                                    {


                                                        View v = line.getChildAt(m);

                                                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                                                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                                                        layoutParams.setMargins((int)netspace , 0 , (int)netspace , 0);

                                                        //v.setPadding((int)netspace , 0 , (int)netspace , 0);

                                                        v.setLayoutParams(layoutParams);

                                                    }

//                            line.setPadding((int)netspace , 0 , (int)netspace , 0);



                                                }

                                            }
                                        });




                                    }

                                    Log.d("maxWidth" , String.valueOf(maxwidth));


                                    para.addView(line);


                                    Log.d("asdasd", line + "\n");

                                }

                                final int finalI = i;
                                para.setOnLongClickListener(new View.OnLongClickListener() {
                                    @Override
                                    public boolean onLongClick(View view) {

                                        toast.setText("para pressed: " + String.valueOf(finalI + 1));

                                        toast.show();
                                        return true;
                                    }
                                });

                                con.addView(para);
                                Space ap = new Space(MainActivity.this);
                                ap.setMinimumHeight(30);
                                con.addView(ap);
                                Log.d("asdasd", "\n");

                            }


                            content.addView(con);

                            progress.setVisibility(View.GONE);

                            flag = 5;

                        } catch (Exception e) {
                            e.printStackTrace();
                            progress.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onFailure(Call<dataBean> call, Throwable throwable) {

                        progress.setVisibility(View.GONE);

                    }
                });
            } else if (flag == 6) {
                content.removeAllViews();
                progress.setVisibility(View.VISIBLE);

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://14.140.111.4:20002")
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                AllAPIs cr = retrofit.create(AllAPIs.class);


                Call<dataBean> call = cr.getData("3", "EEBC8F15-6904-49A9-BFE4-6E76E71E28BB");

                call.enqueue(new Callback<dataBean>() {
                    @Override
                    public void onResponse(Call<dataBean> call, Response<dataBean> response) {


                        try {

                            title.setText(response.body().getR().getCT());
                            title.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

                            author.setText(response.body().getR().getPoet().getPN());
                            author.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

                            String ddata = response.body().getR().getCR();

                            JSONObject jsonObject = new JSONObject(ddata);


                            JSONArray paraArray = jsonObject.getJSONArray("P");

                            LinearLayout con = new LinearLayout(MainActivity.this);
                            con.setOrientation(LinearLayout.VERTICAL);
                            con.setGravity(Gravity.CENTER_HORIZONTAL);

                            for (int i = 0; i < paraArray.length(); i++) {


                                JSONObject pobj = paraArray.getJSONObject(i);
                                JSONArray lineArray = pobj.getJSONArray("L");

                                LinearLayout para = new LinearLayout(MainActivity.this);
                                para.setOrientation(LinearLayout.VERTICAL);
                                para.setGravity(Gravity.CENTER_HORIZONTAL);
                                para.setPadding(5, 10, 5, 10);

                                for (int j = 0; j < lineArray.length(); j++) {

                                    JSONObject lobj = lineArray.getJSONObject(j);

                                    JSONArray wordArray = lobj.getJSONArray("W");

                                    //String line = "";

                                    final LinearLayout line = new LinearLayout(MainActivity.this);
                                    line.setOrientation(LinearLayout.HORIZONTAL);
                                    line.setGravity(Gravity.CENTER_HORIZONTAL);
                                    line.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                                    line.setPadding(5, 10, 5, 10);

                                    for (int k = 0; k < wordArray.length(); k++) {

                                        JSONObject wordwrap = wordArray.getJSONObject(k);

                                        final String wo = wordwrap.getString("W");

                                        final TextView word = new TextView(MainActivity.this);
                                        word.setPadding(5, 0, 5, 0);
                                        word.setBackgroundColor(Color.TRANSPARENT);
                                        word.setText(wo);
                                        word.setTextSize(16);
                                        word.setTextColor(Color.BLACK);





                                        word.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                toast.setText("word: " + wo);
                                                toast.show();
                                            }
                                        });


                                        line.addView(word);

                                    }

                                    final int maxwidth = getWindowManager().getDefaultDisplay().getWidth();

                                    final int[] wordwidth = {0};

                                    Log.d("asdCount" , String.valueOf(line.getChildCount()));

                                    for (int m = 0 ; m < line.getChildCount() ; m++)
                                    {

                                        final View v = line.getChildAt(m);


                                        final int finalM = m;
                                        v.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                                            @Override
                                            public void onGlobalLayout() {
                                                v.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                                                wordwidth[0] = wordwidth[0] + v.getWidth();

                                                Log.d("asd" , String.valueOf(v.getWidth()));

                                                if (finalM == line.getChildCount()-1)
                                                {

                                                    int emptySpace = maxwidth - wordwidth[0];

                                                    Log.d("wordWidth" , String.valueOf(wordwidth[0]));

                                                    Log.d("empty" , String.valueOf(emptySpace));

                                                    float space = emptySpace / (line.getChildCount() + 1);

                                                    float netspace = space / 2;

                                                    for (int m = 0 ; m < line.getChildCount() ; m++)
                                                    {


                                                        View v = line.getChildAt(m);

                                                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                                                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                                                        layoutParams.setMargins((int)netspace , 0 , (int)netspace , 0);

                                                        //v.setPadding((int)netspace , 0 , (int)netspace , 0);

                                                        v.setLayoutParams(layoutParams);

                                                    }

//                            line.setPadding((int)netspace , 0 , (int)netspace , 0);



                                                }

                                            }
                                        });




                                    }

                                    Log.d("maxWidth" , String.valueOf(maxwidth));


                                    para.addView(line);


                                    Log.d("asdasd", line + "\n");

                                }

                                final int finalI = i;
                                para.setOnLongClickListener(new View.OnLongClickListener() {
                                    @Override
                                    public boolean onLongClick(View view) {

                                        toast.setText("para pressed: " + String.valueOf(finalI + 1));

                                        toast.show();
                                        return true;
                                    }
                                });

                                con.addView(para);
                                Space ap = new Space(MainActivity.this);
                                ap.setMinimumHeight(30);
                                con.addView(ap);
                                Log.d("asdasd", "\n");

                            }


                            content.addView(con);

                            progress.setVisibility(View.GONE);

                            flag = 6;

                        } catch (Exception e) {
                            e.printStackTrace();
                            progress.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onFailure(Call<dataBean> call, Throwable throwable) {

                        progress.setVisibility(View.GONE);

                    }
                });
            }


        }

        return true;


    }

















    @Override
    public boolean onTouch(View v, MotionEvent event) {
        TextView view = (TextView) v;

        float scale;

        dumpEvent(event);
        // Handle touch events here...

        switch (event.getAction() & 255)
        {
            case MotionEvent.ACTION_DOWN:   // first finger down only
                savedMatrix.set(matrix);
                start.set(event.getX(), event.getY());
                Log.d("asd", "mode=DRAG"); // write to LogCat
                mode = DRAG;
                break;

            case MotionEvent.ACTION_UP: // first finger lifted

            case 6: // second finger lifted

                mode = NONE;
                Log.d("asd", "mode=NONE");
                break;

            case 5: // first and second finger down

                oldDist = spacing(event);
                Log.d("asd", "oldDist=" + oldDist);
                if (oldDist > 5f) {
                    savedMatrix.set(matrix);
                    midPoint(mid, event);
                    mode = ZOOM;
                    Log.d("asd", "mode=ZOOM");
                }
                break;

            case MotionEvent.ACTION_MOVE:

                if (mode == DRAG)
                {
                    matrix.set(savedMatrix);
                    matrix.postTranslate(event.getX() - start.x, event.getY() - start.y); // create the transformation in the matrix  of points
                }
                else if (mode == ZOOM)
                {
                    // pinch zooming
                    float newDist = spacing(event);
                    Log.d("asd", "newDist=" + newDist);
                    if (newDist > 5f)
                    {
                        matrix.set(savedMatrix);
                        scale = newDist / oldDist; // setting the scaling of the
                        // matrix...if scale > 1 means
                        // zoom in...if scale < 1 means
                        // zoom out
                        matrix.postScale(scale, scale, mid.x, mid.y);
                    }
                }
                break;
        }

         // display the transformation on screen

        return true; // indicate event was handled
    }




    private float spacing(MotionEvent event)
    {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

/*
 * --------------------------------------------------------------------------
 * Method: midPoint Parameters: PointF object, MotionEvent Returns: void
 * Description: calculates the midpoint between the two fingers
 * ------------------------------------------------------------
 */

    private void midPoint(PointF point, MotionEvent event)
    {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    /** Show an event in the LogCat view, for debugging */
    private void dumpEvent(MotionEvent event)
    {
        String names[] = { "DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE","POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?" };
        StringBuilder sb = new StringBuilder();
        int action = event.getAction();
        int actionCode = action & MotionEvent.ACTION_MASK;
        sb.append("event ACTION_").append(names[actionCode]);

        if (actionCode == MotionEvent.ACTION_POINTER_DOWN || actionCode == MotionEvent.ACTION_POINTER_UP)
        {
            sb.append("(pid ").append(action >> MotionEvent.ACTION_POINTER_ID_SHIFT);
            sb.append(")");
        }

        sb.append("[");
        for (int i = 0; i < event.getPointerCount(); i++)
        {
            sb.append("#").append(i);
            sb.append("(pid ").append(event.getPointerId(i));
            sb.append(")=").append((int) event.getX(i));
            sb.append(",").append((int) event.getY(i));
            if (i + 1 < event.getPointerCount())
                sb.append(";");
        }

        sb.append("]");
        Log.d("Touch Events ---------", sb.toString());
    }













}
