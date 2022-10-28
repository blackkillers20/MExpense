package com.example.m_expense_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddTripName extends AppCompatActivity {
    EditText Tripname;
    EditText Transport;
    EditText Expensetypes;
    private DBManager databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip_name);
        Tripname = (EditText) findViewById(R.id.edTripName);
        Transport = (EditText) findViewById(R.id.edTrans);
        Expensetypes = (EditText) findViewById(R.id.edExpenseTypes);
        databaseHelper = new DBManager(this);
        databaseHelper.open();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.addtrip, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.saveTrip) {

            String TripName = Tripname.getText().toString();
            databaseHelper.InsertsTripName(TripName);
            Toast toast = Toast.makeText(getApplicationContext(), "Info Added!", Toast.LENGTH_SHORT);
            toast.show();
        }
        if (id == R.id.saveTrans)
        {
            String TransPort = Transport.getText().toString();
            databaseHelper.InsertsTransports(TransPort);
            Toast toast = Toast.makeText(getApplicationContext(), "Info Added!", Toast.LENGTH_SHORT);
            toast.show();
        }
        if (id == R.id.saveExpense)
        {
            String Extype = Expensetypes.getText().toString();
            databaseHelper.InsertsExpenseTypes(Extype);
            Toast toast = Toast.makeText(getApplicationContext(), "Info Added!", Toast.LENGTH_SHORT);
            toast.show();
        }
        if (id == R.id.BackMain)
        {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}