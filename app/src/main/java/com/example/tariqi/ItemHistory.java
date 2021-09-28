package com.example.tariqi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ItemHistory extends AppCompatActivity {
    TextView tripname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_history);
        tripname=findViewById(R.id.tripnamehistory);



    }
}