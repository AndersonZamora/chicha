package com.example.lasesteras.viewAdministrator.tableView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lasesteras.R;
import com.example.lasesteras.adapter.adapterTableA.tableAdapterA;
import com.example.lasesteras.model.employeesModel;
import com.example.lasesteras.model.tableModel;
import com.example.lasesteras.presenter.getInformationPresenter.presenterInformation;
import com.example.lasesteras.presenterAdmin.tablePresenterA.tablePresenterA;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Objects;

public class tableActivity extends AppCompatActivity implements tableContractA.attachViewTableA {

    private RecyclerView mRecyclerView;
    private tablePresenterA presenterM;
    private ProgressDialog mDialog;
    private Dialog dialog;
    private Dialog dialogAdd;
    private DatabaseReference mReference;
    private static final String KEY_DATE = "dateKey";
    private static final String SHARED_PREFERENCES = "prefLogin";
    private String date = null;
    private TextInputLayout nameTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);

        getUser();

        SharedPreferences preferences = getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        date = preferences.getString(KEY_DATE, null);
        TextView dateApp_dashboard = findViewById(R.id.dateApp_dashboard);
        dateApp_dashboard.setText(date);

        mReference = FirebaseDatabase.getInstance().getReference();
        presenterM = new tablePresenterA(new presenterInformation(), this);
        presenterM.attachView(this);
        Init();
        presenterM.getListTableA(mReference);

        findViewById(R.id.add_table).setOnClickListener(v -> LoadMesa());

    }

    private void getUser() {

        TextView textView = findViewById(R.id.fullName_table);
        employeesModel modelG = (employeesModel) getIntent().getSerializableExtra("model");
        assert modelG != null;
        textView.setText(modelG.getUsername());

    }

    private void LoadMesa() {

        presenterM.mWindowManagerAdd(dialogAdd);

        dialogAdd.setCancelable(false);
        dialogAdd.show();

        dialogAdd.findViewById(R.id.dialog_close_add).setOnClickListener(v -> dialogAdd.dismiss());
        dialogAdd.findViewById(R.id.save_table).setOnClickListener(v -> {
            nameTable = dialogAdd.findViewById(R.id.name_table);
            String table_name = Objects.requireNonNull(nameTable.getEditText()).getText().toString().trim();

            if (!table_name.isEmpty()) {
                tableModel model = new tableModel();
                model.setAttended("FALSE");
                model.setTableStatus("FALSE");
                model.setTableName(table_name);
                presenterM.saveTable(mReference, model);
            } else {
                Toast.makeText(tableActivity.this, "INGRESE UN NOMBRE", Toast.LENGTH_LONG).show();
            }
        });
    }

    void Init() {

        mDialog = new ProgressDialog(this);
        dialogAdd = new Dialog(this);
        dialog = new Dialog(this);

        nameTable = dialogAdd.findViewById(R.id.name_table);

        mRecyclerView = findViewById(R.id.recycler_table);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(tableActivity.this);
        mRecyclerView.setLayoutManager(layoutManager);
        RecyclerView.LayoutManager manager = new GridLayoutManager(tableActivity.this, getResources().getInteger(R.integer.number_of_grid_items));
        mRecyclerView.setLayoutManager(manager);
        presenterM.mWindowManager(dialog);
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
    public void showToast() {
        Toast.makeText(tableActivity.this, "Guardado", Toast.LENGTH_SHORT).show();
        dialogAdd.dismiss();
    }

    @Override
    public void showToastError() {
        Toast.makeText(tableActivity.this, "ERROR, Mesa no Guardada", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showError() {
        Toast.makeText(tableActivity.this, "Comprube su conexion a internet", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setListTableAd(ArrayList<tableModel> listTable) {

        tableAdapterA mAdapter = new tableAdapterA(listTable, tableActivity.this, dialog, date, presenterM);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}