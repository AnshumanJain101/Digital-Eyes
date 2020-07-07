package com.google.android.gms.samples.vision.ocrreader;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.ByteArrayOutputStream;

public class NotificationService extends NotificationListenerService {


    Context context;

    @Override

    public void onCreate() {

        super.onCreate();
        context = getApplicationContext();
        Log.e("aa","aaa");

    }
    @Override

    public void onNotificationPosted(StatusBarNotification sbn) {


        String pack = sbn.getPackageName();
        Log.e("calllll",pack);
        String ticker="";
        Bundle extras;
        String title="";
        String text="";
        String c="incallui";
        extras = sbn.getNotification().extras;
        title = extras.getString("android.title");

        if(!(pack.toLowerCase().contains(c.toLowerCase()))) {
            //  ticker = sbn.getNotification().tickerText.toString();
            ticker="aaa";
            text = extras.getCharSequence("android.text").toString();
            Log.e("Package", pack);
            Log.e("Ticker", ticker);
            Log.e("Title", title);
            Log.e("Text", text);
        }
        Log.e("upper","swfw");
        Intent msgrcv = new Intent("Msg");
        msgrcv.putExtra("package", pack);
        Log.e("middleeeee","kmfgrf");
        msgrcv.putExtra("ticker", ticker);
        msgrcv.putExtra("title", title);
        msgrcv.putExtra("text", text);
        Log.e("Belowwww","kfnwkf");
        LocalBroadcastManager.getInstance(context).sendBroadcast(msgrcv);


    }

    @Override

    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.i("Msg","Notification Removed");

    }
}
