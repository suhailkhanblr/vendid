package com.phoenix.vendido.buysell.login

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Spannable
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.util.Base64
import android.util.Log
import android.view.View
import com.phoenix.vendido.buysell.R
import com.phoenix.vendido.buysell.activities.BylancerBuilderActivity
import com.phoenix.vendido.buysell.dashboard.DashboardActivity
import com.phoenix.vendido.buysell.utils.*
import com.phoenix.vendido.buysell.webservices.RetrofitController
import com.phoenix.vendido.buysell.webservices.registration.UserRegistrationData
import com.phoenix.vendido.buysell.webservices.registration.UserRegistrationStatus
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*
import kotlin.random.Random.Default.nextInt

class LoginActivity : BylancerBuilderActivity(), View.OnClickListener, Callback<UserRegistrationStatus> {
    private var callbackManager: CallbackManager? = null
    private lateinit var mGoogleSignInClient : GoogleSignInClient
    companion object {
        const val RC_SIGN_IN = 9001
    }

    override fun setLayoutView() = R.layout.activity_login

    override fun initialize(savedInstanceState: Bundle?) {
       // printHashKey()
        if (intent != null && intent.hasExtra(AppConstants.BUNDLE)) {
            intent.getBundleExtra(AppConstants.BUNDLE).getString(AppConstants.MESSAGE)?.let { Utility.showSnackBar(login_screen_parent_layout, it, this) }
        }

        setUpGoogleLogin()

        app_name_login_text_view.text = if (SessionState.instance.appName != null) SessionState.instance.appName else getString(R.string.app_name)
        sign_up_to_continue_text_view.text = LanguagePack.getString(getString(R.string.sign_up_to_continue))
        login_with_email_text_view.text = LanguagePack.getString(getString(R.string.login_with_email))
        sign_up_with_email_text_view.text = LanguagePack.getString(getString(R.string.sign_up_with_email))
        setTermsAndCondition()
    }

    private fun setUpGoogleLogin() {
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)
        google_sign_in_button?.setOnClickListener {
            googleSignIn()
        }
    }

    override fun onClick(view: View?) {
        when(view?.id) {
            R.id.login_close_button_view -> onBackPressed()
            R.id.login_with_email_text_view -> startActivity(ManualLoginActivity::class.java, false)
            R.id.sign_up_with_email_text_view -> startActivity(RegisterUserActivity::class.java, false)
            R.id.login_with_facebook -> loginWithFacebook()
        }
    }

    private fun loginWithFacebook() {
        callbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"))
        LoginManager.getInstance().registerCallback(callbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(loginResult: LoginResult) {
                        val request = GraphRequest.newMeRequest(
                                loginResult.accessToken
                        ) { jsonObject, response ->
                            if (jsonObject != null && response != null) {
                                SessionState.instance.profilePicUrl = "https://graph.facebook.com/" + jsonObject.get("id") + "/picture?type=large"
                                SessionState.instance.saveValuesToPreferences(applicationContext, AppConstants.Companion.PREFERENCES.PROFILE_PIC.toString(), SessionState.instance.profilePicUrl)
                                val email = if (jsonObject.has("email")) jsonObject.getString("email") else ""
                                val userData = UserRegistrationData()
                                 userData.email = email
                                 userData.username = if (!email.isNullOrEmpty()) email.split("@")[0] + nextInt(10000, 1000000).toString() else UUID.randomUUID().toString()
                                 userData.name = if (jsonObject.has("name")) jsonObject.getString("name") else ""
                                 userData.password = UUID.randomUUID().toString()
                                 userData.fbLogin = "1"
                                if (userData.email!!.isNotEmpty()) {
                                    logoutFromFacebook()
                                    registerUser(userData)
                                }
                            }
                        }
                        val parameters = Bundle()
                        parameters.putString("fields", "id,name,email")
                        request.parameters = parameters
                        request.executeAsync()
                    }

                    override fun onCancel() {
                        Utility.showSnackBar(login_screen_parent_layout, LanguagePack.getString("You have cancelled facebook login, please login to continue"), this@LoginActivity)
                    }

                    override fun onError(error: FacebookException) {
                        Utility.showSnackBar(login_screen_parent_layout, LanguagePack.getString(getString(R.string.some_wrong)), this@LoginActivity)
                    }
                })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == Activity.RESULT_OK) {
                var task : Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data);
                handleGoogleSignInResult(task)
            } else {
                Utility.showSnackBar(login_screen_parent_layout, LanguagePack.getString("You have cancelled Google login, please login to continue"), this@LoginActivity)
            }
        } else {
            callbackManager?.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun logoutFromFacebook() {
        Utility.hideKeyboard(this)
        register_user_sliding_progress_indicator.visibility = View.VISIBLE
        if (AccessToken.getCurrentAccessToken() != null) {
            GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, GraphRequest.Callback {
                AccessToken.setCurrentAccessToken(null)
                LoginManager.getInstance().logOut()
            }).executeAsync()
        }
    }

    private fun registerUser(userData: UserRegistrationData) {
        RetrofitController.registerUser(userData, this)
    }

    override fun onFailure(call: Call<UserRegistrationStatus>?, t: Throwable?) {
        Utility.removeProgressBar(register_user_sliding_progress_indicator)
        Utility.showSnackBar(login_screen_parent_layout, LanguagePack.getString(getString(R.string.internet_issue)), this)
    }

    override fun onResponse(call: Call<UserRegistrationStatus>?, response: Response<UserRegistrationStatus>?) {
        Utility.removeProgressBar(register_user_sliding_progress_indicator)

        if (response != null && response.isSuccessful && response.body() != null) {
            val responseBody: UserRegistrationStatus = response.body()!!
            if (responseBody != null && AppConstants.SUCCESS.equals(responseBody.status!!)) {
                SessionState.instance.displayName = responseBody.name!!
                SessionState.instance.userName = responseBody.username!!
                SessionState.instance.email = responseBody.email!!
                SessionState.instance.userId = responseBody.userId!!
                SessionState.instance.profilePicUrl = if (responseBody.profilePicture != null) responseBody.profilePicture!! else ""

                SessionState.instance.saveValuesToPreferences(this, AppConstants.Companion.PREFERENCES.PROFILE_PIC.toString(), SessionState.instance.profilePicUrl)
                SessionState.instance.saveValuesToPreferences(this, AppConstants.Companion.PREFERENCES.USERNAME.toString(), responseBody.username!!)
                SessionState.instance.saveValuesToPreferences(this, AppConstants.Companion.PREFERENCES.EMAIL.toString(), responseBody.email!!)
                SessionState.instance.saveValuesToPreferences(this, AppConstants.Companion.PREFERENCES.DISPLAY_NAME.toString(), responseBody.name!!)
                SessionState.instance.saveValuesToPreferences(this, AppConstants.Companion.PREFERENCES.USER_ID.toString(), responseBody.userId!!)

                fetchLanguagePackDetails()
            } else {
                Utility.showSnackBar(login_screen_parent_layout, LanguagePack.getString("Username or email is already occupied"), this)
            }
        } else {
            Utility.showSnackBar(login_screen_parent_layout, LanguagePack.getString(getString(R.string.some_wrong)), this)
        }
    }

    private fun fetchLanguagePackDetails() {
        val response = getJsonDataFromAsset(this)
        if (response != null) {
            LanguagePack.instance.saveLanguageData(this@LoginActivity, response.toString())
            LanguagePack.instance.setLanguageData(response.toString())
        }
        SessionState.instance.isLogin = true
        SessionState.instance.saveBooleanToPreferences(this, AppConstants.Companion.PREFERENCES.LOGIN_STATUS.toString(), true)
        moveToDashboard()
    }

    private fun moveToDashboard() {
        sendTokenToServer()
        Utility.removeProgressBar(register_user_sliding_progress_indicator)
        Utility.showSnackBar(login_screen_parent_layout, LanguagePack.getString("You have successfully logged in"), this)
        startActivity(DashboardActivity::class.java, true)
        finish()
    }
    private fun setTermsAndCondition() {
        val span = Spannable.Factory.getInstance().newSpannable(LanguagePack.getString(getString(R.string.privacy_policy)))
        var minLength = 31
        var maxLength = 49
        if (!span.isNullOrEmpty() && span.length >= 54) {
            span.setSpan(object : ClickableSpan() {
                override fun onClick(v: View) {
                    loadTermsAndConditionWebView(R.string.terms_condition, if (SessionState.instance.termsConditionUrl != null) SessionState.instance.termsConditionUrl else "")
                }
            }, minLength, maxLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            val cs = object : ClickableSpan() {
                override fun onClick(v: View) {
                    loadTermsAndConditionWebView(R.string.privacy_text, if (SessionState.instance.privacyPolicyUrl != null) SessionState.instance.privacyPolicyUrl else "")
                }
            }

            span.setSpan(cs, 54, span.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            span.setSpan(ForegroundColorSpan(resources.getColor(R.color.denied_red)), minLength, maxLength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            span.setSpan(ForegroundColorSpan(resources.getColor(R.color.denied_red)), 54, span.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            login_terms_condition.setText(span)
            login_terms_condition.setMovementMethod(LinkMovementMethod.getInstance())
        }
    }

    private fun loadTermsAndConditionWebView(titleId: Int, url: String) {
        val bundle = Bundle()
        bundle.putString(AppConstants.TERMS_CONDITION_TITLE, LanguagePack.getString(getString(titleId)))
        bundle.putString(AppConstants.TERMS_CONDITION_URL, url)
        startActivity(TermsAndConditionWebView ::class.java, false, bundle)
    }

    private fun googleSignIn() {
        val signInIntent = mGoogleSignInClient.signInIntent;
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private fun googleSignOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this) {
                    fun onComplete(task : Task<Void>) {
                        // [START_EXCLUDE]
                        // [END_EXCLUDE]
                    }
                }
    }

    private fun handleGoogleSignInResult(completedTask: Task<GoogleSignInAccount> ) {
        if (!this.isFinishing) {
            try {
                val account = completedTask.getResult(ApiException::class.java)
                val email = account?.email
                val userData = UserRegistrationData()
                userData.email = email ?: ""
                userData.username = if (email != null && email.isNotEmpty()) email.split("@")[0] + nextInt(10000, 1000000).toString() else UUID.randomUUID().toString()
                userData.name = account?.displayName
                userData.password = UUID.randomUUID().toString()
                userData.fbLogin = "1"
                if (userData.email!!.isNotEmpty()) {
                    register_user_sliding_progress_indicator.visibility = View.VISIBLE
                    googleSignOut()
                    registerUser(userData)
                }
            } catch (e: ApiException) {
                val ee = e.message
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun printHashKey() {
        try {
            val info = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val hashKey = String(Base64.encode(md.digest(), 0))
                Log.i("Facebook Hash Key",  hashKey)
            }
        } catch (e: NoSuchAlgorithmException) {
            // Log.e(FragmentActivity.TAG, "printHashKey()", e)
        } catch (e: Exception) {
            //Log.e(FragmentActivity.TAG, "printHashKey()", e)
        }

    }
}
