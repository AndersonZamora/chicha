package com.example.lasesteras.adapter.adpaterTable;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lasesteras.R;
import com.example.lasesteras.model.dishesModel;
import com.example.lasesteras.model.tableModel;
import com.example.lasesteras.view.tableOrderView.mainLayoutActivity;
import com.example.lasesteras.view.tableOrderView.tableOrderActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class tableAdapter extends RecyclerView.Adapter<tableAdapter.tableAdapterViewHolder> {


    private final ArrayList<tableModel> mList;
    private final Context mContext;

    public tableAdapter(ArrayList<tableModel> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
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
        String tableId = mTablet.getChildId();

        Log.e("tableId", tableId);

        holder.name.setText(name);
        holder.cardView.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, mainLayoutActivity.class);
            intent.putExtra("model", mTablet);
            mContext.startActivity(intent);
        });

        if (mTablet.getAttended().equals("TRUE")) {
            holder.stateColor.setBackgroundColor(Color.YELLOW);
        } else if (mTablet.getTableStatus().equals("FALSE")) {
            holder.stateColor.setBackgroundColor(Color.GREEN);
        } else if (mTablet.getTableStatus().equals("TRUE")) {
            holder.stateColor.setBackgroundColor(Color.RED);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class tableAdapterViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        CardView cardView;
        Button stateColor;

        public tableAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.table_name);
            cardView = itemView.findViewById(R.id.card_view);
            stateColor = itemView.findViewById(R.id.stateColor);
        }
    }
}
