package com.example.lasesteras.presenter.validationsPresenter;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.lasesteras.R;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class presenterValidation implements validationF {

    private final Context mContext;

    public presenterValidation(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public boolean validateUsername(TextInputLayout username) {

        String user = Objects.requireNonNull(username.getEditText()).getText().toString().trim().toLowerCase();

        if (user.isEmpty()) {
            username.setError(mContext.getString(R.string.val_empty));
            return false;
        } else {
            username.setError(null);
            username.setErrorEnabled(false);
            return true;
        }
    }

    @Override
    public boolean validatePassword(TextInputLayout password) {
        String pass = Objects.requireNonNull(Objects.requireNonNull(password.getEditText()).getText()).toString().trim().toLowerCase();
        if (pass.isEmpty()) {
            password.setError(mContext.getString(R.string.val_empty));
            return false;
        } else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }

    @Override
    public boolean CONNECTIVITY_SERVICE() {

        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

}
