package com.mintsnap;

import android.Manifest;
import android.Manifest.permission;

import android.app.AlertDialog;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import java.io.File;

public class SplashScreen extends Activity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;
    String name,country;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();

        name = pref.getString("i_name", null); // getting String
         country = pref.getString("i_country", null); // getting String




        if (Build.VERSION.SDK_INT >= 23) {
            ActivityCompat.requestPermissions(SplashScreen.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);
        } else {
            theprocess();
        }


    }


    public void theprocess(){
        File folder = new File(Environment.getExternalStorageDirectory()  +
                File.separator + "Mintsnap");
        boolean success = true;
        if (!folder.exists()) {
            folder.mkdirs();
        }
        new Handler().postDelayed(new Runnable() {
            @Override

            public void run() {
                if(name == null || country == null){
                    Intent i = new Intent(SplashScreen.this, info.class);
                    startActivity(i);
                    finish();
                }else{


                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(i);
                finish();

                }
            }
        }, SPLASH_TIME_OUT);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    theprocess();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(SplashScreen.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


}