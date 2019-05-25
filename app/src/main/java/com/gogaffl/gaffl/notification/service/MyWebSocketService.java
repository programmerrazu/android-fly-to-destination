package com.gogaffl.gaffl.notification.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class MyWebSocketService extends Service {

    boolean status=false;
    public static final String BROADCAST_ACTION = "com.supun.broadcasttest";
    Intent intent;
    Handler handler;
    //WebSocketClient client;
    private OkHttpClient client;
    public static final int NORMAL_CLOSURE_STATUS = 1000;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        intent=new Intent(BROADCAST_ACTION);
        handler=new Handler();
        client = new OkHttpClient();

    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        Toast.makeText(this, "Service Started",Toast.LENGTH_LONG).show();// Let it continue running until it is stopped.
        ////////////////////////////////////////////////////////////////////////////////

        try {
            Request request = new Request.Builder().url("ws://198.168.0.123:3000/cable")
                     .removeHeader("Connection")
                     .addHeader("Connection","keep-alive,Upgrade").build();
            MyWebSocketService.EchoWebSocketListener listener = new MyWebSocketService.EchoWebSocketListener();

            WebSocket ws = client.newWebSocket(request, listener);

            client.dispatcher().executorService().shutdown();
            client = new OkHttpClient();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return START_STICKY;
    }





    public class EchoWebSocketListener extends WebSocketListener {

        public static final int NORMAL_CLOSURE_STATUS = 1000;

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            super.onOpen(webSocket, response);
//            webSocket.send("Hello,it's SSaurel");
//            webSocket.send("What's up");
//            webSocket.send(ByteString.decodeHex("deadbeef"));
//            webSocket.close(NORMAL_CLOSURE_STATUS, "Goodbye!");

            //   webSocket.send("{\"command\":\"subscribe\", \"identifier\":\"{\\\"channel\\\":\\\"CommunicationsChannel\\\"}\"}");
            //   webSocket.send("{\"command\":\"subscribe\", \"identifier\":\"{\\\"channel\\\":\\\"PrivateChatChannel\\\",\\\"chat_room_id\\\":\\\"1\\\"}\"}");
            //  webSocket.send("{\"command\":\"subscribe\", \"identifier\":\"{\\\"channel\\\":\\\"FeedsChannel\\\",\\\"user_id\\\":\\\"1\\\"}\"}");


            webSocket.send("{\"command\":\"subscribe\", \"identifier\":\"{\\\"channel\\\":\\\"MobileAppNotificationsChannel\\\",\\\"user_id\\\":\\\"1\\\"}\"}");
            webSocket.send("{\"command\":\"subscribe\", \"identifier\":\"{\\\"channel\\\":\\\"PrivateChatChannel\\\",\\\"chat_room_id\\\":\\\"1\\\"}\"}");
            Log.d("1", "open");
            status = true;

        }


        @Override
        public void onMessage(WebSocket webSocket, String text) {
            // output("Receiving: " + text);
            try {
                JSONObject jsonBot = new JSONObject(text);
                Log.d("JSONObject", jsonBot.toString());
              //  output("Receiving bytes: " + jsonBot.toString());
                Log.d("reply", jsonBot.toString());
                intent.putExtra("msg", jsonBot.toString());
                sendBroadcast(intent);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
           // output("Receiving bytes: " + bytes.hex());
        }


        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            super.onClosing(webSocket, code, reason);
            webSocket.close(NORMAL_CLOSURE_STATUS, null);
           // output("Closing: " + code + "/" + reason);
            status = false;
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            super.onFailure(webSocket, t, response);
           // output("Error: " + t.getMessage());
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }


}