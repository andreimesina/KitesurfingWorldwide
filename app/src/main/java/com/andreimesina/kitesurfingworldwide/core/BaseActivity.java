package com.andreimesina.kitesurfingworldwide.core;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public abstract class BaseActivity extends AppCompatActivity {

    private Toolbar toolbar;

    protected void initToolbar(int id) {
        toolbar = findViewById(id);
        setSupportActionBar(toolbar);
    }

    protected void setBackButtonToolbar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
}
