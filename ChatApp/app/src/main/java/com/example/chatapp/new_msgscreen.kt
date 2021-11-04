package com.example.chatapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item


class new_msgscreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_msgscreen)
        supportActionBar?.title = "Select Recipient";

        fetchUser();


    }


    private fun fetchUser() {
        val ref = FirebaseDatabase.getInstance().getReference("/users")
        val adapter = GroupieAdapter();

//        val ref2=FirebaseFirestore.getInstance();
//        ref2.addSnapshotsInSyncListener { object :ValueEventListener{
//
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
//
//
//        }

//        }
        ref.addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    val user=it.getValue(User::class.java);
                    if(user!=null)
                    {
                        adapter.add(UserItem(user));
                    }

                }
                findViewById<RecyclerView>(R.id.recycler_view).adapter = adapter;

            }

            override fun onCancelled(error: DatabaseError) {


            }
        })



    }
}
class UserItem(private val user:User) :Item<GroupieViewHolder>() {

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
//        TODO("Not yet implemented")

        viewHolder.itemView.findViewById<TextView>(R.id.nmsg_view_usrname).text=user.username;
    }

    override fun getLayout(): Int {
//        TODO("Not yet implemented")
        return R.layout.nmsg_view_layout;
    }

}