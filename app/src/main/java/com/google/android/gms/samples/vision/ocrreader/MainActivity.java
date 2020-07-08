package com.google.android.gms.samples.vision.ocrreader;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;

import com.majeur.cling.Cling;
import com.majeur.cling.ClingManager;

import java.util.Locale;

public class MainActivity extends AppCompatActivity  {
    Button textDetect,file,objectdetect;

    private CompoundButton autoFocus;
    private CompoundButton useFlash;
    private Button srate,tshare;
    int speechrate;
    private TextToSpeech tts;
    private AlertDialog enableNotificationListenerAlertDialog;
    private static final String FOR_FIRST_TIME="for first time";
    private static final String ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners";
    private static final String ACTION_NOTIFICATION_LISTENER_SETTINGS = "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS";

    public void ShowDialog() {

        final AlertDialog.Builder popDialog = new AlertDialog.Builder(this);
        final SeekBar seek = new SeekBar(this);
        int value = 0;
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("mySharedPrefsFilename", Context.MODE_PRIVATE);
        value = prefs.getInt("seekBarValue", 1);
        seek.setProgress(value);
        seek.setMax(3);
        popDialog.setView(seek);
        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
                //Update textview value
                //              tv.setText("Value : " + progress);
            }
            public void onStartTrackingTouch(SeekBar arg0) {
                // TODO Auto-generated method stub
            }
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Update your preferences only when the user has finished moving the seek bar
                SharedPreferences prefs =getApplicationContext().getSharedPreferences("mySharedPrefsFilename", Context.MODE_PRIVATE);
                // Don't forget to call commit() when changing preferences.
                prefs.edit().putInt("seekBarValue", seekBar.getProgress()).commit();
                speechrate = prefs.getInt("seekBarValue", 1);

                tts.setSpeechRate(speechrate);

            }
        });
        // Button
        popDialog.setMessage("Set speech rate").setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        popDialog.create();
        popDialog.show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        textDetect=(Button) findViewById(R.id.text_detection_button);
        file=(Button) findViewById(R.id.read_file_button);
        objectdetect=(Button) findViewById(R.id.detect_object_button);
        autoFocus = (CompoundButton) findViewById(R.id.auto_focus_id);
        useFlash = (CompoundButton) findViewById(R.id.auto_flash_switch_id);
        srate = (Button) findViewById(R.id.srate);
        tshare = (Button) findViewById(R.id.tshare);

        TextToSpeech.OnInitListener listener =
                new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(final int status) {
                        if (status == TextToSpeech.SUCCESS) {
                            Log.d("TTS", "Text to speech engine started successfully.");
                            tts.setLanguage(Locale.US);
                        } else {
                            Log.d("TTS", "Error starting the text to speech engine.");
                        }
                    }
                };
        tts = new TextToSpeech(this.getApplicationContext(), listener);
        if(!isNotificationServiceEnabled()){
            enableNotificationListenerAlertDialog = buildNotificationServiceAlertDialog();
            enableNotificationListenerAlertDialog.show();
        }

        SharedPreferences prefs=getSharedPreferences("com.google.android.gms.samples.ocrreader",MODE_PRIVATE);
        if(prefs.getBoolean("FOR_FIRST_TIME",true)){
            showTut();
            prefs.edit().putBoolean("FOR_FIRST_TIME",false).commit();
        }

        textDetect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tts.speak("Text Detection Started",TextToSpeech.QUEUE_FLUSH,null,"DEFAULT");
                Intent intent2=new Intent(MainActivity.this, OcrCaptureActivity.class);
                intent2.putExtra(OcrCaptureActivity.AutoFocus, autoFocus.isChecked());
                intent2.putExtra(OcrCaptureActivity.UseFlash, useFlash.isChecked());
                startActivity(intent2);
            }
        });

        file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, FileActivity.class));
            }
        });

        objectdetect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tts.speak("Object Detection Started",TextToSpeech.QUEUE_FLUSH,null,"DEFAULT");
                Intent intent1=new Intent(MainActivity.this,DetectorActivity.class);
                startActivity(intent1);
                //startActivity(new Intent(MainActivity.this, ObjectDetectionActivity.class));
            }
        });
        srate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowDialog();
            }
        });
        //Here we need to share the link to google play
        tshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "Here is the share content body";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });

    }

    private void showTut() {
        //Toast.makeText(this.getApplicationContext(),"Here You Go..",Toast.LENGTH_LONG).show();
        ClingManager mClingManager=new ClingManager(this);

        mClingManager.addCling(new Cling.Builder(this)
                .setTitle("Welcome to eSPEAKK!")
                .setContent("An application that will read everything for you")
                .build());
        String content = "Help read out any text to you\n\nRead notifications for you\n\nDetect any object near you";
        mClingManager.addCling(new Cling.Builder(this)
                .setTitle("This app can...")
                .setContent(content)
                .build());
        mClingManager.addCling(new Cling.Builder(this)
                .setTitle("SPEECH RATE")
                .setContent("Set the text speech rate")
                .setTarget(new com.majeur.cling.ViewTarget(this, R.id.srate))
                .build());
        mClingManager.addCling(new Cling.Builder(this)
                .setTitle("TEXT SIZE")
                .setContent("Edit text size here")
                .setTarget(new com.majeur.cling.ViewTarget(this, R.id.text_size_button))
                .build());
        mClingManager.addCling(new Cling.Builder(this)
                .setTitle("Speak File")
                .setContent("Tap here to speak the file contents")
                .setTarget(new com.majeur.cling.ViewTarget(this, R.id.read_file_button))
                .build());
        mClingManager.addCling(new Cling.Builder(this)
                .setTitle("AutoFocus")
                .setContent("Slide to enable/disable auto focus")
                .setTarget(new com.majeur.cling.ViewTarget(this, R.id.auto_focus_id))
                .build());
        mClingManager.addCling(new Cling.Builder(this)
                .setTitle("Flash")
                .setContent("Slide this to enable/disable flash on image")
                .setTarget(new com.majeur.cling.ViewTarget(this, R.id.auto_flash_switch_id))
                .build());
        mClingManager.addCling(new Cling.Builder(this)
                .setTitle("Detect Text")
                .setContent("Press this to detect any text on image")
                .setTarget(new com.majeur.cling.ViewTarget(this, R.id.text_detection_button))
                .build());
        mClingManager.addCling(new Cling.Builder(this)
                .setTitle("Detect Object")
                .setContent("Detect Object by tapping here")
                .setTarget(new com.majeur.cling.ViewTarget(this, R.id.detect_object_button))
                .build());
        mClingManager.addCling(new Cling.Builder(this)
                .setTitle("Read Notification")
                .setContent("Allow These app To listen all notifications")
                .build());
        mClingManager.addCling(new Cling.Builder(this)
                .setTitle("Go ahead..")
                .setContent("You are ready to use app now")
                .build());
        mClingManager.start();
    }

    private boolean isNotificationServiceEnabled(){
        String pkgName = getPackageName();
        final String flat = Settings.Secure.getString(getContentResolver(),
                ENABLED_NOTIFICATION_LISTENERS);
        if (!TextUtils.isEmpty(flat)) {
            final String[] names = flat.split(":");
            for (int i = 0; i < names.length; i++) {
                final ComponentName cn = ComponentName.unflattenFromString(names[i]);
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.getPackageName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    private AlertDialog buildNotificationServiceAlertDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(R.string.notification_listener_service);
        alertDialogBuilder.setMessage(R.string.notification_listener_service_explanation);
        alertDialogBuilder.setPositiveButton(R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS));
                    }
                });
        alertDialogBuilder.setNegativeButton(R.string.no,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        return(alertDialogBuilder.create());
    }

}
