package com.example.tariqi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.provider.ContactsContract;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;

import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.SQLOutput;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class Add_Trip extends AppCompatActivity {
TextView textView;
    //ghanem add for datebase
    FirebaseFirestore db;
    EditText tripName,startPoint1,endPoint;
    Button addTrip;
    RadioGroup radioGroup;
    RadioButton radioButton;
    TextView date_tv , time_tv;
    String name, location, date,time,note, type,startPoint;
    SharedPreferences sp;
    int cyear,cmonth,cday,syear,smonth,sday,chour,cminute,shour,sminute;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);
        Calendar calendar =Calendar.getInstance();
        cyear=calendar.get(Calendar.YEAR);
        cmonth=calendar.get(Calendar.MONTH);
        cday=calendar.get(Calendar.DAY_OF_MONTH);
        chour=calendar.get(Calendar.HOUR_OF_DAY);
        cminute=calendar.get(Calendar.MINUTE);
        String date = new SimpleDateFormat("dd-MM-yyyy",
                Locale.getDefault()).format(new Date());
        String time = new SimpleDateFormat("hh:mm aa",Locale.getDefault()).format(new Date());
        //realtime database
        sp=getSharedPreferences("UserPrefrence",MODE_PRIVATE);


        //ghanem add for datebase
        db = FirebaseFirestore.getInstance();
        tripName = (EditText)findViewById(R.id.edt_trip_name);
        startPoint1 = (EditText)findViewById(R.id.edt_start_point);
        endPoint = (EditText)findViewById(R.id.edt_end_point);
        addTrip = (Button)findViewById(R.id.btn_add_trip);
        radioGroup = (RadioGroup) findViewById(R.id.groupradio);
        date_tv = findViewById(R.id.addtrip_tv_date);
        time_tv = findViewById(R.id.addtrip_tv_time);

        addTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = tripName.getText().toString();
                String startPoint = startPoint1.getText().toString();
                String location = endPoint.getText().toString();
                String date = date_tv.getText().toString();
                String time = time_tv.getText().toString();
                String type = radioButton.getText().toString();
                //replace 10 with date and time

                Trip trip = new Trip(name,location,date,time,type,startPoint,note);
                add(trip);


                //realtime database (rahma)
                HashMap<String,String> userMap= new HashMap<>();
                userMap.put("name",name);
                userMap.put("start",startPoint);
                userMap.put("location",location);
                userMap.put("date",date);
                userMap.put("time",time);
                userMap.put("type",type);



                sp=getApplicationContext().getSharedPreferences("UserPrefrence", Context.MODE_PRIVATE);
                String tripuid=sp.getString("uid","");
                FirebaseDatabase.getInstance().getReference("Users").child(tripuid).child("upcomingtrip").child(name).setValue(userMap);


            }
        });


        time_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(Add_Trip.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            shour=hourOfDay;
                            sminute=minute;
                            Calendar calendar =Calendar.getInstance();
                            String sdate = date_tv.getText().toString().trim();
                            String[] strings =sdate.split("-");
                            int sday = Integer.parseInt(strings[0]);
                            calendar.set(Calendar.DAY_OF_MONTH,sday);
                            calendar.set(Calendar.HOUR_OF_DAY,shour);
                            calendar.set(Calendar.MINUTE,sminute);
                            calendar.set(Calendar.SECOND,0);
                            setAlarm(calendar);
                            // time_tv.setText(DateFormat.format("hh:mm aa",calendar));
                            if (calendar.getTimeInMillis() == Calendar.getInstance().getTimeInMillis()){
                                Toast.makeText(Add_Trip.this, "You Select Current Time", Toast.LENGTH_SHORT).show();
                                time_tv.setText(DateFormat.format("hh:mm aa",calendar));
                                }
                            else if (calendar.getTimeInMillis() > Calendar.getInstance().getTimeInMillis()){
                                time_tv.setText(DateFormat.format("hh:mm aa",calendar));
                            }else{
                                Toast.makeText(Add_Trip.this, "You Select Past Time", Toast.LENGTH_SHORT).show();
                            }

                    }
                },chour,cminute,false

                );
                timePickerDialog.show();
              }
        });

        date_tv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(Add_Trip.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        syear=year;
                        smonth=month;
                        sday=dayOfMonth;
                        String sdate = sday+ "-"+(smonth+1)+"-"+syear;
                        date_tv.setText(sdate);
                    }
                },cyear,cmonth,cday
                );
                datePickerDialog.updateDate(syear,smonth,sday);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
                datePickerDialog.show();
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
                        System.out.println( upcomingid);
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
    public void setAlarm(Calendar calendar){
        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(getApplicationContext().ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(),AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),888,intent,0);
        if (calendar.before(Calendar.getInstance())){
            calendar.add(calendar.DATE,1);
        }
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),pendingIntent);
    }
}