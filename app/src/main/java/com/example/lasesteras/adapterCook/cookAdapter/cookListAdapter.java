package com.example.lasesteras.adapterCook.cookAdapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lasesteras.R;
import com.example.lasesteras.model.orderDishes;
import com.example.lasesteras.model.orderModel;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class cookListAdapter extends RecyclerView.Adapter<cookListAdapter.viewHolderCook> {

    List<orderDishes> dishesList;
    Context mContext;

    DatabaseReference reference;

    public cookListAdapter(List<orderDishes> dishesList, Context mContext) {
        this.dishesList = dishesList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public viewHolderCook onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new viewHolderCook(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_list_cook, parent, false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolderCook holder, int position) {

        List<orderDishes> lstEmployees = dishesList;

        Collections.sort(lstEmployees, (o1, o2) -> o1.getHour().compareToIgnoreCase(o2.getHour()));

        orderDishes model = lstEmployees.get(position);
        holder.name_table.setText(model.getTableName());
        reference = FirebaseDatabase.getInstance().getReference();

        holder.delete_order.setOnClickListener(v -> {

            ProgressDialog mDialog = new ProgressDialog(mContext);
            mDialog.setMessage("Guardando..");
            mDialog.setCancelable(false);
            mDialog.show();

            for (orderModel dishes : model.getOrder()) {
                reference.child("tableList").child(model.getChild()).child("order").child(dishes.getId()).child("plateState").setValue("TRUE");
            }

            reference.child("tableList").child(model.getChild()).child("attended").setValue("FALSE").addOnSuccessListener(aVoid -> mDialog.dismiss()).addOnFailureListener(e -> Toast.makeText(mContext, "INTENTAR DE NUEVO", Toast.LENGTH_LONG).show());
        });

        if (model.getOrder() != null) {

            holder.mRecyclerView.setHasFixedSize(true);
            LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
            holder.mRecyclerView.setLayoutManager(layoutManager);
            cookOrderAdapter mAdapter = new cookOrderAdapter(model.getOrder());
            holder.mRecyclerView.setAdapter(mAdapter);
        }


        int[] androidColors = mContext.getResources().getIntArray(R.array.androidcolors);
        int randomAndroidColor = androidColors[new Random().nextInt(androidColors.length)];

        holder.relativeLayout.setBackgroundColor(randomAndroidColor);
    }

    @Override
    public int getItemCount() {
        return dishesList.size();
    }

    static class viewHolderCook extends RecyclerView.ViewHolder {
        TextView name_table;
        RecyclerView mRecyclerView;
        Button delete_order;
        RelativeLayout relativeLayout;

        public viewHolderCook(@NonNull View itemView) {
            super(itemView);
            name_table = itemView.findViewById(R.id.name_table);
            delete_order = itemView.findViewById(R.id.delete_order);
            mRecyclerView = itemView.findViewById(R.id.recycler_view_order);
            relativeLayout = itemView.findViewById(R.id.relative_back);
        }
    }
}
