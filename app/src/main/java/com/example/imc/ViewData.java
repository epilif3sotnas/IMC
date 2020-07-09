package com.example.imc;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Build;
import android.os.Bundle;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class ViewData extends AppCompatActivity {
    private ListView listView;
    private  static CollectionReference db = FirebaseFirestore.getInstance().collection("user");

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_data);

        Toolbar topbar = findViewById(R.id.topBar);
        setSupportActionBar(topbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("IMC Calculator");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = findViewById(R.id.data);

        loadData();
    }
    public void loadData(){
        db.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                ArrayList<objData> data = new ArrayList<>();
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                        objData obj = new objData(Objects.requireNonNull(document.get("Date")).toString(), Objects.requireNonNull(document.get("Age")).toString(), Objects.requireNonNull(document.get("Height")).toString(), Objects.requireNonNull(document.get("Weight")).toString(), Objects.requireNonNull(document.get("IMC")).toString());
                        data.add(obj);
                    }
                }
                if ((!data.isEmpty())){
                    CustomAdapterData customAdapter = new CustomAdapterData(data, getApplicationContext());
                    listView.setAdapter(customAdapter);
                }
            }
        });
    }
}