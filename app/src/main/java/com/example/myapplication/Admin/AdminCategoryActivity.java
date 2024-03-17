package com.example.myapplication.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.myapplication.R;

public class AdminCategoryActivity extends AppCompatActivity {
    private ImageView logosausage, logoberries, logocheese, logocow, logofruit, logopig;
    private ImageView logovegetable, logoseed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        initLogo();
        logosausage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewActivity.class);
                intent.putExtra("category", "sausage");
                startActivity(intent);
            }
        });
        logoberries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewActivity.class);
                intent.putExtra("category", "berries");
                startActivity(intent);
            }
        });
        logocheese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewActivity.class);
                intent.putExtra("category", "cheese");
                startActivity(intent);
            }
        });
        logocow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewActivity.class);
                intent.putExtra("category", "cow");
                startActivity(intent);
            }
        });
        logofruit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewActivity.class);
                intent.putExtra("category", "fruit");
                startActivity(intent);
            }
        });
        logopig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewActivity.class);
                intent.putExtra("category", "pig");
                startActivity(intent);
            }
        });
        logovegetable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewActivity.class);
                intent.putExtra("category", "vegetable");
                startActivity(intent);
            }
        });
        logoseed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewActivity.class);
                intent.putExtra("category", "seed");
                startActivity(intent);
            }
        });
    }


    private void initLogo()
    {
        logosausage = findViewById(R.id.logosausage);
        logoberries = findViewById(R.id.logoberries);
        logocheese = findViewById(R.id.logocheese);
        logocow = findViewById(R.id.logocow);

        logofruit = findViewById(R.id.logofruit);
        logopig = findViewById(R.id.logopig);
        logovegetable = findViewById(R.id.logovegetable);
        logoseed = findViewById(R.id.logoseed);
    }
}