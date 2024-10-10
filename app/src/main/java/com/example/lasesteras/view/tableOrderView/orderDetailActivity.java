package com.example.lasesteras.view.tableOrderView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.lasesteras.R;
import com.example.lasesteras.adapter.adapterSummary.orderSummaryAdapter;
import com.example.lasesteras.adapter.tableLayoutAdapter.summaryAdapter;
import com.example.lasesteras.model.dishesModel;
import com.example.lasesteras.model.tableModel;
import com.example.lasesteras.view.checkRoleView.CheckRoleActivity;
import com.example.lasesteras.view.mainView.mainActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class orderDetailActivity extends AppCompatActivity {

    DatabaseReference mReference;
    tableModel nameTable;
    List<dishesModel> listOder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        mReference = FirebaseDatabase.getInstance().getReference("tableList");

        Button MakeOrder = findViewById(R.id.MakeOrder);
        RecyclerView mRecyclerView = findViewById(R.id.list_shopping);
        TextView quantity = findViewById(R.id.quantity);

        String state = getIntent().getStringExtra("state");
        nameTable = (tableModel) getIntent().getSerializableExtra("name");
        //noinspection unchecked
        listOder = (List<dishesModel>) getIntent().getSerializableExtra("list");

        assert listOder != null;
        assert state != null;
        assert nameTable != null;

        if (listOder != null) {
            if (state.equals("true")) {
                MakeOrder.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.VISIBLE);

            } else if (state.equals("false")) {
                MakeOrder.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
            }
        } else {
            MakeOrder.setVisibility(View.VISIBLE);
            MakeOrder.setVisibility(View.GONE);
        }

        mRecyclerView = findViewById(R.id.list_shopping);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(orderDetailActivity.this));
        summaryAdapter mAdapter = new summaryAdapter(listOder, this, quantity);
        mRecyclerView.setAdapter(mAdapter);
        findViewById(R.id.add_plat).setOnClickListener(v -> onBackPressed());


        MakeOrder.setOnClickListener(v -> {
            ProgressDialog mDialog = new ProgressDialog(orderDetailActivity.this);
            mDialog.setMessage("Ingresando...");
            mDialog.setCancelable(false);
            mDialog.show();

            List<dishesModel> models = new ArrayList<>();

            for (dishesModel st : listOder) {
                if (!st.getPlateState().equals("TRUE")) {
                    models.add(st);
                }
            }
            PostOrder(mDialog, models);
        });
    }

    private void PostOrder(ProgressDialog mDialog, List<dishesModel> models) {

        dishesModel model;
        int con;

        if (listOder.size() == models.size()) {
            con = 0;
        } else {
            con = listOder.size();
        }
        for (dishesModel a : models) {
            model = a;
            con = con + 1;
            mReference.child(nameTable.getChildId()).child("order").child(String.valueOf(con)).setValue(model);
        }

        Date date = new Date();
        @SuppressLint("SimpleDateFormat") DateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");
        Log.e("hora: ", hourFormat.format(date));

        mReference.child(nameTable.getChildId()).child("tableStatus").setValue("TRUE");
        mReference.child(nameTable.getChildId()).child("hour").setValue(hourFormat.format(date));
        mReference.child(nameTable.getChildId()).child("attended").setValue("TRUE");
        mDialog.dismiss();
        models.clear();
        Intent intent = new Intent(orderDetailActivity.this, CheckRoleActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}