package com.example.imc;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Build;
import android.os.Bundle;

import java.util.Objects;

public class About extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Toolbar topbar = findViewById(R.id.topBar);
        setSupportActionBar(topbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("IMC Calculator");
    }
}