package com.example.realtimechat.core.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.realtimechat.R
import com.example.realtimechat.core.shared_preference.RealTimeChatSharedPreference
import com.example.realtimechat.features.chat.data.model.AddRequestStatus
import com.example.realtimechat.features.chat.domain.repository.request.AddRequestRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AddRequestBroadCastReceiver : BroadcastReceiver(), KoinComponent {
    val repository by inject<AddRequestRepository>()
    val sharedPreference by inject<RealTimeChatSharedPreference>()
    val job = CoroutineScope(SupervisorJob())
    override fun onReceive(p0: Context, p1: Intent) {
        val receiverEmail = sharedPreference.getEmail()
        val channelUId = sharedPreference.getChannelUid()
        if (receiverEmail == null || channelUId == null ) return
        when (p1.action) {
            p0.getString(R.string.Accept) -> {
                job.launch {
                    repository.addRequestStatus(
                        receiverEmail,
                        AddRequestStatus.ACCEPTED,
                        channelUId
                    )
                }

            }
            p0.getString(R.string.Reject) -> {
                job.launch {
                    repository.addRequestStatus(
                        receiverEmail,
                        AddRequestStatus.REJECTED,
                        channelUId
                    )
                }
            }
        }
    }

}