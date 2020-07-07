package com.google.android.gms.samples.vision.ocrreader;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.speech.tts.TextToSpeech;
import android.support.v4.content.LocalBroadcastManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import java.util.Locale;

public class SplashActivity extends Activity {
    TextToSpeech tts;
    String t1 = "";
    LinearLayout view;

    private BroadcastReceiver onNotice = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String pack = "Notification from ";
            String title = intent.getStringExtra("title");
            String ticker = intent.getStringExtra("ticker");

            pack += title;
            String text = intent.getStringExtra("text");
            if (ticker == "") {
                pack = "Call from " + title;
                //         if(MainActivity.flag==1)
                tts.speak(pack, TextToSpeech.QUEUE_FLUSH, null, "DEFAULT");
                // TODO This would be a good place to "Do something when the phone rings" ;-)
            } else if (!title.toLowerCase().contains("change keyboard")) {
                Log.e("inside on recieve", title);
                pack += " and the notification says ";
                pack += text;
                tts.speak(pack, TextToSpeech.QUEUE_FLUSH, null, "DEFAULT");
            }


        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("f", "f");
        TextToSpeech.OnInitListener listener =
                new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(final int status) {
                        if (status == TextToSpeech.SUCCESS) {
                            Log.d("OnInitListener", "Text to speech engine started successfully.");
                            tts.setLanguage(Locale.US);
                        } else {
                            Log.d("OnInitListener", "Error starting the text to speech engine.");
                        }
                    }
                };
        Log.e("ff", "ff");
        tts = new TextToSpeech(this.getApplicationContext(), listener);
        LocalBroadcastManager.getInstance(this).registerReceiver(onNotice, new IntentFilter("Msg"));
        Log.e("dd", "dd");
    }
}
