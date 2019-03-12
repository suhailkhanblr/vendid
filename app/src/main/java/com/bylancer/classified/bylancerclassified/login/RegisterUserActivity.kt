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
import com.bylancer.classified.bylancerclassified.utils.LanguagePack
import com.bylancer.classified.bylancerclassified.utils.SessionState
import com.bylancer.classified.bylancerclassified.utils.Utility
import com.bylancer.classified.bylancerclassified.webservices.RetrofitController
import com.bylancer.classified.bylancerclassified.webservices.registration.UserRegistrationData
import com.bylancer.classified.bylancerclassified.webservices.registration.UserRegistrationStatus
import kotlinx.android.synthetic.main.activity_register_user.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterUserActivity : BylancerBuilderActivity(), View.OnFocusChangeListener , TextWatcher, View.OnClickListener, Callback<UserRegistrationStatus> {

    override fun setLayoutView() = R.layout.activity_register_user

    override fun initialize(savedInstanceState: Bundle?) {
        register_name_edit_text.addTextChangedListener(this)
        register_email_id_edit_text.addTextChangedListener(this)
        register_username_edit_text.addTextChangedListener(this)
        register_user_password_edit_text.addTextChangedListener(this)
        register_user_password_edit_text.setOnFocusChangeListener(this)
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun afterTextChanged(p0: Editable?) {
        register_user_button.isEnabled = Utility.hasText(register_name_edit_text) && Utility.hasText(register_email_id_edit_text)
                && Utility.hasText(register_user_password_edit_text) && Utility.hasText(register_username_edit_text)

    }

    override fun onFocusChange(view: View?, hasFocus: Boolean) {
        if(hasFocus) {
            register_user_password_frame.setBackgroundResource(R.drawable.rounded_edittext_focussed)
        } else {
            register_user_password_frame.setBackgroundResource(R.drawable.rounded_edittext_normal)
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.register_user_show_hide_password_text_view -> {
                if (getString(R.string.show).equals(register_user_show_hide_password_text_view.text)) {
                    register_user_show_hide_password_text_view.text = getString(R.string.hide)
                    register_user_password_edit_text.setTransformationMethod(HideReturnsTransformationMethod.getInstance())
                } else {
                    register_user_show_hide_password_text_view.text = getString(R.string.show)
                    register_user_password_edit_text.setTransformationMethod(PasswordTransformationMethod.getInstance())
                }
                register_user_password_edit_text.setSelection(register_user_password_edit_text.text.length)
            }

            R.id.register_user_screen_back -> onBackPressed()

            R.id.register_user_button -> {
                if (register_name_edit_text.text.toString().length < 2) {
                    Utility.showSnackBar(activity_register_user_parent_layout, "Please enter your full name", this)
                } else if (register_username_edit_text.text.toString().length < 6) {
                    Utility.showSnackBar(activity_register_user_parent_layout, "Please enter minimum 6 characters of Username", this)
                } else if (!Utility.isValidAlphaNumeric(register_username_edit_text.text.toString())) {
                    Utility.showSnackBar(activity_register_user_parent_layout, "Username can contain only Alphanumeric characters", this)
                } else if (!Utility.isValidEmail(register_email_id_edit_text.text.toString())) {
                    Utility.showSnackBar(activity_register_user_parent_layout, "Please enter correct Email id", this)
                } else if (register_user_password_edit_text.text.toString().length < 6) {
                    Utility.showSnackBar(activity_register_user_parent_layout, "Please enter minimum 6 characters of Password", this)
                } else {
                    registerUser()
                }
            }
        }
    }

    private fun registerUser() {
        Utility.hideKeyboard(this)
        register_user_sliding_progress_indicator.visibility = View.VISIBLE

        val userData = UserRegistrationData()
        userData.email = register_email_id_edit_text.text.toString()
        userData.username = register_username_edit_text.text.toString()
        userData.name = register_name_edit_text.text.toString()
        userData.password = register_user_password_edit_text.text.toString()
        userData.fbLogin = ""

        RetrofitController.registerUser(userData, this)
    }

    override fun onFailure(call: Call<UserRegistrationStatus>?, t: Throwable?) {
        register_user_sliding_progress_indicator.visibility = View.GONE
        Utility.showSnackBar(activity_register_user_parent_layout, LanguagePack.getString("Please check your internet connection"), this)
    }

    override fun onResponse(call: Call<UserRegistrationStatus>?, response: Response<UserRegistrationStatus>?) {
        register_user_sliding_progress_indicator.visibility = View.GONE

        if (response != null && response.isSuccessful) {
            val responseBody: UserRegistrationStatus = response.body()
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

                Utility.showSnackBar(activity_register_user_parent_layout, LanguagePack.getString("You have successfully logged in"), this)
                startActivity(DashboardActivity::class.java, true)
                finish()
            } else {
                Utility.showSnackBar(activity_register_user_parent_layout, LanguagePack.getString("Username or email is already occupied"), this)
            }
        } else {
            Utility.showSnackBar(activity_register_user_parent_layout, LanguagePack.getString("Something went wrong, we are working on it to fix it as soon as possible"), this)
        }
    }
}
