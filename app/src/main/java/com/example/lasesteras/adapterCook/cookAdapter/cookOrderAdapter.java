package com.example.lasesteras.adapterCook.cookAdapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lasesteras.R;
import com.example.lasesteras.model.orderModel;

import java.util.ArrayList;
import java.util.List;

public class cookOrderAdapter extends RecyclerView.Adapter<cookOrderAdapter.viewHolderOrder> {

    List<orderModel> dishesList;

    public cookOrderAdapter(List<orderModel> dishesList) {
        this.dishesList = dishesList;
    }

    @NonNull
    @Override
    public viewHolderOrder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new viewHolderOrder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_order_cook, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolderOrder holder, int position) {

        orderModel dishes = dishesList.get(position);
        holder.text_1.setText(dishes.getPrice());
        holder.text_2.setText(dishes.getName());
    }

    @Override
    public int getItemCount() {

        int a = 0;

        try {
            a = dishesList.size();
        } catch (Exception e) {
            Log.e("vacia: ", e.getMessage());
        }
        return a;
    }

    static class viewHolderOrder extends RecyclerView.ViewHolder {
        TextView text_1;
        TextView text_2;

        public viewHolderOrder(@NonNull View itemView) {
            super(itemView);

            text_1 = itemView.findViewById(R.id.text_1);
            text_2 = itemView.findViewById(R.id.text_2);
        }
    }
}
