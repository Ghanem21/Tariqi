package com.example.tariqi;

import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Locale;

public class MyAdabter extends RecyclerView.Adapter<MyAdabter.MyViewholder> {
    private static final int REQUEST_CODE_ALARM = 8888;
    Context context;
    ArrayList<Trip> tripArrayList;
    int position;
    SharedPreferences sp;
    String startPoint;
    String str;
    Trip trip;

    public void setStartPoint(String startPoint) {
        this.startPoint = startPoint;
    }
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

        // setAlarm(tripArayList.get(position).getCal());
        holder.start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat simpleTimeFormat =new SimpleDateFormat("HH:mm:ss", Locale.US);
                String newTime=simpleTimeFormat.format(new Date());
                trip.setTime(newTime);

                SimpleDateFormat simpleDateFormat =new SimpleDateFormat("MM/dd/yyyy", Locale.US);
                String newDate=simpleDateFormat.format(new Date());
                trip.setDate(newDate);

                String name = holder.tripname.getText().toString();
                String location = holder.location.getText().toString();
                String date = holder.date.getText().toString();
                String time = holder.time.getText().toString();
                String type = holder.typetrip.getText().toString();
                long timecalender= tripArrayList.get(position).getCal();

                HashMap<String,String> userMap= new HashMap<>();
                userMap.put("name",name);
                userMap.put("location",location);
                userMap.put("date",date);
                userMap.put("time",time);
                userMap.put("type",type);
                userMap.put("note",tripArrayList.get(position).getNote());
                userMap.put("calender",timecalender+"");
                userMap.put("trip_cancle","");



                sp= context.getSharedPreferences("UserPrefrence",Context.MODE_PRIVATE);
                tripuid=sp.getString("uid","");

                FirebaseDatabase.getInstance().getReference().child("Users").child(tripuid).child("upcomingtrip")
                        .child(tripArrayList.get(position).getName()).child("time").setValue(newTime);
                FirebaseDatabase.getInstance().getReference().child("Users").child(tripuid).child("upcomingtrip")
                        .child(tripArrayList.get(position).getName()).child("date").setValue(newDate);

                FirebaseDatabase.getInstance().getReference("Users").child(tripuid).child("donetrip").child(trip.name).setValue(userMap);
                displayMap();
                notifyDataSetChanged();
               // removeItem(position);


                FirebaseDatabase.getInstance().getReference().child("Users").child(tripuid).child("upcomingtrip")
                        .child(tripArrayList.get(position).getName()).removeValue();



                tripArrayList.remove(position);
                notifyItemRemoved(position);
            }
        });


    }
    public void setAlarm(long time){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        Intent intent = new Intent(context,AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,888,intent,0);
        if (time <Calendar.getInstance().getTimeInMillis()){

        }else {
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, time,pendingIntent);}
    }

    private void displayMap() {
        Geocoder geocoder = new Geocoder(context);
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocationName(tripArrayList.get(position).getLocation(), 5);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Uri uri = Uri.parse("https://www.google.co.in/maps/dir/" + startPoint +
                "/" + addresses.get(0).getAddressLine(0));
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
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

        if(tripArrayList.get(position).getNote() != null) {
            str = tripArrayList.get(position).getNote() + "\n" + note;
        }else {
            str = note;
        }
        tripArrayList.get(position).setNote(str);
        sp= context.getSharedPreferences("UserPrefrence",Context.MODE_PRIVATE);
        tripuid=sp.getString("uid","");
        FirebaseDatabase.getInstance().getReference().child("Users").child(tripuid).child("upcomingtrip")
                .child(tripArrayList.get(position).getName()).child("note").setValue(str);
    }

    public void editTrip(){
        Intent intentTrip= new Intent(context,Add_Trip.class);

        intentTrip.putExtra("tripName",tripArrayList.get(position).getName());
        intentTrip.putExtra("triplocation",tripArrayList.get(position).getLocation());
        intentTrip.putExtra("tripDate",tripArrayList.get(position).getDate());
        intentTrip.putExtra("tripTime",tripArrayList.get(position).getTime());
        context.startActivity(intentTrip);


    }
    public void removeTrip(){
        sp= context.getSharedPreferences("UserPrefrence",Context.MODE_PRIVATE);
        tripuid=sp.getString("uid","");
        FirebaseDatabase.getInstance().getReference().child("Users").child(tripuid).child("upcomingtrip").child(tripArrayList.get(position).getName()).removeValue();
    }
    public void cancleTrip(){

        HashMap <String,Object> user= new HashMap<>();
        user.put("name", tripArrayList.get(position).getName());
        user.put("location", tripArrayList.get(position).getLocation());
        user.put("date", tripArrayList.get(position).getDate());
        user.put("time", tripArrayList.get(position).getTime());
        user.put("note", tripArrayList.get(position).getNote());
        user.put("trip_cancle", "");
        user.put("type",tripArrayList.get(position).getType());
        sp= context.getSharedPreferences("UserPrefrence",Context.MODE_PRIVATE);
        tripuid=sp.getString("uid","");
        String cancle="cancle";

        FirebaseDatabase.getInstance().getReference("Users").child(tripuid).child("donetrip").child(tripArrayList.get(position)
                .getName()).setValue(user);

        FirebaseDatabase.getInstance().getReference().child("Users").child(tripuid).child("donetrip")
                .child(tripArrayList.get(position).getName()).child("trip_cancle").setValue(cancle);

        FirebaseDatabase.getInstance().getReference().child("Users").child(tripuid).child("upcomingtrip")
                .child(tripArrayList.get(position).getName()).removeValue();
    }
}
