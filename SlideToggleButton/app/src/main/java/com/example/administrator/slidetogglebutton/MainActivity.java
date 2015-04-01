package com.example.administrator.slidetogglebutton;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import view.SlidingToggleView;


public class MainActivity extends Activity {
    TextView txt, txt2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SlidingToggleView slide = (SlidingToggleView)findViewById(R.id.slide);
        txt = (TextView)findViewById(R.id.txt);
        slide.setOnMiddle(new SlidingToggleView.onMiddle() {
            @Override
            public void onMiddle(int scrollX, int middle) {
                Log.d("middle", "scrollx : "+ scrollX + "  middle : " +middle);
            }

            @Override
            public void LeftValue() {
                txt.setText("Left");
            }

            @Override
            public void RightValue() {
                txt.setText("Right");
            }
        });
        SlidingToggleView slide2 = (SlidingToggleView)findViewById(R.id.slide2);
        txt2 = (TextView)findViewById(R.id.txt2);
        slide2.setOnMiddle(new SlidingToggleView.onMiddle() {
            @Override
            public void onMiddle(int scrollX, int middle) {

            }

            @Override
            public void LeftValue() {
                txt2.setText("Left");

            }
            @Override
            public void RightValue() {
                txt2.setText("Right");
            }
        });
        slide2.setButtonRight();
    }
}
