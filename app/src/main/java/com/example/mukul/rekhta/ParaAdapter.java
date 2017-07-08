package com.example.mukul.rekhta;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class ParaAdapter extends BaseAdapter implements PinchAdapter {

    PinchListView listView;
    JSONArray paraarray;
    Context context;
    LayoutInflater inflater = null;

    public ParaAdapter(PinchListView listView , Context context , JSONArray paraarray)
    {
        this.context = context;
        this.listView = listView;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
        {
            vi = inflater.inflate(R.layout.para_row, null);
        }

        LinearLayout para = (LinearLayout)vi.findViewById(R.id.para);

        try {

            for (int i = 0; i < paraarray.length(); i++) {


                JSONObject pobj = paraarray.getJSONObject(i);
                JSONArray lineArray = pobj.getJSONArray("L");



                para.setOrientation(LinearLayout.VERTICAL);
                para.setGravity(Gravity.CENTER_HORIZONTAL);
                para.setPadding(5, 10, 5, 10);

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





                    /*    word.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                toast.setText("word: " + wo);
                                toast.show();
                            }
                        });
*/

                        line.addView(word);

                    }

  /*                  final int maxwidth = context.getWindowManager().getDefaultDisplay().getWidth();*/

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

                                   // int emptySpace = maxwidth - wordwidth[0];

                                    Log.d("wordWidth" , String.valueOf(wordwidth[0]));

                                   // Log.d("empty" , String.valueOf(emptySpace));

                                   // float space = emptySpace / (line.getChildCount() + 1);

                                    //float netspace = space / 2;

                                    for (int m = 0 ; m < line.getChildCount() ; m++)
                                    {


                                        View v = line.getChildAt(m);

                                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                                      //  layoutParams.setMargins((int)netspace , 0 , (int)netspace , 0);

                                        //v.setPadding((int)netspace , 0 , (int)netspace , 0);

                                        v.setLayoutParams(layoutParams);

                                    }

//                            line.setPadding((int)netspace , 0 , (int)netspace , 0);



                                }

                            }
                        });




                    }

                  //  Log.d("maxWidth" , String.valueOf(maxwidth));

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

                    /*    toast.setText("para pressed: " + String.valueOf(finalI + 1));

                        toast.show();*/
                        return true;
                    }
                });



                Log.d("asdasd", "\n");

            }




                    /*container.removeAllViews();

                    ZoomView zv = new ZoomView(MainActivity.this);

                    zv.addView(scroll);

                    zv.setNestedScrollingEnabled(false);

                    container.addView(zv);*/





        } catch (JSONException e) {
            e.printStackTrace();
        }






        return vi;


    }

    @Override
    public boolean isRowPinchable(int position) {
        return false;
    }
}
