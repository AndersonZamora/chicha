package com.example.lasesteras.view.loginView;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import com.example.lasesteras.R;
import com.example.lasesteras.model.employeesModel;
import com.example.lasesteras.presenter.loginPresenter.presenterLogin;
import com.example.lasesteras.presenter.validationsPresenter.presenterValidation;
import com.example.lasesteras.view.checkRoleView.CheckRoleActivity;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class loginActivity extends AppCompatActivity implements loginContract.loginView {

    private presenterLogin mPresenterLogin;
    private presenterValidation mValidation;
    private TextInputLayout name, pass;
    private boolean state;
    private static final String SHARED_PREFERENCES = "prefLogin";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_NAME = "fullName";
    private static final String KEY_PHONE = "cellular";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_ROLE = "role";

    private SharedPreferences mPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        DatabaseReference mReference = FirebaseDatabase.getInstance().getReference();
        mPreferences = getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);

        mPresenterLogin = new presenterLogin(this, mReference);
        mPresenterLogin.attachViewLogin(this);
        mValidation = new presenterValidation(this);

        name = findViewById(R.id.username);
        pass = findViewById(R.id.password);
        Login();
    }

    private void Login() {

        findViewById(R.id.login).setOnClickListener(v -> {
            if (mValidation.CONNECTIVITY_SERVICE()) {
                if (!mValidation.validateUsername(name) | !mValidation.validatePassword(pass)) {
                    return;
                }
                mPresenterLogin.singInUser(Objects.requireNonNull(name.getEditText()).getText().toString().trim(),
                        Objects.requireNonNull(pass.getEditText()).getText().toString().trim(), state);
            } else {
                showAlertInternet();
            }
        });
    }

    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        if (view.getId() == R.id.remember_checkBox) {
            state = checked;
        }
    }

    private void showAlertInternet() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.app_name);
        builder.setMessage(R.string.Internet);
        builder.setPositiveButton(R.string.toAccept, null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void saveUser(employeesModel model) {

        SharedPreferences.Editor mEditor = mPreferences.edit();
        mEditor.putString(KEY_EMAIL, model.getEmail());
        mEditor.putString(KEY_NAME, model.getFullName());
        mEditor.putString(KEY_PHONE, model.getCellular());
        mEditor.putString(KEY_PASSWORD, model.getPassword());
        mEditor.putString(KEY_USERNAME, model.getUsername());
        mEditor.putString(KEY_ROLE, model.getRole());
        mEditor.apply();
    }

    @Override
    public void navigateToMainNoShape(employeesModel model) {
        Intent intent = new Intent(loginActivity.this, CheckRoleActivity.class);
        intent.putExtra("model", model);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void navigateToMain() {
        Intent intent = new Intent(loginActivity.this, CheckRoleActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}