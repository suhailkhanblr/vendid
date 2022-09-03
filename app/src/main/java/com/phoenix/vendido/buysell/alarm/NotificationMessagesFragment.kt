package com.phoenix.vendido.buysell.alarm

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.phoenix.vendido.buysell.R
import com.phoenix.vendido.buysell.fragments.BylancerBuilderFragment
import com.phoenix.vendido.buysell.utils.LanguagePack
import com.phoenix.vendido.buysell.utils.SessionState
import com.phoenix.vendido.buysell.utils.Utility
import com.phoenix.vendido.buysell.webservices.RetrofitController
import com.phoenix.vendido.buysell.webservices.notificationmessage.NotificationDataModel
import com.gmail.samehadar.iosdialog.IOSDialog
import kotlinx.android.synthetic.main.activity_notification_alarm.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotificationMessagesFragment : BylancerBuilderFragment(), Callback<List<NotificationDataModel>> {
    var iOSDialog: IOSDialog? = null

    override fun setLayoutView() = R.layout.activity_notification_alarm

    override fun initialize(savedInstanceState: Bundle?) {
        iOSDialog = Utility.showProgressView(context!!, LanguagePack.getString("Loading..."))
        notification_alarm_title_text_view.text = LanguagePack.getString(getString(R.string.notification))

        recycler_view_notification_alarm_message_list.setHasFixedSize(false)
        recycler_view_notification_alarm_message_list.layoutManager = LinearLayoutManager(context)
        fetchNotificationList()
    }

    private fun fetchNotificationList() {
        iOSDialog?.show()
        RetrofitController.getNotificationMessage(SessionState.instance.userId, this)
    }

    override fun onFailure(call: Call<List<NotificationDataModel>>?, t: Throwable?) {
        removeProgressBar()
        if (context != null) {
            if (!Utility.isNetworkAvailable(context!!)) {
                Utility.showSnackBar(notification_alarm_parent_layout, getString(R.string.internet_issue), context!!)
            } else {
                Utility.showSnackBar(notification_alarm_parent_layout, getString(R.string.chat_issue), context!!)
            }
        }
    }

    override fun onResponse(call: Call<List<NotificationDataModel>>?, response: Response<List<NotificationDataModel>>?) {
        removeProgressBar()
        if (context != null) {
            if (response != null && response.isSuccessful && recycler_view_notification_alarm_message_list != null) {
                if (response.body().isNullOrEmpty()) {
                    Utility.showSnackBar(notification_alarm_parent_layout, getString(R.string.no_notification), context!!)
                } else {
                    recycler_view_notification_alarm_message_list.adapter =
                            NotificationMessageAdapter(this@NotificationMessagesFragment, response.body()!!)
                }
            } else {
                Utility.showSnackBar(notification_alarm_parent_layout, getString(R.string.some_wrong), context!!)
            }
        }
    }

    private fun removeProgressBar() {
        if (iOSDialog != null) {
            iOSDialog?.dismiss()
        }
    }
}
