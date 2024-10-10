package com.example.lasesteras.viewAdministrator.mainAdministratorView;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lasesteras.R;
import com.example.lasesteras.model.employeesModel;
import com.example.lasesteras.viewAdministrator.letterView.letterActivity;
import com.example.lasesteras.viewAdministrator.personalView.personalActivity;
import com.example.lasesteras.viewAdministrator.salesView.salesActivity;
import com.example.lasesteras.viewAdministrator.tableView.tableActivity;
import com.example.lasesteras.viewProfile.profileView;

public class mainAdministratorActivity extends AppCompatActivity implements View.OnClickListener {

    TextView dateApp_dashboard;
    private static final String KEY_DATE = "dateKey";
    private static final String SHARED_PREFERENCES = "prefLogin";
    private employeesModel modelG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_administrator);

        getUser();
        SharedPreferences preferences = getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        String date = preferences.getString(KEY_DATE, null);

        dateApp_dashboard = findViewById(R.id.dateApp_dashboard);
        dateApp_dashboard.setText(date);

        LinearLayout layoutTables = findViewById(R.id.layoutTables);
        layoutTables.setOnClickListener(this);

        LinearLayout layoutLetter = findViewById(R.id.layoutLetter);
        layoutLetter.setOnClickListener(this);

        LinearLayout layoutSales = findViewById(R.id.layoutSales);
        layoutSales.setOnClickListener(this);

        LinearLayout layoutPersonal = findViewById(R.id.layoutPersonal);
        layoutPersonal.setOnClickListener(this);

    }

    private void getUser() {

        TextView textView = findViewById(R.id.fullName_order);
        modelG = (employeesModel) getIntent().getSerializableExtra("model");
        assert modelG != null;
        textView.setText(modelG.getUsername());

        textView.setOnClickListener(v -> {
            Intent intent = new Intent(mainAdministratorActivity.this, profileView.class);
            intent.putExtra("model", modelG);
            startActivity(intent);
        });

    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        if (id == R.id.layoutTables) {
            Intent intentTable = new Intent(mainAdministratorActivity.this, tableActivity.class);
            intentTable.putExtra("model", modelG);
            interActivity(intentTable);
        } else if (id == R.id.layoutLetter) {
            Intent intentLetter = new Intent(mainAdministratorActivity.this, letterActivity.class);
            intentLetter.putExtra("model", modelG);
            interActivity(intentLetter);
        } else if (id == R.id.layoutSales) {
            Intent intentOrders = new Intent(mainAdministratorActivity.this, salesActivity.class);
            intentOrders.putExtra("model", modelG);
            interActivity(intentOrders);
        } else if (id == R.id.layoutPersonal) {
            Intent intentPersonal = new Intent(mainAdministratorActivity.this, personalActivity.class);
            intentPersonal.putExtra("model", modelG);
            interActivity(intentPersonal);
        }
    }

    private void interActivity(Intent intent) {
        startActivity(intent);
    }

}