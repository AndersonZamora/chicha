package com.example.lasesteras.view.tableOrderView.tableOrderFragment;

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
import com.example.lasesteras.adapter.adpaterDishes.dishesDrinksAdapter;
import com.example.lasesteras.model.dishesModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class beveragesFragment extends Fragment {

    List<dishesModel> modelsNew;
    private final Context mContext;
    List<dishesModel> listDrinks;
    private DatabaseReference mReference;
    RecyclerView mRecyclerView;

    public beveragesFragment(Context mContext, List<dishesModel> modelsNew) {
        this.mContext = mContext;
        this.modelsNew = modelsNew;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_beverages, container, false);
        mReference = FirebaseDatabase.getInstance().getReference();
        mRecyclerView = root.findViewById(R.id.recycler_view_drinks);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(layoutManager);
        listDrinks();

        return root;
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
                    dishesDrinksAdapter mAdapter = new dishesDrinksAdapter(mContext, modelListAdditional, modelsNew, getParentFragmentManager());
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