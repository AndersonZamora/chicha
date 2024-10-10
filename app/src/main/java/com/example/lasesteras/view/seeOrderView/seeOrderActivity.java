package com.example.lasesteras.view.seeOrderView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lasesteras.R;
import com.example.lasesteras.adapter.adapterSummary.orderSummaryAdapter;
import com.example.lasesteras.model.dishesModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class seeOrderActivity extends AppCompatActivity {

    private static final String SHARED_PREFERENCES = "prefLogin";
    private static final String KEY_NAME = "fullName";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_order);

        DatabaseReference mReference = FirebaseDatabase.getInstance().getReference("tableList");
        TextView nameSee = findViewById(R.id.nameSee);
        TextView quantity = findViewById(R.id.total);
        TextView SubTotal = findViewById(R.id.SubTotal);
        TextView fullName_summary = findViewById(R.id.fullName_summary);

        SharedPreferences preferences = getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        String key = preferences.getString(KEY_NAME, null);
        fullName_summary.setText(key);

        findViewById(R.id.add_plat).setOnClickListener(v -> {
            onBackPressed();
        });

        List<dishesModel> dishesMenu = (List<dishesModel>) getIntent().getSerializableExtra("listMenu");
        assert dishesMenu != null;

        String nameTable = getIntent().getStringExtra("nameTable");
        String tableId = getIntent().getStringExtra("tableId");
        assert nameSee != null;
        assert tableId != null;
        nameSee.setText(nameTable);
        Log.e("tableId", tableId);

        RecyclerView mRecyclerView = findViewById(R.id.recycler_view_detail);
        orderSummaryAdapter mAdapter = new orderSummaryAdapter(dishesMenu, this, quantity, SubTotal);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);

        findViewById(R.id.MakeOrder).setOnClickListener(v -> {

            mReference.child(tableId).child("order").setValue(dishesMenu).addOnCompleteListener(task -> {
                Toast.makeText(seeOrderActivity.this, "Pedido guardado", Toast.LENGTH_LONG).show();
            }).addOnFailureListener(e -> {
                Toast.makeText(seeOrderActivity.this, "Pedido no guardado", Toast.LENGTH_LONG).show();
            });
        });


    }
}
     /*   try {
            for (int i = 0; i < dishesMenu.size(); i++) {
                Log.e("aritculo", dishesMenu.get(i).getNamePlate());
                Log.e("cantidad", dishesMenu.get(i).getQuantity());
            }recycler_view_detail
        } catch (Exception e) {
            Log.e("error", e.getMessage());
        }*/