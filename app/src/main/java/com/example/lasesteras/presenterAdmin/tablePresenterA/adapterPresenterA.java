package com.example.lasesteras.presenterAdmin.tablePresenterA;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.lasesteras.R;
import com.example.lasesteras.adapter.adapterTableA.adapterTableContract;
import com.example.lasesteras.model.dishesModel;
import com.example.lasesteras.model.tableModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class adapterPresenterA implements adapterTableContract.window {


    private adapterTableContract.attachViewAdapterA adapterContractA;

    @Override
    public void attachView(adapterTableContract.attachViewAdapterA adapterContractA) {
        this.adapterContractA = adapterContractA;
    }

    @Override
    public void mWindowManager(Dialog dialog, WindowManager windowManager) {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        //noinspection deprecation
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);

        int displayWidth = displayMetrics.widthPixels;
        int displayHeight = displayMetrics.heightPixels;

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());

        int dialogWindowWidth = (int) (displayWidth * 0.9f);
        int dialogWindowHeight = (int) (displayHeight * 0.9f);

        layoutParams.width = dialogWindowWidth;
        layoutParams.height = dialogWindowHeight;
        dialog.setContentView(R.layout.dialog_collect_order);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setAttributes(layoutParams);
    }

    @Override
    public void collect_table(tableModel mTablet, Dialog dialog, DatabaseReference mReference) {

        TextView name_collect_table = dialog.findViewById(R.id.name_collect_table);
        name_collect_table.setText(mTablet.getTableName());

        adapterContractA.loadMesa();
        mReference.child("tableList").child(mTablet.getChildId()).child("order").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    adapterContractA.closeLoadMesa();
                    List<dishesModel> models = new ArrayList<>();
                    models.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        dishesModel model = dataSnapshot.getValue(dishesModel.class);
                        assert model != null;
                        model.setNamePlate(model.getNamePlate());
                        model.setPricePlate(model.getPricePlate());
                        model.setQuantity(model.getQuantity());
                        model.setTotal(model.getTotal());
                        model.setTotalQuantity(model.getTotalQuantity());
                        models.add(model);

                    }
                    adapterContractA.Init(models, mTablet);
                    //Init(dialog, models, getId);
                } else {
                    //cDialog.dismiss();
                    adapterContractA.closeLoadMesa();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                adapterContractA.closeLoadMesa();
                adapterContractA.errorLoadMesa(error.getMessage());
            }
        });
    }


}
