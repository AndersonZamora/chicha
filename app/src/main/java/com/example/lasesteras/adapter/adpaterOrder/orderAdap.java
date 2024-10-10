package com.example.lasesteras.adapter.adpaterOrder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.lasesteras.R;
import com.example.lasesteras.model.dishesModel;

import java.util.ArrayList;
import java.util.List;

public class orderAdap extends BaseAdapter {

    Context mContext;
    List<dishesModel> dishesModels;

    public orderAdap(Context mContext, List<dishesModel> dishesModels) {
        this.mContext = mContext;
        this.dishesModels = dishesModels;
    }

    @Override
    public int getCount() {
        return dishesModels.size();
    }

    @Override
    public Object getItem(int position) {
        return dishesModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        dishesModel model = (dishesModel) getItem(position);

        convertView = LayoutInflater.from(mContext).inflate(R.layout.item_order_list, null);

        TextView namePlateOrder, quantityOrder, priceOrder, priceTotalOrder;


        namePlateOrder = convertView.findViewById(R.id.namePlateOrder);
        quantityOrder = convertView.findViewById(R.id.quantityOrder);
        priceOrder = convertView.findViewById(R.id.priceOrder);
        priceTotalOrder = convertView.findViewById(R.id.priceTotalOrder);


        namePlateOrder.setText(model.getNamePlate());
        quantityOrder.setText("(" + model.getQuantity() + ")");
        priceOrder.setText("S/" + model.getPricePlate());
        priceTotalOrder.setText("" + "S/" + model.getTotalQuantity() + "0");

        return convertView;
    }
}
