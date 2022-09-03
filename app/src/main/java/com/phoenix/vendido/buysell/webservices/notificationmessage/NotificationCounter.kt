package com.phoenix.vendido.buysell.webservices.notificationmessage

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class NotificationCounter {

    @SerializedName("unread_notification")
    @Expose
    var unreadNotification: String? = null
    @SerializedName("unread_chat")
    @Expose
    var unreadChat: String? = null
}
