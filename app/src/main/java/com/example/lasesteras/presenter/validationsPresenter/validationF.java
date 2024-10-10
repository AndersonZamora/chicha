package com.example.lasesteras.presenter.validationsPresenter;

import com.google.android.material.textfield.TextInputLayout;

public interface validationF {

    boolean validateUsername(TextInputLayout username);

    boolean validatePassword(TextInputLayout password);

    boolean CONNECTIVITY_SERVICE();
}
