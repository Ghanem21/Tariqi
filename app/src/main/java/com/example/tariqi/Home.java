package com.example.tariqi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

public class Home extends AppCompatActivity implements OnNavigationItemSelectedListener ,DialogFragment.PositiveClickListener{
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ImageButton add ;
    private NavigationView navigationView;
    private Toolbar toolbar;
    RecyclerView recyclerView;
    ArrayList<Trip> tripArrayList;
    MyAdabter adapter ;
    FirebaseFirestore db;
    ProgressDialog progressDialog;
    CircularImageView userImg;
    StorageReference reference;
    FirebaseAuth auth;
    AlertDialog.Builder builder;
    private FirebaseDatabase FD=FirebaseDatabase.getInstance();
    private DatabaseReference DR;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

//        progressDialog = new ProgressDialog(this);
//        progressDialog.setCancelable(true);
//        progressDialog.setMessage("fetching Data ...");
//        progressDialog.show();

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        tripArrayList = new ArrayList<Trip>();
        adapter = new MyAdabter(Home.this,tripArrayList);

        recyclerView.setAdapter(adapter);

        sp=getApplicationContext().getSharedPreferences("UserPrefrence", Context.MODE_PRIVATE);
        String tripuid=sp.getString("uid","");

        DR=FD.getReference().child("Users").child(tripuid).child("upcomingtrip");

        // add trip from realtime database
        DR.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Trip trip=dataSnapshot.getValue(Trip.class);
                    tripArrayList.add(trip);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

      //  EventChangeListener();

        add = (ImageButton) findViewById(R.id.home_add_btn);

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

        reference = FirebaseStorage.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        StorageReference storage = reference.child(auth.getCurrentUser().getEmail() + "/profile.jpg");
        storage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                userImg = (CircularImageView) findViewById(R.id.menu_img);
                userImg.setBackground(null);
                Picasso.get().load(uri).placeholder(R.drawable.traveling).into(userImg);
                //userImg.setImageURI(uri);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),Add_Trip.class);
                startActivity(i);
            }
        });

        registerForContextMenu(recyclerView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100 && resultCode == Activity.RESULT_OK)
        {
            Uri uri = data.getData();
            userImg = (CircularImageView) findViewById(R.id.menu_img);
            userImg.setBackground(null);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                Bitmap b = Bitmap.createScaledBitmap(bitmap, 500, 500, false);
                uri = getImageUri(getApplicationContext(),b);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //userImg.setImageURI(uri);
            upLoadImage(uri);
        }
    }

    private void upLoadImage(Uri uri) {
        StorageReference storage = reference.child(auth.getCurrentUser().getEmail() + "/profile.jpg");
        storage.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Picasso.get().load(uri).placeholder(R.drawable.traveling).into(userImg);
                Toast.makeText(getApplicationContext(), "upload done", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void EventChangeListener() {
        db.collection("trip")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null){

                            if (progressDialog.isShowing())
                                progressDialog.dismiss();
                            Log.e("firebase Error",error.getMessage());
                            return;
                        }
                        for (DocumentChange dc :value.getDocumentChanges()){
                            if(dc.getType() == DocumentChange.Type.ADDED){
                               tripArrayList.add(dc.getDocument().toObject(Trip.class));

                            }
                            adapter.notifyDataSetChanged();
                            if (progressDialog.isShowing())
                                progressDialog.dismiss();
                        }
                    }
                });
    }

    /*@Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu,v,menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_list_view_menu,menu);
    }*/

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case 120:
                DialogFragment dialogFragment = new DialogFragment(item.getGroupId());
                dialogFragment.show(getSupportFragmentManager(),null);
                return true;
            case 121:
                Toast.makeText(getApplicationContext(), "Cancel", Toast.LENGTH_SHORT).show();
                return true;
            case 122:
                adapter.removeItem(item.getGroupId());
                return true;
            case 123:
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
                Toast.makeText(getApplicationContext(), "We are in Home", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_history:
                startActivity(new Intent(getApplicationContext(), History.class));
                finish();
                break;
            case R.id.menu_set_profile_pic:
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,100);
                break;
            case R.id.menu_log_out:
                builder =new AlertDialog.Builder(this);
                builder.setTitle("Log Out")
                        .setMessage("Are you sure to log out ?")
                        .setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefsFile",0);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.remove("hasLoggedIn");
                                editor.commit();
                                Intent i = new Intent(Home.this,SignInActivity.class);
                                startActivity(i);
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).show();
                break;


        }
        return true;
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    @Override
    public void onPositiveButtonCliced(String note,int position) {
        String str =  adapter.getNote(position);
        Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
    }
}