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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class AddNewProduct extends AppCompatActivity  implements View.OnClickListener {


    Button addNewProduct;
    ListView listView;

    DBHelper dbHelper;
    SQLiteDatabase db;
    Cursor c;
    String listName="";

    Context context = this;
    ArrayList<String> categoryesArray;
    ArrayAdapter<String> adapter;
    ContentValues contentValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_product);

        contentValues = new ContentValues();

        listName = getIntent().getExtras().getString("LIST_NAME");

        addNewProduct = (Button)findViewById(R.id.addSvoiProduct);
        addNewProduct.setOnClickListener(this);

        listView = (ListView)findViewById(R.id.addProductsList);
        categoryesArray = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, categoryesArray);

        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();

        update();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addSvoiProduct:
                    addSvoiProduct();
                break;
            default:
                break;
        }
    }

    public void update()
    {
        // проверка существования записей
        c = db.query("defaultProducts", null, null, null, null, null, null);

        if (c.getCount() > 0) {

            if (c.moveToFirst()) {
                do {
                    if(categoryesArray.contains(c.getString(c.getColumnIndex("category"))) == false)
                        categoryesArray.add(0,c.getString(c.getColumnIndex("category")));//починить дублирование нескольких категорий
                } while (c.moveToNext());
            }

            // используем адаптер данных

            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View itemClicked, int position,
                                        long id) {



                    Intent intent = new Intent(AddNewProduct.this, CategoryProductsList.class);
                    intent.putExtra("CATEGORY_NAME", ((TextView) itemClicked).getText().toString());
                    intent.putExtra("LIST_NAME", listName);
                    startActivity(intent);

//                    update_products(((TextView) itemClicked).getText().toString());
//                    Toast.makeText(getApplicationContext(), ((TextView) itemClicked).getText(),
//                            Toast.LENGTH_SHORT).show();
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
        final EditText inputCount = (EditText) promptView.findViewById(R.id.countText);
        final EditText inputPrice = (EditText) promptView.findViewById(R.id.priceText);


        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {


                        contentValues.put("category", inputCategory.getText().toString());
                        contentValues.put("count", String.valueOf(inputCount.getText().toString()));
                        contentValues.put("name", inputName.getText().toString());
                        contentValues.put("price", String.valueOf(inputPrice.getText().toString()));
                        db.insert("defaultProducts", null, contentValues);
                        update();

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


