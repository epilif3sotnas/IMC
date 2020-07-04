package com.example.imc;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private Button btSubmit, btViewAll, btDelete;
    private EditText editAge, editWeight, editHeight;
    private CollectionReference db = FirebaseFirestore.getInstance().collection("user");

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar topbar = findViewById(R.id.topBar);
        setSupportActionBar(topbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("IMC Calculator");
        editAge = findViewById(R.id.editAge);
        editWeight = findViewById(R.id.editWeight);
        editHeight = findViewById(R.id.editHeight);
        btSubmit = findViewById(R.id.btSubmit);
        btViewAll = findViewById(R.id.btViewAll);
        btDelete = findViewById(R.id.btDelete);

        Submit();
        ViewAll();
        Delete();
    }
    public void Submit(){
        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                String currentDate = format.format(Calendar.getInstance().getTime());

                if(editAge.length() == 0 || editHeight.length() == 0 || editWeight.length() == 0){
                    Toast.makeText(MainActivity.this, "Parameters empty", Toast.LENGTH_LONG).show();
                    return;
                }
                double weight = Double.parseDouble(editWeight.getText().toString());
                double height_m = 0.01 * Integer.parseInt(editHeight.getText().toString());
                int age = Integer.parseInt(editAge.getText().toString());

                if(age == 0 || height_m == 0 || weight == 0){
                    Toast.makeText(MainActivity.this, "Parameters with 0", Toast.LENGTH_LONG).show();
                    return;
                }

                String imc = IMC.calcIMC(weight, height_m);

                Map<String, Object> user = new HashMap<>();
                user.put("Name", "User");
                user.put("Age", age);
                user.put("Weight", weight);
                user.put("Height", height_m);
                user.put("Date", currentDate);
                user.put("IMC", imc);

                db.document(currentDate).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("MainActivity", "DocumentSnapshot added with ID: " + db.getId());
                        Toast.makeText(MainActivity.this, "Data inserted", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("MainActivity", "Error adding document", e);
                        Toast.makeText(MainActivity.this, "Data not inserted", Toast.LENGTH_LONG).show();
                    }
                });
                message(age, height_m, imc);
            }
        });
    }
    public void message(int age, double height, String imc){
        String buffer = IMC.refIMC(imc, age, height);
        showMessage2("IMC", buffer);
    }
    public void ViewAll(){
        btViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        StringBuffer buffer = new StringBuffer();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                buffer.append("Name: " + document.getString("Name") + "\n");
                                buffer.append("Date: " + document.getString("Date") + "\n");
                                buffer.append("Age: " + document.get("Age").toString() + "\n");
                                buffer.append("Height: " + document.get("Height").toString() + "\n");
                                buffer.append("Weight: " + document.get("Weight").toString() + "\n");
                                buffer.append("IMC: " + document.get("IMC").toString() + "\n\n");
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "Nothing found", Toast.LENGTH_LONG).show();
                            Log.d("MainActivity", "Error getting documents: ", task.getException());
                        }
                        showMessage("Data", buffer.toString());
                    }
                });
            }
        });
    }
    public void showMessage(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
    public void showMessage2(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
    public void Delete(){
        btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDelete();
            }
        });
    }
    public void openDelete(){
        Intent intent = new Intent(this, Delete.class);
        startActivity(intent);
    }
}
