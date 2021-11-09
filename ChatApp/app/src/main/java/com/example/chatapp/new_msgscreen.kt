package com.example.chatapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import de.hdodenhof.circleimageview.CircleImageView


class new_msgscreen : AppCompatActivity() {

    lateinit var fb_dbref:FirebaseFirestore;
    lateinit var adapter: GroupieAdapter;
    lateinit var nmsg_intent:Intent;

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_msgscreen)
        supportActionBar?.title = "Select Recipient";

        fb_dbref= FirebaseFirestore.getInstance();
        adapter=GroupieAdapter();

        fetchUsers();

        adapter.setOnItemClickListener { item, view ->

            val user= item as UserItem;
            Log.d("user","${user.username}");
            nmsg_intent=Intent(this,chatlog::class.java);
            nmsg_intent.putExtra(USER_NAME,user.username);
            nmsg_intent.putExtra(USER_UID,user.uid);
            nmsg_intent.putExtra(USER_PROFILE,user.profileUrl);
            startActivity(nmsg_intent);
            finish();
        }
        findViewById<RecyclerView>(R.id.recycler_view).adapter=adapter;



    }

    companion object{
        val USER_UID="USER_UID";
        val USER_PROFILE="USER_PROFILE";
        val USER_NAME="USER_NAME";
    }

    private fun fetchUsers()
    {
        fb_dbref.collection("users").get()
            .addOnSuccessListener { documents->

                for(document in documents)
                {
                    if(document!=null)
                    {
                        adapter.add(UserItem(document.data.get("uid").toString(),document.data.get("username").toString(),document.data.get("profile_pic").toString()));
                    }
                }
            }


    }


}
    // deprecated version use

//    private fun fetchUser() {
//        val ref = FirebaseDatabase.getInstance().getReference("/users")
//        val adapter = GroupieAdapter();
//
//        val ref2=FirebaseFirestore.getInstance();
//        ref2.addSnapshotsInSyncListener { object :ValueEventListener{
//
//            override fun onDataChange(snapshot: DataSnapshot) {
//                snapshot.children.forEach {
//                    val user=it.getValue(User::class.java);
//                    if(user!=null)
//                    {
//                        adapter.add(UserItem(user));
//                        Log.d("user","${it.toString()}");
//                    }
//
//                }
//                findViewById<RecyclerView>(R.id.recycler_view).adapter = adapter;
//
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//
//
//            }
//
//
//        }
//
//        }
//        ref.addListenerForSingleValueEvent(object:ValueEventListener{
//            override fun onDataChange(snapshot: DataSnapshot) {
//                snapshot.children.forEach {
//                    val user=it.getValue(User::class.java);
//                    if(user!=null)
//                    {
//                        adapter.add(UserItem(user));
//                    }
//
//                }
//                findViewById<RecyclerView>(R.id.recycler_view).adapter = adapter;
//
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//
//
//            }
//        })

class UserItem(val uid:String,val username:String,val profileUrl:String) :Item<GroupieViewHolder>() {

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
//        TODO("Not yet implemented")
        viewHolder.itemView.findViewById<TextView>(R.id.nmsg_view_usrname).text=username;
        Picasso.get().load(profileUrl).into(viewHolder.itemView.findViewById<CircleImageView>(R.id.nmsg_view_profile));
    }

    override fun getLayout(): Int {
//        TODO("Not yet implemented")
        return R.layout.nmsg_view_layout;
    }

}