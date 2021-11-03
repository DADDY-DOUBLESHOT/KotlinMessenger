package com.example.chatapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Adapter
import androidx.recyclerview.widget.RecyclerView
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item




class new_msgscreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_msgscreen)
        supportActionBar?.title="Select Recipient";

        val adapter = GroupieAdapter();
        adapter.add(UserItem());
        adapter.add(UserItem());
        adapter.add(UserItem());

        findViewById<RecyclerView>(R.id.recycler_view).adapter=adapter;







    }
}

class UserItem() :Item<GroupieViewHolder>() {




    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getLayout(): Int {
        TODO("Not yet implemented")
        return R.layout.nmsg_view_layout;
    }

}