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

public class adapterOrder extends RecyclerView.Adapter<adapterOrder.viewHolder> {

    List<dishesModel> modelOrders;
    Context mContext;
    TextView textView;
    double TotalProduct = 0;
    double total = 0;

    public adapterOrder(List<dishesModel> modelOrders, Context mContext, TextView textView) {
        this.modelOrders = modelOrders;
        this.mContext = mContext;
        this.textView = textView;

        for (dishesModel list : modelOrders) {
            double price = Double.parseDouble(list.getPricePlate());
            int cant = Integer.parseInt(list.getQuantity());
            TotalProduct = (price * cant);
            total = total + TotalProduct;
        }
        setTotal(textView);
    }

    @SuppressLint("SetTextI18n")
    private void setTotal(@NonNull TextView textView) {
        textView.setText("S/ " + total + "0");
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new viewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item_order, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        holder.content.setText(modelOrders.get(position).getNamePlate());
        holder.item_number.setText(modelOrders.get(position).getPricePlate());
        holder.item_quantity.setText(modelOrders.get(position).getQuantity());
    }

    @Override
    public int getItemCount() {
        return modelOrders.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class viewHolder extends RecyclerView.ViewHolder {

        TextView item_number;
        TextView content;
        TextView item_quantity;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            item_number = itemView.findViewById(R.id.item_number);
            content = itemView.findViewById(R.id.content);
            item_quantity = itemView.findViewById(R.id.item_quantity);
        }
    }
}
