package com.example.admin.product_list;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CategoryList extends AppCompatActivity {

    DBHelper dbHelper;
    SQLiteDatabase db;
    Cursor c;

    ListView myView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorylist);

        myView = (ListView) findViewById(R.id.listView);

        dbHelper = new DBHelper(this);
        // подключаемся к базе
        db = dbHelper.getWritableDatabase();
        // проверка существования записей
        c = db.query("productlist", null, null, null, null, null, null);

        if (c.getCount() > 0) {

            final ArrayList<String> categoryesArray = new ArrayList<String>();

            if (c.moveToFirst()) {
                do {
                    if(categoryesArray.contains(c.getString(c.getColumnIndex("category"))) == false)
                        categoryesArray.add(0,c.getString(c.getColumnIndex("category")));//починить дублирование нескольких категорий
                } while (c.moveToNext());
            }

            // используем адаптер данных
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, categoryesArray);
            myView.setAdapter(adapter);
            myView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View itemClicked, int position,
                                        long id) {
                    Toast.makeText(getApplicationContext(), ((TextView) itemClicked).getText(),
                            Toast.LENGTH_SHORT).show();
                }
            });
            c.close();
            dbHelper.close();

        }

    }


}