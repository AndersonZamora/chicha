package com.example.lasesteras.view.checkRoleView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.example.lasesteras.R;
import com.example.lasesteras.model.employeesModel;
import com.example.lasesteras.view.mainView.mainActivity;
import com.example.lasesteras.viewAdministrator.mainAdministratorView.mainAdministratorActivity;
import com.example.lasesteras.viewKitchen.mainCookView.mainCookActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class CheckRoleActivity extends AppCompatActivity {

    private static final String ROLE_WAITRESS = "waitress";
    private static final String ROLE_COOK = "cook";
    private static final String ROLE_ADMINISTRATOR = "administrator";
    private static final String SHARED_PREFERENCES = "prefLogin";
    private static final String SHARED_INTERNET = "CONNECTIVITY";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_DATE = "dateKey";
    private static final String KEY_INTERNET = "KEY_INTERNET";
    private static final String KEY_NAME = "fullName";
    private static final String KEY_PHONE = "cellular";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_ROLE = "role";

    ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_role);
        initDataPicker();
        mDialog = new ProgressDialog(this);

        mDialog.setMessage("Comprobando conexión..");
        mDialog.setCancelable(false);
        mDialog.show();
        connected();

        SharedPreferences mPreferences = getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mPreferences.edit();
        mEditor.putString(KEY_DATE, getTodayDate());
        Log.e("date", getTodayDate());
        mEditor.apply();
    }

    private void connected() {

        SharedPreferences mPreferences = getSharedPreferences(SHARED_INTERNET, Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mPreferences.edit();

        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);

                if (connected) {

                    mDialog.dismiss();
                    mEditor.putString(KEY_INTERNET, "TRUE");
                    mEditor.apply();
                    checkConnected();

                } else {

                    mDialog.setMessage("Intentando conectar a internet...");
                    mEditor.putString(KEY_INTERNET, "FALSE");
                    mEditor.apply();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void checkConnected() {

        employeesModel model = (employeesModel) getIntent().getSerializableExtra("model");

        if (model == null) {

            SharedPreferences preferences = getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
            String email = preferences.getString(KEY_EMAIL, null);
            String name = preferences.getString(KEY_NAME, null);
            String phone = preferences.getString(KEY_PHONE, null);
            String pass = preferences.getString(KEY_PASSWORD, null);
            String user = preferences.getString(KEY_USERNAME, null);
            String rol = preferences.getString(KEY_ROLE, null);

            employeesModel modelShared = new employeesModel(name, phone, email, pass, user, rol);
            checkRole(modelShared);

        } else {
            checkRole(model);
        }
    }

    private void checkRole(employeesModel role) {

        switch (role.getRole()) {
            case ROLE_WAITRESS: {
                Intent intent = new Intent(CheckRoleActivity.this, mainActivity.class);
                intent.putExtra("model", role);
                interActivity(intent);
                break;
            }
            case ROLE_COOK: {

                Intent intent = new Intent(CheckRoleActivity.this, mainCookActivity.class);
                intent.putExtra("model", role);
                interActivity(intent);
                break;
            }
            case ROLE_ADMINISTRATOR: {

                Intent intent = new Intent(CheckRoleActivity.this, mainAdministratorActivity.class);
                intent.putExtra("model", role);
                interActivity(intent);
                break;
            }
        }
    }

    private void interActivity(Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
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

        };
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int moth = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, moth, day);
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