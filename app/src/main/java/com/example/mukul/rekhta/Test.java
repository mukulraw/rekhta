package com.example.mukul.rekhta;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ScrollView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Test extends AppCompatActivity {

    ScrollView scroll;
    TextView text;
    ZoomLayout zoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        scroll = (ScrollView)findViewById(R.id.scroll);
        text = (TextView)findViewById(R.id.text);
        zoom = (ZoomLayout) findViewById(R.id.zoom);

    }
}
