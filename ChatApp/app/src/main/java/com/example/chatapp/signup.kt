package com.example.chatapp

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import java.net.URI
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


    lateinit var signup_image_btn:ImageView;
    lateinit var profile_pic_uri:Uri;

    lateinit var login_intent:Intent;
    lateinit var signup_img_intent:Intent;


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
        signup_image_btn=findViewById(R.id.signup_usr_img);



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
                firebaseCreate();
            }


        };


//backbtn listeners

        signin_bck_btn.setOnClickListener{

            login_intent= Intent(this,login::class.java);
            startActivity(login_intent);
        };

        login_link.setOnClickListener {
            login_intent= Intent(this,login::class.java);
            startActivity(login_intent);
        };

//    signin img listeners

       signup_image_btn.setOnClickListener {

           signup_img_intent = Intent(Intent.ACTION_PICK);
           signup_img_intent.type="image/*";

           startActivityForResult(signup_img_intent,0)

       }



    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode==0&&resultCode==Activity.RESULT_OK&&data!=null)
        {
            val profile_pic_uri=data.data;
            val bitmap=MediaStore.Images.Media.getBitmap(contentResolver,profile_pic_uri);

            val bitmapDrawable=BitmapDrawable(bitmap);

            signup_image_btn.setBackgroundDrawable(bitmapDrawable);

            Toast.makeText(this,"Image upload stage 1",Toast.LENGTH_SHORT).show();
        }
    }




//    firebase activities

//    user-creation
    private fun firebaseCreate()
    {
        mAuth.createUserWithEmailAndPassword(signin_usr_email.text.toString(),signin_usr_pass.text.toString())
            .addOnCompleteListener{

                login_intent= Intent(this,login::class.java);
                login_intent.putExtra("usr_email",signin_usr_email.text);
                login_intent.putExtra("usr_pass",signin_usr_pass.text);
                FirebaseProfileUpload();

                Timer("Creating",false).schedule(500)
                {
                       startActivity(login_intent);
                }

//                Toast.makeText(this,"Registration Successfully done ",Toast.LENGTH_SHORT).show();

            }

            .addOnCanceledListener {
                Toast.makeText(this,"User creation cancelled",Toast.LENGTH_SHORT).show();
                signin_usr_pass.text.clear();
            }

            .addOnFailureListener {



                    Toast.makeText(this,"Sorry there was some problem please try again",Toast.LENGTH_SHORT).show();



            }
    }
//      profile-pic upload

    private fun FirebaseProfileUpload()
    {
        if(profile_pic_uri==null)return ;

        val filename=UUID.randomUUID().toString();
        val ref=FirebaseStorage.getInstance().getReference("/images/$filename")

        ref.putFile(profile_pic_uri!!)
            .addOnSuccessListener {
                Toast.makeText(this,"Image uploaded",Toast.LENGTH_SHORT).show();
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

