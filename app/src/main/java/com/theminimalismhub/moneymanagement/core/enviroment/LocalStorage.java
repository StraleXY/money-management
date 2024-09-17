package com.theminimalismhub.moneymanagement.core.enviroment;

import android.content.Context;
import android.content.SharedPreferences;

public class LocalStorage {

    public static void putString(Context context, String prefsName, String key, String value) {
        SharedPreferences prefs;
        SharedPreferences.Editor edit;
        prefs = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE);
        edit=prefs.edit();
        edit.putString(key, value);
        edit.apply();
    }
    public static String getString(Context context, String prefsName, String key) {
        SharedPreferences pref = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE);
        return pref.getString(key, "");
    }

    public static void putInt(Context context, String prefsName, String key, Integer value) {
        SharedPreferences prefs;
        SharedPreferences.Editor edit;
        prefs = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE);
        edit=prefs.edit();
        edit.putInt(key, value);
        edit.apply();
    }

    public static Integer getInt(Context context, String prefsName, String key) {
        SharedPreferences pref = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE);
        return pref.getInt(key, 0);
    }

    public static Integer getInt(Context context, String prefsName, String key, Integer def) {
        SharedPreferences pref = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE);
        return pref.getInt(key, def);
    }

    public static void putFloat(Context context, String prefsName, String key, Float value) {
        SharedPreferences prefs;
        SharedPreferences.Editor edit;
        prefs = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE);
        edit=prefs.edit();
        edit.putFloat(key, value);
        edit.apply();
    }
    public static Float getFloat(Context context, String prefsName, String key, Float def) {
        SharedPreferences pref = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE);
        return pref.getFloat(key, def);
    }

    public static void putLong(Context context, String prefsName, String key, Long value) {
        SharedPreferences prefs;
        SharedPreferences.Editor edit;
        prefs = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE);
        edit=prefs.edit();
        edit.putLong(key, value);
        edit.apply();
    }
    public static Long getLong(Context context, String prefsName, String key) {
        SharedPreferences pref = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE);
        return pref.getLong(key, 0L);
    }

    public static void putBoolean(Context context, String prefsName, String key, Boolean value) {
        SharedPreferences prefs;
        SharedPreferences.Editor edit;
        prefs = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE);
        edit=prefs.edit();
        edit.putBoolean(key, value);
        edit.apply();
    }
    public static Boolean getBoolean(Context context, String prefsName, String key) {
        SharedPreferences pref = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE);
        return pref.getBoolean(key, false);
    }

    public static Boolean getBoolean(Context context, String prefsName, String key, Boolean def) {
        SharedPreferences pref = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE);
        return pref.getBoolean(key, def);
    }
}
