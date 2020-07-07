package com.example.imc;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.about:
                Intent add = new Intent(this, About.class);
                startActivity(add);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
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
                editWeight.getText().clear();
                message(age, height_m, imc);
            }
        });
    }
    public void message(int age, double height, String imc){
        String buffer = IMC.refIMC(imc, age, height);
        showMessage("IMC", buffer);
    }
    public void ViewAll(){
        btViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityViewData();
            }
        });
    }
    public void startActivityViewData() {
        Intent viewData = new Intent(this, ViewData.class);
        startActivity(viewData);
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
}
