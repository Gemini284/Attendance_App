package com.example.attendanceapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnloginog = findViewById<Button>(R.id.buttonLogin)
        val btnregister = findViewById<Button>(R.id.btnRegister)

        btnloginog.setOnClickListener{
            val intent = Intent(this, login::class.java)
            startActivity(intent)
        }
        btnregister.setOnClickListener{
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }
    }
}