package kr.oklabs.meal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class Preference {
    private SharedPreferences mPref;
    private SharedPreferences.Editor mEditor;

    @SuppressLint("CommitPrefEdits")
    Preference(Context mContext, String prefName) {
        mPref = mContext.getSharedPreferences(prefName, 0);
        mEditor = mPref.edit();
    }

    public String getString(String key, String defValue) {
        return mPref.getString(key, defValue);
    }

    void putString(String key, String value) {
        mEditor.putString(key, value).commit();
    }

    void remove(String key) {
        mEditor.remove(key).commit();
    }
}