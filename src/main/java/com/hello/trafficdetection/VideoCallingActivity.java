package com.hello.trafficdetection;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;


public class VideoCallingActivity extends AppCompatActivity implements TextToSpeech.OnInitListener{
    EditText editText;
    TextToSpeech tts;
    MediaPlayer click , speak_sound;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calling);
        click= MediaPlayer.create(VideoCallingActivity.this,R.raw.buttonpressed);
        speak_sound= MediaPlayer.create(VideoCallingActivity.this,R.raw.speech);
        tts = new TextToSpeech(this, this);

        editText = findViewById(R.id.conferenceName);
        checkPermission();
        final SpeechRecognizer mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);


        final Intent mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                Locale.getDefault());


        try{
            this.getSupportActionBar().hide();
        }catch(NullPointerException e){

        }
        mSpeechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {
                //getting all the matches
                ArrayList<String> matches = bundle
                        .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

                //displaying the first match
                if (matches != null)
                    editText.setText(matches.get(0));
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });
        findViewById(R.id.microphone).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_UP:
                        mSpeechRecognizer.stopListening();
                        editText.setHint(" ");
                        break;

                    case MotionEvent.ACTION_DOWN:
                        speak_sound.start();
                        mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
                        editText.setText("");
                        editText.setHint("");
                        break;
                }
                return false;
            }
        });
    }
    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:" + getPackageName()));
                startActivity(intent);
                finish();
            }
        }

//jistiMeet setUp
        try {
            JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
                    .setServerURL(new URL(""))
                    .setWelcomePageEnabled(false)
                    .build();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                speakOut("Video Conferencing window has two button .join room and microphone button.keep pressing microphone which is at bottom and create a room by speaking to give its name and after that press join which is at top. ");
                //speak after 1000ms
            }

            private void speakOut(String text) {

                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
            }
        }, 500);


    }
    public void onButtonClick(View v) {
        click.start();
        String text = editText.getText().toString();
        if (text.length() > 0) {
            JitsiMeetConferenceOptions options
                    = new JitsiMeetConferenceOptions.Builder()
                    .setRoom(text)
                    .build();
            JitsiMeetActivity.launch(this, options);
        }
    }
    @Override
    public void onInit(int i) {

        if (i == TextToSpeech.SUCCESS) {

            int result = tts.setLanguage(Locale.US);


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
