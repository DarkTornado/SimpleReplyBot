package com.darktornado.simplereplybot;

import android.content.Context;
import android.content.SharedPreferences;

public class Bot {

    public static final String TAG = "_srb";
    private static final String PREFERENCES_NAME = "chatbot_appdata";

    public static String readString(Context ctx, String key) {
        return getPreferences(ctx).getString(key, null);
    }

    public static void saveString(Context ctx, String key, String value) {
        getPreferences(ctx).edit().putString(key, value).apply();
    }

    public static boolean readBoolean(Context ctx, String key, boolean defaultValue) {
        return getPreferences(ctx).getBoolean(key, defaultValue);
    }

    public static boolean readBoolean(Context ctx, String key) {
        return readBoolean(ctx, key, false);
    }

    public static void saveBoolean(Context ctx, String key, boolean value) {
        getPreferences(ctx).edit().putBoolean(key, value).apply();
    }

    public static void remove(Context ctx, String key) {
        getPreferences(ctx).edit().remove(key).apply();
    }

    private static SharedPreferences getPreferences(Context ctx) {
        return ctx.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

}
