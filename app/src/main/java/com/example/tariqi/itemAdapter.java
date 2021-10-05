package com.example.tariqi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class itemAdapter extends RecyclerView.Adapter<itemAdapter.ViewHolder> {
    private ArrayList<Trip> tripArrayList;
    Context context;

    public itemAdapter(ArrayList<Trip> tripArrayList) {
        this.tripArrayList = tripArrayList;
    }

    @NonNull
    @Override
    public itemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_card,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull itemAdapter.ViewHolder holder, int position) {
        holder.tripname.setText(tripArrayList.get(position).getName());
        holder.startPosition.setText(tripArrayList.get(position).getLocation());
        holder.Type.setText(tripArrayList.get(position).getType());
        holder.Time.setText(tripArrayList.get(position).getTime());
        holder.Date.setText(tripArrayList.get(position).getDate());
        holder.delet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return  tripArrayList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tripname,startPosition,Type,Time,Date,Note;
        Button delet;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tripname=itemView.findViewById(R.id.tripnamehistory);
            startPosition=itemView.findViewById(R.id.txtstartPos);
            Type=itemView.findViewById(R.id.txtType);
            Time=itemView.findViewById(R.id.txtTime);
            Date=itemView.findViewById(R.id.txtDate);
            Note=itemView.findViewById(R.id.txtNote);
            delet=itemView.findViewById(R.id.btnDelet);
        }
    }
}
