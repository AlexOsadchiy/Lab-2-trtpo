package com.example.admin.product_list;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by admin on 14.11.2015.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String LOG_TAG = "myLog";

    public DBHelper(Context context) {
            // конструктор суперкласса
            super(context, "myDataBase", null, 1);
        }

        public void onCreate(SQLiteDatabase db) {

            // создаем таблицы
            db.execSQL("create table defaultProducts ("
                    + "id integer primary key autoincrement," + "category text,"
                    + "name text," + "count integer," + "price integer" + ");");

            db.execSQL("create table listNames ("
                    + "id integer primary key autoincrement," + "name text" + ");");

            db.execSQL("create table products ("
                    + "id integer primary key autoincrement," + "category text,"
                    + "name text," + "count integer," + "price integer," + "listname text" + ");");

        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
}
