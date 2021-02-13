package com.erlite.qrlite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.erlite.qrlite.Fragments.Encode;
import com.erlite.qrlite.Fragments.Menu;
import com.erlite.qrlite.Fragments.Scan;
import com.gauravk.bubblenavigation.BubbleNavigationLinearView;
import com.gauravk.bubblenavigation.BubbleToggleView;
import com.gauravk.bubblenavigation.listener.BubbleNavigationChangeListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class MainActivity extends AppCompatActivity {

    public static BubbleNavigationLinearView bubbleNavigationLinearView;
    public static FragmentTransaction fragmentTransaction;
    BubbleNavigationChangeListener bubbleNavigationChangeListener;
    public static BubbleToggleView bubbleEncode, bubbleScan, bubbleMenu;
    public static FragmentManager fragmentManager;

    SharedPrefs sharedPrefs;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //import shared prefs
        sharedPrefs = new SharedPrefs(this);


        //declare variables
        bubbleNavigationLinearView = findViewById(R.id.bubbleNavBar);
        bubbleEncode = findViewById(R.id.bubble_encode);
        bubbleScan = findViewById(R.id.bubble_scan);
        bubbleMenu = findViewById(R.id.bubble_menu);



        //handling bubble clicks
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new Encode());
        fragmentTransaction.commit();

        bubbleNavigationLinearView.setNavigationChangeListener(new BubbleNavigationChangeListener() {
            @Override
            public void onNavigationChanged(View view, int position) {

                switch (position){

                    case 0:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_container, new Encode());
                        fragmentTransaction.commit();


                        break;

                    case 1:
                        fragmentManager = getSupportFragmentManager();
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_container, new Scan());
                        fragmentTransaction.commit();


                        break;

                    case 2:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_container, new Menu());
                        fragmentTransaction.commit();


                        break;


                }

            }
        });



    }


    //save prefs
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        sharedPrefs.saveTheme(this);

    }

    @Override
    public void onBackPressed(){

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
        System.exit(0);
    }

}


