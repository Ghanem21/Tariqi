package com.example.tariqi;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class MyAdabter extends RecyclerView.Adapter<MyAdabter.MyViewholder> {
    Context context;
    ArrayList<Trip> tripArrayList;
    int position;
    SharedPreferences sp;

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
        holder.start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HashMap<String,String> userMap= new HashMap<>();
                userMap.put("name",trip.getName());
                userMap.put("start",trip.getLocation());
                userMap.put("date",trip.getDate());
                userMap.put("time",trip.getTime());
                userMap.put("type",trip.getType());


                sp= context.getSharedPreferences("UserPrefrence",Context.MODE_PRIVATE);
                String tripuid=sp.getString("uid","");

                FirebaseDatabase.getInstance().getReference("Users").child(tripuid).child("donetrip").child(trip.name).setValue(userMap);
            }
        });

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

            show_Note.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(itemView.getContext());
                    dialog.setTitle("Note");
                    dialog.setMessage("message test");
                    dialog.setIcon(R.drawable.sticky_notes);
                    dialog.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    dialog.create().show();
                }
            });
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
    public String getNote(int position){
        return tripArrayList.get(position).getName();
    }
}
