package com.example.imc;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

import static com.google.common.base.Strings.isNullOrEmpty;

public class Delete extends AppCompatActivity {
    private EditText editDate;
    private Button btSubmit;
    private CollectionReference db = FirebaseFirestore.getInstance().collection("user");


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);

        Toolbar topbar = findViewById(R.id.topBar);
        setSupportActionBar(topbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("IMC Calculator");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        editDate = findViewById(R.id.editDate);
        btSubmit = findViewById(R.id.btSubmit);

        Submit();
    }
    public void Submit(){
        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = editDate.getText().toString();
                if (isNullOrEmpty(date)) {
                    Toast.makeText(Delete.this, "No Data", Toast.LENGTH_LONG).show();
                    openActivity();
                    return;
                }
                db.document(date)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("MainActivity", "DocumentSnapshot successfully deleted!");
                                Toast.makeText(Delete.this, "Data deleted", Toast.LENGTH_LONG).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("MainActivity", "Error deleting document", e);
                                Toast.makeText(Delete.this, "Data not deleted", Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });
    }
    public void openActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
