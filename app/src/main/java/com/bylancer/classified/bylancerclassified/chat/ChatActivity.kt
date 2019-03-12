package com.bylancer.classified.bylancerclassified.chat

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bylancer.classified.bylancerclassified.R
import com.bylancer.classified.bylancerclassified.activities.BylancerBuilderActivity
import com.bylancer.classified.bylancerclassified.utils.AppConstants
import com.bylancer.classified.bylancerclassified.utils.LanguagePack
import com.bylancer.classified.bylancerclassified.utils.SessionState
import com.bylancer.classified.bylancerclassified.utils.Utility
import com.bylancer.classified.bylancerclassified.webservices.RetrofitController
import com.bylancer.classified.bylancerclassified.webservices.chat.ChatMessageAdapter
import com.bylancer.classified.bylancerclassified.webservices.chat.ChatMessageModel
import kotlinx.android.synthetic.main.activity_chat.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatActivity : BylancerBuilderActivity(), View.OnClickListener, Callback<List<ChatMessageModel>> {
    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.chat_back_image_view -> onBackPressed()
            R.id.chat_box_send -> Utility.hideKeyboard(this)
            R.id.chat_box_image_attachment -> Utility.hideKeyboard(this)
        }
    }

    override fun setLayoutView() = R.layout.activity_chat

    override fun initialize(savedInstanceState: Bundle?) {
        val bundle = intent.getBundleExtra(AppConstants.BUNDLE)

        if (bundle != null) {
            chat_title_text_view.text = bundle.getString(AppConstants.CHAT_TITLE, "")
            var chatUser = bundle.getString(AppConstants.CHAT_USER_NAME, "")
            val chatUserImage = AppConstants.IMAGE_URL_SMALL + bundle.getString(AppConstants.CHAT_USER_IMAGE, "")
            Glide.with(this).load(chatUserImage).apply(RequestOptions().circleCrop()).into(chat_user_image_view)

            val linearLayoutManager = LinearLayoutManager(this)
            linearLayoutManager.reverseLayout = true
            recycler_view_chat_message_list.layoutManager = linearLayoutManager
            recycler_view_chat_message_list.setHasFixedSize(false)
            getChatMessages(chatUser)
        }
    }

    private fun getChatMessages(chatUser: String) {
        chat_sliding_progress_indicator.visibility = View.VISIBLE
        RetrofitController.fetchChatMessages(SessionState.instance.userName,
                chatUser, "1", this)
        /*RetrofitController.fetchChatMessages("Demo",
                "dev", "1", this)*/
    }

    override fun onFailure(call: Call<List<ChatMessageModel>>?, t: Throwable?) {
        Utility.removeProgressBar(chat_sliding_progress_indicator)
        Utility.showSnackBar(chat_parent_layout, LanguagePack.getString(getString(R.string.internet_issue)), this@ChatActivity)
    }

    override fun onResponse(call: Call<List<ChatMessageModel>>?, response: Response<List<ChatMessageModel>>?) {
        Utility.removeProgressBar(chat_sliding_progress_indicator)
        if(response != null && response.isSuccessful && recycler_view_chat_message_list != null) {
            recycler_view_chat_message_list.adapter = ChatMessageAdapter(response.body())
        } else  {
            Utility.showSnackBar(chat_parent_layout, LanguagePack.getString(getString(R.string.some_wrong)), this@ChatActivity)
        }
    }
}
