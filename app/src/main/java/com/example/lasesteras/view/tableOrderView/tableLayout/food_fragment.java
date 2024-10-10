package com.example.lasesteras.view.tableOrderView.tableLayout;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.lasesteras.R;
import com.example.lasesteras.adapter.adpaterDishes.dishesAdapter;
import com.example.lasesteras.adapter.tableLayoutAdapter.adapterMenu;
import com.example.lasesteras.model.dishesModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class food_fragment extends Fragment {


    List<dishesModel> modelOrders;
    private DatabaseReference mReference;
    RecyclerView recyclerView;
    Context context;

    public food_fragment(List<dishesModel> modelOrders) {
        this.modelOrders = modelOrders;
    }

    public food_fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = view.getContext();
        mReference = FirebaseDatabase.getInstance().getReference();
        recyclerView = view.findViewById(R.id.listFood);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        menuList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_food_fragment, container, false);
    }

    void menuList() {
        ProgressDialog mDialog = new ProgressDialog(getContext());
        mDialog.setMessage("Obteniendo lista de menú...");
        mDialog.setCancelable(false);
        mDialog.show();

        List<dishesModel> ist = new ArrayList<>();

        mReference.child("menu").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    ist.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        String attended = dataSnapshot.child("available").getValue(String.class);

                        assert attended != null;
                        if (attended.equals("TRUE")) {

                            dishesModel model = dataSnapshot.getValue(dishesModel.class);
                            assert model != null;
                            model.setNamePlate(model.getNamePlate());
                            model.setPricePlate(model.getPricePlate());
                            model.setAvailable(model.getAvailable());
                            ist.add(model);
                        }
                    }
                    adapterMenu mAdapterMenu = new adapterMenu(ist, modelOrders, context, getParentFragmentManager());
                    recyclerView.setAdapter(mAdapterMenu);
                    mDialog.dismiss();
                } else {
                    mDialog.dismiss();
                    Toast.makeText(getContext(), "NO HAY LISTA", Toast.LENGTH_LONG).show();
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