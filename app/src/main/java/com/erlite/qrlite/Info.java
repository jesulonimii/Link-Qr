package com.erlite.qrlite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Info extends AppCompatActivity {

    SharedPrefs sharedPrefs;
    TextView fixIt, developer, librariesUsed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        //import shared prefs
        sharedPrefs = new SharedPrefs(this);
        
        
        fixIt = findViewById(R.id.fixIt);
        developer = findViewById(R.id.developer);
        librariesUsed = findViewById(R.id.librariesUsed);
        


        Toolbar toolbar = findViewById(R.id.infoToolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Info");
        toolbar.setTitleTextColor(getResources().getColor(R.color.themeColor1));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Info.super.onBackPressed();
            }
        });


        fixIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);

                String toMail = "erlite.team@gmail.com";
                String mailSubject = "Bug Report for Lite-qr App";
                // add email to send to with intent using putExtra function
                intent.putExtra(Intent.EXTRA_SUBJECT,  mailSubject);
                intent.putExtra(Intent.EXTRA_EMAIL,
                        new String[] { toMail });

                // set type of intent
                intent.setType("message/rfc822");

                // startActivity with intent with chooser
                // as Email client using createChooser function
                startActivity(
                        Intent
                                .createChooser(intent,
                                        "Select your mail app"));
            }
        });



        developer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=jesulonimii&link_click_id=849749756177782622"));
                    startActivity(intent);
                }
                catch(Exception e){
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/jesulonimii"));
                    startActivity(intent);
                }

            }
        });

        librariesUsed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(Info.this);
                alertDialog.setTitle("Open Source Licenses");
                alertDialog.setMessage(R.string.license);
                alertDialog.setNegativeButton("Close", null);
                alertDialog.show();
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

        super.onBackPressed();
        finish();
    }


}
