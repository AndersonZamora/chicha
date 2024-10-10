package com.example.lasesteras.view.tableOrderView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lasesteras.R;
import com.example.lasesteras.adapter.adapterViewPager.viewPagerAdapter;
import com.example.lasesteras.model.dishesModel;
import com.example.lasesteras.model.tableModel;
import com.example.lasesteras.view.tableOrderView.tableOrderFragment.additionalFragment;
import com.example.lasesteras.view.tableOrderView.tableOrderFragment.beveragesFragment;
import com.example.lasesteras.view.tableOrderView.tableOrderFragment.foodMenuFragment;
import com.example.lasesteras.view.tableOrderView.tableOrderFragment.orderFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class tableOrderActivity extends AppCompatActivity {

    private ViewPager2 viewPager2;
    private static final String SHARED_PREFERENCES = "prefLogin";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_DATE = "dateKey";
    private DatabaseReference mReference;
    private tableModel nameTable;
    viewPagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_order);

        nameTable = (tableModel) getIntent().getSerializableExtra("model");
        assert nameTable != null;
        mReference = FirebaseDatabase.getInstance().getReference();

        SharedPreferences preferences = getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        String date = preferences.getString(KEY_DATE, null);
        TextView dateApp_dashboard = findViewById(R.id.dateApp_dashboard);
        dateApp_dashboard.setText(date);

        TextView fullName_table = findViewById(R.id.fullName_ord);
        String key = preferences.getString(KEY_USERNAME, null);
        fullName_table.setText(key);

        viewPager2 = findViewById(R.id.pager);

        List<dishesModel> modelList;
        if (nameTable.getTableStatus().equals("TRUE")) {
            modelList = getListOrder();

        } else {
            modelList = new ArrayList<>();
        }

        TextView name = findViewById(R.id.nameTable);
        name.setText(nameTable.getTableName());
        viewPagerOrder(modelList);
    }

    private void viewPagerOrder(List<dishesModel> dishesModels) {

        mPagerAdapter = new viewPagerAdapter(getSupportFragmentManager(), getLifecycle());
        mPagerAdapter.addFragment(new foodMenuFragment(tableOrderActivity.this, dishesModels));
        mPagerAdapter.addFragment(new beveragesFragment(tableOrderActivity.this, dishesModels));
        mPagerAdapter.addFragment(new additionalFragment(tableOrderActivity.this, dishesModels));
        mPagerAdapter.addFragment(new orderFragment(dishesModels, this, nameTable));
        viewPager2.setAdapter(mPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tab_layout);

        orderView1(tabLayout);
    }

    private void orderView1(TabLayout tabLayout) {
        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {

            switch (position) {
                case 0:
                    tab.setText(getString(R.string.menu));
                    break;
                case 1:
                    tab.setText(getString(R.string.drinks));
                    break;
                case 2:
                    tab.setText(getString(R.string.additional));
                    break;
                case 3:
                    tab.view.setClickable(false);
                    tab.setText(getString(R.string.order));
                    break;
            }

        }).attach();
    }

    List<dishesModel> getListOrder() {

        ProgressDialog mDialog = new ProgressDialog(tableOrderActivity.this);
        mDialog.setMessage("Obteniendo lista de ordenes...");
        mDialog.setCancelable(false);
        mDialog.show();

        List<dishesModel> models = new ArrayList<>();

        mReference.child("tableList").child(nameTable.getChildId()).child("order").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    models.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        dishesModel model = dataSnapshot.getValue(dishesModel.class);
                        assert model != null;
                        model.setNamePlate(model.getNamePlate());
                        model.setPricePlate(model.getPricePlate());
                        model.setQuantity(model.getQuantity());
                        model.setTotal(model.getTotal());
                        model.setTotalQuantity(model.getTotalQuantity());
                        model.setPlateState(model.getPlateState());
                        models.add(model);
                    }
                    mDialog.dismiss();
                } else {
                    models.clear();
                    mDialog.dismiss();
                    Toast.makeText(tableOrderActivity.this, "Error al cargar pedido", Toast.LENGTH_SHORT).show();
                    try {
                        mDialog.dismiss();
                        viewPagerOrder(models);
                    } catch (Exception e) {
                        mDialog.dismiss();
                        Toast.makeText(tableOrderActivity.this, "OCURRIO UN ERROR", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(tableOrderActivity.this, "OCURRIO UN ERROR", Toast.LENGTH_LONG).show();
                mDialog.dismiss();
            }
        });

        return models;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}