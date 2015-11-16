package com.example.admin.product_list;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StatisticActivity extends AppCompatActivity {

    ListView listView;
    DBHelper dbHelper;
    SQLiteDatabase db;
    Cursor dbCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);

        listView = (ListView)findViewById(R.id.statisticView);

        dbHelper = new DBHelper(this);
        // подключаемся к базе
        db = dbHelper.getWritableDatabase();

        makeStatistic();
    }


    public void makeStatistic(){

        java.util.List<Map<String, String>> data = new ArrayList<Map<String, String>>();;
        Map<String, String> datum = new HashMap<String, String>(2);

        int list_names_count = 0;
        int products_counter = 0;
        int price_counter = 0;
        int big_price = 0;
        int average_price = 0;

        dbCursor = db.query("listNames", null, null, null, null, null, null);
        if (dbCursor.moveToFirst()) {
            do {
                list_names_count++;
            } while (dbCursor.moveToNext());
        }


        datum.put("q", "Колличество списков");
        datum.put("w", String.valueOf(list_names_count));
        data.add(datum);



        dbCursor = db.query("products", null, null, null, null, null, null);
        if (dbCursor.moveToFirst()) {
            do {
                products_counter++;
                price_counter+=Integer.parseInt(dbCursor.getString(dbCursor.getColumnIndex("price")));
                if(big_price < Integer.parseInt(dbCursor.getString(dbCursor.getColumnIndex("price"))))
                    big_price = Integer.parseInt(dbCursor.getString(dbCursor.getColumnIndex("price")));
            } while (dbCursor.moveToNext());
        }

    if(price_counter > 0)
        average_price = price_counter/products_counter;

        Map<String, String> datum1 = new HashMap<String, String>(2);
        datum1.put("q", "Колличество продуктов");
        datum1.put("w", String.valueOf(products_counter));
        data.add(datum1);
        Map<String, String> datum2 = new HashMap<String, String>(2);
        datum2.put("q", "Общая стоимость всех товаров");
        datum2.put("w", String.valueOf(price_counter));
        data.add(datum2);
        Map<String, String> datum3 = new HashMap<String, String>(2);
        datum3.put("q", "Средная стоимость товара");
        datum3.put("w", String.valueOf(average_price));
        data.add(datum3);


        final SimpleAdapter adapter = new SimpleAdapter(this, data,
                android.R.layout.simple_list_item_2,
                new String[] {"q", "w"},
                new int[] {android.R.id.text1,
                        android.R.id.text2});

        listView.setAdapter(adapter);


    }
}
