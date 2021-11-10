package com.example.chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.google.firebase.auth.EmailAuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class login : AppCompatActivity()
{
    lateinit var google_account:ImageView;
    lateinit var apple_account:ImageView;
    lateinit var usr_email:EditText;
    lateinit var usr_pass:EditText;
    lateinit var login_btn:Button;
    lateinit var signup_link:TextView;
    lateinit var signup_Intent :Intent;
    lateinit var msgscreen_Intent:Intent;

    lateinit var  fb_mAuth:FirebaseAuth;


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





//        Checking  user already exist
        fb_mAuth= FirebaseAuth.getInstance();
        if(fb_mAuth.currentUser!=null)
        {
            msgscreen_Intent=Intent(this,msgscreen::class.java);
            msgscreen_Intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(msgscreen_Intent);
        }



        google_account.setOnClickListener {

                Toast.makeText(this,"Google Account not linked",Toast.LENGTH_SHORT).show();
        };
        apple_account.setOnClickListener {

            Toast.makeText(this,"Apple Account not linked",Toast.LENGTH_SHORT).show();
        };


        login_btn.setOnClickListener {



            fb_mAuth.signInWithEmailAndPassword(usr_email.text.toString(),usr_pass.text.toString())
                .addOnSuccessListener {
                    Log.d("user","${fb_mAuth.uid}")
                    msgscreen_Intent=Intent(this,msgscreen::class.java);
                    msgscreen_Intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(msgscreen_Intent);
                }
                .addOnFailureListener {
                    Toast.makeText(this,"Wrong usr",Toast.LENGTH_SHORT).show();
                }

        };
        signup_link.setOnClickListener {

            signup_Intent= Intent(this,signup::class.java);
            signup_Intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(signup_Intent);

        };

    }

    override fun onRestart() {
        super.onRestart()


    }

    override fun onResume() {
        super.onResume()
        if(fb_mAuth.currentUser!=null)
        {
            msgscreen_Intent=Intent(this,msgscreen::class.java);
            msgscreen_Intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(msgscreen_Intent);
        }


    }
}