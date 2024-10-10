package com.example.lasesteras.adapter.adapterTableA;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lasesteras.R;
import com.example.lasesteras.adapter.adapterCollectA.collectAdapterA;
import com.example.lasesteras.model.dishesModel;
import com.example.lasesteras.model.tableModel;
import com.example.lasesteras.presenterAdmin.tablePresenterA.adapterPresenterA;
import com.example.lasesteras.presenterAdmin.tablePresenterA.tablePresenterA;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class tableAdapterA extends RecyclerView.Adapter<tableAdapterA.tableAdapterViewHolder> implements adapterTableContract.attachViewAdapterA {

    private final ArrayList<tableModel> mList;
    private final Context mContext;
    private final Dialog mDialog;
    private Dialog dialog;
    private final String date;
    private ProgressDialog cDialog;
    private adapterPresenterA adapterPresenterA;
    private Dialog dialogUpdate;
    private final tablePresenterA presenterM;

    public tableAdapterA(ArrayList<tableModel> mList, Context mContext, Dialog mDialog, String date, tablePresenterA presenterM) {
        this.mList = mList;
        this.mContext = mContext;
        this.mDialog = mDialog;
        this.date = date;
        this.presenterM = presenterM;
    }

    @NonNull
    @Override
    public tableAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new tableAdapterViewHolder(
                LayoutInflater.from(mContext)
                        .inflate(R.layout.table_row,
                                parent,
                                false
                        ));
    }

    @Override
    public void onBindViewHolder(@NonNull tableAdapterViewHolder holder, int position) {

        tableModel mTablet = mList.get(position);
        String name = mTablet.getTableName();
        holder.name.setText(name);
        DatabaseReference mReference = FirebaseDatabase.getInstance().getReference();
        holder.cardView.setOnClickListener(v -> {

            TextView name_table = mDialog.findViewById(R.id.name_table_add);
            name_table.setText(mTablet.getTableName());

            holder.collect_table.setOnClickListener(collect -> {
                mDialog.dismiss();
                adapterPresenterA = new adapterPresenterA();
                adapterPresenterA.attachView(this);
                mWindowManager();
                adapterPresenterA.collect_table(mTablet, dialog, mReference);
                dialog.setCancelable(false);
                dialog.show();
            });
            holder.edit_table.setOnClickListener(edit -> {

                mDialog.dismiss();
                dialogUpdate = new Dialog(mContext);
                presenterM.mWindowManagerAdd(dialogUpdate);
                dialogUpdate.setCancelable(false);
                dialogUpdate.show();

                TextInputLayout nameTable = dialogUpdate.findViewById(R.id.name_table);

                Objects.requireNonNull(nameTable.getEditText()).setText(name);

                dialogUpdate.findViewById(R.id.save_table).setOnClickListener(v12 -> {

                    String nameTab = nameTable.getEditText().getText().toString().trim();
                    if (!nameTab.equals(name) && !nameTab.isEmpty()) {
                        mReference.child("tableList").child(mTablet.getChildId()).child("tableName").setValue(nameTab);
                        dialogUpdate.dismiss();
                        Toast.makeText(mContext, "MESA ACTUALIZADA", Toast.LENGTH_SHORT).show();
                    } else {
                        dialogUpdate.dismiss();
                    }

                });
                dialogUpdate.findViewById(R.id.dialog_close_add).setOnClickListener(v1 -> dialogUpdate.dismiss());
            });
            holder.remove_table.setOnClickListener(remove -> {
                mReference.child("tableList").child(mTablet.getChildId()).removeValue().addOnSuccessListener(aVoid -> Toast.makeText(mContext, "MESA ELIMINADA", Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Toast.makeText(mContext, "OCURRIO UN ERROR INTENTAR DE NUEVO", Toast.LENGTH_SHORT).show());
                mDialog.dismiss();
            });

            if (mTablet.getAttended().equals("TRUE")) {
                holder.collect_table.setVisibility(View.GONE);
                holder.edit_table.setVisibility(View.GONE);
                holder.remove_table.setVisibility(View.GONE);
            } else if (mTablet.getTableStatus().equals("FALSE")) {
                holder.collect_table.setVisibility(View.GONE);
                holder.edit_table.setVisibility(View.VISIBLE);
                holder.remove_table.setVisibility(View.VISIBLE);
            } else if (mTablet.getTableStatus().equals("TRUE")) {
                holder.collect_table.setVisibility(View.VISIBLE);
                holder.edit_table.setVisibility(View.GONE);
                holder.remove_table.setVisibility(View.GONE);
            }

            mDialog.setCancelable(false);
            mDialog.show();

        });

        mDialog.findViewById(R.id.dialog_close).setOnClickListener(v -> mDialog.dismiss());

            if (mTablet.getAttended().equals("TRUE")) {
                holder.stateColor.setBackgroundColor(Color.YELLOW);
            } else if (mTablet.getTableStatus().equals("FALSE")) {
                holder.stateColor.setBackgroundColor(Color.GREEN);
            } else if (mTablet.getTableStatus().equals("TRUE")) {
                holder.stateColor.setBackgroundColor(Color.RED);
            }

    }

    @Override
    public void loadMesa() {
        cDialog = new ProgressDialog(mContext);
        cDialog.setMessage("Cargando orden de mesa...");
        cDialog.setCancelable(false);
        cDialog.show();
    }

    @Override
    public void closeLoadMesa() {
        cDialog.dismiss();
    }

    @Override
    public void errorLoadMesa(String error) {
        Toast.makeText(mContext, "ERROR: COMPRUEBE SU CONEXIÓN A INTERNET", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void Init(List<dishesModel> listModels, tableModel recur) {

        RecyclerView mRecyclerView = dialog.findViewById(R.id.recycler_collect_order);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(layoutManager);
        int layout = R.layout.item_order_list;
        TextView textView = dialog.findViewById(R.id.total_collect);
        Button button = dialog.findViewById(R.id.save_collect);
        collectAdapterA mAdapter = new collectAdapterA(mContext, listModels, layout, textView, button, date, recur, dialog);
        mRecyclerView.setAdapter(mAdapter);
        dialog.findViewById(R.id.dialog_close_collect).setOnClickListener(v -> dialog.dismiss());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class tableAdapterViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        CardView cardView;
        Button stateColor, collect_table, edit_table, remove_table;

        public tableAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.table_name);
            cardView = itemView.findViewById(R.id.card_view);
            stateColor = itemView.findViewById(R.id.stateColor);
            collect_table = mDialog.findViewById(R.id.collect_table);
            edit_table = mDialog.findViewById(R.id.edit_table);
            remove_table = mDialog.findViewById(R.id.remove_table);
        }
    }

    @Override
    public void mWindowManager() {
        //DIALOG - 2
        dialog = new Dialog(mContext);
        WindowManager windowManager = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        adapterPresenterA.mWindowManager(dialog, windowManager);
    }
}
