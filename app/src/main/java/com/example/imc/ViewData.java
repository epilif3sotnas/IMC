package com.example.imc;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class ViewData extends AppCompatActivity {
    private ListView listView;
    private ProgressBar loading;
    private TextView date, age, weight, height, imc;
    FirebaseUser userCurrent = FirebaseAuth.getInstance().getCurrentUser();
    private  CollectionReference db = FirebaseFirestore.getInstance().collection(Objects.requireNonNull(userCurrent).getUid());

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_data);

        loading = findViewById(R.id.loading);
        loading.setVisibility(View.VISIBLE);
        date = findViewById(R.id.date);
        age = findViewById(R.id.age);
        weight = findViewById(R.id.weight);
        height = findViewById(R.id.height);
        imc = findViewById(R.id.imc);
        date.setVisibility(View.GONE);
        age.setVisibility(View.GONE);
        weight.setVisibility(View.GONE);
        height.setVisibility(View.GONE);
        imc.setVisibility(View.GONE);

        Toolbar topbar = findViewById(R.id.topBar);
        setSupportActionBar(topbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("IMC Calculator");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = findViewById(R.id.data);
        listView.setVisibility(View.GONE);

        boolean network = isConnectedNetwork();
        if (!network){
            Toast.makeText(ViewData.this, "No internet connection", Toast.LENGTH_LONG).show();
        }
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
                Collections.reverse(data);
                if ((!data.isEmpty())){
                    CustomAdapterData customAdapter = new CustomAdapterData(data, getApplicationContext());
                    loading.setVisibility(View.GONE);
                    date.setVisibility(View.VISIBLE);
                    age.setVisibility(View.VISIBLE);
                    weight.setVisibility(View.VISIBLE);
                    height.setVisibility(View.VISIBLE);
                    imc.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.VISIBLE);
                    listView.setAdapter(customAdapter);
                }
            }
        });
    }
    public boolean isConnectedNetwork(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}