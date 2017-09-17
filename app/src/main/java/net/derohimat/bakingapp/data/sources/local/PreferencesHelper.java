package net.derohimat.bakingapp.data.sources.local;

import android.content.Context;
import android.content.SharedPreferences;

import net.derohimat.baseapp.util.BasePreferenceUtils;

public class PreferencesHelper extends BasePreferenceUtils {

    private static SharedPreferences mPref;

    private static final String KEY_WIDGET_ID = "widget_id";
    private static final String PREFERENCE_SYNCED = "baking_app_synced";

    public PreferencesHelper(Context context) {
        mPref = getSharedPreference(context);
    }

    public String getChosenRecipeName(int keySuffix) {
        return mPref.getString(KEY_WIDGET_ID + keySuffix, "");
    }

    public void saveChosenRecipeName(int keySuffix, String name) {
        mPref.edit().putString(KEY_WIDGET_ID + keySuffix, name).apply();
    }

    public void deleteRecipeName(int keySuffix) {
        mPref.edit().remove(KEY_WIDGET_ID + keySuffix).apply();
    }

    public void setRecipeListSynced(boolean flag) {
        mPref.edit().putBoolean(PREFERENCE_SYNCED, flag).apply();
    }

    public boolean isRecipeListSynced() {
        return mPref.getBoolean(PREFERENCE_SYNCED, false);
    }

}
