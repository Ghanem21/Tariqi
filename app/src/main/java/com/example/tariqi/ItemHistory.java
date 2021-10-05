package com.example.tariqi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ItemHistory extends AppCompatActivity {
    ImageButton back;
    private FirebaseDatabase FD=FirebaseDatabase.getInstance();
    private DatabaseReference DR;
    SharedPreferences sp;
    String tripName,start,end,date,note,type,time;
    RecyclerView recyclerView;
    itemAdapter adapter;
    ArrayList <Trip>tripArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_history);

        recyclerView=findViewById(R.id.recycleViewHistory);
        tripArrayList= new ArrayList<Trip>();

        itemAdapter itemAdapter=  new itemAdapter(tripArrayList);
        recyclerView.setLayoutManager(new GridLayoutManager(this,1));
        recyclerView.setAdapter(itemAdapter);

        sp=getApplicationContext().getSharedPreferences("UserPrefrence", Context.MODE_PRIVATE);
        String tripuid=sp.getString("uid","");
        DR=FD.getReference().child("Users").child(tripuid).child("donetrip");
        DR.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Trip trip=dataSnapshot.getValue(Trip.class);
                    tripArrayList.add(trip);
                }
                itemAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        back = findViewById(R.id.back_to_history);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),History.class);
                startActivity(intent);
            }
        });



    }
}