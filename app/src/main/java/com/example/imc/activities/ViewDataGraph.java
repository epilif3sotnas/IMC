package com.example.imc.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.imc.R;
import com.example.imc.objects.objData;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class ViewDataGraph extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private LineChart graph;
    FirebaseUser userCurrent = FirebaseAuth.getInstance().getCurrentUser();
    private CollectionReference db = FirebaseFirestore.getInstance().collection(Objects.requireNonNull(userCurrent).getUid());
    private ArrayList<objData> data;
    private Spinner options;
    private Button btdataByYear;
    private EditText year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_data_graph);

        Toolbar topbar = findViewById(R.id.topBar);
        setSupportActionBar(topbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("IMC Calculator");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        graph = findViewById(R.id.graph);
        btdataByYear = findViewById(R.id.btYear);
        year = findViewById(R.id.year);
        options = findViewById(R.id.options);
        options.setOnItemSelectedListener(this);

        loadDataSpinner();
        Buttons();
    }
    public void loadDataSpinner(){
        String[] opts = {"This Month", "Last Month", "Last three Months", "Last six Months", "Last year"};

        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, opts);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        options.setAdapter(aa);
    }
    public void loadDataGraph(final String date, final int op){
        db.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                Calendar calendar1 = Calendar.getInstance();
                Calendar calendar2 = Calendar.getInstance();
                try {
                    calendar1.setTime(Objects.requireNonNull(new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(date)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                data = new ArrayList<>();
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                        try {
                            calendar2.setTime(Objects.requireNonNull(new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(Objects.requireNonNull(document.get("Date")).toString())));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        switch (op){
                            case 1:
                                    if (calendar1.get(Calendar.MONTH ) == calendar2.get(Calendar.MONTH)){
                                        objData obj = new objData(Objects.requireNonNull(document.get("Date")).toString(),
                                                Objects.requireNonNull(document.get("Age")).toString(),
                                                Objects.requireNonNull(document.get("Height")).toString(),
                                                Objects.requireNonNull(document.get("Weight")).toString(),
                                                Objects.requireNonNull(document.get("IMC")).toString());
                                        data.add(obj);
                                    }
                                break;
                            case 2:
                                    if(calendar2.get(Calendar.YEAR) == calendar1.get(Calendar.YEAR) &&
                                            calendar1.get(Calendar.DAY_OF_YEAR) - calendar2.get(Calendar.DAY_OF_YEAR) <= 30){
                                            objData obj = new objData(Objects.requireNonNull(document.get("Date")).toString(),
                                                    Objects.requireNonNull(document.get("Age")).toString(),
                                                    Objects.requireNonNull(document.get("Height")).toString(),
                                                    Objects.requireNonNull(document.get("Weight")).toString(),
                                                    Objects.requireNonNull(document.get("IMC")).toString());
                                            data.add(obj);
                                    }else{
                                        int diffYear = calendar1.get(Calendar.YEAR) - calendar2.get(Calendar.YEAR);
                                        if ((calendar1.get(Calendar.DAY_OF_YEAR) + (365 * diffYear)) - calendar2.get(Calendar.DAY_OF_YEAR) <= 30){
                                            objData obj = new objData(Objects.requireNonNull(document.get("Date")).toString(),
                                                    Objects.requireNonNull(document.get("Age")).toString(),
                                                    Objects.requireNonNull(document.get("Height")).toString(),
                                                    Objects.requireNonNull(document.get("Weight")).toString(),
                                                    Objects.requireNonNull(document.get("IMC")).toString());
                                            data.add(obj);
                                        }
                                    }
                                break;
                            case 3:
                                if(calendar2.get(Calendar.YEAR) == calendar1.get(Calendar.YEAR) &&
                                        calendar1.get(Calendar.DAY_OF_YEAR) - calendar2.get(Calendar.DAY_OF_YEAR) <= 90){
                                    objData obj = new objData(Objects.requireNonNull(document.get("Date")).toString(),
                                            Objects.requireNonNull(document.get("Age")).toString(),
                                            Objects.requireNonNull(document.get("Height")).toString(),
                                            Objects.requireNonNull(document.get("Weight")).toString(),
                                            Objects.requireNonNull(document.get("IMC")).toString());
                                    data.add(obj);
                                }else{
                                    int diffYear = calendar1.get(Calendar.YEAR) - calendar2.get(Calendar.YEAR);
                                    if ((calendar1.get(Calendar.DAY_OF_YEAR) + (365 * diffYear)) - calendar2.get(Calendar.DAY_OF_YEAR) <= 90){
                                        objData obj = new objData(Objects.requireNonNull(document.get("Date")).toString(),
                                                Objects.requireNonNull(document.get("Age")).toString(),
                                                Objects.requireNonNull(document.get("Height")).toString(),
                                                Objects.requireNonNull(document.get("Weight")).toString(),
                                                Objects.requireNonNull(document.get("IMC")).toString());
                                        data.add(obj);
                                    }
                                }
                                break;
                            case 4:
                                if(calendar2.get(Calendar.YEAR) == calendar1.get(Calendar.YEAR) &&
                                        calendar1.get(Calendar.DAY_OF_YEAR) - calendar2.get(Calendar.DAY_OF_YEAR) <= 180){
                                    objData obj = new objData(Objects.requireNonNull(document.get("Date")).toString(),
                                            Objects.requireNonNull(document.get("Age")).toString(),
                                            Objects.requireNonNull(document.get("Height")).toString(),
                                            Objects.requireNonNull(document.get("Weight")).toString(),
                                            Objects.requireNonNull(document.get("IMC")).toString());
                                    data.add(obj);
                                }else{
                                    int diffYear = calendar1.get(Calendar.YEAR) - calendar2.get(Calendar.YEAR);
                                    if ((calendar1.get(Calendar.DAY_OF_YEAR) + (365 * diffYear)) - calendar2.get(Calendar.DAY_OF_YEAR) <= 180){
                                        objData obj = new objData(Objects.requireNonNull(document.get("Date")).toString(),
                                                Objects.requireNonNull(document.get("Age")).toString(),
                                                Objects.requireNonNull(document.get("Height")).toString(),
                                                Objects.requireNonNull(document.get("Weight")).toString(),
                                                Objects.requireNonNull(document.get("IMC")).toString());
                                        data.add(obj);
                                    }
                                }
                                break;
                            case 5:
                                if(calendar2.get(Calendar.YEAR) == calendar1.get(Calendar.YEAR)){
                                    objData obj = new objData(Objects.requireNonNull(document.get("Date")).toString(),
                                            Objects.requireNonNull(document.get("Age")).toString(),
                                            Objects.requireNonNull(document.get("Height")).toString(),
                                            Objects.requireNonNull(document.get("Weight")).toString(),
                                            Objects.requireNonNull(document.get("IMC")).toString());
                                    data.add(obj);
                                }else{
                                    int diffYear = calendar1.get(Calendar.YEAR) - calendar2.get(Calendar.YEAR);
                                    if ((calendar1.get(Calendar.DAY_OF_YEAR) + (365 * diffYear)) - calendar2.get(Calendar.DAY_OF_YEAR) <= 365){
                                        objData obj = new objData(Objects.requireNonNull(document.get("Date")).toString(),
                                                Objects.requireNonNull(document.get("Age")).toString(),
                                                Objects.requireNonNull(document.get("Height")).toString(),
                                                Objects.requireNonNull(document.get("Weight")).toString(),
                                                Objects.requireNonNull(document.get("IMC")).toString());
                                        data.add(obj);
                                    }
                                }
                                break;
                            case 6:
                                if(calendar2.get(Calendar.YEAR) == calendar1.get(Calendar.YEAR)){
                                    objData obj = new objData(Objects.requireNonNull(document.get("Date")).toString(),
                                            Objects.requireNonNull(document.get("Age")).toString(),
                                            Objects.requireNonNull(document.get("Height")).toString(),
                                            Objects.requireNonNull(document.get("Weight")).toString(),
                                            Objects.requireNonNull(document.get("IMC")).toString());
                                    data.add(obj);
                                }
                                break;
                        }
                    }
                }
                if(data.size() == 0){
                    Toast.makeText(ViewDataGraph.this, "No data available", Toast.LENGTH_LONG).show();
                }
                if (!data.isEmpty()){
                    createGraph();
                }
            }
        });
    }
    public void createGraph(){
        ArrayList<Entry> entries = new ArrayList<>();
        final ArrayList<String> dates = new ArrayList<>();
        int size = data.size();
        for (int j = 0; j < size; j++){
            dates.add(data.get(j).getDate());
            Entry entry = new Entry(j, Float.parseFloat(data.get(j).getWeight()));
            entries.add(entry);
        }
        LineDataSet lineDataSet = new LineDataSet(entries, "Weight");
        lineDataSet.setColor(Color.parseColor("#000000"));
        lineDataSet.setValueTextSize(0);
        lineDataSet.setLineWidth(6);
        lineDataSet.setCircleColor(Color.parseColor("#FF0000"));
        lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);

        XAxis xAxis = graph.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(10f);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(dates));
        xAxis.setXOffset(50f);
        xAxis.setLabelRotationAngle(-25f);

        List<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet);
        LineData data = new LineData(dataSets);
        graph.setData(data);
        graph.getDescription().setText("Your Weight");
        graph.getDescription().setTextSize(14);
        graph.invalidate();
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        String currentDate = format.format(Calendar.getInstance().getTime());
            if(parent.getItemAtPosition(position).toString().equals("Last Month")){
                loadDataGraph(currentDate, 2);
            }else if(parent.getItemAtPosition(position).toString().equals("Last three Months")){
                loadDataGraph(currentDate, 3);
            }else if (parent.getItemAtPosition(position).toString().equals("Last six Months")){
                loadDataGraph(currentDate, 4);
            }else if (parent.getItemAtPosition(position).toString().equals("Last Year")){
                loadDataGraph(currentDate, 5);
            }else if (parent.getItemAtPosition(position).toString().equals("This Month")){
                loadDataGraph(currentDate, 1);
            }
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(getApplicationContext(), "Select an option", Toast.LENGTH_LONG).show();
    }
    public void Buttons(){
        btdataByYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btdataByYear.getText().toString().equals("Show by year")){
                    options.setVisibility(View.GONE);
                    year.setVisibility(View.VISIBLE);
                    btdataByYear.setText("Submit");
                }else{
                    loadDataGraph(year.getText().toString() + "-01-01", 6);
                    options.setVisibility(View.VISIBLE);
                    year.setVisibility(View.GONE);
                    btdataByYear.setText("Show by year");
                }
            }
        });
    }
}
