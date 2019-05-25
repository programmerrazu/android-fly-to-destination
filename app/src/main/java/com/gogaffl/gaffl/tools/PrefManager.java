package com.gogaffl.gaffl.tools;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.AccessToken.USER_ID_KEY;

public class PrefManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "NewsViewsV2-welcome";

    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final String IS_SECOND_TIME_LAUNCH = "IsSecondTimeLaunch";

    private static final String FEED_ID_KEY = "feed_id_key";

    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.apply();
    }

    public void setSecondTimeLaunch(boolean isSecondTime) {
        editor.putBoolean(IS_SECOND_TIME_LAUNCH, isSecondTime);
        editor.apply();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public boolean isSecondTimeLaunch() {
        return pref.getBoolean(IS_SECOND_TIME_LAUNCH, false);
    }


    public boolean getSwitchStatus(String keyValue, boolean defaultValue) {
        return pref.getBoolean(keyValue, defaultValue);
    }

    public void setStatus(String key, boolean defaultValue) {
        editor.putBoolean(key, defaultValue);
    }

    public void setUserId(String userId) {
        editor.putString(USER_ID_KEY, userId);
        editor.apply();
    }

    public void setFeedId(int feedId) {
        editor.putInt(FEED_ID_KEY, feedId);
        editor.apply();
    }

    public String getUserId() {
        return pref.getString(USER_ID_KEY, "");
    }
  /*  public int getFeedId() {
        return pref.getInt(FEED_ID_KEY, 0);
    }*/


    private static List<String> list = new ArrayList<>();

    public void saveSelectedFeedId(String id) {

        list.add(id);
        Gson gson = new Gson();
        String json = gson.toJson(list);
        set(json);

    }

    public List<String> getSavedIds() {
        String str = getId();
        if (str != null && str.length() > 0) {
            Gson gson = new Gson();
            list = gson.fromJson(str, new TypeToken<List<String>>() {
            }.getType());
            return list;
        } else return new ArrayList<String>();
    }

    public void set(String value) {
        editor.putString(FEED_ID_KEY, value);
        editor.commit();
    }

    public String getId() {
        return pref.getString(FEED_ID_KEY, null);
    }

}
