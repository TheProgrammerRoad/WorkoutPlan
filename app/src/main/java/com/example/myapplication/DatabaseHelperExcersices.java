package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.preference.PreferenceManager;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DatabaseHelperExcersices extends SQLiteOpenHelper {

    private Context mContext;

    public DatabaseHelperExcersices(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    public void queryData(String sql)
    {
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
    }

    public void insertData(String tableName, String name, String einstellungen, String kg1, String kg2, String kg3, String kg4, String wdh1, String wdh2, String wdh3, String wdh4, String bildUebung)
    {
        SQLiteDatabase database = getWritableDatabase();

        String sql = "INSERT INTO " + tableName + " VALUES (NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindString(1, name);
        statement.bindString(2, einstellungen);
        statement.bindString(3, kg1);
        statement.bindString(4, kg2);
        statement.bindString(5, kg3);
        statement.bindString(6, kg4);
        statement.bindString(7, wdh1);
        statement.bindString(8, wdh2);
        statement.bindString(9, wdh3);
        statement.bindString(10, wdh4);
        statement.bindString(11, bildUebung);

        statement.executeInsert();


    }

    public void updateData(String sqlQuery)
    {
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL(sqlQuery);
    }

    public Cursor getData(String sql)
    {
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql, null);
    }

    public Integer deleteData (String tableName, int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sId = Integer.toString(id);
        int deleteSuceeded = db.delete(tableName, "ID = ?", new String[] {sId});
        return deleteSuceeded;
    }

    public void updateOrder(String tableName, ArrayList<Training> trainingList)
    {
        String tableNameNeu = tableName + "Neu";
        MainActivity.sqLiteHelperExcersice.queryData("CREATE TABLE IF NOT EXISTS " + tableNameNeu + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, einstellungen TEXT, kg1 TEXT, kg2 TEXT, kg3 TEXT, kg4 TEXT, wdh1 TEXT, wdh2 TEXT, wdh3 TEXT, wdh4 TEXT, bildUebung TEXT)");

        for (int i = 0; i < trainingList.size(); i++)
        {
            insertData(
                    tableNameNeu,
                    trainingList.get(i).getNameTraining(),
                    trainingList.get(i).getEinstellungen(),
                    trainingList.get(i).getKg1(),
                    trainingList.get(i).getKg2(),
                    trainingList.get(i).getKg3(),
                    trainingList.get(i).getKg4(),
                    trainingList.get(i).getWdh1(),
                    trainingList.get(i).getWdh2(),
                    trainingList.get(i).getWdh3(),
                    trainingList.get(i).getWdh4(),
                    trainingList.get(i).getTrainingImage()
            );
        }

        MainActivity.sqLiteHelperExcersice.queryData("DELETE FROM sqlite_sequence WHERE NAME='" + tableName + "';");
        MainActivity.sqLiteHelperExcersice.queryData("DELETE FROM " + tableName);
        MainActivity.sqLiteHelperExcersice.queryData("INSERT INTO " + tableName + " (name, einstellungen, kg1, kg2, kg3, kg4, wdh1, wdh2, wdh3, wdh4, bildUebung) SELECT name, einstellungen, kg1, kg2, kg3, kg4, wdh1, wdh2, wdh3, wdh4, bildUebung FROM " + tableNameNeu);
        MainActivity.sqLiteHelperExcersice.queryData("DROP TABLE IF EXISTS " + tableNameNeu);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
