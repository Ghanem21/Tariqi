package com.example.tariqi;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdabter extends RecyclerView.Adapter<MyAdabter.MyViewholder> {
    Context context;
    ArrayList<Trip> tripArrayList;

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



    }

    @Override
    public int getItemCount() {
        return tripArrayList.size();
    }
    public static class MyViewholder extends RecyclerView.ViewHolder{

        TextView tripname,location,date,time,typetrip;
        ImageButton show_Note;

        public MyViewholder(@NonNull View itemView) {
            super(itemView);
            tripname = itemView.findViewById(R.id.tv_trip_name);
            location = itemView.findViewById(R.id.tv_location);
            date = itemView.findViewById(R.id.tv_date);
            time = itemView.findViewById(R.id.tv_time);
            typetrip = itemView.findViewById(R.id.tv_trip_type);
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





        }
    }
}
