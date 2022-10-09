package com.example.remindme.screens;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.remindme.adapters.AllReminderAdapter;
import com.example.remindme.MainActivity;
import com.example.remindme.R;
import com.example.remindme.dbManager;
import com.example.remindme.models.Model;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class AllReminders extends AppCompatActivity {

    RecyclerView mRecyclerview;
    ArrayList<Model> dataholder = new ArrayList<Model>();
    ImageView imageView;
    AllReminderAdapter adapter;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ActionBarDrawerToggle actionBarDrawerToggle;


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
        setContentView(R.layout.allreminders);

        createExampleList();
        buildRecyclerView();
        imageView= (ImageView) findViewById(R.id.alldone);

        if(dataholder.size()==0){
            imageView.setVisibility(View.VISIBLE);
        }
        else {
            imageView.setVisibility(View.INVISIBLE);
        }
//navigation view
        navigationView=(NavigationView) findViewById(R.id.navigation_viewaa);
        drawerLayout=(DrawerLayout) findViewById(R.id.draweraa);
        toolbar=(Toolbar)findViewById(R.id.toolbaraa);
        setSupportActionBar(toolbar);
        actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.menu_open,R.string.menu_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
                        Intent i =new Intent(getApplicationContext(),AllReminders.class);
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
        Cursor cursor = new dbManager(getApplicationContext()).readallreminders();                  //Cursor To Load data From the database
        while (cursor.moveToNext()) {
            Model model = new Model(cursor.getString(1), cursor.getString(2), cursor.getString(3));
            dataholder.add(model);
        }
    }
    public void buildRecyclerView(){
        mRecyclerview = (RecyclerView) findViewById(R.id.rec);
        mRecyclerview.setHasFixedSize(true);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new AllReminderAdapter(dataholder);
        mRecyclerview.setAdapter(adapter);
        adapter.setOnItemClickListener(new AllReminderAdapter.onItemClickListener() {
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
