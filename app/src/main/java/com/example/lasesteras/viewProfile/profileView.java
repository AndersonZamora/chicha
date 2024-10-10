package com.example.lasesteras.viewProfile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.lasesteras.R;
import com.example.lasesteras.model.employeesModel;
import com.example.lasesteras.view.loginView.loginActivity;
import com.example.lasesteras.view.splashView.splashActivity;
import com.example.lasesteras.viewAdministrator.personalView.personalActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class profileView extends AppCompatActivity {

    TextInputLayout lob_name, lob_email, lob_phone, username_add, lob_password, role_personal;
    Button save_update, sign_app;
    private DatabaseReference mReference;
    employeesModel model;
    private static final String SHARED_PREFERENCES = "prefLogin";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_NAME = "fullName";
    private static final String KEY_PHONE = "cellular";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_ROLE = "role";
    ProgressDialog mDialog;
    private SharedPreferences mPreferences;
    SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);


        mReference = FirebaseDatabase.getInstance().getReference();
        mPreferences = getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);


        mDialog = new ProgressDialog(profileView.this);

        lob_name = findViewById(R.id.lob_name);
        lob_email = findViewById(R.id.lob_email);
        lob_phone = findViewById(R.id.lob_phone);
        username_add = findViewById(R.id.username_add);
        lob_password = findViewById(R.id.lob_password);
        role_personal = findViewById(R.id.role_personal);

        save_update = findViewById(R.id.save_update);
        save_update.setBackgroundColor(Color.GREEN);


        sign_app = findViewById(R.id.sign_app);
        sign_app.setBackgroundColor(Color.RED);

        model = (employeesModel) getIntent().getSerializableExtra("model");

        String nameU = model.getFullName();
        String emailU = model.getEmail();
        String cellularU = model.getCellular();
        String passwordU = model.getPassword();
        String usernameU = model.getUsername();

        Objects.requireNonNull(lob_name.getEditText()).setText(nameU);
        Objects.requireNonNull(lob_email.getEditText()).setText(emailU);
        Objects.requireNonNull(lob_phone.getEditText()).setText(cellularU);
        Objects.requireNonNull(username_add.getEditText()).setText(model.getUsername());
        Objects.requireNonNull(lob_password.getEditText()).setText(passwordU);
        Objects.requireNonNull(role_personal.getEditText()).setText(model.getRole());


        save_update.setOnClickListener(v -> {

            String name = Objects.requireNonNull(lob_name.getEditText()).getText().toString().trim();
            String email = Objects.requireNonNull(lob_email.getEditText()).getText().toString().trim();
            String cellular = Objects.requireNonNull(lob_phone.getEditText()).getText().toString().trim();
            String password = Objects.requireNonNull(lob_password.getEditText()).getText().toString().replace(" ", "").trim();

            if (validateName(name) | !validateEmail(email) | !validateCellular(cellular) | !validatePassword(password)) {
                return;
            }

            if (validaNameUpdate(name, nameU, usernameU) | validateEmailUpdate(email, emailU, usernameU)
                    | validateCellularUpdate(cellular, cellularU, usernameU) | validatePasswordUpdate(password, passwordU, usernameU)) {
                starActivityL();
            }
            onBackPressed();
        });
        sign_app.setOnClickListener(v -> {
            SharedPreferences.Editor editor = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE).edit();
            editor.clear().apply();
            Intent intent = new Intent(profileView.this, loginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }

    private boolean validateName(String validate) {

        if (validate.isEmpty()) {
            lob_name.setError(getString(R.string.val_empty));
            return true;
        } else {
            lob_name.setError(null);
            lob_name.setErrorEnabled(false);
            return false;
        }
    }

    private boolean validateEmail(String validate) {
        if (validate.isEmpty()) {
            lob_email.setError(getString(R.string.val_empty));
            return false;
        } else {
            lob_email.setError(null);
            lob_email.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateCellular(String validate) {
        if (validate.isEmpty()) {
            lob_phone.setError(getString(R.string.val_empty));
            return false;
        } else {
            lob_phone.setError(null);
            lob_phone.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePassword(String validate) {
        if (validate.isEmpty()) {
            lob_password.setError(getString(R.string.val_empty));
            return false;
        } else {
            lob_password.setError(null);
            lob_password.setErrorEnabled(false);
            return true;
        }
    }

    ///
    private boolean validaNameUpdate(String validate, String exist, String child) {

        mEditor = mPreferences.edit();
        mDialog.setMessage("Actualizando Nombre...");
        mDialog.setCancelable(false);
        mDialog.show();

        if (!validate.equals(exist)) {
            mReference.child("employees").child(child).child("fullName").setValue(validate).addOnSuccessListener(aVoid -> {
                Toast.makeText(profileView.this, "Nombre actualizado", Toast.LENGTH_LONG).show();
                mEditor.putString(KEY_NAME, validate);
                mEditor.apply();
                mDialog.dismiss();
            }).addOnFailureListener(e -> {
                Toast.makeText(profileView.this, "ERROR NO SE ACTUALIZO", Toast.LENGTH_LONG).show();
                mDialog.dismiss();
            });
            mDialog.dismiss();
            lob_name.setError(null);
            lob_name.setErrorEnabled(false);
            return true;
        }
        mDialog.dismiss();
        return false;
    }

    private boolean validateEmailUpdate(String validate, String exist, String child) {

        mEditor = mPreferences.edit();
        mDialog.setMessage("Actualizando Email...");
        mDialog.setCancelable(false);
        mDialog.show();

        if (!validate.equals(exist)) {

            mReference.child("employees").child(child).child("email").setValue(validate).addOnSuccessListener(aVoid -> {
                Toast.makeText(profileView.this, "Email actualizado", Toast.LENGTH_LONG).show();
                mEditor.putString(KEY_EMAIL, validate);
                mEditor.apply();
                mDialog.dismiss();
            }).addOnFailureListener(e -> {
                Toast.makeText(profileView.this, "ERROR: NO SE ACTUALIZO Email", Toast.LENGTH_LONG).show();
                mDialog.dismiss();
            });
            mDialog.dismiss();
            lob_email.setError(null);
            lob_email.setErrorEnabled(false);
            return true;
        } else {
            mDialog.dismiss();
            return false;
        }
    }

    private boolean validateCellularUpdate(String validate, String exist, String child) {

        mEditor = mPreferences.edit();
        mDialog.setMessage("Actualizando Celular...");
        mDialog.setCancelable(false);
        mDialog.show();

        if (!validate.equals(exist)) {
            mReference.child("employees").child(child).child("cellular").setValue(validate).addOnSuccessListener(aVoid -> {
                Toast.makeText(profileView.this, "Celular actualizado", Toast.LENGTH_LONG).show();
                mEditor.putString(KEY_NAME, validate);
                mEditor.apply();
                mDialog.dismiss();
            }).addOnFailureListener(e -> {
                Toast.makeText(profileView.this, "ERROR: NO SE ACTUALIZO Celular", Toast.LENGTH_LONG).show();
                mDialog.dismiss();
            });
            mDialog.dismiss();
            lob_phone.setError(null);
            lob_phone.setErrorEnabled(false);
            return true;
        } else {
            mDialog.dismiss();
            return false;
        }
    }

    private boolean validatePasswordUpdate(String validate, String exist, String child) {

        mEditor = mPreferences.edit();
        mDialog.setMessage("Actualizando Contraseña...");
        mDialog.setCancelable(false);
        mDialog.show();

        if (!validate.equals(exist)) {
            mReference.child("employees").child(child).child("password").setValue(validate).addOnSuccessListener(aVoid -> {
                Toast.makeText(profileView.this, "Contraseña actualizada", Toast.LENGTH_LONG).show();
                mEditor.putString(KEY_PASSWORD, validate);
                mEditor.apply();
                mDialog.dismiss();
            }).addOnFailureListener(e -> {
                Toast.makeText(profileView.this, "ERROR: NO SE ACTUALIZO Contraseña", Toast.LENGTH_LONG).show();
                mDialog.dismiss();
            });
            mDialog.dismiss();
            lob_password.setError(null);
            lob_password.setErrorEnabled(false);
            return true;
        } else {
            mDialog.dismiss();
            return false;
        }
    }

    private void starActivityL() {
        Intent intent = new Intent(profileView.this, splashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}