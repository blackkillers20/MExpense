package com.example.m_expense_final;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.m_expense_final.JSON.Payload;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DBManager {

    private DatabaseHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public DBManager(Context c) {
        context = c;
    }

    public DBManager open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public long Inserts(String trip, String des, String date, String dateEnd, String risk, String trans, String description) {
        ContentValues rowValues = new ContentValues();

        rowValues.put(DatabaseHelper.TRIP_COL, trip);
        rowValues.put(DatabaseHelper.DES_COL, des);
        rowValues.put(DatabaseHelper.DATE_COL, date);
        rowValues.put(DatabaseHelper.DATE_END_COL, dateEnd);
        rowValues.put(DatabaseHelper.RISK_COL, risk);
        rowValues.put(DatabaseHelper.TRANSPORTS, trans);
        rowValues.put(DatabaseHelper.INFO_COL, description);

        return database.insertOrThrow(DatabaseHelper.TABLE_NAME, null, rowValues);
    }

    public long InsertsExpense(String extype, String examount, String exdate, String comt, String tripname) {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.EXTYPE_COL, extype);
        cv.put(DatabaseHelper.EXAMOUT_COL, examount);
        cv.put(DatabaseHelper.EXTIME_COL, exdate);
        cv.put(DatabaseHelper.CMT_COL, comt);
        cv.put(DatabaseHelper.TRIPNAME_COL, tripname);

        return database.insertOrThrow(DatabaseHelper.TABLE_NAME2, null, cv);
    }

    public Cursor fetchExpenxe(long _id) {
//        String[] col2 = new String[]
//                {DatabaseHelper.ID, DatabaseHelper.EXTYPE_COL, DatabaseHelper.EXAMOUT_COL
//                        , DatabaseHelper.EXTIME_COL, DatabaseHelper.CMT_COL, DatabaseHelper.TRIPNAME_COL};
//        String query = " SELECT * FROM " + DatabaseHelper.TABLE_NAME2 + " INNER JOIN " + DatabaseHelper.TABLE_NAME
//                + " ON " + DatabaseHelper.TABLE_NAME + " . " + DatabaseHelper.ID + " = " + DatabaseHelper.TABLE_NAME2
//                + " . " + DatabaseHelper.TRIPNAME_COL + " WHERE " + DatabaseHelper.TABLE_NAME + " . " + DatabaseHelper.ID +
//                " = " + _id;

        String query2 = " SELECT * FROM ExpenseDetails WHERE TripName = " + String.valueOf(_id);
//        Cursor crs = database.query(DatabaseHelper.TABLE_NAME2  + " INNER JOIN "
//                + DatabaseHelper.TABLE_NAME + " ON " + DatabaseHelper.TABLE_NAME2 + " . " + DatabaseHelper.ID
//                + " = " + DatabaseHelper.TABLE_NAME + " . " + DatabaseHelper.ID, col2, null, null, null, null, null);
        Cursor crs;

        crs = database.query(DatabaseHelper.TABLE_NAME2, null, DatabaseHelper.TRIPNAME_COL + "=" + _id, null, null, null, null);

        return crs;
    }



    public Cursor fetch() {
//         String[] clmn = new String[]{DatabaseHelper._ID, DatabaseHelper.TRIP_COL, DatabaseHelper.DES_COL, DatabaseHelper.DATE_COL
//                , DatabaseHelper.RISK_COL, DatabaseHelper.INFO_COL};
        String[] col = new String[]
                {DatabaseHelper.ID, DatabaseHelper.TRIP_COL, DatabaseHelper.DES_COL, DatabaseHelper.DATE_COL
                        , DatabaseHelper.DATE_END_COL, DatabaseHelper.RISK_COL, DatabaseHelper.TRANSPORTS, DatabaseHelper.INFO_COL};

        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, col, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

//    public int getTripNameId(String NameTrip)
//    {
//        String query = " SELECT * FROM TripName WHERE Name " + " = \"" + NameTrip + "\"";
//        Cursor crs = database.rawQuery(query, null);
//
//        if (crs != null) crs.moveToFirst();
//
//        assert crs != null;
//        return crs.getInt(0);
//    }

    public int Update(long _id, String trip, String des, String date, String dateEnd, String risk, String trans, String description) {
        ContentValues rowValues = new ContentValues();
        rowValues.put(DatabaseHelper.TRIP_COL, trip);
        rowValues.put(DatabaseHelper.DES_COL, des);
        rowValues.put(DatabaseHelper.DATE_COL, date);
        rowValues.put(DatabaseHelper.DATE_END_COL, dateEnd);
        rowValues.put(DatabaseHelper.RISK_COL, risk);
        rowValues.put(DatabaseHelper.TRANSPORTS, trans);
        rowValues.put(DatabaseHelper.INFO_COL, description);

        int u = database.update(DatabaseHelper.TABLE_NAME, rowValues, DatabaseHelper.ID + " = " + _id, null);
        return u;
    }

    public int UpdateExpense(long _id, String exType, String exAmount, String exDate, String Comt, String TripName) {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.EXTYPE_COL, exType);
        cv.put(DatabaseHelper.EXAMOUT_COL, exAmount);
        cv.put(DatabaseHelper.EXTIME_COL, exDate);
        cv.put(DatabaseHelper.CMT_COL, Comt);
        cv.put(DatabaseHelper.TRIPNAME_COL, TripName);

        int u = database.update(DatabaseHelper.TABLE_NAME2, cv, DatabaseHelper.ID + " = " + _id, null);
        return u;
    }

    public void Remove(long id) {
        database.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper.ID + "=" + id, null);
    }

    public void RemoveExpense(long id) {
        database.delete(DatabaseHelper.TABLE_NAME2, DatabaseHelper.ID + "=" + id, null);
    }

    public ArrayList<String> getTripID() {
        ArrayList<String> TripName = new ArrayList<String>();
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getReadableDatabase();

        Cursor cr = database.query(DatabaseHelper.TABLE_NAME, null, null, null, null, null, DatabaseHelper.ID);
        if (cr.moveToFirst()) {
            do {
                TripName.add(cr.getString(0));
            } while (cr.moveToNext());
        }

        return TripName;

    }

    public ArrayList<Payload.Data> getDetailsList()
    {
        String[] col = new String[]
                {DatabaseHelper.ID, DatabaseHelper.TRIP_COL, DatabaseHelper.DES_COL, DatabaseHelper.DATE_COL
                        , DatabaseHelper.DATE_END_COL, DatabaseHelper.RISK_COL, DatabaseHelper.TRANSPORTS, DatabaseHelper.INFO_COL};

        Cursor crs = database.query(DatabaseHelper.TABLE_NAME, col, null, null, null, null, null);
        ArrayList<Payload.Data> DetailsList = new ArrayList<>();
        while (crs.moveToNext())
        {
            Payload.Data data = new Payload.Data();
            data.setName(crs.getString(1));
            data.setDescription(crs.getString(7));
            DetailsList.add(data);
        }
        return DetailsList;
    }


    public Cursor SearchTrip(String newText) {
        ArrayList<String> Trips = new ArrayList<String>();
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getReadableDatabase();
        Cursor cr = database.rawQuery(" SELECT * FROM " + DatabaseHelper.TABLE_NAME + " WHERE " + DatabaseHelper.TRIP_COL + " LIKE ? ",
                new String[]{"%" + newText + "%"});
        if (cr.moveToFirst()) {
            do {
                Trips.add(cr.getString(0));

            } while (cr.moveToNext());
        }
        return cr;
    }


    public ArrayList<String> getTrip() {
        ArrayList<String> ar = new ArrayList<>();
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getReadableDatabase();
        Cursor cr = database.query(DatabaseHelper.TABLE_NAME3, null, null, null, null, null, DatabaseHelper.TRIP_COL);
        while (cr.moveToNext()) {
            ar.add(cr.getString(1));
        }
        cr.close();
        return ar;
    }

    public ArrayList<String> getTrans() {
        ArrayList<String> ar = new ArrayList<>();
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getReadableDatabase();
        Cursor cr = database.query(DatabaseHelper.TABLE_NAME4, null, null, null, null, null, DatabaseHelper.TRANSPORTS);
        while (cr.moveToNext()) {
            ar.add(cr.getString(1));
        }
        cr.close();
        return ar;
    }

    public ArrayList<String> getExtypes() {
        ArrayList<String> ar = new ArrayList<>();
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getReadableDatabase();
        Cursor cr = database.query(DatabaseHelper.TABLE_NAME5, null, null, null, null, null, DatabaseHelper.EXTYPE_COL);
        while (cr.moveToNext()) {
            ar.add(cr.getString(1));
        }
        cr.close();
        return ar;
    }


    public long InsertsTripName(String trip) {
        ContentValues rowValues = new ContentValues();

        rowValues.put(DatabaseHelper.TRIP_COL, trip);

        return database.insertOrThrow(DatabaseHelper.TABLE_NAME3, null, rowValues);

    }

    public long InsertsTransports(String trans) {
        ContentValues rowValues = new ContentValues();

        rowValues.put(DatabaseHelper.TRANSPORTS, trans);

        return database.insertOrThrow(DatabaseHelper.TABLE_NAME4, null, rowValues);

    }

    public long InsertsExpenseTypes(String exTypes) {
        ContentValues rowValues = new ContentValues();

        rowValues.put(DatabaseHelper.EXTYPE_COL, exTypes);

        return database.insertOrThrow(DatabaseHelper.TABLE_NAME5, null, rowValues);

    }

//    public ArrayList<String> getByTripID(long _id)
//    {
//        ArrayList<String> ar = new ArrayList<>();
//        dbHelper = new DatabaseHelper(context);
//        database = dbHelper.getReadableDatabase();
//        String query = " SELECT * FROM " + DatabaseHelper.TABLE_NAME + " INNER JOIN " + DatabaseHelper.TABLE_NAME2
//                + " ON " + DatabaseHelper.TABLE_NAME + " . " + DatabaseHelper.ID + " = " + DatabaseHelper.TABLE_NAME2
//                + " . " + DatabaseHelper.ID + " WHERE " + DatabaseHelper.TABLE_NAME2 + " . " + DatabaseHelper.ID +
//                " = " + _id;
//
//        Cursor cr = database.rawQuery(query, null);
//        while (cr.moveToNext()) {
//            ar.add(cr.getString(0));
//        }
//        cr.close();
//
//        return ar;
//    }
}
