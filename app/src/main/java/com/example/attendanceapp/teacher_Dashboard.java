package com.example.attendanceapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.Calendar;


public class teacher_Dashboard extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private ImageView menu;
    private LinearLayout home, settings, calendar, logout, analytics, classes;
    private RecyclerView recyclerView;
    private rvAdapter mainAdapter;
    private String userId; // Declare userId variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_dashboard);

        // Initialize views
        drawerLayout = findViewById(R.id.drawerLayout);
        menu = findViewById(R.id.menu);
        home = findViewById(R.id.home);
        settings = findViewById(R.id.settings);
        calendar = findViewById(R.id.calendar);
        logout = findViewById(R.id.logout);
        analytics = findViewById(R.id.statistics);
        classes = findViewById(R.id.classes);
        userId = getIntent().getStringExtra("userId");

        menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openDrawer(drawerLayout);
                    // Display userId as a toast when the drawer is opened
                    //Next Toast to ensure that the userId has been carried
                    //Toast.makeText(teacher_Dashboard.this, "User ID: " + userId, Toast.LENGTH_SHORT).show();
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
                redirectActivity(teacher_Dashboard.this, settings.class);
            }
        });

        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(teacher_Dashboard.this, calendar.class);
            }
        });

        classes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(teacher_Dashboard.this, classes.class);
            }
        });

        analytics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(teacher_Dashboard.this, statistic.class);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLogoutConfirmationDialog();
            }
        });

        recyclerView = findViewById(R.id.rv);
        recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        String currentDay = getCurrentDayOfWeek();
        // Display the current day using a Toast (you can comment this line if not needed)
        Toast.makeText(this, "Current Day: " + currentDay, Toast.LENGTH_SHORT).show();

        // Use the correct node name (case-sensitive) in your database query
        DatabaseReference coursesRef = FirebaseDatabase.getInstance().getReference().child("courses").child(currentDay);

        // Create a query to fetch the specific user's courses
        Query teacherQuery = coursesRef.orderByChild("TEACHER/userId").equalTo(userId);

        //Check the user Type
        //rvAdapter mainAdapter = new rvAdapter(options, userType);
        recyclerView.setAdapter(mainAdapter);

        FirebaseRecyclerOptions<modelCourse> options =
                new FirebaseRecyclerOptions.Builder<modelCourse>()
                        .setQuery(teacherQuery, modelCourse.class)
                        .build();

        mainAdapter = new rvAdapter(options);
        recyclerView.setAdapter(mainAdapter);

        // Add a listener to check if there are no items in the RecyclerView
        /*mainAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                // Check if items are inserted
                checkIfNoItems();
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onItemRangeRemoved(positionStart, itemCount);
                // Check if items are removed
                checkIfNoItems();
            }
        });

        // Check if there are no items initially
        checkIfNoItems();
    }

    private void checkIfNoItems() {
        if (mainAdapter.getItemCount() == 0) {
            // If no items, show a message
            Toast.makeText(this, "No courses to display", Toast.LENGTH_SHORT).show();
            // Alternatively, you can update a TextView or use a Snackbar to show a message
        }*/
    }

    private void showLogoutConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                logoutUser();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void logoutUser() {
        FirebaseAuth.getInstance().signOut();
        redirectActivity(teacher_Dashboard.this, login.class);
    }
    private void displayUserIdToast() {
        // Display userId as a toast
        Toast.makeText(teacher_Dashboard.this, "User ID: " + userId, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mainAdapter != null) {
            mainAdapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mainAdapter != null) {
            mainAdapter.stopListening();
        }
    }

    public static void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public static void closeDrawer(DrawerLayout drawerLayout) {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public static void redirectActivity(Activity activity, Class secondActivity) {
        Intent intent = new Intent(activity, secondActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.finish();
    }
    private String getCurrentDayOfWeek() {
        Calendar calendar = Calendar.getInstance();

        // Get the current day of the week as an integer (1 = Sunday, 2 = Monday, ..., 7 = Saturday)
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        // Convert the integer day of the week to a string representation
        String[] daysOfWeek = {"", "SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY"};
        String currentDayOfWeek = daysOfWeek[dayOfWeek];

        return currentDayOfWeek;
    }

    @Override
    protected void onPause() {
        super.onPause();
        closeDrawer(drawerLayout);
    }

}