package com.example.imc;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private Button btSubmit, btViewAll, btDelete, btGuest;
    private EditText editAge, editWeight, editHeight, input;
    private CollectionReference db = FirebaseFirestore.getInstance().collection("user");
    private String datePass = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editAge = (EditText) findViewById(R.id.editAge);
        editWeight = (EditText) findViewById(R.id.editWeight);
        editHeight = (EditText) findViewById(R.id.editHeight);
        btSubmit = (Button) findViewById(R.id.btSubmit);
        btViewAll = (Button) findViewById(R.id.btViewAll);
        btDelete = (Button) findViewById(R.id.btDelete);
        btGuest = (Button) findViewById(R.id.btGuest);

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

                int age = Integer.parseInt(editAge.getText().toString());
                double weight = Double.parseDouble(editWeight.getText().toString());
                int height_cm = Integer.parseInt(editHeight.getText().toString());
                double height_m = height_cm * 0.01;
                DecimalFormat df2 = new DecimalFormat("#.##");
                String imc = df2.format(weight / (height_m * height_m));
                String name = "User";

                if(age == 0 || height_m == 0 || weight == 0){
                    Toast.makeText(MainActivity.this, "Parameters with 0", Toast.LENGTH_LONG).show();
                    return;
                }

                Map<String, Object> user = new HashMap<>();
                user.put("Name", name);
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
        Double imc_d = Double.parseDouble(imc);
        Double weightIdeal_min;
        Double weightIdeal_max;
        StringBuffer buffer = new StringBuffer();
        if(age < 15)
            return;
        if(age <= 24){
            if(imc_d < 19){
                buffer.append("Your IMC is " + imc_d + ", you are in low weight.\n");
            }
            else if(imc_d >= 19 && imc_d < 24){
                buffer.append("Your IMC is " + imc_d + ", you are in normal weight.\n");
            }
            else if(imc_d >= 24 && imc_d < 29){
                buffer.append("Your IMC is " + imc_d + ", you are in above weight.\n");
            }
            else if(imc_d >= 29 && imc_d < 39){
                buffer.append("Your IMC is " + imc_d + ", you are in obesity.\n");
            }
            else if(imc_d >= 39){
                buffer.append("Your IMC is " + imc_d + ", you are in serious obesity.\n");
            }
            else{
                return;
            }
            weightIdeal_min = height * height * 19;
            weightIdeal_max = height * height * 24;

            buffer.append("Your normal weight is between " + weightIdeal_min + " and " + weightIdeal_max + ".\n");

        }
        if(age >= 25 && age <= 34){
            if(imc_d < 20){
                buffer.append("Your IMC is " + imc_d + ", you are in low weight.\n");            }
            else if(imc_d >= 20 && imc_d < 25){
                buffer.append("Your IMC is " + imc_d + ", you are in normal weight.\n");
            }
            else if(imc_d >= 25 && imc_d < 30){
                buffer.append("Your IMC is " + imc_d + ", you are in above weight.\n");
            }
            else if(imc_d >= 30 && imc_d < 40){
                buffer.append("Your IMC is " + imc_d + ", you are in obesity.\n");
            }
            else if(imc_d >= 40){
                buffer.append("Your IMC is " + imc_d + ", you are in serious obesity.\n");
            }
            else{
                return;
            }
            weightIdeal_min = height * height * 20;
            weightIdeal_max = height * height * 25;

            buffer.append("Your normal weight is between " + weightIdeal_min + " and " + weightIdeal_max + ".\n");
        }
        if(age >= 35 && age <= 44) {
            if (imc_d < 21) {
                buffer.append("Your IMC is " + imc_d + ", you are in low weight.\n");
            } else if (imc_d >= 21 && imc_d < 26) {
                buffer.append("Your IMC is " + imc_d + ", you are in normal weight.\n");
            } else if (imc_d >= 26 && imc_d < 31) {
                buffer.append("Your IMC is " + imc_d + ", you are in above weight.\n");
            } else if (imc_d >= 31 && imc_d < 41) {
                buffer.append("Your IMC is " + imc_d + ", you are in obesity.\n");
            } else if (imc_d >= 41) {
                buffer.append("Your IMC is " + imc_d + ", you are in serious obesity.\n");
            } else {
                return;
            }
            weightIdeal_min = height * height * 21;
            weightIdeal_max = height * height * 26;

            System.out.println("Your normal weight is between " + weightIdeal_min + " and " + weightIdeal_max + ".\n");
        }
        if(age >= 45 && age <= 54){
            if(imc_d < 22){
                buffer.append("Your IMC is " + imc_d + ", you are in low weight.\n");
            }
            else if(imc_d >= 22 && imc_d < 27){
                buffer.append("Your IMC is " + imc_d + ", you are in normal weight.\n");
            }
            else if(imc_d >= 27 && imc_d < 32){
                buffer.append("Your IMC is " + imc_d + ", you are in above weight.\n");
            }
            else if(imc_d >= 32 && imc_d < 42){
                buffer.append("Your IMC is " + imc_d + ", you are in obesity.\n");
            }
            else if(imc_d >= 42){
                buffer.append("Your IMC is " + imc_d + ", you are in serious obesity.\n");
            }
            else{
                return;
            }
            weightIdeal_min = height * height * 22;
            weightIdeal_max = height * height * 27;

            buffer.append("Your normal weight is between " + weightIdeal_min + " and " + weightIdeal_max + ".\n");
        }
        if(age >= 55 && age <= 64){
            if(imc_d < 23){
                buffer.append("Your IMC is " + imc_d + ", you are in low weight.\n");
            }
            else if(imc_d >= 23 && imc_d < 28){
                buffer.append("Your IMC is " + imc_d + ", you are in normal weight.\n");
            }
            else if(imc_d >= 28 && imc_d < 33){
                buffer.append("Your IMC is " + imc_d + ", you are in above weight.\n");
            }
            else if(imc_d >= 33 && imc_d < 43){
                buffer.append("Your IMC is " + imc_d + ", you are in obesity.\n");
            }
            else if(imc_d >= 43){
                buffer.append("Your IMC is " + imc_d + ", you are in serious obesity.\n");
            }
            else{
                return;
            }
            weightIdeal_min = height * height * 23;
            weightIdeal_max = height * height * 28;

            System.out.println("Your normal weight is between " + weightIdeal_min + " and " + weightIdeal_max + ".\n");
        }
        if(age >= 65){
            if(imc_d < 24){
                buffer.append("Your IMC is " + imc_d + ", you are in low weight.\n");
            }
            else if(imc_d >= 24 && imc_d < 29){
                buffer.append("Your IMC is " + imc_d + ", you are in normal weight.\n");
            }
            else if(imc_d >= 29 && imc_d < 34){
                buffer.append("Your IMC is " + imc_d + ", you are in above weight.\n");
            }
            else if(imc_d >= 34 && imc_d < 44){
                buffer.append("Your IMC is " + imc_d + ", you are in obesity.\n");
            }
            else if(imc_d >= 44){
                buffer.append("Your IMC is " + imc_d + ", you are in serious obesity.\n");
            }
            else{
                return;
            }
            weightIdeal_min = height * height * 24;
            weightIdeal_max = height * height * 29;

            buffer.append("Your normal weight is between " + weightIdeal_min + " and " + weightIdeal_max + ".\n");
        }
        showMessage2("IMC", buffer.toString());
    }
    public void ViewAll(){
        btViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        StringBuffer buffer = new StringBuffer();
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                buffer.append("Name: " + document.getString("Name") + "\n");
                                buffer.append("Date: " + document.getString("Date") + "\n");
                                buffer.append("Age: " + document.get("Age").toString() + "\n");
                                buffer.append("Height: " + document.get("Height").toString() + "\n");
                                buffer.append("Weight: " + document.get("Weight").toString() + "\n");
                                buffer.append("IMC: " + document.get("IMC").toString() + "\n\n");
                            }
                        }else{
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
