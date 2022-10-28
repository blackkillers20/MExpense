package com.example.m_expense_final;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddActivityExpense extends AppCompatActivity {
    private DatePickerDialog picker;
    private DBManager databaseHelper;
    boolean isFormChecked = false;
//    String ExpenseTypes = "";
    Spinner spTrip;
    Spinner spExpenseTypes;
    EditText edAmount;
    EditText edTime;
    EditText edComt;
    List<String> TripName;
    ArrayList<String> ExTypes;

    String CurrentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
    long _id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        spExpenseTypes = (Spinner) findViewById(R.id.spnExpense);
        edAmount = (EditText) findViewById(R.id.edtAmt);
        edTime = (EditText) findViewById(R.id.edName);
        edComt = (EditText) findViewById(R.id.edComt);
        spTrip = (Spinner) findViewById(R.id.spnTrip);

        databaseHelper = new DBManager(this);
        databaseHelper.open();

        loadData();
        loadExTypes();


        edTime.setText(CurrentDate);
    }

    public void onClick (View v)
    {
        switch (v.getId())
        {
            case R.id.savebtn:
            String spinExTypes = ExTypes.get(spExpenseTypes.getSelectedItemPosition());
            String edAmt = edAmount.getText().toString();
            String edtime = edTime.getText().toString();
            String edCmt = edComt.getText().toString();
            String Trip = TripName.get(spTrip.getSelectedItemPosition());
            _id = Long.parseLong((String) spTrip.getSelectedItem());

            isFormChecked = CheckForm();
            if (isFormChecked)
            {
                databaseHelper.InsertsExpense(spinExTypes, edAmt, edtime, edCmt, Trip);
//                onBackPressed();
                Intent intent = new Intent(AddActivityExpense.this, MainActivity.class);
                intent.putExtra("ID", _id);
                startActivity(intent);
            }


        }
    }



    private void loadData()
    {
        TripName = databaseHelper.getTripID();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, TripName);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTrip.setAdapter(adapter);
    }

    private void loadExTypes()
    {
        ExTypes = databaseHelper.getExtypes();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ExTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spExpenseTypes.setAdapter(adapter);
    }

    private boolean CheckForm() {
        if (edAmount.length() == 0) {
            edAmount.setError("Required Form!");
            return false;
        }
        if (edTime.length() == 0) {
            edTime.setError("Required Form!");
            return false;
        }
        if (edComt.length() == 0) {
            edComt.setError("Required Form!");
            return false;
        }
        return true;

    }
    public void onClick2(View v) {
        final Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        EditText dobTxt = (EditText) findViewById(R.id.edName);

        picker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                dobTxt.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
            }
        }, year, month, day);
        picker.show();
    }
}