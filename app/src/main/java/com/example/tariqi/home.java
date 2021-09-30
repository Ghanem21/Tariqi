package com.example.tariqi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.ArrayList;

public class home extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ListView listView;
    private HomeAdapter adapter;
    private ImageButton add;
    String[] names , locations , dates , times , types;
    Trip[] trips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        add = (ImageButton) findViewById(R.id.home_add_btn);

        //custm toolbar to make drawer over toolbar
        androidx.appcompat.widget.Toolbar toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.new_toolbar);
        setSupportActionBar(toolbar);
        //navigation slideable toggle button
        drawerLayout = findViewById(R.id.drawer);
        drawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //create Listview
        listView = findViewById(R.id.home_listview);
        names = new String[]{"Trip 1" , "Trip 2" , "Trip 2"};
        locations = new String[]{"Alex" , "Cairo" , "Demo"};
        dates = new String[]{"10/12" , "1/10" , "15/10"};
        times = new String[]{"10:30" , "00:00" , "20:10"};
        types = new String[]{"one way" , "one way" , "round trip"};
        trips = new Trip[3];
        for (int i = 0;i < names.length;i++)
            trips[i] = new Trip(names[i],locations[i],dates[i],times[i],types[i]);
        adapter = new HomeAdapter(getApplicationContext(),R.layout.home_list_view_layout,trips);
        listView.setAdapter(adapter);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"it work",Toast.LENGTH_SHORT).show();
            }
        });
        registerForContextMenu(listView);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu,v,menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_list_view_menu,menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_add_note:
                Toast.makeText(getApplicationContext(), "Add note", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_cancel:
                Toast.makeText(getApplicationContext(), "Cancel", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_delete:
                Toast.makeText(getApplicationContext(), "Delete", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_edit:
                Toast.makeText(getApplicationContext(), "Edit", Toast.LENGTH_SHORT).show();
                return true;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
       if(drawerToggle.onOptionsItemSelected(item))
           return true;
        return true;
    }
}