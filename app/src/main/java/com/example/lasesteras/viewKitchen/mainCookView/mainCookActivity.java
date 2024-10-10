package com.example.lasesteras.viewKitchen.mainCookView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Adapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lasesteras.R;
import com.example.lasesteras.adapterCook.cookAdapter.cookListAdapter;
import com.example.lasesteras.model.employeesModel;
import com.example.lasesteras.model.orderDishes;
import com.example.lasesteras.model.orderModel;
import com.example.lasesteras.viewProfile.profileView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class mainCookActivity extends AppCompatActivity {

    private static final String KEY_DATE = "dateKey";
    private static final String SHARED_PREFERENCES = "prefLogin";
    private employeesModel modelG;
    DatabaseReference mReference;
    RecyclerView mRecyclerView;
    private Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_cook);

        mReference = FirebaseDatabase.getInstance().getReference();

        SharedPreferences preferences = getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        String date = preferences.getString(KEY_DATE, null);
        TextView dateApp_dashboard = findViewById(R.id.dateApp_dashboard);
        dateApp_dashboard.setText(date);
        getUser();

        mRecyclerView = findViewById(R.id.recycler_view_cook);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        getListOrder();
    }

    private void getUser() {

        TextView textView = findViewById(R.id.fullName_order);
        modelG = (employeesModel) getIntent().getSerializableExtra("model");
        assert modelG != null;
        textView.setText(modelG.getUsername());

        textView.setOnClickListener(v -> {
            Intent intent = new Intent(mainCookActivity.this, profileView.class);
            intent.putExtra("model", modelG);
            startActivity(intent);
        });

    }

    private void getListOrder() {

        ProgressDialog mDialog = new ProgressDialog(mainCookActivity.this);
        mDialog.setMessage("Obteniendo lista de ordenes...");
        mDialog.setCancelable(false);
        mDialog.show();

        Date date = new Date();

        @SuppressLint("SimpleDateFormat") DateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");
        Log.e("hora: ", hourFormat.format(date));
        mReference.child("tableList").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    List<orderDishes> orderList2 = new ArrayList<>();


                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        String nameTable = dataSnapshot.child("tableName").getValue(String.class);
                        String statusTable = dataSnapshot.child("tableStatus").getValue(String.class);
                        String attended = dataSnapshot.child("attended").getValue(String.class);
                        String hour = dataSnapshot.child("hour").getValue(String.class);
                        String childId = dataSnapshot.getKey();

                        assert statusTable != null;

                        if (statusTable.equals("TRUE")) {
                            assert attended != null;
                            if (attended.equals("TRUE")) {

                                orderDishes model = new orderDishes();
                                model.setTableName(nameTable);
                                model.setChild(childId);
                                model.setHour(hour);

                                List<orderModel> list = new ArrayList<>();

                                for (DataSnapshot a : dataSnapshot.getChildren()) {
                                    for (DataSnapshot e : a.getChildren()) {

                                        String namePlate = e.child("namePlate").getValue(String.class);
                                        String quantity = e.child("quantity").getValue(String.class);
                                        String plateState = e.child("plateState").getValue(String.class);
                                        String childP = e.getKey();

                                        if (!plateState.equals("TRUE")) {
                                            list.add(new orderModel(namePlate, quantity, childP));
                                            model.setOrder(list);
                                        }
                                    }
                                }
                                orderList2.add(model);
                            }

                        }

                    }

                    cookListAdapter mAdapter = new cookListAdapter(orderList2, mainCookActivity.this);
                    mRecyclerView.setAdapter(mAdapter);
                    mDialog.dismiss();

                } else {
                    mDialog.dismiss();
                    Toast.makeText(mainCookActivity.this, "NO HAY ORDENES", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                mDialog.dismiss();
                Toast.makeText(mainCookActivity.this, "NO ORDENES", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void setIN(List<orderDishes> orderList, ProgressDialog mDialog) {
        cookListAdapter mAdapter = new cookListAdapter(orderList, mainCookActivity.this);
        mRecyclerView.setAdapter(mAdapter);
        mDialog.dismiss();
    }
}
