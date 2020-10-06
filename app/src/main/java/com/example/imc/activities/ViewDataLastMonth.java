package com.example.imc.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import com.example.imc.R;
import com.example.imc.objects.objData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;
import java.util.Objects;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class ViewDataLastMonth extends AppCompatActivity {
    private ArrayList<objData> data = new ArrayList<>();
    private TextView weightLost, weightMax, weightMin, weightFluctuation;
    FirebaseUser userCurrent = FirebaseAuth.getInstance().getCurrentUser();
    private CollectionReference db = FirebaseFirestore.getInstance().collection(Objects.requireNonNull(userCurrent).getUid());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_data_last_month);

        weightLost = findViewById(R.id.weightLOST);
        weightMax = findViewById(R.id.weightMAX);
        weightMin = findViewById(R.id.weightMIN);
        weightFluctuation = findViewById(R.id.weightFluctuation);

        Toolbar topbar = findViewById(R.id.topBar);
        setSupportActionBar(topbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("IMC Calculator");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadData();

    }
    public void loadData(){
        db.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                    Calendar calendar1 = Calendar.getInstance();
                    Calendar calendar2 = Calendar.getInstance();
                    try {
                        calendar1.setTime(Objects.requireNonNull(new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(format.format(Calendar.getInstance().getTime()))));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                        try {
                            calendar2.setTime(Objects.requireNonNull(new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(Objects.requireNonNull(document.get("Date")).toString())));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if (calendar1.get(Calendar.MONTH) == Calendar.JANUARY && calendar2.get(Calendar.MONTH) == Calendar.DECEMBER){
                            objData obj = new objData(Objects.requireNonNull(document.get("Date")).toString(),
                                    Objects.requireNonNull(document.get("Weight")).toString(),
                                    Objects.requireNonNull(document.get("IMC")).toString());
                            data.add(obj);
                        }else if((calendar1.get(Calendar.MONTH) - 1) == calendar2.get(Calendar.MONTH)){
                            objData obj = new objData(Objects.requireNonNull(document.get("Date")).toString(),
                                    Objects.requireNonNull(document.get("Weight")).toString(),
                                    Objects.requireNonNull(document.get("IMC")).toString());
                            data.add(obj);
                        }
                    }
                }
                loadTextViews();
            }
        });
    }
    public void loadTextViews(){
        if (data.size() == 0){
            weightMin.setText("No data available");
            weightLost.setText("No data available");
            weightMax.setText("No data available");
        }else{
            weightLost.setText(String.format(Locale.ENGLISH, "Weight Lost: %.2f kg", Math.abs(Double.parseDouble(data.get(data.size() - 1).getWeight())
                    - Double.parseDouble(data.get(0).getWeight()))));
            sortArray();
            weightMax.setText(MessageFormat.format("Highest weight: {0}", data.get(0).getWeight()));
            weightMin.setText(MessageFormat.format("Lowest weight: {0}", data.get(data.size() - 1).getWeight()));
            weightFluctuation.setText(String.format(Locale.ENGLISH, "Weight Fluctuation: %.2f kg", Double.parseDouble(data.get(0).getWeight())
                    - Double.parseDouble(data.get(data.size() - 1).getWeight())));
        }
    }
    public void sortArray(){
        Collections.sort(data, new Comparator<objData>() {
            @Override
            public int compare(objData o1, objData o2) {
                return o2.getWeight().compareTo(o1.getWeight());
            }
        });
    }
}