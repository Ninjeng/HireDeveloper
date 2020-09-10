package com.theakatsuki.hiredevelopers.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.view.WindowManager.LayoutParams;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.SeekBar;
import android.widget.TextView;

import android.content.ContentResolver;
import android.os.Bundle;
import android.provider.Settings.SettingNotFoundException;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.theakatsuki.hiredevelopers.R;

public class SettingActivity extends AppCompatActivity {

    TextView Logout, NotificationSetting,BrightNessSetting, Result1;
    FirebaseUser firebaseUser;
    SeekBar seekBar;
    View view2;
    int brigtness;
    ContentResolver cResolver;
    Window window;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        getSupportActionBar().setTitle("Settings");
        Logout=findViewById(R.id.logout);
//        NotificationSetting=findViewById(R.id.txt_notification);
//        BrightNessSetting=findViewById(R.id.txt_brightness);
        seekBar=findViewById(R.id.seek_bar);
        Result1=findViewById(R.id.txt_result);
        view2=findViewById(R.id.view2);

        window=getWindow();
        cResolver=getContentResolver();
        seekBar.setKeyProgressIncrement(1);

        try{
            brigtness = Settings.System.getInt(cResolver, Settings.System.SCREEN_BRIGHTNESS);

        }catch(Settings.SettingNotFoundException e){
            Log.e("Error","Cannnot Access system Brigtness");
            e.printStackTrace();
        }

        seekBar.setProgress(brigtness);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                if(progress<=20)
                {
                    brigtness=20;
                }
                else
                {
                    brigtness = progress;
                }
                //Calculate the brightness percentage
                float perc = (brigtness /(float)255)*100;
                Result1.setText((int)perc +" %");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                Settings.System.putInt(cResolver, Settings.System.SCREEN_BRIGHTNESS, brigtness);

                LayoutParams layoutpars = window.getAttributes();

                layoutpars.screenBrightness = brigtness / (float)255;

                window.setAttributes(layoutpars);
            }
        });

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent=new Intent(SettingActivity.this,SplashActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
               startActivity(intent);
            }
        });
        
//        NotificationSetting.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                notificationSetting();
//            }
//        });
//
//        BrightNessSetting.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                brigntNessSetting();
//            }
//        });
        
    }

    private void brigntNessSetting() {



        if(view2.getVisibility()==View.GONE){
            view2.setVisibility(View.VISIBLE);
            seekBar.setVisibility(View.VISIBLE);
            Result1.setVisibility(View.VISIBLE);
        }
        else{
            view2.setVisibility(View.GONE);
            seekBar.setVisibility(View.GONE);
            Result1.setVisibility(View.GONE);
        }
    }

    private void notificationSetting() {
        AlertDialog.Builder builder=new AlertDialog.Builder(SettingActivity.this);
        builder.setTitle("Notification Settings");
        builder.setMessage("Do you want to turn off Notification");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(SettingActivity.this, "Notification will be Turn OFf", Toast.LENGTH_SHORT).show();

                Intent intent=new Intent(SettingActivity.this,SettingActivity.class);
                startActivity(intent);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();
    }
}