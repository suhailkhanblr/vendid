package com.bylancer.classified.bylancerclassified.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import com.bylancer.classified.bylancerclassified.R
import com.bylancer.classified.bylancerclassified.activities.BylancerBuilderActivity
import com.bylancer.classified.bylancerclassified.dashboard.DashboardActivity
import com.bylancer.classified.bylancerclassified.utils.AppConstants
import com.bylancer.classified.bylancerclassified.utils.SessionState
import com.bylancer.classified.bylancerclassified.utils.Utility
import com.bylancer.classified.bylancerclassified.webservices.RetrofitController
import com.bylancer.classified.bylancerclassified.webservices.login.UserLoginData
import com.bylancer.classified.bylancerclassified.webservices.login.UserLoginStatus
import kotlinx.android.synthetic.main.activity_manual_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ManualLoginActivity : BylancerBuilderActivity(), View.OnFocusChangeListener , TextWatcher, View.OnClickListener,
        Callback<UserLoginStatus> {

    override fun setLayoutView() = R.layout.activity_manual_login

    override fun initialize(savedInstanceState: Bundle?) {
        login_email_id_edit_text.addTextChangedListener(this)
        login_password_edit_text.addTextChangedListener(this)
        login_password_edit_text.setOnFocusChangeListener(this)
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun afterTextChanged(p0: Editable?) {
        login_button.isEnabled = Utility.hasText(login_email_id_edit_text) && Utility.hasText(login_password_edit_text)
    }

    override fun onFocusChange(view: View?, hasFocus: Boolean) {
        if(hasFocus) {
            login_password_frame.setBackgroundResource(R.drawable.rounded_edittext_focussed)
        } else {
            login_password_frame.setBackgroundResource(R.drawable.rounded_edittext_normal)
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.show_hide_password_text_view -> {
                if (getString(R.string.show).equals(show_hide_password_text_view.text)) {
                    show_hide_password_text_view.text = getString(R.string.hide)
                    login_password_edit_text.setTransformationMethod(HideReturnsTransformationMethod.getInstance())
                } else {
                    show_hide_password_text_view.text = getString(R.string.show)
                    login_password_edit_text.setTransformationMethod(PasswordTransformationMethod.getInstance())
                }
                login_password_edit_text.setSelection(login_password_edit_text.text.length)
            }

            R.id.manual_login_screen_back -> onBackPressed()

            R.id.login_forget_password_text_view -> startActivity(ForgotPasswordActivity::class.java,false)

            R.id.login_button -> loginUser()
        }
    }

    fun loginUser() {
        Utility.hideKeyboard(this)
        login_sliding_progress_indicator.visibility = View.VISIBLE
        val userLoginData = UserLoginData()
        userLoginData.email = login_email_id_edit_text.text.toString()
        userLoginData.username = login_email_id_edit_text.text.toString()
        userLoginData.password = login_password_edit_text.text.toString()

        if (login_email_id_edit_text.text.contains("@")) {
            RetrofitController.loginUserUsingEmail(userLoginData, this)
        } else {
            RetrofitController.loginUserUsingUsername(userLoginData, this)
        }
    }

    override fun onFailure(call: Call<UserLoginStatus>?, t: Throwable?) {
        login_sliding_progress_indicator.visibility = View.GONE
        Utility.showSnackBar(activity_login_user_parent_layout, "Please check your internet connection", this)
    }

    override fun onResponse(call: Call<UserLoginStatus>?, response: Response<UserLoginStatus>?) {
        login_sliding_progress_indicator.visibility = View.GONE

        if (response != null && response.isSuccessful) {
            val responseBody: UserLoginStatus = response.body()
            if (responseBody != null && AppConstants.SUCCESS.equals(responseBody.status!!)) {
                SessionState.instance.displayName = responseBody.name!!
                SessionState.instance.userName = responseBody.username!!
                SessionState.instance.email = responseBody.email!!
                SessionState.instance.userId = responseBody.userId!!
                SessionState.instance.isLogin = true

                SessionState.instance.saveValuesToPreferences(this, AppConstants.Companion.PREFERENCES.USERNAME.toString(), responseBody.username!!)
                SessionState.instance.saveValuesToPreferences(this, AppConstants.Companion.PREFERENCES.EMAIL.toString(), responseBody.email!!)
                SessionState.instance.saveValuesToPreferences(this, AppConstants.Companion.PREFERENCES.DISPLAY_NAME.toString(), responseBody.name!!)
                SessionState.instance.saveValuesToPreferences(this, AppConstants.Companion.PREFERENCES.USER_ID.toString(), responseBody.userId!!)
                SessionState.instance.saveBooleanToPreferences(this, AppConstants.Companion.PREFERENCES.LOGIN_STATUS.toString(), true)

                Utility.showSnackBar(activity_login_user_parent_layout, "You have successfully logged in", this)
                startActivity(DashboardActivity::class.java, true)
                finish()
            } else {
                Utility.showSnackBar(activity_login_user_parent_layout, "Username/Email or Password not matched", this)
            }
        } else {
            Utility.showSnackBar(activity_login_user_parent_layout, "Something went wrong, we are working on it to fix it as soon as possible", this)
        }
    }
}
