package com.gogaffl.gaffl.tools;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class MyActionCable extends WebSocketListener {

    private static final String AUTH_TOKEN = "";
    private OkHttpClient client;

    WebSocket webSocket = null;
    Intent intent;


    public static final int NORMAL_CLOSURE_STATUS = 1000;
    public static final String BROADCAST_ACTION = "com.supun.broadcasttest";
    Context context;

    public MyActionCable(Context context) {
        this.context = context;
        client = new OkHttpClient();
        intent=new Intent(BROADCAST_ACTION);
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
          /*  super.onOpen(webSocket, response);
            webSocket.send("Hello,it's SSaurel");
            webSocket.send("What's up");
            webSocket.send(ByteString.decodeHex("deadbeef"));
            webSocket.close(NORMAL_CLOSURE_STATUS, "Goodbye!");
*/
        //   webSocket.send("{\"command\":\"subscribe\", \"identifier\":\"{\\\"channel\\\":\\\"CommunicationsChannel\\\"}\"}");
        //   webSocket.send("{\"command\":\"subscribe\", \"identifier\":\"{\\\"channel\\\":\\\"PrivateChatChannel\\\",\\\"chat_room_id\\\":\\\"1\\\"}\"}");
        //  webSocket.send("{\"command\":\"subscribe\", \"identifier\":\"{\\\"channel\\\":\\\"FeedsChannel\\\",\\\"user_id\\\":\\\"1\\\"}\"}");
        webSocket.send("{\"command\":\"subscribe\", \"identifier\":\"{\\\"channel\\\":\\\"MobileAppNotificationsChannel\\\",\\\"user_id\\\":\\\"1\\\"}\"}");

    }


    @Override
    public void onMessage(WebSocket webSocket, String text) {
        // output("Receiving: " + text);
        try {
            JSONObject jsonBot = new JSONObject(text);
            Log.d("JSONObject", jsonBot.toString());
            //output("Receiving bytes: " + jsonBot.toString(), context);
//            intent.putExtra("msg", jsonBot.toString());
//            context.sendBroadcast(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes) {
        // output("Receiving bytes: " + bytes.hex(),context);
    }


    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        super.onClosing(webSocket, code, reason);
        webSocket.close(NORMAL_CLOSURE_STATUS, null);
        // output("Closing: " + code + "/" + reason,context);
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        super.onFailure(webSocket, t, response);
        //  output("Error: " + t.getMessage(), context);
    }


    private void output(String s, Context context) {
        //output.setText(output.getText().toString() + "\n\n" + s);
//        Toast.makeText(context, "notify "+s.toString(), Toast.LENGTH_SHORT).show();

      //  MyNotification.createNotification(context, "feedTime", "userName", "userPicture", "userTitle");
    }


   /* private void output(final String s, Context context) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                output.setText(output.getText().toString() + "\n\n" + s);
                MyNotification.createNotification(context);
                //startActivity(new Intent(ActionCableActivity.this,NotificationActivity.class));
            }
        });
    }*/

    public void startActionCable() {

       /* Request request = new Request.Builder().url("ws://echo.websocket.org").build();
        EchoWebSocketListener listener = new EchoWebSocketListener();
        WebSocket ws = client.newWebSocket(request, listener);
        client.dispatcher().executorService().shutdown();
        ws://198.168.0.112:3000/cable*/
        try {
            Request request = new Request.Builder().url("ws://198.168.0.103:3000/cable")
                    /* .removeHeader("Connection")
                     .addHeader("Connection","keep-alive,Upgrade")*/.build();
            MyActionCable listener = new MyActionCable(context);

            WebSocket ws = client.newWebSocket(request, listener);

            client.dispatcher().executorService().shutdown();
            client = new OkHttpClient();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
