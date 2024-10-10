package com.example.lasesteras.view.tableOrderView.tableLayout;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.lasesteras.R;
import com.example.lasesteras.adapter.tableLayoutAdapter.adapterBeverages;
import com.example.lasesteras.model.dishesModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class beverages_fragment extends Fragment {


    List<dishesModel> modelOrders;
    private DatabaseReference mReference;
    RecyclerView recyclerView;
    Context context;

    public beverages_fragment() {
        // Required empty public constructor
    }

    public beverages_fragment(List<dishesModel> modelOrders) {
        this.modelOrders = modelOrders;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_beverages_fragment, container, false);


        context = view.getContext();
        mReference = FirebaseDatabase.getInstance().getReference();
        recyclerView = view.findViewById(R.id.list);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        listDrinks();
        return view;
    }

    void listDrinks() {

        ProgressDialog mDialog = new ProgressDialog(getContext());
        mDialog.setMessage("Obteniendo lista de Bebidas...");
        mDialog.setCancelable(false);
        mDialog.show();

        List<dishesModel> modelListAdditional = new ArrayList<>();

        mReference.child("drinks").addValueEventListener(new ValueEventListener() {
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
                    adapterBeverages mAdapter = new adapterBeverages(modelListAdditional, modelOrders, context, getParentFragmentManager());
                    recyclerView.setAdapter(mAdapter);
                    mDialog.dismiss();
                } else {
                    mDialog.dismiss();
                    Toast.makeText(getActivity(), "NO HAY LISTA", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                mDialog.dismiss();
                Toast.makeText(getActivity(), "NO HAY LISTA", Toast.LENGTH_LONG).show();
            }
        });
    }
}