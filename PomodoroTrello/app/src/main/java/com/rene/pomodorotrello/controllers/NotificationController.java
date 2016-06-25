package com.rene.pomodorotrello.controllers;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;

import com.rene.pomodorotrello.R;
import com.rene.pomodorotrello.ui.pomodoro.PomodoroActivity;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by rene on 6/23/16.
 */

public class NotificationController {

    private final static int NOTIFICATION_ID = 999;
    private Context context;

    public NotificationController(Context context) {
        this.context = context;
    }

    public void generateNotification() {

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.pomodoro_small)
                        .setContentTitle(context.getString(R.string.notification_title))
                        .setContentText(context.getString(R.string.notification_text));

        notificationBuilder.setVibrate(new long[] {1000, 1000});
        notificationBuilder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);

        Intent resultIntent = new Intent(context, PomodoroActivity.class);

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        context,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        notificationBuilder.setContentIntent(resultPendingIntent);

        issueNotification(notificationBuilder);
    }

    private void issueNotification(NotificationCompat.Builder notificationBuilder) {

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }

    public void cancelNotification() {
        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        manager.cancel(NOTIFICATION_ID);
    }

}
