package com.example.m_expense_final;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import androidx.cursoradapter.widget.SimpleCursorAdapter;

import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.m_expense_final.JSON.HttpUtils;
import com.example.m_expense_final.JSON.Payload;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Handler handler;

    private DBManager databaseHelper;
    private ListView listView;
    private SimpleCursorAdapter adapter;
    Cursor search;
    SearchView sv;
    private SimpleCursorAdapter adapter2;


        final String[] start = new String[]
                { DatabaseHelper.ID, DatabaseHelper.TRIP_COL, DatabaseHelper.DES_COL ,DatabaseHelper.DATE_COL, DatabaseHelper.DATE_END_COL,
                        DatabaseHelper.RISK_COL, DatabaseHelper.TRANSPORTS, DatabaseHelper.INFO_COL };
        final int[] end = new int[]
                { R.id.id, R.id.txtDateEx, R.id.txtdesti, R.id.txtDate, R.id.txtDateReadEnd, R.id.txtRisk, R.id.txTransp, R.id.txtTripName};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handler = new Handler();

        setTitle("M EXPENSE");
        databaseHelper = new DBManager(this);
        databaseHelper.open();

        Cursor cr = databaseHelper.fetch();

        listView = (ListView) findViewById(R.id.list_view);
        listView.setEmptyView(findViewById(R.id.empty));

        adapter = new SimpleCursorAdapter(this, R.layout.activity_read, cr, start, end, 0);
        adapter.notifyDataSetChanged();

        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, viewId) -> {
            TextView IDs = (TextView) view.findViewById(R.id.id);
            TextView txTrip = (TextView) view.findViewById(R.id.txtDateEx);
            TextView txDes = (TextView) view.findViewById(R.id.txtdesti);
            TextView txDate = (TextView) view.findViewById(R.id.txtDate);
            TextView txDateEnds = (TextView) view.findViewById(R.id.txtDateReadEnd);
            TextView txRisk = (TextView) view.findViewById(R.id.txtRisk);
            TextView txDescrip = (TextView) view.findViewById(R.id.txtTripName);
            TextView txTransports = (TextView) view.findViewById(R.id.txTransp);

            String ID = IDs.getText().toString();
            String Trip = txTrip.getText().toString();
            String Desti = txDes.getText().toString();
            String Date = txDate.getText().toString();
            String DateEnd = txDateEnds.getText().toString();
            String Risk = txRisk.getText().toString();
            String Transports = txTransports.getText().toString();
            String Descrip = txDescrip.getText().toString();

            Intent modify_intent = new Intent(getApplicationContext(), UpdateActivity.class);
            modify_intent.putExtra("Name", Trip);
            modify_intent.putExtra("Destination", Desti);
            modify_intent.putExtra("TripDate", Date);
            modify_intent.putExtra("TripDateEnd", DateEnd);
            modify_intent.putExtra("RiskAsses", Risk);
            modify_intent.putExtra("Transportation", Transports);
            modify_intent.putExtra("Description", Descrip);
            modify_intent.putExtra("ID",ID);

            startActivity(modify_intent);
//            listView.setSelector(R.color.teal_200);
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        sv = (SearchView) menu.findItem(R.id.search).getActionView();
        sv.setSubmitButtonEnabled(true);
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                SearchResult(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });
        sv.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                return true;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                Cursor cr = sv.getSuggestionsAdapter().getCursor();
                cr.moveToPosition(position);
                String suggest = cr.getString(1);

                sv.setQuery(suggest, true);
                return true;
            }
        });
        sv.setOnCloseListener(() -> {
            Intent returns = getIntent();
            finish();
            startActivity(returns);
            return false;
        });

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.add_record) {

            Intent add_mem = new Intent(this, AddActivity.class);
            startActivity(add_mem);

        }
        if (id == R.id.addTripNames)
        {
            Intent addtrip = new Intent(this, AddTripName.class);
            startActivity(addtrip);
        }
        if (id == R.id.BackAll)
        {
            sendJSON();
        }
        return super.onOptionsItemSelected(item);
    }

    private void SearchResult(String newText)
    {
        search = databaseHelper.SearchTrip(newText);
        if (adapter2 == null)
        {
            adapter2 = new SimpleCursorAdapter(this, R.layout.activity_read, search, start, end,0);
            listView.setAdapter(adapter2);
        }
    }

    public void sendJSON()
    {
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        Payload payload = new Payload();
        payload.setUserId("123");
        payload.setDetailList(databaseHelper.getDetailsList());
        new Thread(()->{
            String response;
            JSONObject json = null;

            try {
                String stringPayload = payload.toJson();
                response = new HttpUtils()
                        .postJson(new URL("http://cwservice1786.herokuapp.com/sendPayLoad"),
                                stringPayload, "jsonpayload");
                json = new JSONObject(response);
                JSONObject finalJson = json;
                handler.post(() -> {
                    progressBar.setVisibility(View.GONE);
                    try {
                        new AlertDialog.Builder(this).setMessage(finalJson.getString("message") + "\n"
                                + "userId: " + finalJson.getString("userid") + "\n"
                                + "number: " + finalJson.getString("number")
                                + "\n" + "name: " + finalJson.getString("names"))
                                .setTitle(finalJson.getString("uploadResponseCode"))
                                .setNeutralButton("OK", (d, w) -> d.dismiss()).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });
            } catch (Exception e) {
                // error
            }
        }).start();



    }


}

