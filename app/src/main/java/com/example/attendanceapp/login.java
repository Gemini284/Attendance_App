package com.example.attendanceapp;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class login extends AppCompatActivity {

    DatabaseReference dataRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://attendaceproject-default-rtdb.firebaseio.com/");


    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        final EditText loginEmail = findViewById(R.id.loingEmail);
        final EditText loginPass = findViewById(R.id.password);
        final Button loginBtn = findViewById(R.id.loginUnm_btn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String uidTxt = loginEmail.getText().toString();
                final String passTxt = loginPass.getText().toString();
                boolean isTeacher = false;

                if (uidTxt.isEmpty() || passTxt.isEmpty()){
                    Toast.makeText(login.this, "Please enter the required data", Toast.LENGTH_SHORT).show();
                }
                else {

                    dataRef.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) { //checks the dataset for user
                            if (snapshot.hasChild(uidTxt)){ //checks uid

                                final String getPassword = snapshot.child(uidTxt).child("password").getValue(String.class);

                                if (getPassword.equals(passTxt)){
                                    //HERE CHECK ISTEACHER
                                    Toast.makeText(login.this,"Sucessful",Toast.LENGTH_SHORT).show();

                                    startActivity(new Intent(login.this, student_dashboard.class));
                                    finish();
                                }
                                else {
                                    Toast.makeText(login.this, "Incorrect Password",Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(login.this, "Incorrect Password",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

    }

}
