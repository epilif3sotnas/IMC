package com.example.imc.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
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

import com.example.imc.objects.IMC;
import com.example.imc.R;
import com.example.imc.ui.login.LoginActivity;
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

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class MainActivity extends AppCompatActivity {
    private Button btSubmit, btViewAll;
    private EditText editWeight;
    FirebaseUser userCurrent = FirebaseAuth.getInstance().getCurrentUser();
    private CollectionReference db = FirebaseFirestore.getInstance().collection(Objects.requireNonNull(userCurrent).getUid());
    private CollectionReference db2 = FirebaseFirestore.getInstance().collection(Objects.requireNonNull(userCurrent).getUid() + "Data");
    private static final String TAG = MainActivity.class.getName();
    private String age, height_cm;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar topbar = findViewById(R.id.topBar);
        setSupportActionBar(topbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("IMC Calculator");
        editWeight = findViewById(R.id.editWeight);
        btSubmit = findViewById(R.id.btSubmit);
        btViewAll = findViewById(R.id.btViewAll);

        if (!isConnectedNetwork()){
            Toast.makeText(MainActivity.this, "No internet connection", Toast.LENGTH_LONG).show();
        }
        buttons();
        loadData();
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
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.tableIMC:
                startActivity(new Intent(this, tableIMC.class));
                break;
            case R.id.settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    public void buttons(){
        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isConnectedNetwork()){
                    Toast.makeText(MainActivity.this, "No internet Connection", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "No internet Connection");
                    return;
                }
                if (userCurrent == null){
                    Toast.makeText(MainActivity.this, "No user logged", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "No user logged");
                    return;
                }
                Log.d(TAG, "Internet Connection");
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                String currentDate = format.format(Calendar.getInstance().getTime());

                if(editWeight.length() == 0){
                    Toast.makeText(MainActivity.this, "Parameter empty", Toast.LENGTH_LONG).show();
                    return;
                }
                if (age == null || height_cm == null){
                    Toast.makeText(MainActivity.this, "No age or height entered in settings", Toast.LENGTH_LONG).show();
                    return;
                }
                double weight = Double.parseDouble(editWeight.getText().toString());
                double height_m = 0.01 * Double.parseDouble(height_cm);
                int ageInt = Integer.parseInt(age);

                if(weight == 0){
                    Toast.makeText(MainActivity.this, "Parameters with 0", Toast.LENGTH_LONG).show();
                    return;
                }

                String imc = IMC.calcIMC(weight, height_m);

                Map<String, Object> user = new HashMap<>();
                user.put("Name", userCurrent.getEmail());
                user.put("Age", ageInt);
                user.put("Weight", weight);
                user.put("Height", height_m);
                user.put("Date", currentDate);
                user.put("IMC", imc);

                db.document(currentDate).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + db.getId());
                        Toast.makeText(MainActivity.this, "Data inserted", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                        Toast.makeText(MainActivity.this, "Data not inserted", Toast.LENGTH_LONG).show();
                    }
                });
                editWeight.getText().clear();
                message(ageInt, height_m, imc, weight);
            }
        });
        btViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ViewData.class));
            }
        });
    }
    public void message(int age, double height, String imc, double weight){
        String buffer = IMC.refIMC(imc, age, height);
        showMessage("IMC", "Your weight: " + weight + "kg\n" + buffer);
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
    public boolean isConnectedNetwork(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
    public void loadData(){
        db2.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                        age = Objects.requireNonNull(document.get("Age")).toString();
                        height_cm = Objects.requireNonNull(document.get("Height")).toString();
                    }
                }
            }
        });
    }
}