package edu.mobile.assignment;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class LectureNotifyReceiver extends BroadcastReceiver {
    public LectureNotifyReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String name = intent.getStringExtra("module");
        String room = intent.getStringExtra("room");


        Notification.Builder nBuilder = new Notification.Builder(context)
                .setContentTitle(name)
                .setContentInfo(room)
                .setSmallIcon(android.R.drawable.ic_lock_idle_alarm);
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0,nBuilder.build());

    }
}
