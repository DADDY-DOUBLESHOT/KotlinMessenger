package com.example.chatapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.RecyclerView
//import com.example.chatapp.new_msgscreen.Companion.USER_KEY
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item




class chatlog : AppCompatActivity() {


    lateinit var adapter:GroupieAdapter;
    lateinit var bundle: Bundle;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatlog)

//        bundle= intent.extras!!;
//        supportActionBar?.title=bundle.getString(USER_KEY);
        adapter= GroupieAdapter();
        adapter.add(ChatFromItem());
        adapter.add(ChatToItem());
        adapter.add(ChatFromItem());
        adapter.add(ChatToItem());
        adapter.add(ChatFromItem());
        adapter.add(ChatToItem());
        adapter.add(ChatFromItem());
        adapter.add(ChatToItem());

        findViewById<RecyclerView>(R.id.chatlog_recycler_view).adapter=adapter;



    }

}

class ChatFromItem():Item<GroupieViewHolder>()
{
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

    }

    override fun getLayout(): Int {
       return R.layout.chatlog_from_layout;
    }
}
class ChatToItem():Item<GroupieViewHolder>()
{
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

    }

    override fun getLayout(): Int {
        return R.layout.chatlog_to_layout;
    }
}