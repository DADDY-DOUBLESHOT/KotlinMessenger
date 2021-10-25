package com.example.chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*

class MainActivity : AppCompatActivity()
{
    lateinit var google_account:ImageView;
    lateinit var apple_account:ImageView;
    lateinit var usr_email:EditText;
    lateinit var usr_pass:EditText;
    lateinit var login_btn:Button;
    lateinit var signup_link:TextView;
    lateinit var signup_Intent :Intent;




    lateinit var  email:String;
    lateinit var  password:String;

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        google_account=findViewById(R.id.login_google_ac);
        apple_account=findViewById(R.id.login_apple_ac);
        usr_email=findViewById(R.id.usr_email);
        usr_pass=findViewById(R.id.usr_pass);
        login_btn=findViewById(R.id.login_btn);
        signup_link=findViewById(R.id.signup_link);


//        login pre setting
        val bundle:Bundle?=intent.extras;
        email= bundle?.getString("usr_email").toString();
        password=bundle?.getString("usr_pass").toString();



        google_account.setOnClickListener {

                Toast.makeText(this,"Google Account not linked",Toast.LENGTH_SHORT).show();
        };
        apple_account.setOnClickListener {

            Toast.makeText(this,"Apple Account not linked",Toast.LENGTH_SHORT).show();
        };


        login_btn.setOnClickListener {

            Toast.makeText(this,"Will be connecting to login page",Toast.LENGTH_SHORT).show();
        };
        signup_link.setOnClickListener {

            signup_Intent= Intent(this,signup::class.java);
            startActivity(signup_Intent);

        };

    }

    override fun onRestart() {
        super.onRestart()
        if(!email.isNullOrEmpty())usr_email.setText(email);
        if(!password.isNullOrEmpty())usr_pass.setText(password);

    }

    override fun onResume() {
        super.onResume()
        if(!email.isNullOrEmpty())usr_email.setText(email);
        if(!password.isNullOrEmpty())usr_pass.setText(password);

    }
}