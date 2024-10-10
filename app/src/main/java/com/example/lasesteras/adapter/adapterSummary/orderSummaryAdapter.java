package com.example.lasesteras.adapter.adapterSummary;

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

public class orderSummaryAdapter extends RecyclerView.Adapter<orderSummaryAdapter.detailViewHolder> {


    private List<dishesModel> dishesModels;
    private Context mContext;
    TextView quantity;
    TextView SubTotal;
    int TotalProduct = 0;
    double total = 0;

    public orderSummaryAdapter(List<dishesModel> dishesModels, Context mContext, TextView quantity, TextView subTotal) {
        this.dishesModels = dishesModels;
        this.mContext = mContext;
        this.quantity = quantity;
        this.SubTotal = subTotal;

        for (int i = 0; i < dishesModels.size(); i++) {

            double price = Double.parseDouble(dishesModels.get(i).getPricePlate());

            int cant = Integer.parseInt(dishesModels.get(i).getQuantity());

            TotalProduct = TotalProduct + cant;

            double total2 = price * cant;

            total = total + Double.parseDouble("" + total2);

            dishesModels.get(i).setTotal(String.valueOf(total2));
        }
        quantity.setText("" + "S/. " + total + "0");
        SubTotal.setText("" + "S/. " + total + "0");
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

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull detailViewHolder holder, int position) {


        dishesModel plate = dishesModels.get(position);

        holder.namePlateOrder.setText(plate.getNamePlate());
        holder.quantityOrder.setText("(" + plate.getQuantity() + ")");
        holder.priceOrder.setText("S/" + plate.getPricePlate());
        holder.priceTotalOrder.setText("" + "S/" + plate.getTotalQuantity() + "0");

    }

    @Override
    public int getItemCount() {
        return dishesModels.size();
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
