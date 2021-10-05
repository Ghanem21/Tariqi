package com.example.tariqi;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;


import android.view.View;

import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;

import java.util.Calendar;

import java.util.Locale;

public class Add_Trip<savedInstanceState> extends AppCompatActivity {
TextView textView;
    //ghanem add for datebase
    FirebaseFirestore db;
    EditText tripName,startPoint,endPoint;
    Button addTrip;
    RadioGroup radioGroup;
    RadioButton radioButton;
    TextView date_tv , time_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);
        //ghanem add for datebase
        db = FirebaseFirestore.getInstance();
        tripName = (EditText)findViewById(R.id.edt_trip_name);
        startPoint = (EditText)findViewById(R.id.edt_start_point);
        endPoint = (EditText)findViewById(R.id.edt_end_point);
        addTrip = (Button)findViewById(R.id.btn_add_trip);
        radioGroup = (RadioGroup) findViewById(R.id.groupradio);
        date_tv = findViewById(R.id.addtrip_tv_date);
        time_tv = findViewById(R.id.addtrip_tv_time);





        addTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = tripName.getText().toString();
                String start = startPoint.getText().toString();
                String end = endPoint.getText().toString();
                String date = date_tv.getText().toString();
                String time = time_tv.getText().toString();
                String type = radioButton.getText().toString();
                //replace 10 with date and time
                Trip trip = new Trip(name,end,date,time,type);
                add(trip);
            }
        });

        textView = findViewById(R.id.addtrip_tv_time);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(Add_Trip.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        textView.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });
        final Calendar myCalendar = Calendar.getInstance();


        TextView edittext= (TextView) findViewById(R.id.addtrip_tv_date);


        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

            private void updateLabel() {
                String myFormat = "MM/dd/yy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                edittext.setText(sdf.format(myCalendar.getTime()));

            }

        };

        edittext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(Add_Trip.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });
    }
    public  void checkedRadioButton(View view){
        int radiobtn = radioGroup.getCheckedRadioButtonId();
        radioButton = (RadioButton) findViewById(radiobtn);
    }
    public void add(Trip trip){
        // Add a new document with a generated ID
        db.collection("trip")
                .add(trip)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getApplicationContext(),Home.class);
                        startActivity(i);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "failed in add Trip", Toast.LENGTH_SHORT).show();
                    }
    });
}


}