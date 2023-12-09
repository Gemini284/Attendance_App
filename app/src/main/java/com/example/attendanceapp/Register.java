package com.example.attendanceapp;

import static java.nio.file.Files.find;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Register extends AppCompatActivity {

    DatabaseReference dataReference = FirebaseDatabase.getInstance().getReference("users");
    Button registerBtn;
    boolean valid = true;
    FirebaseAuth Auth;
    Switch teacherSwitch;
    FirebaseDatabase FStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        Auth =FirebaseAuth.getInstance();
        FStore = FirebaseDatabase.getInstance();
        final EditText editTextname = findViewById(R.id.nameReg);
        final EditText editTextpassword = findViewById(R.id.passwordReg);
        final EditText editTextuserId = findViewById(R.id.idReg);
        final EditText editTextemail = findViewById(R.id.emailReg);
        final Button registerBtn = findViewById(R.id.register_btn);
        teacherSwitch = findViewById(R.id.isTeacher);

        registerBtn.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View view) {
                String email, password, userId, name;
                name = String.valueOf(editTextname.getText());
                email = String.valueOf(editTextemail.getText());
                userId = String.valueOf(editTextuserId.getText());
                password = String.valueOf(editTextpassword.getText());

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(Register.this, "Enter email",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(Register.this, "Enter password",Toast.LENGTH_SHORT).show();
                    return;
                }else if(TextUtils.isEmpty(userId)){
                    Toast.makeText(Register.this, "Enter Userid",Toast.LENGTH_SHORT).show();
                    return;
                }else if(TextUtils.isEmpty(name)){
                    Toast.makeText(Register.this, "Enter name",Toast.LENGTH_SHORT).show();
                    return;

                } else{
                    dataReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(userId)){ //register with the userId
                                Toast.makeText(Register.this, "Already exists", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                //sending the data.UID is used as unique identifier
                                dataReference.child(userId).child("name").setValue(name);
                                dataReference.child(userId).child("email").setValue(email);
                                dataReference.child(userId).child("password").setValue(password);
                                dataReference.child(userId).child("name").setValue(name);
                                dataReference.child(userId).child("userId").setValue(userId);
                                dataReference.child(userId).child("isTeacher").setValue(getSwitchIsChecked());

                                Toast.makeText(Register.this, "Registered Sucessfully", Toast.LENGTH_SHORT).show();
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
    public boolean getSwitchIsChecked() {
        boolean isChecked = false;
        if (teacherSwitch != null) {
            isChecked = teacherSwitch.isChecked();
        }
        return isChecked;
    }
}
