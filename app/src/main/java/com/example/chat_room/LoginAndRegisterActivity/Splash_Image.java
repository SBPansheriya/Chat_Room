package com.example.chat_room.LoginAndRegisterActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chat_room.R;
import com.google.firebase.auth.FirebaseAuth;

public class Splash_Image extends AppCompatActivity {

    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;
    Runnable runnable;
    int login;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_image);

        sharedPreferences = getSharedPreferences("myperf",MODE_PRIVATE);
        editor=sharedPreferences.edit();
        login = sharedPreferences.getInt("login",0);
//
//        auth = FirebaseAuth.getInstance();
//
//        if(auth.getCurrentUser()==null){
//            startActivity(new Intent(Splash_Image.this, LoginAndRegister_Activity.class));
//        }

        runnable = new Runnable() {
            @Override
            public void run() {
               // if(login==0) {
                    Intent intent = new Intent(getApplicationContext(), LoginWithEmail1.class);
                    startActivity(intent);
                    finish();
               // }
//               else {
//                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                    startActivity(intent);
//                    finish();
//                }
            }
        };
        Handler handler = new Handler();
        handler.postDelayed(runnable,5000);
    }
}