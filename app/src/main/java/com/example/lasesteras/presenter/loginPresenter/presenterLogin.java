package com.example.lasesteras.presenter.loginPresenter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.lasesteras.R;
import com.example.lasesteras.model.employeesModel;
import com.example.lasesteras.model.tableModel;
import com.example.lasesteras.view.loginView.loginContract;
import com.example.lasesteras.view.mainView.mainActivity;
import com.example.lasesteras.view.mainView.mainContract;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class presenterLogin implements loginF, loginContract.sharedPresenter {


    private final Context mContext;
    private final DatabaseReference mReference;
    private employeesModel model;
    private loginContract.loginView view = null;

    public presenterLogin(Context mContext, DatabaseReference mReference) {
        this.mContext = mContext;
        this.mReference = mReference;
    }

    @Override
    public void singInUser(String user, String pass, boolean state) {

        ProgressDialog mDialog = new ProgressDialog(mContext);
        mDialog.setMessage("Ingresando...");
        mDialog.setCancelable(false);
        mDialog.show();
        Log.e("USER ", user);
        mReference.child("employees").child(user).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    String password = Objects.requireNonNull(snapshot.child("password").getValue()).toString();
                    if (password.equals(pass)) {

                        model = new employeesModel();
                        String username = Objects.requireNonNull(snapshot.child("username").getValue()).toString();
                        String fullName = Objects.requireNonNull(snapshot.child("fullName").getValue()).toString();
                        String email = Objects.requireNonNull(snapshot.child("email").getValue()).toString();
                        String cellular = Objects.requireNonNull(snapshot.child("cellular").getValue()).toString();
                        String rol = Objects.requireNonNull(snapshot.child("role").getValue()).toString();

                        model.setUsername(username);
                        model.setFullName(fullName);
                        model.setEmail(email);
                        model.setCellular(cellular);
                        model.setPassword(password);
                        model.setRole(rol);

                        if (state) {
                            mDialog.dismiss();
                            view.saveUser(model);
                            view.navigateToMain();
                        } else {
                            mDialog.dismiss();
                            view.navigateToMainNoShape(model);
                        }
                    } else {
                        mDialog.dismiss();
                        showAlertBill();
                    }
                } else {
                    mDialog.dismiss();
                    showAlertBill();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                mDialog.dismiss();
                showAlertInternet();
            }
        });
    }

    private void showAlertBill() {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(R.string.app_name);
        builder.setMessage(R.string.Incorrect);
        builder.setPositiveButton(R.string.toAccept, null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void attachViewLogin(loginContract.loginView loginView) {
        this.view = loginView;
    }

    private void showAlertInternet() {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(R.string.app_name);
        builder.setMessage(R.string.InternetError);
        builder.setPositiveButton(R.string.toAccept, null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
