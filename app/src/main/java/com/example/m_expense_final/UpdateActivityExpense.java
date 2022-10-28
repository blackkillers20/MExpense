package com.example.m_expense_final;

import androidx.annotation.NonNull;
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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class UpdateActivityExpense extends AppCompatActivity {
    long _id;
    private DatePickerDialog picker;
    private DBManager databaseHelper;
    boolean isFormChecked = false;
    String ExpenseTypes = "";
    Spinner spTrip;
    Spinner spExpenseTypes;
    EditText edAmount;
    EditText edTime;
    EditText edComt;
    ArrayList<String> TripName;
    ArrayList<String> ExTypes;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_expense);

        edAmount = (EditText) findViewById(R.id.edtAmt);
        edTime = (EditText) findViewById(R.id.edName);
        edComt = (EditText) findViewById(R.id.edComt);
        spTrip = (Spinner) findViewById(R.id.spnTrip);
        spExpenseTypes = (Spinner) findViewById(R.id.spnExpense);
        databaseHelper = new DBManager(this);
        databaseHelper.open();

        Intent updateIntent = getIntent();
        String ExTypesUp = updateIntent.getStringExtra("ExpenseTypes");
        String ExAmount = updateIntent.getStringExtra("ExpenseAmount");
        String ExTime = updateIntent.getStringExtra("ExpenseTime");
        String ExCom = updateIntent.getStringExtra("AddtionalComment");
        String ExTrip = updateIntent.getStringExtra("TripName");
        String id = updateIntent.getStringExtra("ID");
        _id = Long.parseLong(id);
        loadData();
        loadExTypes();
        spExpenseTypes.setSelection(getItemPosition(ExTypes, ExTypesUp));
        spTrip.setSelection(getItemPosition(TripName, ExTrip));
        edAmount.setText(ExAmount);
        edTime.setText(ExTime);
        edComt.setText(ExCom);





    }


    private void loadExTypes()
    {
        ExTypes = databaseHelper.getExtypes();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ExTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spExpenseTypes.setAdapter(adapter);
    }

    private void loadData() {
        TripName = databaseHelper.getTripID();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, TripName);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTrip.setAdapter(adapter);
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

            String edAmt = edAmount.getText().toString();
            String edtime = edTime.getText().toString();
            String edCmt = edComt.getText().toString();
            String Trip = TripName.get(spTrip.getSelectedItemPosition());
            String Ex = ExTypes.get(spExpenseTypes.getSelectedItemPosition());
            isFormChecked = CheckForm();
            if (isFormChecked)
            {
                databaseHelper.UpdateExpense(_id, Ex, edAmt, edtime, edCmt, Trip);
                Intent intent = new Intent(UpdateActivityExpense.this, MainActivity.class);
                startActivity(intent);
            }

        }
        if (id == R.id.removeIcon)
        {
            databaseHelper.RemoveExpense(_id);
            return this.Return();
        }
        return super.onOptionsItemSelected(item);
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

    public boolean Return()
    {
        Intent intent = new Intent(this, MainActivityExpense.class);
        startActivity(intent);
        return false;
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

    int getItemPosition(ArrayList<String> Items, String value)
    {
        for (int i = 0; i < Items.size(); i++ )
        {
            if (Objects.equals(Items.get(i), value)) return i;

        }
        return 0;
    }

}