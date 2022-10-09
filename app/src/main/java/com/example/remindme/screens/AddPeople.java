package com.example.remindme.screens;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.remindme.alarms.BirthdayAlarm;
import com.example.remindme.R;
import com.example.remindme.dbManager;
import com.example.remindme.models.Person;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddPeople extends AppCompatActivity {
    Button add,dob;
    EditText name,contact;
    String timeTonotify="00:00";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_people_activity);
        name = (EditText) findViewById(R.id.editName);
        contact = (EditText) findViewById(R.id.editcontact);
        dob = (Button) findViewById(R.id.btnDOB);
//        MA = (Button) findViewById(R.id.btnMA);
        add = (Button) findViewById(R.id.btnSubmitp);


        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddPeople.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        dob.setText(day + "-" + (month + 1) + "-" + year);                             //sets the selected date as test for button
                    }
                }, year, month, day);
                datePickerDialog.show();                                                                       //when we click on the choose time button it calls the select time method
            }
        });

//        MA.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Calendar calendar = Calendar.getInstance();
//                int year = calendar.get(Calendar.YEAR);
//                int month = calendar.get(Calendar.MONTH);
//                int day = calendar.get(Calendar.DAY_OF_MONTH);
//                DatePickerDialog datePickerDialog = new DatePickerDialog(AddPeople.this, new DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
//                        MA.setText(day + "-" + (month + 1) + "-" + year);                             //sets the selected date as test for button
//                    }
//                }, year, month, day);
//                datePickerDialog.show();
//            }                                        //when we click on the choose date button it calls the select date method
//        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                String name_ = name.getText().toString().trim();                               //access the data from the input field
                String contact_ = contact.getText().toString().trim();                                 //access the date from the choose date button
                String dob_ = dob.getText().toString().trim();                                 //access the time from the choose time button
                String MA_ = "MA.getText().toString().trim()";                                 //access the time from the choose time button


                if (name_.isEmpty() || contact_.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please Enter name and contact details", Toast.LENGTH_SHORT).show();   //shows the toast if input field is empty
                } else {
                    if (dob_.equals("date of birth") ) {                                               //shows toast if date and time are not selected
                        Toast.makeText(getApplicationContext(), "Please enter dob", Toast.LENGTH_SHORT).show();
                    } else {
                        processinsert(name_,contact_,dob_,MA_,year);

                    }
                }
            }
        });
    }
    private void processinsert(String pname, String pcontact, String pdob,String pMA,int year) {
        String result = new dbManager(this).addperson(pname,pdob,pMA,pcontact);
        Calendar calendar=Calendar.getInstance();
        int c_month = calendar.get(Calendar.MONTH)+1;
        int c_day = calendar.get(Calendar.DAY_OF_MONTH);
        int c_year = calendar.get(Calendar.YEAR);
        if(c_year<year)return;
        int b_year=c_year;
        int m_year=c_year;
        String[] dd = pdob.split("-", 3);
        if(c_day>Integer.parseInt(dd[0]) && c_month>=Integer.parseInt(dd[1])){
            b_year++;
        }
        if(!pMA.equals("Date of Marriage Anniversary")){

        String[] mm = pMA.split("-", 3);
        if(c_day>Integer.parseInt(mm[0]) && c_month>=Integer.parseInt(mm[1])){
            m_year++;
        }
        pMA=dd[0]+"-" +dd[1]+"-" +m_year;
        }
        String b_date=dd[0]+"-" +dd[1]+"-" +b_year;

        setAlarm(pname,pcontact,b_date,pMA);                                                                //calls the set alarm method to set alarm
        name.setText("");
        contact.setText("");
        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void setAlarm(String namee, String contactt,String dobb, String maa) {
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);                   //assigning alarm manager object to set alarm

        Intent intent = new Intent(getApplicationContext(), BirthdayAlarm.class);
        intent.putExtra("name", namee);                                                       //sending data to alarm class to create channel and notification
        intent.putExtra("dobb", dobb);
        intent.putExtra("ma", maa);
        intent.putExtra("contact", contactt);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        String dateandtime = dobb + " " + timeTonotify;
        DateFormat formatter = new SimpleDateFormat("d-M-yyyy hh:mm");
        try {
            Date date1 = formatter.parse(dateandtime);
//            am.setInexactRepeating();
            am.set(AlarmManager.RTC_WAKEUP, date1.getTime(), pendingIntent);
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            Person person=new Person(namee,dobb,maa,contactt);
            dbManager dbManager = new dbManager(getApplicationContext());
            dbManager.delete_person(person);
            processinsert(namee,contactt,dobb,maa,++year);
            Toast.makeText(getApplicationContext(), "Birthday reminder set for "+dateandtime  +" and "+ date1.toString(), Toast.LENGTH_LONG).show();

        } catch (ParseException e) {
            e.printStackTrace();
        }

        Intent intentBack = new Intent(getApplicationContext(), PeopleActivity.class);                //this intent will be called once the setting alarm is complete
        intentBack.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intentBack);                                                                  //navigates from adding reminder activity to mainactivity

    }
}