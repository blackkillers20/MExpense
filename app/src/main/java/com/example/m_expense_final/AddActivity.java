package com.example.m_expense_final;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddActivity extends AppCompatActivity {

    private DatePickerDialog picker;
    private DatabaseHelper db;
    private DBManager databaseHelper;
    boolean isFormChecked = false;
    Spinner Tripname;
    Spinner Tranmethod;
    EditText Destination;
    EditText DateofTrip;
    EditText DateEnd;
    TextView RiskAssess;
    EditText Description;
    RadioGroup RiskAsses;
    ArrayList<String> tripslist;
    ArrayList<String> TransportMethod;
    ArrayAdapter<String> adapter;
    Switch Risk;

    String RiskAssese = "";
//    String Transports = "";
    String CurrentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Tripname = (Spinner) findViewById(R.id.edName);
        Tranmethod = (Spinner) findViewById(R.id.edTranMethod);
        Destination = (EditText) findViewById(R.id.Desti);
        DateofTrip = (EditText) findViewById(R.id.DateEdit);
        DateEnd = (EditText) findViewById(R.id.AdddateEditEnd);
        RiskAssess = (TextView) findViewById(R.id.txtRisk);
        Description = (EditText) findViewById(R.id.spnTrip);
        RiskAsses = (RadioGroup) findViewById(R.id.riskgroup);
        Risk = (Switch) findViewById(R.id.switchRisk);
        Risk.setTextOff("No");
        Risk.setTextOn("Yes");
        Risk.setShowText(false);
        databaseHelper = new DBManager(this);
        databaseHelper.open();

        tripslist = databaseHelper.getTrip();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tripslist);
        loadData();
        TransportMethod = databaseHelper.getTrans();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, TransportMethod);
        loadTrans();

        DateofTrip.setText(CurrentDate);

    }

    private boolean CheckForm()

    {
        boolean check1 = false;
        boolean check2 = false;
        boolean check3 = false;

        if (Destination.length() == 0 )
        {
            Destination.setError("Required Form!");
            check1 = false;
        }
        else{
            check1 = true;
        }
        if(DateofTrip.length() == 0){
            DateofTrip.setError("Required Form!");
            check2 = false;
        }
        else{
            check2 = true;
        }
        if(DateEnd.length() == 0){
            DateEnd.setError("Required Form!");
            check3 = false;
        }
        else{
            check3= true;
        }
//        if (DateofTrip.length() == 0)
//        {
//            DateofTrip.setError("Required Form!");
//            return false;
//        }
        if(!check1 | !check2 | !check3) return false;
        return true;

    }



    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.savebtn:
                String TripName = tripslist.get(Tripname.getSelectedItemPosition());
                String Transportations = TransportMethod.get(Tranmethod.getSelectedItemPosition());
                String destination = Destination.getText().toString();
                String tripdate = DateofTrip.getText().toString();
                String dateEnd = DateEnd.getText().toString();
//                String Riskass = RiskAssess.getText().toString();
                String description = Description.getText().toString();
                isFormChecked = CheckForm();
//                if (!CheckDestination()|!CheckDate()|CheckEndDate())
//                {
//                    return;
//                }
               if (Risk.isChecked()) RiskAssese = "Yes";
               else RiskAssese = "No";

//                if (RiskYes.isChecked())
//                {
//                    RiskAssese = "Yes";
//                } else if (RiskNo.isChecked())
//                {
//                    RiskAssese = "No";
//                } else
//                {
//                    Toast toast = Toast.makeText(getApplicationContext(), "Check one of the option!", Toast.LENGTH_SHORT);
//                    toast.show();
//                    return;
//                }




                    if(isFormChecked)
                    {
                        databaseHelper.Inserts(TripName, destination, tripdate,dateEnd, RiskAssese, Transportations, description);
                        Intent intent = new Intent(AddActivity.this, MainActivity.class);
                        startActivity(intent);
                    }



        }
    }

    public void onClick2(View v) {
        final Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        EditText dobTxt = (EditText) findViewById(R.id.DateEdit);

        picker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                dobTxt.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
            }
        }, year, month, day);
        picker.show();
    }

    public void onClickEnd(View v) {
        final Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        EditText dobTxt = (EditText) findViewById(R.id.AdddateEditEnd);

        picker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                dobTxt.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
            }
        }, year, month, day);
        picker.show();
    }



    private void loadData()
    {
        tripslist = databaseHelper.getTrip();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tripslist);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Tripname.setAdapter(adapter);
    }

    private void loadTrans()
    {
        TransportMethod = databaseHelper.getTrans();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, TransportMethod);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Tranmethod.setAdapter(adapter);
    }

    public boolean CheckDestination()
    {
        if (Destination.length() == 0)
        {
            Destination.setError("Required Form!");
        }
        return true;
    }

    public boolean CheckDate()
    {
        if (DateofTrip.length() == 0)
        {
            DateofTrip.setError("Required Form!");
        }
        return true;
    }

    public boolean CheckEndDate()
    {
        if (DateEnd.length() == 0)
        {
            DateEnd.setError("Required Form!");
        }
        return true;
    }




}