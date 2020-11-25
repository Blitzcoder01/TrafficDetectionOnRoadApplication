package com.hello.trafficdetection;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;

import java.util.Locale;

import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.Manifest.permission.CAMERA;

public class PermissionActivity extends AppCompatActivity implements TextToSpeech.OnInitListener, View.OnClickListener{
    Button button ,button_skip;
    int counter =1;
    TextToSpeech tts;
    private static final int PERMISSION_REQUEST_CODE = 200;
    private View view;
    MediaPlayer click;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        button= findViewById(R.id.button);
        button_skip= findViewById(R.id.button_skip);
        click= MediaPlayer.create(PermissionActivity.this,R.raw.buttonpressed);

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
                    speakOut("List of permissions required by this app are: Internet permission .Camera permission .Manage calling and read contacts permission.Storage permission .Press allow button which is at right hand side in bottom of display window.or if you already done that then press skip button which is on top right corner.");

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
        button.setOnClickListener(this);

        button_skip.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                click.start();
                Intent cameralayout =new Intent(PermissionActivity.this,Choose.class);
                startActivity(cameralayout);
                finish();
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
    public void onClick(View view) {
        view = view;

        int id = view.getId();
        switch (id) {

            case R.id.button:
                if (!checkPermission()) {

                    requestPermission();
                    Intent cameralayout =new Intent(PermissionActivity.this,Choose.class);
                    startActivity(cameralayout);
                    finish();


                } else {

                    Snackbar.make(view, "Permission already granted.", Snackbar.LENGTH_LONG).show();
                    Intent choice =new Intent(PermissionActivity.this,Choose.class);
                   choice.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(choice);
                    finish();



                }
                break;
        }


    }


    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int result2 = ContextCompat.checkSelfPermission(getApplicationContext(), INTERNET);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);

        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE,INTERNET, CAMERA}, PERMISSION_REQUEST_CODE);

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean internetAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean writeExternalStorageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (internetAccepted && cameraAccepted && writeExternalStorageAccepted)
                        Snackbar.make(view, "Permission Granted, Now you have all required permission.", Snackbar.LENGTH_LONG).show();
                    else {

                        Snackbar.make(view, "Permission Denied, You don't have enough permission granted.", Snackbar.LENGTH_LONG).show();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(INTERNET)) {
                                showMessageOKCancel("You need to allow access internet",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, INTERNET, CAMERA},
                                                            PERMISSION_REQUEST_CODE);
                                                }
                                            }
                                        });
                                return;
                            }
                        }

                    }
                }


                break;
        }

    }
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(PermissionActivity.this)
                .setMessage(message)
                .setPositiveButton("Allow", okListener)
                .create()
                .show();
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