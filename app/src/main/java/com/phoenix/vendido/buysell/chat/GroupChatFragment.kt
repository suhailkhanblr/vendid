package com.phoenix.vendido.buysell.chat

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.phoenix.vendido.buysell.R
import com.phoenix.vendido.buysell.dashboard.DashboardActivity
import com.phoenix.vendido.buysell.fragments.BylancerBuilderFragment
import com.phoenix.vendido.buysell.utils.LanguagePack
import com.phoenix.vendido.buysell.utils.SessionState
import com.phoenix.vendido.buysell.utils.Utility
import com.phoenix.vendido.buysell.webservices.RetrofitController
import com.phoenix.vendido.buysell.webservices.chat.GroupChatModel
import com.gmail.samehadar.iosdialog.IOSDialog
import kotlinx.android.synthetic.main.activity_group_chat.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GroupChatFragment : BylancerBuilderFragment(), Callback<List<GroupChatModel>> {
    var iOSDialog: IOSDialog? = null

    override fun setLayoutView() = R.layout.activity_group_chat

    override fun initialize(savedInstanceState: Bundle?) {
        iOSDialog = Utility.showProgressView(context!!, LanguagePack.getString("Loading..."))
        group_chat_title_text_view.text = LanguagePack.getString(getString(R.string.my_chats))

        recycler_view_group_chat_message_list.setHasFixedSize(false)
        recycler_view_group_chat_message_list.layoutManager = LinearLayoutManager(context!!)
        fetchGroupChatList()
    }

    private fun fetchGroupChatList() {
        iOSDialog?.show()
        RetrofitController.fetchGroupChatMessages(SessionState.instance.userId, this)
    }

    override fun onFailure(call: Call<List<GroupChatModel>>?, t: Throwable?) {
        removeProgressBar()
        if (!Utility.isNetworkAvailable(context!!)) {
            Utility.showSnackBar(group_chat_parent_layout, getString(R.string.internet_issue), context!!)
        } else {
            Utility.showSnackBar(group_chat_parent_layout, getString(R.string.chat_issue), context!!)
        }
    }

    override fun onResponse(call: Call<List<GroupChatModel>>?, response: Response<List<GroupChatModel>>?) {
        removeProgressBar()
        if (response != null && response.isSuccessful && recycler_view_group_chat_message_list != null && response.body() != null) {
            recycler_view_group_chat_message_list.adapter = GroupMessageAdapter(response.body()!!, activity as DashboardActivity)
        } else {
            Utility.showSnackBar(group_chat_parent_layout, getString(R.string.some_wrong), context!!)
        }
    }

    private fun removeProgressBar() {
        if (iOSDialog != null) {
            iOSDialog?.dismiss()
        }
    }
}
