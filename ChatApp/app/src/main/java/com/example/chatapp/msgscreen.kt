package com.example.chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth

class msgscreen : AppCompatActivity() {


//    intents
    lateinit var signup_intent: Intent;
    lateinit var login_intent:Intent;
    lateinit var mscreen_intent:Intent;
    lateinit var nmsg_intent:Intent;

//    Firebase

    lateinit var fb_mAuth:FirebaseAuth;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_msgscreen)

        fb_mAuth= FirebaseAuth.getInstance();

        validateUser();



    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId)
        {
            R.id.mscreen_newmsg-> {
                nmsg_intent=Intent(this,new_msgscreen::class.java);
                startActivity(nmsg_intent);

            }
            R.id.mscreen_exit->{
                fb_mAuth.signOut();
                login_intent=Intent(this,login::class.java);
                login_intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(login_intent);

            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.msgscreen_menu,menu);
        return super.onCreateOptionsMenu(menu)

    }

    private fun validateUser()
    {
        if(fb_mAuth.currentUser==null)
        {
            Log.d("msgscreen","here now");
            signup_intent=Intent(this,signup_intent::class.java);
            signup_intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(signup_intent);

        }
        else return;
    }
}