package com.example.lasesteras.view.tableOrderView.tableOrderFragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lasesteras.R;
import com.example.lasesteras.adapter.adpaterDishes.dishesAdditionalAdapter;
import com.example.lasesteras.model.dishesModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class additionalFragment extends Fragment {

    Context mContext;
    List<dishesModel> listAdditional;
    private DatabaseReference mReference;
    RecyclerView mRecyclerView;

    public additionalFragment(Context mContext, List<dishesModel> listAdditional) {

        this.mContext = mContext;
        this.listAdditional = listAdditional;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_additional, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        mReference = FirebaseDatabase.getInstance().getReference();
        mRecyclerView = view.findViewById(R.id.recycler_view_add);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(layoutManager);
        additional();
    }

    void additional() {

        ProgressDialog mDialog = new ProgressDialog(getContext());
        mDialog.setMessage("Obteniendo lista de adicionales...");
        mDialog.setCancelable(false);
        mDialog.show();

        List<dishesModel> modelListAdditional = new ArrayList<>();

        mReference.child("extra").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    modelListAdditional.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        String attended = dataSnapshot.child("available").getValue(String.class);

                        if (attended.equals("TRUE")) {
                            dishesModel model = dataSnapshot.getValue(dishesModel.class);
                            assert model != null;
                            model.setNamePlate(model.getNamePlate());
                            model.setPricePlate(model.getPricePlate());
                            model.setAvailable(model.getAvailable());
                            modelListAdditional.add(model);
                        }

                    }
                    dishesAdditionalAdapter mAdapter = new dishesAdditionalAdapter(mContext, modelListAdditional, listAdditional, getParentFragmentManager());
                    mRecyclerView.setAdapter(mAdapter);
                    mDialog.dismiss();
                } else {
                    mDialog.dismiss();
                    Toast.makeText(getActivity(), "NO HAY LISTA", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                mDialog.dismiss();
                Toast.makeText(getActivity(), "NO HAY LISTA2", Toast.LENGTH_LONG).show();
            }
        });

    }
}