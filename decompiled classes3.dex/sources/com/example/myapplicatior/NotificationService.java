package com.example.myapplicatior;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class NotificationService extends NotificationListenerService {
    Context context;
    String idData = "";
    String textData = "";
    String titleData = "";

    public void onCreate() {
        super.onCreate();
        this.context = getApplicationContext();
    }

    public void onNotificationPosted(StatusBarNotification statusBarNotification) {
        String packageName = statusBarNotification.getPackageName();
        Bundle bundle = statusBarNotification.getNotification().extras;
        if (bundle.getString("android.title") != null) {
            this.titleData = bundle.getString("android.title");
        } else {
            this.titleData = "";
        }
        if (bundle.getCharSequence("android.text") != null) {
            this.textData = bundle.getCharSequence("android.text").toString();
        } else {
            this.textData = "";
        }
        if (bundle.getCharSequence("android.id ") != null) {
            this.idData = bundle.getCharSequence("android.id ").toString();
        } else {
            this.idData = "";
        }
        Log.d("Package", packageName);
        Log.d("Title", this.titleData);
        Log.d("Text", this.textData);
        Log.d("ID", this.idData);
        Intent intent = new Intent("Msg");
        intent.putExtra("package", packageName);
        intent.putExtra("title", this.titleData);
        intent.putExtra("text", this.textData);
        intent.putExtra("id", this.idData);
        LocalBroadcastManager.getInstance(this.context).sendBroadcast(intent);
    }

    public void onNotificationRemoved(StatusBarNotification statusBarNotification) {
        Log.d("Msg", "Notification Removed");
    }
}
