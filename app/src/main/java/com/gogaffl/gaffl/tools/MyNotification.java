package com.gogaffl.gaffl.tools;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.NotificationCompat;
import com.gogaffl.gaffl.R;
import com.gogaffl.gaffl.home.view.HomeActivity;

public class MyNotification {

    private static NotificationCompat.Builder builder;
    Context context;


    private static final int NOTIFY_ID = 1001;

    public static void createNotification(Context context, String feedTime, String userName, Bitmap bitmap, String userTitle) {
        // TODO: create the NotificationCompat Builder
         builder = new NotificationCompat.Builder(context,MyApplication.getContext().getString(R.string.default_notification_channel_id));
        // TODO: Create the intent that will start the ResultActivity when the user
        // taps the notification or chooses an action button
        Intent intent = new Intent(context, HomeActivity.class);
        intent.putExtra("notifyID", NOTIFY_ID);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, NOTIFY_ID, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        // Store the notification ID so we can cancel it later in the ResultActivity
        // TODO: Set the three required items all notify_accept_join_request must have
        //  String message=userName+userTitle;
        // builder.setSmallIcon(R.drawable.ic_stat_sample_notification);
        builder.setSmallIcon(R.drawable.gaffl_white_icon);

//        builder.setContentTitle("Sample MyNotification");
        builder.setContentTitle("userName"+" "+"userTitle userTitle userTitle");
//        builder.setContentText("This is a sample MyNotification");
       // builder.setContentText(userTitle);

        // TODO: Set the notification to cancel when the user taps on it
        builder.setAutoCancel(true);

        // TODO: Set the large icon to be our app's launcher icon
        //  builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher_round));
        builder.setLargeIcon(bitmap);


        // TODO: Set the small subtext message
        //  builder.setSubText("Tap to view");

        // TODO: Set the content intent to launch our result activity
        builder.setContentIntent(pendingIntent);


        // TODO: Add an expanded layout to the notification
//        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
//        bigTextStyle.setBigContentTitle("This is big notification");
//        bigTextStyle.bigText(context.getResources().getString(R.string.LongMsg));
//        builder.setStyle(bigTextStyle);


        // TODO: Add action buttons to the MyNotification if they are supported
        // Use the same PendingIntent as we use for the main notification action
        builder.addAction(R.mipmap.ic_launcher, "Action 1", pendingIntent);
        builder.addAction(R.mipmap.ic_launcher, "Action 2", pendingIntent);
        // TODO: Set the lock screen visibility of the notification


        // TODO: Build the finished notification and then display it to the user
        android.app.Notification notification = builder.build();
        NotificationManager mgr = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        mgr.notify(NOTIFY_ID, notification);


    }



}
