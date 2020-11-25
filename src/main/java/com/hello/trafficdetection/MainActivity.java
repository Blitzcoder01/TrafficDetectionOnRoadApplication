package com.hello.trafficdetection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import com.airbnb.lottie.LottieAnimationView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {
    TextToSpeech tts;
    LottieAnimationView lottieAnimationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tts = new TextToSpeech(this, this);
        try{
            this.getSupportActionBar().hide();
        }catch(NullPointerException e){

        }
        lottieAnimationView=findViewById(R.id.lottie_animation);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                speakOut("Welcome to Traffic Detection App");
                //speak after 1000ms
            }

            private void speakOut(String text) {

                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
            }
        }, 500);
        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {
                // This method will be executed once the timer is over
                Intent splashscreen = new Intent(MainActivity.this, PermissionActivity.class);

                startActivity(splashscreen);
                finish();
            }
        }, 3000);



    }

    @Override
    public void onInit(int i) {

        if (i == TextToSpeech.SUCCESS) {

            int result = tts.setLanguage(Locale.US);

            // tts.setPitch(5); // set pitch level

            // tts.setSpeechRate(2); // set speech speed rate

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "Language is not supported");
            }
            else {

            }

        } else {
            Log.e("TTS", "Initilization Failed");
        }
    }
}
