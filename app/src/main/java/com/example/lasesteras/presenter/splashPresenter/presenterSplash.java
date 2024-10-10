package com.example.lasesteras.presenter.splashPresenter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.example.lasesteras.view.loginView.loginActivity;
import com.example.lasesteras.view.splashView.splashContract;

public class presenterSplash implements animationF, splashContract.splashPresenter {

    Context context;
    splashContract.splashView view = null;
    ActivityOptions options;

    public presenterSplash(Context context) {
        this.context = context;
    }

    @Override
    public void setAnimation(Animation topAnim, Animation bottomAnim, ImageView logo, ImageView name) {

        logo.setAnimation(topAnim);
        name.setAnimation(bottomAnim);

        int SPLASH_SCREEN = 3000;
        new Handler().postDelayed(() -> {

            Pair[] pairs = new Pair[2];
            pairs[0] = new Pair<View, String>(logo, "logo_image");
            pairs[1] = new Pair<View, String>(logo, "logo_name");

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {

                options = ActivityOptions.makeSceneTransitionAnimation((Activity) context, pairs);
                view.navigateToMain(options);
                /* context.startActivity(intent, options.toBundle());*/
            }
        }, SPLASH_SCREEN);
    }

    @Override
    public void attachView(splashContract.splashView splashView) {
        this.view = splashView;
    }
}
