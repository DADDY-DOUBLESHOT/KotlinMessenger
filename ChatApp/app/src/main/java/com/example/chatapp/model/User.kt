package com.example.chatapp.model

import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.parcelize.Parcelize


@IgnoreExtraProperties

@Parcelize
class User(val uid:String?=null,val username:String?=null,val profile_pic :String?=null) :
    Parcelable {
    constructor():this("","","");
}