package com.example.administrator.slidetogglebutton;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import view.SlidingToggleView;


public class MainActivity extends Activity {
    TextView txt, txt2, txt3, txt4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SlidingToggleView slide = (SlidingToggleView)findViewById(R.id.slide);
        SlidingToggleView slide2 = (SlidingToggleView)findViewById(R.id.slide2);
        SlidingToggleView slide3 = (SlidingToggleView)findViewById(R.id.slide3);
        SlidingToggleView slide4 = (SlidingToggleView)findViewById(R.id.slide4);
        txt = (TextView)findViewById(R.id.txt);
        txt2 = (TextView)findViewById(R.id.txt2);
        txt3 = (TextView)findViewById(R.id.txt3);
        txt4 = (TextView)findViewById(R.id.txt4);

        slide.setOnMiddle(new SlidingToggleView.onMiddle() {
            @Override
            public void onMiddle(int scrollX, int middle) {
                Log.d("middle", "scrollx : "+ scrollX + "  middle : " +middle);
            }

            @Override
            public void DefaultValue() {
                txt.setText("Left");
            }

            @Override
            public void SettingValue() {
                txt.setText("Right");
            }
        });


        slide2.setOnMiddle(new SlidingToggleView.onMiddle() {
            @Override
            public void onMiddle(int scrollX, int middle) {

            }

            @Override
            public void DefaultValue() {
                txt2.setText("Left");

            }
            @Override
            public void SettingValue() {
                txt2.setText("Right");
            }
        });
        slide2.initToggleState();



        slide3.setOnMiddle(new SlidingToggleView.onMiddle() {
            @Override
            public void onMiddle(int scroll, int middle) {
                Log.d("middle", "scrollx : "+ scroll + "  middle : " +middle);
            }

            @Override
            public void DefaultValue() {
                txt3.setText("Top");
            }

            @Override
            public void SettingValue() {
                txt3.setText("Bottom");
            }
        });

        slide4.setOnMiddle(new SlidingToggleView.onMiddle() {
            @Override
            public void onMiddle(int scroll, int middle) {
                Log.d("middle", "scrollx : "+ scroll + "  middle : " +middle);
            }

            @Override
            public void DefaultValue() {
                txt4.setText("Top");
            }

            @Override
            public void SettingValue() {
                txt4.setText("Bottom");
            }
        });
        slide4.initToggleState();
    }
}
