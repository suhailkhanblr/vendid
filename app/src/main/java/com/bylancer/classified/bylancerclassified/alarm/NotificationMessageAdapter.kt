package com.bylancer.classified.bylancerclassified.alarm

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bylancer.classified.bylancerclassified.R
import com.bylancer.classified.bylancerclassified.activities.BylancerBuilderActivity
import com.bylancer.classified.bylancerclassified.webservices.notificationmessage.NotificationDataModel
import kotlinx.android.synthetic.main.notification_message_adapter.view.*

class NotificationMessageAdapter(val mNotificationMessageListModel: List<NotificationDataModel>): RecyclerView.Adapter<NotificationMessageAdapter.ChatViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.notification_message_adapter, parent, false)
        return ChatViewHolder(view)
    }

    override fun getItemCount(): Int {
        if (mNotificationMessageListModel != null) return mNotificationMessageListModel.size else return 0
    }

    override fun onBindViewHolder(chatViewHolder: ChatViewHolder, position: Int) {
        val notificationMessageModel = mNotificationMessageListModel.get(position)
        chatViewHolder.notificationMessage.text = notificationMessageModel.message
        chatViewHolder.notificationMessageSenderName.text = notificationMessageModel.senderName
    }

    class ChatViewHolder(view: View): RecyclerView.ViewHolder(view) {
        internal var notificationMessage = view.notification_message_text_view
        internal var notificationMessageSenderName = view.notification_sender_name
    }
}