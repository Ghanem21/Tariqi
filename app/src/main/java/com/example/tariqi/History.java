package com.example.tariqi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Adapter;

import java.util.ArrayList;
import java.util.List;

public class History extends AppCompatActivity {
    RecyclerView recyclerView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
 List<DataTrip>dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        //custm toolbar to make drawer over toolbar
        androidx.appcompat.widget.Toolbar toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.new_toolbar);
        setSupportActionBar(toolbar);
        //navigation slideable toggle button
        drawerLayout = findViewById(R.id.drawer);
        drawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(drawerToggle.onOptionsItemSelected(item))
            return true;
        return true;
    }
}