package com.hello.trafficdetection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.util.Locale;

public class Choose extends AppCompatActivity implements TextToSpeech.OnInitListener {
private ImageView nav_view, searchview ,menu;
    TextToSpeech tts;
    int counter =1;
    MediaPlayer click;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);
        nav_view=findViewById(R.id.nav_view);
        searchview=findViewById(R.id.searchview);
        menu= findViewById(R.id.menu);
        click= MediaPlayer.create(Choose.this,R.raw.buttonpressed);
        try{
            this.getSupportActionBar().hide();
        }catch(NullPointerException e){

        }
        tts = new TextToSpeech(this, this);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (counter == 1) {
                    speakOut("choice window has three options.search anything .Road help. read menu.search anything is at top. road walking help is in center.and read menu is in bottom of window. choose any one.");
                    //speak after 1000ms
                    counter++;

                }
                else{

                }

            }

            private void speakOut(String text) {

                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
            }
        }, 500);
        nav_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click.start();
                Intent calling =new Intent(Choose.this, VideoCallingActivity.class);
                startActivity(calling);
            }
        });
        searchview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click.start();
                Intent search =new Intent(Choose.this,SearchMain.class);
                startActivity(search);
            }
        });
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click.start();
                Intent read_menu =new Intent(Choose.this,MenuRead.class);
                startActivity(read_menu);
            }
        });
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
    @Override
    protected void onStop()
    {
        super.onStop();

        if(tts != null){
            tts.shutdown();
        }
    }

}
