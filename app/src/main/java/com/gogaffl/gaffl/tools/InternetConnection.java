package com.gogaffl.gaffl.tools;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;

/**
 * Created by M. Onirban on 18-Feb-19.
 * Time: 10: 50
 * Project: Huthat.
 * Email: onirban27@gmail.com
 */
public class InternetConnection {
    /**
     * CHECK WHETHER INTERNET CONNECTION IS AVAILABLE OR NOT
     */
//    public static boolean checkConnection(@NonNull Context context) {
//        return ((ConnectivityManager) Objects.requireNonNull(context.getSystemService
//                (Context.CONNECTIVITY_SERVICE))).getActiveNetworkInfo() != null;
//    }
    public static boolean isConnectedToInternet(@NonNull Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }
}
