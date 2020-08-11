package com.example.imc;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Build;
import android.os.Bundle;

import java.util.Objects;

public class tableIMC extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_i_m_c);

        Toolbar topBar = findViewById(R.id.topBar);
        setSupportActionBar(topBar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("IMC Calculator");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}