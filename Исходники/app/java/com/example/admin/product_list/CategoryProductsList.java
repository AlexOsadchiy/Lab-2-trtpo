package com.example.admin.product_list;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CategoryProductsList extends AppCompatActivity implements View.OnClickListener{

    private static final String LOG_TAG = "111: ";
    Button addNewProduct;
    DBHelper dbHelper;
    SQLiteDatabase db;
    Cursor c;
    ContentValues cv;
    ListView listView;
    Context context = this;
    String categoryNameS;
    String productListName;
    ContentValues contentValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_products_list);
        categoryNameS = new String();
        productListName = new String();

        Intent intent = getIntent();

        contentValues = new ContentValues();

        categoryNameS = intent.getExtras().getString("CATEGORY_NAME");
        productListName = intent.getExtras().getString("LIST_NAME");

//        addNewProduct = (Button)findViewById(R.id.addToList);
//        addNewProduct.setOnClickListener(this);

        listView = (ListView) findViewById(R.id.listWithProductFromCategory);

        dbHelper = new DBHelper(this);

        // подключаемся к базе
        db = dbHelper.getWritableDatabase();
        cv = new ContentValues();


        update_products();
    }

        @Override
        public void onClick(View v) {
                switch (v.getId()) {
                case R.id.addToList:
                    //addSvoiProduct();
                break;
        default:
                break;
                }
        }


    public void update_products() {
        // проверка существования записей
        c = db.query("defaultProducts", null, null, null, null, null, null);
        final java.util.List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        if (c.getCount() > 0) {




            if (c.moveToFirst()) {
                do {
                    if(c.getString(c.getColumnIndex("category")).equals(categoryNameS) &&
                            !data.contains(c.getString(c.getColumnIndex("name")))) {
                        Map<String, String> datum = new HashMap<String, String>(2);
                        datum.put("Имя: ", c.getString(c.getColumnIndex("name")).toString());
                        datum.put("Стоимость: ", "Стоимость: " + c.getString(c.getColumnIndex("price")).toString());
                        data.add(datum);
                    }
                } while (c.moveToNext());
            }


            SimpleAdapter adapter = new SimpleAdapter(this, data,
                    android.R.layout.simple_list_item_2,
                    new String[] {"Имя: ", "Стоимость: "},
                    new int[] {android.R.id.text1,
                            android.R.id.text2});


            listView.setAdapter(adapter);




            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View itemClicked, int position,
                                        long id) {
                    c = db.query("defaultProducts", null, null, null, null, null, null);

                    if (c.moveToFirst()) {
                        do {
                            if(c.getString(c.getColumnIndex("category")).equals(categoryNameS) &&
                                    c.getString(c.getColumnIndex("name")).equals(data.get(position).get("Имя: ").toString()))
                            {
                                Log.d("!!!!!!!!!!!!!!!!!!: ", data.get(position).get("Имя: ").toString());
                                cv.put("category", c.getString(c.getColumnIndex("category")));
                                cv.put("name", c.getString(c.getColumnIndex("name")));
                                cv.put("count", Integer.parseInt(c.getString(c.getColumnIndex("count"))));
                                cv.put("price", Integer.parseInt(c.getString(c.getColumnIndex("price"))));
                                cv.put("listname", productListName);
                                db.insert("products", null, cv);


                                Toast.makeText(getApplicationContext(), productListName+": Добавлено",
                                        Toast.LENGTH_SHORT).show();

                                break;
                            }
                        } while (c.moveToNext());
                    }

                }
            });
        }
    }

    public void addSvoiProduct(){

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View promptView = layoutInflater.inflate(R.layout.activity_create_product, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(promptView);
        final EditText inputCategory = (EditText) promptView.findViewById(R.id.categoryText);
        final EditText inputName = (EditText) promptView.findViewById(R.id.nameText);
        //final EditText inputCount = (EditText) promptView.findViewById(R.id.countText);
        final EditText inputPrice = (EditText) promptView.findViewById(R.id.priceText);


        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        contentValues.put("category", inputCategory.getText().toString());
                        contentValues.put("count", 1);
                        contentValues.put("name", inputName.getText().toString());
                        contentValues.put("price", String.valueOf(inputPrice.getText().toString()));
                        db.insert("defaultProducts", null, contentValues);
                        update_products();

                    }
                })
                .setNegativeButton("Отмена",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        AlertDialog alertD = alertDialogBuilder.create();
        alertD.show();

    }


}
