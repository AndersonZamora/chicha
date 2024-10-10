package com.example.lasesteras.adapter.adapterSalesA;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lasesteras.R;
import com.example.lasesteras.model.reports;

import java.util.ArrayList;

public class salesAdapterA extends RecyclerView.Adapter<salesAdapterA.viewHolder> {

    ArrayList<reports> mReports;
    Context mContext;

    TextView reports_total;
    double total = 0;

    public salesAdapterA(ArrayList<reports> mReports, Context mContext, TextView reports_total) {
        this.mReports = mReports;
        this.mContext = mContext;
        this.reports_total = reports_total;

    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new viewHolder(
                LayoutInflater.from(mContext)
                        .inflate(R.layout.reports_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        reports model = mReports.get(position);
        holder.report.setText(model.getDate());
        holder.reports_total_v.setText("(" + model.getTotal().size() + ")");

        double TotalProduct = 0;

        for (String s : model.getTotal()) {

            double price = Double.parseDouble(s);
            TotalProduct = TotalProduct + price;
        }
        holder.reports_total_A.setText("S/" + TotalProduct + "0");

        total = total + TotalProduct;
        reports_total.setTextColor(Color.BLACK);
        reports_total.setText("S/" + total + "0");
    }

    @Override
    public int getItemCount() {
        return mReports.size();
    }

    class viewHolder extends RecyclerView.ViewHolder {

        TextView report;
        TextView reports_total_A;
        TextView reports_total_v;
        //RecyclerView mRecyclerView;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            report = itemView.findViewById(R.id.reports_date);
            reports_total_A = itemView.findViewById(R.id.reports_total_A);
            reports_total_v = itemView.findViewById(R.id.reports_total_v);
            //mRecyclerView = itemView.findViewById(R.id.reports_recyclerView_price);
        }
    }
}
