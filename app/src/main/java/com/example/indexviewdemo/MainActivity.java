package com.example.indexviewdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.indexviewdemo.view.IndexView;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private IndexView indexView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.text);
        indexView = (IndexView) findViewById(R.id.indexView);
        if (indexView != null) {
            indexView.setTextView(textView);
        }
    }
}
