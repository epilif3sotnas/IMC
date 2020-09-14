package com.example.imc.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import com.example.imc.R;
import com.example.imc.objects.objData;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class ViewDataGraph extends AppCompatActivity {
    private LineChart graph;
    FirebaseUser userCurrent = FirebaseAuth.getInstance().getCurrentUser();
    private CollectionReference db = FirebaseFirestore.getInstance().collection(Objects.requireNonNull(userCurrent).getUid());
    private ArrayList<objData> data;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_data_graph);

        Toolbar topbar = findViewById(R.id.topBar);
        setSupportActionBar(topbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("IMC Calculator");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        graph = findViewById(R.id.graph);

        loadData();
    }
    public void loadData(){
        db.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                data = new ArrayList<>();
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                        objData obj = new objData(Objects.requireNonNull(document.get("Date")).toString(),
                                Objects.requireNonNull(document.get("Age")).toString(),
                                Objects.requireNonNull(document.get("Height")).toString(),
                                Objects.requireNonNull(document.get("Weight")).toString(),
                                Objects.requireNonNull(document.get("IMC")).toString());
                        data.add(obj);
                    }
                }
                if (!data.isEmpty()){
                    createGraph();
                }
            }
        });
    }
    public void createGraph(){
        ArrayList<Entry> entries = new ArrayList<>();
        int size = data.size();
        for (int j = 0; j < size; j++){
            Entry entry = new Entry(j, Float.parseFloat(data.get(j).getWeight()));
            entries.add(entry);
        }
        LineDataSet lineDataSet = new LineDataSet(entries, "Weight");
        lineDataSet.setColor(Color.parseColor("#000000"));
        lineDataSet.setValueTextSize(0);
        lineDataSet.setLineWidth(6);
        lineDataSet.setCircleColor(Color.parseColor("#FF0000"));
        lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);

        List<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet);
        LineData data = new LineData(dataSets);
        graph.setData(data);
        graph.invalidate();
    }
}