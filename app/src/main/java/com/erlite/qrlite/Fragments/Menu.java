package com.erlite.qrlite.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.erlite.qrlite.Info;
import com.erlite.qrlite.MainActivity;
import com.erlite.qrlite.R;
import com.erlite.qrlite.Settings;
import com.google.android.material.snackbar.Snackbar;

import static android.content.Context.MODE_PRIVATE;


public class Menu extends Fragment {

    ImageView fbImage, twitterImage, igImage;
    LinearLayout infoCard, donateCard, settingsCard, exitCard;

    int theme, nightMode;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Activity activity;
    Context context;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);





    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_menu, container, false);


        //declare variables
        fbImage = mView.findViewById(R.id.ic_fb);
        twitterImage = mView.findViewById(R.id.ic_twitter);
        igImage = mView.findViewById(R.id.ic_ig);
        infoCard = mView.findViewById(R.id.infoCard);
        exitCard = mView.findViewById(R.id.exitCard);
        donateCard = mView.findViewById(R.id.donateCard);
        settingsCard = mView.findViewById(R.id.settingsCard);
        activity = getActivity();
        context = getActivity().getApplicationContext();




        settingsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, Settings.class);
                startActivity(intent);
                activity.finish();
            }
        });


        donateCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://bit.ly/erlite-donate"));
                startActivity(intent);
            }
        });

        infoCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, Info.class);
                startActivity(intent);
            }
        });



        exitCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitSnackbar();
            }
        });

        fbImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{  Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/erliteHQ"));
                    startActivity(intent);}
                catch (Exception e){
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com/erliteHQ"));
                    startActivity(intent);
                }

            }
        });

        twitterImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=erliteHQ&link_click_id=849749756177782622"));
                    startActivity(intent);
                }
                catch(Exception e){
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/erliteHQ"));
                    startActivity(intent);
                }

            }
        });

        igImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/_u/erlitehq"));
                    intent.setPackage("com.instagram.android");
                    startActivity(intent);
                }
                catch (Exception e){
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/_u/erlitehq"));
                    startActivity(intent);
                }
            }
        });




        return mView;

    }



    public void exitSnackbar(){
        final  Snackbar snackbar = Snackbar.make(getView(), "Exit the app?", Snackbar.LENGTH_SHORT);
        snackbar.setAction("Yes", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                activity.finish();
                System.exit(0);
            }
        }).setActionTextColor(getResources().getColor(R.color.themeColor1));


        snackbar.show();
    }


}
