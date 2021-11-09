package com.example.chatapp

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Icon
import android.media.Image
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.versionedparcelable.VersionedParcelize
import com.example.chatapp.R.id.signup_usr_img
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storageMetadata
import kotlinx.parcelize.Parcelize
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
    lateinit var  profile_pic_uri: Uri;
    lateinit var  profile_pic_url: String;

    lateinit var login_intent:Intent;
    lateinit var signup_img_intent:Intent;
    lateinit var msgscreen_intent:Intent;


    private lateinit var fb_mAuth :FirebaseAuth;
    private lateinit var  fb_profile_image:FirebaseStorage;
    private lateinit var fb_userDB:FirebaseFirestore;

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
        signup_image_btn=findViewById(signup_usr_img);

        profile_pic_uri= Uri.EMPTY;
//        profile_pic_url="";



//        firebase
//              authentication
        fb_mAuth= FirebaseAuth.getInstance();
//              profile-image
        fb_profile_image= FirebaseStorage.getInstance();
//                user-data
        fb_userDB= FirebaseFirestore.getInstance();

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

// profile selection will be done here
    @SuppressLint("ResourceType")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if(requestCode==0&&resultCode==Activity.RESULT_OK&&data!=null)
        {

            profile_pic_uri=data.data!!;
            val ic:Icon?= Icon.createWithContentUri(profile_pic_uri);
            signup_image_btn.setImageIcon(ic);
            Toast.makeText(this,"Image Selected ",Toast.LENGTH_SHORT).show();

        }

    }




//    firebase activities

//    user-creation
    private fun firebaseCreate()
    {
        fb_mAuth.createUserWithEmailAndPassword(signin_usr_email.text.toString(),signin_usr_pass.text.toString())
            .addOnCompleteListener{

                msgscreen_intent= Intent(this,login::class.java);


                Timer("Creating",false).schedule(500)
                {
                       startActivity(msgscreen_intent);
                }

                Toast.makeText(this,"Registration Successfully done ",Toast.LENGTH_SHORT).show();

            }
            .addOnSuccessListener {


//              profile-pic upload to server
                FirebaseProfileUpload();






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

        if(profile_pic_uri==null)return;

        val filename=filenameCreate();

        fb_profile_image.getReference("/profile_pics/$filename").putFile(profile_pic_uri!!)
            .addOnCompleteListener {


                fb_profile_image.getReference("/profile_pics/$filename").downloadUrl.addOnSuccessListener {

                    profile_pic_url=it.toString();
                    Toast.makeText(this,"profile pic url : $profile_pic_url",Toast.LENGTH_SHORT).show();
                    Log.d("profile","profile pic url : $profile_pic_url")

                        userDBUpload();
                    }

                //                user db creation in server


            }

    }
//      user-databse-save
    private fun userDBUpload()
    {

//        if(profile_pic_url==null)return;
        val userid=fb_mAuth.uid;


        val uniqueid=signin_usr_name.text.toString()+"_"+filenameCreate();
        val user =User(userid,signin_usr_name.text.toString(),profile_pic_url);
//        val user= hashMapOf(
//            "uid" to userid.toString(),
//            "username" to signin_usr_name.text.toString(),
//            "profileURL" to profile_pic_url
//        );
        fb_userDB.collection("users").document(uniqueid)
            .set(user)
            .addOnSuccessListener {
                Toast.makeText(this,"user account created ",Toast.LENGTH_SHORT).show();
            }
            .addOnFailureListener {
                Toast.makeText(this,"Error creating user ",Toast.LENGTH_SHORT).show();
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

//    filename generator
    private fun filenameCreate():String
    {
            return UUID.randomUUID().toString();
    }
}

@IgnoreExtraProperties

@Parcelize
class User(val uid:String?=null,val username:String?=null,val profile_pic :String?=null) :Parcelable{
    constructor():this("","","");
}