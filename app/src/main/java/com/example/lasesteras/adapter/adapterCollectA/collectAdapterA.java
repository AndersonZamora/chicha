package com.example.lasesteras.adapter.adapterCollectA;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lasesteras.R;
import com.example.lasesteras.model.dishesModel;
import com.example.lasesteras.model.salesModel;
import com.example.lasesteras.model.tableModel;
import com.example.lasesteras.presenterAdmin.tablePresenterA.collectPresenterA;

import java.util.List;

public class collectAdapterA extends RecyclerView.Adapter<collectAdapterA.dishesAdapterViewHolder> implements collectAdapterContract.attachViewCollect {


    List<dishesModel> listModels;
    private final Context mContext;
    int layout;
    TextView quantity;
    String totalSave;
    double TotalProduct = 0;
    double total = 0;
    Button makeOrder;
    salesModel sales = new salesModel();
    String date;
    tableModel recur;
    ProgressDialog mDialog;
    ProgressDialog cDialog;
    Dialog dialog;
    private collectPresenterA presenterA;

    public collectAdapterA(Context mContext, List<dishesModel> listModels, int layout, TextView quantity, Button makeOrder, String date, tableModel recur, Dialog dialog) {
        this.mContext = mContext;
        this.listModels = listModels;
        this.layout = layout;
        this.quantity = quantity;
        this.makeOrder = makeOrder;
        this.date = date;
        this.recur = recur;
        this.dialog = dialog;

        /*for (int i = 0; i < listModels.size(); i++) {

            double price = Double.parseDouble(listModels.get(i).getPricePlate());
            int cant = Integer.parseInt(listModels.get(i).getQuantity());
            TotalProduct = TotalProduct + cant;
            double total2 = price * cant;
            total = total + Double.parseDouble("" + total2);

            listModels.get(i).setTotal(String.valueOf(total2));
        }
        totalSave = String.valueOf(total);
        setTotal(quantity);*/

        for (dishesModel list : listModels) {
            double price = Double.parseDouble(list.getPricePlate());
            int cant = Integer.parseInt(list.getQuantity());
            TotalProduct = (price * cant);
            total = total + TotalProduct;
            list.setTotalQuantity(String.valueOf(TotalProduct));
        }
        totalSave = String.valueOf(total);
        setTotal(quantity);
    }

    @SuppressLint("SetTextI18n")
    private void setTotal(TextView quantity) {
        quantity.setText("" + "S/" + total + "0");
    }

    @NonNull
    @Override
    public dishesAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new dishesAdapterViewHolder(
                LayoutInflater.from(mContext)
                        .inflate(layout,
                                parent,
                                false
                        ));
    }

    @Override
    public void onBindViewHolder(@NonNull dishesAdapterViewHolder holder, int position) {

        dishesModel plate = listModels.get(position);
        holder.namePlateOrder.setText(plate.getNamePlate());
        setInfoPlate(holder, plate);

        tableModel model = new tableModel();
        mDialog = new ProgressDialog(mContext);
        cDialog = new ProgressDialog(mContext);

        makeOrder.setOnClickListener(v -> {

            sales = new salesModel(totalSave);
            presenterA = new collectPresenterA();
            presenterA.attachViewC(this);
            presenterA.saveCollect(model, sales, date, recur);
        });
    }

    @SuppressLint("SetTextI18n")
    private void setInfoPlate(@NonNull dishesAdapterViewHolder holder, dishesModel plate) {
        holder.quantityOrder.setText("(" + plate.getQuantity() + ")");
        holder.priceOrder.setText("S/" + plate.getPricePlate());
        holder.priceTotalOrder.setText("" + "S/" + plate.getTotalQuantity() + "0");
    }

    @Override
    public int getItemCount() {

        if (listModels.size() == 0) {
            makeOrder.setVisibility(View.GONE);
        } else {
            makeOrder.setVisibility(View.VISIBLE);
        }

        return listModels.size();
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public void shotProgressSaveMesa() {
        mDialog.setMessage("Guardando venta...");
        mDialog.setCancelable(false);
        mDialog.show();
    }

    @Override
    public void hideProgressSaveMesa() {
        mDialog.dismiss();
    }

    @Override
    public void shotProgressUpdateMesa() {
        cDialog.setMessage("Actualizando mesa...");
        cDialog.setCancelable(false);
        cDialog.show();
    }

    @Override
    public void hideProgressUpdateMesa() {
        cDialog.dismiss();
    }

    @Override
    public void toastView(String s) {
        Toast.makeText(mContext, s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void hideDialog() {
        dialog.dismiss();
    }

    static class dishesAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView namePlateOrder, quantityOrder, priceOrder, priceTotalOrder;

        public dishesAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            namePlateOrder = itemView.findViewById(R.id.namePlateOrder);
            quantityOrder = itemView.findViewById(R.id.quantityOrder);
            priceOrder = itemView.findViewById(R.id.priceOrder);
            priceTotalOrder = itemView.findViewById(R.id.priceTotalOrder);

        }
    }
}
