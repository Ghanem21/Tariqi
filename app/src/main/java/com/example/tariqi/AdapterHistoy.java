package com.example.tariqi;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.helper.widget.Carousel;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterHistoy extends RecyclerView.Adapter<AdapterHistoy.ViewHolder> {
    private List<DataTrip> dataList;
    Context context;

    public AdapterHistoy(List<DataTrip> dataList,Context mcontext) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public AdapterHistoy.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_history,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterHistoy.ViewHolder holder, int position) {
        holder.tripname.setText(dataList.get(position).getTripName());



    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tripname;
        CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tripname= itemView.findViewById(R.id.tridnameid);
            cardView=itemView.findViewById(R.id.cardview);

        }
    }
}
