package com.example.m_expense_final;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class UpdateActivity extends AppCompatActivity {

    private DatePickerDialog picker;
    private DBManager databaseHelper;
    private DatabaseHelper db;
    boolean isFormChecked = false;
    long _id;
    Spinner Tripname;
    Spinner Tranmethod;
    EditText Destination;
    EditText DateofTrip;
    EditText DateEnd;
//    TextView RiskAssess;
//    TextView TransPorts;
    EditText Description;
    Switch RiskSwitch;
    ArrayList<String> tripslist;
    ArrayList<String> TransportMethod;
    ArrayAdapter<String> adapter;


//    String Transports = "";
    String RiskAssese = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        databaseHelper = new DBManager(this);
        databaseHelper.open();



        Tripname = (Spinner) findViewById(R.id.edName);
        Tranmethod = (Spinner) findViewById(R.id.edUpdateTrans);
        Destination = (EditText) findViewById(R.id.Desti);
        DateofTrip = (EditText) findViewById(R.id.DateEdit);
        DateEnd = (EditText) findViewById(R.id.dateEditEnd);
        Description = (EditText) findViewById(R.id.spnTrip);
        RiskSwitch = (Switch) findViewById(R.id.switchRisk);
        RiskSwitch.setTextOff("No");
        RiskSwitch.setTextOn("Yes");
        RiskSwitch.setShowText(false);



        loadData();
        tripslist = databaseHelper.getTrip();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tripslist);
        Tripname.setAdapter(adapter);
        loadTrans();



        Intent intent = getIntent();
        String TripNames =  intent.getStringExtra("Name");
        String Desti = intent.getStringExtra("Destination");
        String Date = intent.getStringExtra("TripDate");
        String DateEnds = intent.getStringExtra("TripDateEnd");
        String Risk = intent.getStringExtra("RiskAsses");
        String Trans = intent.getStringExtra("Transportation");
        String Descrip = intent.getStringExtra("Description");
        String id = intent.getStringExtra("ID");
        _id = Long.parseLong(id);
        Tripname.setSelection(getItemPosition(tripslist, TripNames));
        Destination.setText(Desti);
        DateofTrip.setText(Date);
        DateEnd.setText(DateEnds);
        Tranmethod.setSelection(getItemPosition(TransportMethod, Trans));
//        RiskAssess.setText(Risk);
//        boolean choice = false;
        if (Objects.equals(Risk, "Yes")) RiskSwitch.setChecked(true);
        else if (Objects.equals(Risk, "No")) RiskSwitch.setChecked(false);
        Description.setText(Descrip);
        // risk
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
        EditText dobTxt = (EditText) findViewById(R.id.dateEditEnd);

        picker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                dobTxt.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
            }
        }, year, month, day);
        picker.show();
    }

    private boolean CheckForm()

    {

        if (Destination.length() == 0 && DateofTrip.length() == 0 && DateEnd.length() == 0)
        {
            Destination.setError("Required Form!");
            DateofTrip.setError("Required Form!");
            DateEnd.setError("Required Form!");
            return false;
        }
        return true;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.update, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.updateIcon) {

            String TripName = tripslist.get(Tripname.getSelectedItemPosition());
            String Transportations = TransportMethod.get(Tranmethod.getSelectedItemPosition());
            String destination = Destination.getText().toString();
            String tripdate = DateofTrip.getText().toString();
            String dateend = DateEnd.getText().toString();
            String description = Description.getText().toString();
            isFormChecked = CheckForm();
            if (RiskSwitch.isChecked()) RiskAssese = "Yes";
            else RiskAssese = "No";

            if (isFormChecked)
            {
                databaseHelper.Update(_id, TripName, destination, tripdate, dateend, RiskAssese, Transportations, description);
                return this.Return();
            }


        }
        if (id == R.id.removeIcon)
        {
            databaseHelper.Remove(_id);
            return this.Return();
        }
        if (id == R.id.expense)
        {
            Intent expense = new Intent(this, MainActivityExpense.class);
            expense.putExtra("ID", _id);
            startActivity(expense);
        }
        return super.onOptionsItemSelected(item);
    }


    public boolean Return()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        return false;
    }

    private void loadData()
    {
        tripslist = databaseHelper.getTrip();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tripslist);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Tripname.setAdapter(adapter);
    }
    private void loadTrans()
    {
        TransportMethod = databaseHelper.getTrans();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, TransportMethod);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Tranmethod.setAdapter(adapter);
    }

    int getItemPosition(ArrayList<String> Items, String value)
    {
        for (int i = 0; i < Items.size(); i++ )
        {
            if (Objects.equals(Items.get(i), value)) return i;

        }
        return 0;
    }


//    public boolean CheckDestination()
//    {
//        if (Destination.length() == 0)
//        {
//            Destination.setError("Required Form!");
//        }
//        return true;
//    }
//
//    public boolean CheckDate()
//    {
//        if (DateofTrip.length() == 0)
//        {
//            DateofTrip.setError("Required Form!");
//        }
//        return true;
//    }
//
//    public boolean CheckEndDate()
//    {
//        if (DateEnd.length() == 0)
//        {
//            DateEnd.setError("Required Form!");
//        }
//        return true;
//    }





}