package com.example.lasesteras.view.splashView;

import android.app.ActivityOptions;

public class splashContract {

    public interface splashView {
        void navigateToMain( ActivityOptions options);
    }
    public interface splashPresenter{
        void attachView(splashView splashView);
    }
}
