package com.example.attendanceapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class login extends AppCompatActivity {

    DatabaseReference dataRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://attendaceproject-default-rtdb.firebaseio.com/");
    FirebaseAuth mAuth;
    boolean isTeacher; // Move it to class level

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        mAuth = FirebaseAuth.getInstance();

        final EditText loginUid = findViewById(R.id.loginUid);
        final EditText loginPass = findViewById(R.id.password);
        final Button loginBtn = findViewById(R.id.loginUnm_btn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String uidTxt = loginUid.getText().toString();
                final String passTxt = loginPass.getText().toString();

                if (TextUtils.isEmpty(uidTxt) || TextUtils.isEmpty(passTxt)) {
                    Toast.makeText(login.this, "Please enter the required data", Toast.LENGTH_SHORT).show();
                } else {
                    signIn(uidTxt, passTxt);
                }
            }
        });
    }

    private void signIn(final String uidTxt, final String passTxt) {
        // Now, let's check the database for user authentication
        checkDatabaseForUser(uidTxt, passTxt);
    }

    private void checkDatabaseForUser(final String uidTxt, final String password) {
        dataRef.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // checks the dataset for user
                if (snapshot.hasChild(uidTxt)) { // checks uid
                    final String getPassword = snapshot.child(uidTxt).child("password").getValue(String.class);
                    isTeacher = snapshot.child(uidTxt).child("isTeacher").getValue(Boolean.class);
                    String userId = snapshot.child(uidTxt).child("userId").getValue(String.class);

                    if (getPassword.equals(password)) {
                        Toast.makeText(login.this, "Login successful", Toast.LENGTH_SHORT).show();

                        navigateDashboard(userId);
                    } else {
                        Toast.makeText(login.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(login.this, "User not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error if needed
            }
        });
    }

    private void navigateDashboard(String userId) {
        // Navigate to the dashboard based on your custom logic
        Intent intent;

        if (isTeacher) {
            intent = new Intent(login.this, teacher_Dashboard.class);
        } else {
            intent = new Intent(login.this, student_dashboard.class);
        }

        // Pass the userId to the next activity and isTeacher for the rvAdapter
        intent.putExtra("userId", userId);
        intent.putExtra("isTeacher", isTeacher);



        // Start the next activity
        startActivity(intent);

        // Finish the current activity
        finish();
    }
}

