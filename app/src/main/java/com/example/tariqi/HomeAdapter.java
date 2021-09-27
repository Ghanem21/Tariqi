package com.example.tariqi;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class HomeAdapter extends ArrayAdapter {
    Context context;
    int resources;
    Trip[] trips;

    public HomeAdapter(@NonNull Context context, int resource, @NonNull Trip[] trips) {
        super(context, resource, trips);
        this.context = context;
        this.resources = resource;
        this.trips = trips;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(resources, parent, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.getName().setText(trips[position].getName());
        viewHolder.getLocation().setText(trips[position].getLocation());
        viewHolder.getDate().setText(trips[position].getDate());
        viewHolder.getTime().setText(trips[position].getTime());
        viewHolder.getType().setText(trips[position].getType());
        viewHolder.getStart().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Start Trip", Toast.LENGTH_SHORT).show();
            }
        });
        viewHolder.getNote().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Note Dialog", Toast.LENGTH_SHORT).show();
            }
        });
        view.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

            }
        });
        return view;
    }

    public class ViewHolder {
        View convertView;
        TextView name, location, date, time, type;
        ImageButton note;
        Button start;

        public Button getStart() {
            if(start == null)
                start = convertView.findViewById(R.id.btn_start);
            return start;
        }

        public ViewHolder(View convertView) {
            this.convertView = convertView;
        }

        public TextView getName() {
            if (name == null)
                name = (TextView) convertView.findViewById(R.id.tv_trip_name);
            return name;
        }

        public TextView getLocation() {
            if(location == null)
                location = (TextView) convertView.findViewById(R.id.tv_location);
            return location;
        }

        public TextView getDate() {
            if(date == null)
                date = (TextView) convertView.findViewById(R.id.tv_date);
            return date;
        }

        public TextView getTime() {
            if(time == null)
                time = (TextView) convertView.findViewById(R.id.tv_time);
            return time;
        }

        public TextView getType() {
            if(type == null)
                type = (TextView) convertView.findViewById(R.id.tv_trip_type);
            return type;
        }

        public ImageButton getNote() {
            if (note == null)
                note = (ImageButton) convertView.findViewById(R.id.imageView_note);
            return note;
        }
    }

}