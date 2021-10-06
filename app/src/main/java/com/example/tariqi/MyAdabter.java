package com.example.tariqi;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MyAdabter extends RecyclerView.Adapter<MyAdabter.MyViewholder> {
    Context context;
    ArrayList<Trip> tripArrayList;
    int position;
    SharedPreferences sp;
    String startPoint;
    public void setStartPoint(String startPoint) {
        this.startPoint = startPoint;
    }

    public MyAdabter(Context context, ArrayList<Trip> tripArrayList) {
        this.context = context;
        this.tripArrayList = tripArrayList;
    }

    @NonNull
    @Override
    public MyAdabter.MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.home_list_view_layout,parent,false);
        v.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

            }
        });

        return new MyViewholder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull MyAdabter.MyViewholder holder, int position) {

        Trip trip =tripArrayList.get(position);

        holder.tripname.setText(trip.name);
        holder.location.setText(trip.location);
        holder.date.setText(trip.date);
        holder.time.setText(trip.time);
        holder.typetrip.setText(trip.type);
        holder.show_Note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(holder.itemView.getContext());
                dialog.setTitle("Note");
                dialog.setMessage(tripArrayList.get(position).getNote());
                dialog.setIcon(R.drawable.sticky_notes);
                dialog.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.create().show();
            }
        });
        holder.start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat simpleTimeFormat =new SimpleDateFormat("HH:mm:ss", Locale.US);
                trip.setTime(simpleTimeFormat.format(new Date()));

                SimpleDateFormat simpleDateFormat =new SimpleDateFormat("MM/dd/yyyy", Locale.US);
                trip.setDate(simpleDateFormat.format(new Date()));

                HashMap<String,String> userMap= new HashMap<>();
                userMap.put("name",trip.getName());
                userMap.put("end",trip.getLocation());
                userMap.put("date",trip.getDate());
                userMap.put("time",trip.getTime());
                userMap.put("type",trip.getType());


                sp= context.getSharedPreferences("UserPrefrence",Context.MODE_PRIVATE);
                String tripuid=sp.getString("uid","");

                FirebaseDatabase.getInstance().getReference("Users").child(tripuid).child("donetrip").child(trip.name).setValue(userMap);
                displayMap();
                notifyDataSetChanged();
                removeItem(position);
            }
        });

    }

    private void displayMap() {
        Geocoder geocoder = new Geocoder(context);
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocationName("cairo",5);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Uri uri = Uri.parse("https://www.google.co.in/maps/dir/" + startPoint +
                    "/" + addresses.get(0).getAddressLine(0));
            Intent intent = new Intent(Intent.ACTION_VIEW,uri);
            intent.setPackage("com.google.android.apps.maps");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);

    }
    @Override
    public int getItemCount() {
        return tripArrayList.size();
    }



    public static class MyViewholder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{

        TextView tripname,location,date,time,typetrip;
        Button start;
        ImageButton show_Note;

        public MyViewholder(@NonNull View itemView) {
            super(itemView);
            tripname = itemView.findViewById(R.id.tv_trip_name);
            location = itemView.findViewById(R.id.tv_location);
            date = itemView.findViewById(R.id.tv_date);
            time = itemView.findViewById(R.id.tv_time);
            typetrip = itemView.findViewById(R.id.tv_trip_type);
            start=itemView.findViewById(R.id.btn_start);
            show_Note = itemView.findViewById(R.id.imageButton_note);


            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select an option");
            menu.add(this.getAdapterPosition(), 120,0,"Add Note");
            menu.add(this.getAdapterPosition(), 121,1,"Edit");
            menu.add(this.getAdapterPosition(), 122,2,"Delete");
            menu.add(this.getAdapterPosition(), 123,3,"Cancel");
        }
    }
    public void removeItem(int position){
        tripArrayList.remove(position);
        notifyDataSetChanged();
    }
    public void setNot(String note,int position){
        String str;
        if(tripArrayList.get(position).getNote() != null) {
            str = tripArrayList.get(position).getNote() + "\n" + note;
        }else {
            str = note;
        }
        tripArrayList.get(position).setNote(str);
    }
}
