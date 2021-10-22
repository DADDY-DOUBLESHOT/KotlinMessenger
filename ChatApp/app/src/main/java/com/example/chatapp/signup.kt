package com.example.chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*

class signup : AppCompatActivity()
{
    lateinit var signin_bck_btn:Button;
    lateinit var signin_google:ImageView;
    lateinit var signin_apple:ImageView;
    lateinit var signin_usr_name:EditText;
    lateinit var signin_usr_email:EditText;
    lateinit var signin_usr_pass:EditText;
    lateinit var signin_create_ac:Button;
    lateinit var login_link:TextView;

    lateinit var login_intent:Intent;

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        signin_bck_btn=findViewById(R.id.signup_back_btn);
        signin_google=findViewById(R.id.signup_google_ac);
        signin_apple=findViewById(R.id.signup_apple_ac);
        signin_usr_name=findViewById(R.id.signup_usr_name);
        signin_usr_email=findViewById(R.id.signup_usr_email);
        signin_usr_pass=findViewById(R.id.signup_usr_pass);
        signin_create_ac=findViewById(R.id.signup_create_btn);
        login_link=findViewById(R.id.login_link);


        signin_google.setOnClickListener {
            Toast.makeText(this,"Google Account not linked", Toast.LENGTH_SHORT).show();
        };
        signin_apple.setOnClickListener {
            Toast.makeText(this,"Apple Account not linked", Toast.LENGTH_SHORT).show();
        };
        signin_create_ac.setOnClickListener {
            Toast.makeText(this,"Account creation page will be created..", Toast.LENGTH_SHORT).show();
        };




        signin_bck_btn.setOnClickListener{

            login_intent= Intent(this,MainActivity::class.java);
            startActivity(login_intent);
        };

        login_link.setOnClickListener {
            login_intent= Intent(this,MainActivity::class.java);
            startActivity(login_intent);
        };




    }
}