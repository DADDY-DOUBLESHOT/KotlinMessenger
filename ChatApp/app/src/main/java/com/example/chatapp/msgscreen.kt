package com.example.chatapp

import android.content.AsyncQueryHandler
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.widget.Adapter
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapp.model.ChatMessage
import com.example.chatapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.okhttp.Dispatcher
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import de.hdodenhof.circleimageview.CircleImageView

class msgscreen : AppCompatActivity() {


    //    intents
    lateinit var signup_intent: Intent;
    lateinit var login_intent: Intent;
    lateinit var nmsg_intent: Intent;

//    Firebase

    lateinit var fb_mAuth: FirebaseAuth;
    lateinit var fb_RelDB: FirebaseDatabase;
    lateinit var fb_UserDB: FirebaseFirestore;

    //    Adapter
    lateinit var adapter: GroupieAdapter;

    //    hashmap for messages
    lateinit var hashMap: HashMap<String, ChatMessage>




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_msgscreen)

        hashMap = HashMap<String, ChatMessage>()
        fb_mAuth = FirebaseAuth.getInstance();
        fb_RelDB =
            FirebaseDatabase.getInstance("https://kotlinmessenger-901b5-default-rtdb.asia-southeast1.firebasedatabase.app/");
        fb_UserDB = FirebaseFirestore.getInstance();
        validateUser();



        adapter = GroupieAdapter();


        refresh();
        findViewById<RecyclerView>(R.id.msgscreen_recycler_view).adapter = adapter;
        findViewById<RecyclerView>(R.id.msgscreen_recycler_view).addItemDecoration(
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        )
//    listening for selected user

        adapter.setOnItemClickListener { item, view ->

            val userid= item as Messages;
            val ref=fb_UserDB.collection("users").document(userid.chatPartner).get()
                .addOnSuccessListener {
                    Log.d("user","${userid}");
                    nmsg_intent=Intent(this,chatlog::class.java);
                    nmsg_intent.putExtra(new_msgscreen.USER_NAME,it.data?.get("username").toString());
                    nmsg_intent.putExtra(new_msgscreen.USER_UID,userid.chatPartner);
                    nmsg_intent.putExtra(new_msgscreen.USER_PROFILE,it.data?.get("profile_pic").toString());
                    startActivity(nmsg_intent);
                }



        }

    }

//    override fun onResume() {
//        super.onResume()
//        refresh()
//    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.mscreen_newmsg -> {
                nmsg_intent = Intent(this, new_msgscreen::class.java);
                startActivity(nmsg_intent);

            }
            R.id.mscreen_exit -> {
                fb_mAuth.signOut();
                login_intent = Intent(this, login::class.java);
                login_intent.flags =
                    Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(login_intent);

            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.msgscreen_menu, menu);
        return super.onCreateOptionsMenu(menu)

    }


    //    reload last user messages
    private fun refresh() {

        val ref = fb_RelDB.getReference("latest_user-messages");
        ref.child(fb_mAuth.uid.toString()).addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val frnd_uid = snapshot.key
                val msg = snapshot.getValue(ChatMessage::class.java) ?: return;
                hashMap[frnd_uid.toString()] = msg;
                Log.d("users", "${hashMap[frnd_uid]}")
                adapter.add(Messages(msg));
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val frnd_uid = snapshot.key
                val msg = snapshot.getValue(ChatMessage::class.java) ?: return;
                hashMap[frnd_uid.toString()] = msg;
                LoadMessages();
                Log.d("users", "${hashMap[frnd_uid]}")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onCancelled(error: DatabaseError) {

            }


        })


    }

    //    peer to peer user messages

    private fun LoadMessages() {
        adapter.clear();
        hashMap.values.forEach {
            adapter.add(Messages(it));
        }
    }

    private fun validateUser() {
        if (fb_mAuth.currentUser == null) {
            Log.d("msgscreen", "here now");
            signup_intent = Intent(this, signup_intent::class.java);
            signup_intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(signup_intent);

        } else return;
    }

}
    class Messages(val message: ChatMessage) : Item<GroupieViewHolder>() {
        lateinit var chatPartner: String;

        override fun bind(viewHolder: GroupieViewHolder, position: Int) {
            val partnerid: String;
            if (message.fromID == FirebaseAuth.getInstance().uid) {
                partnerid = message.toID;
            } else {
                partnerid = message.fromID;
            }

            FirebaseFirestore.getInstance().collection("users").document(partnerid).get()
                .addOnSuccessListener {
                        chatPartner= it.data?.get("uid").toString();
                    Picasso.get().load(it.data?.get("profile_pic").toString())
                        .error(R.drawable.sample_img)
                        .into(viewHolder.itemView.findViewById<CircleImageView>(R.id.msgscreen_profile_pic));
                    viewHolder.itemView.findViewById<TextView>(R.id.msgcreen_username).text =
                        it.data?.get("username").toString();
                }


            viewHolder.itemView.findViewById<TextView>(R.id.msgcreen_latest_msg).text =
                message.text;
        }

        override fun getLayout(): Int {
            return R.layout.msgscreen_profile_layout;
        }


    }
