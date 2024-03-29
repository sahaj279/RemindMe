package com.example.remindme;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.remindme.models.Model;

import java.util.Set;

//this class creates the Reminder Notification Message

public class NotificationMessage extends AppCompatActivity {
    TextView msg, date, time;
    Button md;//mark done button

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification);
        msg = findViewById(R.id.str);
        date = findViewById(R.id.date);
        time = findViewById(R.id.time);
        Bundle bundle = getIntent().getExtras();                                                    //call the data which is passed by another intent
        msg.setText(bundle.getString("message"));
        date.setText(bundle.getString("date"));
        Set<String> s=bundle.keySet();
        if(s.contains("time")){
            time.setText(bundle.getString("time"));
            Model model = new Model(bundle.getString("message"), bundle.getString("date"), bundle.getString("time"));
            md = findViewById(R.id.md);
            md.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dbManager dbManager = new dbManager(NotificationMessage.this);
                    dbManager.update_complete(model);
                    Intent intent = new Intent(NotificationMessage.this, MainActivity.class);
                    Toast.makeText(NotificationMessage.this, "Task Completed!", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }
            });
        }
        else{
            time.setText(bundle.getString("contact"));

            msg.setText(bundle.getString("message"));
            md = findViewById(R.id.md);
            md.setText("Happy Birthday");

        }



    }
}