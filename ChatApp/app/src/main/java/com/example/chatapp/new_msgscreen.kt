package com.example.chatapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class new_msgscreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_msgscreen)
        supportActionBar?.title="Select Recipient";
    }
}