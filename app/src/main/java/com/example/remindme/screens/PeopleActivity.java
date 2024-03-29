package com.example.remindme.screens;

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
import com.example.remindme.MainActivity;
import com.example.remindme.R;
import com.example.remindme.adapters.PeopleAdapter;
import com.example.remindme.dbManager;
import com.example.remindme.models.Person;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
public class PeopleActivity extends AppCompatActivity {

    FloatingActionButton addpeople;
    RecyclerView mRecyclerview;
    ArrayList<Person> dataholder = new ArrayList<>();
    ImageView imageView;
    PeopleAdapter adapter;
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
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_layout);

        createExampleList();
        buildRecyclerView();
        imageView= (ImageView) findViewById(R.id.img_no_people);
        addpeople = (FloatingActionButton) findViewById(R.id.add_people);
        addpeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddPeople.class);
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

        navigationView=(NavigationView) findViewById(R.id.navigation_view_people);
        drawerLayout=(DrawerLayout) findViewById(R.id.drawer_layout_people);
        toolbar=(Toolbar)findViewById(R.id.toolbar_p);
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
                        Intent i1 =new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i1);
                        break;
                    case R.id.history:
                        Log.i("clicked","history");
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Intent i =new Intent(getApplicationContext(), AllReminders.class);
                        startActivity(i);
                        break;
                    case R.id.people:
                        Log.i("clicked","people");
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Intent i2 =new Intent(getApplicationContext(),PeopleActivity.class);
                        startActivity(i2);
                        break;

                }
                return true;
            }
        });


    }

    public void createExampleList(){
        Cursor cursor = new dbManager(getApplicationContext()).all_people();//Cursor To Load data From the database
        while (cursor.moveToNext()) {

            Person person=new Person(cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4));
            dataholder.add(person);
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////

    public void buildRecyclerView(){
        mRecyclerview = (RecyclerView) findViewById(R.id.recyclerViewp);
        mRecyclerview.setHasFixedSize(true);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new PeopleAdapter(dataholder);
        mRecyclerview.setAdapter(adapter);
        adapter.setOnItemClickListener(new PeopleAdapter.onItemClickListenerp() {
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



}