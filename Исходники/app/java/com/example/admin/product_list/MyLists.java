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

public class MyLists extends AppCompatActivity implements View.OnClickListener {


    private static final String LOG_TAG = "the log from MyLists";
    Button addList;
    Context context = this;
    ListView listsView;

    DBHelper dbHelper;
    SQLiteDatabase db;
    ContentValues contentValues;
    Cursor dbCursor;
    final ArrayList<String> listArray = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_lists);

        addList = (Button) findViewById(R.id.addList);
        addList.setOnClickListener(this);

        listsView = (ListView) findViewById(R.id.myLists);
//        listsView.setOnClickListener(this);

        contentValues = new ContentValues();

        dbHelper = new DBHelper(this);
        // подключаемся к базе
        db = dbHelper.getWritableDatabase();

        update();
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.addList:

                LayoutInflater layoutInflater = LayoutInflater.from(context);
                View promptView = layoutInflater.inflate(R.layout.pop_out_input_list_name, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setView(promptView);
                final EditText input = (EditText) promptView.findViewById(R.id.userInput);


                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                contentValues.put("name", input.getText().toString());
                                db.insert("listNames", null, contentValues);
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

                break;
            default:
                break;
        }
    }


    public void update() {

        dbCursor = db.query("listNames", null, null, null, null, null, null);

        if (dbCursor.getCount() > 0) {

            final ArrayList<String> listArray = new ArrayList<String>();

            if (dbCursor.moveToFirst()) {
                do {
                    if (listArray.contains(dbCursor.getString(dbCursor.getColumnIndex("name"))) == false)
                        listArray.add(0,dbCursor.getString(dbCursor.getColumnIndex("name")));
                } while (dbCursor.moveToNext());
            }


            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listArray);
            listsView.setAdapter(adapter);

            listsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View itemClicked, int position,
                                        long id) {

                    Intent intent = new Intent(MyLists.this, List.class);
                    intent.putExtra("LIST_NAME", ((TextView) itemClicked).getText().toString());
                    startActivity(intent);

                }
            });

            listsView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view,
                                               int arg2, long arg3) {


                    db.delete("listNames", "name = ?", new String[]{listArray.get(arg2)});
                    db.delete("products", "listname = ?", new String[]{listArray.get(arg2)});
                    dbCursor = db.query("listNames", null, null, null, null, null, null);
                    if(dbCursor.getCount() > 0){
                        update();
                    }else{
                        adapter.clear();
                    }
                    return false;
                }
            });

        }
    }



}
