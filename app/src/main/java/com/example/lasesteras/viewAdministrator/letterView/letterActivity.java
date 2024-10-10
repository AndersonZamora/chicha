package com.example.lasesteras.viewAdministrator.letterView;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import com.example.lasesteras.R;
import com.example.lasesteras.model.employeesModel;

public class letterActivity extends AppCompatActivity {

    private static final String TYPE_ = "type";
    private static final String TYPE_DRINKS = "Bebidas";
    private static final String TYPE_MENU = "Menú";
    private static final String TYPE_ADDITIONAL = "Extras";
    private static final String KEY_DATE = "dateKey";
    private static final String SHARED_PREFERENCES = "prefLogin";
    employeesModel modelG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_letter);

        SharedPreferences preferences = getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        String date = preferences.getString(KEY_DATE, null);
        TextView dateApp_dashboard = findViewById(R.id.dateApp_dashboard);
        dateApp_dashboard.setText(date);

        findViewById(R.id.layoutTables).setOnClickListener(v -> startActivityType(TYPE_DRINKS));
        findViewById(R.id.layoutLetter).setOnClickListener(v -> startActivityType(TYPE_MENU));
        findViewById(R.id.layoutSales).setOnClickListener(v -> startActivityType(TYPE_ADDITIONAL));
        getUser();
    }

    private void getUser() {

        TextView textView = findViewById(R.id.fullName_letter);
        modelG = (employeesModel) getIntent().getSerializableExtra("model");
        assert modelG != null;
        textView.setText(modelG.getUsername());

    }

    private void startActivityType(String type) {
        Intent intent = new Intent(letterActivity.this, bringListActivity.class);
        intent.putExtra("model", modelG);
        intent.putExtra(TYPE_, type);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}