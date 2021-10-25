package com.example.chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.*
import androidx.core.os.HandlerCompat.postDelayed
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import java.util.*
import java.util.regex.Pattern
import kotlin.concurrent.schedule

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

    lateinit var mAuth :FirebaseAuth;
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)


//        UI links
        signin_bck_btn=findViewById(R.id.signup_back_btn);
        signin_google=findViewById(R.id.signup_google_ac);
        signin_apple=findViewById(R.id.signup_apple_ac);
        signin_usr_name=findViewById(R.id.signup_usr_name);
        signin_usr_email=findViewById(R.id.signup_usr_email);
        signin_usr_pass=findViewById(R.id.signup_usr_pass);
        signin_create_ac=findViewById(R.id.signup_create_btn);
        login_link=findViewById(R.id.login_link);


//        firebase
        mAuth= FirebaseAuth.getInstance();

//        listeners
//        imagelistener
        signin_google.setOnClickListener {
            Toast.makeText(this,"Google Account not linked", Toast.LENGTH_SHORT).show();
        };
        signin_apple.setOnClickListener {
            Toast.makeText(this,"Apple Account not linked", Toast.LENGTH_SHORT).show();
        };

//create listener
        signin_create_ac.setOnClickListener {

            if(signin_usr_name.text.isEmpty())
            {

            }
            if(signin_usr_email.text.isEmpty())
            {

            }
            if(signin_usr_pass.text.isEmpty())
            {

            }

            firebaseCreate();
        };


//backbtn listeners

        signin_bck_btn.setOnClickListener{

            login_intent= Intent(this,MainActivity::class.java);
            startActivity(login_intent);
        };

        login_link.setOnClickListener {
            login_intent= Intent(this,MainActivity::class.java);
            startActivity(login_intent);
        };




    }

//    firebase activities
    private fun firebaseCreate()
    {
        mAuth.createUserWithEmailAndPassword(signin_usr_email.text.toString(),signin_usr_pass.text.toString())
            .addOnCompleteListener{

                login_intent= Intent(this,MainActivity::class.java);
                login_intent.putExtra("usr_email",signin_usr_email.text);
                login_intent.putExtra("usr_pass",signin_usr_pass.text);

                Timer("Creating",false).schedule(500)
                {
                       startActivity(login_intent);
                }

                Toast.makeText(this,"Registration Successfully done ",Toast.LENGTH_SHORT).show();
            }

            .addOnCanceledListener {
                Toast.makeText(this,"User creation cancelled",Toast.LENGTH_SHORT).show();
                signin_usr_pass.text.clear();
            }

            .addOnFailureListener {

                if(!emailCheck())
                {
                    Toast.makeText(this,"Please enter valid email address",Toast.LENGTH_SHORT).show();
                }
                else if(!passwordCheck())
                {
                    Toast.makeText(this,"Please enter a strong password",Toast.LENGTH_SHORT).show();
                }
                else if(!nameCheck())
                {
                    Toast.makeText(this,"Please enter name with more than 4 character ",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(this,"Sorry there was some problem please try again",Toast.LENGTH_SHORT).show();
                }


            }
    }


//  name checking

    private fun nameCheck():Boolean
    {
        val NAME_PATTERN=Pattern.compile(
            ".{4,}"
        );

        return NAME_PATTERN.matcher(signin_usr_name.text).matches();
    }


//    email checking

    private fun  emailCheck():Boolean
    {
        val EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
        )
        return EMAIL_ADDRESS_PATTERN.matcher(signin_usr_email.text).matches();
    }
// for password checking
    private fun passwordCheck():Boolean
    {
        val PASSWORD_PATTERN=Pattern.compile(
            "^" +
                    "(?=.*[@#$%^&+=])" +     // at least 1 special character
                    "(?=\\S+$)" +            // no white spaces
                    ".{4,}" +                // at least 4 characters
                    "$"
        )

        return PASSWORD_PATTERN.matcher(signin_usr_pass.text).matches();
    }
}