package com.example.admin.product_list;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String LOG_TAG = "myLogs";
    Button my_list;
    Button show_product;
    Button statistic;
    Button exit;

    DBHelper dbHelper;
    SQLiteDatabase db;
    Cursor c;
    ContentValues contentValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        my_list = (Button) findViewById(R.id.myListButton);
        my_list.setOnClickListener(this);

        statistic = (Button) findViewById(R.id.statisticButton);
        statistic.setOnClickListener(this);

        exit = (Button) findViewById(R.id.exitButton);
        exit.setOnClickListener(this);

        dbHelper = new DBHelper(this);
        // подключаемся к базе
        db = dbHelper.getWritableDatabase();
        contentValues = new ContentValues();



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.exitButton:
                System.exit(0);
                break;
            case R.id.myListButton:
                Intent intent1 = new Intent(this, MyLists.class);
                startActivity(intent1);
                break;
            case R.id.statisticButton:
                Intent intent2 = new Intent(this, StatisticActivity.class);
               startActivity(intent2);
                break;
            default:
                break;
        }
    }



}
