package com.example.tariqi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Adapter;

import java.util.ArrayList;
import java.util.List;

public class History extends AppCompatActivity {
    RecyclerView recyclerView;

 List<DataTrip>dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        dataList= new ArrayList<>();
        dataList.add(new DataTrip("alexandria"));
        dataList.add(new DataTrip("paris"));
        dataList.add(new DataTrip("Marsa Matrouh"));
        dataList.add(new DataTrip("Pyramids"));
        dataList.add(new DataTrip("Italy"));
        dataList.add(new DataTrip("Mansoura"));
        dataList.add(new DataTrip("Pyramids"));
        dataList.add(new DataTrip("Italy"));
        dataList.add(new DataTrip("Mansoura"));

        recyclerView=findViewById(R.id.recycleID);
        //AdapterHistoy adapterHistoy= new AdapterHistoy(this,dataList);
        AdapterHistoy adapterHistoy= new AdapterHistoy(dataList,this);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.setAdapter(adapterHistoy);
        adapterHistoy.notifyDataSetChanged();
    }
    /*@Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }*/
}