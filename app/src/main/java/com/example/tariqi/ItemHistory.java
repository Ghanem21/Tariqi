package com.example.tariqi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ItemHistory extends AppCompatActivity {
    TextView tripname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_history);
        tripname=findViewById(R.id.tripnamehistory);
        // get data
        Intent intentItem= getIntent();
        String trip=intentItem.getExtras().getString("tripname");
        tripname.setText(trip);




    }
}