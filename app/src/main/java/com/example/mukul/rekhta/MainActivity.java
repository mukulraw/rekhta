package com.example.mukul.rekhta;

import android.content.Context;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.ScaleAnimation;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mukul.rekhta.POJO.dataBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import pl.polidea.view.ZoomView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;

    DrawerLayout drawer;
    CustomListView content;
    ScaleGestureDetector scaleGD;
    Toast toast;
    ProgressBar progress;
    LinearLayout contain;

ProgressBar progress1;

List<TextView> l;

    private enum Mode {
        NONE,
        DRAG,
        ZOOM
    }

    private static final String TAG = "ZoomLayout";
    private static final float MIN_ZOOM = 1.0f;
    private static final float MAX_ZOOM = 4.0f;

    private Mode mode = Mode.NONE;
    private float scale = 1.0f;
    private float lastScaleFactor = 0f;

    // Where the finger first  touches the screen
    private float startX = 0f;
    private float startY = 0f;

    // How much to translate the canvas
    private float dx = 0f;
    private float dy = 0f;
    private float prevDx = 0f;
    private float prevDy = 0f;

    LinearLayout continer;


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

        content = (CustomListView) findViewById(R.id.content);
        container = (LinearLayout) findViewById(R.id.container);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        title = (TextView) findViewById(R.id.title);
        author = (TextView) findViewById(R.id.author);
        //scView = (ScrollView)findViewById(R.id.scview);

//        scrollContainer = (LinearLayout)findViewById(R.id.scroll_container);




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

                String ddata = response.body().getR().getCR();

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(ddata);
                    JSONArray paraArray = jsonObject.getJSONArray("P");
                    ParaAdapter adapter = new ParaAdapter(MainActivity.this , paraArray);

                    content.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }



                progress.setVisibility(View.GONE);



            }

            @Override
            public void onFailure(Call<dataBean> call, Throwable throwable) {

                progress.setVisibility(View.GONE);

            }
        });










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



        return true;


    }











    public class ParaAdapter extends BaseAdapter{

        PinchListView listView;
        JSONArray paraarray;
        Context context;
        LayoutInflater inflater = null;

        public ParaAdapter(Context context , JSONArray paraarray)
        {
            this.context = context;
            //this.listView = listView;
            this.paraarray = paraarray;
            inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }


        @Override
        public int getCount() {
            return paraarray.length();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View vi=convertView;
            if(convertView==null)
            {
                vi = inflater.inflate(R.layout.para_row, null);
            }

            LinearLayout para = (LinearLayout)vi.findViewById(R.id.para);

            //isRowPinchable(position);

            try {




                    JSONObject pobj = paraarray.getJSONObject(position);
                    JSONArray lineArray = pobj.getJSONArray("L");



                    for (int j = 0; j < lineArray.length(); j++) {

                        JSONObject lobj = lineArray.getJSONObject(j);

                        JSONArray wordArray = lobj.getJSONArray("W");

                        //String line = "";

                        final LinearLayout line = new LinearLayout(context);
                        line.setOrientation(LinearLayout.HORIZONTAL);
                        line.setGravity(Gravity.CENTER_HORIZONTAL);
                        line.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                        line.setPadding(5, 10, 5, 10);


                        for (int k = 0; k < wordArray.length(); k++) {

                            JSONObject wordwrap = wordArray.getJSONObject(k);

                            final String wo = wordwrap.getString("W");

                            final TextView word = new TextView(context);
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


                    para.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {

                            toast.setText("para pressed: " + String.valueOf(position + 1));

                            toast.show();
                            return true;
                        }
                    });



                    Log.d("asdasd", "\n");





                    /*container.removeAllViews();

                    ZoomView zv = new ZoomView(MainActivity.this);

                    zv.addView(scroll);

                    zv.setNestedScrollingEnabled(false);

                    container.addView(zv);*/





            } catch (JSONException e) {
                e.printStackTrace();
            }



            //listView.adjustCellHeight(vi, position);


            return vi;


        }

        /*@Override
        public boolean isRowPinchable(int position) {
            return true;
        }*/
    }












}
