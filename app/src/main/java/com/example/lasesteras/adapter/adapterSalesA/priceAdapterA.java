package com.example.lasesteras.adapter.adapterSalesA;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lasesteras.R;

import java.util.List;

public class priceAdapterA extends RecyclerView.Adapter<priceAdapterA.viewHolderPrice> {

    List<String> list;
    TextView textView;

    double TotalProduct = 0;

    public priceAdapterA(List<String> list, TextView textView) {
        this.list = list;
        this.textView = textView;

        for (int i = 0; i < list.size(); i++) {

            double price = Double.parseDouble(list.get(i));
            TotalProduct = TotalProduct + price;
        }
        textView.setText("S/" + TotalProduct + "0");
    }

    @NonNull
    @Override
    public viewHolderPrice onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new viewHolderPrice(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.reports_list_price, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolderPrice holder, int position) {

        // String price = list.get(position);
        holder.reports_price.setText("(" + list.size() + ")");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class viewHolderPrice extends RecyclerView.ViewHolder {
        TextView reports_price;

        public viewHolderPrice(@NonNull View itemView) {
            super(itemView);
            reports_price = itemView.findViewById(R.id.reports_price);
        }
    }
}
