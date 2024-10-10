package com.example.lasesteras.adapter.tableLayoutAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lasesteras.R;
import com.example.lasesteras.model.dishesModel;

import java.io.Serializable;
import java.util.List;

public class adapterBeverages extends RecyclerView.Adapter<adapterBeverages.viewHolder> {

    List<dishesModel> modelClasses;
    List<dishesModel> listOrders;
    Context mContext;
    FragmentManager parentFragmentManager;
    Bundle bundle = new Bundle();

    public adapterBeverages(List<dishesModel> modelClasses, List<dishesModel> listOrders, Context mContext, FragmentManager parentFragmentManager) {
        this.modelClasses = modelClasses;
        this.listOrders = listOrders;
        this.mContext = mContext;
        this.parentFragmentManager = parentFragmentManager;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new viewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item_beverages, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        dishesModel model = modelClasses.get(position);
        holder.content.setText(model.getNamePlate());
        holder.item_number.setText(model.getPricePlate());

        holder.Active_Check.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (holder.Active_Check.isChecked()) {

                holder.cant_id.setText("" + (Integer.parseInt(holder.cant_id.getText().toString().trim()) + 1));

                modelClasses.get(position).setQuantity(holder.cant_id.getText().toString());
                listOrders.add(modelClasses.get(position));

                holder.add_id.setEnabled(true);
                holder.remove_id.setEnabled(true);

                bundle.putSerializable("product", (Serializable) listOrders);
                parentFragmentManager.setFragmentResult("key", bundle);

            } else if (!holder.Active_Check.isChecked()) {

                holder.cant_id.setText("0");


                modelClasses.get(position).setQuantity("0");
                listOrders.remove(modelClasses.get(position));
                holder.add_id.setEnabled(false);
                holder.remove_id.setEnabled(false);

                bundle.putSerializable("product", (Serializable) listOrders);
                parentFragmentManager.setFragmentResult("key", bundle);
            }
        });

        holder.add_id.setOnClickListener(v -> {

            holder.cant_id.setText("" + (Integer.parseInt(holder.cant_id.getText().toString().trim()) + 1));

            modelClasses.get(position).setQuantity(holder.cant_id.getText().toString());

            bundle.putSerializable("product", (Serializable) listOrders);
            parentFragmentManager.setFragmentResult("key", bundle);
        });
        holder.remove_id.setOnClickListener(v -> {

            if (Integer.parseInt(holder.cant_id.getText().toString()) > 1) {
                holder.cant_id.setText("" + (Integer.parseInt(holder.cant_id.getText().toString().trim()) - 1));

                modelClasses.get(position).setQuantity(holder.cant_id.getText().toString());
                bundle.putSerializable("product", (Serializable) listOrders);
                parentFragmentManager.setFragmentResult("key", bundle);
            }
        });

    }

    @Override
    public int getItemCount() {
        return modelClasses.size();
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
        TextView cant_id;
        CheckBox Active_Check;
        ImageView add_id;
        ImageView remove_id;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            item_number = itemView.findViewById(R.id.item_number);
            content = itemView.findViewById(R.id.content);
            Active_Check = itemView.findViewById(R.id.Active_Check);
            cant_id = itemView.findViewById(R.id.cant_id);
            add_id = itemView.findViewById(R.id.add_id);
            remove_id = itemView.findViewById(R.id.remove_id);
        }
    }
}
