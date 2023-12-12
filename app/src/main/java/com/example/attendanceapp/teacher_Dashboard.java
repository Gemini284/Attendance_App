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

        initializeViews();
        setClickListeners();
        setupRecyclerView();
    }

    private void initializeViews() {
        drawerLayout = findViewById(R.id.drawerLayout);
        menu = findViewById(R.id.menu);
        home = findViewById(R.id.home);
        settings = findViewById(R.id.settings);
        calendar = findViewById(R.id.calendar);
        logout = findViewById(R.id.logout);
        analytics = findViewById(R.id.statistics);
        classes = findViewById(R.id.classes);
        userId = getIntent().getStringExtra("userId");
    }

    private void setClickListeners() {
        menu.setOnClickListener(view -> openDrawer(drawerLayout));
        home.setOnClickListener(view -> recreate());
        settings.setOnClickListener(view -> redirectActivity(teacher_Dashboard.this, settings.class));
        calendar.setOnClickListener(view -> redirectActivity(teacher_Dashboard.this, calendar.class));
        classes.setOnClickListener(view -> redirectActivity(teacher_Dashboard.this, classes.class));
        analytics.setOnClickListener(view -> redirectActivity(teacher_Dashboard.this, statistic.class));
        logout.setOnClickListener(view -> showLogoutConfirmationDialog());
    }

    private void setupRecyclerView() {
        recyclerView = findViewById(R.id.rv);
        recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        String currentDay = getCurrentDayOfWeek();
        Toast.makeText(this, "Current Day: " + currentDay, Toast.LENGTH_SHORT).show();

        DatabaseReference coursesRef = FirebaseDatabase.getInstance().getReference().child("courses").child(currentDay);
        Query teacherQuery = coursesRef.orderByChild("TEACHER/userId").equalTo(userId);

        FirebaseRecyclerOptions<modelCourse> options =
                new FirebaseRecyclerOptions.Builder<modelCourse>()
                        .setQuery(teacherQuery, modelCourse.class)
                        .build();

        mainAdapter = new rvAdapter(options, true);
        recyclerView.setAdapter(mainAdapter);

        mainAdapter.setOnItemClickListener(model -> {
            if ("Open".equals(model.getStatus())) {
                showSignInDialog();
            } else {
                // Handle logic for other statuses (e.g., show a message)
            }
        });
    }

    private void showSignInDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sign In");
        builder.setMessage("Do you want to sign in to this class?");

        builder.setPositiveButton("Yes", (dialogInterface, i) -> {
            // Implement logic for signing in (e.g., update the student's attendance)
            dialogInterface.dismiss();  // Dismiss the dialog
        });

        builder.setNegativeButton("No", (dialogInterface, i) -> dialogInterface.dismiss());

        AlertDialog signInDialog = builder.create();
        signInDialog.setCancelable(true);
        signInDialog.show();
    }

    private void showLogoutConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout?");

        builder.setPositiveButton("Yes", (dialogInterface, i) -> logoutUser());

        builder.setNegativeButton("No", (dialogInterface, i) -> dialogInterface.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void logoutUser() {
        FirebaseAuth.getInstance().signOut();
        redirectActivity(teacher_Dashboard.this, login.class);
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

    private void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    private void redirectActivity(Activity activity, Class secondActivity) {
        Intent intent = new Intent(activity, secondActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.finish();
    }

    private String getCurrentDayOfWeek() {
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        String[] daysOfWeek = {"", "SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY"};
        return daysOfWeek[dayOfWeek];
    }

    @Override
    protected void onPause() {
        super.onPause();
        closeDrawer(drawerLayout);
    }

    private void closeDrawer(DrawerLayout drawerLayout) {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }
}