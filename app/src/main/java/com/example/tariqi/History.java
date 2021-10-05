package com.example.tariqi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class History extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    RecyclerView recyclerView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    ArrayList <Trip>tripArrayList;
    AdapterHistoy adapter;
Context context;

    private NavigationView navigationView;
    private Toolbar toolbar;
    private ListView listView;
    private FirebaseDatabase FD=FirebaseDatabase.getInstance();
    private DatabaseReference DR;
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        //custm toolbar to make drawer over toolbar
        toolbar = findViewById(R.id.new_toolbar);
        setSupportActionBar(toolbar);
        //navigation slideable toggle button
        navigationView = findViewById(R.id.home_navigation_drawer);
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        drawerLayout = findViewById(R.id.drawer);
        drawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        recyclerView=findViewById(R.id.recycleID);
        tripArrayList= new ArrayList<Trip>();

        AdapterHistoy adapterHistoy= new AdapterHistoy(tripArrayList,this);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.setAdapter(adapterHistoy);


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
                adapterHistoy.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(drawerToggle.onOptionsItemSelected(item))
            return true;
        return true;
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_home:
                startActivity(new Intent(getApplicationContext(), Home.class));
                finish();
                break;
            case R.id.menu_history:
                Toast.makeText(getApplicationContext(), "We are in History", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }
}