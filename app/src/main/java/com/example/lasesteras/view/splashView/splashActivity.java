package com.example.lasesteras.view.splashView;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.lasesteras.R;
import com.example.lasesteras.presenter.splashPresenter.presenterSplash;
import com.example.lasesteras.view.checkRoleView.CheckRoleActivity;
import com.example.lasesteras.view.loginView.loginActivity;
import com.example.lasesteras.view.mainView.mainActivity;

public class splashActivity extends AppCompatActivity implements splashContract.splashView {

    int SPLASH_SCREEN = 3000;
    presenterSplash splash;
    private static final String SHARED_PREFERENCES = "prefLogin";
    private static final String KEY_EMAIL = "email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_main);

        splash = new presenterSplash(this);
        splash.attachView(this);
        Animation topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        Animation bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        ImageView logo = findViewById(R.id.logo_app);
        ImageView name = findViewById(R.id.name_app);
        splash.setAnimation(topAnim, bottomAnim, logo, name);
        new Handler().postDelayed(this::startActivityLogin, SPLASH_SCREEN);
    }

    @Override
    public void navigateToMain(ActivityOptions options) {

    }

    void startActivityLogin() {

        SharedPreferences preferences = getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        String email = preferences.getString(KEY_EMAIL, null);
        Intent intent;
        if (email != null) {
            intent = new Intent(splashActivity.this, CheckRoleActivity.class);
        } else {
            intent = new Intent(splashActivity.this, loginActivity.class);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}