package com.example.lasesteras.viewAdministrator.letterView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lasesteras.R;
import com.example.lasesteras.adapterMenu.menuAdapter;
import com.example.lasesteras.model.dishesModel;
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

public class bringListActivity extends AppCompatActivity {

    private static final String TYPE_ = "type";
    private static final String TYPE_DRINKS = "Bebidas";
    private static final String TYPE_MENU = "Menú";
    private static final String TYPE_ADDITIONAL = "Extras";

    private static final String DRINKS = "drinks";
    private static final String MENU = "menu";
    private static final String ADDITIONAL = "extra";

    private static final String KEY_DATE = "dateKey";
    private static final String SHARED_PREFERENCES = "prefLogin";

    private String type_list;
    private Dialog dialogAdd;
    private String availableI = "TRUE";
    private TextInputLayout name_add, price_add;
    private DatabaseReference mReference;
    private String postType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bring_list);
        mReference = FirebaseDatabase.getInstance().getReference();

        SharedPreferences preferences = getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        String date = preferences.getString(KEY_DATE, null);
        TextView dateApp_dashboard = findViewById(R.id.dateApp_dashboard);
        dateApp_dashboard.setText(date);

        getUser();

        TextView types_text = findViewById(R.id.types_text);
        type_list = getIntent().getStringExtra(TYPE_);
        assert type_list != null;
        types_text.setText(type_list);
        list_type();
        findViewById(R.id.add_item).setOnClickListener(v -> addDialog());

    }

    private void getUser() {
        TextView textView = findViewById(R.id.fullName_order);
        employeesModel modelG = (employeesModel) getIntent().getSerializableExtra("model");
        assert modelG != null;
        textView.setText(modelG.getUsername());
    }

    private void addDialog() {
        initDialog();
        dialogAdd.setCancelable(false);
        dialogAdd.show();

        name_add = dialogAdd.findViewById(R.id.name_add);
        price_add = dialogAdd.findViewById(R.id.price_add);
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch available_id = dialogAdd.findViewById(R.id.available_id);
        available_id.setVisibility(View.GONE);
        ImageView close = dialogAdd.findViewById(R.id.dialog_close_add);
        availableSwitch();

        close.setOnClickListener(v -> dialogAdd.dismiss());
        dialogAdd.findViewById(R.id.save_item).setOnClickListener(v -> postFieldInformation());
    }

    private void postFieldInformation() {


        String name = Objects.requireNonNull(name_add.getEditText()).getText().toString().trim();
        String price = Objects.requireNonNull(price_add.getEditText()).getText().toString().trim();

        if (name.isEmpty()) {
            Toast.makeText(bringListActivity.this, "INGRESE UN NOMBRE", Toast.LENGTH_LONG).show();
            return;
        }
        if (price.isEmpty()) {
            Toast.makeText(bringListActivity.this, "INGRESE un MONTO", Toast.LENGTH_LONG).show();
            return;
        }
        dishesModel model = new dishesModel();
        model.setNamePlate(name);
        model.setPricePlate(price);
        model.setPlateState("FALSE");
        model.setAvailable(availableI);

        mReference.child(postType).push().setValue(model).addOnSuccessListener(aVoid -> {
            dialogAdd.dismiss();
            Toast.makeText(bringListActivity.this, "GUARDADO", Toast.LENGTH_LONG).show();
        }).addOnFailureListener(e -> {
            dialogAdd.dismiss();
            Toast.makeText(bringListActivity.this, "OCURRIO UN ERROR", Toast.LENGTH_LONG).show();
        });
    }

    private void availableSwitch() {
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch available_id = dialogAdd.findViewById(R.id.available_id);
        available_id.setOnClickListener(v -> {
            if (available_id.isChecked()) {
                availableI = "TRUE";
            } else {
                availableI = "FALSE";
            }
        });
    }

    private void initDialog() {

        dialogAdd = new Dialog(bringListActivity.this);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        //noinspection deprecation
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);

        int displayWidth = displayMetrics.widthPixels;
        int displayHeight = displayMetrics.heightPixels;

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialogAdd.getWindow().getAttributes());

 +

        layoutParams.width = dialogWindowWidth;
        layoutParams.height = dialogWindowHeight;
        dialogAdd.setContentView(R.layout.dialog_add_item);
        dialogAdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogAdd.getWindow().setAttributes(layoutParams);
    }

    private void setInfoAdapter(List<dishesModel> modelList) {
        RecyclerView mRecyclerView = findViewById(R.id.recycler_view_type);
        menuAdapter menuAdapter = new menuAdapter(modelList, this, postType);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(menuAdapter);
    }

    private void list_type() {

        switch (type_list) {

            case TYPE_DRINKS:
                get_list(DRINKS);
                break;
            case TYPE_MENU:
                get_list(MENU);
                break;
            case TYPE_ADDITIONAL:
                get_list(ADDITIONAL);
                break;
        }

    }

    private void get_list(String type) {

        postType = type;
        mReference.child(type).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    List<dishesModel> modelList = new ArrayList<>();

                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        dishesModel model = snapshot1.getValue(dishesModel.class);
                        assert model != null;
                        model.setChildId(snapshot1.getKey());
                        modelList.add(model);
                    }
                    setInfoAdapter(modelList);
                } else {
                    Toast.makeText(bringListActivity.this, "ERROR", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(bringListActivity.this, "CONTACTAR CON EL ADMINISTRADOR", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}