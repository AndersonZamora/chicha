package com.example.lasesteras.view.tableOrderView.tableLayout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lasesteras.R;
import com.example.lasesteras.adapter.tableLayoutAdapter.adapterOrder;
import com.example.lasesteras.model.dishesModel;
import com.example.lasesteras.model.tableModel;
import com.example.lasesteras.view.tableOrderView.orderDetailActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class order_fragment extends Fragment {

    private List<dishesModel> listOrder = new ArrayList<>();
    private RecyclerView recyclerView;
    private Context context;
    private TextView textView;
    private Button view_order;

    private DatabaseReference mReference;

    int state = 0;
    tableModel nameTable;

    public order_fragment(tableModel nameTable) {
        this.nameTable = nameTable;
    }

    public order_fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textView = view.findViewById(R.id.total_order);
        view_order = view.findViewById(R.id.view_order);
        context = view.getContext();
        mReference = FirebaseDatabase.getInstance().getReference();
        recyclerView = view.findViewById(R.id.recycler_view_orderL);

        getParentFragmentManager().setFragmentResultListener("key", this, (requestKey, result) -> {
            if (result != null) {
                //noinspection unchecked
                listOrder = (List<dishesModel>) result.getSerializable("product");
                setInfo(listOrder);
            }
        });
        getListOrder();

        view_order.setOnClickListener(v -> {

            Intent intent = new Intent(context, orderDetailActivity.class);
            if (state == listOrder.size()) {
                intent.putExtra("state", "false");
            } else {
                intent.putExtra("state", "true");
            }
            intent.putExtra("name", nameTable);
            intent.putExtra("list", (Serializable) listOrder);
            startActivity(intent);
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order_fragment, container, false);
    }

    void getListOrder() {

        ProgressDialog mDialog = new ProgressDialog(context);
        mDialog.setMessage("Obteniendo lista de ordenes...");
        mDialog.setCancelable(false);
        mDialog.show();

        mReference.child("tableList").child(nameTable.getChildId()).child("order").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    listOrder.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        dishesModel model = dataSnapshot.getValue(dishesModel.class);
                        assert model != null;
                        model.setNamePlate(model.getNamePlate());
                        model.setPricePlate(model.getPricePlate());
                        model.setQuantity(model.getQuantity());
                        model.setTotal(model.getTotal());
                        model.setTotalQuantity(model.getTotalQuantity());
                        model.setPlateState(model.getPlateState());
                        Log.e("name: ", model.getNamePlate());
                        listOrder.add(model);
                    }
                    state = listOrder.size();
                    setInfo(listOrder);
                    mDialog.dismiss();
                } else {
                    mDialog.dismiss();
                    listOrder.clear();
                    Toast.makeText(context, "Lista de pedido vacia", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "OCURRIO UN ERROR", Toast.LENGTH_LONG).show();
                mDialog.dismiss();
            }
        });

    }

    private void setInfo(List<dishesModel> listOrder) {

        if (listOrder.size() > 0) {
            Log.e("setInfo 1 ", "VISIBLE");
            view_order.setVisibility(View.VISIBLE);
        } else {
            view_order.setVisibility(View.GONE);
        }

        adapterOrder adapterMenu = new adapterOrder(listOrder, context, textView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapterMenu);
    }
}