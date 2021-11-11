package com.example.chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Adapter
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapp.model.ChatMessage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import de.hdodenhof.circleimageview.CircleImageView

class msgscreen : AppCompatActivity() {


//    intents
    lateinit var signup_intent: Intent;
    lateinit var login_intent:Intent;
    lateinit var nmsg_intent:Intent;

//    Firebase

    lateinit var fb_mAuth:FirebaseAuth;
    lateinit var fb_RelDB:FirebaseDatabase;
    lateinit var fb_UserDB:FirebaseFirestore;

//    Adapter
    lateinit var adapter:GroupieAdapter;

//    hashmap for messages
    lateinit var hashMap:HashMap<String,ChatMessage>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_msgscreen)

        hashMap=HashMap<String,ChatMessage>()
        fb_mAuth= FirebaseAuth.getInstance();
        fb_RelDB=FirebaseDatabase.getInstance("https://kotlinmessenger-901b5-default-rtdb.asia-southeast1.firebasedatabase.app/");
        fb_UserDB= FirebaseFirestore.getInstance();
        validateUser();
        adapter= GroupieAdapter();
        findViewById<RecyclerView>(R.id.msgscreen_recycler_view).adapter=adapter;
        LoadMessages();
        findViewById<RecyclerView>(R.id.msgscreen_recycler_view).adapter=adapter;



    }

//    override fun onResume() {
//        super.onResume()
//        refresh()
//    }

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

//    peer to peer user messages

    private fun LoadMessages()
    {
        refresh();
        val ref=fb_UserDB.collection("users");
        hashMap.values.forEach {
            ref.document(it.id).get()
                .addOnSuccessListener{ document->
                    adapter.add(Messages(document.data?.get("username").toString(),it.text,document.data?.get("profile_pic").toString()))
                    Log.d("msgscreen","${document.data?.get("username")},${it.text},${document.data?.get("profile_pic")}")
                }

        }
    }

//    reload last user messages
    private fun refresh()
    {
        adapter.clear();
        val ref=fb_RelDB.getReference("/user-messages/${fb_mAuth.uid}");
        ref.addChildEventListener(object:ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                snapshot.children.forEach {
                    val message=it.getValue(ChatMessage::class.java)?:return;
                    hashMap[it.key!!]=message;
                    adapter.add(Messages("",message.text,""))
                }
            }
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
//                TODO("Not yet implemented")
            }
            override fun onCancelled(error: DatabaseError) {
//                TODO("Not yet implemented")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
//                TODO("Not yet implemented")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
//                TODO("Not yet implemented")
            }
        })
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

class Messages(private val usrname:String,private val lastmsg:String,private val profileUrl:String):Item<GroupieViewHolder>()
{
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

//        Picasso.get().load(profileUrl).error(R.drawable.sample_img).into(viewHolder.itemView.findViewById<CircleImageView>(R.id.msgscreen_profile_pic));
        viewHolder.itemView.findViewById<TextView>(R.id.msgcreen_username).text=usrname;
        viewHolder.itemView.findViewById<TextView>(R.id.msgcreen_latest_msg).text=lastmsg;
    }

    override fun getLayout(): Int {
        return R.layout.msgscreen_profile_layout;
    }


}
