package com.example.lasesteras.viewAdministrator.salesView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lasesteras.R;
import com.example.lasesteras.adapter.adapterSalesA.salesAdapterA;
import com.example.lasesteras.model.employeesModel;
import com.example.lasesteras.model.reports;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class salesActivity extends AppCompatActivity {

    private DatePickerDialog datePickerDialog;
    private DatePickerDialog datePickerDialog2;
    TextView reports_start_date, reports_ending_date, reports_total;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales);

        initDataPicker();
        initDataPicker2();

        getUser();
        TextView date = findViewById(R.id.dateApp_dashboard);

        date.setText(getTodayDate());

        reports_start_date = findViewById(R.id.reports_start_date);
        reports_ending_date = findViewById(R.id.reports_ending_date);
        reports_total = findViewById(R.id.reports_total);


        RecyclerView mRecyclerView = findViewById(R.id.reports_recyclerView);

        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        DatabaseReference mReference = FirebaseDatabase.getInstance().getReference();

        getInfo(mRecyclerView, mReference, getTodayDate(), getTodayDate());

        reports_start_date.setText(getTodayDate());
        reports_ending_date.setText(getTodayDate());

        reports_start_date.setOnClickListener(v -> datePickerDialog.show());
        reports_ending_date.setOnClickListener(v -> datePickerDialog2.show());

        Button reports_consult = findViewById(R.id.reports_consult);
        reports_consult.setOnClickListener(v -> getInfo(mRecyclerView, mReference, reports_start_date.getText().toString().trim(), reports_ending_date.getText().toString().trim()));
    }

    private void getUser() {

        textView = findViewById(R.id.fullName_order);
        employeesModel modelG = (employeesModel) getIntent().getSerializableExtra("model");
        assert modelG != null;
        textView.setText(modelG.getUsername());

    }

    private void getInfo(RecyclerView mRecyclerView, DatabaseReference mReference, String start_date, String ending_date) {

        mReference.child("sales").orderByKey().startAt(start_date).endAt(ending_date).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {

                    ArrayList<reports> reports = new ArrayList<>();

                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {

                        reports reports1 = new reports();

                        reports1.setDate(snapshot1.getKey());

                        List<String> totalList = new ArrayList<>();

                        for (DataSnapshot to : snapshot1.getChildren()) {

                            String total = to.child("total").getValue(String.class);
                            totalList.add(total);
                            reports1.setTotal(totalList);
                        }
                        reports.add(reports1);
                    }
                    salesAdapterA mAdapterA = new salesAdapterA(reports, salesActivity.this, reports_total);
                    mRecyclerView.setAdapter(mAdapterA);

                } else {
                    Toast.makeText(salesActivity.this, "NO SE REGISTRAN VENTAS", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(salesActivity.this, "CONTACTAR CON EL ADMINISTRADOR", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getTodayDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int moth = calendar.get(Calendar.MONTH);
        moth = moth + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        //String dateS = getDayFormat(day) + "-" + getMonthFormat(moth) + "-" + year;
        return makeDateString(day, moth, year);
    }

    private void initDataPicker() {

        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, day) -> {
            month = month + 1;
            String date = makeDateString(day, month, year);
            String dateS = getDayFormat(day) + "-" + getMonthFormat(month) + "-" + year;
            Log.e("Fecha", dateS);
            reports_start_date.setText(date);
        };
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int moth = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, moth, day);
    }

    private void initDataPicker2() {

        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, day) -> {
            month = month + 1;
            String date = makeDateString(day, month, year);
            String dateS = getDayFormat(day) + "-" + getMonthFormat(month) + "-" + year;
            Log.e("Fecha", dateS);
            reports_ending_date.setText(date);
        };
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int moth = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog2 = new DatePickerDialog(this, style, dateSetListener, year, moth, day);
    }

    private String makeDateString(int day, int month, int year) {
        return getDayFormat(day) + "-" + getMonthFormat(month) + "-" + year;
    }

    private String getDayFormat(int day) {
        if (day == 1) {
            return "01";
        }
        if (day == 2) {
            return "02";
        }
        if (day == 3) {
            return "03";
        }
        if (day == 4) {
            return "04";
        }
        if (day == 5) {
            return "05";
        }
        if (day == 6) {
            return "06";
        }
        if (day == 7) {
            return "07";
        }
        if (day == 8) {
            return "08";
        }
        if (day == 9) {
            return "09";
        }

        return String.valueOf(day);
    }

    private String getMonthFormat(int month) {
        if (month == 1) {
            return "01";
        }
        if (month == 2) {
            return "02";
        }
        if (month == 3) {
            return "03";
        }
        if (month == 4) {
            return "04";
        }
        if (month == 5) {
            return "05";
        }
        if (month == 6) {
            return "06";
        }
        if (month == 7) {
            return "07";
        }
        if (month == 8) {
            return "08";
        }
        if (month == 9) {
            return "09";
        }
        if (month == 10) {
            return "10";
        }
        if (month == 11) {
            return "11";
        }
        if (month == 12) {
            return "12";
        }
        return "01";
    }

}