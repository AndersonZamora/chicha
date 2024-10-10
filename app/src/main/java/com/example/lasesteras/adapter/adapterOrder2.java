package com.example.lasesteras.adapter;

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

public class adapterOrder2 extends RecyclerView.Adapter<adapterOrder2.viewHolder2> {

    List<dishesModel> modelOrders;
    Context mContext;
    TextView textView;
    Button button;

    double TotalProduct = 0;
    double total = 0;

    public adapterOrder2(List<dishesModel> modelOrders, Context mContext, TextView textView, Button button) {
        this.modelOrders = modelOrders;
        this.mContext = mContext;
        this.button = button;
        this.textView = textView;

        for (dishesModel list : modelOrders) {
            double price = Double.parseDouble(list.getPricePlate());
            int cant = Integer.parseInt(list.getQuantity());
            TotalProduct = (price * cant);
            total = total + TotalProduct;
        }
        textView.setText("S/ " + total + "0");
    }

    @NonNull
    @Override
    public viewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new viewHolder2(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item_order, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull viewHolder2 holder, int position) {

        holder.content.setText(modelOrders.get(position).getNamePlate());
        holder.item_number.setText(modelOrders.get(position).getPricePlate());
        holder.item_quantity.setText(modelOrders.get(position).getQuantity());

    }

    @Override
    public int getItemCount() {
        return modelOrders.size();
    }

    static class viewHolder2 extends RecyclerView.ViewHolder {

        TextView item_number;
        TextView content;
        TextView item_quantity;

        public viewHolder2(@NonNull View itemView) {
            super(itemView);
            item_number = itemView.findViewById(R.id.item_number);
            content = itemView.findViewById(R.id.content);
            item_quantity = itemView.findViewById(R.id.item_quantity);
        }

    }
}
