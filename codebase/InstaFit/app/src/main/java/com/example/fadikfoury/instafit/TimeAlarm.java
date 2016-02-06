package com.example.fadikfoury.instafit;

import android.content.Intent;


import android.content.BroadcastReceiver;
import android.content.Context;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.R;




/**
 * Created by fadikfoury on 2/3/16.
 */
public class TimeAlarm extends BroadcastReceiver {



    public static String NOTIFICATION_ID = "notification-id";
    public static String NOTIFICATION = "It's gym time, get ready to be twice as awesome!";

    @Override
    public void onReceive(Context context, Intent intent) {


        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = intent.getParcelableExtra(NOTIFICATION);
        int id = intent.getIntExtra(NOTIFICATION_ID, 0);
        notificationManager.notify(id, notification);


    }
}

