package com.erlite.qrlite;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;

public class SharedPrefs {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    int Theme;

    public SharedPrefs(Context context){

        sharedPreferences = context.getSharedPreferences("lite", Context.MODE_PRIVATE);

        //fetch theme for shared prefs
        Theme = sharedPreferences.getInt("ThemeValue", 1);


    }

    //save the theme state
    public void saveTheme(Context context){

        Theme = AppCompatDelegate.getDefaultNightMode();

        sharedPreferences = context.getSharedPreferences("lite", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        editor.putInt("ThemeValue", Theme);
        editor.apply();

    }

}
