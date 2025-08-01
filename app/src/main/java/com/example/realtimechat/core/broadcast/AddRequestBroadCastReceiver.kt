package com.example.realtimechat.core.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.realtimechat.R

class AddRequestBroadCastReceiver : BroadcastReceiver(){
    override fun onReceive(p0: Context, p1: Intent) {
        when(p1.action){
            p0.getString(R.string.Accept) -> {

            }
            p0.getString(R.string.Reject) -> {

            }
        }
    }
}