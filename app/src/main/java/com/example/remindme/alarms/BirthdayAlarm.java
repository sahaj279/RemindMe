package com.example.remindme.alarms;
import android.app.Notification;

import com.example.remindme.MainActivity;
import com.example.remindme.NotificationMessage;
import com.example.remindme.R;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import java.util.Locale;
public class BirthdayAlarm extends BroadcastReceiver {
    Handler handler;
    TextToSpeech textToSpeech;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        String name = bundle.getString("name");
        String dobb = bundle.getString("dobb");
        String ma = bundle.getString("ma") ;
        String contact=bundle.getString("contact");
        String speakable = "It's " + dobb + "  and today is "+name +"'s birthday";
        try {
            handler = new Handler();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (!name.isEmpty() && !dobb.isEmpty()) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.DONUT) {
                            textToSpeech = new TextToSpeech(context.getApplicationContext(), new TextToSpeech.OnInitListener() {
                                @Override
                                public void onInit(int i) {
                                    if (i == TextToSpeech.SUCCESS) {
                                        textToSpeech.setLanguage(Locale.ENGLISH);
                                        textToSpeech.speak(speakable, TextToSpeech.QUEUE_FLUSH, null, "1");

                                    }
                                }
                            });
                        }
                    }

                }


            });
        } catch (Exception e) {
            e.printStackTrace();
        }


        //Click on Notification
        Intent intent1 = new Intent(context, NotificationMessage.class);
        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent1.putExtra("message", name);
        intent1.putExtra("date", dobb);
        intent1.putExtra("ma", ma);
        intent1.putExtra("contact", contact);
        //Notification Builder
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, intent1, PendingIntent.FLAG_UPDATE_CURRENT);//flag one shot
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "notify_001")
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);


        //here we set all the properties for the notification
        RemoteViews contentView = new RemoteViews(context.getPackageName(), R.layout.notification_layout);
        contentView.setImageViewResource(R.id.icon, R.mipmap.ic_launcher);
        PendingIntent pendingSwitchIntent = PendingIntent.getBroadcast(context, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        contentView.setOnClickPendingIntent(R.id.icon, pendingSwitchIntent);
        contentView.setTextViewText(R.id.message, name +"'s birthday");
        contentView.setTextViewText(R.id.date, dobb);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);

        mBuilder.setAutoCancel(true);
        mBuilder.setOngoing(true);
        mBuilder.setAutoCancel(true);
        mBuilder.setPriority(Notification.PRIORITY_MAX);
        mBuilder.setOnlyAlertOnce(true);
        mBuilder.build().flags = Notification.FLAG_NO_CLEAR | Notification.PRIORITY_MAX;
        mBuilder.setContent(contentView);
        mBuilder.setContentIntent(pendingIntent);

        //we have to create notification channel after api level 26
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "channel_id";
            NotificationChannel channel = new NotificationChannel(channelId, "channel name", NotificationManager.IMPORTANCE_HIGH);
            channel.enableVibration(true);
            channel.enableLights(true);
            channel.setLightColor(R.color.orange);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            notificationManager.createNotificationChannel(channel);
            mBuilder.setChannelId(channelId);

        }

        Notification notification = mBuilder.build();
        notificationManager.notify(1, notification);

    }

}
