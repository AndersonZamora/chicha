package com.example.lasesteras.adapter.adpaterOrder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lasesteras.R;
import com.example.lasesteras.model.dishesModel;

import java.util.List;

public class orderAdapter extends RecyclerView.Adapter<orderAdapter.dishesAdapterViewHolder> {


    List<dishesModel> listModels;
    private final Context mContext;
    int layout;
    TextView quantity;
    int TotalProduct = 0;
    double total = 0;
    Button makeOrder;
    TextView empty_order;

    public orderAdapter(Context mContext, List<dishesModel> listModels, int layout, TextView quantity, Button makeOrder, TextView empty_order) {
        this.mContext = mContext;
        this.listModels = listModels;
        this.layout = layout;
        this.quantity = quantity;
        this.makeOrder = makeOrder;
        this.empty_order = empty_order;

        for (int i = 0; i < listModels.size(); i++) {

            double price = Double.parseDouble(listModels.get(i).getPricePlate());
            int cant = Integer.parseInt(listModels.get(i).getQuantity());
            TotalProduct = TotalProduct + cant;
            double total2 = price * cant;
            total = total + Double.parseDouble("" + total2);

            listModels.get(i).setTotal(String.valueOf(total2));
        }
        quantity.setText("" + "S/. " + total + "0");
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

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull dishesAdapterViewHolder holder, int position) {

        dishesModel plate = listModels.get(position);
        holder.namePlateOrder.setText(plate.getNamePlate());
        holder.quantityOrder.setText("(" + plate.getQuantity() + ")");
        holder.priceOrder.setText("S/" + plate.getPricePlate());
        holder.priceTotalOrder.setText("" + "S/" + plate.getTotal() + "0");

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
    public int getItemCount() {

        if (listModels.size() == 0) {
            // makeOrder.setVisibility(View.GONE);
            empty_order.setVisibility(View.VISIBLE);
        } else {
            //makeOrder.setVisibility(View.VISIBLE);
            empty_order.setVisibility(View.GONE);
        }

        return listModels.size();
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
