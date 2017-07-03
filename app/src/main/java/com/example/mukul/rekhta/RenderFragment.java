package com.example.mukul.rekhta;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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

public class RenderFragment extends Fragment {

    LinearLayout content;
    Toast toast;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rendering_layout , container , false);

        content = (LinearLayout) view.findViewById(R.id.content);



        toast = Toast.makeText(getActivity() , null , Toast.LENGTH_SHORT);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://14.140.111.4:20002")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AllAPIs cr = retrofit.create(AllAPIs.class);


        Call<dataBean> call = cr.getData("1" , "827D3643-BCC4-410B-B1B9-00008EBCA797");

        call.enqueue(new Callback<dataBean>() {
            @Override
            public void onResponse(Call<dataBean> call, Response<dataBean> response) {

                String ddata = response.body().getR().getCR();

                try {
                    JSONObject jsonObject = new JSONObject(ddata);


                    JSONArray paraArray = jsonObject.getJSONArray("P");

                    LinearLayout con = new LinearLayout(getActivity());
                    con.setOrientation(LinearLayout.VERTICAL);
                    con.setGravity(Gravity.CENTER_HORIZONTAL);

                    for (int i = 0 ; i < paraArray.length() ; i++)
                    {



                        JSONObject pobj = paraArray.getJSONObject(i);
                        JSONArray lineArray = pobj.getJSONArray("L");

                        LinearLayout para = new LinearLayout(getActivity());
                        para.setOrientation(LinearLayout.VERTICAL);
                        para.setGravity(Gravity.CENTER_HORIZONTAL);
                        para.setPadding(5 , 10 , 5 , 10);

                        for (int j = 0 ; j < lineArray.length() ; j++)
                        {

                            JSONObject lobj = lineArray.getJSONObject(j);

                            JSONArray wordArray = lobj.getJSONArray("W");

                            //String line = "";

                            LinearLayout line = new LinearLayout(getActivity());
                            line.setOrientation(LinearLayout.HORIZONTAL);
                            line.setGravity(Gravity.CENTER_HORIZONTAL);
                            line.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                            line.setPadding(5 , 10 , 5 , 10);

                            for (int k = 0 ; k < wordArray.length() ; k++)
                            {

                                JSONObject wordwrap = wordArray.getJSONObject(k);

                                final String wo = wordwrap.getString("W");

                                TextView word = new TextView(getActivity());
                                word.setPadding(5 , 0 , 5 , 0);
                                word.setText(wo);

                                word.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        toast.setText("word: " + wo);
                                        toast.show();
                                    }
                                });

                                line.addView(word);

                            }





                            para.addView(line);



                            Log.d("asdasd" , line + "\n");

                        }

                        final int finalI = i;
                        para.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View view) {

                                toast.setText("para pressed: " + String.valueOf(finalI +1));

                                toast.show();
                                return true;
                            }
                        });

                        con.addView(para);

                        Log.d("asdasd" , "\n");

                    }


                    content.addView(con);


                } catch (JSONException e) {
                    e.printStackTrace();
                }





                Log.d("asdasd" , ddata);

                String formattedData = ddata.replaceAll("\\/" , ddata);

                Log.d("asdasd" , formattedData);

            }

            @Override
            public void onFailure(Call<dataBean> call, Throwable throwable) {

            }
        });





        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu , menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);




    }
}
