package com.example.attendanceapp;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class student_dashboard extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ImageView menu;
    LinearLayout home,settings, calendar,logout,analytics,classes;
    private FirebaseUser user;
    RecyclerView recyclerView;
    rvAdapter mainAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_dashboard);

        //Probably error here
        drawerLayout = findViewById(R.id.drawerLayout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       TextView userName = findViewById(R.id.name_st);
       TextView idNumber = findViewById(R.id.idnumber);
       menu = findViewById(R.id.menu);
       home = findViewById(R.id.home);
       settings = findViewById(R.id.settings);
       calendar = findViewById(R.id.calendar);
       logout = findViewById(R.id.logout);
       analytics = findViewById(R.id.statistics);
       classes = findViewById(R.id.classes);

       menu.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               openDrawer(drawerLayout);
           }
       });
       home.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               recreate();
           }
       });
       settings.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               redirectActivity(student_dashboard.this,settings.class);
           }
       });
        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(student_dashboard.this,calendar.class);
            }
        });
        classes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(student_dashboard.this,classes.class);
            }
        });
        analytics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(student_dashboard.this,statistic.class);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(student_dashboard.this, "Logout", Toast.LENGTH_SHORT).show();
            }
        });


       recyclerView = (RecyclerView)findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions<modelCourse> options =
                new FirebaseRecyclerOptions.Builder<modelCourse>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Courses"), modelCourse.class)
                        .build();

        mainAdapter = new rvAdapter(options);
        recyclerView.setAdapter(mainAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mainAdapter.startListening();
    }

    @Override
    protected void onStop() {
        mainAdapter.stopListening();
        super.onStop();
    }

    public static void openDrawer(DrawerLayout drawerLayout){
        drawerLayout.openDrawer(GravityCompat.START);
    }
    public static void closeDrawer(DrawerLayout drawerLayout){
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }
    public static void redirectActivity(Activity activity, Class secondActivity){
        Intent intent = new Intent(activity,secondActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        closeDrawer(drawerLayout);
    }
}
