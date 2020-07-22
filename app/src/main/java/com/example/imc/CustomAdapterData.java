package com.example.imc;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class CustomAdapterData extends BaseAdapter implements ListAdapter {
    private ArrayList<objData> list;
    private Context context;
    private CollectionReference db = FirebaseFirestore.getInstance().collection("user");

    private static class Data{
        TextView date;
        TextView age;
        TextView height;
        TextView weight;
        TextView imc;
    }
    public CustomAdapterData(ArrayList<objData> list, Context context){
        this.list = list;
        this.context = context;
    }
    @Override
    public int getCount() {
        return list.size();
    }
    @Override
    public Object getItem(int position) {
        return list.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @SuppressLint("InflateParams")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Data data = new Data();
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert inflater != null;
            convertView = inflater.inflate(R.layout.list_data, parent, false);
            convertView.setTag(data);
        }

        data.date = convertView.findViewById(R.id.date);
        data.age = convertView.findViewById(R.id.age);
        data.height = convertView.findViewById(R.id.height);
        data.weight = convertView.findViewById(R.id.weight);
        data.imc = convertView.findViewById(R.id.imc);

        data.date.setText(list.get(position).getDate());
        data.age.setText(list.get(position).getAge());
        data.height.setText(list.get(position).getHeight());
        data.weight.setText(list.get(position).getWeight());
        data.imc.setText(list.get(position).getImc());

        Button btDel = convertView.findViewById(R.id.btDelete);

        btDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.document(list.get(position).getDate())
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("MainActivity", "DocumentSnapshot successfully deleted!");
                                Toast.makeText(context, "Data deleted", Toast.LENGTH_LONG).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("MainActivity", "Error deleting document", e);
                                Toast.makeText(context, "Data not deleted", Toast.LENGTH_LONG).show();
                            }
                        });
                list.remove(list.get(position));
                int pos = position;
                if (position == list.size()){
                    pos = position - 1;
                }
                data.date.setText(list.get(pos).getDate());
                data.age.setText(list.get(pos).getAge());
                data.height.setText(list.get(pos).getHeight());
                data.weight.setText(list.get(pos).getWeight());
                data.imc.setText(list.get(pos).getImc());
                Toast.makeText(context, "Data deleted", Toast.LENGTH_LONG).show();
            }
        });
        return convertView;
    }
}
