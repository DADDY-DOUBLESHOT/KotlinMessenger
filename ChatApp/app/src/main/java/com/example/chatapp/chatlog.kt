package com.example.chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item




class chatlog : AppCompatActivity() {

//      adapter
    lateinit var adapter:GroupieAdapter;

//        User info
    lateinit var username:String;
    lateinit var userProfile:String;
    lateinit var userUID:String;

//        UI
    lateinit var  sendBTN:Button;
    lateinit var  message:EditText;

//        firebase

    lateinit var fb_mAuth:FirebaseAuth;
    lateinit var fb_DB:FirebaseDatabase;



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatlog)

        username=intent.getStringExtra(new_msgscreen.USER_NAME).toString();
        userProfile=intent.getStringExtra(new_msgscreen.USER_PROFILE).toString();
        userUID=intent.getStringExtra(new_msgscreen.USER_UID).toString();
        supportActionBar?.title=username;

        sendBTN=findViewById<Button>(R.id.chatlog_send_btn);
        message=findViewById<EditText>(R.id.chatlog_message);



        fb_mAuth= FirebaseAuth.getInstance();
        fb_DB= FirebaseDatabase.getInstance("https://kotlinmessenger-901b5-default-rtdb.asia-southeast1.firebasedatabase.app/");


        adapter= GroupieAdapter();


        listenMSG()

        sendBTN.setOnClickListener {
            Log.d("send","Performing send operation")
            sendMessage();
        }



        findViewById<RecyclerView>(R.id.chatlog_recycler_view).adapter=adapter;



    }

    private fun listenMSG()
    {
        val ref=fb_DB.getReference("/message")

        ref.addChildEventListener(object:ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {

                val message=snapshot.getValue(ChatMessage::class.java);

                if(message!=null)
                {
                    if(message.fromID==fb_mAuth.uid)
                    {
                        adapter.add(ChatFromItem(message.text.toString()))
                    }
                    else
                    {
                        adapter.add(ChatToItem(message.text.toString()))
                    }
                }


            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
//                TODO("Not yet implemented")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
//                TODO("Not yet implemented")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
//                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
//                TODO("Not yet implemented")
            }

        })
    }
    private fun sendMessage()
    {
        val text=message.text.toString();
        if(fb_mAuth.uid==null)return

        val ref=fb_DB.getReference("/message").push();

        if(fb_mAuth.uid==null)return;

        val chatMessage =ChatMessage(ref.key!!,text,fb_mAuth.uid!!,userUID,System.currentTimeMillis() /1000);

        ref.setValue(chatMessage)
            .addOnSuccessListener {
                Log.d(TAG,"msg sent ref -> ${ref.key}  $it")
            }
    }

//    dummy checking messages
    private fun dummy()
    {

        adapter.add(ChatFromItem("demotext from "));
        adapter.add(ChatToItem("demotext to"));
        adapter.add(ChatFromItem("demotext from "));
        adapter.add(ChatToItem("demotext to"));
        adapter.add(ChatFromItem("demotext from "));
        adapter.add(ChatToItem("demotext to"));
        adapter.add(ChatFromItem("demotext from "));
        adapter.add(ChatToItem("demotext to"));

    }

    class ChatMessage(val id:String,val text:String,val fromID:String,val toID:String,val timestamp:Long)
    {
        constructor():this("","","","",0);
    }
    companion object{
        val TAG="chatlog";
    }
}


//      Binding values to layout
class ChatFromItem(private val text:String):Item<GroupieViewHolder>()
{
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.findViewById<TextView>(R.id.chatlog_from_msg).text=text;
    }

    override fun getLayout(): Int {
       return R.layout.chatlog_from_layout;
    }
}
class ChatToItem(private val text:String):Item<GroupieViewHolder>()
{
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        viewHolder.itemView.findViewById<TextView>(R.id.chatlog_to_msg).text=text;

    }

    override fun getLayout(): Int {
        return R.layout.chatlog_to_layout;
    }
}

@IgnoreExtraProperties
data class UseR(val username: String? = null, val email: String? = null) {
    // Null default values create a no-argument default constructor, which is needed
    // for deserialization from a DataSnapshot.
}