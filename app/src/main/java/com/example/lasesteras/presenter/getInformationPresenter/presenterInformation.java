package com.example.lasesteras.presenter.getInformationPresenter;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.lasesteras.model.tableModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class presenterInformation implements informationF {

    @Override
    public void getListTable(DatabaseReference reference, Callback callback) {

        reference.child("tableList").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    ArrayList<tableModel> tableList = new ArrayList<>();

                    tableList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        tableModel model = dataSnapshot.getValue(tableModel.class);
                        assert model != null;
                        Log.e("name ", model.getTableName());

                        model.setTableName(model.getTableName());
                        model.setTableStatus(model.getTableStatus());
                        model.setChildId(dataSnapshot.getKey());
                        model.setModels(model.getModels());
                        model.setAttended(model.getAttended());
                        tableList.add(model);
                    }
                    callback.onSuccess(tableList);
                } else {
                    callback.onFailure("Error");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onFailure(error.getMessage());
            }
        });
    }
}
