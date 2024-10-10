package com.example.lasesteras.view.tableOrderView.tableOrderFragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.lasesteras.R;
import com.example.lasesteras.adapter.adpaterOrder.orderAdapter;
import com.example.lasesteras.model.dishesModel;
import com.example.lasesteras.model.tableModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class orderFragment extends Fragment {

    Context mContext;
    Button MakeOrder;
    TextView empty_order, total;
    List<dishesModel> listOder;
    orderAdapter mAdapter;
    DatabaseReference mReference;
    tableModel nameTable;

    public orderFragment(List<dishesModel> listOder, Context mContext, tableModel nameTable) {
        this.listOder = listOder;
        this.mContext = mContext;
        this.nameTable = nameTable;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_order, container, false);
        MakeOrder = root.findViewById(R.id.make_order);

        mReference = FirebaseDatabase.getInstance().getReference("tableList");

        total = root.findViewById(R.id.total);
        empty_order = root.findViewById(R.id.empty_order);
        int layout = R.layout.item_order_list;

        RecyclerView mRecyclerView = root.findViewById(R.id.recycler_view_o);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(layoutManager);
        getProduct(layout, mRecyclerView, listOder);


        getParentFragmentManager().setFragmentResultListener("key", this, (requestKey, result) -> {

            if (result == null) {

                empty_order.setVisibility(View.VISIBLE);
                MakeOrder.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.GONE);

            } else {
                empty_order.setVisibility(View.GONE);
                MakeOrder.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.VISIBLE);
                //noinspection unchecked
                listOder = (List<dishesModel>) result.getSerializable("product");
                getProduct(layout, mRecyclerView, listOder);
            }
        });

        MakeOrder.setOnClickListener(v -> {

            ProgressDialog mDialog = new ProgressDialog(mContext);
            mDialog.setMessage("Ingresando...");
            mDialog.setCancelable(false);
            mDialog.show();

            List<dishesModel> models = new ArrayList<>();

            for (dishesModel state : listOder) {
                if (!state.getPlateState().equals("TRUE")) {
                    models.add(state);
                }
            }

            PostOrder(mDialog, models);
        });

        return root;
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
        getActivity().onBackPressed();
    }

    private void getProduct(int layout, RecyclerView mRecyclerView, List<dishesModel> listOder) {

        if (listOder.size() == 0) {
            List<dishesModel> modelList = new ArrayList<>();
            mAdapter = new orderAdapter(mContext, modelList, layout, total, MakeOrder, empty_order);
        } else {
            mAdapter = new orderAdapter(mContext, listOder, layout, total, MakeOrder, empty_order);
        }
        mRecyclerView.setAdapter(mAdapter);
    }

}
