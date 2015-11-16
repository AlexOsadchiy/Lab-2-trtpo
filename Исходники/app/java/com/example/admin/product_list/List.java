package com.example.admin.product_list;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class List extends AppCompatActivity implements View.OnClickListener {


    private static final String LOG_TAG = "LIST:LOG = ";
    DBHelper dbHelper;
    SQLiteDatabase db;
    Cursor dbCursor;
    Cursor twoCursor;
    Button addProducts;
    Button refresh;
    ListView listView;
    String listName="";
    TextView textViewListName;
    java.util.List<Map<String, String>> data;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Log.d(LOG_TAG, "Back to list");
        listName = new String();
        listName = getIntent().getExtras().getString("LIST_NAME");

        textViewListName = (TextView) findViewById(R.id.listName);
        textViewListName.setText(listName);


        refresh = (Button) findViewById(R.id.refreshButton);
        refresh.setOnClickListener(this);

        addProducts = (Button) findViewById(R.id.addProducts);
        addProducts.setOnClickListener(this);

        listView = (ListView) findViewById(R.id.listContentList);

        dbHelper = new DBHelper(this);
        // подключаемся к базе
        db = dbHelper.getWritableDatabase();


        data = new ArrayList<Map<String, String>>();
        update();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addProducts:
                Intent intent = new Intent(this, AddNewProduct.class);
                intent.putExtra("LIST_NAME", listName);
                startActivity(intent);
                break;
            case R.id.refreshButton:
                update();
                break;
            default:
                break;
        }
    }

    public void update() {

        data.clear();

        Log.d(LOG_TAG, listName);
        dbCursor = db.query("products", null, null, null, null, null, null);
        twoCursor = dbCursor;






        if (dbCursor.getCount() > 0) {

            int count = 0;

            if (dbCursor.moveToFirst()) {
                do {
                    if(dbCursor.getString(dbCursor.getColumnIndex("listname")).equals(listName)) {

                        Map<String, String> datum = new HashMap<String, String>(2);
                        datum.put("Имя", dbCursor.getString(dbCursor.getColumnIndex("name")));
                        datum.put("Стоимость",dbCursor.getString(dbCursor.getColumnIndex("price")).toString());
                        data.add(datum);
                        count++;
                    }

                } while (dbCursor.moveToNext());
            }


                final SimpleAdapter adapter = new SimpleAdapter(this, data,
                    android.R.layout.simple_list_item_2,
                    new String[] {"Имя", "Стоимость"},
                    new int[] {android.R.id.text1,
                            android.R.id.text2});

            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View itemClicked, int position,
                                        long id) {
//
//

                }
            });

            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view,
                                               int arg2, long arg3) {

                    Log.d("QQQ: ", "Long Tab" + data.get(arg2).get("Имя").toString());

                    db.delete("products", "listname = ? and name = ?", new String[]{listName, data.get(arg2).get("Имя").toString()});
                    update();
                    return false;
                    }
                }

                );


//            if(count == 0)
//            {
//                AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
//                dlgAlert.setMessage("Список пуст!");
//                dlgAlert.setTitle("Уведомление");
//                dlgAlert.setPositiveButton("OK", null);
//                dlgAlert.setCancelable(true);
//                dlgAlert.create().show();
//            }

            }else {

            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
            dlgAlert.setMessage("Список пуст!");
            dlgAlert.setTitle("Уведомление");
            dlgAlert.setPositiveButton("OK", null);
            dlgAlert.setCancelable(true);
            dlgAlert.create().show();
        }
    }

}