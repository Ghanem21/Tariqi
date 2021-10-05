package com.example.tariqi;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AdapterHistoy extends RecyclerView.Adapter<AdapterHistoy.ViewHolder> {
    private ArrayList<Trip>tripArrayList;

    Context context;
    private FirebaseDatabase FD=FirebaseDatabase.getInstance();
    private DatabaseReference DR;
    SharedPreferences sp;
    String email,password,uid,name, location, date,end,time, type,upcomingid,doneid;
    Trip trip = new Trip(name,end,date,time,type,uid,email,password,upcomingid,doneid);
    public AdapterHistoy( ArrayList<Trip> tripArrayList, Context context) {
        this.tripArrayList = tripArrayList;
    }


    @NonNull
    @Override
    public AdapterHistoy.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_history,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterHistoy.ViewHolder holder, int position) {
        holder.tripname.setText(tripArrayList.get(position).getName());
        holder.tripLocation.setText(tripArrayList.get(position).getLocation());
        holder.tripNote.setText(tripArrayList.get(position).getNote());
        holder.tripDate.setText(tripArrayList.get(position).getDate());
        holder.tripType.setText(tripArrayList.get(position).getType());
        holder.tripTime.setText(tripArrayList.get(position).getTime());

        holder.showDetils.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder.tripLocation.setVisibility(View.VISIBLE);
                holder.tripTime.setVisibility(View.VISIBLE);
                holder.tripType.setVisibility(View.VISIBLE);
                holder.tripDate.setVisibility(View.VISIBLE);
                holder.tripNote.setVisibility(View.VISIBLE);
                holder.delet.setVisibility(View.VISIBLE);
                holder.showLess.setVisibility(View.VISIBLE);
            }
        });
        holder.showLess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.tripLocation.setVisibility(View.GONE);
                holder.tripTime.setVisibility(View.GONE);
                holder.tripType.setVisibility(View.GONE);
                holder.tripDate.setVisibility(View.GONE);
                holder.tripNote.setVisibility(View.GONE);
                holder.delet.setVisibility(View.GONE);
                holder.showLess.setVisibility(View.GONE);
            }
        });
        holder.delet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder= new AlertDialog.Builder(holder.tripname.getContext());
                builder.setTitle("Are you sure?");
                builder.setMessage("Delet data cannot be undo !");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tripArrayList.remove(position);
                        notifyItemRemoved(position);
//                        sp=context.getSharedPreferences("UserPrefrence", Context.MODE_PRIVATE);
//                        String tripuid=sp.getString("uid","");
//                        DR=FD.getReference().child("Users").child(tripuid).child("donetrip").child(DR.getKey()).removeValue();
//                        Toast.makeText(holder.tripname, "delete", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("cancle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            }
        });
//        holder.cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i= new Intent(v.getContext(),ItemHistory.class);
//                v.getContext().startActivity(i);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return  tripArrayList.size();

    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tripname,tripLocation,tripTime,tripType,tripDate,tripNote,showDetils,delet,showLess;
        CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tripname= itemView.findViewById(R.id.tridnameid);
            cardView=itemView.findViewById(R.id.cardview);
            tripLocation=itemView.findViewById(R.id.txtlocation);
            tripTime=itemView.findViewById(R.id.txttime);
            tripType=itemView.findViewById(R.id.txttype);
            tripDate=itemView.findViewById(R.id.txtdate);
            tripNote=itemView.findViewById(R.id.txtnote);
            showDetils=itemView.findViewById(R.id.details);
            delet=itemView.findViewById(R.id.txtdelet);
            showLess=itemView.findViewById(R.id.less);

        }
    }
}
