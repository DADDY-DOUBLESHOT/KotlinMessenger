package com.example.chatapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapp.model.ChatMessage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import de.hdodenhof.circleimageview.CircleImageView


class chatlog : AppCompatActivity() {

//      adapter
    lateinit var adapter:GroupieAdapter

    //        User info
    lateinit var username:String
    lateinit var userProfile:String
    lateinit var userUID:String

    //        UI
    lateinit var  sendBTN:Button
    lateinit var  message:EditText
    lateinit var cView:CircleImageView
    lateinit var cUser:TextView

//        firebase

    lateinit var fb_mAuth:FirebaseAuth
    lateinit var fb_DB:FirebaseDatabase

//        intent

    lateinit var msgscreenIntent:Intent
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatlog)

        username=intent.getStringExtra(new_msgscreen.USER_NAME).toString()
        userProfile=intent.getStringExtra(new_msgscreen.USER_PROFILE).toString()
        userUID=intent.getStringExtra(new_msgscreen.USER_UID).toString()


        sendBTN=findViewById<Button>(R.id.chatlog_send_btn)

        message=findViewById<EditText>(R.id.chatlog_message)



        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.setCustomView(R.layout.profile_pic_layout)
        supportActionBar?.setHomeButtonEnabled(true)

        fb_mAuth= FirebaseAuth.getInstance()
        fb_DB= FirebaseDatabase.getInstance("https://kotlinmessenger-901b5-default-rtdb.asia-southeast1.firebasedatabase.app/")


        setBarAndUser()


        adapter= GroupieAdapter()


//        constant listen for new messages
        listenMSG()

//        performing send message on picked contact
        sendBTN.setOnClickListener {
            Log.d("send","Performing send operation")
            sendMessage()
        }

        findViewById<Button>(R.id.actionbar_profile_back_btn).setOnClickListener {

            Log.d("back","Back to msgscreen")
            msgscreenIntent=Intent(this,msgscreen::class.java);
            startActivity(msgscreenIntent);
            finish();

        }





        findViewById<RecyclerView>(R.id.chatlog_recycler_view).adapter=adapter


    }

    private fun setBarAndUser()
    {


        cView=findViewById<CircleImageView>(R.id.actionbar_profile_pic)
        cUser=findViewById<TextView>(R.id.actionbar_profile_username)
        cUser.text=username
        Picasso.get().load(userProfile).placeholder(R.drawable.sample_img).error(R.drawable.profile_pic_round_bg_black).into(cView)


    }









//    testing action bar with new options
//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//
//        menuInflater.inflate(R.menu.profile_pic,menu);
//        return super.onCreateOptionsMenu(menu)
//    }

    private fun listenMSG()
    {
        val ref=fb_DB.getReference("/user-messages/${fb_mAuth.uid}/$userUID")

        ref.addChildEventListener(object:ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {

                val message=snapshot.getValue(ChatMessage::class.java)

                if(message!=null)
                {
                    if(message.fromID==fb_mAuth.uid)
                    {
                        adapter.add(ChatFromItem(message.text))
                    }
                    else
                    {
                        adapter.add(ChatToItem(message.text))
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
        val text=message.text.toString()
        if(fb_mAuth.uid==null)return

        val ref=fb_DB.getReference("/user-messages/${fb_mAuth.uid}/$userUID").push()
        val ref2=fb_DB.getReference("/user-messages/$userUID/${fb_mAuth.uid}").push()
        val latest_msg_ref=fb_DB.getReference("/latest_user-messages/$userUID/${fb_mAuth.uid}")
        val latest_msg_ref2=fb_DB.getReference("/latest_user-messages/${fb_mAuth.uid}/$userUID")

        if(fb_mAuth.uid==null)return

        val chatMessage =ChatMessage(ref.key!!,text,fb_mAuth.uid!!,userUID,System.currentTimeMillis() /1000)

        ref.setValue(chatMessage)
            .addOnSuccessListener {
                Log.d(TAG,"msg sent ref -> ${ref.key}  $it")
            }
        ref2.setValue(chatMessage)
            .addOnSuccessListener {
                Log.d(TAG,"msg sent ref -> ${ref.key}  $it")
            }
        latest_msg_ref.setValue(chatMessage)
            .addOnSuccessListener {
                Log.d(TAG,"latest message updated -> ${ref.key}  $it")
            }
        latest_msg_ref2.setValue(chatMessage)
            .addOnSuccessListener {
                Log.d(TAG,"latest message updated -> ${ref.key}  $it")
            }



        message.text.clear();
    }

//    dummy checking messages
    private fun dummy()
    {

        adapter.add(ChatFromItem("demotext from "))
        adapter.add(ChatToItem("demotext to"))
        adapter.add(ChatFromItem("demotext from "))
        adapter.add(ChatToItem("demotext to"))
        adapter.add(ChatFromItem("demotext from "))
        adapter.add(ChatToItem("demotext to"))
        adapter.add(ChatFromItem("demotext from "))
        adapter.add(ChatToItem("demotext to"))

    }


    companion object{
        val TAG="chatlog"
    }
}


//      Binding values to layout
class ChatFromItem(private val text:String):Item<GroupieViewHolder>()
{
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.findViewById<TextView>(R.id.chatlog_from_msg).text=text
    }

    override fun getLayout(): Int {
       return R.layout.chatlog_from_layout
    }
}
class ChatToItem(private val text:String):Item<GroupieViewHolder>()
{
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        viewHolder.itemView.findViewById<TextView>(R.id.chatlog_to_msg).text=text

    }

    override fun getLayout(): Int {
        return R.layout.chatlog_to_layout
    }
}

