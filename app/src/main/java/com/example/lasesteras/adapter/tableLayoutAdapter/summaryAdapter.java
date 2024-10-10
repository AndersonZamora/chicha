package com.example.lasesteras.adapter.tableLayoutAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lasesteras.R;
import com.example.lasesteras.model.dishesModel;

import java.util.List;

public class summaryAdapter extends RecyclerView.Adapter<summaryAdapter.detailViewHolder> {


    private final List<dishesModel> dishesModels;
    private final Context mContext;
    TextView quantity;
    double TotalProduct = 0;
    double total = 0;

    public summaryAdapter(List<dishesModel> dishesModels, Context mContext, TextView quantity) {
        this.dishesModels = dishesModels;
        this.mContext = mContext;
        this.quantity = quantity;

        for (dishesModel list : dishesModels) {
            double price = Double.parseDouble(list.getPricePlate());
            int cant = Integer.parseInt(list.getQuantity());
            TotalProduct = (price * cant);
            total = total + TotalProduct;

            list.setTotalQuantity(String.valueOf(TotalProduct));
        }
        setTotal(quantity);
    }

    @SuppressLint("SetTextI18n")
    private void setTotal(TextView quantity) {
        quantity.setText("" + "S/. " + total + "0");
    }

    @NonNull
    @Override
    public detailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new detailViewHolder(
                LayoutInflater.from(mContext)
                        .inflate(R.layout.item_order_list,
                                parent,
                                false
                        ));
    }


    @Override
    public void onBindViewHolder(@NonNull detailViewHolder holder, int position) {

        dishesModel plate = dishesModels.get(position);
        holder.namePlateOrder.setText(plate.getNamePlate());
        setDe(holder, plate);
    }

    @SuppressLint("SetTextI18n")
    private void setDe(@NonNull detailViewHolder holder, dishesModel plate) {
        holder.quantityOrder.setText("(" + plate.getQuantity() + ")");
        holder.priceOrder.setText("S/" + plate.getPricePlate());
        holder.priceTotalOrder.setText("" + "S/" + plate.getTotalQuantity() + "0");
    }

    @Override
    public int getItemCount() {
        return dishesModels.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class detailViewHolder extends RecyclerView.ViewHolder {
        TextView namePlateOrder, quantityOrder, priceOrder, priceTotalOrder;

        public detailViewHolder(@NonNull View itemView) {
            super(itemView);
            namePlateOrder = itemView.findViewById(R.id.namePlateOrder);
            quantityOrder = itemView.findViewById(R.id.quantityOrder);
            priceOrder = itemView.findViewById(R.id.priceOrder);
            priceTotalOrder = itemView.findViewById(R.id.priceTotalOrder);
        }
    }
}
