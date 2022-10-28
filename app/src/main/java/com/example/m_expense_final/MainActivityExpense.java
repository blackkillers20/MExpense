package com.example.m_expense_final;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cursoradapter.widget.SimpleCursorAdapter;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivityExpense extends AppCompatActivity {

    private DBManager databaseHelper;
    private ListView listView;
    private SimpleCursorAdapter adapter;
    long _id;

    final String[] from = new String[]
            {DatabaseHelper.ID ,DatabaseHelper.EXTYPE_COL, DatabaseHelper.EXAMOUT_COL, DatabaseHelper.EXTIME_COL, DatabaseHelper.CMT_COL
            , DatabaseHelper.TRIPNAME_COL};
    final int[] to = new int[]
            {R.id.idID, R.id.idType, R.id.idAmount, R.id.idTime, R.id.idCmt, R.id.idTrip};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_expense);
        _id = getIntent().getLongExtra("ID", 0);

        databaseHelper = new DBManager(this);
        databaseHelper.open();

        Cursor cr = databaseHelper.fetchExpenxe(_id);

        listView = (ListView) findViewById(R.id.list_view);
        listView.setEmptyView(findViewById(R.id.empty));

        adapter = new SimpleCursorAdapter(this, R.layout.activity_read_expense, cr, from, to, 0);
        adapter.notifyDataSetChanged();

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView exID = (TextView) view.findViewById(R.id.idID);
                TextView exType = (TextView) view.findViewById(R.id.idType);
                TextView exAmt = (TextView) view.findViewById(R.id.idAmount);
                TextView exTime = (TextView) view.findViewById(R.id.idTime);
                TextView exCMT = (TextView) view.findViewById(R.id.idCmt);
                TextView exTrip = (TextView) view.findViewById(R.id.idTrip);


                String exid = exID.getText().toString();
                String extype = exType.getText().toString();
                String examt = exAmt.getText().toString();
                String extime = exTime.getText().toString();
                String excmt = exCMT.getText().toString();
                String extrip = exTrip.getText().toString();


                Intent UpdateExpense = new Intent(getApplicationContext(), UpdateActivityExpense.class);
                UpdateExpense.putExtra("ID", exid);
                UpdateExpense.putExtra("ExpenseTypes", extype);
                UpdateExpense.putExtra("ExpenseAmount", examt);
                UpdateExpense.putExtra("ExpenseTime", extime);
                UpdateExpense.putExtra("AddtionalComment", excmt);
                UpdateExpense.putExtra("TripName", extrip);


                startActivity(UpdateExpense);


            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.expense, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.add_record) {

            Intent add_mem = new Intent(this, AddActivityExpense.class);
            startActivity(add_mem);

        }
        if (id == R.id.Back)
        {
            Intent Return = new Intent(this, MainActivity.class);
            startActivity(Return);
        }
        return super.onOptionsItemSelected(item);
    }
}