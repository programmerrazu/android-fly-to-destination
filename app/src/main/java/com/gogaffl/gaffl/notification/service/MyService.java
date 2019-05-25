package com.gogaffl.gaffl.notification.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.gogaffl.gaffl.notification.model.Feed;
import com.gogaffl.gaffl.notification.model.NotificationModel;
import com.gogaffl.gaffl.tools.AppConstants;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class MyService extends IntentService {
    public static final String TAG = "MyService";
    public static final String MY_SERVICE_MESSAGE = "myServiceMessage";
    public static final String MY_SERVICE_PAYLOAD = "myServicePayload";

    public MyService() {
        super("MyService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

//        Make the web service request
        MyWebService webService =
                MyWebService.retrofit.create(MyWebService.class);
        boolean forceRefresh = false;
        Call<NotificationModel> call = webService.notifyItems(forceRefresh ? "no-cache" : null, AppConstants.EMAIL, AppConstants.TOKEN);
        NotificationModel notification;
        ArrayList<Feed> feeds;

        try {
            notification = call.execute().body();
            Log.d("Notification",notification.toString());
            feeds = (ArrayList<Feed>) notification.getFeeds();
            Log.i(TAG, "onHandleIntent: " + feeds);

        } catch (IOException e) {
            e.printStackTrace();
            Log.i(TAG, "onHandleIntent: " + e.getMessage());
            return;
        }

//        Return the data to MainActivity
        Intent messageIntent = new Intent(MY_SERVICE_MESSAGE);
        messageIntent.putExtra(MY_SERVICE_PAYLOAD, feeds);
        LocalBroadcastManager manager =
                LocalBroadcastManager.getInstance(getApplicationContext());
        manager.sendBroadcast(messageIntent);
    }

}