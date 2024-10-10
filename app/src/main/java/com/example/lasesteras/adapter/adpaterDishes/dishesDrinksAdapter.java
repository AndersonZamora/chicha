package com.example.lasesteras.adapter.adpaterDishes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lasesteras.R;
import com.example.lasesteras.model.dishesModel;

import java.io.Serializable;
import java.util.List;

public class dishesDrinksAdapter extends RecyclerView.Adapter<dishesDrinksAdapter.dishesAdapterViewHolder> {

    Context mContext;
    List<dishesModel> models;
    List<dishesModel> listModels;
    FragmentManager parentFragmentManager;
    Bundle bundle = new Bundle();

    public dishesDrinksAdapter(Context mContext, List<dishesModel> models, List<dishesModel> listModels, FragmentManager parentFragmentManager) {
        this.mContext = mContext;
        this.models = models;
        this.listModels = listModels;
        this.parentFragmentManager = parentFragmentManager;
    }

    @NonNull
    @Override
    public dishesAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new dishesAdapterViewHolder(
                LayoutInflater.from(mContext)
                        .inflate(R.layout.menu_row,
                                parent,
                                false
                        ));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull dishesAdapterViewHolder holder, int position) {

        dishesModel model = models.get(position);
        holder.name_plate_row.setText(model.getNamePlate());
        holder.total_plate_row.setText("S/" + model.getPricePlate());

        holder.add_id.setEnabled(false);
        holder.remove_id.setEnabled(false);


        holder.Active_Check.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (holder.Active_Check.isChecked()) {

                holder.cant_id.setText("" + (Integer.parseInt(holder.cant_id.getText().toString().trim()) + 1));
                models.get(position).setQuantity(holder.cant_id.getText().toString());
                listModels.add(models.get(position));
                holder.add_id.setEnabled(true);
                holder.remove_id.setEnabled(true);

                bundle.putSerializable("product", (Serializable) listModels);
                parentFragmentManager.setFragmentResult("key", bundle);
            } else if (!holder.Active_Check.isChecked()) {
                holder.cant_id.setText("0");
                models.get(position).setQuantity("0");
                listModels.remove(models.get(position));
                holder.add_id.setEnabled(false);
                holder.remove_id.setEnabled(false);

                bundle.putSerializable("product", (Serializable) listModels);
                parentFragmentManager.setFragmentResult("key", bundle);
            }
        });
        holder.add_id.setOnClickListener(v -> {
            holder.cant_id.setText("" + (Integer.parseInt(holder.cant_id.getText().toString().trim()) + 1));
            models.get(position).setQuantity(holder.cant_id.getText().toString());

            bundle.putSerializable("product", (Serializable) listModels);
            parentFragmentManager.setFragmentResult("key", bundle);
        });
        holder.remove_id.setOnClickListener(v -> {

            if (Integer.parseInt(holder.cant_id.getText().toString()) > 1) {
                holder.cant_id.setText("" + (Integer.parseInt(holder.cant_id.getText().toString().trim()) - 1));
                models.get(position).setQuantity(holder.cant_id.getText().toString());

                bundle.putSerializable("product", (Serializable) listModels);
                parentFragmentManager.setFragmentResult("key", bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return models.size();
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
    static class dishesAdapterViewHolder extends RecyclerView.ViewHolder {

        TextView name_plate_row, total_plate_row, cant_id;
        CheckBox Active_Check;
        ImageView add_id, remove_id;
        LinearLayout linearLayout_menu_rey;

        public dishesAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            name_plate_row = itemView.findViewById(R.id.name_plate_row);
            total_plate_row = itemView.findViewById(R.id.totalPlate_);
            cant_id = itemView.findViewById(R.id.cant_id);
            linearLayout_menu_rey = itemView.findViewById(R.id.linearLayout_menu_rey);
            Active_Check = itemView.findViewById(R.id.Active_Check);

            add_id = itemView.findViewById(R.id.add_id);
            remove_id = itemView.findViewById(R.id.remove_id);

        }
    }
}
