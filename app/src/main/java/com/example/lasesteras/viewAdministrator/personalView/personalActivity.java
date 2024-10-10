package com.example.lasesteras.viewAdministrator.personalView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lasesteras.R;
import com.example.lasesteras.adapterPersonal.personalAdapter;
import com.example.lasesteras.model.employeesModel;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class personalActivity extends AppCompatActivity {

    private DatabaseReference mReference;
    private Dialog dialogAdd;
    private Spinner spinner_id;
    private TextInputLayout fullName_add, email_add, cellular_add, username_add, password_add;
    List<employeesModel> modelList;
    private employeesModel modelG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        getUser();

        mReference = FirebaseDatabase.getInstance().getReference();
        get_list();

        findViewById(R.id.add_item).setOnClickListener(v -> {
            addDialog();
        });
    }

    private void getUser() {

        TextView textView = findViewById(R.id.fullName_order);
        modelG = (employeesModel) getIntent().getSerializableExtra("model");
        assert modelG != null;
        textView.setText(modelG.getUsername());

    }

    private void addDialog() {
        initDialog();
        dialogAdd.setCancelable(false);
        dialogAdd.show();
        initSpinner();

        fullName_add = dialogAdd.findViewById(R.id.fullName_add);
        email_add = dialogAdd.findViewById(R.id.email_add);
        cellular_add = dialogAdd.findViewById(R.id.cellular_add);
        username_add = dialogAdd.findViewById(R.id.username_add);
        password_add = dialogAdd.findViewById(R.id.password_add);

        ImageView close = dialogAdd.findViewById(R.id.dialog_close_add);

        close.setOnClickListener(v -> dialogAdd.dismiss());
        dialogAdd.findViewById(R.id.save_item).setOnClickListener(v -> postFieldInformation());
    }

    private void postFieldInformation() {

        ProgressDialog mDialog = new ProgressDialog(personalActivity.this);
        mDialog.setMessage("Registrando...");
        mDialog.setCancelable(false);
        mDialog.show();

        String item = spinner_id.getSelectedItem().toString();
        String name = Objects.requireNonNull(fullName_add.getEditText()).getText().toString().trim();
        String email = Objects.requireNonNull(email_add.getEditText()).getText().toString().trim();
        String cellular = Objects.requireNonNull(cellular_add.getEditText()).getText().toString().trim();
        String username = Objects.requireNonNull(username_add.getEditText()).getText().toString().replace(" ", "").trim();
        String password = Objects.requireNonNull(password_add.getEditText()).getText().toString().replace(" ", "").trim();

        if (!validateName(name) | !validateEmail(email) | !validateCellular(cellular) | !validateUsername(username) | !validatePassword(password) | !validateRol(item)) {
            mDialog.dismiss();
            return;
        }

        employeesModel model = new employeesModel();
        model.setFullName(name);
        model.setEmail(email);
        model.setCellular(cellular);
        model.setUsername(username);
        model.setPassword(password);
        model.setRole(item);

        mReference.child("employees").child(username).setValue(model).addOnCompleteListener(task -> {
            Toast.makeText(personalActivity.this, "Registrado", Toast.LENGTH_LONG).show();
            mDialog.dismiss();
            dialogAdd.dismiss();
        }).addOnFailureListener(e -> {
            Toast.makeText(personalActivity.this, "OCURRIO UN ERROR - VOLVER A INTENTAR", Toast.LENGTH_LONG).show();
            mDialog.dismiss();
            dialogAdd.dismiss();
        });
    }

    private boolean validateName(String validate) {

        if (validate.isEmpty()) {
            fullName_add.setError(getString(R.string.val_empty));
            return false;
        } else {
            fullName_add.setError(null);
            fullName_add.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateEmail(String validate) {
        if (validate.isEmpty()) {
            email_add.setError(getString(R.string.val_empty));
            return false;
        } else {

            for (employeesModel a : modelList) {
                if (a.getEmail().equals(validate)) {
                    email_add.setError(getString(R.string.existsEmail));
                    return false;
                }
            }
            email_add.setError(null);
            email_add.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateCellular(String validate) {
        if (validate.isEmpty()) {
            cellular_add.setError(getString(R.string.val_empty));
            return false;
        } else {
            for (employeesModel a : modelList) {
                if (a.getCellular().equals(validate)) {
                    cellular_add.setError(getString(R.string.existsCellular));
                    return false;
                }
            }
            cellular_add.setError(null);
            cellular_add.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateUsername(String validate) {
        if (validate.isEmpty()) {
            username_add.setError(getString(R.string.val_empty));
            return false;
        } else {
            for (employeesModel a : modelList) {
                if (a.getUsername().equals(validate)) {
                    username_add.setError(getString(R.string.existsName));
                    return false;
                }
            }
            username_add.setError(null);
            username_add.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePassword(String validate) {
        if (validate.isEmpty()) {
            password_add.setError(getString(R.string.val_empty));
            return false;
        } else {
            password_add.setError(null);
            password_add.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateRol(String validate) {
        if (validate.equals("-- Rol --")) {
            Toast.makeText(personalActivity.this, "Seleccione un Rol", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    private void initSpinner() {

        List<String> list = new ArrayList<>();
        list.add("-- Rol --");
        list.add("waitress");
        list.add("cook");
        list.add("administrator");

        ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, list
        );
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_id = dialogAdd.findViewById(R.id.spinner_id);
        spinner_id.setAdapter(mAdapter);
    }

    private void get_list() {

        mReference.child("employees").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    modelList = new ArrayList<>();

                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        employeesModel model = snapshot1.getValue(employeesModel.class);
                        modelList.add(model);
                    }
                    setInfoAdapter(modelList);
                } else {
                    Toast.makeText(personalActivity.this, "ERROR", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(personalActivity.this, "CONTACTAR CON EL ADMINISTRADOR", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setInfoAdapter(List<employeesModel> modelList) {
        RecyclerView mRecyclerView = findViewById(R.id.recycler_view_personal);
        personalAdapter mPersonalAdapter = new personalAdapter(modelList, personalActivity.this, modelG.getUsername());
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mPersonalAdapter);
    }

    private void initDialog() {

        dialogAdd = new Dialog(personalActivity.this);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        //noinspection deprecation
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);

        int displayWidth = displayMetrics.widthPixels;
        int displayHeight = displayMetrics.heightPixels;

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialogAdd.getWindow().getAttributes());

        int dialogWindowWidth = (int) (displayWidth * 0.9f);
        int dialogWindowHeight = (int) (displayHeight * 0.85f);

        layoutParams.width = dialogWindowWidth;
        layoutParams.height = dialogWindowHeight;
        dialogAdd.setContentView(R.layout.dialog_add_personal);
        dialogAdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogAdd.getWindow().setAttributes(layoutParams);
    }
}