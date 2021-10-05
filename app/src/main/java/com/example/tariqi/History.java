package com.example.tariqi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class History extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    RecyclerView recyclerView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
 List<DataTrip>dataList;

    private NavigationView navigationView;
    private Toolbar toolbar;
    private ListView listView;
    CircularImageView userImg;
    StorageReference reference;
    FirebaseAuth auth;
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

        reference = FirebaseStorage.getInstance().getReference();
        auth =FirebaseAuth.getInstance();
        StorageReference storage = reference.child(auth.getCurrentUser().getEmail() + "/profile.jpg");
        storage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                userImg = (CircularImageView) findViewById(R.id.menu_img);
                userImg.setBackground(null);
                //userImg.setImageURI(uri);
                Picasso.get().load(uri).placeholder(R.drawable.traveling).into(userImg);
                //Toast.makeText(getApplicationContext(), "uploaded", Toast.LENGTH_SHORT).show();
            }
        });

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

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
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
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_SHORT).show();
            }
        });
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
            case R.id.menu_set_profile_pic:
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,100);
        }
        return true;
    }
    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        // create a matrix for the manipulation
        Matrix matrix = new Matrix();

        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);

        // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);

        return resizedBitmap;
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
}