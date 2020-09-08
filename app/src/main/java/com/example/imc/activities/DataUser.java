package com.example.imc.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.imc.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class DataUser extends AppCompatActivity {
    private Button btSubmit;
    private EditText editAge, editHeight;
    private TextView currentAge, currentHeight;
    FirebaseUser userCurrent = FirebaseAuth.getInstance().getCurrentUser();
    private CollectionReference db = FirebaseFirestore.getInstance().collection(Objects.requireNonNull(userCurrent).getUid() + "Data");

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_user);

        btSubmit = findViewById(R.id.btSubmit);
        editAge = findViewById(R.id.editAge);
        editHeight = findViewById(R.id.editHeight);
        currentAge = findViewById(R.id.currentAge);
        currentHeight = findViewById(R.id.currentHeight);

        Toolbar topbar = findViewById(R.id.topBar);
        setSupportActionBar(topbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("IMC Calculator");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadData();
        buttons();
    }
    public void buttons(){
        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editAge.length() == 0 || editHeight.length() == 0){
                    Toast.makeText(DataUser.this, "Parameters empty", Toast.LENGTH_LONG).show();
                    return;
                }
                double height = Integer.parseInt(editHeight.getText().toString());
                int age = Integer.parseInt(editAge.getText().toString());
                if(age <= 0 || height <= 0){
                    Toast.makeText(DataUser.this, "Parameters invalid", Toast.LENGTH_LONG).show();
                    return;
                }
                Map<String, Object> user = new HashMap<>();
                user.put("Name", userCurrent.getEmail());
                user.put("Age", age);
                user.put("Height", height);

                db.document("dataUser").set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("MainActivity", "DocumentSnapshot added with ID: " + db.getId());
                        Toast.makeText(DataUser.this, "Data inserted", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("MainActivity", "Error adding document", e);
                        Toast.makeText(DataUser.this, "Data not inserted", Toast.LENGTH_LONG).show();
                    }
                });
                editAge.getText().clear();
                editHeight.getText().clear();
                loadData();
            }
        });
    }
    public void loadData(){
        db.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                        currentAge.setText(Objects.requireNonNull(document.get("Age")).toString());
                        currentHeight.setText(Objects.requireNonNull(document.get("Height")).toString());
                    }
                }
            }
        });

    }
}