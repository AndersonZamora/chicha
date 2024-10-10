package com.example.lasesteras.view.mainView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lasesteras.R;
import com.example.lasesteras.adapter.adpaterTable.tableAdapter;
import com.example.lasesteras.model.employeesModel;
import com.example.lasesteras.model.tableModel;
import com.example.lasesteras.presenter.getInformationPresenter.presenterInformation;
import com.example.lasesteras.presenter.mainPresenter.presenterMain;
import com.example.lasesteras.viewProfile.profileView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class mainActivity extends AppCompatActivity implements mainContract.attachViewTable {

    private RecyclerView mRecyclerView;
    presenterMain presenterM;
    ProgressDialog mDialog;

    private static final String KEY_DATE = "dateKey";
    private static final String SHARED_PREFERENCES = "prefLogin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences preferences = getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        String date = preferences.getString(KEY_DATE, null);
        TextView dateApp_dashboard = findViewById(R.id.dateApp_dashboard);
        dateApp_dashboard.setText(date);

        Init();
        getUser();

        DatabaseReference mReference = FirebaseDatabase.getInstance().getReference();
        presenterM = new presenterMain(new presenterInformation());
        presenterM.attachView(this);
        presenterM.getListTable(mReference);
    }

    private void getUser() {

        TextView textView = findViewById(R.id.fullName_order);
        employeesModel model = (employeesModel) getIntent().getSerializableExtra("model");
        assert model != null;
        textView.setText(model.getUsername());

        textView.setOnClickListener(v -> {
            Intent intent = new Intent(mainActivity.this, profileView.class);
            intent.putExtra("model", model);
            startActivity(intent);
        });
    }

    void Init() {
        mDialog = new ProgressDialog(this);
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mainActivity.this);
        mRecyclerView.setLayoutManager(layoutManager);
        RecyclerView.LayoutManager manager = new GridLayoutManager(mainActivity.this, getResources().getInteger(R.integer.number_of_grid_items));
        mRecyclerView.setLayoutManager(manager);
    }

    @Override
    public void showProgressBar() {
        mDialog.setMessage("Cargando lista...");
        mDialog.setCancelable(false);
        mDialog.show();
    }

    @Override
    public void hideProgressBar() {
        mDialog.dismiss();
    }

    @Override
    public void showError() {
        Toast.makeText(mainActivity.this, "Comprube su conexion a internet", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setListTableMain(ArrayList<tableModel> listTable) {
        tableAdapter mAdapter = new tableAdapter(listTable, mainActivity.this);
        mRecyclerView.setAdapter(mAdapter);
    }
}