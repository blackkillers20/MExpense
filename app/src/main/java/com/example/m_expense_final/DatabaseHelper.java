package com.example.m_expense_final;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private SQLiteDatabase database;
    public static final String DATABASE_NAME = "M_EXPENSE2";


    public static final String TABLE_NAME = "tripExpense";
    public static final String TABLE_NAME2 = "ExpenseDetails";
    public static final String TABLE_NAME3 = "TripName";
    public static final String TABLE_NAME4 = "Transportations";
    public static final String TABLE_NAME5 = "TypeOfExpenses";

    public static final String ID = "_id";

    public static final String TRIP_COL = "Name";
    public static final String DES_COL = "Destination";
    public static final String DATE_COL = "TripDate";
    public static final String RISK_COL = "RiskAsses";
    public static final String INFO_COL = "Description";
    public static final String DATE_END_COL = "TripDateEnd";
    public static final String TRANSPORTS = "Transportation";


    public static final String EXTYPE_COL = "ExpenseTypes";
    public static final String EXAMOUT_COL = "ExpenseAmount";
    public static final String EXTIME_COL = "ExpenseTime";
    public static final String CMT_COL = "AddtionalComment";
    public static final String TRIPNAME_COL = "TripName";


    public DatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, 22);
        database = this.getWritableDatabase();
    }

    private static final String CREATE_TABLE = " create table " + TABLE_NAME + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TRIP_COL + " TEXT, "
            + DES_COL + " TEXT, " + DATE_COL +  " TEXT, " + DATE_END_COL + " TEXT, " + TRANSPORTS + " TEXT, " + RISK_COL + " TEXT, " + INFO_COL + " TEXT);";

    private static final String CREATE_TABLE2 = " create table " + TABLE_NAME2 + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + EXTYPE_COL + " TEXT, " + EXAMOUT_COL + " TEXT, " + EXTIME_COL + " TEXT, " + CMT_COL + " TEXT, " +
            TRIPNAME_COL + " TEXT,  " + " FOREIGN KEY ("+ID+") REFERENCES "+TABLE_NAME+"("+ID+"));";

    private static final String CREATE_TABLE3 = " create table " + TABLE_NAME3 + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + TRIP_COL + " TEXT);";

    private static final String CREATE_TABLE4 = " create table " + TABLE_NAME4 + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + TRANSPORTS + " TEXT);";

    private static final String CREATE_TABLE5 = " create table " + TABLE_NAME5 + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + EXTYPE_COL + " TEXT);";



    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(CREATE_TABLE);
        db.execSQL(CREATE_TABLE2);
        db.execSQL(CREATE_TABLE3);
        db.execSQL(CREATE_TABLE4);
        db.execSQL(CREATE_TABLE5);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME2);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME3);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME4);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME5);
        onCreate(db);
    }

    public long addTrip(String trip)
    {
        ContentValues rowValues = new ContentValues();
        rowValues.put(DatabaseHelper.TRIP_COL, trip);
        return  database.insertOrThrow(DatabaseHelper.TABLE_NAME, null, rowValues);
    }


}
