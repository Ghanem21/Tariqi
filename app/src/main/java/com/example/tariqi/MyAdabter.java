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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyAdabter extends RecyclerView.Adapter<MyAdabter.MyViewholder> {
    Context context;
    ArrayList<Trip> tripArrayList;
    int position;
    SharedPreferences sp;
    String tripuid;

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

        holder.tripname.setText(trip.getName());
        holder.location.setText(trip.getLocation());
        holder.date.setText(trip.getDate());
        holder.time.setText(trip.getTime());
        holder.typetrip.setText(trip.getType());
        holder.start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = holder.tripname.getText().toString();
                String location = holder.location.getText().toString();
                String date = holder.date.getText().toString();
                String time = holder.time.getText().toString();
                String type = holder.typetrip.getText().toString();

                HashMap<String,String> userMap= new HashMap<>();
                userMap.put("name",name);
                userMap.put("start",trip.getStartPoint());
                userMap.put("location",location);
                userMap.put("date",date);
                userMap.put("time",time);
                userMap.put("type",type);


                sp= context.getSharedPreferences("UserPrefrence",Context.MODE_PRIVATE);
                 tripuid=sp.getString("uid","");

                FirebaseDatabase.getInstance().getReference("Users").child(tripuid).child("donetrip").child(trip.name).setValue(userMap);


                FirebaseDatabase.getInstance().getReference().child("Users").child(tripuid).child("upcomingtrip")
                        .child(tripArrayList.get(position).getName()).removeValue();



                tripArrayList.remove(position);
                notifyItemRemoved(position);
            }

        });
        // update data in add trip page
        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogPlus dialogPlus=DialogPlus.newDialog(holder.tripname.getContext())
                        .setContentHolder(new ViewHolder(R.layout.update_data)).setExpanded(true,1700).create();

                View view=dialogPlus.getHolderView();
              EditText editTripName,editTriplocation,editTripStart,editTripTime,editTripDate,editTripType;
              Button btnUpdateTrip;
                editTripName=view.findViewById(R.id.edt_trip_name);
                editTriplocation=view.findViewById(R.id.edt_end_point);
                editTripStart=view.findViewById(R.id.edt_start_point);
                editTripDate=view.findViewById(R.id.addtrip_tv_date);
                editTripTime=view.findViewById(R.id.addtrip_tv_time);
                editTripType=view.findViewById(R.id.radia_id1);
                btnUpdateTrip=view.findViewById(R.id.btn_update_trip);

                editTripName.setText(trip.getName());
                editTriplocation.setText(trip.getLocation());
                editTripDate.setText(trip.getDate());
                editTripTime.setText(trip.getTime());
                editTripStart.setText(trip.getStartPoint());
                editTripType.setText(trip.getType());

                btnUpdateTrip.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String,Object> map= new HashMap<>();
                        map.put("name",editTripName.getText().toString());
                        map.put("start",editTripStart.getText().toString());
                        map.put("end",editTriplocation.getText().toString());
                        map.put("date",editTripDate.getText().toString());
                        map.put("time",editTripTime.getText().toString());
                        map.put("type",editTripType.getText().toString());

                        sp= context.getSharedPreferences("UserPrefrence",Context.MODE_PRIVATE);
                        tripuid=sp.getString("uid","");
                        FirebaseDatabase.getInstance().getReference().child("Users").child(tripuid).child("upcomingtrip")
                                .child(tripArrayList.get(position).getName()).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(holder.tripname.getContext(), "success", Toast.LENGTH_SHORT).show();
                                dialogPlus.dismiss();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(holder.tripname.getContext(), "faild", Toast.LENGTH_SHORT).show();
                                dialogPlus.dismiss();
                            }
                        });
                    }
                });
                dialogPlus.show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return tripArrayList.size();
    }



    public static class MyViewholder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{

        TextView tripname,location,date,time,typetrip;
        Button start,update;
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
            update=itemView.findViewById(R.id.btn_edit);
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
