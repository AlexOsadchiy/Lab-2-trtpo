package com.example.admin.product_list;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button create_product;
    Button show_product;
    Button statistic;
    Button exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        show_product = (Button) findViewById(R.id.showListButton);
        show_product.setOnClickListener(this);

        create_product = (Button) findViewById(R.id.makeListButton);
        create_product.setOnClickListener(this);

        statistic = (Button) findViewById(R.id.statisticButton);
        statistic.setOnClickListener(this);

        exit = (Button) findViewById(R.id.exitButton);
        exit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.exitButton:
                System.exit(0);
                break;
            case R.id.showListButton:
                Intent intent = new Intent(this, ProductList.class);
                startActivity(intent);
                break;
            case R.id.makeListButton:
                Intent intent1 = new Intent(this, CreateProduct.class);
                startActivity(intent1);
                break;
            case R.id.statisticButton:

                Intent intent2 = new Intent(this, StatisticActivity.class);
                startActivity(intent2);
//
//                AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
//                dlgAlert.setMessage("This is an alert with no consequence");
//                dlgAlert.setTitle("App Title");
//                dlgAlert.setPositiveButton("OK", null);
//                dlgAlert.setCancelable(true);
//                dlgAlert.create().show();
                break;
            default:
                break;
        }
    }



}
