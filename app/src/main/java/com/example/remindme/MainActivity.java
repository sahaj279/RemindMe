package com.example.remindme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.widget.Toolbar;
//import android.widget.Toolbar;
import com.example.remindme.adapters.myAdapter;
import com.example.remindme.models.Model;
import com.example.remindme.screens.AllReminders;
import com.example.remindme.screens.PeopleActivity;
import com.example.remindme.screens.ReminderActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton mCreateRem;
    RecyclerView mRecyclerview;
    ArrayList<Model> dataholder = new ArrayList<Model>();
    ImageView imageView;
    myAdapter adapter;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ActionBarDrawerToggle actionBarDrawerToggle;

//method for clicking item of menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createExampleList();
        buildRecyclerView();
        imageView= (ImageView) findViewById(R.id.iii);
        mCreateRem = (FloatingActionButton) findViewById(R.id.create_reminder);
        mCreateRem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ReminderActivity.class);
                startActivity(intent);
                //Starts the new activity to add Reminders


            }
        });
        if(dataholder.size()==0){
            imageView.setVisibility(View.VISIBLE);
        }
        else {
            imageView.setVisibility(View.INVISIBLE);
        }


        //navigation view

        navigationView=(NavigationView) findViewById(R.id.navigation_view);
        drawerLayout=(DrawerLayout) findViewById(R.id.drawer_layout);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.menu_open,R.string.menu_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        Log.i("clicked","home");
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Intent i1 =new Intent(MainActivity.this,MainActivity.class);
                        startActivity(i1);
                        break;
                    case R.id.history:
                        Log.i("clicked","history");
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Intent i =new Intent(MainActivity.this, AllReminders.class);
                        startActivity(i);
                        break;
                    case R.id.people:
                        Log.i("clicked","people");
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Intent i2 =new Intent(getApplicationContext(), PeopleActivity.class);
                        startActivity(i2);
                        break;

                }
                return true;
            }
        });


    }


    public void createExampleList(){
        Cursor cursor = new dbManager(getApplicationContext()).read_all_incompleted_reminders();//Cursor To Load data From the database
        while (cursor.moveToNext()) {
            Model model = new Model(cursor.getString(1), cursor.getString(2), cursor.getString(3));
            dataholder.add(model);
        }
    }
    public void buildRecyclerView(){
        mRecyclerview = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerview.setHasFixedSize(true);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new myAdapter(dataholder);
        mRecyclerview.setAdapter(adapter);
        adapter.setOnItemClickListener(new myAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int position) {
                dataholder.get(position);
            }

            @Override
            public void onDeleteClick(int position) {
                removeItem(position);
            }
        });
    }
    public void removeItem(int position){
        dataholder.remove(position);
        adapter.notifyItemRemoved(position);
    }

    @Override
    public void onBackPressed() {
        finish();//Makes the user to exit from the app
        super.onBackPressed();

    }

}